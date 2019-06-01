package be.example.petplanet.petplanet.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.Date;

import be.example.petplanet.petplanet.R;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

//Authenticatie code voor gebruiker

public class MainActivity extends AppCompatActivity {

    // Constanten

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    // Firebase - algemeen

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUniqueReference;
    private DatabaseReference mPlanetReference;
    private DatabaseReference mScoreReference;

    // Firebase - authentication

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mUsername;

    // Score
    private int score;

    //Notification
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static final String CHANNEL_4_ID = "channel4";
    public static final String CHANNEL_5_ID = "channel5";
    private NotificationManagerCompat notificationManager;


    Button signout;
    ImageView anim;
    ImageButton planet;
    ImageButton graphs;
    ImageButton qrcode;
    ImageButton leaderbord;
    TextView scoring;
    private Intent MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoring = (TextView) findViewById(R.id.score);
        //signout
        signout = (Button) findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        // go to zonnestelsel
        planet = (ImageButton) findViewById(R.id.planet);
        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, SolarsystemActivity.class);
                startActivity(intent);
            }
        });
        // planet for animation
        anim = findViewById(R.id.animation);
        anim.setImageResource(R.drawable.planet_neutral);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        anim.startAnimation(animation);

        // go to qrcode
        qrcode = (ImageButton) findViewById(R.id.qrcode);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        // go to grafieken
        graphs = (ImageButton) findViewById(R.id.graphs);
        graphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
        // go to grafieken
        leaderbord = (ImageButton) findViewById(R.id.leader);
        leaderbord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        //time notification
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.format(date);
        try {
            if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse("18:39"))){
                addNotification();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Firebase - initializeren van algemene variabelen.

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Firebase - initializeren van authenticatie variabelen.

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Firebase - initializeren van referentie.

        mUniqueReference = mFirebaseDatabase.getReference();

        //Notification
        notificationManager = NotificationManagerCompat.from(this);

        //get score
        mPlanetReference = mFirebaseDatabase.getReference().child("planet");
        mScoreReference = mPlanetReference.child("0").child("score");




        // Inloggen
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // this firebaseAuth contains whether at that moment, the user is authenticated or not
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // inloggen
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // uitloggen
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.AppTheme)
                                    .setLogo(R.drawable.logo)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            //new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        mScoreReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String score = dataSnapshot.getValue(String.class);
                Log.i("score", String.valueOf(score));
                scoring.setText("Score= "+ score);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createNotificationChannel();

        //Test tot optimalisatie homescreen
    }




    // Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Een methode om na te gaan of de activiteit al bestond met de bijbehorende parameters.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    //Activity LifyCycle

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    //In- en uitloggen

    private void onSignedInInitialize(String username) {
        mUsername = username;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }

    // Notificatie
    private void addNotification() {

        /*Pas volgende code aan volgens de score die bepaald is bij user story 3!!!*/


        //Planeet = slechter
        if (score <= 0) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Your planet is dying.")
                .setContentText("Oh no! Your planet score is getting low.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();


            anim.setImageResource(R.drawable.planet_bad_state_2);
            notificationManager.notify(1, notification);
        }
        //Planeet slecht
        else if (score > 20 & score <= 40) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Your planet can be better")
                    .setContentText("Dan't forget your planet should be getting better not worse.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(2, notification);
        }
        //Planeet = ok
        else if (score > 40 & score <= 60) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Everything is going okay.")
                .setContentText("Nothing went wrong nor good but don't forget to take care.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

            notificationManager.notify(3, notification);
        }
        //Planeet = goed
        else if (score > 60 & score <= 80) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Everything is going great.")
                    .setContentText("Be sure you stay on your good course.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(4, notification);
        }
        //Planeet = best
        else {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Your planet is full of hapiness.")
                .setContentText("Be sure you stay on your good course.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

            notificationManager.notify(5, notification);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2");

            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("This is Channel 4");
            NotificationChannel channel4 = new NotificationChannel(
                    CHANNEL_4_ID,
                    "Channel 4",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel4.setDescription("This is Channel 4");
            NotificationChannel channel5 = new NotificationChannel(
                    CHANNEL_5_ID,
                    "Channel 5",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel5.setDescription("This is Channel 5");
 
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
            manager.createNotificationChannel(channel4);
            manager.createNotificationChannel(channel5);
        }
    }
}
