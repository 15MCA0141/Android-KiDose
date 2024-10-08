package com.example.newpc.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public List<String> areaNames = new ArrayList<String>();
    ImageView btnMother, btnDueList, btnChild;
    Spinner spinner, spinnerPHC;
    Intent rIntent;
    ValueEventListener listener, listener2, listener3;
    DatabaseReference version, myPHC, myRef, myArea;
    Boolean lock1 = false;
    LinearLayout llMother, llVaccination, llDueList;
    android.support.v7.widget.CardView cardMother, cardVacc, cardDue;
    SessionManager manager;
    SharedPreferences sharedPreferences;

    /*@Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SharedPreferences sharedPreferences = getSharedPreferences("LoginPREF", MODE_PRIVATE);
        //final String anmG = sharedPreferences.getString("anm", "");
        manager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences("LoginPREF", MODE_PRIVATE);
        final String anmG = sharedPreferences.getString("anm", "");
        final String phc = sharedPreferences.getString("phc", "");
        final Boolean flagged = sharedPreferences.getBoolean("Started", true);
        setContentView(R.layout.activity_home);
        cardMother = (android.support.v7.widget.CardView) findViewById(R.id.card_view);
        cardVacc = (android.support.v7.widget.CardView) findViewById(R.id.card_view2);
        cardDue = (android.support.v7.widget.CardView) findViewById(R.id.card_view3);
        int versionCode = BuildConfig.VERSION_CODE;
        final String versionName = BuildConfig.VERSION_NAME;
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        version = db.getReference("Version");
        version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (lock1) {
                    return;
                }
                String ver = dataSnapshot.getValue().toString();

                if (!versionName.equals(ver)) {
                    Toast.makeText(HomeActivity.this, "New version of this app is available!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kidose-f4433.firebaseapp.com/Kidose.apk"));
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        llMother = (LinearLayout) findViewById(R.id.llMother);
        llVaccination = (LinearLayout) findViewById(R.id.llVaccination);
        llDueList = (LinearLayout) findViewById(R.id.llDueList);
        btnMother = (ImageView) findViewById(R.id.btnMother);
        btnDueList = (ImageView) findViewById(R.id.btnDueList);
        btnChild = (ImageView) findViewById(R.id.btnChild);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerPHC = (Spinner) findViewById(R.id.spinnerPHC);
        if (isNetworkAvailable() == true) {
            myPHC = db.getReference("PHC");
            myPHC.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (lock1) {
                        return;
                    }
                    List<String> phcNames = new ArrayList<String>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        phcNames.add(ds.child("Name").getValue().toString());
                    }
                    ArrayAdapter<String> phcAdapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.spinner_item, phcNames);
                    phcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPHC.setAdapter(phcAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            spinnerPHC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    final String phc = spinnerPHC.getSelectedItem().toString();
                    myRef = db.getReference("PHC/" + phc + "/ANM");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (lock1) {
                                return;
                            }
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot es : ds.getChildren()) {
                                    if (es.getKey().equals("Areas")) {
                                        for (DataSnapshot cs : es.getChildren()) {
                                            areaNames.add(cs.getKey().toString());
                                        }
                                    }
                                }
                            }
                            ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.spinner_item, areaNames);
                            nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(nameAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    myArea = db.getReference("PHC/" + phc + "/ANM");
                    myArea.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                try {
                                    Toast.makeText(HomeActivity.this, "2: ", Toast.LENGTH_SHORT).show();
                                } catch (NullPointerException e) {
                                    Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            cardMother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinner.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "No Area Selected!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String phc = spinnerPHC.getSelectedItem().toString();
                    String area = spinner.getSelectedItem().toString();
                    String anm = anmG;
                    Intent rIntent = new Intent(HomeActivity.this, MotherActivity.class);
                    rIntent.putExtra("phc", phc);
                    rIntent.putExtra("anm", anm);
                    rIntent.putExtra("area", area);
                    startActivity(rIntent);

                    //Toast.makeText(HomeActivity.this, "Mother Profiles", Toast.LENGTH_SHORT).show();
                }
            });
/*
            btnANM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phc = spinnerPHC.getSelectedItem().toString();
                    String area = spinner.getSelectedItem().toString();
                    final String anm = anmG;
                    String pin = "";
                    Intent rIntent = new Intent(HomeActivity.this, DataActivity.class);
                    rIntent.putExtra("phc", phc);
                    rIntent.putExtra("anm", anm);
                    rIntent.putExtra("area", area);
                    rIntent.putExtra("pin", pin);
                    startActivity(rIntent);
                    rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            });*/

            cardVacc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinner.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "No Area Selected!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String phc = spinnerPHC.getSelectedItem().toString();
                    final String area = spinner.getSelectedItem().toString();
                    final DatabaseReference myRef = db.getReference("PHC/" + phc + "/ANM/");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot es : ds.getChildren()) {
                                    for (DataSnapshot cs : es.getChildren()) {
                                        if (cs.getKey().equals(spinner.getSelectedItem().toString())) {
                                            String anm = ds.getKey();
                                            rIntent = new Intent(HomeActivity.this, MainActivity.class);
                                            rIntent.putExtra("phc", phc);
                                            rIntent.putExtra("anm", anm);
                                            rIntent.putExtra("area", area);
                                            startActivity(rIntent);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });

            cardDue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinner.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "No Area Selected!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final String phc = spinnerPHC.getSelectedItem().toString();
                    final String area = spinner.getSelectedItem().toString();
                    final String anm = anmG;
                    DatabaseReference myRef = db.getReference("PHC/" + phc + "/ANM/");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot es : ds.getChildren()) {
                                    for (DataSnapshot cs : es.getChildren()) {
                                        if (cs.getKey().equals(spinner.getSelectedItem().toString())) {
                                            Intent rIntent = new Intent(HomeActivity.this, DueListActivity.class);
                                            rIntent.putExtra("phc", phc);
                                            rIntent.putExtra("anm", anm);
                                            rIntent.putExtra("area", area);
                                            startActivity(rIntent);
                                            rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout: {
                sharedPreferences.edit().clear().commit();
                manager.setLogin(false);
                Intent rIntent = new Intent(HomeActivity.this, ANMActivity.class);
                startActivity(rIntent);
                Toast.makeText(HomeActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                ActivityCompat.finishAffinity(this);
                return true;
            }
            case R.id.anmProfile: {
                String phc = spinnerPHC.getSelectedItem().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("LoginPREF", MODE_PRIVATE);
                final String anmG = sharedPreferences.getString("anm", "");
                //final String anm = anmG;
                //String anm = sharedPreferences.getString("anm", "");
                Intent rIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                rIntent.putExtra("phc", phc);
                rIntent.putExtra("anm", anmG);
                Toast.makeText(HomeActivity.this, phc + " : " + anmG, Toast.LENGTH_SHORT).show();
                startActivity(rIntent);
//                rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                return true;
            }
            case R.id.about: {
                Toast.makeText(HomeActivity.this, "We are team Kidose!", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.contact: {
                Toast.makeText(HomeActivity.this, "For Tech-Support Call us at: 9972210926", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.english: {
                setLocale("en");
                Toast.makeText(HomeActivity.this, "English", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.hindi: {
                setLocale("hi");
                Toast.makeText(HomeActivity.this, "Hindi", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.marathi: {
                setLocale("mr");
                Toast.makeText(HomeActivity.this, "Marathi", Toast.LENGTH_SHORT).show();
                return true;
            }

            default:
                return true;
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, HomeActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    protected void onPause() {
        try {
            version.removeEventListener(listener);
            myPHC.removeEventListener(listener2);
            myRef.removeEventListener(listener3);
        } catch (Exception e) {
            Log.e("Er", e.getMessage());
        }
        super.onPause();
//        lock1 = true;
//        SharedPreferences settings = getSharedPreferences("LoginPREF", MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("Started", true);
//        editor.apply();

    }

    @Override
    protected void onDestroy() {
        try {
            version.removeEventListener(listener);
            myPHC.removeEventListener(listener2);
            myRef.removeEventListener(listener3);
        } catch (Exception e) {
            Log.e("Er", e.getMessage());
        }
        super.onDestroy();
//        lock1 = true;
//        SharedPreferences settings = getSharedPreferences("LoginPREF", MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("Started", true);
//        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

