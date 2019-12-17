package com.kapalert.kadunastategovernment.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedList;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedList1;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedList2;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedList3;

import com.kapalert.kadunastategovernment.adapters.FileListAdapter;
import com.kapalert.kadunastategovernment.adapters.RelatedCase_HearingNotice_Adapter;
import com.kapalert.kadunastategovernment.response.ActionListRespons;
import com.kapalert.kadunastategovernment.response.ActionModel;
import com.kapalert.kadunastategovernment.response.AdapterOffenceNatureList1;
import com.kapalert.kadunastategovernment.response.AssignRespons;
import com.kapalert.kadunastategovernment.response.AssignTo;
import com.kapalert.kadunastategovernment.response.DisputeRespons;
import com.kapalert.kadunastategovernment.response.MdaInvokedRespons;
import com.kapalert.kadunastategovernment.response.MdaList;

import com.kapalert.kadunastategovernment.response.OffenceLocationList;
import com.kapalert.kadunastategovernment.templates.BeanUploadImage;
import com.kapalert.kadunastategovernment.templates.Bean_RelatedCase_POJO;
import com.kapalert.kadunastategovernment.templates.OffenceModal;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.ServerRequest;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;
import com.kapalert.kadunastategovernment.webservice.WebServiceCaller;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.kapalert.kadunastategovernment.utils.Constants.FILE_PROVIDER;
import static com.kapalert.kadunastategovernment.utils.Constants.USER_ID;
import static com.kapalert.kadunastategovernment.utils.Utils.PERMISSIONS;
import static com.kapalert.kadunastategovernment.utils.Utils.PERMISSION_ALL;
import static com.kapalert.kadunastategovernment.utils.Utils.PICK_Gallery_IMAGE;
import static com.kapalert.kadunastategovernment.utils.Utils.REQUEST_CAMERA_ACCESS_PERMISSION;
import static com.kapalert.kadunastategovernment.utils.Utils.hasPermissions;
import static com.kapalert.kadunastategovernment.utils.Utils.showToast;
import static com.kapalert.kadunastategovernment.webservice.ApiConstant.CreateaseApi;
import static com.kapalert.kadunastategovernment.webservice.ApiConstant.RelatedCaseApi;

public class CreateCivilCaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {






         private RecyclerView accusedList3;
        EditText case_file_name;
        EditText  plaintiffs,defendants;
        ImageView case_file_select;
        RecyclerView accused_list,accused_list1,recycleFileList;
        Button add_accused,add_accused1,submit;
        Spinner mda,Dispute,assign,genderspinner;
        TextView cause,datereqmda,datebrimda;
        String mdavalue="",assignvalue="";
       List<BeanUploadImage> beanUploadImages;
       Uri uri;
        FileListAdapter fileListAdapter ;
        TinyDB db ;

    String filename;

    private ArrayList<AccusedModal1> accusedList;
    private AdapterAccusedList adapterAccusedList;
    private AdapterAccusedList2 adapterAccusedList2;
    private AdapterAccusedList3 adapterAccusedList3;
    private AdapterAccusedList3 adapterAccusedList4;
    private ArrayList<AccusedModal1> accusedList2;
    private ArrayList<String> imagesPathList;

    private ArrayList<AccusedModal2> accusedList1;
    private AdapterAccusedList1 adapterAccusedList1;

    private static final int PERMISSION_REQUEST_CODE = 36;
    private static final int PICK_FILE = 28;

    private ArrayList<OffenceModal> mOffenceNature;
    private ArrayList<ActionModel> mOffenceInvest;
    private ArrayList<OffenceModal> mOffenceLoc;
    private Calendar mDateOffence;
    private String mInvestigationID = "", mLocationID = "",location_of_dispute="";
    private ArrayList<String> mSelectedNatures = new ArrayList<>();

