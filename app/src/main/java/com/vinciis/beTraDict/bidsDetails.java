package com.vinciis.beTraDict;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class bidsDetails extends AppCompatActivity {
    public RecyclerView mRecycler;
    public String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    public String[] arr;
    public String id= FirebaseAuth.getInstance().getUid();
    TextView tvHead;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_details);
        arr=getIntent().getStringArrayExtra("details");
        tvHead=findViewById(R.id.tvquesthead);
        mRecycler=findViewById(R.id.bidsrecycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(bidsDetails.this));
        tvHead.setText(arr[7]);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bidsAdapter madapter=new bidsAdapter(bidsDetails.this);
        mRecycler.setAdapter(madapter);
    }

    public class bidsViewHolder extends RecyclerView.ViewHolder{

        TextView tvquest,tvAns,tvRate,tvAmt,tvCans,tvPop;
        ImageView ivStatus;
        public bidsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAns=itemView.findViewById(R.id.aAns);
            tvRate=itemView.findViewById(R.id.aRate);
            tvAmt=itemView.findViewById(R.id.aAmt);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvPop=itemView.findViewById(R.id.aPop);
            tvPop.setVisibility(View.GONE);

        }
    }
    public class bidsAdapter extends RecyclerView.Adapter<bidsViewHolder>
    {
        public List<Trollars> mTrollars=new ArrayList<>();
        public List<String> mId=new ArrayList<>();
        public Context mContext;
        public bidsAdapter(Context context) {
            mContext=context;
            final DatabaseReference mdata= FirebaseDatabase.getInstance().getReference().child("Trollars").child(id).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(arr[4]).child(arr[5]);
            mdata.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Trollars trollars=dataSnapshot.getValue(Trollars.class);

                        mTrollars.add(trollars);
                        mId.add(dataSnapshot.getKey());
                        notifyItemInserted(mTrollars.size()-1);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Trollars trollars=dataSnapshot.getValue(Trollars.class);
                    Integer integer=mId.indexOf(dataSnapshot.getKey());
                    if(integer>-1)
                    {
                        mTrollars.set(integer,trollars);
                    }
                    notifyItemChanged(integer);

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public bidsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater=LayoutInflater.from(bidsDetails.this);
            View view=inflater.inflate(R.layout.answered_questions,viewGroup,false);
            return new bidsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final bidsViewHolder bidsViewHolder, int i) {
            final int k=i;
            bidsViewHolder.tvAns.setText(mTrollars.get(i).opt);
            bidsViewHolder.tvAmt.setText(Float.toString(mTrollars.get(i).amt));
            bidsViewHolder.tvRate.setText(Float.toString(mTrollars.get(i).rate));
            if(mTrollars.get(k).opt.equals("A")||mTrollars.get(k).opt.equals("B")||mTrollars.get(k).opt.equals("C")) {
                bidsViewHolder.ivStatus.setVisibility(View.VISIBLE);

                bidsViewHolder.ivStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(arr[4]).child(arr[5]).child(mTrollars.get(k).opt).child(mId.get(k)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final Usrwal usr = dataSnapshot.getValue(Usrwal.class);
                                if (usr.cashoutstatus == 0) {
                                    bidsViewHolder.tvPop.setText("Trollars to be deposited: " + arr[5]);
                                    bidsViewHolder.tvPop.setVisibility(View.GONE);
                                    FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("wallet").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Wallet wallet = dataSnapshot.getValue(Wallet.class);
                                            if (wallet.cashoutCards > 0) {
                                                databaseReference.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(arr[4]).child(mTrollars.get(k).id).child(mTrollars.get(k).opt).child(mId.get(k)).child("cashoutstatus").setValue(1);

                                                cashout(usr.bid, arr[5], Float.parseFloat(arr[6]), mId.get(k));
                                                //databaseReference.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(arr[4]).child(mTrollars.get(k).id).child(mTrollars.get(k).opt).child(mId.get(k)).child("cashoutstatus").setValue(1);
                                                databaseReference.child("users").child(userid).child("wallet").child("cashoutCards").setValue(wallet.cashoutCards - 1);

                                            } else {
                                                Toast.makeText(bidsDetails.this, "No cashout card available", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "Already done", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
            else {
                bidsViewHolder.ivStatus.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mTrollars.size();
        }
    }

    public void cashout(final float mybid,final String qid,final float r,final String key)
    {

        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("LeaderBoard").child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(id);
        mRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                LeaderBAmt user=mutableData.getValue(LeaderBAmt.class);

                if(user==null)
                {

                   // Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    float money=mybid*r/2;
                    user.Amt=user.Amt+money;
                    DatabaseReference fire=FirebaseDatabase.getInstance().getReference();
                    String pushkey= fire.child("Trollars").child(id).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(arr[4]).child(arr[5]).push().getKey();
                    fire.child("Trollars").child(id).child(arr[0]).child(arr[1]).child(arr[2]).child(arr[3]).child(arr[4]).child(arr[5]).child(pushkey).setValue(new Trollars(qid,money,r/2,"W"));
                 //   Toast.makeText(bidsDetails.this, "Successfully done...", Toast.LENGTH_SHORT).show();
                    mutableData.setValue(user);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

}
