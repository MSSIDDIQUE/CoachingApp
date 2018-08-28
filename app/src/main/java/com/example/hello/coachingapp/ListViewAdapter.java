package com.example.hello.coachingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<StudyMaterialData> data;
    private Context context;
    private View view;
    private CardView download;
    private TextView bookTitle;

    public ListViewAdapter(ArrayList<StudyMaterialData>data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.book_item_layout,null);
        download = view.findViewById(R.id.Download);
        bookTitle = view.findViewById(R.id.bookTitle);
        bookTitle.setText(data.get(position).getTitle());
        return view;
    }
}
