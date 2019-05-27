package be.example.petplanet.petplanet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import be.example.petplanet.petplanet.R;

import static java.lang.Float.parseFloat;
import static java.lang.Float.toHexString;

//Graph

public class StatsActivity extends AppCompatActivity {

    //Firebase - collections
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSensorsDatabaseReference;
    private DatabaseReference mTemperatureDatabaseReference;

    //Imageviews
    private ImageView animatePlanet;

    //Imagebuttons
    private ImageButton planet;
    private ImageButton graphs;
    private ImageButton qrcode;

    //Listener
    private ChildEventListener mTemperatureChildEventListener;

    //Graph
    private LineGraphSeries seriesTemperatureInside;
    private LineGraphSeries seriesTemperatureOutside;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // planet for animation
        animatePlanet = findViewById(R.id.iv_stats_planet);
        Animation animation = AnimationUtils.loadAnimation(StatsActivity.this, R.anim.rotate);
        animatePlanet.startAnimation(animation);

        // go to zonnestelsel
        planet = findViewById(R.id.btn_menu_planet);
        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(StatsActivity.this, SolarsystemActivity.class);
                startActivity(intent);
            }
        });

        // go to qrcode
        qrcode = findViewById(R.id.btn_menu_camera);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(StatsActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        // go to grafieken
        graphs = findViewById(R.id.btn_menu_graphic);
        graphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(StatsActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });

        //Firebase - initializeren van algemene variabelen.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSensorsDatabaseReference = mFirebaseDatabase.getReference().child("sensors");
        mTemperatureDatabaseReference = mSensorsDatabaseReference.child("temperature");

        GraphView graph = findViewById(R.id.graph_temperature);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(40);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        seriesTemperatureInside = new LineGraphSeries();
        graph.addSeries(seriesTemperatureInside);

        seriesTemperatureOutside = new LineGraphSeries();
        graph.addSeries(seriesTemperatureOutside);
    }

    @Override
    protected void onStart(){
        super.onStart();

        mTemperatureDatabaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataPoint[] dpTempInside = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                DataPoint[] dpTempOutside = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for(DataSnapshot tempDataSnapshot : dataSnapshot.getChildren()){
                    TemperatureClass temp = tempDataSnapshot.getValue(TemperatureClass.class);
                    dpTempInside[index] = new DataPoint(index, temp.getTemperatureInside());
                    dpTempOutside[index] =  new DataPoint(index, temp.getTemperatureOutside());
                    index++;

                    Log.i("temperature", String.valueOf(temp.getTemperatureOutside()));
                }

                seriesTemperatureInside.resetData(dpTempInside);
                seriesTemperatureOutside.resetData(dpTempOutside);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
