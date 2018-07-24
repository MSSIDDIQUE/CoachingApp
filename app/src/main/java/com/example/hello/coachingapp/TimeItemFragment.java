package com.example.hello.coachingapp;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimeItemFragment extends android.support.v4.app.Fragment {
    public View view;
    public TextView Class;
    public TextView Subject;
    public TextView Teacher;
    public TextView From;
    public TextView To;
    public TextView msg;
    public android.widget.ImageView ChangeFrom;
    public android.widget.ImageView ChangeTo;
    public LinearLayout linearLayout;
    public CardView daypicker;
    public ToggleButton tb;
    Button Edit;
    static final int DIALOG_ID=0;
    int Hour;
    int Minute;
    boolean isTeacher;
    static final int TIME_DIALOG_ID = 1111;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences UserType = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean IsTeacher=UserType.getBoolean("Teacher",false);
        view = inflater.inflate(R.layout.time_item_layout, container, false);
        Class =  view.findViewById(R.id.Class);
        Subject = view.findViewById(R.id.Subject);
        Teacher = view.findViewById(R.id.Teacher);
        From = view.findViewById(R.id.From);
        To = view.findViewById(R.id.To);
        msg = view.findViewById(R.id.msg);
        msg.setVisibility(View.GONE);
        ChangeFrom = view.findViewById(R.id.ChangeFrom);
        ChangeTo = view.findViewById(R.id.ChangeTo);
        linearLayout = view.findViewById(R.id.daypicker_layout);
        daypicker = view.findViewById(R.id.DayPicker);
        Edit = view.findViewById(R.id.Edit);
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Edit.getText().equals("Edit"))
                {
                    linearLayout.setClickable(true);
                    msg.setVisibility(View.VISIBLE);
                    msg.setText("Please Select the days of your Class");
                    setClick(true);
                    Edit.setText("Update");
                }
                else if(Edit.getText().equals("Update"))
                {
                    Toast.makeText(getContext(),"The Time Table is Updated Successfully",Toast.LENGTH_LONG).show();
                    setClick(false);
                    Edit.setText("Edit");
                    msg.setVisibility(View.GONE);
                }
            }
        });
        final java.util.Calendar c = java.util.Calendar.getInstance();
        Hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        Minute = c.get(java.util.Calendar.MINUTE);
        if(IsTeacher)
        {
            Edit.setVisibility(View.VISIBLE);
           ChangeTo.setVisibility(View.VISIBLE);
           ChangeFrom.setVisibility(View.VISIBLE);
        }
        return view;
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