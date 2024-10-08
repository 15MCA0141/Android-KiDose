package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildActivity extends AppCompatActivity {
    private int status = 0;
    private Button btnEdit, btnVac;
    private TextView tvName, tvMother, tvFather, tvDOB, tvMobile, tvGender, tvHepB0, tvOPV0, tvBCG, tvPenta1, tvPenta2, tvPenta3, tvOPV1, tvOPV2, tvOPV3, tvIPV1, tvIPV2, tvMeasles1, tvMeasles2, tvDPTBooster1, tvDPTBooster2, tvOPVBooster;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        final Bundle bundle = getIntent().getExtras();
        final String Child = bundle.getString("noc");
//        final int noc = Integer.parseInt(Child);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final String anm = bundle.getString("anm");
        final String phc = bundle.getString("phc");
        final String area = bundle.getString("area");
        final String qr = bundle.getString("qr");

        btnEdit = (Button) findViewById(R.id.btnEdit);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvName = (EditText) findViewById(R.id.tvName);
        tvMother = (TextView) findViewById(R.id.tvMother);
        tvFather = (TextView) findViewById(R.id.tvFather);
        tvDOB = (TextView) findViewById(R.id.tvDOB);
        tvMobile = (EditText) findViewById(R.id.tvMobile);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvHepB0 = (TextView) findViewById(R.id.tvHepB0);
        tvOPV0 = (TextView) findViewById(R.id.tvOPV0);
        tvBCG = (TextView) findViewById(R.id.tvBCG);
        tvPenta1 = (TextView) findViewById(R.id.tvPenta1);
        tvPenta2 = (TextView) findViewById(R.id.tvPenta2);
        tvPenta3 = (TextView) findViewById(R.id.tvPenta3);
        tvOPV1 = (TextView) findViewById(R.id.tvOPV1);
        tvOPV2 = (TextView) findViewById(R.id.tvOPV2);
        tvOPV3 = (TextView) findViewById(R.id.tvOPV3);
        tvIPV1 = (TextView) findViewById(R.id.tvIPV1);
        tvIPV2 = (TextView) findViewById(R.id.tvIPV2);
        tvMeasles1 = (TextView) findViewById(R.id.tvMeasles1);
        tvMeasles2 = (TextView) findViewById(R.id.tvMeasles2);
        tvOPVBooster = (TextView) findViewById(R.id.tvOPVBooster);
        tvDPTBooster1 = (TextView) findViewById(R.id.tvDPTBooster1);
        tvDPTBooster2 = (TextView) findViewById(R.id.tvDPTBooster2);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String mother = dataSnapshot.child("Name").getValue().toString();
                String father = dataSnapshot.child("Father").getValue().toString();
                String mobile = dataSnapshot.child("Mobile").getValue().toString();

                tvMother.setText(mother);
                tvFather.setText(father);
                tvMobile.setText(mobile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference myChild = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr + "/Child");

        myChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> childNames = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    childNames.add(ds.getKey().toString());
                }
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(ChildActivity.this, android.R.layout.simple_spinner_item, childNames);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(nameAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //  try {


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String selChild = spinner.getSelectedItem().toString();
                final DatabaseReference myRef1 = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr + "/Child/" + selChild);

                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String name = spinner.getSelectedItem().toString();
                        String bd = dataSnapshot.child("DOB").getValue().toString();
                        String gender = dataSnapshot.child("Gender").getValue().toString();

                        tvName.setText(name);
                        tvDOB.setText(bd);
                        tvGender.setText(gender);

                        final DatabaseReference myRef2 = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr + "/Child/" + selChild + "/Vaccines");
                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String HepB0 = "", OPV0 = "", BCG = "", Penta1 = "", Penta2 = "", Penta3 = "", OPV1 = "", OPV2 = "", OPV3 = "", IPV1 = "", IPV2 = "", Measles1 = "", Measles2 = "", DPTBooster1 = "", DPTBooster2 = "", OPVBooster = "";

                                if (dataSnapshot.hasChild("HepB-0")) {
                                    if (dataSnapshot.child("HepB-0").hasChild("Given On")) {
                                        HepB0 = dataSnapshot.child("HepB-0").child("Given On").getValue().toString();
                                        tvHepB0.setText(HepB0);
                                    }
                                }
                                if (dataSnapshot.hasChild("OPV-0")) {
                                    if (dataSnapshot.child("OPV-0").hasChild("Given On")) {
                                        OPV0 = dataSnapshot.child("OPV-0").child("Given On").getValue().toString();
                                        tvOPV0.setText(OPV0);
                                    }
                                }
                                if (dataSnapshot.hasChild("BCG")) {
                                    if (dataSnapshot.child("BCG").hasChild("Given On")) {
                                        BCG = dataSnapshot.child("BCG").child("Given On").getValue().toString();
                                        tvBCG.setText(BCG);
                                    }
                                }
                                if (dataSnapshot.hasChild("Penta-1")) {
                                    if (dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                        Penta1 = dataSnapshot.child("Penta-1").child("Given On").getValue().toString();
                                        tvPenta1.setText(Penta1);
                                    }
                                }
                                if (dataSnapshot.hasChild("Penta-2")) {
                                    if (dataSnapshot.child("Penta-2").hasChild("Given On")) {
                                        Penta2 = dataSnapshot.child("Penta-2").child("Given On").getValue().toString();
                                        tvPenta2.setText(Penta2);
                                    }
                                }
                                if (dataSnapshot.hasChild("Penta-3")) {
                                    if (dataSnapshot.child("Penta-3").hasChild("Given On")) {
                                        Penta3 = dataSnapshot.child("Penta-3").child("Given On").getValue().toString();
                                        tvPenta3.setText(Penta3);
                                    }
                                }
                                if (dataSnapshot.hasChild("OPV-1")) {
                                    if (dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                        OPV1 = dataSnapshot.child("OPV-1").child("Given On").getValue().toString();
                                        tvOPV1.setText(OPV1);
                                    }
                                }
                                if (dataSnapshot.hasChild("OPV-2")) {
                                    if (dataSnapshot.child("OPV-2").hasChild("Given On")) {
                                        OPV2 = dataSnapshot.child("OPV-2").child("Given On").getValue().toString();
                                        tvOPV2.setText(OPV2);
                                    }
                                }
                                if (dataSnapshot.hasChild("OPV-3")) {
                                    if (dataSnapshot.child("OPV-3").hasChild("Given On")) {
                                        OPV3 = dataSnapshot.child("OPV-3").child("Given On").getValue().toString();
                                        tvOPV3.setText(OPV3);
                                    }
                                }
                                if (dataSnapshot.hasChild("IPV-1")) {
                                    if (dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                        IPV1 = dataSnapshot.child("IPV-1").child("Given On").getValue().toString();
                                        tvIPV1.setText(IPV1);
                                    }
                                }
                                if (dataSnapshot.hasChild("IPV-2")) {
                                    if (dataSnapshot.child("IPV-2").hasChild("Given On")) {
                                        IPV2 = dataSnapshot.child("IPV-2").child("Given On").getValue().toString();
                                        tvIPV2.setText(IPV2);
                                    }
                                }
                                if (dataSnapshot.hasChild("Measles-1")) {
                                    if (dataSnapshot.child("Measles-1").hasChild("Given On")) {
                                        Measles1 = dataSnapshot.child("Measles-1").child("Given On").getValue().toString();
                                        tvMeasles1.setText(Measles1);
                                    }
                                }
                                if (dataSnapshot.hasChild("Measles-2")) {
                                    if (dataSnapshot.child("Measles-2").hasChild("Given On")) {
                                        Measles2 = dataSnapshot.child("Measles-2").child("Given On").getValue().toString();
                                        tvMeasles2.setText(Measles2);
                                    }
                                }
                                if (dataSnapshot.hasChild("OPV Booster")) {
                                    if (dataSnapshot.child("OPV Booster").hasChild("Given On")) {
                                        OPVBooster = dataSnapshot.child("OPV Booster").child("Given On").getValue().toString();
                                        tvOPVBooster.setText(OPVBooster);
                                    }
                                }
                                if (dataSnapshot.hasChild("DPT Booster-1")) {
                                    if (dataSnapshot.child("DPT Booster-1").hasChild("Given On")) {
                                        DPTBooster1 = dataSnapshot.child("DPT Booster-1").child("Given On").getValue().toString();
                                        tvDPTBooster1.setText(DPTBooster1);
                                    }
                                }
                                if (dataSnapshot.hasChild("DPT Booster-2")) {
                                    if (dataSnapshot.child("DPT Booster-2").hasChild("Given On")) {
                                        DPTBooster2 = dataSnapshot.child("DPT Booster-2").child("Given On").getValue().toString();
                                        tvDPTBooster2.setText(DPTBooster2);
                                    }
                                }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String selChild = tvName.getText().toString();
                final DatabaseReference myRef2 = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr + "/Child/" + selChild + "/Vaccines");
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String HepB0 = "", OPV0 = "", BCG = "", Penta1 = "", Penta2 = "", Penta3 = "", OPV1 = "", OPV2 = "", OPV3 = "", IPV1 = "", IPV2 = "", Measles1 = "", Measles2 = "", DPTBooster1 = "", DPTBooster2 = "", OPVBooster = "";
                        empty();
                        if (dataSnapshot.hasChild("HepB-0")) {
                            if (dataSnapshot.child("HepB-0").hasChild("Given On")) {
                                HepB0 = dataSnapshot.child("HepB-0").child("Given On").getValue().toString();
                                tvHepB0.setText(HepB0);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-0")) {
                            if (dataSnapshot.child("OPV-0").hasChild("Given On")) {
                                OPV0 = dataSnapshot.child("OPV-0").child("Given On").getValue().toString();
                                tvOPV0.setText(OPV0);
                            }
                        }
                        if (dataSnapshot.hasChild("BCG")) {
                            if (dataSnapshot.child("BCG").hasChild("Given On")) {
                                BCG = dataSnapshot.child("BCG").child("Given On").getValue().toString();
                                tvBCG.setText(BCG);
                            }
                        }
                        if (dataSnapshot.hasChild("Penta-1")) {
                            if (dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                Penta1 = dataSnapshot.child("Penta-1").child("Given On").getValue().toString();
                                tvPenta1.setText(Penta1);
                            }
                        }
                        if (dataSnapshot.hasChild("Penta-2")) {
                            if (dataSnapshot.child("Penta-2").hasChild("Given On")) {
                                Penta2 = dataSnapshot.child("Penta-2").child("Given On").getValue().toString();
                                tvPenta2.setText(Penta2);
                            }
                        }
                        if (dataSnapshot.hasChild("Penta-3")) {
                            if (dataSnapshot.child("Penta-3").hasChild("Given On")) {
                                Penta3 = dataSnapshot.child("Penta-3").child("Given On").getValue().toString();
                                tvPenta3.setText(Penta3);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-1")) {
                            if (dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                OPV1 = dataSnapshot.child("OPV-1").child("Given On").getValue().toString();
                                tvOPV1.setText(OPV1);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-2")) {
                            if (dataSnapshot.child("OPV-2").hasChild("Given On")) {
                                OPV2 = dataSnapshot.child("OPV-2").child("Given On").getValue().toString();
                                tvOPV2.setText(OPV2);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-3")) {
                            if (dataSnapshot.child("OPV-3").hasChild("Given On")) {
                                OPV3 = dataSnapshot.child("OPV-3").child("Given On").getValue().toString();
                                tvOPV3.setText(OPV3);
                            }
                        }
                        if (dataSnapshot.hasChild("IPV-1")) {
                            if (dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                IPV1 = dataSnapshot.child("IPV-1").child("Given On").getValue().toString();
                                tvIPV1.setText(IPV1);
                            }
                        }
                        if (dataSnapshot.hasChild("IPV-2")) {
                            if (dataSnapshot.child("IPV-2").hasChild("Given On")) {
                                IPV2 = dataSnapshot.child("IPV-2").child("Given On").getValue().toString();
                                tvIPV2.setText(IPV2);
                            }
                        }
                        if (dataSnapshot.hasChild("Measles-1")) {
                            if (dataSnapshot.child("Measles-1").hasChild("Given On")) {
                                Measles1 = dataSnapshot.child("Measles-1").child("Given On").getValue().toString();
                                tvMeasles1.setText(Measles1);
                            }
                        }
                        if (dataSnapshot.hasChild("Measles-2")) {
                            if (dataSnapshot.child("Measles-2").hasChild("Given On")) {
                                Measles2 = dataSnapshot.child("Measles-2").child("Given On").getValue().toString();
                                tvMeasles2.setText(Measles2);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV Booster")) {
                            if (dataSnapshot.child("OPV Booster").hasChild("Given On")) {
                                OPVBooster = dataSnapshot.child("OPV Booster").child("Given On").getValue().toString();
                                tvOPVBooster.setText(OPVBooster);
                            }
                        }
                        if (dataSnapshot.hasChild("DPT Booster-1")) {
                            if (dataSnapshot.child("DPT Booster-1").hasChild("Given On")) {
                                DPTBooster1 = dataSnapshot.child("DPT Booster-1").child("Given On").getValue().toString();
                                tvDPTBooster1.setText(DPTBooster1);
                            }
                        }
                        if (dataSnapshot.hasChild("DPT Booster-2")) {
                            if (dataSnapshot.child("DPT Booster-2").hasChild("Given On")) {
                                DPTBooster2 = dataSnapshot.child("DPT Booster-2").child("Given On").getValue().toString();
                                tvDPTBooster2.setText(DPTBooster2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

                final String selChild = tvName.getText().toString();
                final DatabaseReference myRef2 = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr + "/Child/" + selChild + "/Vaccines");
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String HepB0 = "", OPV0 = "", BCG = "", Penta1 = "", Penta2 = "", Penta3 = "", OPV1 = "", OPV2 = "", OPV3 = "", IPV1 = "", IPV2 = "", Measles1 = "", Measles2 = "", DPTBooster1 = "", DPTBooster2 = "", OPVBooster = "";
                        empty();
                        if (dataSnapshot.hasChild("HepB-0")) {
                            if (dataSnapshot.child("HepB-0").hasChild("Given On")) {
                                HepB0 = dataSnapshot.child("HepB-0").child("Given On").getValue().toString();
                                tvHepB0.setText(HepB0);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-0")) {
                            if (dataSnapshot.child("OPV-0").hasChild("Given On")) {
                                OPV0 = dataSnapshot.child("OPV-0").child("Given On").getValue().toString();
                                tvOPV0.setText(OPV0);
                            }
                        }
                        if (dataSnapshot.hasChild("BCG")) {
                            if (dataSnapshot.child("BCG").hasChild("Given On")) {
                                BCG = dataSnapshot.child("BCG").child("Given On").getValue().toString();
                                tvBCG.setText(BCG);
                            }
                        }
                        if (dataSnapshot.hasChild("Penta-1")) {
                            if (dataSnapshot.child("Penta-1").hasChild("Given On")) {
                                Penta1 = dataSnapshot.child("Penta-1").child("Given On").getValue().toString();
                                tvPenta1.setText(Penta1);
                            }
                        }
                        if (dataSnapshot.hasChild("Penta-2")) {
                            if (dataSnapshot.child("Penta-2").hasChild("Given On")) {
                                Penta2 = dataSnapshot.child("Penta-2").child("Given On").getValue().toString();
                                tvPenta2.setText(Penta2);
                            }
                        }
                        if (dataSnapshot.hasChild("Penta-3")) {
                            if (dataSnapshot.child("Penta-3").hasChild("Given On")) {
                                Penta3 = dataSnapshot.child("Penta-3").child("Given On").getValue().toString();
                                tvPenta3.setText(Penta3);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-1")) {
                            if (dataSnapshot.child("OPV-1").hasChild("Given On")) {
                                OPV1 = dataSnapshot.child("OPV-1").child("Given On").getValue().toString();
                                tvOPV1.setText(OPV1);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-2")) {
                            if (dataSnapshot.child("OPV-2").hasChild("Given On")) {
                                OPV2 = dataSnapshot.child("OPV-2").child("Given On").getValue().toString();
                                tvOPV2.setText(OPV2);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV-3")) {
                            if (dataSnapshot.child("OPV-3").hasChild("Given On")) {
                                OPV3 = dataSnapshot.child("OPV-3").child("Given On").getValue().toString();
                                tvOPV3.setText(OPV3);
                            }
                        }
                        if (dataSnapshot.hasChild("IPV-1")) {
                            if (dataSnapshot.child("IPV-1").hasChild("Given On")) {
                                IPV1 = dataSnapshot.child("IPV-1").child("Given On").getValue().toString();
                                tvIPV1.setText(IPV1);
                            }
                        }
                        if (dataSnapshot.hasChild("IPV-2")) {
                            if (dataSnapshot.child("IPV-2").hasChild("Given On")) {
                                IPV2 = dataSnapshot.child("IPV-2").child("Given On").getValue().toString();
                                tvIPV2.setText(IPV2);
                            }
                        }
                        if (dataSnapshot.hasChild("Measles-1")) {
                            if (dataSnapshot.child("Measles-1").hasChild("Given On")) {
                                Measles1 = dataSnapshot.child("Measles-1").child("Given On").getValue().toString();
                                tvMeasles1.setText(Measles1);
                            }
                        }
                        if (dataSnapshot.hasChild("Measles-2")) {
                            if (dataSnapshot.child("Measles-2").hasChild("Given On")) {
                                Measles2 = dataSnapshot.child("Measles-2").child("Given On").getValue().toString();
                                tvMeasles2.setText(Measles2);
                            }
                        }
                        if (dataSnapshot.hasChild("OPV Booster")) {
                            if (dataSnapshot.child("OPV Booster").hasChild("Given On")) {
                                OPVBooster = dataSnapshot.child("OPV Booster").child("Given On").getValue().toString();
                                tvOPVBooster.setText(OPVBooster);
                            }
                        }
                        if (dataSnapshot.hasChild("DPT Booster-1")) {
                            if (dataSnapshot.child("DPT Booster-1").hasChild("Given On")) {
                                DPTBooster1 = dataSnapshot.child("DPT Booster-1").child("Given On").getValue().toString();
                                tvDPTBooster1.setText(DPTBooster1);
                            }
                        }
                        if (dataSnapshot.hasChild("DPT Booster-2")) {
                            if (dataSnapshot.child("DPT Booster-2").hasChild("Given On")) {
                                DPTBooster2 = dataSnapshot.child("DPT Booster-2").child("Given On").getValue().toString();
                                tvDPTBooster2.setText(DPTBooster2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on button clicked
                if (status == 0) {
                    //have to change the column number
                    tvName.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    tvName.setEnabled(true);
                    tvName.setClickable(true);
                    tvMobile.setEnabled(true);
                    tvMobile.setClickable(true);
                    status = 1;
                    btnEdit.setText("Update");
                }
                //off button clicked
                else if (status == 1) {
                    String name = tvName.getText().toString();
                    String mob = tvMobile.getText().toString();
                    if (!mob.equals("") && !name.equals("")) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother/" + qr);
                        DatabaseReference myRef1 = myRef.child("Mobile");
                        myRef1.setValue(mob);
                        DatabaseReference myRef2 = myRef.child("Name");
                        myRef2.setValue(name);
                        tvName.setVisibility(View.INVISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        tvName.setEnabled(false);
                        tvName.setClickable(false);
                        tvMobile.setEnabled(false);
                        tvMobile.setClickable(false);
                        status = 0;
                        btnEdit.setText("Edit");
                        Toast.makeText(ChildActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        Intent rIntent = new Intent(ChildActivity.this, HomeActivity.class);
                        startActivity(rIntent);
                    } else
                        Toast.makeText(ChildActivity.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean empty() {
        tvHepB0.setText("");
        tvOPV0.setText("");
        tvBCG.setText("");
        tvPenta1.setText("");
        tvPenta2.setText("");
        tvPenta3.setText("");
        tvOPV1.setText("");
        tvOPV2.setText("");
        tvOPV3.setText("");
        tvIPV1.setText("");
        tvIPV2.setText("");
        tvMeasles1.setText("");
        tvMeasles2.setText("");
        tvDPTBooster1.setText("");
        tvDPTBooster2.setText("");
        tvOPVBooster.setText("");
    return true;
    }
}
