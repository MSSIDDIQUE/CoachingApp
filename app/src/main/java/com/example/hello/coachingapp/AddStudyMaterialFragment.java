package com.example.hello.coachingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class AddStudyMaterialFragment extends android.support.v4.app.Fragment {


    private View view;
    private Spinner SelectSubject, SelectClass, SelectMateralType;

    public AddStudyMaterialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_study_material_layout);
        return view;
    }

}
