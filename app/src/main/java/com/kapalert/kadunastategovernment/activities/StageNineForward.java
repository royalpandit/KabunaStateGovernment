package com.kapalert.kadunastategovernment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterCounselSpinner;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;
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

public class StageNineForward extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final int PERMISSION_REQUEST_CODE = 36;
    private static final int PICK_FILE = 28;
    private File mFile;
    private File destination;
String value=null,caseid=null;
    Spinner genderspinner;
    EditText name1;


    private Spinner mSelectCounsel;
    private Button mSend;
    private String mSelectedCounselID = "";
    private ArrayList<UserListPOJO.UserDetail> mCounselList;

    private String[] state = { "Please Select Type", "Send to police", "Recieved from police"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_nine_forward);
 name1=(EditText) findViewById(R.id.file_name) ;
      ImageView  case_file_select=(ImageView) findViewById(R.id.case_file_select);
        case_file_select.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              addFileClick();
          }
      });
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        caseid =(String) b.get("caseid");

        genderspinner = (Spinner) findViewById(R.id.genderspinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);
        genderspinner.setOnItemSelectedListener(this);

        Button ok=(Button) findViewById(R.id.submit) ;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            senddata(mFile,caseid,value,name1.getText().toString());
            }
        });

        Button cunsel=(Button) findViewById(R.id.ok) ;
        cunsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cunsel(caseid,mSelectedCounselID);
            }
        });
        mSelectCounsel = (Spinner) findViewById(R.id.select_counsel);


        fetchCounselList();

    }


    private void fetchCounselList() {
        new ServerRequest<UserListPOJO>(StageNineForward.this, Constants.getCounselListUrl(StageNineForward.this), true) {
            @Override
            public void onCompletion(Call<UserListPOJO> call, Response<UserListPOJO> response) {

                if (response.body().status) {
                    if (response.body().usersList != null && !response.body().usersList.isEmpty()) {
                        ArrayList<UserListPOJO.UserDetail> list = new ArrayList<>(response.body().usersList);
                        for (int i = 0; i < response.body().usersList.size(); i++) {
                            if (list.get(i).ID.equalsIgnoreCase(Constants.getUser(StageNineForward.this).id)) {
                                list.remove(i);
                                break;
                            }
                        }
                        mCounselList = new ArrayList<>(list);
                    } else {
                        mCounselList = new ArrayList<>();
                    }
                    setupSpinner();
                } else {
                    Utils.showToast(StageNineForward.this, response.body().errorMessage);
                }
            }
        };
    }
    private void setupSpinner() {

        AdapterCounselSpinner adapterOffenceInvestigationSpinner = new AdapterCounselSpinner(StageNineForward.this, mCounselList, getString(R.string.select_counsel));
        mSelectCounsel.setAdapter(adapterOffenceInvestigationSpinner);
        mSelectCounsel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mSelectedCounselID = mCounselList.get(position).ID;
                } else {
                    mSelectedCounselID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        genderspinner.setSelection(position);
        String selState = (String) genderspinner.getSelectedItem();
        // Toast.makeText(Cas, ""+selState, Toast.LENGTH_SHORT).show();
        if(selState.equals("Send to police")){

           value="2";

        }else if(selState.equals("Recieved from police")) {

            value="3";
        }
        else {
            //Toast.makeText(this, "Please Select Case", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void addFileClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoGallery();
            } else {
                Utils.showToast(StageNineForward.this, getString(R.string.permission_error));
            }
        }
    }

    public void gotoGallery() {
        Utils.showDialog(StageNineForward.this, null, getString(R.string.select_from), getString(R.string.gallery), new DialogInterface.OnClickListener() {
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
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(StageNineForward.this, Constants.FILE_PROVIDER, destination));
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
                    realPath = Utils.getRealPathFromURI(StageNineForward.this, chosenImageUri);
                    mFile = new File(realPath);
                } catch (Exception e) {
                    Utils.showToast(StageNineForward.this, getString(R.string.failed_to_fetch));
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
            }
        } else {
            Utils.showToast(StageNineForward.this, getString(R.string.failed_to_fetch));
        }
    }
    private void senddata(File mFile,String caseid, String s,String name) {

        new ServerRequest<UserInfoJson>(this, Constants.forwarddata(this, mFile, caseid, s,name), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                try{
                    if (response.body().status) {
                        Utils.showToast(StageNineForward.this , response.body().message);
                        Log.e("output",""+response.body().message);
                       // onBackPressed();
                    } else {
                        Utils.showToast(StageNineForward.this, response.body().errorMessage);
                        Log.e("errrrrrrrr",""+response.body().errorMessage);
                    }
                }
                catch (Exception e){

                    Log.e("Exception e",""+e.getStackTrace());
                }
            }

        };
    }




    private void cunsel(String caseid,String cuselid) {

        new ServerRequest<UserInfoJson>(this, Constants.cunseldata(this,caseid,cuselid), true) {
            @Override
            public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                try{
                    if (response.body().status) {
                        Utils.showToast(StageNineForward.this , response.body().message);
                        Log.e("output",""+response.body().message);
                     onBackPressed();
                    } else {
                        Utils.showToast(StageNineForward.this, response.body().errorMessage);
                        Log.e("errrrrrrrr",""+response.body().errorMessage);
                    }
                }
                catch (Exception e){

                    Log.e("Exception e",""+e.getStackTrace());
                }
            }

        };
    }

}
