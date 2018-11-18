package com.saquib.hello.coachingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
