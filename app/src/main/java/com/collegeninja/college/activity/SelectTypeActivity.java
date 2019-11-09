package com.collegeninja.college.activity;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.adapter.UserTypeAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectTypeActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arrayList;
    private RecyclerView recyclerView;
    private UserTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        loadGradeData();
    }

    void loadGradeData() {
        String url = "http://collegeninja.fdstech.solutions/api/get_usertypes";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
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

                            map.put("id", id);
                            map.put("name", name);

                            arrayList.add(map);
                        }
                        adapter = new UserTypeAdapter(getApplicationContext(), arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
