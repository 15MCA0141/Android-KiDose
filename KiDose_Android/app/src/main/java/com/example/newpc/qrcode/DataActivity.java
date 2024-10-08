package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataActivity extends AppCompatActivity {

    public EditText e14,e15,e16,e17,e18,e19,e20,e21,e22,e23,e24,e25,e26,e27,e28,e29,e30,e31,e32;
    public Button btnSubmit,btnUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Bundle bundle = getIntent().getExtras();
        final String phc = bundle.getString("phc");
        final String anm = bundle.getString("anm");
        final String area = bundle.getString("area");
        final String pin = bundle.getString("pin");
        final String name = bundle.getString("name");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ANMRef=database.getReference("PHC/"+phc+"/ANM");
        ANMRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()) {
                    if(eventSnapshot.hasChild(area)){
                       // Toast.makeText(DataActivity.this, eventSnapshot.getKey()+"", Toast.LENGTH_SHORT).show();
                        //final String anm=
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        e14=(EditText)findViewById(R.id.editText14);
        e15=(EditText)findViewById(R.id.editText15);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnUpload=(Button)findViewById(R.id.btnUpload);

        e14.setText(pin);
        e15.setText(name);
        Toast.makeText(DataActivity.this, phc+" : "+anm+" : "+area, Toast.LENGTH_SHORT).show();

            btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mother=e14.getText().toString();
                String child=e15.getText().toString();

                final DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother/"+mother+"/Child/"+child+"/Vaccines");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        e16=(EditText)findViewById(R.id.editText16);
                        e17=(EditText)findViewById(R.id.editText17);
                        e18=(EditText)findViewById(R.id.editText18);
                        e19=(EditText)findViewById(R.id.editText19);
                        e20=(EditText)findViewById(R.id.editText20);
                        e21=(EditText)findViewById(R.id.editText21);
                        e22=(EditText)findViewById(R.id.editText22);
                        e23=(EditText)findViewById(R.id.editText23);
                        e24=(EditText)findViewById(R.id.editText24);
                        e25=(EditText)findViewById(R.id.editText25);
                        e26=(EditText)findViewById(R.id.editText26);
                        e27=(EditText)findViewById(R.id.editText27);
                        e28=(EditText)findViewById(R.id.editText28);
                        e29=(EditText)findViewById(R.id.editText29);
                        e30=(EditText)findViewById(R.id.editText30);
                        e31=(EditText)findViewById(R.id.editText31);
                        e32=(EditText)findViewById(R.id.editText32);

                        if(!e16.getText().toString().equals("")){
                        DatabaseReference val1=myRef.child("BCG/Given On");
                        val1.setValue(e16.getText().toString());}

                        if(!e17.getText().toString().equals("")){
                        DatabaseReference val2=myRef.child("HepB-0/Given On");
                        val2.setValue(e17.getText().toString());}

                        if(!e18.getText().toString().equals("")){
                        DatabaseReference val3=myRef.child("OPV-0/Given On");
                        val3.setValue(e18.getText().toString());}

                        if(!e19.getText().toString().equals("")){
                        DatabaseReference val4=myRef.child("Penta-1/Given On");
                        val4.setValue(e19.getText().toString());}

                        if(!e20.getText().toString().equals("")){
                        DatabaseReference val5=myRef.child("Penta-2/Given On");
                        val5.setValue(e20.getText().toString());}

                        if(!e21.getText().toString().equals("")){
                        DatabaseReference val6=myRef.child("Penta-3/Given On");
                        val6.setValue(e21.getText().toString());}

                        if(!e25.getText().toString().equals("")){
                        DatabaseReference val7=myRef.child("IPV-1/Given On");
                        val7.setValue(e25.getText().toString());}

                        if(!e26.getText().toString().equals("")){
                        DatabaseReference val8=myRef.child("IPV-2/Given On");
                        val8.setValue(e26.getText().toString());}

                        if(!e30.getText().toString().equals("")){
                        DatabaseReference val9=myRef.child("Measles-1/Given On");
                        val9.setValue(e30.getText().toString());}

                        if(!e31.getText().toString().equals("")){
                        DatabaseReference val10=myRef.child("Measles-2/Given On");
                        val10.setValue(e31.getText().toString());}

                        if(!e27.getText().toString().equals("")){
                        DatabaseReference val11=myRef.child("OPV Booster/Given On");
                        val11.setValue(e27.getText().toString());}

                        if(!e28.getText().toString().equals("")){
                        DatabaseReference val12=myRef.child("DPT Booster-1/Given On");
                        val12.setValue(e28.getText().toString());}

                        if(!e29.getText().toString().equals("")){
                        DatabaseReference val13=myRef.child("DPT Booster-2/Given On");
                        val13.setValue(e29.getText().toString());}

                        if(!e22.getText().toString().equals("")){
                        DatabaseReference val14=myRef.child("OPV-1/Given On");
                        val14.setValue(e22.getText().toString());}

                        if(!e23.getText().toString().equals("")){
                        DatabaseReference val15=myRef.child("OPV-2/Given On");
                        val15.setValue(e23.getText().toString());}

                        if(!e24.getText().toString().equals("")){
                        DatabaseReference val16=myRef.child("OPV-3/Given On");
                        val16.setValue(e24.getText().toString());}

                        if(!e32.getText().toString().equals("")){
                        DatabaseReference val17=myRef.child("Next");
                        val17.setValue(e32.getText().toString());}

                        Toast.makeText(DataActivity.this, "Updated!", Toast.LENGTH_SHORT).show();

                        Intent rIntent = new Intent(DataActivity.this, MainActivity.class);
                        rIntent.putExtra("phc", phc);
                        rIntent.putExtra("anm", anm);
                        rIntent.putExtra("area", area);
                        startActivity(rIntent);
                        rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(DataActivity.this, ImageActivity.class);
                rIntent.putExtra("pin", pin);
                rIntent.putExtra("name", name);
                startActivity(rIntent);
            }
        });
    }
}
