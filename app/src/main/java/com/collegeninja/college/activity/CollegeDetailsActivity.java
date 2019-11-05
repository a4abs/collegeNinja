package com.collegeninja.college.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import com.collegeninja.college.App;
import com.collegeninja.college.adapter.CollegeFeatureAdapter;
import com.collegeninja.college.adapter.CollegeImagesAdaptor;
import com.collegeninja.college.adapter.CollegeVideoAdaptor;
import com.collegeninja.college.adapter.CourseDetailAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.collegeninja.college.model.Videos;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollegeDetailsActivity extends AppCompatActivity {

    String _id, _name, _description, _thumb_img;
    ImageView header_image;
    TextView tvTitle, tvDescription, tvBrochure, tvContact, tvExpressInterest;
    RecyclerView rvCourseOffered, rvFeatures, rvGallery, rvVideos;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList_feature = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListImages = new ArrayList<>();
    List<Videos> videosList;

    SliderLayout sliderLayout;
    HashMap<String,String> hashFileMap ;
    String token = "";
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_details);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("College Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
        _id = getIntent().getStringExtra("id");
        loadCollegeDetails(_id);

        header_image = findViewById(R.id.header_image);
        rvCourseOffered = findViewById(R.id.course_offered);
        rvFeatures = findViewById(R.id.feature);
        rvGallery = findViewById(R.id.gallery);
        rvVideos = findViewById(R.id.videos);
        tvTitle = findViewById(R.id.title);
        tvDescription = findViewById(R.id.description);
        tvBrochure = findViewById(R.id.brochure);
        tvContact = findViewById(R.id.contact);
        sliderLayout = findViewById(R.id.slider);
        tvExpressInterest = findViewById(R.id.express_interest);
        tvExpressInterest.setText("EXPRESS INTEREST");

        tvExpressInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvExpressInterest.getText().toString().equalsIgnoreCase("THANKS FOR EXPRESSING YOUR INTEREST")){
                    expressInterest(_id, "remove");
                } else {
                    expressInterest(_id, "add");
                }
            }
        });

        rvCourseOffered.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvCourseOffered.addItemDecoration(itemDecoration);

        rvFeatures.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration _itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvFeatures.addItemDecoration(_itemDecoration);

        rvGallery.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecorationGallery = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvGallery.addItemDecoration(itemDecorationGallery);

        rvVideos.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecorationVideos = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvVideos.addItemDecoration(itemDecorationVideos);
    }

    public void expressInterest(final String collegeId, final String expressInterest) {
        String url = "http://collegeninja.fdstech.solutions/api/express_interest_to_college";

        final StringRequest expInterestCollegeReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("true")){
                        JSONObject collegeData = jsonObject.getJSONObject("data");
                        boolean currentUserInterested = collegeData.getBoolean("current_user_interested");
                        if(currentUserInterested) {
                            tvExpressInterest.setText("THANKS FOR EXPRESSING YOUR INTEREST");
                        } else {
                            tvExpressInterest.setText("EXPRESS INTEREST");
                        }
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
                params.put("Authorization", App.readUserPrefs("token"));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("college_id", collegeId);
                params.put("action", expressInterest);
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(expInterestCollegeReq);

    }

    public void loadCollegeDetails(String collegeId) {
        String url = "http://collegeninja.fdstech.solutions/api/get_college_details/"+collegeId;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("true")){
                        JSONObject collegeData = jsonObject.getJSONObject("data");

                        HashMap<String, String> map = new HashMap<>();
                        String id = collegeData.getString("id");
                        String name = collegeData.getString("name");
                        String description = collegeData.getString("description");
                        boolean currentUserInterested = collegeData.getBoolean("current_user_interested");
                        final String brochure = collegeData.getString("brochure");
                        final String contact = collegeData.getString("contact");

                        JSONArray features = collegeData.getJSONArray("features");
                        JSONArray courses = collegeData.getJSONArray("courses");
                        JSONArray videos = collegeData.getJSONArray("videos");
                        JSONArray images = collegeData.getJSONArray("images");

                        if(currentUserInterested) {
                            tvExpressInterest.setText("THANKS FOR EXPRESSING YOUR INTEREST");
                        } else {
                            tvExpressInterest.setText("EXPRESS INTEREST");
                        }

                        tvTitle.setText(name);
                        tvDescription.setText(Html.fromHtml(description));

                        if(!contact.isEmpty() && contact != null){
                            tvContact.setText("Call: " + contact);
                        }else{
                            tvContact.setVisibility(View.GONE);
                        }
                        videosList = new ArrayList<>();
                        for (int v = 0; v< videos.length(); v++){
                            JSONObject videoObject = videos.getJSONObject(v);
                            if(!videoObject.getString("youtube_url").equalsIgnoreCase("null")) {
                                videosList.add(new Videos(videoObject.getInt("id"), videoObject.getString("video"), videoObject.getString("video_path"), videoObject.getString("youtube_url")));
                            }
                        }

                        CollegeVideoAdaptor collegeVideoAdaptor = new CollegeVideoAdaptor(CollegeDetailsActivity.this, videosList, getApplicationContext());
                        rvVideos.setAdapter(collegeVideoAdaptor);

                        tvContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                callIntent.setData(Uri.parse("tel:" + contact));
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                getApplicationContext().startActivity(callIntent);
                            }
                        });

                        tvBrochure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(brochure));
                                startActivity(browserIntent);
                            }
                        });

                        // COLLEGE FEATURES
                        try {
                            for (int i = 0; i < features.length(); i++) {
                                JSONObject _jsonObject = features.getJSONObject(i);
                                HashMap<String, String> featureMap = new HashMap<>();
                                String featureId = _jsonObject.getString("id");
                                String featureName = _jsonObject.getString("name");
                                String featureLogo = _jsonObject.getString("logo");
                                String featureDescription = _jsonObject.getString("description");
                                featureMap.put("id", featureId);
                                featureMap.put("name", featureName);
                                featureMap.put("logo", featureLogo);
                                featureMap.put("description", featureDescription);

                                arrayList_feature.add(featureMap);
                            }

                            CollegeFeatureAdapter adapter = new CollegeFeatureAdapter(getApplicationContext(), arrayList_feature);
                            rvFeatures.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // POPULATE GALLERY
                        try {
                            for (int i = 0; i < images.length(); i++) {
                                JSONObject imagesObj = images.getJSONObject(i);
                                HashMap<String, String> imagesMap = new HashMap<>();
                                String imgId = imagesObj.getString("id");
                                String imgName = imagesObj.getString("image");
                                String imgPath = imagesObj.getString("img_path");

                                imagesMap.put("id", imgId);
                                imagesMap.put("name", imgName);
                                imagesMap.put("img_path", imgPath);

                                arrayListImages.add(imagesMap);
                            }

                            CollegeImagesAdaptor adapter = new CollegeImagesAdaptor(getApplicationContext(), CollegeDetailsActivity.this, arrayListImages);
                            rvGallery.setAdapter(adapter);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        // COURSES
                        try {
                            for (int i = 0; i < courses.length(); i++) {
                                JSONObject _jsonObject = courses.getJSONObject(i);
                                HashMap<String, String> coursesMap = new HashMap<>();
                                String courseId = _jsonObject.getString("id");
                                String courseName = _jsonObject.getString("name");
                                String courseImg = _jsonObject.getString("course_img");
                                String colleges = _jsonObject.getJSONArray("colleges").toString();

                                coursesMap.put("id", courseId);
                                coursesMap.put("name", courseName);
                                coursesMap.put("thumb_img", courseImg);
                                coursesMap.put("colleges", colleges);
                                coursesMap.put("domain", "");
                                coursesMap.put("image", null);

                                arrayList.add(coursesMap);
                            }

                            CourseDetailAdapter adapter = new CourseDetailAdapter(getApplicationContext(), arrayList);
                            rvCourseOffered.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // SLIDER
                        try {
                            hashFileMap = new HashMap<>();
                            for(int i = 0; i < images.length(); i++){
                                JSONObject _jsonObject = images.getJSONObject(i);
                                String img_path = _jsonObject.getString("img_path");
                                hashFileMap.put(_name, img_path);

                            }

                            for(String collegeName : hashFileMap.keySet()){
                                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                                textSliderView
                                        .description(collegeName)
                                        .image(hashFileMap.get(collegeName))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra",collegeName);
                                sliderLayout.addSlider(textSliderView);
                            }
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(3000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
