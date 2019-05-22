package be.example.petplanet.petplanet.Activities;

import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import be.example.petplanet.petplanet.R;

import static java.lang.Float.parseFloat;

//Graph

public class StatsActivity extends AppCompatActivity {

    //Firebase - collections
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSensorsDatabaseReference;
    private DatabaseReference mTemperatureDatabaseReference;

    //Listener
    private ChildEventListener mTemperatureChildEventListener;

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

        GraphView graph = findViewById(R.id.graph_temperature);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        mTemperatureDatabaseReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TemperatureClass temperature = new TemperatureClass();
                for (DataSnapshot temperatureSnapshot: dataSnapshot.getChildren()) {
                    //TODO: try & catch van for-loop maken
                    String value = temperatureSnapshot.getValue().toString();
                    temperature.addTemperatureToArray(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void displayTemperature(TemperatureClass temperature){
        //Graph
        GraphView graph = findViewById(R.id.graph_temperature);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(7);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(40);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        List<Integer> allTemperatures = temperature.getTemperatures();
        try {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (int i = 0; i < allTemperatures.size(); i++) {
                new DataPoint(i, allTemperatures.get(i));
            }
            graph.addSeries(series);
        } catch (IllegalArgumentException e) {
            Toast.makeText(StatsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
