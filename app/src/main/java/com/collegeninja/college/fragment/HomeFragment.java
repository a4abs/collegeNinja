package com.collegeninja.college.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View view;

    RecyclerView ourlibrary, domain, top_pic;//top_banner_slide;

    ArrayList<HashMap<String,String>> lib_arrayList = new ArrayList<>();
    ArrayList<HashMap<String,String>> domain_arrayList = new ArrayList<>();
    ArrayList<HashMap<String,String>> toppic_arrayList = new ArrayList<>();

    String token = "";

    public HomeFragment() {
        // Required empty public constructor
    }

    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home1, container, false);

        ourlibrary = view.findViewById(R.id.ourlibrary);
        domain = view.findViewById(R.id.domain);
        top_pic = view.findViewById(R.id.top_pic);
        //top_banner_slide = view.findViewById(R.id.banner_slideing);

        SharedPreferences pref = getActivity().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        //top_banner_slide.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        Hash_file_maps = new HashMap<String, String>();

        sliderLayout = view.findViewById(R.id.slider);


        ourlibrary.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        ourlibrary.addItemDecoration(itemDecoration);

        domain.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration _itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        domain.addItemDecoration(_itemDecoration);

        top_pic.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration __itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        top_pic.addItemDecoration(__itemDecoration);

        loadOurLibrary();

        loadDomain();

        loadTopPicture();

        return view;
    }

    private void loadOurLibrary() {
        lib_arrayList.clear();

        String url = "http://collegeninja.fdstech.solutions/api/get_libraries";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("true")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String,String> map = new HashMap<>();
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");
                            String thumb_img = _jsonObject.getString("thumb_img");
                            String thumb_img_path = _jsonObject.getString("thumb_img_path");

                            map.put("id",id);
                            map.put("name",name);
                            map.put("thumb_img",thumb_img_path);

                            lib_arrayList.add(map);
                        }

                        //Collections.reverse(lib_arrayList);

                        GridOurLibrary adapter= new GridOurLibrary(getActivity(), lib_arrayList);
                        ourlibrary.setAdapter(adapter);
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void loadDomain() {
        domain_arrayList.clear();

        String url = "http://collegeninja.fdstech.solutions/api/get_domains";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("true")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String,String> map = new HashMap<>();

                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");
                            String thumb_img =_jsonObject.getString("img_path");

                            map.put("id",id);
                            map.put("name",name);
                            map.put("thumb_img",thumb_img);

                            domain_arrayList.add(map);
                        }

                        GridDomainLibrary _adapter= new GridDomainLibrary(getActivity(), domain_arrayList);
                        domain.setAdapter(_adapter);
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void loadTopPicture() {

        toppic_arrayList.clear();

        String url = "http://collegeninja.fdstech.solutions/api/get_libraries";
        //String url = "http://collegeninja.fdstech.solutions/api/get_latest_articles";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if(success.equals("true")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);

                            HashMap<String,String> map = new HashMap<>();
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");
                            String thumb_img_path = _jsonObject.getString("thumb_img_path");

                            map.put("id",id);
                            map.put("name",name);

                            map.put("thumb_img",thumb_img_path);
                            toppic_arrayList.add(map);
                            Hash_file_maps.put(name,thumb_img_path);
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            textSliderView
                                    .description(name)
                                    .image(thumb_img_path)
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            //.setOnSliderClickListener(getActivity());
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);
                            sliderLayout.addSlider(textSliderView);

                        }

                        //Collections.reverse(toppic_arrayList);

                        GridTopPicture _adapter= new GridTopPicture(getActivity(), toppic_arrayList);
                        top_pic.setAdapter(_adapter);

                       /* for(int i = 0; i< toppic_arrayList.size(); i++) {
                            Log.d("Hello",  toppic_arrayList.get(i).get("name"));
                            toppic_arrayList.get(i).get("name");
                            Hash_file_maps.put( toppic_arrayList.get(i).get("name"),  toppic_arrayList.get(i).get("thumb_img"));
                        }
*/
                       // TopBannerAdapter mTopBannerAdapter= new TopBannerAdapter(getActivity(), toppic_arrayList);
                       // top_banner_slide.setAdapter(mTopBannerAdapter);


                       /* for(String name : Hash_file_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(getActivity());
                            textSliderView
                                    .description(name)
                                    .image(Hash_file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                                    //.setOnSliderClickListener(getActivity());
                            textSliderView.bundle(new Bundle());
                            textSliderView.getBundle()
                                    .putString("extra",name);
                            sliderLayout.addSlider(textSliderView);
                        }*/
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
}
