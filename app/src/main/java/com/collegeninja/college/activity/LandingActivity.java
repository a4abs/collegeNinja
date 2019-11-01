package com.collegeninja.college.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.collegeninja.college.fragment.CollegesFragment;
import com.collegeninja.college.fragment.CourseFragment;
import com.collegeninja.college.fragment.DiscussionFragment;
import com.collegeninja.college.fragment.HomeFragment;
import com.collegeninja.college.fragment.ProfileFragment;
import com.fdscollege.college.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView name, batch;
    String token;
    BottomNavigationView navigation;
    Dialog preferenceDialog;
    private MaterialSearchView mSearchViewHome;
    MenuItem searchItem;
    Boolean isPreferenceSet;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

       /* mSearchViewHome =  (SearchView) findViewById(R.id.search_view);
        mSearchViewHome.setQueryHint("Search");*/


        //mSearchViewHome.showSearch();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(LandingActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        sharedPreferences = getApplicationContext().getSharedPreferences("college", 0);
        token = sharedPreferences.getString("token", "");
        isPreferenceSet = sharedPreferences.getBoolean("isPreferences", false);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_landing);
        name = headerLayout.findViewById(R.id.nav_name);
        batch = headerLayout.findViewById(R.id.nav_batch);

        preferenceDialog = new Dialog(this);

        // Searchview implementation

        mSearchViewHome = findViewById(R.id.search_view);
        mSearchViewHome.setVoiceSearch(false);
       // mSearchViewHome.performClick();

        mSearchViewHome.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Log.d("Query", "====>"+query);
                Intent i = new Intent(getApplicationContext(), ActivityArticleSearch.class);
                i.putExtra("search_text", query);
                startActivity(i);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        mSearchViewHome.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                searchItem.setVisible(false);
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                searchItem.setVisible(true);
            }
        });

        hideSoftKeyboard();

        loadProfileData();

        if(!isPreferenceSet){
            openSetPermissionPopup();
        }


        if(!isNetworkConnected()){
            Toast.makeText(this, "check your internet connection and try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openSetPermissionPopup() {
        preferenceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        preferenceDialog.setCancelable(false);
        preferenceDialog.setContentView(R.layout.dialog_set_preferences);

        Button btnSettings = (Button) preferenceDialog.findViewById(R.id.yndialog);
        btnSettings.setText(R.string.ok);

        /*TextView tvMessage = (TextView) openNeverAskPermissionPopupDialog.findViewById(R.id.yn_dialog_message);
        tvMessage.setText(R.string.permission_phone_assert);*/

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                loadFragment(fragment);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isPreferenceSet", true);
                editor.commit();
                preferenceDialog.dismiss();
                /*Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

        preferenceDialog.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //toolbar.setTitle("Shop");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_discussion:
                    // toolbar.setTitle("Discussion");
                    fragment = new DiscussionFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_colleges:
                    //toolbar.setTitle("Colleges");
                    fragment = new CollegesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_course:
                    //toolbar.setTitle("Course");
                    fragment = new CourseFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    //toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mSearchViewHome.setMenuItem(searchItem);
        //mSearchViewHome.setIconified
        //View searchView = item.getActionView();
        //searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, LandingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_discussion:
                fragment = new DiscussionFragment();
                loadFragment(fragment);
                navigation.setSelectedItemId(R.id.navigation_discussion);
                return true;
            case R.id.navigation_colleges:
                fragment = new CollegesFragment();
                loadFragment(fragment);
                navigation.setSelectedItemId(R.id.navigation_colleges);
                return true;
            case R.id.navigation_course:
                fragment = new CourseFragment();
                loadFragment(fragment);
                navigation.setSelectedItemId(R.id.navigation_course);
                return true;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                navigation.setSelectedItemId(R.id.navigation_profile);
                loadFragment(fragment);
                return true;
            case R.id.faq:
                startActivity(new Intent(getApplicationContext(),FAQActivity.class));
                return true;
            case R.id.logout:
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("college", 0);
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                return true;
        }

        return false;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void loadProfileData() {
        String url = "http://collegeninja.fdstech.solutions/api/get_profile/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("true")) {
                        JSONObject _jsonObject = jsonObject.getJSONObject("data");

                        Log.i("profile ::::", "" + _jsonObject);
                        String _name = _jsonObject.getString("name");
                        String _academic_status = _jsonObject.getString("academic_status");
                        String _domain = _jsonObject.getString("domain");

                        name.setText(_name);

                        batch.setText(_academic_status +"/"+_domain);
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }

    public void updateProfile(final String p_name, final String p_phone, final String p_email, final String _city_id, final String _gender_id, final int dayOfMonth, final int month, final int year, final String _grades_id, final String _domain_id) {
        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage("please wait.");
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        String url = "http://collegeninja.fdstech.solutions/api/update_user_profile";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    loadProfileData();

                    if (success.equals("true")) {
                        Toast.makeText(LandingActivity.this, "profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("Authorization", token);
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
                MyData.put("user_image", "");
                return MyData;
            }
        };

        MyRequestQueue.add(request);

    }

}
