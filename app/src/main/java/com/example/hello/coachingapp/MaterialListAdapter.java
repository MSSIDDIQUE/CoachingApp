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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_type_item_layout, parent, false);
        return new MaterialListAdapter.MyViewHolder(view, context, ListArray);
    }

    @Override
    public void onBindViewHolder(final MaterialListAdapter.MyViewHolder holder, int position) {
        String d = ListArray.get(position);
        holder.Title.setText(d);
        data = new ArrayList<StudyMaterialData>();
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("StudyMaterial").child(d);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StudyMaterialData sd = ds.getValue(StudyMaterialData.class);
                    data.add(sd);
                }
                adapter=new ListViewAdapter(data,context);
                adapter.notifyDataSetChanged();
                holder.ListOfBooks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        public MyViewHolder(View itemView, final Context context, final ArrayList<String> data) {
            super(itemView);
            Title = (TextView)itemView.findViewById(R.id.Heading);
            ListOfBooks = (ListView)itemView.findViewById(R.id.ListOfBooks);
            this.context=context;
            this.data=data;
        }

    }
}
