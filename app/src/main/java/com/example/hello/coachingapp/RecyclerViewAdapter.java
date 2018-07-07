package com.example.hello.coachingapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.health.SystemHealthManager;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList <ToppersData> listArray;
    private Context context;
    private OnItemClickListener listener;
    private String ViewType;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public RecyclerViewAdapter(Context context, String type){

        this.context = context;
        this.ViewType = type;
    }

    public RecyclerViewAdapter(ArrayList List,Context context, String type){

        this.listArray = List;
        this.context = context;
        this.ViewType = type;
    }
    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new MyViewHolder(view, context, listArray);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ToppersData data = listArray.get(position);
        holder.name.setText(data.getName());
        holder.percentage.setText(data.getPercentage());
        holder.session.setText(data.getSession());
        holder.stream.setText(data.getStream());
        Picasso.get().load(data.getImgurl()).fit().centerCrop().into(holder.img);
    }


    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView percentage;
        public TextView session;
        public TextView stream;
        public ImageView img;
        public View view;
        public  Context context;
        public ArrayList<ToppersData> data;
        public MyViewHolder(View itemView, final Context context, final ArrayList<ToppersData> data) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.TopperName);
            percentage = (TextView)itemView.findViewById(R.id.School);
            img =(ImageView)itemView.findViewById(R.id.image);
            session = (TextView)itemView.findViewById(R.id.RollNo);
            stream = (TextView)itemView.findViewById(R.id.Stream);
            this.context=context;
            this.data=data;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d("Hello", String.valueOf(position));
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                    fm.beginTransaction().add(R.id.screen_area, new ToppersDescription().setData(position, data, context))
                            .addToBackStack("MyBackStack").commit();
                }
            });
        }

    }
}
