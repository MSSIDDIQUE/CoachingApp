package com.example.hello.coachingapp;

import android.app.FragmentManager;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;


public class SignUpFragment extends android.support.v4.app.Fragment {
    private ProgressBar progressBar;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private EditText ContactNo;
    private EditText TeacherCode;
    private Button Register;
    private FirebaseAuth mAuth;
    private View view;
    private RadioButton Teacher;
    private RadioButton User;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_layout, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        Name = view.findViewById(R.id.RegisterName);
        Email = view.findViewById(R.id.Email);
        Password = view.findViewById(R.id.Password);
        ContactNo = view.findViewById(R.id.Phone);
        Register = view.findViewById(R.id.RegisterButton);
        Teacher = view.findViewById(R.id.Teacher);
        User = view.findViewById(R.id.User);
        TeacherCode = (EditText) view.findViewById(R.id.TeachersCode);
        mAuth = FirebaseAuth.getInstance();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
        Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherCode.setVisibility(View.VISIBLE);
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherCode.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    public boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info!=null)
            {
                if(info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void RegisterUser()
    {
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString();
        final String name = Name.getText().toString();
        final String contactno = ContactNo.getText().toString();
        final String teacherscode = TeacherCode.getText().toString();

        if(!isConnected())
        {
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.screen_area,new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network")).commit();
            return;
        }

        if(name.isEmpty())
        {
            Name.setError("Please Enter your Name");
            Name.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            Email.setError("Email is Mandatory");
            Email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Please enter a valid Email Address");
            Email.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            Password.setError("Set password");
            Password.requestFocus();
            return;
        }
        

        if(password.length()<6)
        {
            Password.setError("Password Must be at least six Charecter Long");
            Password.requestFocus();
            return;
        }

        if(contactno.isEmpty())
        {
            ContactNo.setError("Contact number cannot be empty");
            ContactNo.requestFocus();
            return;
        }

        if(contactno.length()<10)
        {
            ContactNo.setError("Please enter a valid Contact Number");
            ContactNo.requestFocus();
            return;
        }

        if((Teacher.isChecked()==false)&&(User.isChecked()==false))
        {
            Toast.makeText(getContext(),"Please Select the user Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Teacher.isChecked()&&teacherscode.isEmpty())
        {
            TeacherCode.setError("Please Enter the Teacher Code");
            return;
        }
        progressBar.setVisibility(view.VISIBLE);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                final String tokenId = instanceIdResult.getToken().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
                    String WelcomeNote = "Hello "+name+" Study Solutions Welcomes You";
                    MessageData md = new MessageData("Welcome", WelcomeNote,"Admin");
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(User.isChecked())
                        {
                            usersRef.child("Users").child(contactno).setValue( new Users(email,name, password,"user","",tokenId));
                            usersRef.child("Users").child(contactno).child("notifications").push().setValue(md);
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences UserType = PreferenceManager.getDefaultSharedPreferences(getContext());
                            prefs.edit().putBoolean("Islogin", true).apply();
                            UserType.edit().putBoolean("Teacher",false).apply();
                            progressBar.setVisibility(view.GONE);
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getContext(),"User Registered Successfully", Toast.LENGTH_SHORT).show();
                                android.support.v4.app.FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.screen_area,new HomeFragment()).commit();
                            }
                            else
                            {
                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                {
                                    Toast.makeText(getContext(),"Contact number or Email Address is already registered", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        if(Teacher.isChecked())
                        {
                            usersRef.child("Teachers").child("TeachersCode").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue().toString().matches(teacherscode))
                                    {
                                        usersRef.child("Teachers").child(contactno).setValue( new Users(email,name+" Sir", password,"teacher", "",tokenId));
                                        usersRef.child("Teachers").child(contactno).child("notifications").push().setValue(md);
                                        usersRef.child("Users").child(contactno).setValue(new Users(email,name+" Sir", password,"teacher", "",tokenId));
                                        usersRef.child("Users").child(contactno).child("notifications").push().setValue(md);
                                        SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getContext());
                                        SharedPreferences UserType = PreferenceManager.getDefaultSharedPreferences(getContext());
                                        prefs.edit().putBoolean("Islogin", true).apply();
                                        UserType.edit().putBoolean("Teacher", true).apply();
                                        progressBar.setVisibility(view.GONE);
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getContext(),"Teacher Registered Successfully", Toast.LENGTH_SHORT).show();
                                            getActivity().setTitle(R.string.Home);
                                            android.support.v4.app.FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                                            fm.beginTransaction().replace(R.id.screen_area,new HomeFragment()).commit();
                                        }
                                        else
                                        {
                                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                            {
                                                Toast.makeText(getContext(),"Contact number is already registered", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(getContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        TeacherCode.setError("Please Enter a valid Teachers Code");
                                        progressBar.setVisibility(view.GONE);
                                        return;
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        });
    }


}
