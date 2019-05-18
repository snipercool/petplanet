package be.example.petplanet.petplanet.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatsActivity extends AppCompatActivity {
    //Firebase - collections
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSensorsDatabaseReference;
    private DatabaseReference mTemperatureDatabaseReference;

    //Listener
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase - initializeren van algemene variabelen.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSensorsDatabaseReference = mFirebaseDatabase.getReference().child("sensors");
        mTemperatureDatabaseReference = mSensorsDatabaseReference.child("temperature");

        attachDatabaseReadListener();
    }

    //Method to listen to database
    private void attachDatabaseReadListener() {
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
            };
            mTemperatureDatabaseReference.addChildEventListener(mChildEventListener);
        }
        // TODO("Add eventlistener on each collection of database.")
    }
}
