package com.kapalert.kadunastategovernment.fragments;


import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterChatUserList;
import com.kapalert.kadunastategovernment.adapters.AdapterInboxList;
import com.kapalert.kadunastategovernment.adapters.AdapterUserList;
import com.kapalert.kadunastategovernment.templates.InboxListPOJO;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**

 */
public class InboxFragment extends BaseFragment {

    private RecyclerView mList;
    private Button mCreateChat;
    private ArrayList<UserListPOJO.UserDetail> mUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.chat_inbox);
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mList = (RecyclerView) view.findViewById(R.id.inbox_list);
        mCreateChat = (Button) view.findViewById(R.id.create_chat);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupList();
    }

    private void showCreateChatDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_select_chat_user);

        final RecyclerView userList = (RecyclerView) dialog.findViewById(R.id.users_list);

        setupUsersList(userList, dialog);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setupUsersList(RecyclerView list, Dialog dialog) {

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);

        AdapterChatUserList adapterUserList = new AdapterChatUserList(mContext, mUserList, dialog);
        list.setAdapter(adapterUserList);
    }

    private void fetchAllUsersList(final ArrayList<InboxListPOJO.ActiveChat> finalList) {
        new ServerRequest<UserListPOJO>(mContext, Constants.getUserListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<UserListPOJO> call, Response<UserListPOJO> response) {
                if (response.body().status) {
                    if (response.body().usersList != null && !response.body().usersList.isEmpty()) {
                        ArrayList<UserListPOJO.UserDetail> list = new ArrayList<>(response.body().usersList);
                        for (int i = 0; i < response.body().usersList.size(); i++) {
                            if (list.get(i).ID.equalsIgnoreCase(Constants.getUser(mContext).id)) {
                                list.remove(i);
                                break;
                            }
                        }
                        ArrayList<UserListPOJO.UserDetail> finalUserList = new ArrayList<>();
                        for (UserListPOJO.UserDetail userDetail : list) {
                            boolean userContainer = false;
                            for (InboxListPOJO.ActiveChat activeChat : finalList) {
                                if (activeChat.toUserID.equalsIgnoreCase(userDetail.ID)) {
                                    userContainer = true;
                                }
                            }
                            if (!userContainer)
                                finalUserList.add(userDetail);
                        }
                        mUserList = new ArrayList<>(finalUserList);
                    } else {
                        mUserList = new ArrayList<>();
                    }
                    showCreateChatDialog();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void setupList() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(manager);

        new ServerRequest<InboxListPOJO>(mContext, Constants.getInboxListUrl(mContext), true) {
            @Override
            public void onCompletion(Call<InboxListPOJO> call, Response<InboxListPOJO> response) {
                final ArrayList<InboxListPOJO.ActiveChat> finalList = new ArrayList<>();
                if (response.body().status) {
                    ArrayList<InboxListPOJO.ActiveChat> list = new ArrayList<>();
                    if (response.body().activeChat != null && !response.body().activeChat.isEmpty()) {
                        list = new ArrayList<>(response.body().activeChat);
                        AdapterInboxList adapterInboxList = new AdapterInboxList(mContext, list);
                        mList.setAdapter(adapterInboxList);
                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                    finalList.addAll(list);
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
                mCreateChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchAllUsersList(finalList);
                    }
                });
            }
        };

    }

}
