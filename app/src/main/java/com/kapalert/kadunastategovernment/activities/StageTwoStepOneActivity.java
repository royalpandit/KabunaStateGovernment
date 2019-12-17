package com.kapalert.kadunastategovernment.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterPoliceFileList;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.PoliceFileListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

public class StageTwoStepOneActivity extends AppBaseActivity {

    private RecyclerView mList;
    private Button mAddFile, mSend;
    private static final int PERMISSION_REQUEST_CODE = 47;
    private CaseListPOJO.CaseList caseObj;
    private File mFile = null;
    private String mFileType = "";
    private TextView mFileName;
    private static final int PICK_FILE = 29;
    private File destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_two_step_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        caseObj = new Gson().fromJson(db.getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);
        if (caseObj == null) {
            finish();
            return;
        }
        Utils.showBackButton(mContext, toolbar);
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                initViews();
            }
        } else {
            initViews();
        }
    }

    private void initViews() {
        mList = (RecyclerView) findViewById(R.id.files_list);
        mAddFile = (Button) findViewById(R.id.add_file);
        mSend = (Button) findViewById(R.id.send);

        mAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClick();
            }
        });
        mSend.setVisibility(View.GONE);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.intentTo(mContext, StageOneStepTwoActivity.class);
                finish();
            }
        });
        setupList();
    }

    private void addClick() {
        mFile = null;
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_add_police_file);

        final TextView imageName = (TextView) dialog.findViewById(R.id.police_file_name);
        ImageView imageSelect = (ImageView) dialog.findViewById(R.id.police_file_select);
        Spinner fileType = (Spinner) dialog.findViewById(R.id.police_file_type);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        ArrayList<String> list = new ArrayList<>();
        list.add(mContext.getString(R.string.select_file_type));
        list.add(mContext.getString(R.string.file_type_send));
        list.add(mContext.getString(R.string.file_type_receive));

        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGallery(imageName);
            }
        });
        fileType.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1, list));
        fileType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mFileType = "" + (position + 1);
                } else {
                    mFileType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFile != null && !mFileType.isEmpty()) {
                    addFileAPI(dialog);
                } else if (mFile == null) {
                    Utils.showToast(mContext, getString(R.string.select_file_error));
                } else {
                    Utils.showToast(mContext, getString(R.string.select_file_type_error));
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

    private void addFileAPI(final Dialog dialog) {
        new ServerRequest<UserInfoJson>(mContext, Constants.getCreateLitigationCommentUrl(mContext, caseObj.id, mFileType, mFile), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                if (response.body().status) {
                    Utils.showToast(mContext, response.body().message);
                    dialog.dismiss();
                    setupList();
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };
    }

    public void gotoGallery(TextView fileName) {
        mFileName = fileName;
        Utils.showDialog(mContext, null, getString(R.string.select_from), getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
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
                    mFile = new File(realPath);
                    mFileName.setText(mFile.getName());
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

                mFile = new File(imagePath);
            }try {
                mFileName.setText(mFile.getName());
            } catch (Exception e) {
                Utils.showToast(mContext, getString(R.string.failed_to_fetch));
            }
        } else {
            Utils.showToast(mContext, getString(R.string.failed_to_fetch));
        }
    }

    private void setupList() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mList.setLayoutManager(manager);

        new ServerRequest<PoliceFileListPOJO>(mContext, Constants.getPoliceFileListUrl(mContext, caseObj.id), true) {
            @Override
            public void onCompletion(Call<PoliceFileListPOJO> call, Response<PoliceFileListPOJO> response) {
                if (response.body().status) {
                    if (response.body().fileList != null && !response.body().fileList.isEmpty()) {
                        ArrayList<PoliceFileListPOJO.PoliceFile> list = new ArrayList<>(response.body().fileList);
                        AdapterPoliceFileList adapterPoliceFileList = new AdapterPoliceFileList(mContext, list);
                        if (adapterPoliceFileList.getItemCount() > 0) {
                            mSend.setVisibility(View.VISIBLE);
                        }
                        mList.setAdapter(adapterPoliceFileList);
                    } else {
                        Utils.showToast(mContext, response.body().message);
                    }
                } else {
                    Utils.showToast(mContext, response.body().errorMessage);
                }
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initViews();
            } else {
                Utils.showToast(mContext, getString(R.string.permission_error));
                finish();
            }
        }
    }
}
