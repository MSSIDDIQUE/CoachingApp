package com.example.hello.coachingapp;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

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
    public TextView Branch;
    public TextView From;
    public TextView To;
    public android.widget.ImageView ChangeFrom;
    public android.widget.ImageView ChangeTo;
    static final int DIALOG_ID=0;
    int Hour;
    int Minute;
    boolean isTeacher;
    static final int TIME_DIALOG_ID = 1111;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.time_item_layout, container, false);
        Class =  view.findViewById(R.id.Class);
        Subject = view.findViewById(R.id.Subject);
        Branch = view.findViewById(R.id.Branch);
        Teacher = view.findViewById(R.id.Teacher);
        From = view.findViewById(R.id.From);
        To = view.findViewById(R.id.To);
        ChangeFrom = view.findViewById(R.id.ChangeFrom);
        final java.util.Calendar c = java.util.Calendar.getInstance();
        ChangeTo = view.findViewById(R.id.ChangeTo);
        Hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        Minute = c.get(java.util.Calendar.MINUTE);
        SharedPreferences UserType = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean IsTeacher=UserType.getBoolean("Teacher",false);
        if(IsTeacher)
        {
           ChangeTo.setVisibility(View.VISIBLE);
           ChangeFrom.setVisibility(View.VISIBLE);
        }
        return view;
    }
}