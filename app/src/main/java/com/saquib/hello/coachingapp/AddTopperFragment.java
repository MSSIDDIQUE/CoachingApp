package com.saquib.hello.coachingapp;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddTopperFragment extends android.support.v4.app.Fragment {

    public View view;
    public ProgressDialog pd;
    public FloatingActionButton SelectImage;
    public FloatingActionButton SelectResult;
    public Button Upload;
    public static final int PICK_IMAGE_REQUEST=1;
    public Uri ImageFilePath, ResultFilePath;
    public ImageView TopperImage;
    public ImageView ResultImage;
    public EditText Name, RollNo, Percentage, SchoolName;
    public RadioButton Xth;
    public RadioButton XIIth;
    public TextView StreamText;
    public Spinner SelectStream;
    public Spinner StartingYear;
    public Spinner EndingYear;
    public String Session=null;

    public AddTopperFragment() {
    }
    StorageReference ImageStorageRef= FirebaseStorage.getInstance().getReference("ToppersImages");
    StorageReference ResultStorageRef= FirebaseStorage.getInstance().getReference("ResultsImages");
    DatabaseReference dbr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.add_topper_layout, container, false);
        SelectImage = view.findViewById(R.id.SelectImage);
        SelectResult = view.findViewById(R.id.SelectResult);
        Name = view.findViewById(R.id.Name);
        RollNo = view.findViewById(R.id.RollNo);
        Percentage = view.findViewById(R.id.Percentage);
        SchoolName = view.findViewById(R.id.SchoolName);
        StreamText = view.findViewById(R.id.StreamText);
        Xth = view.findViewById(R.id.ClassXth);
        XIIth = view.findViewById(R.id.ClassXIIth);
        Upload = view.findViewById(R.id.Upload);
        TopperImage = view.findViewById(R.id.TopperImage);
        ResultImage = view.findViewById(R.id.ResultImage);
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2010; i <= thisYear+1; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        StartingYear= view.findViewById(R.id.StartingYear);
        EndingYear= view.findViewById(R.id.EndingYear);
        SelectStream = view.findViewById(R.id.SelectStream);
        StartingYear.setAdapter(adapter);
        EndingYear.setAdapter(adapter);
        Xth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StreamText.setVisibility(View.GONE);
                SelectStream.setVisibility(View.GONE);
            }
        });
        XIIth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreamText.setVisibility(View.VISIBLE);
                SelectStream.setVisibility(View.VISIBLE);
            }
        });
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Uploading....");
        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image of Topper"), 1);
            }
        });

        SelectResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Result Snapshot"), 2);
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = Name.getText().toString();
                final String rollno = RollNo.getText().toString();
                String temp = Percentage.getText().toString();
                String temp1;
                if(XIIth.isChecked())
                {
                    temp1 = SelectStream.getSelectedItem().toString();
                }
                else
                {
                    temp1="";
                }
                final String percentage =temp+"%";
                final String stream = temp1;
                final String schoolname = SchoolName.getText().toString();
                Session = String.valueOf(StartingYear.getSelectedItem().toString()+"-"+EndingYear.getSelectedItem().toString());

                if(name.isEmpty())
                {
                    Name.setError("Please Enter the Name");
                    Name.requestFocus();
                    return;
                }
                if(rollno.isEmpty())
                {
                    RollNo.setError("Please Enter the Board Roll number");
                    RollNo.requestFocus();
                    return;
                }
                if(temp.isEmpty())
                {
                    Percentage.setError("Please Enter the Percentage of Topper");
                    Percentage.requestFocus();
                    return;
                }
                if(schoolname.isEmpty())
                {
                    SchoolName.setError("Please Enter the Name of School");
                    SchoolName.requestFocus();
                    return;
                }
                if(XIIth.isChecked())
                {
                    dbr = FirebaseDatabase.getInstance().getReference("Class: XIIth");
                }
                else
                {
                    dbr = FirebaseDatabase.getInstance().getReference("Class: Xth");
                }
                if(ImageFilePath != null&&ResultFilePath!=null) {
                    pd.show();

                    final StorageReference childRef = ImageStorageRef.child(name+rollno+ "." + getFileExtension(ImageFilePath));
                    final StorageReference resultChildRef = ResultStorageRef.child(name+rollno+ "." + getFileExtension(ResultFilePath));
                    UploadTask uploadTask = childRef.putFile(ImageFilePath);
                    final String uploadId = dbr.push().getKey();
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
                                ToppersData upload = new ToppersData(downloadUri.toString(),
                                        name, percentage, "Hello", rollno, schoolname, Session, stream);
                                dbr.child(uploadId).setValue(upload);
                                Toast.makeText(getContext(), "Image Upload Successful", Toast.LENGTH_LONG).show();
                                UploadTask uploadTask1 = resultChildRef.putFile(ResultFilePath);
                                Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return resultChildRef.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        dbr.child(uploadId).child("resulturl").setValue(task.getResult().toString());
                                        Toast.makeText(getContext(), "Result Upload Successful", Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
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
            Picasso.get().load(ImageFilePath).fit().centerCrop().into(TopperImage);
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data.getData() != null)
        {
            ResultFilePath = data.getData();
            Picasso.get().load(ResultFilePath).fit().centerCrop().into(ResultImage);
        }
    }

    private String getFileExtension(Uri url)
    {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(url));
    }
}