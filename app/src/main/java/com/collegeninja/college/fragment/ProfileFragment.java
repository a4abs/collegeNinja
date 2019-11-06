package com.collegeninja.college.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.collegeninja.college.activity.LandingActivity;
import com.collegeninja.college.activity.MainActivity;
import com.collegeninja.college.utils.VolleyMultipartRequest;
import com.fdscollege.college.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import  com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog;

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


public class ProfileFragment extends Fragment {

    Spinner city, gender, academic_status, domain;
    View view;

    ArrayList<String> city_id = new ArrayList<>();
    ArrayList<String> city_name = new ArrayList<>();

    ArrayList<String> grades_id = new ArrayList<>();
    ArrayList<String> grades_name = new ArrayList<>();

    ArrayList<String> domain_id = new ArrayList<>();
    ArrayList<String> domain_name = new ArrayList<>();

    String token = "", String, _city_id = "", _city_name = "", _grades_id = "", _grades_name = "", _domain_id = "", _domain_name = "", _gender_id = "", _gender_name = "",base64 = "";

    Button submit;

    public ProfileFragment() {
        // Required empty public constructor
    }

    EditText name, phone, email;
    TextView calender;

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
    TextView HeaderName, HeaderBatch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        city = view.findViewById(R.id.city);
        calender = view.findViewById(R.id.calender);
        gender = view.findViewById(R.id.gender);
        academic_status = view.findViewById(R.id.academic_status);
        domain = view.findViewById(R.id.domain);

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        submit = view.findViewById(R.id.submit);

        SharedPreferences pref = getActivity().getSharedPreferences("college", 0);
        token = pref.getString("token", "");
        Log.i("token :::::: ", "" + token);


        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        loadCity();

