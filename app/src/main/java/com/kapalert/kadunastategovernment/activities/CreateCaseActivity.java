package com.kapalert.kadunastategovernment.activities;

import android.app.DatePickerDialog;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.adapters.AdapterAccusedList;
import com.kapalert.kadunastategovernment.adapters.AdapterOffenceNatureList;
import com.kapalert.kadunastategovernment.adapters.AdapterOffenceSpinner;
import com.kapalert.kadunastategovernment.templates.CreateCaseJSON;
import com.kapalert.kadunastategovernment.templates.OffenceModal;
import com.kapalert.kadunastategovernment.templates.OffencePOJO;
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
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class CreateCaseActivity extends AppBaseActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView mAccuseds;
    private Button mAddAccused, mSubmit;

    private ArrayList<AccusedModal> accusedList;

    private AdapterAccusedList adapterAccusedList;
    private ImageView mFileSelect;
    private EditText mRefNum, mFileName;
    private long mDays;
    private TextView mOffenceDate, mPoliceFileDate, mArrestDate, mOffNature;
    private Spinner mOffLocation, mOffInvestigation;
    private File mFile;
    private File destination;
    private static final int PERMISSION_REQUEST_CODE = 36;
    private static final int PICK_FILE = 28;

    private ArrayList<OffenceModal> mOffenceNature, mOffenceInvest, mOffenceLoc;
    private Calendar mDateOffence;
    private String mInvestigationID = "", mLocationID = "";
    private ArrayList<String> mSelectedNatures = new ArrayList<>();

    Spinner genderspinner;
    private String[] state = { "Male", "Female", "Not Applicable"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_case);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.showBackButton(mContext, toolbar);

        getOffenceNatureList();
    }

    private void getOffenceNatureList() {
        new ServerRequest<OffencePOJO>(mContext, Constants.getNatureOffenceUrl(mContext), true) {
            @Override
            public void onCompletion(Call<OffencePOJO> call, Response<OffencePOJO> response) {
                if (response.body().offenceList != null) {
                    mOffenceNature = new ArrayList<>(response.body().offenceList);
                } else {
                    mOffenceNature = new ArrayList<>();
                }
                getInvestigationList();
            }
        };
    }

    private void getInvestigationList() {
        new ServerRequest<OffencePOJO>(mContext, Constants.getInvestigationOffenceUrl(mContext), true) {
            @Override
            public void onCompletion(Call<OffencePOJO> call, Response<OffencePOJO> response) {
                if (response.body().offenceInvestList != null) {
                    mOffenceInvest = new ArrayList<>(response.body().offenceInvestList);
                } else {
                    mOffenceInvest = new ArrayList<>();
                }
                getLocationList();
            }
        };
    }

    private void getLocationList() {
        new ServerRequest<OffencePOJO>(mContext, Constants.getLocOffenceUrl(mContext), true) {
            @Override
            public void onCompletion(Call<OffencePOJO> call, Response<OffencePOJO> response) {
                if (response.body().offenceLocationList != null) {
                    mOffenceLoc = new ArrayList<>(response.body().offenceLocationList);
                } else {
                    mOffenceLoc = new ArrayList<>();
                }
                setupViews();
            }
        };
    }

    private void setupViews() {
        mAccuseds = (RecyclerView) findViewById(R.id.accused_list);

        mAddAccused = (Button) findViewById(R.id.add_accused);
        mSubmit = (Button) findViewById(R.id.submit);
        mRefNum = (EditText) findViewById(R.id.case_ref);
        mFileSelect = (ImageView) findViewById(R.id.case_file_select);
        mOffNature = (TextView) findViewById(R.id.offence_nature);
        mOffLocation = (Spinner) findViewById(R.id.offence_location);
        mOffInvestigation = (Spinner) findViewById(R.id.offence_investigation);
        mOffenceDate = (TextView) findViewById(R.id.offence_date);
        mFileName = (EditText) findViewById(R.id.case_file_name);
        mArrestDate = (TextView) findViewById(R.id.first_arrest_date);
        mPoliceFileDate = (TextView) findViewById(R.id.police_received_date);

        setupList();
        setupDatePickers();
        setupSpinners();
        mAddAccused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccusedDialog();
            }
        });
        mFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFileClick();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClick();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        genderspinner.setSelection(position);
        String selState = (String) genderspinner.getSelectedItem();
       // Toast.makeText(mContext, ""+selState, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private void submitClick() {
        if (allFieldsValid()) {
            String jsonData = new Gson().toJson(new CreateCaseJSON(accusedList, mSelectedNatures));
            new ServerRequest<UserInfoJson>(mContext, Constants.getCreateCaseUrl(mContext, mFile, mFileName.getText().toString(), mOffenceDate.getText().toString(), mLocationID, mRefNum.getText().toString(), mInvestigationID, mPoliceFileDate.getText().toString(), "" + mDays, mArrestDate.getText().toString(), jsonData), true) {
                @Override
                public void onCompletion(Call<UserInfoJson> call, Response<UserInfoJson> response) {
                    if (response.body().status) {
                        Utils.showToast(mContext, response.body().message);
                        onBackPressed();
                    } else {
                        Utils.showToast(mContext, response.body().errorMessage);
                    }
                }
            };
        }
    }

    private boolean allFieldsValid() {

        if (mFile == null) {
            Utils.showToast(mContext, getString(R.string.invalid_file_selected));
            return false;
        }

        if (mFileName.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.invalid_file_name));
            return false;
        }

        if (accusedList == null || accusedList.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.no_accused_error));
            return false;
        }
        if (mRefNum.getText().toString().isEmpty() /*|| !mRefNum.getText().toString().matches("[0-9][0-9][0-9]/[0-9][0-9][0-9]")*/) {
            Utils.showToast(mContext, getString(R.string.ref_num_error));
            return false;
        }
        if (mOffenceDate.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.offence_date_error));
            return false;
        }
        if (mPoliceFileDate.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.police_file_date_error));
            return false;
        }
        if (mArrestDate.getText().toString().isEmpty()) {
            Utils.showToast(mContext, getString(R.string.arrest_date_error));
            return false;
        }
        if (mSelectedNatures == null || mSelectedNatures.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.nature_select_error));
            return false;
        }
        if (mLocationID.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.location_select_error));
            return false;
        }
        if (mInvestigationID.isEmpty()) {
            Utils.showToast(mContext, getString(R.string.investigation_select_error));
            return false;
        }
        return true;
    }

    private void setupDatePickers() {
        mOffenceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
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
                        mOffenceDate.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        mPoliceFileDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDateOffence == null) {
                    Utils.showToast(mContext, getString(R.string.select_offence_date_first));
                    return;
                }
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        mPoliceFileDate.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        mArrestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDateOffence == null) {
                    Utils.showToast(mContext, getString(R.string.select_offence_date_first));
                    return;
                }
                Calendar cal = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
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

                        long difference = selectedDate.getTime().getTime() - mDateOffence.getTime().getTime();

                        mDays = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
                        if (mDays < 0) {
                            Utils.showToast(mContext, getString(R.string.invalid_offence_date));
                            return;
                        }

                        if (mDays == 0) {
                            mDays = 1;
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        mArrestDate.setText(dateFormat.format(selectedDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private void setupSpinners() {

        mOffNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOffenceNature();
            }
        });

        AdapterOffenceSpinner adapterOffenceInvestigationSpinner = new AdapterOffenceSpinner(mContext, mOffenceInvest, getString(R.string.select_offence_investigation));
        mOffInvestigation.setAdapter(adapterOffenceInvestigationSpinner);
        mOffInvestigation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mInvestigationID = mOffenceInvest.get(position).getId();
                } else {
                    mInvestigationID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        AdapterOffenceSpinner adapterOffenceLocationSpinner = new AdapterOffenceSpinner(mContext, mOffenceLoc, getString(R.string.select_offence_location));
        mOffLocation.setAdapter(adapterOffenceLocationSpinner);
        mOffLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mLocationID = mOffenceLoc.get(position).getId();
                } else {
                    mLocationID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void selectOffenceNature() {
        final Dialog dialog = new Dialog(mContext);
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
                for (OffenceModal offenceModal : mOffenceNature) {
                    if (offenceModal.isSelected())
                        mSelectedNatures.add(offenceModal.getId());
                }
                mOffNature.setText("" + mSelectedNatures.size());
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
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        offenceNatureList.setLayoutManager(manager);

        AdapterOffenceNatureList adapterOffenceNatureList = new AdapterOffenceNatureList(mContext, mOffenceNature);
        offenceNatureList.setAdapter(adapterOffenceNatureList);

    }

    private void addAccusedDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_plaintiffs);

        final EditText accusedName = (EditText) dialog.findViewById(R.id.accused_name);
        final EditText accusedAge = (EditText) dialog.findViewById(R.id.accused_age);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.update);
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
                String crimgen=genderspinner.getSelectedItem().toString();

                if (!name.isEmpty() && !age.isEmpty()) {
                    accusedList.add(new AccusedModal(name, age,crimgen));
                    adapterAccusedList.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Utils.showToast(mContext, getString(R.string.fields_cannot_left_blank));
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
        mAccuseds.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAccuseds.setLayoutManager(manager);

        accusedList = new ArrayList<>();

        adapterAccusedList = new AdapterAccusedList(mContext, accusedList);
        mAccuseds.setAdapter(adapterAccusedList);
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
                Utils.showToast(mContext, getString(R.string.permission_error));
            }
        }
    }

    public void gotoGallery() {
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
            }
        } else {
            Utils.showToast(mContext, getString(R.string.failed_to_fetch));
        }
    }

    public class AccusedModal {
        String name, age;
        String crimgen;

        public AccusedModal(String name, String age, String crimgen) {
            this.name = name;
            this.age = age;
            this.crimgen=crimgen;

        }

        public String getCrimgen() {
            return crimgen;
        }

        public void setCrimgen(String crimgen) {
            this.crimgen = crimgen;
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

}
