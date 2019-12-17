package com.kapalert.kadunastategovernment.activities;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterChatList;
import com.kapalert.kadunastategovernment.adapters.AdapterInboxList;
import com.kapalert.kadunastategovernment.templates.ChatListPOJO;
import com.kapalert.kadunastategovernment.templates.InboxListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class ChatListActivity extends AppBaseActivity {

    private RecyclerView mList;
    private ContentLoadingProgressBar mMessageProgress;
    private EditText mMessage;
    private Button mSend;
    LinearLayoutManager mListLayoutManager;
    AdapterChatList mAdapterChatList;
    ArrayList<ChatListPOJO.ChatMessage> mChatArr;

    private InboxListPOJO.ActiveChat chatObj;
    private UserListPOJO.UserDetail newChatUser;

    private boolean activityDestroyed = false;
    private boolean newChat = false;

    private Handler mHandler;
    private Runnable mRunnable;
    private int chatCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);
        chatObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CHAT), InboxListPOJO.ActiveChat.class);
        newChatUser = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CHAT_NEW), UserListPOJO.UserDetail.class);

        if (chatObj == null && newChatUser == null) {
            Utils.showToast(mContext, getString(R.string.something_went_wrong));
            finish();
            return;
        } else if (newChatUser != null) {
            newChat = true;
        }
        initViews();
    }

    private void initViews() {
        mList = (RecyclerView) findViewById(R.id.private_conversation_list);
        mMessageProgress = (ContentLoadingProgressBar) findViewById(R.id.progress);
        mMessage = (EditText) findViewById(R.id.message);
        mSend = (Button) findViewById(R.id.send);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageAPI();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityDestroyed = false;
        setupList(true, false, "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityDestroyed = true;
        stopChatThread();
    }

    private void stopChatThread() {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void startChatThread(boolean showProgress) {

        if (mHandler == null) {
            mHandler = new Handler();
        }
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    setupList(false, false, "");
                }
            };
        }
        if (!activityDestroyed)
            mHandler.postDelayed(mRunnable, 500);
    }

    private void sendMessageAPI() {
        if (!mMessage.getText().toString().isEmpty()) {
            Call<ChatListPOJO> call;
            UserInfoJson.UserData loggedUser = Constants.getUser(mContext);
            if (newChat) {
                call = Constants.getChatMessageListUrl(mContext, "", mMessage.getText().toString(), "" + newChat, newChatUser.username, loggedUser.username, loggedUser.id);
            } else {
                call = Constants.getChatMessageListUrl(mContext, chatObj.ID, mMessage.getText().toString(), "" + newChat, "", "", "");
            }
            mMessage.setText("");

            mMessageProgress.setVisibility(View.VISIBLE);
            mSend.setVisibility(View.GONE);
            new ServerRequest<ChatListPOJO>(mContext, call, false) {
                @Override
                public void onCompletion(Call<ChatListPOJO> call, Response<ChatListPOJO> response) {
                    mMessageProgress.setVisibility(View.GONE);
                    mSend.setVisibility(View.VISIBLE);
                    if (response.body().status) {
                        if (newChat) {
                            fetchChatObj(response.body().chat.get(0).chatID);
                        } else {
                            setupList(false, true, response.body().chat.get(0).chatID);
                        }
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        } else {
            Utils.showToast(mContext, getString(R.string.please_enter_message_error));
        }
    }

    private void fetchChatObj(final String chatID) {
        new ServerRequest<InboxListPOJO>(mContext, Constants.getInboxListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<InboxListPOJO> call, Response<InboxListPOJO> response) {
                final ArrayList<InboxListPOJO.ActiveChat> finalList = new ArrayList<>();
                if (response.body().status) {
                    ArrayList<InboxListPOJO.ActiveChat> list = new ArrayList<>();
                    if (response.body().activeChat != null && !response.body().activeChat.isEmpty()) {
                        list = new ArrayList<>(response.body().activeChat);
                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                    finalList.addAll(list);
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
                for (InboxListPOJO.ActiveChat activeChat : finalList) {
                    if (activeChat.ID.equalsIgnoreCase(chatID)) {
                        chatObj = activeChat;
                        newChat = false;
                        setupList(false, true, "");
                        break;
                    }
                }
            }
        };
    }

    private void setupList(final boolean showProgress, final boolean messageSent, String chatID) {

        if (newChat && !messageSent) {
            return;
        } else if (!newChat) {
            chatID = chatObj.ID;
        }
        if (mListLayoutManager == null) {
            mListLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mList.setLayoutManager(mListLayoutManager);
        }

        new ServerRequest<ChatListPOJO>(mContext, Constants.getChatListUrl(mContext, chatID), showProgress) {
            @Override
            public void onCompletion(Call<ChatListPOJO> call, Response<ChatListPOJO> response) {

                if (response.body().status) {
                    if (response.body().chat != null && !response.body().chat.isEmpty()) {
                        if (mChatArr == null)
                            mChatArr = new ArrayList<>(response.body().chat);
                        else {
                            mChatArr.clear();
                            mChatArr.addAll(response.body().chat);
                        }
                        if (mAdapterChatList == null) {
                            mAdapterChatList = new AdapterChatList(mContext, mChatArr);
                            mList.setAdapter(mAdapterChatList);
                        } else {
                            mAdapterChatList.notifyDataSetChanged();
                        }
                        int itemVisiblePosition = 0;
                        if (mListLayoutManager != null) {
                            itemVisiblePosition = mListLayoutManager.findLastCompletelyVisibleItemPosition();
                        }
                        Log.e("Last Visible Item > ", "" + itemVisiblePosition);
                        Log.e("List Size > ", "" + (mAdapterChatList.getItemCount() - 1));
                        boolean chatCountChanged = chatCount < mAdapterChatList.getItemCount();
                        chatCount = mAdapterChatList.getItemCount();
                        if (chatCountChanged)
                            mList.scrollToPosition(mAdapterChatList.getItemCount() - 1);
                        startChatThread(showProgress);

                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

}
