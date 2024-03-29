package com.collegeninja.college.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.collegeninja.college.adapter.CourseAdapter;
import com.collegeninja.college.adapter.CourseDetailAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.collegeninja.college.model.CourseColleges;
import com.collegeninja.college.utils.AppConstants;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailActivity extends BaseActivity implements AppConstants {

    RecyclerView coursedetail;
    String id, name;
    String token,image,description;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    TextView header;

    ImageView header_image;
    TextView _desc, tvCourseSectionTitle;
    String _header_image,_title,_description;
    List<CourseColleges> courseColleges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_course_detail, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
        /*setContentView(R.layout.activity_course_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/


        coursedetail = findViewById(R.id.coursedetail);
        header = findViewById(R.id.header);
        _desc = findViewById(R.id.description);
        header_image = findViewById(R.id.header_image);
        tvCourseSectionTitle =findViewById(R.id.course_section_title);
        tvCourseSectionTitle.setText("");

        id = getIntent().getStringExtra("id");
        _title = getIntent().getStringExtra("title");
        _header_image = getIntent().getStringExtra("image");
        _description = getIntent().getStringExtra("description");


        header.setText(_title);
        _desc.setText(_description);

        Glide.with(getApplicationContext()).load(_header_image).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                //holder.progress.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(header_image);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        coursedetail.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        coursedetail.addItemDecoration(itemDecoration);

        loadCourseDetail(id);

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    private void loadCourseDetail(final String id) {
        String url = ROOT_URL+"/api/get_courses/"+id;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Couse","===>"+jsonObject);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {

                        JSONArray courses = jsonObject.getJSONArray("data");
                        if(courses.length() > 0){
                            tvCourseSectionTitle.setText("Course Offered in "+_title);
                        } else {
                            tvCourseSectionTitle.setText("");
                        }

                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject _jsonObject = courses.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<>();
                            //courseColleges = new ArrayList<>();
                            String courseId = _jsonObject.getString("id");
                            String courseName = _jsonObject.getString("name");
                            String course_img = _jsonObject.getString("course_img");
                            JSONArray colleges = _jsonObject.getJSONArray("colleges");

                            //courseColleges.add(new CourseColleges(id, name, _title, course_img, colleges))
                            map.put("courseId", courseId);
                            map.put("collegeId", id);
                            map.put("collegeName", name);
                            map.put("courseName", courseName);
                            map.put("thumb_img", course_img);
                            map.put("domain", _title);
                            map.put("colleges", colleges.toString());

                            arrayList.add(map);
                        }

                        CourseDetailAdapter adapter = new CourseDetailAdapter(getApplicationContext(), arrayList);
                        coursedetail.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("error is ", "" + error.getMessage());
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

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
