package com.collegeninja.college.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.collegeninja.college.adapter.CourseCollegeAdaptor;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegesActivity extends AppCompatActivity {

    private String id, courseName, domainName,strColleges;

    private TextView activityHeading;
    private RecyclerView rvCourseColleges;
    ArrayList<HashMap<String, String>> arrayListColleges = new ArrayList<>();
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleges);
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
        });

        activityHeading = findViewById(R.id.header);
        rvCourseColleges = findViewById(R.id.course_colleges);

        id = getIntent().getStringExtra("id");
        courseName = getIntent().getStringExtra("title");
        domainName = getIntent().getStringExtra("domain");
        strColleges = getIntent().getStringExtra("colleges");

        activityHeading.setText("Colleges offering "+courseName+" in "+domainName);

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
                String id = _jsonObject.getString("id");
                String name = _jsonObject.getString("name");
                String description = _jsonObject.getString("description");
                map.put("id", id);
                map.put("name", name);
                map.put("description", description);

                arrayListColleges.add(map);
            }

            CourseCollegeAdaptor courseCollegeAdaptor = new CourseCollegeAdaptor(this, arrayListColleges);
            rvCourseColleges.setAdapter(courseCollegeAdaptor);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
