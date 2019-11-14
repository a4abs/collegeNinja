package com.collegeninja.college.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.collegeninja.college.adapter.CollegeAdapter;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.collegeninja.college.utils.AppConstants;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBookmarksActivity extends BaseActivity implements AppConstants {
    RecyclerView rvColleges;
    ArrayList<HashMap<String, String>> arrayListColleges = new ArrayList<>();
    TextView header;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_my_bookmarks, null, false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        header = findViewById(R.id.header);
        rvColleges = contentView.findViewById(R.id.college_recyclerview);

        rvColleges.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvColleges.addItemDecoration(itemDecoration);

        header.setText("My Bookmarks");

        fetchBookmarks();
    }

    private void fetchBookmarks() {
        String url = ROOT_URL+"/api/my_bookmarks";
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
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
                            map.put("images", images);
                            map.put("brochure", brochure);

                            arrayListColleges.add(map);
                        }

                        CollegeAdapter adapter = new CollegeAdapter(getApplicationContext(), arrayListColleges);
                        rvColleges.setAdapter(adapter);

                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("error is ", "" + error.getMessage());
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
