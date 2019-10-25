package com.collegeninja.college.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.collegeninja.college.adapter.CollegeAdapter;
import com.collegeninja.college.adapter.GridOurLibrary;
import com.collegeninja.college.extra.ItemOffsetDecoration;
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
public class CollegesFragment extends Fragment {

    RecyclerView collegeRecyclerView;
    ArrayList<HashMap<String, String>> lib_arrayList = new ArrayList<>();
    String token;

    public CollegesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_colleges, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        collegeRecyclerView = view.findViewById(R.id.college_recyclerview);
        collegeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        collegeRecyclerView.addItemDecoration(itemDecoration);

        loadCollegeInfo();

        return view;
    }

    private void loadCollegeInfo() {
        String url = "http://collegeninja.fdstech.solutions/api/get_all_colleges";
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
                            String description = _jsonObject.getString("description");
                            String brochure = _jsonObject.getString("brochure");
                            String contact = _jsonObject.getString("contact");

                            String features = _jsonObject.getJSONArray("features").toString();
                            String courses = _jsonObject.getJSONArray("courses").toString();
                            String videos = _jsonObject.getJSONArray("videos").toString();
                            String images = _jsonObject.getJSONArray("images").toString();

                            map.put("id", id);
                            map.put("name", name);
                            map.put("description", description);
                            map.put("contact", contact);

                            map.put("thumb_img", images);
                            map.put("features", features);
                            map.put("courses", courses);
                            map.put("brochure", brochure);

                            lib_arrayList.add(map);
                        }

                        CollegeAdapter adapter = new CollegeAdapter(getActivity(), lib_arrayList);
                        collegeRecyclerView.setAdapter(adapter);
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
