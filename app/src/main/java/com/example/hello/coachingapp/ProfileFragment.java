package com.example.hello.coachingapp;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;

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
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_layout,container,false);
        EditName = view.findViewById(R.id.EditName);
        Name = view.findViewById(R.id.Name);
        EditEmail = view.findViewById(R.id.EditEmail);
        Email = view.findViewById(R.id.Email);
        EditContactNo = view.findViewById(R.id.EditContactNo);
        ContactNo = view.findViewById(R.id.ContactNo);
        EditPassword = view.findViewById(R.id.EditPassword);
        Password = view.findViewById(R.id.Password);
        Classes = view.findViewById(R.id.Classes);
        rv = view.findViewById(R.id.recyclerView1);
        return view;
    }

}
