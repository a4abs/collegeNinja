package com.collegeninja.college.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collegeninja.college.adapter.CourseCollegeAdaptor;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegesActivity extends BaseActivity {

    private String courseId, courseName, domainName,strColleges;

    private TextView activityHeading;
    private RecyclerView rvCourseColleges;
    ArrayList<HashMap<String, String>> arrayListColleges = new ArrayList<>();
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_colleges, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
        /*setContentView(R.layout.activity_colleges);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        activityHeading = findViewById(R.id.header);
        rvCourseColleges = findViewById(R.id.course_colleges);

        courseId = getIntent().getStringExtra("courseId");
        courseName = getIntent().getStringExtra("courseName");
        domainName = getIntent().getStringExtra("domain");
        strColleges = getIntent().getStringExtra("colleges");
        if(domainName.isEmpty()){
            activityHeading.setText("Colleges offering "+courseName);
        } else {
            activityHeading.setText("Colleges offering "+courseName+" in "+domainName);
        }


        SharedPreferences pref = getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        rvCourseColleges.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvCourseColleges.addItemDecoration(itemDecoration);

        try {
            JSONArray arrayColleges = new JSONArray(strColleges);
            for(int i = 0; i < arrayColleges.length(); i++) {
                JSONObject _jsonObject = arrayColleges.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                String collegeId = _jsonObject.getString("id");
                String name = _jsonObject.getString("name");
                String description = _jsonObject.getString("description");
                map.put("courseId", courseId);
                map.put("collegeId", collegeId);
                map.put("collegeName", name);
                map.put("courseName", courseName);


                arrayListColleges.add(map);
            }

            CourseCollegeAdaptor courseCollegeAdaptor = new CourseCollegeAdaptor(this, arrayListColleges);
            rvCourseColleges.setAdapter(courseCollegeAdaptor);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
