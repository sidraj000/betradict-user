package com.vinciis.beTraDict;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class frag_levels extends Fragment {

    private RecyclerView mRecycler;
    private AnsweredAdapter mAdapter;
    Bundle b;
    String arr[];

    public frag_levels() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_levels, container, false);
        b = getArguments();
        mRecycler=view.findViewById(R.id.levelList);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        arr = b.getStringArray("details");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new AnsweredAdapter(getContext());
        mRecycler.setAdapter(mAdapter);

        // ivAnime.setVisibility(View.VISIBLE);
        //ivAnime.startAnimation(animation);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class AnsweredViewHolder extends RecyclerView.ViewHolder {

        TextView tvType;
        Button btnEnter;

        public AnsweredViewHolder(View itemView) {
            super(itemView);
            tvType=itemView.findViewById(R.id.tvType);
            btnEnter=itemView.findViewById(R.id.btnEnter);
        }
    }


    private class AnsweredAdapter extends RecyclerView.Adapter<AnsweredViewHolder> {


        private Context mContext;

        public List<String> mType = new ArrayList<>();



        public AnsweredAdapter(final Context context) {
            mContext = context;
            mType.add("Rookie");
            mType.add("Pro");
            mType.add("Expert");
        }

        @NonNull
        @Override
        public AnsweredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_levels, viewGroup, false);

            return new AnsweredViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AnsweredViewHolder friendViewHolder, final int i) {
            friendViewHolder.tvType.setText(mType.get(i));

            friendViewHolder.btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    String arrr[] = {arr[0],arr[1],mType.get(i),"b"};
                    intent.putExtra("details", arrr);
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {

            return mType.size();
        }

    }
}
