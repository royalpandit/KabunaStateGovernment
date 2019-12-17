package com.kapalert.kadunastategovernment.adapters;

import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kapalert.kadunastategovernment.fragments.LitigationDetailFragment;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win10 on 8/22/2017.
 */

public class AdapterDetailPager extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<DetailPagerPojo> mList;

    public AdapterDetailPager(FragmentManager fm, Context mContext, ArrayList<DetailPagerPojo> mList) {
        super(fm);

        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }

    public static class DetailPagerPojo {
        public Fragment fragment;
        public String title;



        public DetailPagerPojo(Fragment fragment, String string) {
            this.fragment = fragment;
            this.title = title;
        }


        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
