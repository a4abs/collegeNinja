package com.collegeninja.college.activity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.App;
import com.collegeninja.college.adapter.ArticleAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LatestNewsActivity extends BaseActivity {

    RecyclerView rvLatestNews;
    ArrayList<HashMap<String,String>> arrayListArticles = new ArrayList<>();
    TextView header;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_latest_news, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        rvLatestNews = findViewById(R.id.articles);
        header = findViewById(R.id.header);

        rvLatestNews.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        rvLatestNews.addItemDecoration(itemDecoration);

        header.setText("What is Buzzing?");

        Log.d("latestNews","===>");
        fetchLatestNews();
    }

    private void fetchLatestNews(){
        String url = "http://collegeninja.fdstech.solutions/api/latest_news";
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final StringRequest expInterestCollegeReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    Log.d("latestNews","===>"+jsonObject);
                    if(success.equals("true")){
                        JSONArray latestNews = jsonObject.getJSONArray("data");

                        for(int i = 0; i < latestNews.length(); i++){
                            JSONObject _jsonObject = latestNews.getJSONObject(i);
                            HashMap<String,String> map = new HashMap<>();
                            String id = _jsonObject.getString("id");
                            String name = _jsonObject.getString("name");
                            String description = _jsonObject.getString("description");
                            String thumb_img_path = _jsonObject.getString("img_path");

                            map.put("id",id);
                            map.put("name",name);
                            map.put("description",description);
                            map.put("thumb_img",thumb_img_path);

                            arrayListArticles.add(map);
                        }

                        ArticleAdapter adapter = new ArticleAdapter(getApplicationContext(), arrayListArticles);
                        rvLatestNews.setAdapter(adapter);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
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

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(expInterestCollegeReq);
    }
}
