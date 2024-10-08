package com.example.newpc.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ku$haL on 16-05-2017.
 */
public class ANMActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvNew, tvForgot;
    EditText etUN, etPW;
    ProgressBar pb;
    SessionManager manager;
    ScrollView scrollLogin;

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        tvNew.setClickable(true);
        tvForgot.setClickable(true);
        etUN.setText("");
        etPW.setText("");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anmlogin);

        manager = new SessionManager(getApplicationContext());
        if (manager.isLoggedIn()) {
            startActivity(new Intent(ANMActivity.this, HomeActivity.class));
            finish();
        }
        int versionCode = BuildConfig.VERSION_CODE;
        final String versionName = BuildConfig.VERSION_NAME;

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference version = db.getReference("Version");
        version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ver = dataSnapshot.getValue().toString();

                if (!versionName.equals(ver)) {
                    //download updated app
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvNew = (TextView) findViewById(R.id.tvNew);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        etUN = (EditText) findViewById(R.id.etUN);
        etPW = (EditText) findViewById(R.id.etPW);
        pb = (ProgressBar) findViewById(R.id.pb);
        scrollLogin = (ScrollView) findViewById(R.id.scrollLogin);
        pb.setVisibility(View.GONE);
        scrollLogin.setVisibility(View.VISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable() == true) {
                    if (etUN.getText().toString().equals("")) {
                        Toast.makeText(ANMActivity.this, "Username empty!", Toast.LENGTH_LONG).show();
                    } else if (etPW.getText().toString().equals("")) {
                        Toast.makeText(ANMActivity.this, "Password empty!", Toast.LENGTH_LONG).show();
                    } else {
                        tvNew.setClickable(false);
                        tvForgot.setClickable(false);
                        startSignIn();
                    }
                } else
                    Toast.makeText(ANMActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
        tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(ANMActivity.this, NewANMActivity.class);
                startActivity(rIntent);
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(ANMActivity.this, PasswordActivity.class);
                startActivity(rIntent);
            }
        });
    }

    private void startSignIn() {
        scrollLogin.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        final String anm = etUN.getText().toString();
        final String pw = etPW.getText().toString();
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("PHC/");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot childEventSnapshot : eventSnapshot.getChildren()) {
                            if (eventSnapshot.child("ANM").hasChild(anm)) {
                                for (DataSnapshot passChild : childEventSnapshot.child(anm).getChildren()) {
                                    if (passChild.getKey().toString().equals("Password")) {
                                        if (passChild.getValue().equals(pw)) {
                                            SharedPreferences settings = getSharedPreferences("LoginPREF", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("anm", anm);
                                            editor.putBoolean("Started", false);
                                            editor.commit();
                                            Intent rIntent = new Intent(ANMActivity.this, HomeActivity.class);
                                            // Toast.makeText(ANMActivity.this,anm,Toast.LENGTH_LONG).show();
                                            manager.setLogin(true);
                                            startActivity(rIntent);
                                            finish();
                                        } else {
                                            etPW.setError("Incorrect");
                                            etPW.setText("");
                                            etPW.requestFocus();
                                            Toast.makeText(ANMActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                                            pb.setVisibility(View.GONE);
                                            scrollLogin.setVisibility(View.VISIBLE);
                                        }
                                    } /*else {
                                        Toast.makeText(getApplicationContext(), "Some Error Occurred!!", Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                            } else {
                                etUN.setError("Incorrect");
                                etUN.setText("");
                                etUN.requestFocus();
                                pb.setVisibility(View.GONE);
                                scrollLogin.setVisibility(View.VISIBLE);
                                Toast.makeText(ANMActivity.this, "Wrong usename!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Some Error Occurred!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
