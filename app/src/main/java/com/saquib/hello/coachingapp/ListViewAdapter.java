package com.saquib.hello.coachingapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyViewHolder> {

    private ArrayList<StudyMaterialData> data;
    private Context context;
    private View view;
    private CardView download;
    private TextView bookTitle;
    private DownloadManager dm;
    private int REQUEST_CODE = 1;
    private MainActivity.MyAdapterListener listener;

    public ListViewAdapter(ArrayList<StudyMaterialData>data, Context context, MainActivity.MyAdapterListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_layout, parent, false);
        return new ListViewAdapter.MyViewHolder(view, context, data);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.MyViewHolder holder, int position) {
        String d = data.get(position).getTitle();
        String s[] = d.split("/");
        d =s[s.length-1];
        holder.Title.setText(d);
        if(!data.get(position).getCoverurl().equals(""))
        {
            Picasso.get().load(data.get(position).getCoverurl()).fit().centerCrop().into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Title;
        public View view;
        public ImageView img;
        public  Context context;
        public ArrayList<StudyMaterialData> data;
        public RecyclerView rv;
        public ProgressBar pb;
        public Button download,preview;
        public MyViewHolder(View itemView, final Context context, final ArrayList<StudyMaterialData> data) {
            super(itemView);
            this.context=context;
            this.data=data;
            this.img = itemView.findViewById(R.id.BookImage);
            download = itemView.findViewById(R.id.Download);
            preview = itemView.findViewById(R.id.Preview);
            Title = (TextView)itemView.findViewById(R.id.bookTitle);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.FromClickListner(v,getAdapterPosition());
                }
            });
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.ToOnClickListner(v,getAdapterPosition());
                }
            });
        }

    }
}
