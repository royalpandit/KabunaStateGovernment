package com.kapalert.kadunastategovernment.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;


import com.google.firebase.auth.UserInfo;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileActivity extends AppBaseActivity {

    private CircleImageView mImage;
    private ImageView mAdd;
    private TextView mEmail;
    private EditText mFirstName, mLastName, mPhone;
    private Button mUpdatePass, mSave;
    private static final int PERMISSION_REQUEST_CODE = 32;
    private static final int PICK_FILE = 23;
    private File mImageFile;
    private File destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.showBackButton(mContext, toolbar);

        toolbar.setTitle(R.string.profile);
        initViews();
    }

    private void initViews() {
        mImage = (CircleImageView) findViewById(R.id.user_image);
        mAdd = (ImageView) findViewById(R.id.add_image);
        mEmail = (TextView) findViewById(R.id.email);
        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mPhone = (EditText) findViewById(R.id.phone);
        mUpdatePass = (Button) findViewById(R.id.update_pass);
        mSave = (Button) findViewById(R.id.save_profile);

        fillDetails();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClick();
            }
        });
        mUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClick();
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClick();
            }
        });
    }

    private void fillDetails() {
        UserInfoJson.UserData userInfo = Constants.getUser(mContext);

        mEmail.setText(userInfo.email);
        mFirstName.setText(userInfo.first_name);
        mLastName.setText(userInfo.last_name);
        mPhone.setText(userInfo.phone);
        Utils.loadImage(mContext, userInfo.getImageURL(), mImage, R.drawable.ic_user_placeholder, R.drawable.ic_user_placeholder);
    }

    private void addClick() {
        permissionGranted();
    }

    private void permissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                gotoGallery();
            }
        } else {
            gotoGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gotoGallery();
        } else {
            Utils.showToast(mContext, getString(R.string.permission_error));
        }
    }

    private void gotoGallery() {
        Utils.showDialog(mContext, null, getString(R.string.select_from), getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FILE);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                String name = dateFormat.format(Calendar.getInstance().getTime());
                destination = new File(Environment.getExternalStorageDirectory(), name + ".jpeg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mContext, Constants.FILE_PROVIDER, destination));
                startActivityForResult(intent, Constants.REQUEST_CAMERA_CAPTURE);
            }
        }, getString(R.string.camera), getString(R.string.cancel), null, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FILE) {
                Uri chosenImageUri = data.getData();
                String realPath = "";
                try {
                    realPath = Utils.getRealPathFromURI(mContext, chosenImageUri);
                    mImageFile = new File(realPath);

//                    Glide.with(mContext).load(mImageFile).error(R.drawable.ic_user_placeholder).placeholder(R.drawable.ic_user_placeholder).into(mImage);
                } catch (Exception e) {
                    Utils.showToast(mContext, getString(R.string.failed_to_fetch));
                }
                Log.e("path", realPath);
            } else if (requestCode == Constants.REQUEST_CAMERA_CAPTURE) {
                FileInputStream in;
                try {
                    in = new FileInputStream(destination);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                String imagePath = destination.getAbsolutePath();

                mImageFile = new File(imagePath);
            }
            try {
                Utils.loadImage(mContext, mImageFile, mImage, R.drawable.ic_user_placeholder, R.drawable.ic_user_placeholder);
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showToast(mContext, getString(R.string.failed_to_fetch));
            }
        } else {
            Utils.showToast(mContext, getString(R.string.failed_to_fetch));
        }
    }

    private void updateClick() {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_update_pass);

        final EditText newPass = (EditText) dialog.findViewById(R.id.new_pass);
//        final EditText currentPass = (EditText) dialog.findViewById(R.id.current_pass);
//        final EditText confirmNewPass = (EditText) dialog.findViewById(R.id.confirm_new_pass);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.update);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String current = currentPass.getText().toString();
                String newStr = newPass.getText().toString();
//                String confirmNew = confirmNewPass.getText().toString();

                if (newStr.length() > 5) {
                    updatePasswordAPI(newStr, dialog);
                } else {
                    Utils.showToast(mContext, getString(R.string.password_length_error));
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

    private void updatePasswordAPI(String current, final Dialog dialog) {
        new ServerRequest<UserInfoJson>(mContext, Constants.getUpdatePassUrl(mContext, current), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    dialog.dismiss();
                    Utils.showToast(mContext, response.body().message);
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    private void saveClick() {
        if (allFieldsValid()) {
            new ServerRequest<UserInfoJson>(mContext, Constants.getUpdateProfileUrl(mContext, mFirstName.getText().toString(), mLastName.getText().toString(), mPhone.getText().toString(), mImageFile), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        if (response.body().user_data != null) {
                            Constants.setUser(mContext, response.body().user_data);
                        }
                        Utils.showToast(mContext, response.body().message);
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        }
    }

    private boolean allFieldsValid() {

        if (mFirstName.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.fields_cannot_left_blank));
            return false;
        }

        if (mLastName.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.fields_cannot_left_blank));
            return false;
        }

        if (mPhone.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.fields_cannot_left_blank));
            return false;
        }


        return true;
    }
}
