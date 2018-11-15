package com.example.hello.coachingapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyViewHolder> {

    private ArrayList<StudyMaterialData> data;
    private Context context;
    private View view;
    private CardView download;
    private TextView bookTitle;
    private DownloadManager dm;
    private int REQUEST_CODE = 1;

    public ListViewAdapter(ArrayList<StudyMaterialData>data, Context context) {
        this.data = data;
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Title;
        public View view;
        public  Context context;
        public ArrayList<StudyMaterialData> data;
        public RecyclerView rv;
        public ProgressBar pb;
        public CardView download;
        public MyViewHolder(View itemView, final Context context, final ArrayList<StudyMaterialData> data) {
            super(itemView);
            this.context=context;
            this.data=data;
            Title = (TextView)itemView.findViewById(R.id.bookTitle);
            download = itemView.findViewById(R.id.Download);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File book = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+Title.getText()).getAbsolutePath());
                    if(!book.exists())
                    {
                        dm = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
                        Uri bookurl = Uri.parse(data.get(getAdapterPosition()).getPdfurl());
                        DownloadManager.Request request = new DownloadManager.Request(bookurl);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,data.get(getAdapterPosition()).getTitle());
                        Long reference = dm.enqueue(request);
                        Toast.makeText(context,"Your pdf is Downloading See Notifications", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context,"This book is already downloaded, Please click upon the book for having a preview", Toast.LENGTH_LONG).show();
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File book = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+Title.getText()).getAbsolutePath());
                    if(book.exists())
                    {
                        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context,"This book is not downloaded yet, Please Download it first",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}
