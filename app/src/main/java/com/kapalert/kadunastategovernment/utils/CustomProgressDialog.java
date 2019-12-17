package com.kapalert.kadunastategovernment.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kapalert.kadunastategovernment.R;


public class CustomProgressDialog extends Dialog {

    private Context context;
    private ImageView imgSpinner;
    private TextView txtMessage;

    public CustomProgressDialog(Context context) {

        super(context, R.style.CustomProgressDialog);
        this.context = context;

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        setContentView(R.layout.progress_spinner);
        imgSpinner = (ImageView) findViewById(R.id.imgSpinner);

        txtMessage = (TextView) findViewById(R.id.txtMessage);
        //App.getInstance().setTypeface(txtMessage, App.FontType.ANTONIO_REGULAR);
    }

    @Override
    public void show() {
        super.show();

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1500);

        imgSpinner.setAnimation(anim);
        imgSpinner.startAnimation(anim);
    }

    public void show(CharSequence message) {
        show();
        if (message.toString().isEmpty())
            txtMessage.setVisibility(View.GONE);
        txtMessage.setText(message);
    }

    public void show(int messageId) {
        show(context.getString(messageId));
    }

    public void setMessage(String message) {
        if (this.isShowing())
            txtMessage.setText(message);
    }

    public void dismiss(boolean showProgressCompletedOk) {

        if (showProgressCompletedOk) {
            imgSpinner.clearAnimation();
            imgSpinner.setVisibility(View.GONE);

            findViewById(R.id.imgCheckmark).setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    try {
                        dismiss();
                    } catch (Exception e) {
                    }
                    /*if (!(context instanceof AppBaseActivity) && isShowing())
                        // avoid view not attached to window manager
                        try {
                            dismiss();
                        } catch (IllegalArgumentException e) {
                        }*/
                }
            }, 1000);
        } else {
            super.dismiss();
        }
    }
}