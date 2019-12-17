package com.kapalert.kadunastategovernment.activities;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterDetailPager;
import com.kapalert.kadunastategovernment.adapters.CaseNote_Adapter;
import com.kapalert.kadunastategovernment.fragments.CounselDetailFragment;
import com.kapalert.kadunastategovernment.fragments.DPPDetailFragment;
import com.kapalert.kadunastategovernment.fragments.LitigationDetailFragment;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class CaseDetailActivity extends AppBaseActivity  {

    private TabLayout mTabs;
    private ViewPager mPager;
    public CaseListPOJO.CaseList caseObj;
    public CaseDetailPOJO.Dpp mDataDPP;
    public CaseDetailPOJO notes;
    public CaseDetailPOJO.Litigation mDataLitigation;
    public CaseDetailPOJO.Counsel mDataCounsel;
    RelativeLayout layout_case_ntoes;


    public List<CaseDetailPOJO.CaseNotes> modelList;

    RecyclerView recycle_caseNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        layout_case_ntoes  =  findViewById(R.id.layout_case_ntoes);

        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);

        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);
        if (caseObj != null) {
            fetchCaseData();
        } else {
            Utils.showToast(mContext, getString(R.string.invalid_case_selected));
            finish();
        }
    }

    private void fetchCaseData() {
        modelList = new ArrayList<>();
        new ServerRequest<CaseDetailPOJO>(mContext, Constants.getCaseDetailUrl(mContext, caseObj.id), true) {
            @Override
            public void onCompletion(Call<CaseDetailPOJO> call, Response<CaseDetailPOJO> response) {
                if (response.body().status) {

                    mDataDPP = response.body().dpp;
                    mDataLitigation = response.body().litigation;
                    mDataCounsel = response.body().counsel;
                    modelList = response.body().caseNotes;

                    System.out.println("======modelList.size()============="+modelList.size());


                    initViews();

                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void initViews() {

        mTabs =findViewById(R.id.tabs);
        mPager =  findViewById(R.id.tabs_pager);
        recycle_caseNotes =  findViewById(R.id.recycle_caseNotes);
        mTabs.setupWithViewPager(mPager);

        ArrayList<AdapterDetailPager.DetailPagerPojo> list = new ArrayList<>();
        list.add(new AdapterDetailPager.DetailPagerPojo(new LitigationDetailFragment(), getString(R.string.litigation)));
        list.add(new AdapterDetailPager.DetailPagerPojo(new DPPDetailFragment(), getString(R.string.dpp)));
        list.add(new AdapterDetailPager.DetailPagerPojo(new CounselDetailFragment(), getString(R.string.counsel)));
     ////   list.add(new AdapterDetailPager.DetailPagerPojo(new CaseNotesFragment(), getString(R.string.case_notes)));



        AdapterDetailPager adapterDetailPager = new AdapterDetailPager(getSupportFragmentManager(), mContext, list);
        mPager.setAdapter(adapterDetailPager);
        changeTabFont(mTabs, Constants.FONT_FILE);


        if (modelList.size() != 0)
        {
            layout_case_ntoes.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycle_caseNotes.setLayoutManager(linearLayoutManager);
        CaseNote_Adapter caseNote_adapter = new CaseNote_Adapter(this,modelList);
        recycle_caseNotes.setAdapter(caseNote_adapter);




    }

    private void changeTabFont(TabLayout tabLayout, String fontPath) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    CalligraphyUtils.applyFontToTextView(mContext, (TextView) tabViewChild, fontPath);
                }
            }
        }
    }


}
