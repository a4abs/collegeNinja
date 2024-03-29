package com.collegeninja.college.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.collegeninja.college.adapter.GradeAdapter;
import com.collegeninja.college.utils.AppConstants;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AppConstants {
    Button submit;
    EditText name, phone, email;
    private AwesomeValidation mAwesomeValidation;
    ArrayList<String> arrayList_id = new ArrayList<>();
    ArrayList<String> arrayList_name = new ArrayList<>();
    Spinner city;
    String city_id;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);

        city = findViewById(R.id.city);
        city.setOnItemSelectedListener(this);

        mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(this);

        mAwesomeValidation.addValidation(this, R.id.name, "[a-zA-Z\\s]+", R.string.err_nam);
        mAwesomeValidation.addValidation(this, R.id.phone,  Patterns.PHONE, R.string.err_phone);
        mAwesomeValidation.addValidation(this, R.id.email,  android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAwesomeValidation.validate()){
                    String _name = name.getText().toString();
                    String _phone = phone.getText().toString();
                    String _email = email.getText().toString();

                    SharedPreferences pref = getSharedPreferences("college", 0);
                    String grade_id = pref.getString("grade_id", null);
                    String stream_id = pref.getString("stream_id", null);
                    String user_type = pref.getString("user_type", null);

                    dialog = new ProgressDialog(SignUpActivity.this);

                    if(grade_id != null && stream_id != null && user_type != null) {

                        dialog.setMessage("please wait.");
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);

                        registrationCall(_name, _phone, _email, city_id, grade_id, stream_id, user_type);
                    }else{
                        Toast.makeText(SignUpActivity.this, "Valid name, mobile, and email is required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        loadCity();
    }

    private void registrationCall(final String name, final String phone, final String email, final String city_id, final String grade_id, final String stream_id,final String user_type) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = ROOT_URL+"/api/registration_step_1";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    dialog.dismiss();

                    if (success.equals("true")) {
                        Intent intent = new Intent(SignUpActivity.this, OTPGenerationActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("user_type", user_type);
                        intent.putExtra("stream", stream_id);
                        intent.putExtra("email", email);
                        intent.putExtra("mobile", phone);
                        intent.putExtra("city", city_id);
                        startActivity(intent);
                       // finish();
                    } else {
                        dialog.dismiss();

                        String data = jsonObject.getString("data");
                        Toast.makeText(SignUpActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Enter valid mobile number and email id", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.i("error response :::::: ", "" + error.getMessage());
                dialog.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", name);
                MyData.put("user_type", user_type);
                MyData.put("grade_id", grade_id);
                MyData.put("stream", stream_id);
                MyData.put("email", email);
                MyData.put("mobile", phone);
                MyData.put("city", city_id);
                return MyData;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        city_id = arrayList_id.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void loadCity() {
        String url = ROOT_URL+"/api/get_cities";
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
                            arrayList_id.add(id);
                            arrayList_name.add(name);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(SignUpActivity.this, android.R.layout.simple_spinner_item, arrayList_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city.setAdapter(adapter);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
