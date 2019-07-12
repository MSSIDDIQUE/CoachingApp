package com.saquib.hello.coachingapp;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AddStudyMaterialFragment extends android.support.v4.app.Fragment {


    private View view;
    private Spinner SelectSubject, SelectClass, SelectMaterialType;
    private Button Choose, Upload, ChooseCover;
    private Uri ImageFilePath,PdfFilePath;
    private EditText BookTitle;
    ProgressBar pb;

    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
    StorageReference ImageStorageRef;
    public AddStudyMaterialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_study_material_layout,container, false);
        SelectClass = view.findViewById(R.id.SelectClass);
        SelectSubject = view.findViewById(R.id.SelectSubject);
        SelectMaterialType = view.findViewById(R.id.SelectMaterailType);
        BookTitle = view.findViewById(R.id.BookTitleEditText);
        pb = view.findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        Choose = view.findViewById(R.id.Choose);
        Upload = view.findViewById(R.id.Add);
        ChooseCover = view.findViewById(R.id.ChooseCover);
        Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select pdf file"), 1);
            }
        });

        ChooseCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Cover page Snapshot"), 2);
            }
        });

        dbr.child("StudyMaterial").addValueEventListener(new ValueEventListener() {

            ArrayList<String>Subjects = new ArrayList<String>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Subjects.add(ds.getKey());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, Subjects);
                SelectSubject.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SelectSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dbr.child("StudyMaterial").child(SelectSubject.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {

                    ArrayList<String>Classes = new ArrayList<String>();
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
                dbr.child("StudyMaterial").child(SelectSubject.getSelectedItem().toString()).child(SelectClass.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {

                    ArrayList<String>Type = new ArrayList<String>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            Type.add(ds.getKey().toString());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, Type);
                        SelectMaterialType.setAdapter(dataAdapter);
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

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String booktitle = BookTitle.getText().toString();
                final String subject = SelectSubject.getSelectedItem().toString();
                final String classs = SelectClass.getSelectedItem().toString();
                final String type = SelectMaterialType.getSelectedItem().toString();
                if(type.isEmpty())
                {
                    Toast.makeText(getContext(),"Please Select the Type of StydyMaterial",Toast.LENGTH_LONG).show();
                    SelectMaterialType.requestFocus();
                    return;
                }
                if(booktitle.isEmpty())
                {
                    BookTitle.setError("Enter the book title first");
                    BookTitle.requestFocus();
                    return;
                }
                else
                {
                    pb.setVisibility(View.VISIBLE);
                    ImageStorageRef = FirebaseStorage.getInstance().getReference("StudyMaterial"+"/"+subject+"/"+classs+"/"+type);
                    final StorageReference childRef = ImageStorageRef.child(booktitle+"-"+subject+"-"+classs+"." + getFileExtension(ImageFilePath));
                    UploadTask uploadTask = childRef.putFile(ImageFilePath);
                    final String uploadId = SelectSubject.getSelectedItem().toString();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return childRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                StudyMaterialData sd = new StudyMaterialData(booktitle+"-"+subject+"-"+classs+"." + getFileExtension(ImageFilePath),downloadUri.toString(),"" );
                                dbr.child("StudyMaterial").child(subject).child(classs).child(type).child(booktitle).setValue(sd);
                                Toast.makeText(getContext(), "Pdf file Uploaded Successfully", Toast.LENGTH_LONG).show();
                                final StorageReference newChildRef = ImageStorageRef.child(booktitle+"-"+subject+"-"+classs+"." + getFileExtension(PdfFilePath));
                                UploadTask uploadTask1 = newChildRef.putFile(PdfFilePath);
                                Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return newChildRef.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        dbr.child("StudyMaterial").child(subject).child(classs).child(type).child(booktitle).child("coverurl").setValue(task.getResult().toString());
                                        Toast.makeText(getContext(), "Cover Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                                        pb.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            ImageFilePath = data.getData();
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data.getData() != null) {
            PdfFilePath = data.getData();
        }
    }

    private String getFileExtension(Uri url)
    {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(url));
    }

}
