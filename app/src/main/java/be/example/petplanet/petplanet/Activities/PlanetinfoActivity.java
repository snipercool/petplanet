package be.example.petplanet.petplanet.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import be.example.petplanet.petplanet.R;

public class PlanetinfoActivity extends AppCompatActivity {
    //Imageviews
    private ImageView animatePlanet;

    //Imagebuttons
    private ImageButton planet;
    private ImageButton stats;
    private ImageButton camera;
    private ImageButton share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planetinfo);

        // planet for animation
        animatePlanet = findViewById(R.id.iv_stats_planet);
        Animation animation = AnimationUtils.loadAnimation(PlanetinfoActivity.this, R.anim.rotate);
        animatePlanet.startAnimation(animation);

        // go to zonnestelsel
        planet = findViewById(R.id.btn_menu_planet);
        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(PlanetinfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // go to qrcode
        camera = findViewById(R.id.btn_menu_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(PlanetinfoActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        // go to grafieken
        stats = findViewById(R.id.btn_menu_graphic);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(PlanetinfoActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });

        //share planet
        share = findViewById(R.id.ib_desc_btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                //intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, "Share your planet"));
            }
        });
    }
}
