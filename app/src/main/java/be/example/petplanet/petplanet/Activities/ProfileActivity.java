package be.example.petplanet.petplanet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import be.example.petplanet.petplanet.R;

public class ProfileActivity extends AppCompatActivity {

    Button signout;

    ImageButton solarsystem;
    ImageButton anim;
    ImageButton planet;
    ImageButton graphs;
    ImageButton qrcode;
    ImageButton leaderbord;
    TextView scoring;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUniqueReference;
    private DatabaseReference mPlanetReference;
    private DatabaseReference mScoreReference;

    // Firebase - authentication

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase - initializeren van algemene variabelen.

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Firebase - initializeren van authenticatie variabelen.

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Firebase - initializeren van referentie.

        mUniqueReference = mFirebaseDatabase.getReference();
        //signout
        signout = findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
            }
        });

        //get score
        mPlanetReference = mFirebaseDatabase.getReference().child("planet");
        mScoreReference = mPlanetReference.child("0").child("score");

        scoring = (TextView) findViewById(R.id.score);

        // go to planet
        planet = findViewById(R.id.btn_menu_planet);
        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // go to qrcode
        qrcode = findViewById(R.id.btn_menu_camera);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ProfileActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        // go to grafieken
        graphs = findViewById(R.id.btn_menu_graphic);
        graphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ProfileActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });

        mScoreReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String score = dataSnapshot.getValue().toString();
                scoring.setText("Score= "+ score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
