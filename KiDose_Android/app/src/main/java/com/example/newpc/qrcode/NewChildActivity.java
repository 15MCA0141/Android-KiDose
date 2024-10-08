package com.example.newpc.qrcode;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ku$haL on 05-05-2017.
 */
public class NewChildActivity extends AppCompatActivity {

    Button btnSub;
    ImageView btnCal;
    Spinner spin_gender;
    EditText editText5;
    DatePicker datePicker;
    int year_b,month_b,day_b;
    static final int DIALOG_ID=0;
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childreg);

        final Calendar cal=Calendar.getInstance();
        year_b=cal.get(Calendar.YEAR);
        month_b=cal.get(Calendar.MONTH);
        day_b=cal.get(Calendar.DATE);

        btnSub = (Button) findViewById(R.id.btnSub);
        spin_gender=(Spinner)findViewById(R.id.spin_gender);
        showDialogOnCal();
        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("pin");
        final String phc=bundle.getString("phc");
        final String anm=bundle.getString("anm");
        final String area=bundle.getString("area");
        final String pin=bundle.getString("pin");

        //Toast.makeText(NewChildActivity.this,""+message,Toast.LENGTH_LONG).show();

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText4 = (EditText) findViewById(R.id.editText4);
                EditText editText5 = (EditText) findViewById(R.id.editText5);
                spin_gender = (Spinner) findViewById(R.id.spin_gender);

                String name = editText4.getText().toString();
                String birth = editText5.getText().toString();
                String gender = spin_gender.getSelectedItem().toString();
                if (name.matches(""))
                {
                    Toast.makeText(NewChildActivity.this, "You did not enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(birth.matches(""))
                {
                   Toast.makeText(NewChildActivity.this, "You did not enter the birth date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(gender.matches(""))
                {
                    Toast.makeText(NewChildActivity.this, "You did not enter the gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("PHC/"+phc+"/ANM/"+anm+"/Areas/"+area+"/Mother/"+message+"/Child/"+name);
                    DatabaseReference fbChild1 = myRef.child("Name");
                    fbChild1.setValue(name);
                    DatabaseReference fbChild2 = myRef.child("DOB");
                    fbChild2.setValue(birth);
                    DatabaseReference fbChild3 = myRef.child("Gender");
                    fbChild3.setValue(gender);
                    Toast.makeText(NewChildActivity.this, "Child Registered Successfully!", Toast.LENGTH_SHORT).show();
                    Intent rIntent = new Intent(NewChildActivity.this, DataActivity.class);
                    rIntent.putExtra("phc", phc);
                    rIntent.putExtra("anm", anm);
                    rIntent.putExtra("area", area);
                    rIntent.putExtra("pin", pin);
                    rIntent.putExtra("name", name);
                    startActivity(rIntent);
                    rIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
        });
    }
    public void showDialogOnCal(){
        btnCal=(ImageView) findViewById(R.id.btnCal);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showDialog(DIALOG_ID);
            }

        });
    }
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID)
            return new DatePickerDialog(this,dpListener,year_b,month_b,day_b);
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            EditText editText5 = (EditText) findViewById(R.id.editText5);
            year_b=y;
            month_b=m+1;
            day_b=d;

            editText5.setText(+day_b+"/"+month_b+"/"+year_b);

        }
    };
 }
