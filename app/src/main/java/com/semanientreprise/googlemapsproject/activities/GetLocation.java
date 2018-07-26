package com.semanientreprise.googlemapsproject.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.semanientreprise.googlemapsproject.fragments.MapDetailsFragment;
import com.semanientreprise.googlemapsproject.R;
import com.semanientreprise.googlemapsproject.util.BaseActivity;

import java.io.ByteArrayOutputStream;

public class GetLocation extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CONSTANT = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int REQUEST_CAMERA = 3;
    ImageView im;

    Location currentLocation;
    LatLng position;
    private static final String MAP_DETAILS_FRAG = "MAP_DETAILS_FRAG";
    private Bitmap thumbnail;

    GoogleApiClient apiClient;
    private static final String TAG = GetLocation.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_location);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.lmap);

        mapFragment.getMapAsync(this);

        buildClient();

        initView();
    }

    private void initView() {
        im = findViewById(R.id.getLocationIB);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "Location needs to be enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case REQUEST_CAMERA:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        if (currentLocation != null){
                            thumbnail = (Bitmap)data.getExtras().get("data");

                            Bitmap cropped = Bitmap.createBitmap(thumbnail,0,0,70,70);

                            position = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromBitmap(cropped)));
                        }
                        break;
                }
        }
    }

    private void buildClient() {
        apiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        apiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        final LocationRequest request = new LocationRequest();

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);
        request.setNumUpdates(10);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(request);

        PendingResult<LocationSettingsResult> results = LocationServices.SettingsApi.checkLocationSettings(apiClient,builder.build());

        results.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()){
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(GetLocation.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ActivityCompat.checkSelfPermission(GetLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(GetLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(GetLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CONSTANT);
                            ActivityCompat.requestPermissions(GetLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CONSTANT);

                            currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                        }
                        else {
                           currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(GetLocation.this, "settings unavailable", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (apiClient.isConnected())
            apiClient.disconnect();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        FragmentManager manager = getSupportFragmentManager();

        MapDetailsFragment mapDetailsFragment = (MapDetailsFragment) manager.findFragmentByTag(MAP_DETAILS_FRAG);

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();

        if (mapDetailsFragment == null) {
            mapDetailsFragment = MapDetailsFragment.newInstance(String.valueOf(currentLocation.getLatitude()),
                    String.valueOf(currentLocation.getLongitude()),byteArray);
        }

        addFragmentToActivity(manager,
                mapDetailsFragment,
                R.id.root_view,
                MAP_DETAILS_FRAG
        );
        return true;
    }
}
