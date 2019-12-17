package com.kapalert.kadunastategovernment.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppBaseActivity {

    private EditText mEmail, mPassword;
    private AppCompatCheckBox mRemember;
    private TextView mForgot;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {

        checkUserLoginStatus();

        mEmail =  findViewById(R.id.email);
        mPassword =  findViewById(R.id.password);
        mRemember =  findViewById(R.id.remember);
        mForgot = (TextView) findViewById(R.id.forgot_pass);
        mLogin = (Button) findViewById(R.id.login);

        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotClick();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick();
            }
        });
    }

    private void checkUserLoginStatus() {
        if (db.getBoolean(Constants.DB_REMEMBER_ME)){
            if (Constants.isUserLoggedIn(mContext)){
                Utils.intentToActivity(mContext,HomeActivity.class);
                finish();
            }
        }else {
            Constants.setUser(mContext,null);
        }
    }

    private void loginClick() {
        if (!mEmail.getText().toString().matches(Constants.EMAIL_PATTERN)) {
            Utils.showToast(mContext, getString(R.string.invalid_email));
            return;
        }
        if (mPassword.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.invalid_password));
            return;
        }

        new ServerRequest<UserInfoJson>(mContext, Constants.getLoginUrl(mEmail.getText().toString(), mPassword.getText().toString()), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                UserInfoJson userInfoJson = response.body();
                if (userInfoJson.status) {
                    Constants.setUser(mContext, userInfoJson.user_data);
                    db.putBoolean(Constants.DB_REMEMBER_ME, mRemember.isChecked());
                    db.putString(Constants.USER_ID,userInfoJson.user_data.id);
                    Utils.intentToActivity(mContext, HomeActivity.class);
                    finish();
                } else {
                    Utils.showToast(mContext, userInfoJson.errorMessage);
                }
            }
        };
    }

    private void forgotClick() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_forgot_pass);

        final EditText email = (EditText) dialog.findViewById(R.id.email);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString();

                if (emailStr.matches(Constants.EMAIL_PATTERN)) {
                    hitForgotAPI(emailStr, dialog);
                } else {
                    email.setError(getString(R.string.invalid_email));
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void hitForgotAPI(String emailStr, final Dialog dialog) {
        new ServerRequest<UserInfoJson>(mContext, Constants.getForgotPassUrl(emailStr), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                UserInfoJson userInfoJson = response.body();

                if (userInfoJson.status){
                    dialog.dismiss();
                    Utils.showToast(mContext,userInfoJson.message);
                }else
                    Utils.showToast(mContext,userInfoJson.errorMessage);
            }
        };
    }
}
