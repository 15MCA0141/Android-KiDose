package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    public String str="";
    private Spinner spinner,spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7,spinner8,spinner9,spinner10,spinner11,spinner12,spinner13,spinner14,spinner15,spinner16;
    private CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10,cb11,cb12,cb13,cb14,cb15,cb16;
    private Button btnAdd,btnUpd;
    private TextView tvName,tvDOB,tvAge,tvAlert,tvAges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines);

        Bundle bundle = getIntent().getExtras();

        final String phc = bundle.getString("phc");
        final String anm = bundle.getString("anm");
        final String area = bundle.getString("area");
        final String mother = bundle.getString("pin");
        final String child = bundle.getString("name");

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpd = (Button) findViewById(R.id.btnUpd);
        spinner7=(Spinner)findViewById(R.id.spinner7);

        tvName=(TextView)findViewById(R.id.tvName);
        tvDOB=(TextView)findViewById(R.id.tvDOB);
        tvAge=(TextView)findViewById(R.id.tvAge);
        tvAges=(TextView)findViewById(R.id.tvAges);
        tvAlert=(TextView)findViewById(R.id.tvAlert);

        cb1=(CheckBox)findViewById(R.id.cb1);
        cb2=(CheckBox)findViewById(R.id.cb2);
        cb3=(CheckBox)findViewById(R.id.cb3);
        cb4=(CheckBox)findViewById(R.id.cb4);
        cb5=(CheckBox)findViewById(R.id.cb5);
        cb6=(CheckBox)findViewById(R.id.cb6);
        cb7=(CheckBox)findViewById(R.id.cb7);
        cb8=(CheckBox)findViewById(R.id.cb8);
        cb9=(CheckBox)findViewById(R.id.cb9);
        cb10=(CheckBox)findViewById(R.id.cb10);
        cb11=(CheckBox)findViewById(R.id.cb11);
        cb12=(CheckBox)findViewById(R.id.cb12);
        cb13=(CheckBox)findViewById(R.id.cb13);
        cb14=(CheckBox)findViewById(R.id.cb14);
        cb15=(CheckBox)findViewById(R.id.cb15);
        cb16=(CheckBox)findViewById(R.id.cb16);

        spinner=(Spinner)findViewById(R.id.spinner);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);
        spinner3=(Spinner)findViewById(R.id.spinner3);
        spinner4=(Spinner)findViewById(R.id.spinner4);
        spinner5=(Spinner)findViewById(R.id.spinner5);
        spinner6=(Spinner)findViewById(R.id.spinner6);
        spinner7=(Spinner)findViewById(R.id.spinner7);
        spinner8=(Spinner)findViewById(R.id.spinner8);
        spinner9=(Spinner)findViewById(R.id.spinner9);
        spinner10=(Spinner)findViewById(R.id.spinner10);
        spinner11=(Spinner)findViewById(R.id.spinner11);
        spinner12=(Spinner)findViewById(R.id.spinner12);
        spinner13=(Spinner)findViewById(R.id.spinner13);
        spinner14=(Spinner)findViewById(R.id.spinner14);
        spinner15=(Spinner)findViewById(R.id.spinner15);
        spinner16=(Spinner)findViewById(R.id.spinner16);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother/"+mother+"/Child");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> childNames = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    childNames.add(ds.getKey().toString());
                }
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(TestActivity.this, android.R.layout.simple_spinner_item, childNames);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(nameAdapter);
                if(spinner.getChildCount()==0)
                    disable();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String name=spinner.getSelectedItem().toString();
                tvName.setText(name);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother/"+mother+"/Child/"+name+"/DOB");

                final DatabaseReference myAlert = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother/"+mother+"/Child/"+name+"/Vaccines");

                myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvAlert.setText("");
                        String date="",vac="",alert="";
                        for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()){
                            for(DataSnapshot childEventSnapshot:eventSnapshot.getChildren()){
                                String str=childEventSnapshot.getKey().toString();
                                if(str.matches("Alert")){
                                    vac=eventSnapshot.getKey().toString();
                                    date=childEventSnapshot.child("Date").getValue().toString();
                                    alert+=vac+" : "+date+"\n";
                                    tvAlert.setText(alert);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    disable();
                    String bd = dataSnapshot.getValue().toString();
                    tvDOB.setText(bd);
                    String db = tvDOB.getText().toString();
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                    int day = formatter1.parse(db).getDate();
                    int month = formatter1.parse(db).getMonth();
                    int year = formatter1.parse(db).getYear();
                    year += 1900;
                    //month+=1;
                    tvAges.setText(getAges(year, month, day));
                    tvAge.setText(getAge(year, month, day));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                        String mon = tvAge.getText().toString();

                        String[] arr = String.valueOf(mon).split("\\.");
                        int[] i = new int[2];
                        i[0] = Integer.parseInt(arr[0]);
                        i[1] = Integer.parseInt(arr[1]);
                        int months = i[0];
                        int days = i[1];
                        if (months == 0 && days >= 0) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("HepB-0")){
                                        if (!dataSnapshot.child("HepB-0").hasChild("Given On")) {
                                            cb1.setEnabled(true);
                                            cb1.setClickable(true);
                                            spinner1.setEnabled(true);
                                            spinner1.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-0")){
                                        if (!dataSnapshot.child("OPV-0").hasChild("Given On")) {
                                            cb2.setEnabled(true);
                                            cb2.setClickable(true);
                                            spinner2.setEnabled(true);
                                            spinner2.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("BCG")){
                                        if (!dataSnapshot.child("BCG").hasChild("Given On")) {
                                            cb3.setEnabled(true);
                                            cb3.setClickable(true);
                                            spinner3.setEnabled(true);
                                            spinner3.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                            if (days > 3) {
                                cb1.setEnabled(false);
                                cb1.setClickable(false);
                                spinner1.setEnabled(false);
                                spinner1.setClickable(false);
                            }
                            if (days > 15) {
                                cb2.setEnabled(false);
                                cb2.setClickable(false);
                                spinner2.setEnabled(false);
                                spinner2.setClickable(false);
                            }
                        } else if (months == 1 && days >= 0 && days < 15) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("BCG")){
                                        if (!dataSnapshot.child("BCG").hasChild("Given On")) {
                                            cb3.setEnabled(true);
                                            cb3.setClickable(true);
                                            spinner3.setEnabled(true);
                                            spinner3.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if ((months == 1 && days >= 15 && days <= 31) || (months == 2 && days >= 0 && days < 15)) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("BCG")){
                                        if (!dataSnapshot.child("BCG").hasChild("Given On")) {
                                            cb3.setEnabled(true);
                                            cb3.setClickable(true);
                                            spinner3.setEnabled(true);
                                            spinner3.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-1")){
                                        if (!dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                            cb4.setEnabled(true);
                                            cb4.setClickable(true);
                                            spinner4.setEnabled(true);
                                            spinner4.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-1")){
                                        if (!dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                            cb5.setEnabled(true);
                                            cb5.setClickable(true);
                                            spinner5.setEnabled(true);
                                            spinner5.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("IPV-1")){
                                        if (!dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                            cb6.setEnabled(true);
                                            cb6.setClickable(true);
                                            spinner6.setEnabled(true);
                                            spinner6.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if ((months == 2 && days >= 15 && days <= 31) || (months == 3 && days >= 0 && days < 15)) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("BCG")){
                                        if (!dataSnapshot.child("BCG").hasChild("Given On")) {
                                            cb3.setEnabled(true);
                                            cb3.setClickable(true);
                                            spinner3.setEnabled(true);
                                            spinner3.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-1")){
                                        if (!dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                            Toast.makeText(TestActivity.this, dataSnapshot.hasChild("Penta-1") + "", Toast.LENGTH_SHORT).show();
                                            cb4.setEnabled(true);
                                            cb4.setClickable(true);
                                            spinner4.setEnabled(true);
                                            spinner4.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-1")){
                                        if (!dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                            cb5.setEnabled(true);
                                            cb5.setClickable(true);
                                            spinner5.setEnabled(true);
                                            spinner5.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("IPV-1")){
                                        if (!dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                            cb6.setEnabled(true);
                                            cb6.setClickable(true);
                                            spinner6.setEnabled(true);
                                            spinner6.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-2")){
                                        if (!dataSnapshot.child("Penta-2").hasChild("Given On")) {
                                            cb7.setEnabled(true);
                                            cb7.setClickable(true);
                                            spinner7.setEnabled(true);
                                            spinner7.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-2")){
                                        if (!dataSnapshot.child("OPV-2").hasChild("Given On")) {
                                            cb8.setEnabled(true);
                                            cb8.setClickable(true);
                                            spinner8.setEnabled(true);
                                            spinner8.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if ((months == 3 && days >= 15 && days <= 31) || (months >= 4 && months < 9 && days >= 0 && days <= 31)) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("BCG")){
                                        if (!dataSnapshot.child("BCG").hasChild("Given On")) {
                                            cb3.setEnabled(true);
                                            cb3.setClickable(true);
                                            spinner3.setEnabled(true);
                                            spinner3.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-1")){
                                        if (!dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                            cb4.setEnabled(true);
                                            cb4.setClickable(true);
                                            spinner4.setEnabled(true);
                                            spinner4.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-1")){
                                        if (!dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                            cb5.setEnabled(true);
                                            cb5.setClickable(true);
                                            spinner5.setEnabled(true);
                                            spinner5.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("IPV-1")){
                                        if (!dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                            cb6.setEnabled(true);
                                            cb6.setClickable(true);
                                            spinner6.setEnabled(true);
                                            spinner6.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-2")){
                                        if (!dataSnapshot.child("Penta-2").hasChild("Given On")) {
                                            cb7.setEnabled(true);
                                            cb7.setClickable(true);
                                            spinner7.setEnabled(true);
                                            spinner7.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-2")){
                                        if (!dataSnapshot.child("OPV-2").hasChild("Given On")) {
                                            cb8.setEnabled(true);
                                            cb8.setClickable(true);
                                            spinner8.setEnabled(true);
                                            spinner8.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-3")){
                                        if (!dataSnapshot.child("Penta-3").hasChild("Given On")) {
                                            cb9.setEnabled(true);
                                            cb9.setClickable(true);
                                            spinner9.setEnabled(true);
                                            spinner9.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-3")){
                                        if (!dataSnapshot.child("OPV-3").hasChild("Given On")) {
                                            cb10.setEnabled(true);
                                            cb10.setClickable(true);
                                            spinner10.setEnabled(true);
                                            spinner10.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("IPV-2")){
                                        if (!dataSnapshot.child("IPV-2").hasChild("Given On")) {
                                            cb11.setEnabled(true);
                                            cb11.setClickable(true);
                                            spinner11.setEnabled(true);
                                            spinner11.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if (months >= 9 && months <= 12 && days >= 0 && days < 31) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("BCG")){
                                        if (!dataSnapshot.child("BCG").hasChild("Given On")) {
                                            cb3.setEnabled(true);
                                            cb3.setClickable(true);
                                            spinner3.setEnabled(true);
                                            spinner3.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-1")){
                                        if (!dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                            cb4.setEnabled(true);
                                            cb4.setClickable(true);
                                            spinner4.setEnabled(true);
                                            spinner4.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-1")){
                                        if (!dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                            cb5.setEnabled(true);
                                            cb5.setClickable(true);
                                            spinner5.setEnabled(true);
                                            spinner5.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("IPV-1")){
                                        if (!dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                            cb6.setEnabled(true);
                                            cb6.setClickable(true);
                                            spinner6.setEnabled(true);
                                            spinner6.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-2")){
                                        if (!dataSnapshot.child("Penta-2").hasChild("Given On")) {
                                            cb7.setEnabled(true);
                                            cb7.setClickable(true);
                                            spinner7.setEnabled(true);
                                            spinner7.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-2")){
                                        if (!dataSnapshot.child("OPV-2").hasChild("Given On")) {
                                            cb8.setEnabled(true);
                                            cb8.setClickable(true);
                                            spinner8.setEnabled(true);
                                            spinner8.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Penta-3")){
                                        if (!dataSnapshot.child("Penta-3").hasChild("Given On")) {
                                            cb9.setEnabled(true);
                                            cb9.setClickable(true);
                                            spinner9.setEnabled(true);
                                            spinner9.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV-3")){
                                        if (!dataSnapshot.child("OPV-3").hasChild("Given On")) {
                                            cb10.setEnabled(true);
                                            cb10.setClickable(true);
                                            spinner10.setEnabled(true);
                                            spinner10.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("IPV-2")){
                                        if (!dataSnapshot.child("IPV-2").hasChild("Given On")) {
                                            cb11.setEnabled(true);
                                            cb11.setClickable(true);
                                            spinner11.setEnabled(true);
                                            spinner11.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Measles-1")){
                                        if (!dataSnapshot.child("Measles-1").hasChild("Given On")) {
                                            cb12.setEnabled(true);
                                            cb12.setClickable(true);
                                            spinner12.setEnabled(true);
                                            spinner12.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if (months >= 13 && months < 16 && days >= 0 && days < 31) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("Measles-1")){
                                        if (!dataSnapshot.child("Measles-1").hasChild("Given On")) {
                                            cb12.setEnabled(true);
                                            cb12.setClickable(true);
                                            spinner12.setEnabled(true);
                                            spinner12.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if (months >= 16 && months <= 60 && days >= 0 && days < 31) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("Measles-1")){
                                        if (!dataSnapshot.child("Measles-1").hasChild("Given On")) {
                                            cb12.setEnabled(true);
                                            cb12.setClickable(true);
                                            spinner12.setEnabled(true);
                                            spinner12.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("Measles-2")){
                                        if (!dataSnapshot.child("Measles-2").hasChild("Given On")) {
                                            cb13.setEnabled(true);
                                            cb13.setClickable(true);
                                            spinner13.setEnabled(true);
                                            spinner13.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("OPV Booster")){
                                        if (!dataSnapshot.child("OPV Booster").hasChild("Given On")) {
                                            cb14.setEnabled(true);
                                            cb14.setClickable(true);
                                            spinner14.setEnabled(true);
                                            spinner14.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("DPT Booster-1")){
                                        if (!dataSnapshot.child("DPT Booster-1").hasChild("Given On")) {
                                            cb15.setEnabled(true);
                                            cb15.setClickable(true);
                                            spinner15.setEnabled(true);
                                            spinner15.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if (months > 60 && months <= 84) {
                            myAlert.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild("DPT Booster-1")){
                                        if (!dataSnapshot.child("DPT Booster-1").hasChild("Given On")) {
                                            cb15.setEnabled(true);
                                            cb15.setClickable(true);
                                            spinner15.setEnabled(true);
                                            spinner15.setClickable(true);
                                        }}
                                    if (!dataSnapshot.hasChild("DPT Booster-2")){
                                        if (!dataSnapshot.child("DPT Booster-2").hasChild("Given On")) {
                                            cb16.setEnabled(true);
                                            cb16.setClickable(true);
                                            spinner16.setEnabled(true);
                                            spinner16.setClickable(true);
                                        }}
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else if (months > 84)
                            Toast.makeText(TestActivity.this, "Vaccination duration finished!", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(TestActivity.this, "No vaccine to be given now!", Toast.LENGTH_SHORT).show();
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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message ="";
                Bundle bundle = getIntent().getExtras();
                message = bundle.getString("pin");

                Intent rIntent = new Intent(TestActivity.this, NewChildActivity.class);
                String pin="";
                pin=message;
                rIntent.putExtra("pin",pin);
                rIntent.putExtra("phc",phc);
                rIntent.putExtra("anm",anm);
                rIntent.putExtra("area",area);
                startActivity(rIntent);
            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb1.isChecked()){
                    spinner1.setVisibility(View.INVISIBLE);
                }
                else
                    spinner1.setVisibility(View.VISIBLE);
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb2.isChecked()){
                    spinner2.setVisibility(View.INVISIBLE);
                }
                else
                    spinner2.setVisibility(View.VISIBLE);
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb3.isChecked()){
                    spinner3.setVisibility(View.INVISIBLE);
                }
                else
                    spinner3.setVisibility(View.VISIBLE);
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb4.isChecked()){
                    spinner4.setVisibility(View.INVISIBLE);
                }
                else
                    spinner4.setVisibility(View.VISIBLE);
            }
        });
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb5.isChecked()){
                    spinner5.setVisibility(View.INVISIBLE);
                }
                else
                    spinner5.setVisibility(View.VISIBLE);
            }
        });
        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb6.isChecked()){
                    spinner6.setVisibility(View.INVISIBLE);
                }
                else
                    spinner6.setVisibility(View.VISIBLE);
            }
        });
        cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb7.isChecked()){
                    spinner7.setVisibility(View.INVISIBLE);
                }
                else
                    spinner7.setVisibility(View.VISIBLE);
            }
        });
        cb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb8.isChecked()){
                    spinner8.setVisibility(View.INVISIBLE);
                }
                else
                    spinner8.setVisibility(View.VISIBLE);
            }
        });
        cb9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb9.isChecked()){
                    spinner9.setVisibility(View.INVISIBLE);
                }
                else
                    spinner9.setVisibility(View.VISIBLE);
            }
        });
        cb10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb10.isChecked()){
                    spinner10.setVisibility(View.INVISIBLE);
                }
                else
                    spinner10.setVisibility(View.VISIBLE);
            }
        });
        cb11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb11.isChecked()){
                    spinner11.setVisibility(View.INVISIBLE);
                }
                else
                    spinner11.setVisibility(View.VISIBLE);
            }
        });
        cb12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb12.isChecked()){
                    spinner12.setVisibility(View.INVISIBLE);
                }
                else
                    spinner12.setVisibility(View.VISIBLE);
            }
        });
        cb13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb13.isChecked()){
                    spinner13.setVisibility(View.INVISIBLE);
                }
                else
                    spinner13.setVisibility(View.VISIBLE);
            }
        });
        cb14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb14.isChecked()){
                    spinner14.setVisibility(View.INVISIBLE);
                }
                else
                    spinner14.setVisibility(View.VISIBLE);
            }
        });
        cb15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb15.isChecked()){
                    spinner15.setVisibility(View.INVISIBLE);
                }
                else
                    spinner15.setVisibility(View.VISIBLE);
            }
        });
        cb16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cb16.isChecked()){
                    spinner16.setVisibility(View.INVISIBLE);
                }
                else
                    spinner16.setVisibility(View.VISIBLE);
            }
        });

        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=tvName.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother/"+mother+"/Child/"+name+"/Vaccines");

                String mon = tvAge.getText().toString();

                String[] arr = String.valueOf(mon).split("\\.");
                int[] i = new int[2];
                i[0] = Integer.parseInt(arr[0]);
                i[1] = Integer.parseInt(arr[1]);
                int months = i[0];
                int days = i[1];

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String today=dateFormat.format(date);

                if(months==0 && days>=0)
                {
                    if(days>3)
                    {
                        DatabaseReference fbChild1 = myRef.child("HepB-0/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("HepB-0/Alert/Reason");
                        fbChild2.setValue("Date Exceeded");
                    }
                    if(days>15)
                    {
                        DatabaseReference fbChild1 = myRef.child("OPV-0/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("OPV-0/Alert/Reason");
                        fbChild2.setValue("Date Exceeded");
                    }
                    if (cb1.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("HepB-0/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            //cal.setTime(today);
                            cal.add(Calendar.DATE, 43); // Adding 45 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if(cb1.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("HepB-0/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("HepB-0/Alert/Reason");
                        fbChild2.setValue(spinner1.getSelectedItem().toString());
                        }}
                    if (cb2.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("OPV-0/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 43); // Adding 45 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if(cb2.isEnabled()==true){
                        DatabaseReference fbChild2 = myRef.child("OPV-0/Alert/Date");
                        fbChild2.setValue(today);
                        DatabaseReference fbChild3 = myRef.child("OPV-0/Alert/Reason");
                        fbChild3.setValue(spinner2.getSelectedItem().toString());
                    }}
                    if (cb3.isChecked()) {
                        DatabaseReference fbChild3 = myRef.child("BCG/Given On");
                        fbChild3.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                         //   cal.setTime(next);
                         /*   cal.add(Calendar.DATE, 43); // Adding 45 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else{
                        if(cb3.isEnabled()==true){
                        DatabaseReference fbChild3 = myRef.child("BCG/Alert/Date");
                        fbChild3.setValue(today);
                        DatabaseReference fbChild4 = myRef.child("BCG/Alert/Reason");
                        fbChild4.setValue(spinner3.getSelectedItem().toString());
                    }}
                }
                else if( months==1 && days>=0 && days<15)
                {
                    if(cb3.isChecked()){
                        DatabaseReference fbChild1 = myRef.child("BCG/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                      //      cal.setTime(next);
                        /*    cal.add(Calendar.DATE, 27); // Adding 45 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else{
                        if(cb3.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("BCG/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("BCG/Alert/Reason");
                        fbChild2.setValue(spinner3.getSelectedItem().toString());
                    }}
                }
                else if((months==1 && days>=15 && days<=31)||( months==2 && days>=0 && days<15))
                {
                    if (cb3.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("BCG/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                        //    cal.setTime(next);
                         /*   cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else{
                        if(cb3.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("BCG/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("BCG/Alert/Reason");
                        fbChild2.setValue(spinner3.getSelectedItem().toString());
                    }}
                    if (cb4.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                           cal.add(Calendar.DATE, 27); // Adding 75 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb4.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-1/Alert/Reason");
                        fbChild2.setValue(spinner4.getSelectedItem().toString());
                    }}

                    if(cb5.isChecked()){
                        DatabaseReference fbChild2 = myRef.child("OPV-1/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        if(cb5.isEnabled()==true){
                        DatabaseReference fbChild2 = myRef.child("OPV-1/Alert/Date");
                        fbChild2.setValue(today);
                        DatabaseReference fbChild3 = myRef.child("OPV-1/Alert/Reason");
                        fbChild3.setValue(spinner5.getSelectedItem().toString());
                    }}

                    if(cb6.isChecked()){
                        DatabaseReference fbChild3 = myRef.child("IPV-1/Given On");
                        fbChild3.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                         //   cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        if(cb6.isEnabled()==true){
                        DatabaseReference fbChild3 = myRef.child("IPV-1/Alert/Date");
                        fbChild3.setValue(today);
                        DatabaseReference fbChild4 = myRef.child("IPV-1/Alert/Reason");
                        fbChild4.setValue(spinner6.getSelectedItem().toString());
                    }}
                }
                else if((months==2 && days>=15 && days<=31)|| (months==3 && days>=0 && days<15))
                {
                    if (cb3.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("BCG/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                          /*  cal.add(Calendar.DATE, 103); // Adding 105 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb3.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("BCG/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("BCG/Alert/Reason");
                        fbChild2.setValue(spinner3.getSelectedItem().toString());
                    }}

                    if (cb4.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb4.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-1/Alert/Reason");
                        fbChild2.setValue(spinner4.getSelectedItem().toString());
                    }}

                    if (cb5.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("OPV-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb5.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("OPV-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("OPV-1/Alert/Reason");
                        fbChild2.setValue(spinner5.getSelectedItem().toString());
                    }}

                    if (cb6.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("IPV-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb6.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("IPV-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("IPV-1/Alert/Reason");
                        fbChild2.setValue(spinner6.getSelectedItem().toString());
                    }}

                    if (cb7.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-2/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                         //   cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb7.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-2/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-2/Alert/Reason");
                        fbChild2.setValue(spinner7.getSelectedItem().toString());
                    }}
                    if (cb8.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("OPV-2/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                         //   cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb8.isEnabled()==true){
                        DatabaseReference fbChild2 = myRef.child("OPV-2/Alert/Date");
                        fbChild2.setValue(today);
                        DatabaseReference fbChild3 = myRef.child("OPV-2/Alert/Reason");
                        fbChild3.setValue(spinner8.getSelectedItem().toString());
                    }}
                }
                else if((months==3 && days>=15 && days<=31)||( months>=4 && months<9 && days>=0 && days<=31))
                {
                    if (cb3.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("BCG/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                         /*   cal.add(Calendar.DATE, 268); // Adding 270 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb3.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("BCG/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("BCG/Alert/Reason");
                        fbChild2.setValue(spinner3.getSelectedItem().toString());
                    }}

                    if (cb4.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                         //   cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb4.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-1/Alert/Reason");
                        fbChild2.setValue(spinner4.getSelectedItem().toString());
                    }}

                    if (cb5.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("OPV-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb5.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("OPV-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("OPV-1/Alert/Reason");
                        fbChild2.setValue(spinner5.getSelectedItem().toString());
                    }}

                    if (cb6.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("IPV-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb6.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("IPV-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("IPV-1/Alert/Reason");
                        fbChild2.setValue(spinner6.getSelectedItem().toString());
                    }}

                    if (cb7.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-2/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb7.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-2/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-2/Alert/Reason");
                        fbChild2.setValue(spinner7.getSelectedItem().toString());
                    }}
                    if (cb8.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("OPV-2/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            //cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if (cb8.isEnabled() == true) {
                            DatabaseReference fbChild2 = myRef.child("OPV-2/Alert/Date");
                            fbChild2.setValue(today);
                            DatabaseReference fbChild3 = myRef.child("OPV-2/Alert/Reason");
                            fbChild3.setValue(spinner8.getSelectedItem().toString());
                        }}
                    if (cb9.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-3/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 162); // Adding 165 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb9.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-3/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-3/Alert/Reason");
                        fbChild2.setValue(spinner9.getSelectedItem().toString());
                    }}
                    if (cb10.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("OPV-3/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            //cal.setTime(next);
                            cal.add(Calendar.DATE, 162); // Adding 165 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb10.isEnabled()==true){
                        DatabaseReference fbChild2 = myRef.child("OPV-3/Alert/Date");
                        fbChild2.setValue(today);
                        DatabaseReference fbChild3 = myRef.child("OPV-3/Alert/Reason");
                        fbChild3.setValue(spinner10.getSelectedItem().toString());
                    }}
                    if (cb11.isChecked()) {
                        DatabaseReference fbChild3 = myRef.child("IPV-2/Given On");
                        fbChild3.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb11.isEnabled()==true){
                        DatabaseReference fbChild3 = myRef.child("IPV-2/Alert/Date");
                        fbChild3.setValue(today);
                        DatabaseReference fbChild4 = myRef.child("IPV-2/Alert/Reason");
                        fbChild4.setValue(spinner11.getSelectedItem().toString());
                    }}
                }
                else if(months>=9 && months<=12 && days>=0 && days<31)
                {
                    if (cb3.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("BCG/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                           /* cal.add(Calendar.DATE, 478); // Adding 480 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb3.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("BCG/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("BCG/Alert/Reason");
                        fbChild2.setValue(spinner3.getSelectedItem().toString());
                    }}

                    if (cb4.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb4.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-1/Alert/Reason");
                        fbChild2.setValue(spinner4.getSelectedItem().toString());
                    }}

                    if (cb5.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("OPV-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb5.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("OPV-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("OPV-1/Alert/Reason");
                        fbChild2.setValue(spinner5.getSelectedItem().toString());
                    }}

                    if (cb6.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("IPV-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb6.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("IPV-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("IPV-1/Alert/Reason");
                        fbChild2.setValue(spinner6.getSelectedItem().toString());
                    }}

                    if (cb7.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-2/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb7.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-2/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-2/Alert/Reason");
                        fbChild2.setValue(spinner7.getSelectedItem().toString());
                    }}
                    if (cb8.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("OPV-2/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 27); // Adding 30 days to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb8.isEnabled()==true){
                        DatabaseReference fbChild2 = myRef.child("OPV-2/Alert/Date");
                        fbChild2.setValue(today);
                        DatabaseReference fbChild3 = myRef.child("OPV-2/Alert/Reason");
                        fbChild3.setValue(spinner8.getSelectedItem().toString());
                    }}

                    if (cb9.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Penta-3/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 162); // Adding 165 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb9.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Penta-3/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Penta-3/Alert/Reason");
                        fbChild2.setValue(spinner9.getSelectedItem().toString());
                    }}
                    if (cb10.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("OPV-3/Given On");
                        fbChild2.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 162); // Adding 165 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb10.isEnabled()==true){
                        DatabaseReference fbChild2 = myRef.child("OPV-3/Alert/Date");
                        fbChild2.setValue(today);
                        DatabaseReference fbChild3 = myRef.child("OPV-3/Alert/Reason");
                        fbChild3.setValue(spinner10.getSelectedItem().toString());
                    }}
                    if (cb11.isChecked()) {
                        DatabaseReference fbChild3 = myRef.child("IPV-2/Given On");
                        fbChild3.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                           // cal.setTime(next);
                            cal.add(Calendar.DATE, 162); // Adding 165 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb11.isEnabled()==true){
                        DatabaseReference fbChild3 = myRef.child("IPV-2/Alert/Date");
                        fbChild3.setValue(today);
                        DatabaseReference fbChild4 = myRef.child("IPV-2/Alert/Reason");
                        fbChild4.setValue(spinner11.getSelectedItem().toString());
                    }}

                    if (cb12.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Measles-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            //cal.setTime(next);
                            cal.add(Calendar.DATE, 207); // Adding 210 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb12.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Measles-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Measles-1/Alert/Reason");
                        fbChild2.setValue(spinner12.getSelectedItem().toString());
                    }}
                }
                else if(months>=13 && months<16 && days>=0 && days<31)
                {
                    if (cb12.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Measles-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            cal.setTime(next);
                            cal.add(Calendar.DATE, 207); // Adding 210 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb12.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Measles-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Measles-1/Alert/Reason");
                        fbChild2.setValue(spinner12.getSelectedItem().toString());
                    }}
                }
                else if(months>=16 && months<=60 && days>=0 && days<31)
                {
                    if (cb12.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("Measles-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                          //  cal.setTime(next);
                            cal.add(Calendar.DATE, 177); // Adding 180 days to today
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb12.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("Measles-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("Measles-1/Alert/Reason");
                        fbChild2.setValue(spinner12.getSelectedItem().toString());
                    }}

                    if (cb13.isChecked()) {
                    DatabaseReference fbChild1 = myRef.child("Measles-2/Given On");
                    fbChild1.setValue(today);
                    }else{
                        if(cb13.isEnabled()==true){
                    DatabaseReference fbChild1 = myRef.child("Measles-2/Alert/Date");
                    fbChild1.setValue(today);
                    DatabaseReference fbChild2 = myRef.child("Measles-2/Alert/Reason");
                    fbChild2.setValue(spinner13.getSelectedItem().toString());
                     }}

                    if (cb14.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("OPV Booster/Given On");
                        fbChild1.setValue(today);

                        spinner14.setVisibility(View.INVISIBLE);
                    }else{
                        if(cb14.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("OPV Booster/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("OPV Booster/Alert/Reason");
                        fbChild2.setValue(spinner14.getSelectedItem().toString());
                    }}

                    if (cb15.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("DPT Booster-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            cal.setTime(next);
                            cal.add(Calendar.DATE, 1822); // Adding 5 years to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(cb15.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("DPT Booster-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("DPT Booster-1/Alert/Reason");
                        fbChild2.setValue(spinner15.getSelectedItem().toString());
                    }}
                }

                else if(months>60 && months<=84)
                {
                    if (cb15.isChecked()) {
                        DatabaseReference fbChild1 = myRef.child("DPT Booster-1/Given On");
                        fbChild1.setValue(today);

                        DatabaseReference fbnext = myRef.child("Next");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            String d1=tvDOB.getText().toString();
                            Date next=sdf.parse(d1);
                            Calendar cal=Calendar.getInstance();
                            cal.setTime(next);
                            cal.add(Calendar.DATE, 2190); // Adding 6 years to dob
                            String output = sdf.format(cal.getTime());
                            Toast.makeText(TestActivity.this, "Next vaccination date: "+output, Toast.LENGTH_SHORT).show();
                            fbnext.setValue(output);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else{
                        if(cb15.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("DPT Booster-1/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("DPT Booster-1/Alert/Reason");
                        fbChild2.setValue(spinner15.getSelectedItem().toString());
                    }}
                    if (cb16.isChecked()) {
                        DatabaseReference fbChild2 = myRef.child("DPT Booster-2/Given On");
                        fbChild2.setValue(today);

                        spinner16.setVisibility(View.INVISIBLE);
                    }else{
                        if(cb16.isEnabled()==true){
                        DatabaseReference fbChild1 = myRef.child("DPT Booster-2/Alert/Date");
                        fbChild1.setValue(today);
                        DatabaseReference fbChild2 = myRef.child("DPT Booster-2/Alert/Reason");
                        fbChild2.setValue(spinner16.getSelectedItem().toString());
                    }}
                }
                else if(months>84)
                {
                    DatabaseReference fbChild1 = myRef.child("Status");
                    fbChild1.setValue("Completed Immunization Duration");
                    DatabaseReference fbnext1 = myRef.child("Next");
                    fbnext1.setValue("");
                }
                Toast.makeText(TestActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                Intent rIntent = new Intent(TestActivity.this, MainActivity.class);
                rIntent.putExtra("phc", phc);
                rIntent.putExtra("anm", anm);
                rIntent.putExtra("area", area);
                startActivity(rIntent);
            }
        });
    }

    private String getAge(int year, int month, int day){

        Calendar today = Calendar.getInstance();
        int y = today.get(Calendar.YEAR);
        int m = today.get(Calendar.MONTH);
        int d = today.get(Calendar.DATE);
        int months = (((y-year)*12) + (m-month));
        int n=(d-day);
        if(d<day)
        {
            n=31-day+d;
        }
        if (months<0)
            months*=-1;
        months-=1;
        return months+"."+n;
    }

    private String getAges(int year, int month, int day){

        Calendar today = Calendar.getInstance();
        int y = today.get(Calendar.YEAR);
        int m = today.get(Calendar.MONTH);
        int d = today.get(Calendar.DATE);
        int months = (((y-year)*12) + (m-month));
        int n=(d-day);
        if(d<day)
        {
            n=31-day+d;
        }
        if (months<0)
            months*=-1;
        months-=1;
        return months+" months "+n+" days";
    }

    private boolean disable(){
    spinner1.setEnabled(false);
    spinner1.setClickable(false);

    spinner2.setEnabled(false);
    spinner2.setClickable(false);

    spinner3.setEnabled(false);
    spinner3.setClickable(false);

    spinner4.setEnabled(false);
    spinner4.setClickable(false);

    spinner5.setEnabled(false);
    spinner5.setClickable(false);

    spinner6.setEnabled(false);
    spinner6.setClickable(false);

    spinner7.setEnabled(false);
    spinner7.setClickable(false);

    spinner8.setEnabled(false);
    spinner8.setClickable(false);

    spinner9.setEnabled(false);
    spinner9.setClickable(false);

    spinner10.setEnabled(false);
    spinner10.setClickable(false);

    spinner11.setEnabled(false);
    spinner11.setClickable(false);

    spinner12.setEnabled(false);
    spinner12.setClickable(false);

    spinner13.setEnabled(false);
    spinner13.setClickable(false);

    spinner14.setEnabled(false);
    spinner14.setClickable(false);

    spinner15.setEnabled(false);
    spinner15.setClickable(false);

    spinner16.setEnabled(false);
    spinner16.setClickable(false);

        cb1.setEnabled(false);
        cb1.setClickable(false);

        cb2.setEnabled(false);
        cb2.setClickable(false);

        cb3.setEnabled(false);
        cb3.setClickable(false);

        cb4.setEnabled(false);
        cb4.setClickable(false);

        cb5.setEnabled(false);
        cb5.setClickable(false);

        cb6.setEnabled(false);
        cb6.setClickable(false);

        cb7.setEnabled(false);
        cb7.setClickable(false);

        cb8.setEnabled(false);
        cb8.setClickable(false);

        cb9.setEnabled(false);
        cb9.setClickable(false);

        cb10.setEnabled(false);
        cb10.setClickable(false);

        cb11.setEnabled(false);
        cb11.setClickable(false);

        cb12.setEnabled(false);
        cb12.setClickable(false);

        cb13.setEnabled(false);
        cb13.setClickable(false);

        cb14.setEnabled(false);
        cb14.setClickable(false);

        cb15.setEnabled(false);
        cb15.setClickable(false);

        cb16.setEnabled(false);
        cb16.setClickable(false);

        return true;
    }
}
