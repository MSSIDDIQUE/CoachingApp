package com.saquib.hello.coachingapp;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.MyViewHolder> {

    ArrayList <String> ListArray;
    ArrayList<StudyMaterialData>data;
    ListViewAdapter adapter;

    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MaterialListAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public MaterialListAdapter(Context context){

        this.context = context;
    }

    public MaterialListAdapter(ArrayList List, Context context){

        this.ListArray = List;
        this.context = context;
    }
    @Override
    public MaterialListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_1, parent, false);
        return new MaterialListAdapter.MyViewHolder(view, context, ListArray);
    }

    @Override
    public void onBindViewHolder(final MaterialListAdapter.MyViewHolder holder, int position) {
        String d = ListArray.get(position);
        String s[] = d.split("/");
        d =s[s.length-1];
        holder.Title.setText(d);
    }


    @Override
    public int getItemCount() {
        return ListArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Title;
        public View view;
        public  Context context;
        public ArrayList<String> data;
        public RecyclerView rv;
        public ProgressBar pb;
        public ListView ListOfBooks;
        public LinearLayout PlaceHolder;
        public MyViewHolder(View itemView, final Context context, final ArrayList<String> data) {
            super(itemView);
            Title = (TextView)itemView.findViewById(R.id.Title);
            this.context=context;
            this.data=data;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d("Hello", String.valueOf(position));
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.screen_area, new ListViewFragment().setDbr(position, data, context))
                            .addToBackStack("MyBackStack").commit();
                }
            });
        }

    }
}
