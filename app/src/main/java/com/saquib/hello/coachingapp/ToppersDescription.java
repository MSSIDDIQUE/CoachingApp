package com.saquib.hello.coachingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ToppersDescription extends android.support.v4.app.Fragment{
    ArrayList<ToppersData> listArray;
    int position;
    View view;
    public TextView name;
    public TextView rollno;
    public TextView stream;
    public TextView streamtxt;
    public TextView school;
    public ImageView img;
    public ImageView result;
    public Context context;
    public ToppersDescription setData(int position, ArrayList<ToppersData> d, Context context)
    {
        ToppersDescription td = new ToppersDescription();
        td.listArray = d;
        td.position = position;
        td.context = context;
        return td;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.toppers_description_layout, container, false);
        //mapping data
        name = (TextView) view.findViewById(R.id.NameField);
        rollno = (TextView) view.findViewById(R.id.RollNoField);
        stream = (TextView) view.findViewById(R.id.StreamField);
        streamtxt = (TextView) view.findViewById(R.id.Stream);
        school = (TextView) view.findViewById(R.id.SchoolField);
        img = (ImageView) view.findViewById(R.id.image1);
        result = (ImageView) view.findViewById(R.id.image3);
        view.setClickable(false);
        LoadData();
        return view;
    }
    public void LoadData()
    {
        ToppersData data = listArray.get(position);
        name.setText(data.getName());
        rollno.setText(data.getRollno());
        if(data.getStream().equals(""))
        {
            streamtxt.setVisibility(View.GONE);
        }
        else
        {
            stream.setText(data.getStream());
        }
        school.setText(data.getPercentage());
        Picasso.get().load(data.getImgurl()).fit().centerCrop().into(img);
        Picasso.get().load(data.getResulturl()).fit().centerInside().into(result);
    }

}
