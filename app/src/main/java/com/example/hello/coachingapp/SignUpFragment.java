package com.example.hello.coachingapp;

import android.app.FragmentManager;
import android.app.Service;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpFragment extends android.support.v4.app.Fragment {
    private ProgressBar progressBar;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private EditText ContactNo;
    private Button Register;
    private FirebaseAuth mAuth;
    private View view;

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
        mAuth = FirebaseAuth.getInstance();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
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

        if(!isConnected())
        {
            if(!isConnected())
            {
               android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
               fm.beginTransaction().replace(R.id.screen_area,new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network")).commit();
               return;
            }
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
        progressBar.setVisibility(view.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                usersRef.child(contactno).setValue( new Users(email,name, password));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefs.edit().putBoolean("Islogin", true).commit();
                progressBar.setVisibility(view.GONE);

                if(task.isSuccessful()) {
                    Toast.makeText(getContext(),"User Registered Successfully", Toast.LENGTH_SHORT).show();
                }

                else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getContext(),"contact number is already registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


}
