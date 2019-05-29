package be.example.petplanet.petplanet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import be.example.petplanet.petplanet.R;

public class ProfileActivity extends AppCompatActivity {

    Button signout;

    ImageButton solarsystem;
    ImageButton anim;
    ImageButton planet;
    ImageButton graphs;
    ImageButton qrcode;
    ImageButton leaderbord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
    }
}
