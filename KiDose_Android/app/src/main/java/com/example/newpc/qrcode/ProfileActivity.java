package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

/**
 * Created by Ku$haL on 03-05-2017.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final EditText etName,etUID,etMob,etEmail,etPass,etCpass,etAcnt,etBank,etIfsc,etPHC;
        ImageView btnEdit;
        Button btnUpd;
        final TextView etAreas;
        etName=(EditText)findViewById(R.id.etName);
        etUID=(EditText)findViewById(R.id.etUID);
        etMob=(EditText)findViewById(R.id.etMob);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPass=(EditText)findViewById(R.id.etPass);
        etCpass=(EditText)findViewById(R.id.etCPass);
        etAcnt=(EditText)findViewById(R.id.etAcnt);
        etBank=(EditText)findViewById(R.id.etBank);
        etIfsc=(EditText)findViewById(R.id.etIfsc);
        etPHC=(EditText)findViewById(R.id.etPHC);
        btnEdit=(ImageView) findViewById(R.id.btnEdit);
        btnUpd=(Button) findViewById(R.id.btnUpd);
        etAreas=(TextView)findViewById(R.id.etAreas);

        Bundle bundle = getIntent().getExtras();
        final String phc = bundle.getString("phc");
        final String anm = bundle.getString("anm");

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("PHC/"+phc+"/ANM/"+anm);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    etName.setText(dataSnapshot.child("Name").getValue().toString());
                    etUID.setText(dataSnapshot.child("Aadhar").getValue().toString());
                    etMob.setText(dataSnapshot.child("Mobile").getValue().toString());
                    etEmail.setText(dataSnapshot.child("Email").getValue().toString());
                    etAcnt.setText(dataSnapshot.child("Account").getValue().toString());
                    etBank.setText(dataSnapshot.child("Bank").getValue().toString());
                    etIfsc.setText(dataSnapshot.child("IFSC").getValue().toString());
                    etPHC.setText(dataSnapshot.child("PHC").getValue().toString());
                }catch (NullPointerException e){System.out.print(e);}

                DatabaseReference areaRef = db.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas");
                areaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String area="";
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        area =area+"\n"+ ds.getKey().toString();
                    }
                    etAreas.setText(area);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMob.setEnabled(true);
                etPass.setEnabled(true);
                etEmail.setEnabled(true);
                etCpass.setEnabled(true);
                etAcnt.setEnabled(true);
                etBank.setEnabled(true);
                etIfsc.setEnabled(true);
            }
        });
        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mob = etMob.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String cpass = etCpass.getText().toString();
                String bank = etBank.getText().toString();
                String acnt = etAcnt.getText().toString();
                String ifsc = etIfsc.getText().toString();

                if (!mob.equals("") && !pass.equals("") && !phc.equals("") && !cpass.equals("") && cpass.equals(pass)) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("PHC/" + phc + "/ANM/" + anm);
                    DatabaseReference myRef3 = myRef.child("Mobile");
                    myRef3.setValue(mob);
                    DatabaseReference myRef4 = myRef.child("Email");
                    myRef4.setValue(email);
                    DatabaseReference myRef5 = myRef.child("Password");
                    myRef5.setValue(pass);
                    DatabaseReference myRef8 = myRef.child("Bank");
                    myRef8.setValue(bank);
                    DatabaseReference myRef9 = myRef.child("Account");
                    myRef9.setValue(acnt);
                    DatabaseReference myRef10 = myRef.child("IFSC");
                    myRef10.setValue(ifsc);
                    Toast.makeText(ProfileActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    Intent rIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(rIntent);
                }
                else
                    Toast.makeText(ProfileActivity.this, "Update Failed!", Toast.LENGTH_SHORT).show();
            }

        });


    }

}
