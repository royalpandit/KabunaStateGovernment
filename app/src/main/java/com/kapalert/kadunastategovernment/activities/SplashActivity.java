package com.kapalert.kadunastategovernment.activities;

import android.os.Bundle;
import android.os.Handler;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Utils;

public class SplashActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.intentToActivity(mContext, LoginActivity.class);
                finish();
            }
        }, 2000);

        Bundle extras = getIntent().getExtras();

    }
}
