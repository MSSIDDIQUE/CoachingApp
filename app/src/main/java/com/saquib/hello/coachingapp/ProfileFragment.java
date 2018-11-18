package com.saquib.hello.coachingapp;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends android.support.v4.app.Fragment{

    public View view;
    public TextView Name;
    public EditText EditName;
    public TextView Email;
    public EditText EditEmail;
    public TextView Password;
    public EditText EditPassword;
    public TextView ContactNo,GraduationTxt;
    public EditText EditContactNo;
    public Uri ImageFilePath;
    public ExpandableCardView ProfileDetails;
    public ExpandableCardView Classes;
    public RecyclerView rv;
    public ExpandableCardView Profile;
    public Button Actions;
    public android.support.v4.app.Fragment TimeItemFragment;
    public ScrollView sc;
    public CircleImageView profile;
    public FloatingActionButton change;
    public String Type,CurrnetEmail;
    public ProgressBar progressBar,progressBar1;
    public FrameLayout PlaceHolder;

    public ProfileFragment() {
        // Required empty public constructor
    }

    StorageReference ImageStorageRef;
    DatabaseReference dbr;
    SharedPreferences UserType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_layout,container,false);
        Profile = view.findViewById(R.id.profile);
        EditName = Profile.findViewById(R.id.EditName);
        Name = Profile.findViewById(R.id.Name);
        Name.setVisibility(View.VISIBLE);
        EditEmail = Profile.findViewById(R.id.EditEmail);
        Email = Profile.findViewById(R.id.Email);
        Email.setVisibility(View.VISIBLE);
        EditContactNo = Profile.findViewById(R.id.EditContactNo);
        ContactNo = Profile.findViewById(R.id.ContactNo);
        ContactNo.setVisibility(View.VISIBLE);
        EditPassword = Profile.findViewById(R.id.EditPassword);
        Password = Profile.findViewById(R.id.Password);
        Password.setVisibility(View.VISIBLE);
        PlaceHolder = view.findViewById(R.id.PlaceHolder);
        profile = view.findViewById(R.id.Profile);
        change = view.findViewById(R.id.ChangeProfile);
        progressBar = view.findViewById(R.id.progressBar3);
        progressBar1 = view.findViewById(R.id.progressBar4);
        UserType = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(UserType.getBoolean("Teacher",false))
        {
            Type = "Teacher";
            ImageStorageRef = FirebaseStorage.getInstance().getReference("TeacherImages");
            dbr = FirebaseDatabase.getInstance().getReference("Teachers");
            Log.e("Hello","The Value of Teacher is"+Type);
        }
        else
        {
            Type = "User";
            ImageStorageRef = FirebaseStorage.getInstance().getReference("UserImages");
            dbr = FirebaseDatabase.getInstance().getReference("Users");
        }
        progressBar.setVisibility(View.VISIBLE);
        getUserData();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Result Snapshot"), 1);
            }
        });
        sc = view.findViewById(R.id.scrolView);
        Actions = view.findViewById(R.id.Actions);
        if(!UserType.getBoolean("Teacher",false))
        {
            Actions.setVisibility(View.GONE);
        }
        Actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.screen_area,new ActionsFragment().setContactNo(ContactNo.getText().toString())).addToBackStack("MyBackStack").commit();

            }
        });

        return view;
    }

    public void getUserData()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CurrnetEmail = user.getEmail();
        progressBar.setVisibility(View.VISIBLE);
        dbr.orderByChild("email").equalTo(CurrnetEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Users usr = ds.getValue(Users.class);
                    Name.setText(usr.getName());
                    Email.setText(usr.getEmail());
                    Password.setText(usr.getPassword());
                    ContactNo.setText(ds.getKey());
                    if(!usr.getImgurl().equals(""))
                    {
                        Picasso.get().load(usr.getImgurl()).fit().centerCrop().into(profile);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Sorry no record found for the current user",Toast.LENGTH_LONG);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar1.setVisibility(View.VISIBLE);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            ImageFilePath = data.getData();
            final StorageReference childRef = ImageStorageRef.child(Name.getText().toString()+ContactNo.getText().toString()+ "." + getFileExtension(ImageFilePath));
            UploadTask uploadTask = childRef.putFile(ImageFilePath);
            final String uploadId = ContactNo.getText().toString();
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
                        dbr.child(uploadId).child("imgurl").setValue(downloadUri.toString());
                        dbr = FirebaseDatabase.getInstance().getReference().child("Users");
                        dbr.child(uploadId).child("imgurl").setValue(downloadUri.toString());
                        Toast.makeText(getContext(), "Image Upload Successful", Toast.LENGTH_LONG).show();
                        progressBar1.setVisibility(View.GONE);
                    }
                }
            });
            Picasso.get().load(ImageFilePath).fit().centerCrop().into(profile);
        }
    }

    private String getFileExtension(Uri url)
    {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(url));
    }

}
