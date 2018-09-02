package com.example.hello.coachingapp;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_type_item_layout, parent, false);
        return new MaterialListAdapter.MyViewHolder(view, context, ListArray);
    }

    @Override
    public void onBindViewHolder(final MaterialListAdapter.MyViewHolder holder, int position) {
        String d = ListArray.get(position);
        String s[] = d.split("/");
        d =s[s.length-1];
        holder.Title.setText(d);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setId(position+1);
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        holder.PlaceHolder.addView(frameLayout,flp);
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        fm.beginTransaction().replace(frameLayout.getId(), new ListViewFragment().setDbr(position, ListArray, context))
                .commit();
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
            Title = (TextView)itemView.findViewById(R.id.Heading);
            this.context=context;
            this.data=data;int position = getAdapterPosition();
            PlaceHolder = itemView.findViewById(R.id.PlaceHolder);
            //Log.d("Hello", String.valueOf(position));
        }

    }
}
