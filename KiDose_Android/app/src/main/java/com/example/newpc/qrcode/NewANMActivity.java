package com.example.newpc.qrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Ku$haL on 16-05-2017.
 */
public class NewANMActivity extends AppCompatActivity {

    Spinner spinner3;
    Button btnReg, btnSelect,btnOther;

    EditText etName,etUID,etMob,etEmail,etPass,etCPass,etAcnt,etIfsc,etBank,tvArea;
    String[] area;
    boolean[] selected;
    ArrayList<Integer> userArea=new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anmreg);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnOther = (Button) findViewById(R.id.btnOther);
        tvArea = (EditText) findViewById(R.id.tvArea);
        spinner3=(Spinner)findViewById(R.id.spinner3);

        area = getResources().getStringArray(R.array.area_arrays);
        selected =new boolean[area.length];

        btnSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(NewANMActivity.this);
                mBuilder.setTitle("Select Areas");
                mBuilder.setMultiChoiceItems(area, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    if(isChecked){
                        if(! userArea.contains(position)){
                            userArea.add(position);
                        }
                        else
                        {
                            userArea.remove(position);
                        }
                    }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String str="";
                        String item[]=new String[userArea.size()];
                        for(int i=0;i<userArea.size();i++)
                        {
                        item[i]=area[userArea.get(i)];
                            if(i != userArea.size()-1)
                            {
                                item[i]=item[i]+", ";
                            }
                            str+=item[i];
                         tvArea.setText(str);
                        }
                        //tvArea.setText(item[i]);
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                       for(int i=0;i<selected.length;i++){
                           selected[i]=false;
                           userArea.clear();
                           tvArea.setText("");
                        }
                    }
                });
                AlertDialog mDialog=mBuilder.create();
                mDialog.show();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName=(EditText)findViewById(R.id.etName);
                etUID=(EditText)findViewById(R.id.etUID);
                etMob=(EditText)findViewById(R.id.etMob);
                etEmail=(EditText)findViewById(R.id.etEmail);
                etPass=(EditText)findViewById(R.id.etPass);
                etCPass=(EditText)findViewById(R.id.etCPass);
                etBank=(EditText)findViewById(R.id.etBank);
                etAcnt=(EditText)findViewById(R.id.etAcnt);
                etIfsc=(EditText)findViewById(R.id.etIfsc);

                String name = etName.getText().toString();
                String anm= etUID.getText().toString();
                String mob = etMob.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String cpass = etCPass.getText().toString();
                String bank= etBank.getText().toString();
                String acnt= etAcnt.getText().toString();
                String ifsc= etIfsc.getText().toString();
                String phc=spinner3.getSelectedItem().toString();
                String areas=tvArea.getText().toString();
                String string = areas;
                String[] parts = string.split(", ");

                if (!name.equals("") && !anm.equals("") && !mob.equals("")&& !pass.equals("")&&!phc.equals("")&& !cpass.equals("")&&cpass.equals(pass)) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm);
                    DatabaseReference myRef1=myRef.child("Name");
                    myRef1.setValue(name);
                    DatabaseReference myRef2 = myRef.child("Aadhar");
                    myRef2.setValue(anm);
                    DatabaseReference myRef3 = myRef.child("Mobile");
                    myRef3.setValue(mob);
                    DatabaseReference myRef4 = myRef.child("Email");
                    myRef4.setValue(email);
                    DatabaseReference myRef5 = myRef.child("Password");
                    myRef5.setValue(pass);
                    DatabaseReference myRef6 = myRef.child("PHC");
                    myRef6.setValue(phc);
                    for(int i=0;i<parts.length;i++)
                    {
                        DatabaseReference myRef7 = myRef.child("Areas/"+parts[i]);
                        myRef7.setValue(parts[i]);
                    }
                    DatabaseReference myRef8 = myRef.child("Bank");
                    myRef8.setValue(bank);
                    DatabaseReference myRef9 = myRef.child("Account");
                    myRef9.setValue(acnt);
                    DatabaseReference myRef10 = myRef.child("IFSC");
                    myRef10.setValue(ifsc);

                    Toast.makeText(NewANMActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    Intent rIntent = new Intent(NewANMActivity.this, ANMActivity.class);
                    startActivity(rIntent);
                }
                else
                {
                    Toast.makeText(NewANMActivity.this,"Update Failed, fill all required field!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvArea.setClickable(true);
                tvArea.setEnabled(true);
                tvArea.setText("");
            }
        });
    }
}