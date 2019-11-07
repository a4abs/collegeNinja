package com.collegeninja.college.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.adapter.ArticleAdapter;
import com.collegeninja.college.adapter.SubDomainAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubDomainsActivity extends BaseActivity {

    RecyclerView rvSubdomain;
    String id, name, token;
    TextView header;
    ArrayList<HashMap<String,String>> arrayList_subdomain = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_sub_domains, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);
        //setContentView(R.layout.activity_sub_domains);

        rvSubdomain = findViewById(R.id.subdomain);
        header = findViewById(R.id.header);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SUBDOMAINS");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
*/
        rvSubdomain.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvSubdomain.addItemDecoration(itemDecoration);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("college", Context.MODE_PRIVATE);
        token = pref.getString("token", "");

        header.setText(name);

        loadSubDomains(id);
    }

    private void loadSubDomains(String id) {
        String url = "http://collegeninja.fdstech.solutions/api/get_subdomains/"+id;
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
                            String subdomain_image = _jsonObject.getString("image");
                            String thumb_img_path = _jsonObject.getString("img_path");

                            map.put("id",id);
                            map.put("name",name);
                            map.put("description",description);
                            map.put("thumb_img",thumb_img_path);

                            arrayList_subdomain.add(map);
                        }

                        SubDomainAdapter adapter= new SubDomainAdapter(getApplicationContext(), arrayList_subdomain);
                        rvSubdomain.setAdapter(adapter);
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

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
