package com.example.hello.coachingapp;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddStudyMaterialFragment extends android.support.v4.app.Fragment {


    private View view;
    private Spinner SelectSubject, SelectClass, SelectMateralType;
    private Button Choose, Upload;
    private Uri ImageFilePath;
    ProgressBar pb;

    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
    ArrayList<String>Classes = new ArrayList<String>();
    ArrayList<String>Type = new ArrayList<String>();

    public AddStudyMaterialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_study_material_layout,container, false);
        SelectClass = view.findViewById(R.id.SelectClass);
        SelectSubject = view.findViewById(R.id.SelectSubject);
        SelectMateralType = view.findViewById(R.id.SelectMaterailType);
        pb = view.findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        Choose = view.findViewById(R.id.Choose);
        Upload = view.findViewById(R.id.Add);
        Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select pdf file"), 1);
            }
        });

        SelectSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dbr.child("StudyMaterial").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            Classes.add(ds.getKey().toString());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, Classes);
                        SelectClass.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SelectClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dbr.child("StudyMaterial").child(SelectSubject.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            Type.add(ds.getKey().toString());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, Type);
                        SelectClass.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pb.setVisibility(View.VISIBLE);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            ImageFilePath = data.getData();
        }
    }

    private String getFileExtension(Uri url)
    {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(url));
    }

}
