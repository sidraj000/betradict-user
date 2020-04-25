package com.vinciis.beTraDict;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements PaymentResultListener {

  EditText etAmt;
    Button signout,wd_money,add_money_btn;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    protected int add_amount=1;
    Integer amt;
    FirebaseUser mUser;
    EditText etUpi;
    Button btnSubmit;
    Integer toastcheck=0;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth= FirebaseAuth.getInstance();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        etAmt=findViewById(R.id.addAmt);
        add_money_btn=(Button)findViewById(R.id.addmoney_to_account);
        etUpi=findViewById(R.id.etUpi);
        wd_money=(Button)findViewById(R.id.withdraw_request);
        btnSubmit=findViewById(R.id.btnSubmitWd);
        btnSubmit.setVisibility(View.GONE);
        wd_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                wd_money.setVisibility(View.GONE);
                add_money_btn.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                etUpi.setVisibility(View.VISIBLE);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!etUpi.getText().toString().isEmpty())
                        {
                          updateWallet(Integer.parseInt(etAmt.getText().toString()),true);

                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "Pls enter your upi id", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





            }
        });

        add_money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment(v);
            }
        });


    }

    private void startPayment(View v) {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher_round);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", "Merchant Name");

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Order #123456");

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            amt=Integer.parseInt(etAmt.getText().toString());
            options.put("amount", amt*100);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("ProfileActivity", "Error in starting Razorpay Checkout", e);
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.moveTaskToBack(true);
    }

    @Override
    public void onPaymentSuccess(String s) {

        final GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("user").child(account.getId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
               updateWallet(amt,false);
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(ProfileActivity.this,"Payment Failed  check Account balance!",Toast.LENGTH_SHORT).show();
    }
    public void updateWallet(final Integer enterAmt,final boolean b)
    {
        toastcheck=0;
        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("wallet").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Wallet wallet=mutableData.getValue(Wallet.class);

                if(wallet==null)
                {
                    return (Transaction.success(mutableData));

                }
                else {
                    if(b)
                    {
                        if(wallet.balance>(150+enterAmt))
                        {
                            wallet.balance=wallet.balance-enterAmt;
                            updatenewmsg(mUser.getUid());
                            Calendar cal = Calendar.getInstance();
                            Date currentDate = cal.getTime();
                            String key=FirebaseDatabase.getInstance().getReference().child("msgs").child(mUser.getUid()).push().getKey();
                            FirebaseDatabase.getInstance().getReference().child("msgs").child(mUser.getUid()).child(key).setValue(new Msgs("withdrawl request of amt" +
                                    etAmt.getText().toString()+" to upi id "+etUpi.getText().toString(),0,currentDate));
                            String key2=FirebaseDatabase.getInstance().getReference().child("msgs").child(mUser.getUid()).push().getKey();
                            FirebaseDatabase.getInstance().getReference().child("msgs").child(mUser.getUid()).child(key2).setValue(new Msgs("We are processing your request,it takes upto 24 hrs",1,currentDate));
                            toastcheck=0;
                            mutableData.setValue(wallet);
                            mutableData.setValue(wallet);
                            return (Transaction.success(mutableData));

                        }

                        else
                        {
                            toastcheck++;
                            return (Transaction.success(mutableData));
                        }
                    }
                    else {
                        wallet.balance = wallet.balance + enterAmt;
                        Calendar cal = Calendar.getInstance();
                        Date currentDate = cal.getTime();
                        wallet.lastTransactions.add(new transactions(enterAmt, "credit", "credit", "credit", currentDate));
                        startActivity(new Intent(ProfileActivity.this, wallet_trans.class));
                        mutableData.setValue(wallet);
                        return (Transaction.success(mutableData));
                    }
                }
                }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if(toastcheck!=0)
                {
                    Toast.makeText(ProfileActivity.this, "Not enough balance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updatenewmsg(final String uId) {
        final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("support").child(uId);
        mref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                newMsg m=mutableData.getValue(newMsg.class);

                if(m==null)
                {
                    newMsg n=new newMsg(1,uId,mUser.getDisplayName());
                    mref.setValue(n);
                    return (Transaction.success(mutableData));
                }
                else {
                    m.num=m.num+1;
                    mutableData.setValue(m);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
}
