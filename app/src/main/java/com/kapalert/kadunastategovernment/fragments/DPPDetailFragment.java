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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CaseDetailActivity;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedRecommendationList;
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
public class DPPDetailFragment extends BaseFragment {

    private CaseDetailPOJO.Dpp mDPPData;

    private ImageView mDownloadFile;
    private Button mViewAccusedRecommendation;
    private TextView mCaseReceivedBdfl, mCaseSentBdtc, mSelectedCounsel, mCaseReceivedBdfl2, mCaseSentBdtl, mComment, mFileSent;
    private int PERMISSION_REQUEST_CODE = 37;
    private int stageNum, stepNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        stageNum = ((CaseDetailActivity) mContext).caseObj.stage;
        stepNum = ((CaseDetailActivity) mContext).caseObj.step;

        mDPPData = ((CaseDetailActivity) getActivity()).mDataDPP;

        View view = inflater.inflate(R.layout.fragment_dpp_detail, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mViewAccusedRecommendation = (Button) view.findViewById(R.id.view_accused_recommendation);
        mCaseReceivedBdfl = (TextView) view.findViewById(R.id.case_received_bdfl);
        mCaseReceivedBdfl2 = (TextView) view.findViewById(R.id.case_received_bdfl2);
        mCaseSentBdtc = (TextView) view.findViewById(R.id.case_sent_bdtc);
        mSelectedCounsel = (TextView) view.findViewById(R.id.selected_counsel);
        mFileSent = (TextView) view.findViewById(R.id.file_sent);
        mCaseSentBdtl = (TextView) view.findViewById(R.id.case_received_bdtl);
        mComment = (TextView) view.findViewById(R.id.comment);
        mDownloadFile = (ImageView) view.findViewById(R.id.download_file);

        fillViews();
        mDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });
        mViewAccusedRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAccusedRecommendationDialog();
            }
        });

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

        AdapterAccusedRecommendationList adapterUserList = new AdapterAccusedRecommendationList(mContext, new ArrayList<CaseDetailPOJO.AccusedRecommendations>(mDPPData.stage1.step9.accusedRecommendations), dialog);
        list.setAdapter(adapterUserList);
    }


    private void fillViews() {
        mCaseReceivedBdfl.setText(String.format(getString(R.string.case_received_bdfl_pattern), Constants.getDateStr(mDPPData.stage1.step2.caseRecievedFromLitigation)));
        mCaseSentBdtc.setText(String.format(getString(R.string.case_sent_bdtc_pattern), Constants.getDateStr(mDPPData.stage1.step2.caseSentByDppToCounsel)));
        mSelectedCounsel.setText(String.format(getString(R.string.selected_counsel_pattern), mDPPData.stage1.step2.counselName));

        mCaseReceivedBdfl2.setText(String.format(getString(R.string.case_received_bdfl_pattern), Constants.getDateStr(mDPPData.stage1.step5.caseRecievedByDppFromLitigation)));
        mCaseSentBdtl.setText(String.format(getString(R.string.case_sent_bdtl_pattern), Constants.getDateStr(mDPPData.stage1.step5.caseSentByDppToLitigation)));

        if (mDPPData.stage1.step5.filesAndCommentsByDpp == null){
            mComment.setText(String.format(getString(R.string.comment_pattern), ""));
            mFileSent.setText(String.format(getString(R.string.file_sent_pattern), ""));
        }
        try {
            mComment.setText(String.format(getString(R.string.comment_pattern), Constants.getDateStr(mDPPData.stage1.step5.filesAndCommentsByDpp.comments)));
            mFileSent.setText(String.format(getString(R.string.file_sent_pattern), mDPPData.stage1.step5.filesAndCommentsByDpp.file_name));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void downloadFile() {
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
        if (mDPPData.stage1.step5.filesAndCommentsByDpp != null ) {
            new ServerRequest<ResponseBody>(mContext, Constants.getFileDownloadUrl(Constants.getCaseFileUrlStr(((CaseDetailActivity) mContext).caseObj.id, mDPPData.stage1.step5.filesAndCommentsByDpp.file_name))) {
                @Override
                public void onCompletion(Call<ResponseBody> call, Response<ResponseBody> response) {

                }
            };
        }
    }
}
