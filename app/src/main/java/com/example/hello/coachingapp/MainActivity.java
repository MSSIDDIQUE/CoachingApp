package com.example.hello.coachingapp;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager vp;
    View HeaderView;
    VeiwPagerAdapter adapter;
    Fragment f = null;
    FragmentManager fm;
    Toolbar toolbar;
    TabLayout tabLayout;
    FirebaseUser firebaseUser;
    boolean doubleBackToExitPressedOnce = false;
    ImageView button ;
    boolean Islogin;

    private int STORAGE_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        button = findViewById(R.id.ChangeFrom);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            requestStoragePermission();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Islogin = prefs.getBoolean("Islogin", false);
        if(!isConnected())
        {
            Toast.makeText(this,"Please Connect your phone to Network to Load data from server",Toast.LENGTH_LONG).show();
        }
        if (Islogin) {
            setTitle(R.string.Home);
            f = new HomeFragment();
        } else {
            setTitle("Register/Login");
            f = new RegisterFragment();

        }
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.screen_area, f).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed in order to Download the Study Material to your Device Storage")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    public void replaceFragment(android.support.v4.app.Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.PlaceHolder, f).setPrimaryNavigationFragment(f).addToBackStack("ChildBackStack").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int count = getFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "You hava Successfully Signed out", Toast.LENGTH_SHORT).show();
            setTitle("Register/Login");
            f = new RegisterFragment();
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.screen_area, f).addToBackStack("MyBackStack").commit();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putBoolean("Islogin", false).apply();
            prefs.edit().putBoolean("Teacher", false).commit();
        }

        if ((id == R.id.Profile)&&Islogin) {
            setTitle("Profile Section");
            f = new ProfileFragment();
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.screen_area, f).addToBackStack("MyBackStack").commit();
        }

        else {
            setTitle("Profile Section");
            f = new SorryFragment().setText("Please Sign In / Sign Up first");
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.screen_area, f).addToBackStack("MyBackStack").commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            setTitle(R.string.Home);
            f = new HomeFragment();

        } else if (id == R.id.nav_toppers) {
            setTitle(R.string.Toppers);
            f = new ToppersTab();

        } else if (id == R.id.nav_gallery) {
            setTitle("Time Table");
            f = new TimeTableTab();

        } else if (id == R.id.nav_studymaterial) {
            setTitle(R.string.StudyMaterial);
            f = new StudyMaterialFragment();

        } else if (id == R.id.nav_register) {
            setTitle("Register/Login");
            f = new RegisterFragment();

        } else if (id == R.id.nav_feepayment) {
            setTitle(R.string.FeePayment);
            f = new SorryFragment().setText("This Fragment of App is Still Under Development");

        } else if (id == R.id.nav_contactus) {
            f = new SorryFragment().setText("This Fragment of App is Still Under Development");

        } else if (id == R.id.nav_find_us) {
            f = new SorryFragment().setText("This Fragment of App is Still Under Development");

        }

        if(!isConnected())
        {
            f = new SorryFragment().setText("Please Make Sure that your Phone is Connected to Network");
        }

        if (f != null) {
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.screen_area, f).addToBackStack("MyBackStack").commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface MyAdapterListener {

        void FromClickListner(View v, int position);
        void FromClickListner(View v);
        void ToOnClickListner(View v, int position);
        void ToOnClickListner(View v);
    }

    public void onButtonClicked(View v) {
        String txtid = null;
        if (v.getId() == R.id.ChangeFrom) {
            txtid = "From";
            DialogFragment newFragment = new TimePickerFragment().SetTextId(txtid,v);
            newFragment.show(getFragmentManager(), "TimePicker");
        } else {
            txtid = "To";
            DialogFragment newFragment = new TimePickerFragment().SetTextId(txtid,v);
            newFragment.show(getFragmentManager(), "TimePicker");
        }
    }

    private void sendRegistrationToServer(String token) {
        Log.d("Hello", "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("messaging_token")
                .setValue(token);
    }


    private void initFCM() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d("Hello", "initFCM: token: " + token);
                sendRegistrationToServer(token);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Service.CONNECTIVITY_SERVICE);
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