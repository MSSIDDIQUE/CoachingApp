package com.saquib.hello.coachingapp;


import android.annotation.TargetApi;
import android.app.Service;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class TimeTableTab extends android.support.v4.app.Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public Toolbar toolbar;
    View view;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean Islogin = prefs.getBoolean("Islogin",false);
        if(!Islogin)
        {
            Toast.makeText(getContext(),"Please make sure that you have Successfully Logged in to your Acccount", Toast.LENGTH_LONG);
            view = inflater.inflate(R.layout.sorry_layout,container,false);
            TextView text = (TextView)view.findViewById(R.id.textView);
            text.setText("Please make sure that you have Successfully Logged in to your Acccount");
            return view;
        }
        view=inflater.inflate(R.layout.time_table_layout,container,false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Fayyaz Ganj"));
        tabLayout.addTab(tabLayout.newTab().setText("Idgah"));
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new TimeTableTab.PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if(!isConnected())
                    {
                        return new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network");
                    }
                    return new TimeTableFragment().setDBR("Fayyaz Ganj");
                case 1:
                    if(!isConnected())
                    {
                        return new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network");
                    }
                    return new TimeTableFragment().setDBR("Idgah");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        public boolean isConnected()
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
            if(connectivityManager!=null)
            {
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if(info!=null)
                {
                    if(info.getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
