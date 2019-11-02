package com.collegeninja.college.activity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import android.view.Menu;

import com.collegeninja.college.fragment.CollegesFragment;
import com.collegeninja.college.fragment.CourseFragment;
import com.collegeninja.college.fragment.DiscussionFragment;
import com.collegeninja.college.fragment.HomeFragment;
import com.collegeninja.college.fragment.ProfileFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.fdscollege.college.R;
import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private AppBarConfiguration mAppBarConfiguration;
    private MaterialSearchBar mSearchViewHome;
    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        bottomNavigation =  findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.college));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.courses));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.persion));
        

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d("====>", "===" + text);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {

            case R.id.navigation_home:
                //toolbar.setTitle("Shop");
                fragment = new HomeFragment();
                //loadFragment(fragment);
                return true;
            case R.id.navigation_discussion:
                // toolbar.setTitle("Discussion");
                fragment = new DiscussionFragment();
                //loadFragment(fragment);
                return true;
            case R.id.navigation_colleges:
                //toolbar.setTitle("Colleges");
                fragment = new CollegesFragment();
                //loadFragment(fragment);
                return true;
            case R.id.navigation_course:
                //toolbar.setTitle("Course");
                fragment = new CourseFragment();
                //loadFragment(fragment);
                return true;
            case R.id.navigation_profile:
                //toolbar.setTitle("Profile");
                fragment = new ProfileFragment();
                //loadFragment(fragment);
                return true;
        }
        return false;
    }*/
}
