package be.example.petplanet.petplanet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import be.example.petplanet.petplanet.R;

public class LeaderboardActivity extends AppCompatActivity {
    private ImageButton planet;
    private ImageButton solar;
    private ImageButton graphs;
    private ImageButton qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // go to planetinfo
        planet = findViewById(R.id.planet);
        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LeaderboardActivity.this, PlanetinfoActivity.class);
                startActivity(intent);
            }
        });

        // go to qrcode
        qrcode = findViewById(R.id.qrcode);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LeaderboardActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        // go to grafieken
        graphs = findViewById(R.id.graphs);
        graphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LeaderboardActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
    }
}
