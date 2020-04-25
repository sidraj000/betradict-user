package com.vinciis.beTraDict;

//rzp_test_Y1tbmrjJySt3F4

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterAmount extends Fragment {
    Button b1;
    EditText amt;
    private  int userbalance;
    private String uid;


    public EnterAmount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_enter_amount, container, false);

        b1=v.findViewById(R.id.button_continue);
        amt=v.findViewById(R.id.ed_amount);
        uid=getArguments().getString("userid");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkB(v);
            }


        });
    return v;
    }

    private void checkB(View v) {

        boolean isDigits = TextUtils.isDigitsOnly(amt.getText().toString());
        String i=amt.getText().toString();
        if(isDigits && !TextUtils.isEmpty(i)) {
            final Integer t = Integer.valueOf(i);
            final Integer flag = 99;
            if(t>10000)
            {
                Toast.makeText(getContext(),"You can withdraw Maximum 10000 at a Time",Toast.LENGTH_LONG).show();
            }
            else {

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        UserProfile up = dataSnapshot.getValue(UserProfile.class);
                        userbalance = up.acTroller;
                        if ((userbalance - t) >= flag) {

                            FragmentManager fm = getFragmentManager();
                            AskUPI askUPI = new AskUPI();
                            amt.setEnabled(false);
                            b1.setVisibility(View.GONE);
                            fm.beginTransaction().replace(R.id.enter_amount, askUPI).commit();

                        } else {
                            Toast.makeText(getActivity(), "Your Account Doesn't Have Enough Money ", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });
            }

        }
        else{
            Toast.makeText(getActivity(),"Enter Valid Amount",Toast.LENGTH_SHORT).show();;
        }

    }

}
