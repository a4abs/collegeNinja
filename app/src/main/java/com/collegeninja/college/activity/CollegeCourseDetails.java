package com.collegeninja.college.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.collegeninja.college.App;
import com.fdscollege.college.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CollegeCourseDetails extends BaseActivity {
    String strCourseId, strCollegeId, strCollegeName, strImageUrl, strEligibility, strDuration, strFees;
    TextView tvHeader;
    ImageView imvHeaderImage;
    TextView tvCourseName, tvEligibilityLabel, tvEligibilityValue, tvDurationLabel, tvDurationValue, tvFeesLabel, tvFeesValue;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_college_course_details, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        tvCourseName = findViewById(R.id.course_name);
        tvEligibilityLabel = findViewById(R.id.course_eligibility_label);
        tvEligibilityValue = findViewById(R.id.course_eligibility_value);
        tvDurationLabel = findViewById(R.id.course_duration_label);
        tvDurationValue = findViewById(R.id.course_duration_values);
        tvFeesLabel  = findViewById(R.id.course_fees_label);
        tvFeesValue = findViewById(R.id.course_fees_value);

        tvHeader = findViewById(R.id.header);
        imvHeaderImage = findViewById(R.id.header_image);

        strCollegeId = getIntent().getStringExtra("collegeId");
        strCourseId = getIntent().getStringExtra("courseId");

        strCollegeName = getIntent().getStringExtra("title");
        strImageUrl = getIntent().getStringExtra("image");

        tvHeader.setText(strCollegeName);

        tvCourseName.setText(strCollegeName);

        Glide.with(getApplicationContext()).load(strImageUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(imvHeaderImage);

        fetchCourseDetails(strCollegeId,strCourseId );

    }

    private void fetchCourseDetails(final String collegeId, final String courseId){
        String url = "http://ninza.fdstech.solutions/api/get_college_course_details";
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final StringRequest expInterestCollegeReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("true")){
                        JSONObject courseData = jsonObject.getJSONObject("data");
                        strEligibility = courseData.getString("eligibility");
                        strDuration = courseData.getString("duration");
                        strFees = courseData.getString("fee");

                        tvEligibilityValue.setText(strEligibility);
                        tvDurationValue.setText(strDuration);
                        tvFeesValue.setText(strFees+" Per Year");

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
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
                params.put("college_id", collegeId);
                params.put("course_id", courseId);
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(expInterestCollegeReq);
    }
}
