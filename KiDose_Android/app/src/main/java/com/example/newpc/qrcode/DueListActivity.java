package com.example.newpc.qrcode;

import android.R.layout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DueListActivity extends AppCompatActivity {
    private ListView lv;
    private TextView tvCount;
    private ArrayList<String> usernames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_list);

        Bundle bundle = getIntent().getExtras();

        final String phc=bundle.getString("phc");
        final String anm=bundle.getString("anm");
        final String area=bundle.getString("area");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother");

        lv=(ListView)findViewById(R.id.listView);
        tvCount=(TextView)findViewById(R.id.tvCount);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, layout.simple_list_item_1,usernames);
        lv.setAdapter(arrayAdapter);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 int num=0;
                 for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()){
                     for (DataSnapshot childEventSnapshot : eventSnapshot.getChildren()) {
                         for(DataSnapshot passChild : childEventSnapshot.getChildren()) {
                             for (DataSnapshot passChild1 : passChild.getChildren()) {
                                 if(passChild1.hasChild("Next")){
                                     try{
                                     String value = passChild1.child("Next").getValue().toString();
                                     String mothername=eventSnapshot.child("Name").getValue().toString();
                                     String childname=passChild.getKey();

                                         DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                         Date val = formatter.parse(value);
                                         Date date = new Date();
                                         String str = formatter.format(date);
                                         Date today = formatter.parse(str);
                                         long diff = val.getTime() - today.getTime();
                                         long count= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                         if(count<3)
                                         {
                                             usernames.add(mothername+" : "+childname);
                                             arrayAdapter.notifyDataSetChanged();
                                             num++;
                                         }
                                     }
                                     catch (NullPointerException e)
                                     {
                                         System.out.print(e);
                                     }
                                     catch (ParseException e)
                                     {
                                         e.printStackTrace();
                                     }
                                }
                             }
                         }
                     }
                 }
                tvCount.setText("Total Due Children: "+num);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
