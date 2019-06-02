package be.example.petplanet.petplanet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import be.example.petplanet.petplanet.R;

public class AddingActivity extends AppCompatActivity {
    ImageButton joining;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
        joining = findViewById(R.id.join);
        joining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(AddingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
