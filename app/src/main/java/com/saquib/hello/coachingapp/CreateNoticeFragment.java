package com.saquib.hello.coachingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNoticeFragment extends Fragment {


    private View view;
    private EditText Title, Message;
    private Button create;
    DatabaseReference dbr;
    public ProgressBar pb;
    public  CreateNoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_notice_layout,container,false);
        Title = view.findViewById(R.id.Title);
        Message = view.findViewById(R.id.MessageBody);
        pb = view.findViewById(R.id.progressBar7);
        pb.setVisibility(View.GONE);
        create = view.findViewById(R.id.createnotice);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbr = FirebaseDatabase.getInstance().getReference("messages");
                final String title,message;
                title = Title.getText().toString();
                message = Message.getText().toString();
                if(title.isEmpty())
                {
                    Title.setError("Please Enter the Title");
                    Title.requestFocus();
                    return;
                }

                if(message.isEmpty())
                {
                    Message.setError("Please Enter the Title");
                    Message.requestFocus();
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                dbr.push().setValue(new MessageData(title,message,"Admin"));
                pb.setVisibility(View.GONE);
            }
        });
        return view;
    }

}
