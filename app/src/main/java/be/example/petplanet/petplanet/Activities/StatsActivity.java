package be.example.petplanet.petplanet.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Graph
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import be.example.petplanet.petplanet.R;

public class StatsActivity extends AppCompatActivity {

    //Firebase - collections
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSensorsDatabaseReference;
    private DatabaseReference mTemperatureDatabaseReference;

    //Listener
    private ChildEventListener mChildEventListener;

    //Graph
    LineGraphSeries seriesTemperature;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //Menu
        Button btn_menu_planet = findViewById(R.id.btn_menu_planet);
        Button btn_menu_camera = findViewById(R.id.btn_menu_camera);
        Button btn_menu_stats = findViewById(R.id.btn_menu_graphic);

        //Firebase - initializeren van algemene variabelen.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSensorsDatabaseReference = mFirebaseDatabase.getReference().child("sensors");
        mTemperatureDatabaseReference = mSensorsDatabaseReference.child("temperature");

        //attachDatabaseReadListener();

        //Graph
        GraphView graphTemperature = findViewById(R.id.graph_temperature);
        seriesTemperature = new LineGraphSeries();

        try {
            LineGraphSeries < DataPoint > series = new LineGraphSeries < > (new DataPoint[] {
                    new DataPoint(0, 1),
                    new DataPoint(1, 5),
                    new DataPoint(2, 3),
                    new DataPoint(3, 2),
                    new DataPoint(4, 6)
            });
            graphTemperature.addSeries(series);
        } catch (IllegalArgumentException e) {
            Toast.makeText(StatsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method to listen to database
    /*private void attachDatabaseReadListener() {
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TemperatureClass temperature = dataSnapshot.getValue(TemperatureClass.class);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            mTemperatureDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }*/

    //TODO: display temperature in beautiful graph
}
