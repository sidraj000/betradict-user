package com.vinciis.beTraDict;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class viipsfragment extends Fragment {
    Button btnRedeem,btnBuyCards,btnSubmit;
    EditText etNoc;
    FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
    TextView tvCardInfo,tvRac,tvViips;
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    public viipsfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_viipsfragment, container, false);
        btnRedeem=view.findViewById(R.id.btnRedeem);
        btnBuyCards=view.findViewById(R.id.btnBuyCards);
        btnSubmit=view.findViewById(R.id.btnCardsSubmit);
        etNoc=view.findViewById(R.id.etNumofCards);
        tvCardInfo=view.findViewById(R.id.tvCardinfo);
        tvRac=view.findViewById(R.id.tvRunawayCardBalance);
        tvViips=view.findViewById(R.id.tvViipsBalance);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Wallet w=dataSnapshot.getValue(Wallet.class);
             tvRac.setText(w.cashoutCards.toString());
             tvViips.setText(Integer.toString((int)w.trollars));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnBuyCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNoc.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                tvCardInfo.setVisibility(View.VISIBLE);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etNoc.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Please Enter Number of Cards", Toast.LENGTH_SHORT).show();
                        }
                       int i= Integer.parseInt(etNoc.getText().toString());
                        updatetrollars(i);
                    }
                });
            }
        });
        return view;
    }

    public void updatetrollars(final Integer num)
    {
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("wallet");
        mRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Wallet amt=mutableData.getValue(Wallet.class);

                if(amt==null)
                {

                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    if(amt.trollars>=num*100) {
                        amt.trollars = amt.trollars - num * 100;
                        amt.cashoutCards = amt.cashoutCards + num;
                        mutableData.setValue(amt);
                    }
                    else
                    {
                       tvCardInfo.setText("Not enough Viips");
                    }
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }


}
