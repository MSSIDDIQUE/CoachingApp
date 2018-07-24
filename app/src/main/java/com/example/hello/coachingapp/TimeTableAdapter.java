package com.example.hello.coachingapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.MyViewHolder> {


    ArrayList <TimeData> listArray;
    private Context context;

    public TimeTableAdapter(Context context){

        this.context = context;
    }

    public TimeTableAdapter(ArrayList List, Context context){

        this.listArray = List;
        this.context = context;
    }

    @Override
    public TimeTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item_layout, parent, false);
        return new TimeTableAdapter.MyViewHolder(view, context, listArray);
    }

    @Override
    public void onBindViewHolder(TimeTableAdapter.MyViewHolder holder, int position) {
        TimeData data = listArray.get(position);
        holder.Class.setText(data.getStandard());
        holder.Subject.setText(data.getSubject());
        holder.Teacher.setText(data.getTeacher());
        holder.From.setText(data.getStartingTime());
        holder.To.setText(data.getEndingTime());
        String days = data.getDays();
        ArrayList<String>Tokens= new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(days);
        while(st.hasMoreTokens())
        {
            Tokens.add(st.nextToken());
        }
        for(int i=0;i<Tokens.size();i++)
        {
            if(String.valueOf(Tokens.get(i)).equals("Sunday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Sunday);
                holder.tb.setChecked(true);
            }
            if(String.valueOf(Tokens.get(i)).equals("Monday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Monday);
                holder.tb.setChecked(true);
            }
            if(String.valueOf(Tokens.get(i)).equals("Tuesday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Tuesday);
                holder.tb.setChecked(true);

            }
            if(String.valueOf(Tokens.get(i)).equals("Wednesday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Wednesday);
                holder.tb.setChecked(true);
            }
            if(String.valueOf(Tokens.get(i)).equals("Thursday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Thursday);
                holder.tb.setChecked(true);
            }
            if(String.valueOf(Tokens.get(i)).equals("Friday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Friday);
                holder.tb.setChecked(true);
            }
            if(String.valueOf(Tokens.get(i)).equals("Saturday"))
            {
                holder.tb = holder.daypicker.findViewById(R.id.Saturday);
                holder.tb.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Class;
        public TextView Subject;
        public TextView Teacher;
        public TextView From;
        public TextView To;
        public View view;
        public CardView daypicker;
        public ToggleButton tb;
        public Context context;
        public ArrayList<TimeData> data;
        public MyViewHolder(View itemView, final Context context, final ArrayList<TimeData> data) {
            super(itemView);
            daypicker = (CardView) itemView.findViewById(R.id.DayPicker);
            Class = (TextView)itemView.findViewById(R.id.Class);
            Subject = (TextView)itemView.findViewById(R.id.Subject);
            Teacher = (TextView)itemView.findViewById(R.id.Teacher);
            From = (TextView)itemView.findViewById(R.id.From);
            To = (TextView)itemView.findViewById(R.id.To);
            this.context=context;
            this.data=data;
        }

    }
}
