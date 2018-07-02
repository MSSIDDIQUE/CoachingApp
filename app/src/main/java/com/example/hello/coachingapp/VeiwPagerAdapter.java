package com.example.hello.coachingapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by hello on 12-Mar-18.
 */

public class VeiwPagerAdapter extends FragmentStatePagerAdapter {

    public VeiwPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ToppersFragment toppersFragment= new ToppersFragment();
        return toppersFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }
}
