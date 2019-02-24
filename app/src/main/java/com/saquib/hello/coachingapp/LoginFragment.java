package com.saquib.hello.coachingapp;

import android.app.Service;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends android.support.v4.app.Fragment {
    private ProgressBar progressBar;
    private EditText Email;
    private EditText Password;
    private Button Login;
    private FirebaseAuth mAuth;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        Email = view.findViewById(R.id.Email);
        Password = view.findViewById(R.id.Password);
        Login = (Button) view.findViewById(R.id.LoginButton);
        mAuth = FirebaseAuth.getInstance();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
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


    private void LoginUser()
    {
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString();

        if(!isConnected())
        {
            if(!isConnected())
            {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area,new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network",false)).commit();
                return;
            }
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

        progressBar.setVisibility(view.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(view.GONE);
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

                if(task.isSuccessful()) {
                    Toast.makeText(getContext(),"You have Signed in Successfully",Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getContext());
                    prefs.edit().putBoolean("Islogin", true).apply();
                    getActivity().setTitle(R.string.Home);
                    getUserData();
                    ((MainActivity)getActivity()).Islogin=true;
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.screen_area,new HomeFragment()).commit();
                }

                else {
                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getUserData()
    {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final DatabaseReference dbr;
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        final String CurrentEmail = usr.getEmail();
        dbr= FirebaseDatabase.getInstance().getReference().child("Users");
        //Toast.makeText(getContext(),"The Email of current User is "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_LONG).show();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Users u = ds.getValue(Users.class);
                    if(u.getEmail().equals(CurrentEmail))
                    {
                        prefs.edit().putString("Email",u.getEmail()).apply();
                        prefs.edit().putString("Name",u.getName()).apply();
                        prefs.edit().putString("Password",u.getPassword()).apply();
                        prefs.edit().putString("ContactNo",ds.getKey()).apply();
                        prefs.edit().putString("ImageUrl",u.getImgurl()).apply();
                        if(u.getType()=="teacher")
                        {
                            prefs.edit().putBoolean("Teacher",false).apply();
                        }
                        else
                        {
                            prefs.edit().putBoolean("Teacher",true).apply();
                        }
                        //Toast.makeText(getContext(),"The Prefrences are updated to "+prefs.getString("Name",""),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getContext(),"The Current user is "+prefs.getString("Email",""),Toast.LENGTH_LONG).show();
    }
}