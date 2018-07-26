package com.semanientreprise.googlemapsproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.semanientreprise.googlemapsproject.R;

public class MainActivity extends AppCompatActivity {

    ImageButton im1,im2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im1 = findViewById(R.id.basicMap);
        im2 = findViewById(R.id.locationMap);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BasicMap.class));
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GetLocation.class));
            }
        });
    }
}