    private File mFile;
    String imagePath;
    private File destination;
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_civil_case);
        case_file_name= (EditText) findViewById(R.id.case_file_name);
        case_file_select=(ImageView) findViewById(R.id.case_file_select);
        plaintiffs=(EditText) findViewById(R.id.plaintiffs);
        defendants=(EditText) findViewById(R.id.defendants);
        accused_list=(RecyclerView) findViewById(R.id.accused_list);
        add_accused=(Button) findViewById(R.id.add_accused);
        accused_list1=(RecyclerView) findViewById(R.id.accused_list1);
        add_accused1=(Button) findViewById(R.id.add_accused1);
        mda=(Spinner) findViewById(R.id.mda);
        cause=(TextView) findViewById(R.id.cause);
        Dispute=(Spinner) findViewById(R.id.Dispute);
        datereqmda=(TextView) findViewById(R.id.datereqmda);
        datebrimda=(TextView) findViewById(R.id.datebrimda);
        assign=(Spinner) findViewById(R.id.assign);
        submit=(Button) findViewById(R.id.submit);
        recycleFileList = findViewById(R.id.recycleFileList);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allFieldsValid();
            }
        });



        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        db = new TinyDB(this);
        setupList();
        setupList1();


        add_accused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccusedDialog();
            }
        });
        add_accused1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccusedDialog1();
            }
        });
        case_file_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFileClick();
            }
        });

        cause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOffenceNature();
            }
        });
        getOffenceNatureList();
        setupDatePickers();
    }

    private void setupDatePickers() {
        datereqmda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateCivilCaseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate.set(Calendar.HOUR_OF_DAY, 6);
                        selectedDate.set(Calendar.MINUTE, 6);
                        selectedDate.set(Calendar.SECOND, 6);
                        selectedDate.set(Calendar.MILLISECOND, 6);

                        mDateOffence = selectedDate;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        datereqmda.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        datebrimda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDateOffence == null) {
                    showToast(CreateCivilCaseActivity.this, getString(R.string.select_offence_date_first));
                    return;
                }
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateCivilCaseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        datebrimda.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


    }

    private void selectOffenceNature() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_select_offence_nature);

        final RecyclerView userList = (RecyclerView) dialog.findViewById(R.id.offence_nature_list);

        setupOffenceNatureList(userList);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedNatures = new ArrayList<String>();

                for (ActionModel offenceModal : mOffenceInvest) {
                    if (offenceModal.isSelected())
                        mSelectedNatures.add(offenceModal.getId());
                }
                cause.setText("" + mSelectedNatures.size());
                dialog.dismiss();
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
    private void setupOffenceNatureList(RecyclerView offenceNatureList) {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        offenceNatureList.setLayoutManager(manager);

        AdapterOffenceNatureList1 adapterOffenceNatureList1 = new AdapterOffenceNatureList1(CreateCivilCaseActivity.this, mOffenceInvest);
        offenceNatureList.setAdapter(adapterOffenceNatureList1);

    }

    private void getOffenceNatureList() {
        new ServerRequest<MdaInvokedRespons>(this, Constants.mdaInvokedResponsCall(this), true) {
            @Override
            public void onCompletion(Call<MdaInvokedRespons> call, Response<MdaInvokedRespons> response) {
                List<MdaList> semuadosenItems = response.body().getMdaList();
                    List<MdaList> itemid = response.body().getMdaList();
                    List<String> listSpinner = new ArrayList<String>();
                    final List<String> itemspin = new ArrayList<String>();
                    listSpinner.add("Select MDA Involved");
                    itemspin.add("0");
                    for (int i = 0; i < semuadosenItems.size(); i++){
                        listSpinner.add(semuadosenItems.get(i).getName());
                        itemspin.add(String.valueOf(itemid.get(i).getId()));
                    }
                    // Set hasil result json ke dalam adapter spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.sppiner_item, listSpinner);
                    adapter.setDropDownViewResource(R.layout.sppiner_item);
                    mda.setAdapter(adapter);

                    mda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> adapterView, View view,
                                int i, long l) {
                         mdavalue = itemspin.get(i);


                        }

                        public void onNothingSelected(
                                AdapterView<?> adapterView) {

                        }
                    });
            }
        };
        getdisput();
    }

    private void getdisput() {
        new ServerRequest<DisputeRespons>(this, Constants.disputeResponsCall(this), true) {
            @Override
            public void onCompletion(Call<DisputeRespons> call, Response<DisputeRespons> response) {
                List<OffenceLocationList> semuadosenItems = response.body().getOffenceLocationList();
                List<OffenceLocationList> itemid = response.body().getOffenceLocationList();
                List<String> listSpinner = new ArrayList<String>();
                final List<String> itemspin = new ArrayList<String>();
                listSpinner.add("Select Location of Dispute");
                itemspin.add("0");
                for (int i = 0; i < semuadosenItems.size(); i++){
                    listSpinner.add(semuadosenItems.get(i).getName());
                    itemspin.add(String.valueOf(itemid.get(i).getId()));
                }
                // Set hasil result json ke dalam adapter spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.sppiner_item, listSpinner);
                adapter.setDropDownViewResource(R.layout.sppiner_item);
                Dispute.setAdapter(adapter);

                Dispute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> adapterView, View view,
                            int i, long l) {
                        location_of_dispute= itemspin.get(i);


                    }

                    public void onNothingSelected(
                            AdapterView<?> adapterView) {

                    }
                });
            }
        };
        getOffenceNatureList1();
        getassign();
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
                assign.setAdapter(adapter);

                assign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> adapterView, View view,
                            int i, long l) {
                        assignvalue = itemspin.get(i);
                    }
                    public void onNothingSelected(
                            AdapterView<?> adapterView) {

                    }
                });
            }
        };
        getOffenceNatureList1();
    }
     void getOffenceNatureList1() {

             new ServerRequest<ActionListRespons>(this, Constants.actionListResponsCall(this), true) {
                 @Override
                 public void onCompletion(Call<ActionListRespons> call, Response<ActionListRespons> response) {
                     if (response.body().actionList != null) {
                         mOffenceInvest = new ArrayList<>(response.body().actionList);
                     } else {
                         mOffenceInvest = new ArrayList<>();
                     }
                 }
             };

    }
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        genderspinner.setSelection(position);
        String selState = (String) genderspinner.getSelectedItem();
        //Toast.makeText(this, ""+selState, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    private void addAccusedDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_plaintiffs);

        final EditText accusedName = (EditText) dialog.findViewById(R.id.accused_name);
        final EditText accusedAge = (EditText) dialog.findViewById(R.id.accused_age);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.update);
         String[] state = { "Male", "Female", "Not Applicable"};
        genderspinner = (Spinner) dialog.findViewById(R.id.genderspinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);
        genderspinner.setOnItemSelectedListener(this);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = accusedName.getText().toString();
                String age = accusedAge.getText().toString();
                String plaintiff1=genderspinner.getSelectedItem().toString();
                if (!name.isEmpty() && !age.isEmpty()) {
                    accusedList1.add(new CreateCivilCaseActivity.AccusedModal2(name,age,plaintiff1));

                    adapterAccusedList2.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    showToast(CreateCivilCaseActivity.this, getString(R.string.fields_cannot_left_blank));
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

    private void setupList() {
        accused_list.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        accused_list.setLayoutManager(manager);

        accusedList1 = new ArrayList<>();

        adapterAccusedList2 = new AdapterAccusedList2(CreateCivilCaseActivity.this,accusedList1);
        accused_list.setAdapter(adapterAccusedList2);
    }
    private void addAccusedDialog1() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_defendants);

        final EditText accusedName = (EditText) dialog.findViewById(R.id.accused_name);
        final EditText accusedAge = (EditText) dialog.findViewById(R.id.accused_age);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.update);
        String[] state = { "Male", "Female", "Not Applicable"};
        genderspinner = (Spinner) dialog.findViewById(R.id.genderspinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderspinner.setAdapter(adapter_state);
        genderspinner.setOnItemSelectedListener(this);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = accusedName.getText().toString();
                String age = accusedAge.getText().toString();
                String defand=genderspinner.getSelectedItem().toString();


                if (!name.isEmpty() && !age.isEmpty()) {
                    accusedList.add(new CreateCivilCaseActivity.AccusedModal1(name, age,defand));

                    adapterAccusedList3.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    showToast(CreateCivilCaseActivity.this, getString(R.string.fields_cannot_left_blank));
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
    private void setupList1() {
        accused_list1.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        accused_list1.setLayoutManager(manager);

        accusedList = new ArrayList<>();

        adapterAccusedList3 = new AdapterAccusedList3(CreateCivilCaseActivity.this,accusedList);
        accused_list1.setAdapter(adapterAccusedList3);
    }


    public class AccusedModal2 {
        String name;
        String age;
        String plan;
        public AccusedModal2(String name, String age,String plan) {
            this.name = name;
            this.age = age;
            this.plan=plan;
        }
        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    public class AccusedModal1 {
        String name;
        String age;
        String defand;
        public AccusedModal1(String name, String age, String defand) {
            this.name = name;
            this.age = age;
            this.defand=defand;
        }
        public String getDefand() {
            return defand;
        }

        public void setDefand(String defand) {
            this.defand = defand;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
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


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        beanUploadImages = new ArrayList<>();
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PICK_FILE) {
//                Uri chosenImageUri = data.getData();
//
//                try {
//                    imagePath = Utils.getRealPathFromURI(this, chosenImageUri);
//                    mFile = new File(imagePath);
//                    System.out.println("real list====" + imagePath);
//                    beanUploadImages.add(new BeanUploadImage("",imagePath));
//                                          System.out.println("image list====" + beanUploadImages.size());
//
//                    fileListAdapter = new FileListAdapter(this,beanUploadImages);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//                    recycleFileList.setLayoutManager(linearLayoutManager);
//                    recycleFileList.setAdapter(fileListAdapter);
//                } catch (Exception e) {
//                    Utils.showToast(this, getString(R.string.failed_to_fetch));
//                }
//                ////Log.e("path", imagePath);
//            } else if (requestCode == Constants.REQUEST_CAMERA_CAPTURE) {
//                FileInputStream in;
//                try {
//                    in = new FileInputStream(destination);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                String realPath = destination.getAbsolutePath();
//
//                beanUploadImages.add(new BeanUploadImage("",realPath));
//                //                            System.out.println("image list====" + beanUploadImages.size());
//                fileListAdapter = new FileListAdapter(this,beanUploadImages);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//                recycleFileList.setLayoutManager(linearLayoutManager);
//                recycleFileList.setAdapter(fileListAdapter);
//                mFile = new File(imagePath);
//            }
//
//
//        } else {
//            Utils.showToast(this, getString(R.string.failed_to_fetch));
//        }
//    }

    private void allFieldsValid() {

        if (beanUploadImages == null) {
            showToast(this, getString(R.string.invalid_file_selected));

        }

        else if (case_file_name.getText().toString().isEmpty()) {
            showToast(this, getString(R.string.invalid_file_name));
        }
        else if (plaintiffs.getText().toString().isEmpty() /*|| !mRefNum.getText().toString().matches("[0-9][0-9][0-9]/[0-9][0-9][0-9]")*/) {
            showToast(this, getString(R.string.ref_num_error));
        }
        else if (defendants.getText().toString().isEmpty() /*|| !mRefNum.getText().toString().matches("[0-9][0-9][0-9]/[0-9][0-9][0-9]")*/) {
            showToast(this, getString(R.string.ref_num_error));
        }

        else if (accusedList == null || accusedList.isEmpty()) {
            showToast(this, getString(R.string.no_accused_error));
        }

        else  if (datereqmda.getText().toString().isEmpty()) {
            showToast(this, getString(R.string.police_file_date_error));
        }
        else if (datebrimda.getText().toString().isEmpty()) {
            showToast(this, getString(R.string.arrest_date_error));
        }

        else  if (mSelectedNatures == null || mSelectedNatures.isEmpty()) {
            showToast(this, getString(R.string.nature_select_error));
        }

        else
        {
            String jsonData = new Gson().toJson(new CreateCaseJSON1(accusedList, accusedList1, mSelectedNatures));

            createCase(this, beanUploadImages, case_file_name.getText().toString(), plaintiffs.getText().toString(), defendants.getText().toString(), mdavalue, jsonData, location_of_dispute, datereqmda.getText().toString(), datebrimda.getText().toString(), assignvalue);

        }

    }
//    private void submitClick() {
//        if (allFieldsValid()) {
//            String jsonData = new Gson().toJson(new CreateCaseJSON1(accusedList, accusedList1, mSelectedNatures));
//                String user_id = db.getString(USER_ID);
//
////            for (int i = 0; i < beanUploadImages.size(); i++) {
////                String filePaths = String.valueOf(beanUploadImages.get(i).getUri());
////                File sourceFile = new File(filePaths);
////                bin1 = new FileBody(sourceFile);
////                System.out.print(bin1 + "img=====");
//
////      Log.e("plaintage",plaintiffs.getText().toString());
////        Log.e("defendants",defendants.getText().toString());
////        Log.e("mdavalue",mdavalue);
////        Log.e("jsonData",jsonData);
////        Log.e("location_of_dispute",location_of_dispute);
////        Log.e("datereqmda",datereqmda.getText().toString());
////        Log.e("datebrimda",datebrimda.getText().toString());
////        Log.e("str_assignTo",str_assignTo);
//
//            new ServerRequest<UserInfoJson>(this, Constants.getCreateCaseUrl1(this, user_id,beanUploadImages, case_file_name.getText().toString(), plaintiffs.getText().toString(), defendants.getText().toString(), mdavalue, jsonData, location_of_dispute, datereqmda.getText().toString(), datebrimda.getText().toString(), assignvalue), true) {
//                @Override
//                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
//                    try {
//                        if (response.body().status) {
//                            showToast(CreateCivilCaseActivity.this, response.body().message);
//                            Log.e("output", "" + response.body().message);
//                            onBackPressed();
//                        } else {
//                            showToast(CreateCivilCaseActivity.this, response.body().errorMessage);
//                            Log.e("errrrrrrrr", "" + response.body().errorMessage);
//                        }
//                    } catch (Exception e) {
//
//                        Log.e("Exception e", "" + e.getStackTrace());
//                    }
//                }
//
//            };
//        }
//    }



    private void createCase(Context mContext, List<BeanUploadImage >beanUploadImages, String caseFileName,String plaintiffs, String defendates,String mDavlue, String caseData, String location_of_dispute, String datereqmda, String datebrimda, String assignvalue ) {

        WebServiceCaller webServiceCaller = new WebServiceCaller(WebServiceCaller.POST_TASK,this,"Please Wait Loading....",true) {
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

        entityBuilder.addTextBody("case","civil");
        entityBuilder.addTextBody("file_name",caseFileName);
        entityBuilder.addTextBody("no_of_plaintiffs",plaintiffs);
        entityBuilder.addTextBody("no_of_defendants",defendates);
        entityBuilder.addTextBody("user_id",db.getString(USER_ID));
        entityBuilder.addTextBody("mda_involved",mDavlue);
        entityBuilder.addTextBody("location_of_dispute",location_of_dispute);
        entityBuilder.addTextBody("date_requested_by_mda",datereqmda);
        entityBuilder.addTextBody("date_briefing_mda",datebrimda);
        entityBuilder.addTextBody("casedata",caseData);
        entityBuilder.addTextBody("group[]",assignvalue);

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
        webServiceCaller.execute(CreateaseApi+"token="+Constants.getUser(this).token);
    }

}
