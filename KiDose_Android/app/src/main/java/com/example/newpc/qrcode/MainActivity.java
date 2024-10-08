package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    ImageView scan;
    TextView txtMother, tvArea;
    EditText editText;
    SearchableSpinner spinnerMom;
    private long count = 1;

  /*  @Override
    public void onBackPressed() {
        Bundle bundle = getIntent().getExtras();
        final String anm = bundle.getString("anm");
        final String phc = bundle.getString("phc");
        final String area = bundle.getString("area");
        Intent rIntent = new Intent(MainActivity.this, HomeActivity.class);
        rIntent.putExtra("phc", phc);
        rIntent.putExtra("anm", anm);
        rIntent.putExtra("area", area);
        startActivity(rIntent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan = (ImageView) findViewById(R.id.btnScan);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtMother = (TextView) findViewById(R.id.txtMother);
        editText = (EditText) findViewById(R.id.editText);
        spinnerMom = (SearchableSpinner) findViewById(R.id.spinnerMom);
        spinnerMom.setTitle("Select Mother");
        spinnerMom.setPositiveButton("OK");
        tvArea = (TextView) findViewById(R.id.tvArea);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        final String anm = bundle.getString("anm");
        final String phc = bundle.getString("phc");
        final String area = bundle.getString("area");
        tvArea.setText(area);
        final String pin = editText.getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + pin);
        final DatabaseReference myRef1 = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> motherNames = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String mother = ds.child("Name").getValue().toString();
                        String father = ds.child("Father").getValue().toString();
                        motherNames.add(mother + " (" + father + ")");
                    } catch (NullPointerException e) {
                        System.out.print(e);
                    }
                }

                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, motherNames);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMom.setAdapter(nameAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinnerMom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String mother = spinnerMom.getSelectedItem().toString();

                DatabaseReference dbr = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother");
                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                String name = ds.child("Name").getValue().toString();
                                String father = ds.child("Father").getValue().toString();
                                String data = name + " (" + father + ")";
                                if (data.equals(mother)) {
                                    String pin = ds.getKey().toString();
                                    editText.setText(pin);
                                }
                            } catch (NullPointerException e) {
                                System.out.print(e);
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

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pin = editText.getText().toString();
                if (!pin.equals("")) {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(pin)) {
                                String area = tvArea.getText().toString();
                                Intent rIntent = new Intent(MainActivity.this, TestActivity.class);
                                rIntent.putExtra("phc", phc);
                                rIntent.putExtra("pin", pin);
                                rIntent.putExtra("anm", anm);
                                rIntent.putExtra("area", area);
                                startActivity(rIntent);
                                rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else {
                                Toast.makeText(MainActivity.this, "Selected wrong area or Entered wrong PIN!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Enter PIN first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtMother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String area = tvArea.getText().toString();
                Intent rIntent = new Intent(MainActivity.this, NewMotherActivity.class);
                rIntent.putExtra("phc", phc);
                rIntent.putExtra("anm", anm);
                rIntent.putExtra("area", area);
                startActivity(rIntent);
                rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                editText.setText(result.getContents().toString());
                Intent rIntent = new Intent(MainActivity.this, TestActivity.class);
                String pin = editText.getText().toString();
                Bundle bundle = getIntent().getExtras();
                final String anm = bundle.getString("anm");
                final String phc = bundle.getString("phc");
                final String area = bundle.getString("area");
                rIntent.putExtra("phc", phc);
                rIntent.putExtra("pin", pin);
                rIntent.putExtra("anm", anm);
                rIntent.putExtra("area", area);
                startActivity(rIntent);
                rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}



