package com.kapalert.kadunastategovernment.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kapalert.kadunastategovernment.templates.UserInfoJson;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Dawinder on 07/07/2016.
 */
public class AppBaseActivity extends AppCompatActivity {

    public Context mContext;
    public TinyDB db;
    public UserInfoJson.UserData userPOJO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = AppBaseActivity.this;
        db = new TinyDB(this);
        userPOJO = Constants.getUser(mContext);
        super.onCreate(savedInstanceState);

    }

/*
    private void setupAds() {
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        boolean showSuccess = Appodeal.show(this, Appodeal.BANNER_VIEW);
        if (showSuccess){
            Utils.showLog("Add shown success");
        }else{
            Utils.showLog("Add show failed");
        }
    }
*/

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userPOJO = Constants.getUser(mContext);
    }

    public void backClick(View view) {
        onBackPressed();
    }
}
