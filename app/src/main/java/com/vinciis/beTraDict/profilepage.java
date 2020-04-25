package com.vinciis.beTraDict;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class profilepage extends AppCompatActivity {
    EditText etUserName,etPhone,etReferral;
    TextView etDob;
    Button btnSubmit;
    FirebaseUser mUser;
    Integer count=0;
    Referral referrals;
    Date dob;
    List<String> refList=new ArrayList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        etUserName=findViewById(R.id.etUserName);
        etDob=findViewById(R.id.etDob);
        etPhone=findViewById(R.id.etNumber);
        etReferral=findViewById(R.id.etReferral);
        btnSubmit=findViewById(R.id.btnCreateProfile);

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        profilepage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        //   Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                        String date = month + "/" + day + "/" + year;
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); // Make sure user insert date into edittext in this format.
                        etDob.setText(date);
                        Date dateObject;

                        try{

                            dob = formatter.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etUserName.getText().toString().isEmpty()&&!etPhone.getText().toString().isEmpty()&&dob!=null) {


                    FirebaseDatabase.getInstance().getReference().child("referrals").child(etReferral.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                referrals = dataSnapshot.getValue(Referral.class);
                                updatewallet(mUser.getUid(), referrals.money);
                                updatewallet(referrals.uid, (int)(referrals.money*0.75));
                                FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("per").child("dob").setValue(dob);
                                FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("per").child("gameName").setValue(etUserName.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("per").child("phoneNumber").setValue(etPhone.getText().toString());
                                startActivity(new Intent(profilepage.this, trans_activity.class));
                            }
                            else
                            {
                                FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("per").child("dob").setValue(dob);
                                FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("per").child("gameName").setValue(etUserName.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("per").child("phoneNumber").setValue(etPhone.getText().toString());
                                startActivity(new Intent(profilepage.this, trans_activity.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                else
                {
                    Toast.makeText(profilepage.this, "Please Fill All the details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void updatewallet(String uid, final Integer amt)
    {
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet");
        mRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Wallet wallet=mutableData.getValue(Wallet.class);

                if(wallet==null)
                {
                    return (Transaction.success(mutableData));
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();
                    wallet.balance=wallet.balance+50;
                    wallet.lastTransactions.add(new transactions(amt,"ref","ref","ref",currentDate));
                    mutableData.setValue(wallet);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
            }
        });
    }



}
