package com.vinciis.beTraDict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AskUPI extends Fragment {

    EditText Upi,confirmUPI;
    Button withdraw,backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_ask_upi, container, false);

        Upi=v.findViewById(R.id.upi);
        confirmUPI=v.findViewById(R.id.confirm_upi);
        backButton=v.findViewById(R.id.back_button);
        withdraw=v.findViewById(R.id.withdraw_balance);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upi=Upi.getText().toString();
                String confirm_upi=confirmUPI.getText().toString();
                if(upi.isEmpty())
                {
                    Toast.makeText(getActivity(),"Enter Correct UPI address",Toast.LENGTH_SHORT).show();
                }
                else if(!upi.isEmpty() && upi.equals(confirm_upi)){

                    Toast.makeText(getActivity(),"Your amount will be credited within 2-3 Working days",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"Upi Doesn't Match",Toast.LENGTH_SHORT).show();


                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return v;
    }


}
