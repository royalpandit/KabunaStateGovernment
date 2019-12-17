package com.kapalert.kadunastategovernment.utils;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by win10 on 8/9/2017.
 */

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public TinyDB mDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mDB = new TinyDB(mContext);
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);



}
