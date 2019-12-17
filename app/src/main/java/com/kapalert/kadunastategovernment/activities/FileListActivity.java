package com.kapalert.kadunastategovernment.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterFileList;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.FileListPOJO;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.CustomProgressDialog;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileListActivity extends AppBaseActivity implements AdapterFileList.DownloadClickListener {

    private RecyclerView mList;

    private static final int PERMISSION_REQUEST_CODE = 25;
    private CaseListPOJO.CaseList mCase;
    private String mSelectedFileUrl;

    private ProgressDialog downlaodProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.showBackButton(mContext, toolbar);

        initView();
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                downloadFile();
            }
        } else {
            downloadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadFile();
            } else {
                Utils.showToast(mContext, getString(R.string.permission_error));
                finish();
            }
        }
    }

    private void initView() {
        mCase = new Gson().fromJson(db.getString(Constants.DB_SELECTED_FILE_CASE), CaseListPOJO.CaseList.class);

        if (mCase == null) {
            Utils.showToast(mContext, getString(R.string.invalid_intent_data));
            finish();
            return;
        }

        mList = (RecyclerView) findViewById(R.id.file_list);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(manager);

        setupList();
    }

    private void setupList() {

        new ServerRequest<FileListPOJO>(mContext, Constants.getFileListUrl(mContext, mCase.id), true) {
            @Override
            public void onCompletion(Call<FileListPOJO> call, Response<FileListPOJO> response) {

                if (response.body().status) {

                    AdapterFileList adapterFileList = null;
                    if (response.body().caseFile != null) {
                        ArrayList<FileListPOJO.CaseFile> list = new ArrayList<>(response.body().caseFile);
                        adapterFileList = new AdapterFileList(mContext, list, FileListActivity.this);
                        mList.setAdapter(adapterFileList);
                    }

                    if (adapterFileList == null || adapterFileList.getItemCount() == 0) {
                        Utils.showToast(mContext, getString(R.string.no_list_found));
                    }

                } else {
                    Utils.showToast(mContext, response.body().alert);
                }
            }
        };
    }

    @Override
    public void downloadClicked(String url) {
        mSelectedFileUrl = url;
        permissionCheck();
    }

    public void downloadFile() {

        Call<ResponseBody> call = Constants.getFileDownloadUrl(mSelectedFileUrl);
        new ServerRequest<ResponseBody>(mContext, call) {
            @Override
            public void onCompletion(Call<ResponseBody> call, Response<ResponseBody> response) {

            }
        };
    }
}
