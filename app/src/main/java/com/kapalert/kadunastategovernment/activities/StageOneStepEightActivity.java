package com.kapalert.kadunastategovernment.activities;

import android.Manifest;
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
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselAccusedListPOJO;
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

public class StageOneStepEightActivity extends AppBaseActivity {

    private LinearLayout mAccusedContainer, mCommentsContainer;
    private EditText mComment;
    private TextView mFileName;
    private ImageView mFileSelect;
    private Button mSend, mSave;
    private RecyclerView mAccusedList;
    private CaseListPOJO.CaseList caseObj;
    private ArrayList<CounselAccusedListPOJO.Accused> mSelectedAccusedList;
    private SelectRecommendationJSON mRecommendationAccusedList;
    private static final int PERMISSION_REQUEST_CODE = 48;
    private int PICK_FILE = 69;
    private File mFile;
    private File destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_one_step_eight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);

        checkPermissions();
    }

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

    private void initViews() {

        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);

        mAccusedContainer = (LinearLayout) findViewById(R.id.accused_container);
        mCommentsContainer = (LinearLayout) findViewById(R.id.comment_container);
        mAccusedList = (RecyclerView) findViewById(R.id.accused_list);
        mComment = (EditText) findViewById(R.id.comment_text);
        mFileName = (TextView) findViewById(R.id.comment_file_name);
        mFileSelect = (ImageView) findViewById(R.id.comment_file_select);
        mSend = (Button) findViewById(R.id.send);
        mSave = (Button) findViewById(R.id.save);

        if (caseObj.isRecommendationDone.equalsIgnoreCase("true")) {
            showComments();
        } else {
            showAccused();
        }

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccused();
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        mFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGallery(mFileName);
            }
        });
    }

    private void saveAccused() {
        if (!mSelectedAccusedList.isEmpty() && generateRecommendJson()) {

            new ServerRequest<UserInfoJson>(mContext, Constants.getAccusedRecommendationUrl(mContext, caseObj.id, new Gson().toJson(mRecommendationAccusedList), "9"), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        mAccusedContainer.setVisibility(View.GONE);
                        mCommentsContainer.setVisibility(View.VISIBLE);
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        } else {
            Utils.showToast(mContext, getString(R.string.select_recommendation_error));
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

    private void addComment() {
        String commentStr = mComment.getText().toString();

        if (!commentStr.isEmpty() && mFile != null) {
            addCommentAPI(commentStr);
        } else if (mFile == null) {
            mFileName.setError(getString(R.string.invalid_file_selected));
        } else if (commentStr.isEmpty()) {
            mComment.setError(getString(R.string.required_field));
        }
    }

    private void addCommentAPI(String commentStr) {
        new ServerRequest<UserInfoJson>(mContext, Constants.getCreateCounselCommentUrl(mContext, caseObj.id, commentStr, mFile), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    Utils.showMessageDialog(mContext, getString(R.string.success), response.body().message);
                    sendCase(caseObj.id, Constants.API_EXCHANGE_TYPE_USER, Constants.API_FORWARD_TYPE_SEND, caseObj.litigationID, 1, 9);
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    public void sendCase(String id, String apiExchangeTypeRole, String apiForwardTypeSend, String userRoleDpp, int stageNum, int stepNum) {
        Button senddata=(Button) findViewById(R.id.senddata);
        senddata.setVisibility(View.VISIBLE);
        new ServerRequest<UserInfoJson>(mContext, Constants.getCaseProgress(mContext, id, apiExchangeTypeRole, apiForwardTypeSend, userRoleDpp, "" + stageNum, "" + stepNum), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                Utils.showToast(mContext, response.body().message);
                finish();
            }
        };
    }

    private void showAccused() {
        setupAccusedList();
        mAccusedContainer.setVisibility(View.VISIBLE);
        mCommentsContainer.setVisibility(View.GONE);
    }

    private void showComments() {
        mAccusedContainer.setVisibility(View.GONE);
        mCommentsContainer.setVisibility(View.VISIBLE);
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

    private void setupAccusedList() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAccusedList.setLayoutManager(manager);

        new ServerRequest<CounselAccusedListPOJO>(mContext, Constants.getCounselAccusedListUrl(mContext, caseObj.id), true) {
            @Override
            public void onCompletion(Call<CounselAccusedListPOJO> call, Response<CounselAccusedListPOJO> response) {
                if (response.body().status) {

                    if (response.body().accuseds != null && !response.body().accuseds.isEmpty()) {
                        ArrayList<CounselAccusedListPOJO.Accused> list = new ArrayList<>(response.body().accuseds);
                        mSelectedAccusedList = new ArrayList<>();
                        for (CounselAccusedListPOJO.Accused accused : list) {
                            if (accused.stepNo.equalsIgnoreCase("5")) {
                                mSelectedAccusedList.add(accused);
                            }
                        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViews();
            } else {
                Utils.showToast(mContext, getString(R.string.permission_error));
                finish();
            }
        }
    }
}
