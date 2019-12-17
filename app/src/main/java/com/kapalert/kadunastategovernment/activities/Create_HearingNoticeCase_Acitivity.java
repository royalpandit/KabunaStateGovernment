package com.kapalert.kadunastategovernment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.FileListAdapter;
import com.kapalert.kadunastategovernment.adapters.RelatedCase_HearingNotice_Adapter;
import com.kapalert.kadunastategovernment.response.AssignRespons;
import com.kapalert.kadunastategovernment.response.AssignTo;
import com.kapalert.kadunastategovernment.templates.BeanUploadImage;
import com.kapalert.kadunastategovernment.templates.Bean_RelatedCase_POJO;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;
import com.kapalert.kadunastategovernment.webservice.WebServiceCaller;

import static com.kapalert.kadunastategovernment.utils.Constants.NOTICE_RELATED_CASE_ID;
import static com.kapalert.kadunastategovernment.utils.Constants.USER_ID;
import static com.kapalert.kadunastategovernment.utils.Utils.PERMISSIONS;
import static com.kapalert.kadunastategovernment.utils.Utils.PERMISSION_ALL;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.kapalert.kadunastategovernment.utils.Utils.showToast;
import static com.kapalert.kadunastategovernment.webservice.ApiConstant.CreateaseApi;
import static com.kapalert.kadunastategovernment.webservice.ApiConstant.Createcase_OTHER_Api;
import static com.kapalert.kadunastategovernment.webservice.ApiConstant.RelatedCaseApi;

