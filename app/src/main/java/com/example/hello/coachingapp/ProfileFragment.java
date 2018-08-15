package com.example.hello.coachingapp;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;

import java.sql.Time;

public class ProfileFragment extends android.support.v4.app.Fragment{

    public View view;
    public TextView Name;
    public EditText EditName;
    public TextView Email;
    public EditText EditEmail;
    public TextView Password;
    public EditText EditPassword;
    public TextView ContactNo;
    public EditText EditContactNo;
    public ExpandableCardView ProfileDetails;
    public ExpandableCardView Classes;
    public RecyclerView rv;
    public ExpandableCardView Profile,Actions;
    public CardView CreateClass,UpdateTimings;
    public android.support.v4.app.Fragment TimeItemFragment;
    public ScrollView sc;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_layout,container,false);
        Profile = view.findViewById(R.id.profile);
        EditName = Profile.findViewById(R.id.EditName);
        Name = Profile.findViewById(R.id.Name);
        EditEmail = Profile.findViewById(R.id.EditEmail);
        Email = Profile.findViewById(R.id.Email);
        EditContactNo = Profile.findViewById(R.id.EditContactNo);
        ContactNo = Profile.findViewById(R.id.ContactNo);
        EditPassword = Profile.findViewById(R.id.EditPassword);
        Password = Profile.findViewById(R.id.Password);
        sc = view.findViewById(R.id.scrolView);
        Actions = view.findViewById(R.id.actions);
        CreateClass = Actions.findViewById(R.id.CreateClass);
        CreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.PlaceHolder,new CreateClassFragment()).commit();
                sc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        sc.post(new Runnable() {
                            public void run() {
                                sc.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                });
            }
        });
        UpdateTimings = Actions.findViewById(R.id.UpdateTimings);
        UpdateTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.screen_area,new TimeTableTab()).commit();
            }
        });

        return view;
    }

}
