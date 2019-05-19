package be.example.petplanet.petplanet.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import be.example.petplanet.petplanet.R;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

//Authenticatie code voor gebruiker

public class MainActivity extends AppCompatActivity {

    // Constanten

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    // Firebase - algemeen

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUniqueReference;

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
    private NotificationManagerCompat notificationManager;

    ImageButton signout;
    ImageButton planet;
    ImageButton graphs;
    ImageButton qrcode;
    private Intent MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //signout
        signout = (ImageButton) findViewById(R.id.signout);
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

        // Firebase - initializeren van algemene variabelen.

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Firebase - initializeren van authenticatie variabelen.

        mFirebaseAuth = FirebaseAuth.getInstance();

        //Notification
        notificationManager = NotificationManagerCompat.from(this);

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


        createNotificationChannel();

        //Test tot optimalisatie homescreen
        Button btn_notification = findViewById(R.id.btn_notification);

        //Start functie bij klik
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNotification();
            }
        });
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
        score = 52;

        //Planeet = slecht
        if (score <= 0) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Your planet is dying.")
                .setContentText("Oh no! Your planet score is getting low.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();
 
            notificationManager.notify(1, notification);
        }
        //Planeet = ok
        else if (score > 0 & score <= 50) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Everything is going okay.")
                .setContentText("Nothing went wrong nor good but don't forget to take care.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

            notificationManager.notify(2, notification);
        }
        //Planeet = goed
        else {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Your planet is full of hapiness.")
                .setContentText("Nothing went wrong nor good yet don't forget to take care.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

            notificationManager.notify(3, notification);
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
            channel2.setDescription("This is Channel 3");
 
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
}
