package com.collegeninja.college.activity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.App;
import com.collegeninja.college.adapter.GridDomainLibrary;
import com.collegeninja.college.adapter.GridOurLibrary;
import com.collegeninja.college.adapter.GridTopPicture;
import com.collegeninja.college.extra.ItemOffsetDecoration;
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
import java.util.Map;

public class HomeActivity extends BaseActivity {
    View view;
    RecyclerView rvLibrary, rvDomain, rvTopPick;
    Activity activityHome;
    Dialog preferenceDialog;
    ArrayList<HashMap<String, String>> arrayListLibrary = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListDomain = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListTopPick = new ArrayList<>();

    SliderLayout sliderLayout;
    HashMap<String, String> hashMapSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHome = this;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.show(1, true);

        rvLibrary = contentView.findViewById(R.id.ourlibrary);
        rvDomain = contentView.findViewById(R.id.domain);
        rvTopPick = contentView.findViewById(R.id.top_pic);
        sliderLayout = contentView.findViewById(R.id.slider);

        hashMapSlider = new HashMap<String, String>();

        rvLibrary.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvLibrary.addItemDecoration(itemDecoration);

        rvDomain.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration _itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvDomain.addItemDecoration(_itemDecoration);

        rvTopPick.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration __itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvTopPick.addItemDecoration(__itemDecoration);

        preferenceDialog = new Dialog(this);
        // NEED RE-FACTORIZATION FOR API CALLS IT MAY CAUSE ERROR
        loadOurLibrary();

        loadDomain();

        loadTopPicture();

        if (!App.readUserPrefs("isPreferenceSet").equalsIgnoreCase("true")) {
            openSetPermissionPopup();
        }
    }


    private void openSetPermissionPopup() {
        preferenceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        preferenceDialog.setCancelable(false);
        preferenceDialog.setContentView(R.layout.dialog_set_preferences);

        Button btnSettings = (Button) preferenceDialog.findViewById(R.id.yndialog);
        btnSettings.setText(R.string.ok);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.writeUserPrefs("isPreferenceSet", "true");
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                preferenceDialog.dismiss();
                /*Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

        preferenceDialog.show();
    }

    private void loadOurLibrary() {
        arrayListLibrary.clear();

        String url = "http://collegeninja.fdstech.solutions/api/get_libraries";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            String thumb_img = _jsonObject.getString("thumb_img");
                            String thumb_img_path = _jsonObject.getString("thumb_img_path");

                            map.put("id", id);
                            map.put("name", name);
                            map.put("thumb_img", thumb_img_path);

                            arrayListLibrary.add(map);
                        }

                        GridOurLibrary adapter = new GridOurLibrary(activityHome, getApplicationContext(), arrayListLibrary);
                        rvLibrary.setAdapter(adapter);
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

    private void loadDomain() {
        arrayListDomain.clear();

        String url = "http://collegeninja.fdstech.solutions/api/get_domains";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            String thumb_img = _jsonObject.getString("img_path");

                            map.put("id", id);
                            map.put("name", name);
                            map.put("thumb_img", thumb_img);

                            arrayListDomain.add(map);
                        }

                        GridDomainLibrary _adapter = new GridDomainLibrary(activityHome, getApplicationContext(), arrayListDomain);
                        rvDomain.setAdapter(_adapter);
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

    private void loadTopPicture() {

        arrayListTopPick.clear();

        String url = "http://collegeninja.fdstech.solutions/api/get_latest_articles";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                            final String name = _jsonObject.getString("name");
                            String thumb_img_path = _jsonObject.getString("img_path");
                            String description = _jsonObject.getString("description");

                            map.put("id", id);
                            map.put("name", name);
                            map.put("thumb_img", thumb_img_path);
                            map.put("description", description);

                            arrayListTopPick.add(map);
                            hashMapSlider.put(name, thumb_img_path);
                            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                            textSliderView
                                    .description(name)
                                    .image(thumb_img_path)
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    int sliderPosition = sliderLayout.getCurrentPosition();

                                    Intent intent = new Intent(getApplicationContext(), ArticleDetailActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("image", arrayListTopPick.get(sliderPosition).get("thumb_img"));
                                    intent.putExtra("title", arrayListTopPick.get(sliderPosition).get("name"));
                                    intent.putExtra("desc", arrayListTopPick.get(sliderPosition).get("description"));

                                    startActivity(intent);
                                }
                            });
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra", name);
                            sliderLayout.addSlider(textSliderView);

                        }

                        //Collections.reverse(toppic_arrayList);

                        GridTopPicture _adapter = new GridTopPicture(activityHome, getApplicationContext(), arrayListTopPick);
                        rvTopPick.setAdapter(_adapter);

                        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
                        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        sliderLayout.setCustomAnimation(new DescriptionAnimation());
                        sliderLayout.setDuration(3000);
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

}
