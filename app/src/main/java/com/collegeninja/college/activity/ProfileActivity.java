package com.collegeninja.college.activity;

import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.App;
import com.collegeninja.college.utils.VolleyMultipartRequest;
import com.fdscollege.college.R;
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {

    Spinner spinnerCity, spinnerGender, spinnerAcademicStatus, spinnerDomain;
    View view;

    ArrayList<String> arrayListCityIds = new ArrayList<>();
    ArrayList<String> arrayListCityName = new ArrayList<>();

    ArrayList<String> arrayListGradeIds = new ArrayList<>();
    ArrayList<String> arrayListGradeName = new ArrayList<>();

    ArrayList<String> arrayListDomainIds = new ArrayList<>();
    ArrayList<String> arrayListDomainName = new ArrayList<>();

    String _city_id = "", _city_name = "", _grades_id = "", _grades_name = "", _domain_id = "", _domain_name = "", _gender_id = "", _gender_name = "", base64 = "";

    Button buttonSubmit;

    EditText editTextName, editTextPhone, editTextEmail;
    TextView tvCalender;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private ProgressDialog dialog;

    private Button btn;
    private CircleImageView imgViewProfile;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_profile, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
        bottomNavigation.show(4, false);

        spinnerCity = contentView.findViewById(R.id.city);
        tvCalender = contentView.findViewById(R.id.calender);
        spinnerGender = contentView.findViewById(R.id.gender);
        spinnerAcademicStatus = contentView.findViewById(R.id.academic_status);
        spinnerDomain = contentView.findViewById(R.id.domain);

        editTextName = contentView.findViewById(R.id.name);
        editTextPhone = contentView.findViewById(R.id.phone);
        editTextEmail = contentView.findViewById(R.id.email);
        buttonSubmit = contentView.findViewById(R.id.submit);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        fetchCity();

        btn = contentView.findViewById(R.id.btn);
        imgViewProfile = contentView.findViewById(R.id.profile_image);

        imgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkConnected()) {
                    String p_name = editTextName.getText().toString();
                    String p_phone = editTextPhone.getText().toString();
                    String p_email = editTextPhone.getText().toString();
                    String dob = tvCalender.getText().toString();

                    /*dialog.setMessage("please wait.");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);*/

                    updateProfile(p_name, p_phone, p_email, _city_id, _gender_id, dayOfMonth, month, year, _grades_id, _domain_id);
                } else {
                    Toast.makeText(getApplicationContext(), "check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    _city_id = arrayListCityIds.get(position);
                    _city_name = arrayListCityName.get(position);

                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAcademicStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    _grades_id = arrayListGradeIds.get(position);
                    _grades_name = arrayListGradeName.get(position);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDomain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    _domain_id = arrayListDomainIds.get(position);
                    _domain_name = arrayListDomainName.get(position);

                   // spinnerDomain.setSelection(position);

                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = spinnerGender.getSelectedItem().toString();

                try {
                    if (val.equals("male")) {
                        _gender_id = "1";
                        _gender_name = "Male";
                    } else {
                        _gender_id = "2";
                        _gender_name = "Female";
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int initialYear = calendar.get(Calendar.YEAR);
                int initialMonth = calendar.get(Calendar.MONTH);
                int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
                new SupportedDatePickerDialog(ProfileActivity.this, R.style.SpinnerDatePickerDialogTheme, new SupportedDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(@NotNull DatePicker datePicker, int yr, int mnth, int day) {
                        dayOfMonth = day;
                        year = yr;
                        month = mnth + 1;
                        tvCalender.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, initialYear, initialMonth, initialDay).show();
            }
        });

        if (!isNetworkConnected()) {
            Toast.makeText(this, "check your internet connection and try again!", Toast.LENGTH_SHORT).show();
        }

        requestMultiplePermissions();


    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void fetchCity() {
        String url = "http://collegeninja.fdstech.solutions/api/get_cities";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");
                            arrayListCityIds.add(id);
                            arrayListCityName.add(name);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arrayListCityName);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinnerCity.setAdapter(adapter);

                        fetchGrades();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void fetchGrades() {
        String url = "http://collegeninja.fdstech.solutions/api/get_grades";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");

                            arrayListGradeIds.add(id);
                            arrayListGradeName.add(name);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arrayListGradeName);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinnerAcademicStatus.setAdapter(adapter);

                        fetchDomain();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void fetchDomain() {
        String url = "http://collegeninja.fdstech.solutions/api/get_streams";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");

                            arrayListDomainIds.add(id);
                            arrayListDomainName.add(name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arrayListDomainName);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinnerDomain.setAdapter(adapter);

                        fetchProfileData();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchProfileData() {
        String url = "http://collegeninja.fdstech.solutions/api/get_profile";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject _jsonObject = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        dialog.dismiss();
                        _jsonObject = jsonObject.getJSONObject("data");

                        Log.i("profile ::::", "" + _jsonObject);

                        String _id = _jsonObject.getString("id");
                        String _name = _jsonObject.getString("name");
                        String _email = _jsonObject.getString("email");
                        String _mobile = _jsonObject.getString("mobile");
                        int _city_id = _jsonObject.getInt("city_id");
                        String _city = _jsonObject.getString("city");
                        String _gender = _jsonObject.getString("gender");
                        int dob_day = _jsonObject.getInt("dob_day");
                        int dob_month = _jsonObject.getInt("dob_month");
                        int dob_year = _jsonObject.getInt("dob_year");
                        int _academic_status_id = _jsonObject.getInt("academic_status_id");
                        String _academic_status = _jsonObject.getString("academic_status");
                        int _domain_id = _jsonObject.getInt("domain_id");
                        String _domain = _jsonObject.getString("domain");
                        String user_image = _jsonObject.getString("profile_pic");
                        String user_image_path = _jsonObject.getString("user_image_path");
                        String _image = _jsonObject.getString("profile_pic");

                        App.writeUserPrefs("uName", _name);
                        App.writeUserPrefs("batch", _academic_status + "/" + _domain);
                        App.writeUserPrefs("profilePic", _image);

                        upDateDrawerHeader();

                        editTextName.setText(_name);
                        editTextPhone.setText(_mobile);
                        editTextEmail.setText(_email);

                        year = dob_year;
                        month = dob_month;
                        dayOfMonth = dob_day;

                        if (user_image != null) {
                            byte[] decodedString = Base64.decode(user_image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgViewProfile.setImageBitmap(decodedByte);
                        } else {
                            imgViewProfile.setImageResource(R.drawable.upload_profile_pic);
                        }

                        if (dob_day != 0) {
                            tvCalender.setText(dob_day + "/" + dob_month + "/" + dob_year);
                        } else {
                            tvCalender.setText("DOB");
                        }

                        int city_pos = arrayListCityIds.indexOf("" + _city_id);
                        spinnerCity.setSelection(city_pos);

                        int academic_status_pos = arrayListGradeIds.indexOf("" + _academic_status_id);
                        spinnerAcademicStatus.setSelection(academic_status_pos);

                        int domain_pos = arrayListDomainIds.indexOf("" + _domain_id);
                        spinnerDomain.setSelection(domain_pos);
                        Log.d("===>","==="+spinnerDomain.getSelectedItem());

                        try {
                            int _gender_id = _jsonObject.getInt("gender_id");

                            if (_gender_id == 1) {
                                spinnerGender.setSelection(1);
                            } else if (_gender_id == 2) {
                                spinnerGender.setSelection(2);
                            } else {
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();

                    try {
                        String _name = _jsonObject.getString("name");
                        String _email = _jsonObject.getString("email");
                        String _mobile = _jsonObject.getString("mobile");

                        editTextName.setText(_name);
                        editTextPhone.setText(_mobile);
                        editTextEmail.setText(_email);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", App.readUserPrefs("token"));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String imageFile = saveImage(bitmap);
                    base64 = encodeTobase64(bitmap);

                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imgViewProfile.setImageBitmap(bitmap);

                    uploadProfilePic(base64, bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgViewProfile.setImageBitmap(thumbnail);
            String imageFile = saveImage(thumbnail);
            base64 = encodeTobase64(thumbnail);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            uploadProfilePic(base64, thumbnail);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadProfilePic(final String base64, final Bitmap imageFile) {
        dialog = new ProgressDialog(this);

        dialog.setMessage("please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageFile.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        String url = "http://collegeninja.fdstech.solutions/api/update_user_profile_pic";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                dialog.dismiss();
                fetchProfileData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", App.readUserPrefs("token"));
                return params;
            }

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("profile_pic", new DataPart(imagename + ".png", getFileDataFromDrawable(imageFile)));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void loadPic(final String base64, File imageFile) {
        dialog = new ProgressDialog(this);

        dialog.setMessage("please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = "http://collegeninja.fdstech.solutions/api/update_user_profile_pic";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error:ImageUpload", "==>" + error);
                dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", App.readUserPrefs("token"));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("profile_pic", base64);
                return MyData;
            }
        };

        MyRequestQueue.add(request);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


}
