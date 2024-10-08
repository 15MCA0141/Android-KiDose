package com.example.newpc.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/*
 Created by Ku$haL on 05-05-2017.
*/
public class NewMotherActivity extends AppCompatActivity {
    //  DataBase myDB;
    Button btnScan, btnSub;
    TextView tvPIN;
    String pin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momreg);

        final Activity activity = this;
        btnScan = (Button) findViewById(R.id.btnScan);
        btnSub = (Button) findViewById(R.id.mbtnSub);
        tvPIN = (TextView) findViewById(R.id.tvPIN);
        Bundle bundle = getIntent().getExtras();
        final String phc = bundle.getString("phc");
        final String anm = bundle.getString("anm");
        final String area = bundle.getString("area");

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                EditText editText3 = (EditText) findViewById(R.id.editText3);
                EditText editText7 = (EditText) findViewById(R.id.editText7);
                EditText editText11 = (EditText) findViewById(R.id.editText11);

                String mother = editText2.getText().toString();
                String father = editText11.getText().toString();
                String uid = editText3.getText().toString();
                String mob = editText7.getText().toString();
                char m=mob.charAt(0);
                if (mother.equals("")) {
                    Toast.makeText(NewMotherActivity.this, "You did not enter the Mother's Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (father.equals("")) {
                    Toast.makeText(NewMotherActivity.this, "You did not enter the Father's Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tvPIN.getText().equals("")) {
                    Toast.makeText(NewMotherActivity.this, "You did not Scanned QR Code", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mob.matches("")) {
                    Toast.makeText(NewMotherActivity.this, "You did not enter the Mobile number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mob.length()<10) {
                    Toast.makeText(NewMotherActivity.this, "You did not enter a valid Mobile number", Toast.LENGTH_SHORT).show();
                    return;
                } else if(m!='7'&&m!='8'&&m!='9'){
                        Toast.makeText(NewMotherActivity.this, "A Mobile number can't start with "+m, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + pin);
                    DatabaseReference fbChild1 = myRef.child("Name");
                    fbChild1.setValue(mother);
                    DatabaseReference fbChild2 = myRef.child("Father");
                    fbChild2.setValue(father);
                    DatabaseReference fbChild3 = myRef.child("Aadhar");
                    fbChild3.setValue(uid);
                    DatabaseReference fbChild4 = myRef.child("Mobile");
                    fbChild4.setValue(mob);
                    DatabaseReference fbChild5 = myRef.child("PIN");
                    fbChild5.setValue(pin);

                    Toast.makeText(NewMotherActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    Intent rIntent = new Intent(NewMotherActivity.this, NewChildActivity.class);
                    rIntent.putExtra("phc", phc);
                    rIntent.putExtra("anm", anm);
                    rIntent.putExtra("area", area);
                    rIntent.putExtra("pin", pin);
                    startActivity(rIntent);
                    rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                }
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
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
                btnSub.setVisibility(View.VISIBLE);
                pin = result.getContents().toString();
                tvPIN.setText(" Registered with PIN No.: \n" + pin);
                btnScan.setVisibility(View.INVISIBLE);
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
