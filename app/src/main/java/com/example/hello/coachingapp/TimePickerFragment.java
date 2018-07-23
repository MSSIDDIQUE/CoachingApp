package com.example.hello.coachingapp;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private String txtid=null;

    public TimePickerFragment SetTextId(String txtid)
    {
        TimePickerFragment tpf = new TimePickerFragment();
        tpf.txtid=txtid;
        return tpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.time_picker_layout, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK
                ,this, hour, minute, DateFormat.is24HourFormat(getActivity()));

        //You can set a simple text title for TimePickerDialog
        //tpd.setTitle("Title Of Time Picker Dialog");

        /*.........Set a custom title for picker........*/
        TextView tvTitle = new TextView(getActivity());
        if(txtid=="From")
        {
            tvTitle.setText("Set the commencement time of class");
        }
        else
        {
            tvTitle.setText("Set the finish time of class");
        }
        tvTitle.setTextColor(Color.parseColor("#ea0583"));
        tvTitle.setBackgroundColor(Color.parseColor("#ffea16"));
        tvTitle.setPadding(5, 3, 5, 3);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tpd.setCustomTitle(tvTitle);
        /*.........End custom title section........*/

        return tpd;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        TextView From = (TextView) getActivity().findViewById(R.id.From);
        TextView To = (TextView) getActivity().findViewById(R.id.To);
        String aMpM = "AM";
        if(hourOfDay >11)
        {
            aMpM = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        if(hourOfDay>11)
        {
            currentHour = hourOfDay - 12;
        }
        else
        {
            currentHour = hourOfDay;
        }
        //Display the user changed time on TextView
        DecimalFormat formatter = new DecimalFormat("00");
        String hr =String.valueOf(currentHour);
        if(currentHour==0)
        {
            hr = formatter.format(currentHour);
        }
        String min = formatter.format(minute);
        if (txtid=="From")
        {
            From.setText(hr + ":" + min+ " " + aMpM);
        }
        else
        {
            To.setText(hr + ":" + min+ " " + aMpM);
        }
    }
}
