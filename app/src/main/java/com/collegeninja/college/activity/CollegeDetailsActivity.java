package com.collegeninja.college.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.collegeninja.college.adapter.CollegeFeatureAdapter;
import com.collegeninja.college.adapter.CollegeImagesAdaptor;
import com.collegeninja.college.adapter.CourseDetailAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.fdscollege.college.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeDetailsActivity extends AppCompatActivity {

    String _id, _name, _description, _contact, _thumb_img, _features, _courses, _brochure, _images;
    ImageView header_image;
    TextView title, description, brochure, contact;
    RecyclerView rvCourseOffered, rvFeatures, rvGallery;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList_feature = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListImages = new ArrayList<>();

    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;

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

        Hash_file_maps = new HashMap<String, String>();

        _id = getIntent().getStringExtra("id");
        _name = getIntent().getStringExtra("name");
        _description = getIntent().getStringExtra("description");
        _contact = getIntent().getStringExtra("contact");

        _thumb_img = getIntent().getStringExtra("thumb_img");
        _features = getIntent().getStringExtra("features");
        _courses = getIntent().getStringExtra("courses");
        _brochure = getIntent().getStringExtra("brochure");
        _images = getIntent().getStringExtra("images");

        Log.d("images","========>"+_images);
        header_image = findViewById(R.id.header_image);
        rvCourseOffered = findViewById(R.id.course_offered);
        rvFeatures = findViewById(R.id.feature);
        rvGallery = findViewById(R.id.gallery);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        brochure = findViewById(R.id.brochure);
        contact = findViewById(R.id.contact);
        sliderLayout = findViewById(R.id.slider);

        title.setText(_name);
        description.setText(Html.fromHtml(_description));

        if(!_contact.isEmpty() && _contact != null){
            contact.setText("Call: " + _contact);
        }else{
            contact.setVisibility(View.GONE);
        }

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + _contact));
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

        brochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_brochure));
                startActivity(browserIntent);
            }
        });

      /*  Glide.with(getApplicationContext()).load(_thumb_img).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                //holder.progress.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(header_image);*/

        //rvCourseOffered.setLayoutManager(new LinearLayoutManager(CollegeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCourseOffered.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvCourseOffered.addItemDecoration(itemDecoration);

        rvFeatures.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration _itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvFeatures.addItemDecoration(_itemDecoration);

        rvGallery.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecorationGallery = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvGallery.addItemDecoration(itemDecorationGallery);

        try {
            JSONArray jsonArray = new JSONArray(_thumb_img);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject _jsonObject = jsonArray.getJSONObject(i);
                String img_path = _jsonObject.getString("img_path");
                Hash_file_maps.put(_name, img_path);

            }

            for(String name : Hash_file_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                textSliderView
                        .description(name)
                        .image(Hash_file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                //.setOnSliderClickListener(getActivity());
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(3000);
            //Picasso.with(CollegeDetailsActivity.this).load(img_path).placeholder(R.drawable.logo).error(R.drawable.logo).fit().networkPolicy(NetworkPolicy.NO_CACHE).into(header_image);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jsonArray = new JSONArray(_courses);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject _jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                String id = _jsonObject.getString("id");
                String name = _jsonObject.getString("name");
                String course_img = _jsonObject.getString("course_img");

                map.put("id", id);
                map.put("name", name);
                map.put("thumb_img", course_img);

                arrayList.add(map);
            }

            CourseDetailAdapter adapter = new CourseDetailAdapter(getApplicationContext(), arrayList);
            rvCourseOffered.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // populate gallery
        try {
            JSONArray jsonArray = new JSONArray(_images);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject _jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                String id = _jsonObject.getString("id");
                String image = _jsonObject.getString("image");
                String img_path = _jsonObject.getString("img_path");

                map.put("id", id);
                map.put("name", image);
                map.put("img_path", img_path);

                arrayListImages.add(map);
            }

            CollegeImagesAdaptor adapter = new CollegeImagesAdaptor(getApplicationContext(), arrayListImages);
            rvGallery.setAdapter(adapter);
        } catch (JSONException e){
            e.printStackTrace();
        }


        try {
            JSONArray jsonArray = new JSONArray(_features);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject _jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                String id = _jsonObject.getString("id");
                String name = _jsonObject.getString("name");
                String logo = _jsonObject.getString("logo");
                String description = _jsonObject.getString("description");
                map.put("id", id);
                map.put("name", name);
                map.put("logo", logo);
                map.put("description", description);

                arrayList_feature.add(map);
            }

            CollegeFeatureAdapter adapter = new CollegeFeatureAdapter(getApplicationContext(), arrayList_feature);
            rvFeatures.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
