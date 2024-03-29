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
import com.collegeninja.college.adapter.GradeAdapter;
import com.collegeninja.college.adapter.StreamAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.collegeninja.college.utils.AppConstants;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectStreamActivity extends AppCompatActivity implements AppConstants {

    ArrayList<HashMap<String,String>> arrayList;
    private RecyclerView recyclerView;
    private StreamAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stream);
        arrayList = new ArrayList<>();

        recyclerView= findViewById(R.id.recyclerView);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        loadGradeData();
    }


    void loadGradeData(){
        String url = ROOT_URL+"/api/get_streams";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
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

                            map.put("id",id);
                            map.put("name",name);

                            arrayList.add(map);
                        }
                        adapter= new StreamAdapter(getApplicationContext(), arrayList);
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
