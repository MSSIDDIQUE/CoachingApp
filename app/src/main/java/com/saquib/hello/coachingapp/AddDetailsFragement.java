package com.saquib.hello.coachingapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddDetailsFragement extends android.support.v4.app.Fragment {

    private String ContactNo;
    private TextView Heading1, Heading2, Heading3, Heading4;
    private EditText FromEditText1, FromEditText2, About;
    private EditText YopEditText1, YopEditText2;
    private EditText Degree, Field, SinceEditText;
    private AutoCompleteTextView ClassesEditText, SubjectsEditText;
    private Button Save;
    public ProgressBar pb;
    private DatabaseReference dbr;
    private String[] Classes, Subjects;
    public AddDetailsFragement() {
        // Required empty public constructor
    }

    public AddDetailsFragement setContactNo(String ContactNo)
    {
        AddDetailsFragement f = new AddDetailsFragement();
        f.ContactNo = ContactNo;
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_details_layout, container, false);
        Heading1 = view.findViewById(R.id.Heading1);
        Heading2 = view.findViewById(R.id.Heading2);
        Heading3 = view.findViewById(R.id.Heading3);
        Heading4 = view.findViewById(R.id.Heading4);
        FromEditText1 = view.findViewById(R.id.FromEditText1);
        FromEditText2 = view.findViewById(R.id.FromEditText2);
        YopEditText1 = view.findViewById(R.id.YopEditText1);
        YopEditText2 = view.findViewById(R.id.YopEditText2);
        About = view.findViewById(R.id.AboutEditText);
        Degree = view.findViewById(R.id.DegreeEditText);
        Field = view.findViewById(R.id.FieldEditText);
        SinceEditText = view.findViewById(R.id.SinceEditText);
        Classes = getResources().getStringArray(R.array.Classes);
        Subjects = getResources().getStringArray(R.array.Subjects);
        ArrayAdapter<String> ClasssAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,Classes);
        ArrayAdapter<String> SubjectsAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,Subjects);
        ClassesEditText = view.findViewById(R.id.ClassesEditText);
        ClassesEditText.setAdapter(ClasssAdapter);
        SubjectsEditText = view.findViewById(R.id.SubjectsEditText);
        SubjectsEditText.setAdapter(SubjectsAdapter);
        pb = view.findViewById(R.id.pb);
        Save = view.findViewById(R.id.Save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String f1 = FromEditText1.getText().toString();
                final String f2 = FromEditText2.getText().toString();
                final String h1= Heading1.getText().toString();
                final String h2= Heading2.getText().toString();
                final String h3= Heading3.getText().toString();
                final String h4= Heading4.getText().toString();
                final String y1= YopEditText1.getText().toString();
                final String y2= YopEditText2.getText().toString();
                final String ab= About.getText().toString();
                final String d= Degree.getText().toString();
                final String f= Field.getText().toString();
                final String si= SinceEditText.getText().toString();
                final String c= ClassesEditText.getText().toString();
                final String su= SubjectsEditText.getText().toString();
                if(!((MainActivity)getActivity()).isConnected())
                {
                    Toast.makeText(getContext(),"Unable to Upload the Data, Make sure you are connected to Network",Toast.LENGTH_LONG).show();
                    return;
                }
                if(f1.isEmpty())
                {
                    FromEditText1.setError("Enter University Name First");
                    FromEditText1.requestFocus();
                    Toast.makeText(getContext(), "Enter the University Name First", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(f2.isEmpty())
                {
                    FromEditText2.setError("Enter School Name First");
                    FromEditText2.requestFocus();
                    Toast.makeText(getContext(), "Enter School Name First", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(y1.isEmpty())
                {
                    YopEditText1.setError("Enter Passing Year of Graduation");
                    YopEditText1.requestFocus();
                    Toast.makeText(getContext(), "Enter Passing Year of Graduation", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(y2.isEmpty())
                {
                    YopEditText2.setError("Enter Passing Year of HighSchool");
                    YopEditText2.requestFocus();
                    Toast.makeText(getContext(), "Enter Passing Year of HighSchool", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(d.isEmpty())
                {
                    Degree.setError("Enter the Name of Degree/Diploma");
                    Degree.requestFocus();
                    Toast.makeText(getContext(),"Enter the Name of Degree/Diploma",Toast.LENGTH_LONG).show();
                    return;
                }
                if(si.isEmpty())
                {
                    SinceEditText.setError("Enter the Teaching Year");
                    SinceEditText.requestFocus();
                    Toast.makeText(getContext(),"Enter the Teaching Year",Toast.LENGTH_LONG).show();
                    return;
                }
                if(su.isEmpty())
                {
                    SubjectsEditText.setError("Enter the Subjects");
                    SubjectsEditText.requestFocus();
                    Toast.makeText(getContext(),"Enter the Subjects",Toast.LENGTH_LONG).show();
                    return;
                }
                if(c.isEmpty())
                {
                    ClassesEditText.setError("Enter the Classes");
                    ClassesEditText.requestFocus();
                    Toast.makeText(getContext(),"Enter the Classes",Toast.LENGTH_LONG).show();
                }
                pb.setVisibility(View.VISIBLE);
                dbr = FirebaseDatabase.getInstance().getReference();
                dbr.child("Teachers").child(ContactNo).child("Education").child(h1).setValue(new TeachersData(f1,y1,d,f));
                dbr.child("Teachers").child(ContactNo).child("Education").child(h3).setValue(new TeachersData(si,c,su));
                dbr.child("Teachers").child(ContactNo).child("Education").child(h2).setValue(new TeachersData(f2,y2));
                dbr.child("Teachers").child(ContactNo).child("Education").child(h4).setValue(new TeachersData(ab));
                Toast.makeText(getContext(), "Data is Uploaded Successfully", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            }
        });
        return view;
    }

}