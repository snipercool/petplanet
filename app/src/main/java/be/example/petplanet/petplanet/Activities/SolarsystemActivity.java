package be.example.petplanet.petplanet.Activities;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import be.example.petplanet.petplanet.R;

public class SolarsystemActivity extends AppCompatActivity {
    ImageView outerring;
    ImageView middlering;
    ImageView innerring;
    ImageView sun;
    ImageButton user;
    Button planet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solarsystem);

        outerring = findViewById(R.id.outerring);
        Animation outer = AnimationUtils.loadAnimation(SolarsystemActivity.this, R.anim.rotate);
        outerring.startAnimation(outer);

        middlering = findViewById(R.id.middlering);
        Animation middle = AnimationUtils.loadAnimation(SolarsystemActivity.this, R.anim.rotate);
        middlering.startAnimation(middle);

        innerring = findViewById(R.id.innerring);
        Animation inner = AnimationUtils.loadAnimation(SolarsystemActivity.this, R.anim.rotate);
        innerring.startAnimation(inner);

        sun = findViewById(R.id.sun);
        Animation sunny = AnimationUtils.loadAnimation(SolarsystemActivity.this, R.anim.rotate);
        sun.startAnimation(sunny);

        planet = (Button) findViewById(R.id.planet);
        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SolarsystemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        user = (ImageButton) findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SolarsystemActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
