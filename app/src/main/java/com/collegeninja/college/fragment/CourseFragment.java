package com.collegeninja.college.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.collegeninja.college.App;
import com.collegeninja.college.adapter.CourseAdapter;
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
public class CourseFragment extends Fragment {

    RecyclerView course;
    String token;

    public CourseFragment() {
        // Required empty public constructor
    }
    ArrayList<HashMap<String,String>> lib_arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_course, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjY2Njg4NTlhYTM4NTY2Y2E5Mjg0NWE3MjNjMmVjNGZjMDljZTM4Mzc5NWMzMjRmM2ZiN2U5NGNiNGExZDM5YzY1ZmJmYTYyMDNkZTY3ZGI5In0.eyJhdWQiOiIxIiwianRpIjoiNjY2ODg1OWFhMzg1NjZjYTkyODQ1YTcyM2MyZWM0ZmMwOWNlMzgzNzk1YzMyNGYzZmI3ZTk0Y2I0YTFkMzljNjVmYmZhNjIwM2RlNjdkYjkiLCJpYXQiOjE1NjgwOTk4MDIsIm5iZiI6MTU2ODA5OTgwMiwiZXhwIjoxNTk5NzIyMjAyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.jhFGdas7A-9m3VmRlOErENFT3r_LN9PyX0IfUFx-VWRo2okQxOKfkxi1qATIaU4Fs4emkOi4yIsZs9vSWXQ5lNfq5CLRXcXuUYJaGdz48_1AlPt6kBETDfh_bCwARpXq6gboyKmp0QPj47CvtumH66LEpgaWAvifAa5trPTKzhz8NWoInHe81adcBmOrwrbZ5wtgpHRP9_vOzr9slbY5Xgtq-GIHudyg7MiQAKXs7Z8GassnFhLkfEShytmdJnPk3S2fvdnsF038Qt7WSHnAOWC9oDiznsysjq6RCB78sgHzHppkWHaoeOSa7hfBwi1pkmbE-sucJ5nA3nUMmowStXGAN94nIMRiCf2Op0jD2RbL1EfUWFODZu6csMzeOQvWiT4lN7IZtL8VpBqJY-DR4oJzjx0ZUI_ZF8XngJdPjLVogVgmA4IaNRAb5KeVPRwD79TdKoiCo-DNwwtB_QnmkgkSE4QrFx60yICxm20NBwvBbv0UAJULu1vLxSPPqR6BaRTX5LA5Vw5VO00Gt0XJ-M3b8wk8OctfJL6pMo-uRxciTyybOErH2AfZ1w4QKYg3gKZtYWGmKY7W6CpIiG5HOrGybqujtdvGfeK-wai34yJBHJERdytqr2qIYsOtW2s7C_X_t3VDXRzvsNKqzs483vPOgYjFtonmJTHADJrE9dk");

        Log.i("token :::::: ", "" + token);
        course = view.findViewById(R.id.course);
        course.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        course.addItemDecoration(itemDecoration);

        loadcourseInfo();

        return view;
    }

    private void loadcourseInfo() {
        String url = "http://collegeninja.fdstech.solutions/api/get_all_courses";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    Log.d("Courses","===>"+jsonObject);
                    if (success.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject _jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");
                            String thumb_img_path = _jsonObject.getString("course_img");
                            String colleges = _jsonObject.getJSONArray("colleges").toString();

                            map.put("id", id);
                            map.put("name", name);
                            map.put("thumb_img", thumb_img_path);
                            map.put("colleges", colleges);
                            map.put("domain", "");
                            map.put("image", null);

                            lib_arrayList.add(map);

                        }

                        CourseAdapter adapter = new CourseAdapter(getActivity(), lib_arrayList);
                        course.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error.getMessage());
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

}
