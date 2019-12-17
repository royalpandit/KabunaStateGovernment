package com.kapalert.kadunastategovernment.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterCounselAccusedList;
import com.kapalert.kadunastategovernment.adapters.AdapterCounselCommentList;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselAccusedListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselCommentsPOJO;
import com.kapalert.kadunastategovernment.templates.SelectRecommendationJSON;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class StageOneStepFourActivity extends AppBaseActivity {

    private LinearLayout mAccusedContainer, mCommentsContainer;
    private RecyclerView mCommentsList, mAccusedList;
    private Button mSave, mSend, mAddComments;
    private static final int PERMISSION_REQUEST_CODE = 58;
    private static final int PICK_FILE = 15;
    private File mFile = null;
    private TextView mFileName;
    private CaseListPOJO.CaseList caseObj;
    private SelectRecommendationJSON mRecommendationAccusedList;
    private ArrayList<CounselAccusedListPOJO.Accused> mSelectedAccusedList = new ArrayList<>();
    private File destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_one_step_four);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);

        checkPermissions();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                initViews();
            }
        } else {
            initViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViews();
            } else {
                Utils.showToast(mContext, getString(R.string.permission_error));
                finish();
            }
        }
    }

    private void initViews() {
        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);

        mAccusedContainer = (LinearLayout) findViewById(R.id.container_accused);
        mCommentsContainer = (LinearLayout) findViewById(R.id.container_comments);
        mCommentsList = findViewById(R.id.comments_list);
        mAccusedList =  findViewById(R.id.accused_list);
        mSave = (Button) findViewById(R.id.save);
        mSend = (Button) findViewById(R.id.send);
        mAddComments = (Button) findViewById(R.id.add_comment);

        if (caseObj.isRecommendationDone.equalsIgnoreCase("true")) {
            showComments();
        } else {
            showAccused();
        }
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccuseds();
            }
        });
        mAddComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCommentDialog();
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCase(caseObj.id, "Role", Constants.API_FORWARD_TYPE_SEND, "25", 1, 7);
            }
        });

        mSend.setVisibility(View.GONE);
        mAddComments.setVisibility(View.VISIBLE);
    }

    public void sendCase(String caseId, String apiExchangeTypeRole, String apiForwardTypeSend, String userRoleDpp, int stageNum, int stepNum) {

        new ServerRequest<UserInfoJson>(mContext, Constants.getCaseProgressUrl(mContext, caseId, apiExchangeTypeRole, apiForwardTypeSend, userRoleDpp, "" + stageNum, "" + stepNum), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    Utils.showToast(mContext, response.body().message);
                    finish();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void showAddCommentDialog() {
        mFile = null;

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_counsel_add_comment);

        final EditText comment = (EditText) dialog.findViewById(R.id.comment_text);
        final TextView fileName = (TextView) dialog.findViewById(R.id.comment_file_name);
        final ImageView selectFile = (ImageView) dialog.findViewById(R.id.comment_file_select);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGallery(fileName);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentStr = comment.getText().toString();

                mSend.setVisibility(View.VISIBLE);
                if (!commentStr.isEmpty() && mFile != null) {
                    addComment(commentStr, dialog);
                } else if (mFile == null) {
                    fileName.setError(getString(R.string.invalid_file_selected));
                } else if (commentStr.isEmpty()) {
                    comment.setError(getString(R.string.required_field));
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void addComment(String commentStr, final Dialog dialog) {
        new ServerRequest<UserInfoJson>(mContext, Constants.getCreateCounselCommentUrl(mContext, caseObj.id, commentStr, mFile), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    Utils.showMessageDialog(mContext, getString(R.string.success), response.body().message);
                    dialog.dismiss();
                    setupCommentsList();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void setupCommentsList() {
        mAddComments.setVisibility(View.VISIBLE);
        mSend.setText("Forward To Group secretary");
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mCommentsList.setLayoutManager(manager);

        new ServerRequest<CounselCommentsPOJO>(mContext, Constants.getCounselCommentListUrl(mContext, caseObj.id), true) {
            @Override
            public void onCompletion(Call<CounselCommentsPOJO> call, Response<CounselCommentsPOJO> response) {
                if (response.body().status) {

                    if (response.body().comments != null && !response.body().comments.isEmpty()) {
                        mSend.setVisibility(View.VISIBLE);
                        ArrayList<CounselCommentsPOJO.Comment> list = new ArrayList<>(response.body().comments);
                        AdapterCounselCommentList adapterCounselCommentList = new AdapterCounselCommentList(mContext, list);
                        mCommentsList.setAdapter(adapterCounselCommentList);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };

    }

    private void setupAccusedList() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAccusedList.setLayoutManager(manager);

        new ServerRequest<CounselAccusedListPOJO>(mContext, Constants.getCounselAccusedListUrl(mContext, caseObj.id), true) {
            @Override
            public void onCompletion(Call<CounselAccusedListPOJO> call, Response<CounselAccusedListPOJO> response) {
                if (response.body().status) {

                    if (response.body().accuseds != null && !response.body().accuseds.isEmpty()) {
                        ArrayList<CounselAccusedListPOJO.Accused> list = new ArrayList<>(response.body().accuseds);
                        mSelectedAccusedList = list;

                        AdapterCounselAccusedList adapterCounselCommentList = new AdapterCounselAccusedList(mContext, list);
                        mAccusedList.setAdapter(adapterCounselCommentList);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void showAccused() {
        setupAccusedList();
        mAccusedContainer.setVisibility(View.VISIBLE);
        mCommentsContainer.setVisibility(View.GONE);
    }

    private void showComments() {
        setupCommentsList();
        mAccusedContainer.setVisibility(View.GONE);
        mCommentsContainer.setVisibility(View.VISIBLE);
    }

    private void saveAccuseds() {
        if (!mSelectedAccusedList.isEmpty() && generateRecommendJson()) {

            new ServerRequest<UserInfoJson>(mContext, Constants.getAccusedRecommendationUrl(mContext, caseObj.id, new Gson().toJson(mRecommendationAccusedList), "5"), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        showComments();
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        } else {
            Utils.showToast(mContext, getString(R.string.select_recommendation_error));
        }
    }

    private boolean generateRecommendJson() {

        ArrayList<SelectRecommendationJSON.AccusedJson> recommendationAccusedList = new ArrayList<>();

        for (CounselAccusedListPOJO.Accused accused : mSelectedAccusedList) {
            if (accused.selectedReccomendation != 0) {
                recommendationAccusedList.add(new SelectRecommendationJSON.AccusedJson(accused.id, accused.selectedReccomendation));
            }
        }
        if (recommendationAccusedList.isEmpty()) {
            return false;
        } else {
            mRecommendationAccusedList = new SelectRecommendationJSON(recommendationAccusedList);
            return true;
        }
    }

    public void gotoGallery(TextView fileName) {
        mFileName = fileName;
        Utils.showDialog(mContext, null, getString(R.string.select_from), getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICK_FILE);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                String name = dateFormat.format(Calendar.getInstance().getTime());
                destination = new File(Environment.getExternalStorageDirectory(), name + ".jpeg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mContext, Constants.FILE_PROVIDER, destination));
                startActivityForResult(intent, Constants.REQUEST_CAMERA_CAPTURE);
            }
        }, getString(R.string.camera), getString(R.string.cancel), null, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FILE) {
                Uri chosenImageUri = data.getData();
                String realPath = "";
                try {
                    realPath = Utils.getRealPathFromURI(mContext, chosenImageUri);
                    mFile = new File(realPath);
                    mFileName.setText(mFile.getName());
                } catch (Exception e) {
                    Utils.showToast(mContext, getString(R.string.failed_to_fetch));
                }
                Log.e("path", realPath);
            } else if (requestCode == Constants.REQUEST_CAMERA_CAPTURE) {
                FileInputStream in;
                try {
                    in = new FileInputStream(destination);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                String imagePath = destination.getAbsolutePath();

                mFile = new File(imagePath);
            }
            try {
                mFileName.setText(mFile.getName());
            } catch (Exception e) {
                Utils.showToast(mContext, getString(R.string.failed_to_fetch));
            }
        } else {
            Utils.showToast(mContext, getString(R.string.failed_to_fetch));
        }
    }


}
