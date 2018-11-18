package com.saquib.hello.coachingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SorryFragment extends android.support.v4.app.Fragment {
    private String text;

    public SorryFragment setText(String s)
    {
        SorryFragment fragment = new SorryFragment();
        fragment.text = s;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sorry_layout, container, false);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(text);
        return view;
    }
}