package com.example.hello.coachingapp;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudyMaterialFragment extends android.support.v4.app.Fragment {
    private View view,view1;
    private CardView Chemistry,Physics,Mathematics,Biology,ComputerScience,Accounts,Economics,Business,History,PoliticalScience,English,Geography;
    private ImageView ChemistryImg,PhysicsImg,MathematicsImg,BiologyImg,ComputerScienceImg,AccountsImg,EconomicsImg,BusinessImg,HistoryImg,PoliticalScienceImg,EnglishImg,GeographyImg;
    private ArrayList<String>data;

    RecyclerView rv;
    ClassListAdapter adapter;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean Islogin = prefs.getBoolean("Islogin",false);
        if(!Islogin)
        {
            Toast.makeText(getContext(),"Please make sure that you have Successfully Logged in to your Acccount", Toast.LENGTH_LONG);
            view = inflater.inflate(R.layout.sorry_layout,container,false);
            TextView text = (TextView)view.findViewById(R.id.textView);
            text.setText("Please make sure that you have Successfully Logged in to your Acccount");
            return view;
        }
        final View view = inflater.inflate(R.layout.study_material_layout,container,false);
        Chemistry = view.findViewById(R.id.Chemistry);
        ChemistryImg = Chemistry.findViewById(R.id.ChemistryImg);
        Picasso.get().load(R.drawable.chemistry).fit().centerCrop().into(ChemistryImg);
        Chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Chemistry"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Physics = view.findViewById(R.id.Physics);
        PhysicsImg = Physics.findViewById(R.id.PhysicsImg);
        Picasso.get().load(R.drawable.physics).fit().centerCrop().into(PhysicsImg);
        Physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Physics"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Mathematics = view.findViewById(R.id.Mathematics);
        MathematicsImg = Mathematics.findViewById(R.id.MathematicsImg);
        Picasso.get().load(R.drawable.maths).fit().centerCrop().into(MathematicsImg);
        Mathematics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Mathematics"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Biology = view.findViewById(R.id.Biology);
        BiologyImg = Biology.findViewById(R.id.BiologyImg);
        Picasso.get().load(R.drawable.biology).fit().centerCrop().into(BiologyImg);
        Biology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Biology"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Accounts = view.findViewById(R.id.Accounts);
        AccountsImg = Accounts.findViewById(R.id.AccountsImg);
        Picasso.get().load(R.drawable.accounting).fit().centerCrop().into(AccountsImg);
        Accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Accounts"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Economics = view.findViewById(R.id.Economics);
        EconomicsImg = Economics.findViewById(R.id.EconomicsImg);
        Picasso.get().load(R.drawable.economics).fit().centerCrop().into(EconomicsImg);
        Economics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Economics"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Business = view.findViewById(R.id.Business);
        BusinessImg = Business.findViewById(R.id.BusinessImg);
        Picasso.get().load(R.drawable.business).fit().centerCrop().into(BusinessImg);
        Business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Business"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        History = view.findViewById(R.id.History);
        HistoryImg = History.findViewById(R.id.HistoryImg);
        Picasso.get().load(R.drawable.history).fit().centerCrop().into(HistoryImg);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("History"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        Geography = view.findViewById(R.id.Geography);
        GeographyImg = Geography.findViewById(R.id.GeographyImg);
        Picasso.get().load(R.drawable.geography).fit().centerCrop().into(GeographyImg);
        Geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("Geography"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        PoliticalScience = view.findViewById(R.id.PoliticalScience);
        PoliticalScienceImg = PoliticalScience.findViewById(R.id.PoliticalScienceImg);
        Picasso.get().load(R.drawable.politicalscience).fit().centerCrop().into(PoliticalScienceImg);
        PoliticalScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("PoliticalScience"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        ComputerScience = view.findViewById(R.id.ComputerScience);
        ComputerScienceImg = ComputerScience.findViewById(R.id.ComputerScienceImg);
        Picasso.get().load(R.drawable.computerscience).fit().centerCrop().into(ComputerScienceImg);
        ComputerScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("ComputerScience"))
                        .addToBackStack("MyBackStack").commit();
            }
        });
        English = view.findViewById(R.id.English);
        EnglishImg = English.findViewById(R.id.EnglishImg);
        Picasso.get().load(R.drawable.english).fit().centerCrop().into(EnglishImg);
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.screen_area, new ClassListFragment().setDbr("English"))
                        .addToBackStack("MyBackStack").commit();
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

}