        btn = (Button) view.findViewById(R.id.btn);
        imgViewProfile = (CircleImageView) view.findViewById(R.id.profile_image);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkConnected()) {
                    String p_name = name.getText().toString();
                    String p_phone = phone.getText().toString();
                    String p_email = email.getText().toString();
                    String dob = calender.getText().toString();

                    /*dialog.setMessage("please wait.");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);*/

                    ((MainActivity)getActivity()).updateProfile(p_name, p_phone, p_email, _city_id,_gender_id, dayOfMonth,month, year, _grades_id, _domain_id);
                    //updateProfile(p_name, p_phone, p_email);
                } else {
                    Toast.makeText(getActivity(), "check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    _city_id = city_id.get(position);
                    _city_name = city_name.get(position);

                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        academic_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    _grades_id = grades_id.get(position);
                    _grades_name = grades_name.get(position);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    _domain_id = domain_id.get(position);
                    _domain_name = domain_name.get(position);

                } catch (Exception exc) {
                    exc.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = gender.getSelectedItem().toString();

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

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int initialYear = calendar.get(Calendar.YEAR);
                int initialMonth = calendar.get(Calendar.MONTH);
                int initialDay = calendar.get(Calendar.DAY_OF_MONTH);
                new SupportedDatePickerDialog(getActivity(), R.style.SpinnerDatePickerDialogTheme, new SupportedDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(@NotNull DatePicker datePicker, int yr, int mnth, int day) {
                        dayOfMonth = day;
                        year = yr;
                        month = mnth+1;
                        calender.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, initialYear, initialMonth, initialDay).show();
                /*datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                calender.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                datePickerDialog.show();*/
            }
        });

        if (!isNetworkConnected()) {
            Toast.makeText(getActivity(), "check your internet connection and try again!", Toast.LENGTH_SHORT).show();
        }

        requestMultiplePermissions();

        return view;
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void loadCity() {
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
                            city_id.add(id);
                            city_name.add(name);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, city_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city.setAdapter(adapter);

                        loadGradeData();

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    void loadGradeData() {
        String url = "http://collegeninja.fdstech.solutions/api/get_grades";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

                            grades_id.add(id);
                            grades_name.add(name);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, grades_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        academic_status.setAdapter(adapter);

                        loadDomain();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    void loadDomain() {
        String url = "http://collegeninja.fdstech.solutions/api/get_domains";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response","==>"+response);
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");

                            domain_id.add(id);
                            domain_name.add(name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, domain_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        domain.setAdapter(adapter);

                        loadProfileData();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", token);
                return params;
            }
        };;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadProfileData() {
        String url = "http://collegeninja.fdstech.solutions/api/get_user_pref";
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
                        String pref_domain_name = _jsonObject.getString("pref_domain_name");
                        Log.d("pref_domain_name","==>"+pref_domain_name);
                        App.writeUserPrefs("uName", _name);
                        App.writeUserPrefs("batch", _academic_status+"/"+pref_domain_name);
                        App.writeUserPrefs("profilePic", _image);

                        ((MainActivity) Objects.requireNonNull(getActivity())).upDateDrawerHeader();

                        name.setText(_name);
                        phone.setText(_mobile);
                        email.setText(_email);

                        year = dob_year;
                        month = dob_month;
                        dayOfMonth = dob_day;

                        if(user_image != null){
                            byte[] decodedString = Base64.decode(user_image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgViewProfile.setImageBitmap(decodedByte);
                        } else {
                            imgViewProfile.setImageResource(R.drawable.upload_profile_pic);
                        }

                        if (dob_day != 0) {
                            calender.setText(dob_day + "/" + dob_month + "/" + dob_year);
                        } else {
                            calender.setText("DOB");
                        }

                        int city_pos = city_id.indexOf(""+_city_id);
                        city.setSelection(city_pos);

                        int academic_status_pos = grades_id.indexOf(""+_academic_status_id);
                        academic_status.setSelection(academic_status_pos);

                        int domain_pos = domain_id.indexOf(""+_domain_id);
                        domain.setSelection(domain_pos);


                        try {
                            int _gender_id = _jsonObject.getInt("gender_id");

                            if (_gender_id == 1) {
                                gender.setSelection(1);
                            } else if (_gender_id == 2) {
                                gender.setSelection(2);
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

                        name.setText(_name);
                        phone.setText(_mobile);
                        email.setText(_email);
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
                params.put("Authorization", token);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void updateProfile(final String p_name, final String p_phone, final String p_email) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());

        String url = "http://collegeninja.fdstech.solutions/api/update_user_profile";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("true")) {
                        Toast.makeText(getActivity(), "profile updated successfully", Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", token);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", p_name);
                MyData.put("mobile", p_phone);
                MyData.put("email", p_email);
                MyData.put("city", _city_id);
                MyData.put("gender", _gender_id);
                MyData.put("dob_day", String.valueOf(dayOfMonth));
                MyData.put("dob_month", String.valueOf(month));
                MyData.put("dob_year", String.valueOf(year));
                MyData.put("academic_status", _grades_id);
                MyData.put("domain", _domain_id);
                MyData.put("user_image", "");
                return MyData;
            }
        };

        MyRequestQueue.add(request);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
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
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String imageFile = saveImage(bitmap);
                    base64 = encodeTobase64(bitmap);

                    Toast.makeText(getActivity(),"Image Saved!", Toast.LENGTH_SHORT).show();
                    imgViewProfile.setImageBitmap(bitmap);

                    uploadProfilePic(base64, bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgViewProfile.setImageBitmap(thumbnail);
            String imageFile = saveImage(thumbnail);
            base64 = encodeTobase64(thumbnail);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
            uploadProfilePic(base64, thumbnail);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadProfilePic(final String base64, final Bitmap imageFile){
        dialog = new ProgressDialog(getActivity());

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
                loadProfileData();
                Log.d("Response","====>"+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Log.d("====>", "=="+error);
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", token);
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
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }
    private void loadPic(final String base64, File imageFile) {
        dialog = new ProgressDialog(getActivity());

        dialog.setMessage("please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());

        String url = "http://collegeninja.fdstech.solutions/api/update_user_profile_pic";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Log.d("Response","===>"+response);

//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//
//                    if (success.equals("true")) {
//                        Toast.makeText(getActivity(), "profile updated successfully", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error:ImageUpload", "==>"+error);
                dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", token);
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
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(), new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("imageEncoded :::::: ", "" + imageEncoded);
        return imageEncoded;
    }
}
