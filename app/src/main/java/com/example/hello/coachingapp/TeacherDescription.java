package com.example.hello.coachingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeacherDescription extends android.support.v4.app.Fragment{
    ArrayList<TeachersData> listArray;
    int position;
    View view;
    public TextView name;
    public TextView subjects;
    public TextView classes;
    public TextView since;
    public ImageView img;
    public ImageView result;
    public Context context;
    public TextView fromH,fromG,yopH,yopG,field,degree, about;
    public TeacherDescription setData(int position, ArrayList<TeachersData> d, Context context)
    {
        TeacherDescription td = new TeacherDescription();
        td.listArray = d;
        td.position = position;
        td.context = context;
        return td;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.teacher_details_layout, container, false);
        //mapping data
        name = (TextView) view.findViewById(R.id.TeacherName);
        subjects = (TextView) view.findViewById(R.id.SubjectsTxt);
        classes = (TextView) view.findViewById(R.id.ClassesTxt);
        since = (TextView) view.findViewById(R.id.SinceTxt);
        fromH = (TextView) view.findViewById(R.id.FromEditText2);
        yopH = (TextView) view.findViewById(R.id.YopEditText2);
        fromG = (TextView) view.findViewById(R.id.FromEditText3);
        yopG = (TextView) view.findViewById(R.id.YopEditText3);
        degree = (TextView) view.findViewById(R.id.DegreeEditText);
        field = (TextView) view.findViewById(R.id.FieldEditText);
        about = (TextView) view.findViewById(R.id.AboutEditText);
        img = (ImageView) view.findViewById(R.id.image);
        LoadData();
        return view;
    }

    public void LoadData()
    {
        TeachersData d1 = listArray.get(0);
        TeachersData d2 = listArray.get(1);
        TeachersData d3 = listArray.get(2);
        TeachersData d4 = listArray.get(3);
        TeachersData d5 = listArray.get(4);
        name.setText(d5.getName());
        subjects.setText(d1.getSubjects());
        classes.setText(d1.getClasses());
        since.setText(d1.getSince());
        fromH.setText(d3.getFrom());
        fromG.setText(d2.getFrom());
        yopG.setText(d2.getYop());
        yopH.setText(d3.getYop());
        degree.setText(d2.getDegree());
        field.setText(d2.getField());
        about.setText(d4.getAbout());
        Picasso.get().load(d5.getImgurl()).fit().centerCrop().into(img);
    }

}
