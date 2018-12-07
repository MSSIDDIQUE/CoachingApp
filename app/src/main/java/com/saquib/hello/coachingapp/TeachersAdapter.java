package com.saquib.hello.coachingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.MyViewHolder> {

    ArrayList<ArrayList<TeachersData>> ListArray;
    private Context context;
    private TeachersData d1,d5;

    public TeachersAdapter(ArrayList <ArrayList<TeachersData>> List, Context context){

        this.ListArray = List;
        this.context = context;
    }

    @NonNull
    @Override
    public TeachersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item_layout, parent, false);
        return new TeachersAdapter.MyViewHolder(view, context, ListArray);
    }

    @Override
    public void onBindViewHolder(final TeachersAdapter.MyViewHolder holder, final int position) {
        d1 = ListArray.get(position).get(0);
        d5 = ListArray.get(position).get(4);
        holder.img.setTransitionName("SharedImage"+position);
        holder.name.setText(d5.getName());
        holder.subjects.setText(d1.getSubjects());
        if(!d5.getImgurl().equals(""))
        {
            Picasso.get().load(d5.getImgurl()).fit().centerCrop().into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return ListArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView subjects;
        public ImageView img;
        public Context context;
        public ArrayList<ArrayList<TeachersData> >data;
        public MyViewHolder(View itemView, final Context context, final ArrayList<ArrayList<TeachersData>> data) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.Name);
            subjects = (TextView)itemView.findViewById(R.id.Subjects);
            img =(ImageView)itemView.findViewById(R.id.Profile);
            this.data= data;
            this.context=context;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d("Hello", String.valueOf(position));
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                    TeacherDescription f = new TeacherDescription().setData(position, data.get(position), context);
                    f.setSharedElementEnterTransition(new DetailTransition());
                    f.setEnterTransition(new Explode());
                    f.setExitTransition(new Explode());
                    f.setSharedElementReturnTransition(new DetailTransition());
                    fm.beginTransaction().addSharedElement(img,"SharedImage"+position).add(R.id.screen_area,f )
                            .addToBackStack("MyBackStack").commit();
                }
            });
        }

    }
}
