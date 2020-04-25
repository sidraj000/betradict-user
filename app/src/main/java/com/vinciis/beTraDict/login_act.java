package com.vinciis.beTraDict;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class login_act extends AppCompatActivity {

    public int PERMISSIONS_REQUEST = 100;
    private FirebaseAuth mAuth;
    public static final int RC_SIGN_IN=1;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    DatabaseReference mDatabase;
    public int flag;
    public  String uId;
    public List<AllQuest> mAllQuest=new ArrayList<>();
    public List<String> mQid=new ArrayList<>();
    public List<User> mUser=new ArrayList<>();
    public List<String> cric=new ArrayList<>();
    public List<List<Quest>> mQuest=new ArrayList<>();
    public per_det det=null;
    public ChildEventListener mChildEventListener;
    FirebaseUser currentUser;
    public User u=null;
    TextView tnc;
    String versionName = "";
    int versionCode = -1;
    String currVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);
        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        tnc=findViewById(R.id.tnc);
        tnc.setMovementMethod(LinkMovementMethod.getInstance());

        try {

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


            }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {

         //   String token= FirebaseInstanceId.getInstance().getToken();
         //   DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Tokens");
         //   Token tok=new Token(token);
         //   ref.child(currentUser.getUid()).setValue(tok);
            FirebaseDatabase.getInstance().getReference().child("versionName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currVersion=dataSnapshot.child("code").getValue().toString();

                    if(currVersion.equals(versionName)) {
                        startActivity(new Intent(login_act.this, trans_activity.class));
                    }
                    else {
                        startActivity(new Intent(login_act.this,checkLatestVersion.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else
        {
            FirebaseDatabase.getInstance().getReference().child("versionName").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currVersion=dataSnapshot.child("code").getValue().toString();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            signInButton.setVisibility(View.VISIBLE);
            mGoogleSignInClient.signOut();
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("match").child("cricket");
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Match match=dataSnapshot.getValue(Match.class);
                    if(match.status==0) {
                        cric.add(match.id);
                    }

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
            mDatabase.child("quest").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    // A new comment has been added, add it to the displayed list
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String key=dataSnapshot.getKey();
                    if(allQuest==null && allQuest.quest_wall.ans.equals("U")) {
                        // [START_EXCLUDE]
                        // Update RecyclerView
                        mAllQuest.add(allQuest);
                        mQid.add(key);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String userKey=dataSnapshot.getKey();
                    if(allQuest==null && allQuest.quest_wall.ans.equals("U")) {

                        // [START_EXCLUDE]
                        int userIndex = mQid.indexOf(userKey);
                        if (userIndex > -1) {
                            // Replace with the new data
                            mAllQuest.set(userIndex, allQuest);

                            // Update the RecyclerView
                        } else {
                            // Log.w(TAG, "onChildChanged:unknown_child:" + userKey);
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String userKey = dataSnapshot.getKey();
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);

                    if(allQuest==null && allQuest.quest_wall.ans.equals("U")) {
                        // [START_EXCLUDE]
                        int userIndex = mQid.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQid.remove(userIndex);
                            mAllQuest.remove(userIndex);

                            // Update the RecyclerView
                        } else {
                            // Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mDatabase.child("users").addChildEventListener(
                    new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            User usr=dataSnapshot.getValue(User.class);
                            mUser.add(usr);
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



    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //  Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in User's information
                            // Log.d(TAG, "signInWithCredential:success");
                           currentUser = mAuth.getCurrentUser();
                            uId=currentUser.getUid();

                            updateUI(currentUser);



                        } else {
                            // If sign in fails, display a message to the User.
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            currentUser=null;
                            updateUI(currentUser);

                        }



                        // ...
                    }
                });

    }



    private void updateUI(final FirebaseUser user) {

        int a=0;
        for(int p=0;p<mUser.size();p++)
        {
            if(mUser.get(p).per.uid.equals(uId))
            {
               a++;
               break;
            }
        }

        if(user!=null&&a==0)
     {
         writeNewUser(user.getUid(),user.getDisplayName(),user.getEmail(),user.getPhotoUrl().toString());

         if(currVersion.equals(versionName)) {
             startActivity(new Intent(login_act.this, profilepage.class));
         }
         else {
             startActivity(new Intent(login_act.this,checkLatestVersion.class));
         }
     }
     else if(user!=null)
        {
            //String token= FirebaseInstanceId.getInstance().getToken();
            //DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Tokens");
           // Token tok=new Token(token);
          //  ref.child(currentUser.getUid()).setValue(tok);
            if(currVersion.equals(versionName)) {
                startActivity(new Intent(login_act.this, trans_activity.class));
            }
            else {
                startActivity(new Intent(login_act.this,checkLatestVersion.class));
            }

        }
        else
        {
            mGoogleSignInClient.signOut();
        }

    }
    private void writeNewUser(String userId, String name, String email,String pid) {
        per_det details=new per_det(name,email,pid,userId,userId.substring(0,6));
        List<transactions> li=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        li.add(new transactions(0,"u","u","u",currentDate));
        Wallet wall=new Wallet(0,100,5,li);
        User usr = new User(details,wall);
        mDatabase.child("users").child(userId).setValue(usr);
        String token= FirebaseInstanceId.getInstance().getToken();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Tokens");
        FirebaseDatabase.getInstance().getReference().child("referrals").child(userId.substring(0,6)).setValue(new Referral(userId.substring(0,6),userId,50));
        Token tok=new Token(token);
        ref.child(currentUser.getUid()).setValue(tok);
    }


}



