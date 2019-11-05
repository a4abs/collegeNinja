package com.collegeninja.college.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.App;
import com.fdscollege.college.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPGenerationActivity extends AppCompatActivity {
    String _name, _user_type, _stream, _email, _mobile, _city;
    EditText otp;
    Button submit_otp;
    TextView resend;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpgeneration);

        dialog = new ProgressDialog(OTPGenerationActivity.this);

        otp = findViewById(R.id.otp);
        submit_otp = findViewById(R.id.submit_otp);
        resend = findViewById(R.id.resend);

        _name = getIntent().getStringExtra("name");
        _user_type = getIntent().getStringExtra("user_type");
        _stream = getIntent().getStringExtra("stream");
        _email = getIntent().getStringExtra("email");
        _mobile = getIntent().getStringExtra("mobile");
        _city = getIntent().getStringExtra("city");


        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp.getText().toString().isEmpty()) {
                    Toast.makeText(OTPGenerationActivity.this, "not a valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.setMessage("please wait.");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);

                    loadOTPCall(_mobile);
                }

            }
        });

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("please wait.");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                resendotp(_mobile);
            }
        });

    }

    private void loadOTPCall(final String _phone) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = "http://collegeninja.fdstech.solutions/api/registration_step_2";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        dialog.dismiss();
                        String token = jsonObject.getJSONObject("token").getString("token");
                        SharedPreferences pref = getSharedPreferences("college", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", "Bearer " + token);
                        editor.apply();
                        App.writeUserPrefs("token", "Bearer " + token);

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(OTPGenerationActivity.this, "try again!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.i("error response :::::: ", "" + error.getMessage());
                dialog.dismiss();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("mobile", _phone);
                MyData.put("otp", otp.getText().toString());
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


    private void resendotp(final String _phone) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = "http://collegeninja.fdstech.solutions/api/resend_otp";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response :::::: ", "" + response);

                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(OTPGenerationActivity.this, "try again!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.i("error response :::::: ", "" + error.getMessage());
                dialog.dismiss();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("mobile", _phone);
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

}
