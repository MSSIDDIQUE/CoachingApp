package com.saquib.hello.coachingapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActionsFragment extends android.support.v4.app.Fragment {

    private View view;
    private String ContactNo;
    public CardView CreateClass,UpdateTimings,AddTopper,ChangePassword,CreateNotices,AddStudyMaterial;

    public ActionsFragment() {
    }

    public ActionsFragment setContactNo(String ContactNo)
    {
        ActionsFragment f = new ActionsFragment();
        f.ContactNo = ContactNo;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.actions_details_layout,container,false);
        CreateClass = view.findViewById(R.id.CreateClass);
        CreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.screen_area,new CreateClassFragment()).addToBackStack("MyBackStack").commit();
            }
        });
        UpdateTimings = view.findViewById(R.id.UpdateTimings);
        UpdateTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.screen_area,new TimeTableTab()).addToBackStack("MyBackStack").commit();
            }
        });

        AddTopper = view.findViewById(R.id.AddTopper);
        AddTopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.screen_area,new AddTopperFragment()).addToBackStack("MyBackStack").commit();
            }
        });

        ChangePassword = view.findViewById(R.id.ChangePassword);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.screen_area,new ChangePasswordFragement()).addToBackStack("MyBackStack").commit();

            }
        });

        CreateNotices = view.findViewById(R.id.AddDetails);
        CreateNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.screen_area,new AddDetailsFragement().setContactNo(ContactNo)).addToBackStack("MyBackStack").commit();
            }
        });

        AddStudyMaterial = view.findViewById(R.id.AddStudyMaterial);
        AddStudyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment f = new AddStudyMaterialFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.screen_area,new AddStudyMaterialFragment()).addToBackStack("MyBackStack").commit();
            }
        });
        return view;
    }

}
