package com.example.hello.coachingapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.MyViewHolder> {



    ArrayList <TimeData> listArray;
    private Context context;
    private DatabaseReference dbr;
    public MainActivity.MyAdapterListener onClickListener;

    public TimeTableAdapter(Context context){

        this.context = context;
    }

    public TimeTableAdapter(ArrayList List, Context context, DatabaseReference dbr, MainActivity.MyAdapterListener listener){

        this.listArray = List;
        this.context = context;
        this.dbr = dbr;
        onClickListener = listener;
    }

    @Override
    public TimeTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item_layout, parent, false);
        return new TimeTableAdapter.MyViewHolder(view, context, listArray);
    }

    @Override
    public void onBindViewHolder(final TimeTableAdapter.MyViewHolder holder, final int position) {
        TimeData data = listArray.get(position);
        holder.Class.setText(data.getStandard());
        holder.Subject.setText(data.getSubject());
        holder.Teacher.setText(data.getTeacher());
        holder.From.setText(data.getStartingTime());
        holder.To.setText(data.getEndingTime());
        holder.msg.setVisibility(View.GONE);
        String days = data.getDays();
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.Edit.getText().equals("Edit"))
                {
                    holder.msg.setVisibility(View.VISIBLE);
                    holder.msg.setText("Please Select the days of your Class");
                    holder.setClick(true);
                    holder.Edit.setText("Update");
                    holder.ChangeTo.setVisibility(View.VISIBLE);
                    holder.ChangeFrom.setVisibility(View.VISIBLE);
                }
                else if(holder.Edit.getText().equals("Update"))
                {
                    String c = holder.Class.getText().toString();
                    String s = holder.Subject.getText().toString();
                    String t = holder.Teacher.getText().toString();
                    String st = holder.From.getText().toString();
                    String et = holder.To.getText().toString();
                    String d = holder.getDays();
                    holder.setClick(false);

                    dbr.child(c).child(s).setValue(new TimeData(c,s,t,d,st,et)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,"The Time Table is Updated Successfully",Toast.LENGTH_LONG).show();
                            holder.Edit.setText("Edit");
                            holder.msg.setVisibility(View.GONE);
                            holder.ChangeTo.setVisibility(View.GONE);
                            holder.ChangeFrom.setVisibility(View.GONE);
                        }
                    });
                    listArray.clear();
                    notifyDataSetChanged();
                }
            }
        });
        ArrayList<String>Tokens= new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(days);
        SharedPreferences UserType = PreferenceManager.getDefaultSharedPreferences(context);
        if(UserType.getBoolean("Teacher",false))
        {
            holder.Edit.setVisibility(View.VISIBLE);
        }
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

    public List<TimeData> getList() {
        return this.listArray;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Class;
        public TextView Subject;
        public TextView Teacher;
        public TextView From;
        public TextView To;
        public View view;
        public Button Edit;
        public CardView daypicker;
        public ToggleButton tb;
        public LinearLayout linearLayout;
        public TextView msg;
        public Context context;
        public ArrayList<TimeData> data;
        public android.widget.ImageView ChangeFrom;
        public android.widget.ImageView ChangeTo;
        public ToggleButton Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday;

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

        public void removeItem(int position,TimeTableAdapter.MyViewHolder holder) {
            int newPosition = holder.getAdapterPosition();
            listArray.remove(newPosition);
            notifyItemChanged(newPosition);
        }

        public MyViewHolder(View itemView, final Context context, final ArrayList<TimeData> data) {
            super(itemView);
            daypicker = (CardView) itemView.findViewById(R.id.DayPicker);
            Sunday = (ToggleButton) daypicker.findViewById(R.id.Sunday);
            Monday = (ToggleButton)daypicker.findViewById(R.id.Monday);
            Tuesday = (ToggleButton)daypicker.findViewById(R.id.Tuesday);
            Wednesday = (ToggleButton)daypicker.findViewById(R.id.Wednesday);
            Thursday = (ToggleButton)daypicker.findViewById(R.id.Thursday);
            Friday = (ToggleButton)daypicker.findViewById(R.id.Friday);
            Saturday = (ToggleButton)daypicker.findViewById(R.id.Saturday);
            Class = (TextView)itemView.findViewById(R.id.Class);
            Subject = (TextView)itemView.findViewById(R.id.Subject);
            Teacher = (TextView)itemView.findViewById(R.id.Teacher);
            From = (TextView)itemView.findViewById(R.id.From);
            To = (TextView)itemView.findViewById(R.id.To);
            Edit = (Button)itemView.findViewById(R.id.Edit);
            ChangeFrom = (ImageView) itemView.findViewById(R.id.ChangeFrom);
            ChangeTo = (ImageView) itemView.findViewById(R.id.ChangeTo);
            msg = (TextView) itemView.findViewById(R.id.msg);
            this.context=context;
            this.data=data;
            ChangeFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.FromClickListner(v,getAdapterPosition());
                }
            });
            ChangeTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.ToOnClickListner(v,getAdapterPosition());
                }
            });
        }
    }
}
