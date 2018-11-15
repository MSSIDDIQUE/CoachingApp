package com.example.hello.coachingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;

public class CreateClassFragment extends android.support.v4.app.Fragment {
    public View view;
    public Spinner Class;
    public Spinner Subject;
    public TextView Teacher;
    public TextView From;
    public TextView To;
    public TextView msg;
    public android.widget.ImageView ChangeFrom;
    public android.widget.ImageView ChangeTo;
    public ToggleButton tb;
    public CardView daypicker;
    public Spinner SelectClass,SelectBranch,SelectSubject ;
    public Button Create;
    public EditText TeachersName;
    public ToggleButton Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday;
    public String days;
    public ProgressBar pb;
    public MainActivity.MyAdapterListener onClickListener;

    public CreateClassFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_class_layout, container, false);
        Class =  view.findViewById(R.id.SelectClass);
        Subject = view.findViewById(R.id.SelectSubject);
        TeachersName = view.findViewById(R.id.TeacherName);
        From = view.findViewById(R.id.From);
        To = view.findViewById(R.id.To);
        daypicker = view.findViewById(R.id.DayPicker);
        Sunday = daypicker.findViewById(R.id.Sunday);
        Monday = daypicker.findViewById(R.id.Monday);
        Tuesday = daypicker.findViewById(R.id.Tuesday);
        Wednesday = daypicker.findViewById(R.id.Wednesday);
        Thursday = daypicker.findViewById(R.id.Thursday);
        Friday = daypicker.findViewById(R.id.Friday);
        Saturday = daypicker.findViewById(R.id.Saturday);
        SelectBranch = view.findViewById(R.id.SelectBranch);
        SelectClass = view.findViewById(R.id.SelectClass);
        SelectSubject = view.findViewById(R.id.SelectSubject);
        pb = view.findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);
        setClick(true);
        Create = view.findViewById(R.id.Create);
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days = getDays();
                CreateClass();
            }
        });
        ChangeFrom = view.findViewById(R.id.ChangeFrom);
        ChangeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onButtonClicked(From);
            }
        });
        ChangeTo = view.findViewById(R.id.ChangeTo);
        ChangeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onButtonClicked(To);
            }
        });
        return view;
    }

    public String getDays()
    {
        String days="";
        if(Sunday.isChecked())
        {
            days+="Sunday ";
        }
        if(Monday.isChecked())
        {
            days+="Monday ";
        }
        if(Tuesday.isChecked())
        {
            days+="Tuesday ";
        }
        if(Wednesday.isChecked())
        {
            days+="Wednesday ";
        }
        if(Thursday.isChecked())
        {
            days+="Thursday ";
        }
        if(Friday.isChecked())
        {
            days+="Friday ";
        }
        if(Saturday.isChecked())
        {
            days+="Saturday ";
        }
        return days;
    }

    public void CreateClass()
    {
        if(SelectClass.getSelectedItem()==null)
        {
            Toast.makeText(getContext(),"Please Select the Class first", Toast.LENGTH_LONG);
            return;
        }
        if(SelectBranch.getSelectedItem()==null)
        {
            Toast.makeText(getContext(),"Please Select the Branch first", Toast.LENGTH_LONG);
            return;
        }
        if(SelectSubject.getSelectedItem()==null)
        {
            Toast.makeText(getContext(),"Please Select the Subject first", Toast.LENGTH_LONG).show();
            return;
        }
        String name= TeachersName.getText().toString();
        if(name.isEmpty())
        {
            TeachersName.setError("Please Enter the name of Teacher");
            TeachersName.requestFocus();
            return;
        }
        if(days.matches(""))
        {
            Toast.makeText(getContext(),"Please Select The Days of your Class",Toast.LENGTH_LONG).show();
            return;
        }
        String Class = SelectClass.getSelectedItem().toString();
        String Subject = SelectSubject.getSelectedItem().toString();
        String Branch = SelectBranch.getSelectedItem().toString();
        String StartingTime = From.getText().toString();
        String EndingTime = To.getText().toString();
        pb.setVisibility(View.VISIBLE);
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child(Branch).child(Class).child(Subject).setValue(new TimeData(Class,Subject,name,days,StartingTime,EndingTime)).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getContext(),"Sorry this class is already created",Toast.LENGTH_LONG);
            }
        });
        pb.setVisibility(View.GONE);
        Toast.makeText(getContext(),"Class is Created Successfully",Toast.LENGTH_LONG).show();
    }

    public void setClick(boolean value)
    {
        tb = daypicker.findViewById(R.id.Sunday);
        tb.setClickable(value);
        tb = daypicker.findViewById(R.id.Saturday);
        tb.setClickable(value);
        tb = daypicker.findViewById(R.id.Monday);
        tb.setClickable(value);
        tb = daypicker.findViewById(R.id.Tuesday);
        tb.setClickable(value);
        tb = daypicker.findViewById(R.id.Wednesday);
        tb.setClickable(value);
        tb = daypicker.findViewById(R.id.Thursday);
        tb.setClickable(value);
        tb = daypicker.findViewById(R.id.Friday);
        tb.setClickable(value);
    }

}