public class Create_HearingNoticeCase_Acitivity extends AppCompatActivity {
    Spinner sp_assign;
    public static EditText edt_case_filename,edt_caseFile_reference;
    public static TextView tv_relatedCase;
    Button btn_submit;
    ImageView img_file_select;
   public static RecyclerView recycleFileList,recycle_relatedCase_list;
    Context mContext;
    List<Bean_RelatedCase_POJO> relatedCase_pojoList;
    RelatedCase_HearingNotice_Adapter related_hearing_adapter;
    private  boolean RelatedCaseList_IsVisible = true;
    LinearLayout layout_relatedCase;
    TinyDB sessionManager;
    String str_caseFileName = "",str_caseFileReference= "",str_tvRelatedCase="", str_assignTo = "";

    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PERMISSION_REQUEST_CODE = 36;
    private static final int PICK_FILE = 28;
    FileListAdapter fileListAdapter;
    List<BeanUploadImage> beanUploadImages;
    Uri uri;
    String imagePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__hearing_notice);
        mContext = Create_HearingNoticeCase_Acitivity.this;
        sp_assign = findViewById(R.id.assign);
        edt_case_filename = findViewById(R.id.edt_case_filename);
        tv_relatedCase = findViewById(R.id.tv_relatedCase);
        edt_caseFile_reference = findViewById(R.id.edt_caseFile_reference);
        btn_submit = findViewById(R.id.btn_submit);
        img_file_select = findViewById(R.id.img_file_select);
        recycleFileList = findViewById(R.id.recycleFileList);
        recycle_relatedCase_list = findViewById(R.id.recycle_relatedCase_list);
        layout_relatedCase = findViewById(R.id.layout_relatedCase);

        sessionManager = new TinyDB(mContext);

        if (!Utils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        tv_relatedCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(RelatedCaseList_IsVisible==true  )
                {
                    recycle_relatedCase_list.setVisibility(View.GONE);
                    RelatedCaseList_IsVisible = false;
                }
                else if(RelatedCaseList_IsVisible==false && tv_relatedCase.length() == 0)
                {
                    recycle_relatedCase_list.setVisibility(View.VISIBLE);
                    RelatedCaseList_IsVisible = true;
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               allFieldsValid();
            }
        });

        img_file_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFileClick();
            }
        });

        getassign();
        getRelatedcaseData();

    }


    public void addFileClick() {

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }else {
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
                showToast(this, getString(R.string.permission_error));
            }
        }
    }

    public void gotoGallery() {
        Utils.showDialog(this, null, getString(R.string.select_from), getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_FILE);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, "", getString(R.string.cancel), null, true);
    }

    private void getassign() {
        new ServerRequest<AssignRespons>(this, Constants.assignResponsCall(this), true) {
            @Override
            public void onCompletion(Call<AssignRespons> call, Response<AssignRespons> response) {
                List<AssignTo> semuadosenItems = response.body().getAssignTo();
                List<AssignTo> itemid = response.body().getAssignTo();
                List<String> listSpinner = new ArrayList<String>();
                final List<String> itemspin = new ArrayList<String>();
                listSpinner.add("Select Assign To");
                itemspin.add("0");
                for (int i = 0; i < semuadosenItems.size(); i++){
                    listSpinner.add(semuadosenItems.get(i).getGroupName());
                    itemspin.add(String.valueOf(itemid.get(i).getId()));
                }
                // Set hasil result json ke dalam adapter spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.sppiner_item, listSpinner);
                adapter.setDropDownViewResource(R.layout.sppiner_item);
                sp_assign.setAdapter(adapter);

                sp_assign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> adapterView, View view,
                            int i, long l) {
                        str_assignTo = itemspin.get(i);
                    }
                    public void onNothingSelected(
                            AdapterView<?> adapterView) {

                    }
                });
            }
        };

    }


    private void getRelatedcaseData() {

        WebServiceCaller webServiceCaller = new WebServiceCaller(WebServiceCaller.POST_TASK,mContext,"Please Wait Loading....",true) {
            @Override
            public void handleResponse(String response) {
                try {
                    relatedCase_pojoList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);

                    System.out.println("=======response======="+response);

                    if(jsonObject.getString("status").equalsIgnoreCase("true"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("usersList");
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            relatedCase_pojoList.add(new Bean_RelatedCase_POJO(
                                    obj.getString("id"),
                                    obj.getString("file_name")
                            ));

                            System.out.println("==hospital_modelList==============="+relatedCase_pojoList.size());

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            recycle_relatedCase_list.setLayoutManager(mLayoutManager);

                            related_hearing_adapter = new RelatedCase_HearingNotice_Adapter(mContext, relatedCase_pojoList);
                            recycle_relatedCase_list.setAdapter(related_hearing_adapter);
                        }
                    }
                    else
                    {
                        Utils.showToast(mContext,getString(R.string.no_data_found));
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        };
        webServiceCaller.addNameValuePair("user_id",sessionManager.getString(USER_ID));
        webServiceCaller.execute(RelatedCaseApi+"token="+Constants.getUser(mContext).token);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_FILE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data



                String[] filePathColumn = { MediaStore.Files.FileColumns.DATA  };
                beanUploadImages = new ArrayList<>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();
                    File file = new File(mImageUri.getPath().toString());
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    //////    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    ///     imagePath  = cursor.getString(columnIndex);
                    /// imagePath = file.getName().toString();

                    imagePath = String.valueOf(data.getData());


                    beanUploadImages.add(new BeanUploadImage("",imagePath));
                    cursor.close();
                    System.out.println("image list====" + beanUploadImages.size());
                    fileListAdapter = new FileListAdapter(this,beanUploadImages);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
                    recycleFileList.setLayoutManager(linearLayoutManager);
                    recycleFileList.setAdapter(fileListAdapter);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imagePath  = cursor.getString(columnIndex);


                            beanUploadImages.add(new BeanUploadImage("",imagePath));
                            cursor.close();
                            System.out.println("image list====" + beanUploadImages.size());
                            fileListAdapter = new FileListAdapter(this,beanUploadImages);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
                            recycleFileList.setLayoutManager(linearLayoutManager);
                            recycleFileList.setAdapter(fileListAdapter);
                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            }

            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void allFieldsValid() {

        if (beanUploadImages == null) {
            showToast(this, getString(R.string.invalid_file_selected));

        }

        else if (edt_case_filename.getText().toString().isEmpty()) {

            showToast(this, getString(R.string.invalid_file_name));
        }
        else if (tv_relatedCase.getText().toString().isEmpty() /*|| !mRefNum.getText().toString().matches("[0-9][0-9][0-9]/[0-9][0-9][0-9]")*/) {
            showToast(this, getString(R.string.ref_num_error));

        }

        else
        {
            str_caseFileName = edt_case_filename.getText().toString();
            str_caseFileReference = edt_caseFile_reference.getText().toString();
            str_tvRelatedCase = sessionManager.getString(NOTICE_RELATED_CASE_ID);
            createCase(mContext, beanUploadImages, str_caseFileName,str_caseFileReference,str_tvRelatedCase,str_assignTo);

        }

    }




    private void createCase(Context mContext,List<BeanUploadImage> beanUploadImages, String str_caseFileName,String str_caseFileReference,String str_tvRelatedCase,String str_assignTo)
    {

        WebServiceCaller webServiceCaller = new WebServiceCaller(WebServiceCaller.POST_TASK,mContext,"Please Wait Loading....",true) {
            @Override
            public void handleResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    System.out.println("=======response======="+response);

                    if(jsonObject.getString("status").equalsIgnoreCase("true"))
                    {
                        Utils.showToast(mContext,getString(R.string.successfully_uploaded));
                        finish();startActivity(getIntent());

                    }
                    else
                    {
                        String err = jsonObject.getString("errorMessage");
                        Utils.showToast(mContext,err);
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        };
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        entityBuilder.addTextBody("token",Constants.getUser(this).token);
        entityBuilder.addTextBody("case","Hearing Notice");
        entityBuilder.addTextBody("user_id",sessionManager.getString(USER_ID));
        entityBuilder.addTextBody("file_name",str_caseFileName);
        entityBuilder.addTextBody("case_file_reference",str_caseFileReference);
        entityBuilder.addTextBody("group",str_assignTo);
        entityBuilder.addTextBody("related_case_id   ",str_tvRelatedCase);




        FileBody bin1 = null;
        for (int i = 0; i < beanUploadImages.size(); i++) {
            String filePaths = String.valueOf(beanUploadImages.get(i).getUri());
            File sourceFile = new File(filePaths);
            bin1 = new FileBody(sourceFile);
            System.out.print(bin1 + "img=====");

            entityBuilder.addPart("casefile[" + i + "]", new FileBody(sourceFile));
            //entity.addPart("images[1]",  new FileBody(sourceFile2));
        }
        HttpEntity entity = entityBuilder.build();
        webServiceCaller.addEntity(entity);
        webServiceCaller.execute(Createcase_OTHER_Api);
    }
}
