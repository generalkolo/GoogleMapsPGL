package com.semanientreprise.googlemapsproject.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.semanientreprise.googlemapsproject.R;

public class BasicMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageButton im1,im2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_map);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.bmap);

        mapFragment.getMapAsync(this);

        im1 = findViewById(R.id.imageButton);
        im2 = findViewById(R.id.imageButton2);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng Sligo = new LatLng(54.27,-8.4761);

                mMap.addMarker(new MarkerOptions().position(Sligo).title("Sligo"));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(Sligo));

                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
    }
}
