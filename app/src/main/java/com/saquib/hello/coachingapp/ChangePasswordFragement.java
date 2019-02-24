package com.saquib.hello.coachingapp;


import android.app.Service;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordFragement extends Fragment {

    private ProgressBar progressBar;
    private EditText OldPassword;
    private EditText NewPassword;
    private EditText ConfirmPassword;
    private Button Update;
    private FirebaseAuth mAuth;
    private View view;

    public ChangePasswordFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_password_layout, container, false);
        progressBar = view.findViewById(R.id.progressBar5);
        OldPassword = view.findViewById(R.id.OldPassword);
        NewPassword = view.findViewById(R.id.NewPassword);
        ConfirmPassword = view.findViewById(R.id.ConfirmPassword);
        Update = (Button) view.findViewById(R.id.Update);
        mAuth = FirebaseAuth.getInstance();
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });
        return view;
    }

    void ChangePassword()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        final String op = OldPassword.getText().toString();
        final String np = NewPassword.getText().toString();
        final String cp = ConfirmPassword.getText().toString();

        if(!isConnected())
        {
            if(!isConnected())
            {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area,new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network",false)).commit();
                return;
            }
        }

        if(op.isEmpty())
        {
            OldPassword.setError("Please Enter the Old Password");
            OldPassword.requestFocus();
            return;
        }

        if(np.isEmpty())
        {
            NewPassword.setError("Please enter the new Password");
            NewPassword.requestFocus();
            return;
        }

        if(cp.isEmpty())
        {
            ConfirmPassword.setError("Please Enter new password again");
            ConfirmPassword.requestFocus();
            return;
        }

        if(!np.equals(cp))
        {
            ConfirmPassword.setError("Entered password does not match");
            ConfirmPassword.requestFocus();
            return;

        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            AuthCredential credential = EmailAuthProvider.getCredential(email,op);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        user.updatePassword(np).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");
                                    dbr.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds: dataSnapshot.getChildren())
                                            {
                                                Users usr = ds.getValue(Users.class);
                                                usr.setPassword(np);
                                                dbr.child(ds.getKey()).setValue(usr);
                                                if(usr.getType().equals("teacher"))
                                                {
                                                    final DatabaseReference d = FirebaseDatabase.getInstance().getReference("Teachers");
                                                    d.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot ds:dataSnapshot.getChildren())
                                                            {
                                                                Users u = ds.getValue(Users.class);
                                                                u.setPassword(np);
                                                                d.child(ds.getKey()).setValue(u);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(),"Password is Changed Successfully",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(),"Sorry, Something went Wrong",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Sorry, You have Entered Wrong Password" ,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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

}
