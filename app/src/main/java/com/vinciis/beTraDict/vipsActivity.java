package com.vinciis.beTraDict;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class vipsActivity extends AppCompatActivity {
    TextView tvViips;
    Button btnBuy;
    EditText etNumber;
    Wallet wallet;
    FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vips);

        etNumber=findViewById(R.id.etNumberofCards);
        btnBuy=findViewById(R.id.btnBuyRunout);
        tvViips=findViewById(R.id.tvViips);
        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallet=dataSnapshot.getValue(Wallet.class);
                tvViips.setText(Float.toString(wallet.trollars));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num=Integer.parseInt(etNumber.getText().toString());
                if(wallet.trollars>(100*num))
                {
                    updatetrollars(num);
                    Toast.makeText(vipsActivity.this, "RunOuts cards Added!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(vipsActivity.this, "Insufficient Vips", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void updatetrollars(final Integer num)
    {
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("wallet");
        mRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Wallet amt=mutableData.getValue(Wallet.class);

                if(amt==null)
                {

                    Toast.makeText(vipsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    amt.trollars=amt.trollars-num*100;
                    amt.cashoutCards=amt.cashoutCards+num;
                    mutableData.setValue(amt);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }


}
