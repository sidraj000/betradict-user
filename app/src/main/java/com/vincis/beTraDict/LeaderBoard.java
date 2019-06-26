package com.vincis.beTraDict;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoard extends Fragment {

    private RecyclerView mRecycler;
    private LeaderAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Wallet wallet = new Wallet();
    public Bundle b;
    public String arr[];
    public List<Integer> amtlist=new ArrayList<>();
    public List<String> userarr=new ArrayList<>();
    DatabaseReference reference;
    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public List<User> mUser=new ArrayList<>();
    public List<String> mKey=new ArrayList<>();


    public LeaderBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        mRecycler = view.findViewById(R.id.leaderlist);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        b=getArguments();
        arr=b.getStringArray("details");
        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("LeaderBoard").child(arr[1]).child(arr[2]+arr[3]);
        mManager = new LinearLayoutManager(getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        reference=FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new LeaderAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private static class LeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvBal, tvName,tvLastMatch;
        ImageView ivPic;

        public LeaderViewHolder(View itemView) {
            super(itemView);
            tvBal = itemView.findViewById(R.id.tvBal);
            tvName = itemView.findViewById(R.id.tvName);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvLastMatch=itemView.findViewById(R.id.tvBalPerMatch);
        }
    }

    private class LeaderAdapter extends RecyclerView.Adapter<LeaderViewHolder> {

    public Context mContext;
    public  DatabaseReference mDatabaseReference;
    public List<LeaderBAmt> mLeaderBoard=new ArrayList<>();
    public List<String> mKey=new ArrayList<>();

        public LeaderAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;
            Query query=mDatabaseReference.orderByChild("Amt").limitToFirst(100);

           query.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                   LeaderBAmt user=dataSnapshot.getValue(LeaderBAmt.class);
                   mLeaderBoard.add(user);
                   notifyItemInserted(mLeaderBoard.size()-1);
               }

               @Override
               public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        public LeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = null;

                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.item_leaderboard, viewGroup, false);

                return new LeaderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final LeaderViewHolder leaderViewHolder, int i) {
            leaderViewHolder.tvBal.setText(Float.toString(mLeaderBoard.get(i).Amt));
            leaderViewHolder.tvName.setText(mLeaderBoard.get(i).username);
            new DownLoadImageTask(leaderViewHolder.ivPic).execute(mLeaderBoard.get(i).picid);


        }

        @Override
        public int getItemCount() {
            return mLeaderBoard.size();
        }
    }

    private static class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlOfImage = strings[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }
            catch(IOException e)
            {
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
