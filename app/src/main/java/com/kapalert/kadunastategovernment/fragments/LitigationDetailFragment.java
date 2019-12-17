package com.kapalert.kadunastategovernment.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CaseDetailActivity;
import com.kapalert.kadunastategovernment.activities.StageTwoStepOneActivity;
import com.kapalert.kadunastategovernment.adapters.AdapterChatUserList;
import com.kapalert.kadunastategovernment.adapters.AdapterFilesUploadedList;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 */
public class LitigationDetailFragment extends BaseFragment implements AdapterFilesUploadedList.OnDownloadClickListener {

    private CaseDetailPOJO.Litigation mLitigationData;
    private AppCompatActivity mActivity;

    private LinearLayout mStep1, mStep4, mStep6;
    private CardView mStage2;
    private TextView mCaseCreated, mCaseSentBltd, mCaseAcceptedDpp, mCaseReceivedBlfc, mCaseSentBltd2, mCaseReceivedBlfc2, mCaseSentBltc;
    private Button mViewFilesUploaded;
    private String mSelectedFileUrl = "";
    private final int PERMISSION_REQUEST_CODE = 15;

    private int stageNum;
    private int stepNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = (AppCompatActivity) mContext;
        stageNum = ((CaseDetailActivity) mContext).caseObj.stage;
        stepNum = ((CaseDetailActivity) mContext).caseObj.step;

        mLitigationData = ((CaseDetailActivity) getActivity()).mDataLitigation;

        View view = inflater.inflate(R.layout.fragment_litigation_detail, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mStep1 = (LinearLayout) view.findViewById(R.id.step_1_container);
        mStep4 = (LinearLayout) view.findViewById(R.id.step_4_container);
        mStep6 = (LinearLayout) view.findViewById(R.id.step_6_container);
        mStage2 = (CardView) view.findViewById(R.id.stage_2_container);

        mCaseCreated = (TextView) view.findViewById(R.id.case_created);
        mCaseSentBltd = (TextView) view.findViewById(R.id.case_sent_bltd);
        mCaseAcceptedDpp = (TextView) view.findViewById(R.id.case_accepted_dpp);
        mCaseReceivedBlfc = (TextView) view.findViewById(R.id.case_received_blfc);
        mCaseSentBltd2 = (TextView) view.findViewById(R.id.case_sent_bltd2);
        mCaseReceivedBlfc2 = (TextView) view.findViewById(R.id.case_received_blfc2);
        mCaseSentBltc = (TextView) view.findViewById(R.id.case_sent_bltc);

        mViewFilesUploaded = (Button) view.findViewById(R.id.files_uploaded);

        mViewFilesUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFilesDialog();
            }
        });
        fillViews();
    }

    private void fillViews() {
        mCaseCreated.setText(String.format(getString(R.string.case_created_pattern), Constants.getDateStr(mLitigationData.stage1.step1.caseCreatedbyLitigation)));
        mCaseSentBltd.setText(String.format(getString(R.string.case_sent_bltd_pattern), Constants.getDateStr(mLitigationData.stage1.step1.caseSentByDppToLitigation)));
        mCaseAcceptedDpp.setText(String.format(getString(R.string.case_accepted_dpp_pattern), Constants.getDateStr(mLitigationData.stage1.step1.caseAcceptByDpp)));

        mCaseReceivedBlfc.setText(String.format(getString(R.string.case_received_blfc_pattern), Constants.getDateStr(mLitigationData.stage1.step4.caseRecievedByLitigationFromCounsel)));
        mCaseSentBltd2.setText(String.format(getString(R.string.case_sent_bltd_pattern), Constants.getDateStr(mLitigationData.stage1.step4.caseSentByLitigationToDpp)));

        mCaseReceivedBlfc2.setText(String.format(getString(R.string.case_received_blfc_pattern), Constants.getDateStr(mLitigationData.stage2.step1.caseRecievedByLitigationFromCounsel)));
        mCaseSentBltc.setText(String.format(getString(R.string.case_sent_bltc_pattern), Constants.getDateStr(mLitigationData.stage2.step1.caseSentByLitigationToCounsel)));

        /*if (stageNum == 1) {
            switch (stepNum) {
                case 0:
                    Constants.hideViews(mCaseSentBltd, mCaseAcceptedDpp, mStep4, mStage2);
                    break;
                case 1:
                    Constants.hideViews(mCaseAcceptedDpp, mStep4, mStage2);
                    break;
                case 2:
                    Constants.hideViews(mStep4, mStage2);
                    break;
                case 3:
                    Constants.hideViews(mStep4, mStage2);
                    break;
                case 4:
                    Constants.hideViews(mStep4, mStage2);
                    break;
                case 5:
                    Constants.hideViews(mStep4, mStage2);
                    break;
                case 6:
                    Constants.hideViews(mCaseSentBltd, mStage2);
                    break;
                case 7:
                    Constants.hideViews(mStage2);
                    break;
                case 8:
                    Constants.hideViews(mStage2);
                    break;
                case 9:
                    Constants.hideViews(mStage2);
                    break;
            }
        } else if (stageNum == 2) {
            if (stepNum == 10) {
                Constants.hideViews(mCaseSentBltd2);
            }
        }*/

    }

    private void viewFilesDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_files_uploaded_list);

        final RecyclerView userList = (RecyclerView) dialog.findViewById(R.id.files_list);

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

        AdapterFilesUploadedList adapterUserList = new AdapterFilesUploadedList(mContext, new ArrayList<CaseDetailPOJO.FilesAndComments>(mLitigationData.stage2.step10.filesAndCommentsLitigation), dialog, this);
        list.setAdapter(adapterUserList);
    }

    @Override
    public void downloadFile(String url) {
        mSelectedFileUrl = url;
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                startFileDownload();
            }
        } else {
            startFileDownload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFileDownload();
            } else {
                Utils.showToast(mContext, getString(R.string.permission_error));
            }
        }
    }

    private void startFileDownload() {
        new ServerRequest<ResponseBody>(mContext, Constants.getFileDownloadUrl(Constants.getCaseFileUrlStr(((CaseDetailActivity) mContext).caseObj.id, mSelectedFileUrl))) {
            @Override
            public void onCompletion(Call<ResponseBody> call, Response<ResponseBody> response) {

            }
        };
    }
}
