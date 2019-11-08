package com.collegeninja.college.adapter;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.collegeninja.college.fragment.IntroFragment;
import com.collegeninja.college.fragment.SelectType;
import com.fdscollege.college.R;

public class IntroductionPagerAdaptor extends FragmentPagerAdapter {

    public IntroductionPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(Color.parseColor("#03A9F4"), position);
            case 1:
                return IntroFragment.newInstance(Color.parseColor("#4CAF50"), position);  // blue
            case 2:
                return IntroFragment.newInstance(Color.parseColor("#4CAF50"), position);  // blue
            case 3:
                return new SelectType();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
