package com.vinciis.beTraDict;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class frag2 extends Fragment {
    ImageView tv;


    public frag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_frag2, container, false);
      tv=view.findViewById(R.id.link);
     final String url="https://www.vinciis.com/stats-gurug";
     tv.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Uri uriUrl = Uri.parse(url);
             Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
             startActivity(launchBrowser);
         }
     });
      return view;
    }

}
