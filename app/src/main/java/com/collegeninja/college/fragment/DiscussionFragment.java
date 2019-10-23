package com.collegeninja.college.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.collegeninja.college.adapter.DiscussionAdapter;
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
public class DiscussionFragment extends Fragment {

    RecyclerView discussion;
    String token;
    ArrayList<HashMap<String,String>> arrayList_discussion = new ArrayList<>();

    public DiscussionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_discussion, container, false);
        discussion = view.findViewById(R.id.discussion);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        discussion.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        discussion.addItemDecoration(itemDecoration);

        SharedPreferences pref = getActivity().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        loadDiscussion();

        return view;
    }

    private void loadDiscussion() {
        String url = "http://collegeninja.fdstech.solutions/api/get_discussions/";
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
                            String title = _jsonObject.getString("title");
                            String likes = _jsonObject.getString("likes");
                            String created_by_id = _jsonObject.getJSONObject("created_by").getString("id");
                            String created_by_name = _jsonObject.getJSONObject("created_by").getString("name");
                            String comments = _jsonObject.getJSONArray("comments").toString();

                            map.put("id",id);
                            map.put("title",title);
                            map.put("likes",likes);

                            map.put("created_by_id",created_by_id);
                            map.put("created_by_name",created_by_name);
                            map.put("comments",comments);

                            arrayList_discussion.add(map);
                        }

                        DiscussionAdapter adapter= new DiscussionAdapter(getActivity(), arrayList_discussion);
                        discussion.setAdapter(adapter);
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
