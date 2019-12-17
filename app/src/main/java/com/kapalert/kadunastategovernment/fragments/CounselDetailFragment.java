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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CaseDetailActivity;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedRecommendationList;
import com.kapalert.kadunastategovernment.adapters.AdapterFilesUploadedList;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;
import com.kapalert.kadunastategovernment.utils.BaseFragment;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 */
public class CounselDetailFragment extends BaseFragment implements AdapterFilesUploadedList.OnDownloadClickListener {

    private CaseDetailPOJO.Counsel mCounselData;
    private CaseDetailPOJO.CaseNotes mCaseNotes;

    private Button mViewComments, mAccusedRecommendation;
    private TextView mCaseSentBdtc, mCaseSentBctl, mCaseReceivedBcfl, mCaseClosed,notes_data;
    private AppCompatActivity mActivity;
    private String mSelectedFileUrl = "";
    private int PERMISSION_REQUEST_CODE = 32;
    private int stageNum, stepNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        stageNum = ((CaseDetailActivity) mContext).caseObj.stage;
        stepNum = ((CaseDetailActivity) mContext).caseObj.step;

        mActivity = (AppCompatActivity) mContext;
        mCounselData = ((CaseDetailActivity) getActivity()).mDataCounsel;
        ////mCaseNotes = ((CaseDetailActivity) getActivity()).mCaseNotes;

        View view = inflater.inflate(R.layout.fragment_counsel_detail, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mCaseSentBdtc = (TextView) view.findViewById(R.id.case_sent_bdtc);
        mCaseReceivedBcfl = (TextView) view.findViewById(R.id.case_received_bcfl);
        mCaseSentBctl = (TextView) view.findViewById(R.id.case_sent_bctl);
        mCaseClosed = (TextView) view.findViewById(R.id.case_closed_date);
        mViewComments = (Button) view.findViewById(R.id.view_comments);
        mAccusedRecommendation = (Button) view.findViewById(R.id.view_accused_recommendation);

        mAccusedRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAccusedRecommendationDialog();
            }
        });
        mViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFilesDialog();
            }
        });
        fillViews();
    }

    private void viewAccusedRecommendationDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_accused_recommendation_list);

        final RecyclerView userList = (RecyclerView) dialog.findViewById(R.id.accused_list);

        setupAccusedRecommendationList(userList, dialog);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setupAccusedRecommendationList(RecyclerView list, Dialog dialog) {

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);

        AdapterAccusedRecommendationList adapterUserList = new AdapterAccusedRecommendationList(mContext, new ArrayList<CaseDetailPOJO.AccusedRecommendations>(mCounselData.stage1.step3.accusedRecommendations), dialog);
        list.setAdapter(adapterUserList);
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

        AdapterFilesUploadedList adapterUserList = new AdapterFilesUploadedList(mContext, new ArrayList<CaseDetailPOJO.FilesAndComments>(mCounselData.stage1.step3.filesAndCommentsByCounsel), dialog, this);
        list.setAdapter(adapterUserList);
    }


    private void fillViews() {
        mCaseSentBdtc.setText(String.format(getString(R.string.case_sent_bdtc_pattern), Constants.getDateStr(mCounselData.stage1.step3.caseSentByDpp)));
        mCaseSentBctl.setText(String.format(getString(R.string.case_sent_bctl_pattern), Constants.getDateStr(mCounselData.stage1.step3.caseSentByCounselToLitigation)));

        mCaseReceivedBcfl.setText(String.format(getString(R.string.case_received_bcfl_pattern), Constants.getDateStr(mCounselData.stage3.step1.caseRecievedByCounselFromLitigation)));
        mCaseClosed.setText(String.format(getString(R.string.case_closed_pattern), Constants.getDateStr(mCounselData.stage3.step1.caseClosedOn)));

       ///// notes_data.setText(mCaseNotes.getCaseNote());



        if (stageNum == 1) {
            switch (stepNum) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
            }
        } else if (stageNum == 2) {
            if (stepNum == 10) {
            } else if (stepNum == 11) {
            }
        } else if (stageNum == 3) {
            if (stepNum == 12) {
            } else if (stepNum == 13) {
            } else if (stepNum == 14) {
            } else {
            }
        } else {
        }
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
