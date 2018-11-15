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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


public class SignUpFragment extends android.support.v4.app.Fragment {
    private ProgressBar progressBar;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private EditText ContactNo;
    private Button Register;
    private FirebaseAuth mAuth;
    private EditText TeacherCode;
    private EditText VerificationCode;
    private View view;
    private RadioButton Teacher;
    private RadioButton User;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVarificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

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
        TeacherCode.setVisibility(View.GONE);
        VerificationCode = (EditText)view.findViewById(R.id.VerificationCode);
        VerificationCode.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContactNo.getText().toString().isEmpty())
                {
                    ContactNo.setError("Contact number cannot be empty");
                    ContactNo.requestFocus();
                    return;
                }
                if(VerificationCode.getText().toString().isEmpty())
                {
                  VerificationCode.setError("Enter the Verification Code first");
                  VerificationCode.requestFocus();
                  return;
                }
                else
                {
                    verifyVerificationCode(VerificationCode.getText().toString());
                }
            }
        });
        Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContactNo.getText().toString().isEmpty())
                {
                    ContactNo.setError("Enter the contact no first");
                    ContactNo.requestFocus();
                    return;
                }
                else if(isConnected())
                {
                    VerifyPhone(ContactNo.getText().toString());
                    VerificationCode.setVisibility(View.VISIBLE);
                }
                TeacherCode.setVisibility(View.VISIBLE);
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeacherCode.setVisibility(View.GONE);
                if(ContactNo.getText().toString().isEmpty())
                {
                    ContactNo.setError("Enter the contact no first");
                    ContactNo.requestFocus();
                    return;
                }
                else if(isConnected())
                {
                    VerifyPhone(ContactNo.getText().toString());
                    VerificationCode.setVisibility(View.VISIBLE);
                }
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

    private void RegisterUser(final PhoneAuthCredential credential)
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
                                mAuth.signInWithEmailAndPassword(email,password);
                                mAuth.getCurrentUser().linkWithCredential(credential);
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
                                        //
                                        //
                                        //
                                        // usersRef.child("Teachers").child(contactno).child("notifications").push().setValue(md);
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
                                            mAuth.signInWithEmailAndPassword(email,password);
                                            mAuth.getCurrentUser().linkWithCredential(credential);
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

    public void VerifyPhone(String contactno)
    {
        mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVarificationId= verificationId;
                mResendToken = token;

                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+contactno,60, TimeUnit.SECONDS,getActivity(),mCallbacks);

    }

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVarificationId, otp);

        //signing the user
        RegisterUser(credential);
    }

}
