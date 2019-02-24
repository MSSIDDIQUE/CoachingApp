package com.saquib.hello.coachingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class SorryFragment extends android.support.v4.app.Fragment {
    private String text;
    public boolean visible;

    public SorryFragment setText(String s, boolean visible)
    {
        SorryFragment fragment = new SorryFragment();
        fragment.text = s;
        fragment.visible = visible;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sorry_layout, container, false);
        Button b = view.findViewById(R.id.LoginButton);
        if(visible==false)
        {
            b.setVisibility(View.GONE);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle(R.string.RegisterLogin);
                FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area,new RegisterFragment()).commit();
            }
        });
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(text);
        return view;
    }
}
