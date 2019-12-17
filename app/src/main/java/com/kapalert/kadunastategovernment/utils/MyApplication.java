package com.kapalert.kadunastategovernment.utils;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;


import androidx.multidex.MultiDex;

import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ConfigurationBuilder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//Main application class
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(Constants.FONT_FILE)
                .setFontAttrId(uk.co.chrisjenx.calligraphy.R.attr.fontPath)
                .build()
        );

        final ACRAConfiguration config;
        try {
            config = new ConfigurationBuilder(this)
                    .setMailTo("naveen.orem@gmail.com")
                    .build();
            ACRA.init(this, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
