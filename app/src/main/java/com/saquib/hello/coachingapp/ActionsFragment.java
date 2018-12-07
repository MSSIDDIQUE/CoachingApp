package com.saquib.hello.coachingapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.transition.Explode;
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
                CreateClassFragment f = new CreateClassFragment();
                f.setSharedElementEnterTransition(new DetailTransition());
                f.setEnterTransition(new Explode());
                f.setExitTransition(new Explode());
                f.setSharedElementReturnTransition(new DetailTransition());
                ((MainActivity)getActivity()).replaceFragment(f,"Create new Class");
            }
        });
        UpdateTimings = view.findViewById(R.id.UpdateTimings);
        UpdateTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeTableTab f = new TimeTableTab();
                f.setSharedElementEnterTransition(new DetailTransition());
                f.setEnterTransition(new Explode());
                f.setExitTransition(new Explode());
                f.setSharedElementReturnTransition(new DetailTransition());
                ((MainActivity)getActivity()).replaceFragment(f,"Time Table");
            }
        });

        AddTopper = view.findViewById(R.id.AddTopper);
        AddTopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTopperFragment f = new AddTopperFragment();
                f.setSharedElementEnterTransition(new DetailTransition());
                f.setEnterTransition(new Explode());
                f.setExitTransition(new Explode());
                f.setSharedElementReturnTransition(new DetailTransition());
                ((MainActivity)getActivity()).replaceFragment(f,"Add new Topper");
            }
        });

        ChangePassword = view.findViewById(R.id.ChangePassword);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragement f = new ChangePasswordFragement();
                f.setSharedElementEnterTransition(new DetailTransition());
                f.setEnterTransition(new Explode());
                f.setExitTransition(new Explode());
                f.setSharedElementReturnTransition(new DetailTransition());
                ((MainActivity)getActivity()).replaceFragment(f,"Change Password");
            }
        });

        CreateNotices = view.findViewById(R.id.AddDetails);
        CreateNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDetailsFragement f = new AddDetailsFragement().setContactNo(ContactNo);
                f.setSharedElementEnterTransition(new DetailTransition());
                f.setEnterTransition(new Explode());
                f.setExitTransition(new Explode());
                f.setSharedElementReturnTransition(new DetailTransition());
                ((MainActivity)getActivity()).replaceFragment(f,"Add your Details");
            }
        });

        AddStudyMaterial = view.findViewById(R.id.AddStudyMaterial);
        AddStudyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudyMaterialFragment f = new AddStudyMaterialFragment();
                f.setSharedElementEnterTransition(new DetailTransition());
                f.setEnterTransition(new Explode());
                f.setExitTransition(new Explode());
                f.setSharedElementReturnTransition(new DetailTransition());
                ((MainActivity)getActivity()).replaceFragment(f,"Add new Material  ");
            }
        });
        return view;
    }

}
