package com.collegeninja.college.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.collegeninja.college.App;
import com.collegeninja.college.fragment.CollegesFragment;
import com.collegeninja.college.fragment.CourseFragment;
import com.collegeninja.college.fragment.HomeFragment;
import com.collegeninja.college.fragment.ProfileFragment;
import com.collegeninja.college.utils.AppConstants;
import com.collegeninja.college.utils.ShowMessageDialog;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.fdscollege.college.R;
import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class BaseActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener, NavigationView.OnNavigationItemSelectedListener, AppConstants {

    MaterialSearchBar mSearchViewHome;
    MeowBottomNavigation bottomNavigation;
    CircleImageView imvUserImage;
    TextView tvUserName, tvUserBatch;

    Dialog preferencesUpdatedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        mSearchViewHome = findViewById(R.id.search_view);
        mSearchViewHome.setHint("Search");
        mSearchViewHome.setOnSearchActionListener(this);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imvUserImage = headerLayout.findViewById(R.id.user_image);
        tvUserName = headerLayout.findViewById(R.id.user_name);
        tvUserBatch = headerLayout.findViewById(R.id.user_batch);

        preferencesUpdatedDialog = new Dialog(this);

        if (!App.readUserPrefs("uName").isEmpty()) {
            tvUserName.setText(App.readUserPrefs("uName"));
        } else {
            tvUserName.setText("");
        }

        if (!App.readUserPrefs("batch").isEmpty()) {
            tvUserBatch.setText(App.readUserPrefs("batch"));
        } else {
            tvUserBatch.setText("");
        }

        if (App.readUserPrefs("profilePic") != null || !App.readUserPrefs("profilePic").isEmpty()) {
            byte[] decodedString = Base64.decode(App.readUserPrefs("profilePic"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imvUserImage.setImageBitmap(decodedByte);
        } else {
            imvUserImage.setImageResource(R.drawable.upload_profile_pic);
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.college));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.courses));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.persion));


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model p1) {
                switch (p1.getId()) {
                    case 1:
                        Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intentHome);
                        return Unit.INSTANCE;

                    case 2:
                        Intent intentCollege = new Intent(getApplicationContext(), CollegeActivity.class);
                        startActivity(intentCollege);
                        return Unit.INSTANCE;
                    case 3:
                        Intent intentCourses = new Intent(getApplicationContext(), CourseActivity.class);
                        startActivity(intentCourses);
                        return Unit.INSTANCE;
                    case 4:
                        Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intentProfile);
                        return Unit.INSTANCE;
                }
                return Unit.INSTANCE;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Intent i = new Intent(BaseActivity.this, ActivityArticleSearch.class);
        i.putExtra("search_text", text.toString());
        startActivity(i);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, HomeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_tools:
                finish();
                startActivity(new Intent(getApplicationContext(), FAQActivity.class));
                return true;
            case R.id.nav_contact:
                finish();
                startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                return true;
            case R.id.nav_settings:
                finish();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.nav_news:
                finish();
                startActivity(new Intent(getApplicationContext(), LatestNewsActivity.class));
                return true;
            case R.id.nav_bookmark:
                finish();
                startActivity(new Intent(getApplicationContext(), MyBookmarksActivity.class));
                return true;
        }
        return false;
    }

    public void upDateDrawerHeader() {
        tvUserName.setText(App.readUserPrefs("uName"));
        tvUserBatch.setText(App.readUserPrefs("batch"));

        if (App.readUserPrefs("profilePic") != null || !App.readUserPrefs("profilePic").isEmpty()) {
            byte[] decodedString = Base64.decode(App.readUserPrefs("profilePic"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imvUserImage.setImageBitmap(decodedByte);
        } else {
            imvUserImage.setImageResource(R.drawable.upload_profile_pic);
        }
    }

    private void loadProfileData() {
        String url = ROOT_URL+"/get_user_pref";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        JSONObject _jsonObject = jsonObject.getJSONObject("data");

                        String _name = _jsonObject.getString("name");
                        String _academic_status = _jsonObject.getString("academic_status");
                        String _domain = _jsonObject.getString("domain");
                        String _image = _jsonObject.getString("profile_pic");
                        String pref_domain_name = _jsonObject.getString("pref_domain_name");
                        App.writeUserPrefs("uName", _name);
                        App.writeUserPrefs("batch", _academic_status + "/" + pref_domain_name);
                        App.writeUserPrefs("profilePic", _image);

                        tvUserName.setText(App.readUserPrefs("uName"));
                        tvUserBatch.setText(App.readUserPrefs("batch"));

                        if (_image != null) {
                            byte[] decodedString = Base64.decode(App.readUserPrefs("profilePic"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imvUserImage.setImageBitmap(decodedByte);
                        } else {
                            imvUserImage.setImageResource(R.drawable.upload_profile_pic);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Error", "==>" + e);
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
        queue.add(request);
    }

    // UpdateUserProfile
    public void updateProfile(final String p_name, final String p_phone, final String p_email, final String _city_id, final String _gender_id, final int dayOfMonth, final int month, final int year, final String _grades_id, final String _domain_id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = ROOT_URL+"/update_user_profile";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    loadProfileData();

                    if (success.equals("true")) {
                        ShowMessageDialog showMessageDialog = new ShowMessageDialog();
                        showMessageDialog.displayDialog(BaseActivity.this, "profile and preferences updated successfully");
                        Toast.makeText(BaseActivity.this, "profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "==>" + error);
                parseVolleyError(error);
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
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", p_name);
                MyData.put("mobile", p_phone);
                MyData.put("email", p_email);
                MyData.put("city", _city_id);
                MyData.put("gender", _gender_id);
                MyData.put("dob_day", String.valueOf(dayOfMonth));
                MyData.put("dob_month", String.valueOf(month));
                MyData.put("dob_year", String.valueOf(year));
                MyData.put("academic_status", _grades_id);
                MyData.put("domain", _domain_id);
                MyData.put("pref_domain", _domain_id);
                MyData.put("user_image", "");
                return MyData;
            }
        };

        MyRequestQueue.add(request);

    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            Log.d("Error", "==>" + data);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);
            String message = jsonMessage.getString("message");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

}
