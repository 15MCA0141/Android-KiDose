package com.example.newpc.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MotherActivity extends AppCompatActivity {
    public SearchableSpinner spinnerM;
    RecyclerView motherList;
    List<MotherDTO> motherDTOList;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    private List<String> qr=new ArrayList<String>();
    private List<Long> noc=new ArrayList<Long>();

    RecyclerView.Adapter recyclerViewadapter;
    private int i=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother);

        spinnerM= (SearchableSpinner) findViewById(R.id.spinnerM);
        spinnerM.setTitle("Select Mother");
        spinnerM.setPositiveButton("OK");
        motherDTOList=new ArrayList<>();
        motherList=(RecyclerView)findViewById(R.id.recyclerListMothers);

        motherList.setHasFixedSize(true);
        recyclerViewlayoutManager=new LinearLayoutManager(this);
        motherList.setLayoutManager(recyclerViewlayoutManager);
        getData();
    }
    private void getData(){
        Bundle bundle = getIntent().getExtras();
        final String phc=bundle.getString("phc");
        final String anm=bundle.getString("anm");
        final String area=bundle.getString("area");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()){
                    MotherDTO cadapter=new MotherDTO();
                    try{
                    long child=eventSnapshot.child("Child").getChildrenCount();
                    String mother=eventSnapshot.child("Name").getValue().toString();
                    String father=eventSnapshot.child("Father").getValue().toString();
                    String mobile=eventSnapshot.child("Mobile").getValue().toString();
                    String aadhar=eventSnapshot.child("Aadhar").getValue().toString();
                    qr.add(eventSnapshot.child("PIN").getValue().toString());
                    noc.add(eventSnapshot.child("Child").getChildrenCount());

                    cadapter.setMotherName(i+". "+mother);
                    cadapter.setFatherName(father);
                    cadapter.setAadhar(aadhar);
                    cadapter.setMobile(mobile);
                    cadapter.setChildren(String.valueOf(child));
                    motherDTOList.add(cadapter);
                    i++;
                    }catch (NullPointerException e){}
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerViewadapter=new MotherAdapter(this,motherDTOList);
        motherList.setAdapter(recyclerViewadapter);

        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = getIntent().getExtras();
                final String phc=bundle.getString("phc");
                final String anm=bundle.getString("anm");
                final String area=bundle.getString("area");
                final String mother = spinnerM.getSelectedItem().toString();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbr = database.getReference("PHC/" + phc + "/ANM/" + anm + "/Areas/" + area + "/Mother");
                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            try {
                                String name = ds.child("Name").getValue().toString();
                                String father = ds.child("Father").getValue().toString();
                                String data =name + " (" + father + ")";
                                long noc=ds.child("Child").getChildrenCount();
                                if(!(mother.equals("Select Receipent"))){
                                    if (data.equals(mother)) {
                                        String qr = ds.getKey().toString();
                                        Intent rIntent = new Intent(MotherActivity.this, ViewPagerItemFragment.class);
                                        rIntent.putExtra("phc", phc);
                                        rIntent.putExtra("anm", anm);
                                        rIntent.putExtra("area", area);
                                        rIntent.putExtra("qr", qr);
                                        rIntent.putExtra("noc", noc);
                                        startActivity(rIntent);
                                    }
                                }
                            }
                            catch(NullPointerException e){
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

        motherList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, motherList ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent rIntent = new Intent(MotherActivity.this, ChildActivity.class);
                        rIntent.putExtra("phc", phc);
                        rIntent.putExtra("anm", anm);
                        rIntent.putExtra("area", area);
                        rIntent.putExtra("qr", qr.get(position));//qr code
                        rIntent.putExtra("noc", noc.get(position));//no. of child
                        startActivity(rIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {

                        Toast.makeText(MotherActivity.this,qr.get(position)+"", Toast.LENGTH_SHORT).show();
                    }
                })
        );


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> motherNames = new ArrayList<String>();
                motherNames.add("Select Receipent");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try{
                        String mother=ds.child("Name").getValue().toString();
                        String father=ds.child("Father").getValue().toString();
                        motherNames.add(mother+" ("+father+")");
                    }
                    catch(NullPointerException e){
                        System.out.print(e);
                    }
                }
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(MotherActivity.this, android.R.layout.simple_spinner_item, motherNames);
                nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerM.setAdapter(nameAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}