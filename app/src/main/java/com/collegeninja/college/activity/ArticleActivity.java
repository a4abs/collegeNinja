package com.collegeninja.college.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.adapter.ArticleAdapter;
import com.collegeninja.college.adapter.GridOurLibrary;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArticleActivity extends AppCompatActivity {

    RecyclerView articel;
    ArrayList<HashMap<String,String>> arrayList_articel = new ArrayList<>();

    String token = "";
    TextView header;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Article");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        articel = findViewById(R.id.articel);
        header = findViewById(R.id.header);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
           }
        });


        articel.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        articel.addItemDecoration(itemDecoration);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
        Log.i("token :::::: ", "" + token);

        String _id = getIntent().getStringExtra("id");
        String _name = getIntent().getStringExtra("title");

        header.setText(_name);

        loadArticle(_id);
    }


    private void loadArticle(String id) {
        String url = "http://collegeninja.fdstech.solutions/api/get_articles/"+id;
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
                            String description = _jsonObject.getString("description");
                            String thumb_img_path = _jsonObject.getString("img_path");

                            map.put("id",id);
                            map.put("name",name);
                            map.put("description",description);
                            map.put("thumb_img",thumb_img_path);

                            arrayList_articel.add(map);
                        }

                        ArticleAdapter adapter = new ArticleAdapter(getApplicationContext(), arrayList_articel);
                        articel.setAdapter(adapter);
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
