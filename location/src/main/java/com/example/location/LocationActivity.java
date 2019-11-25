package com.example.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class LocationActivity extends AppCompatActivity {


    private ToggleButton locationOnOff;
    private String longitude, latitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean updatesOn = false;
    private TextView lat,lion;
    Context mainContext;
    MainInterface mainInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        lat=(TextView)findViewById(R.id.lat);
        lion=(TextView)findViewById(R.id.longi);


        Log.e("loca_Activity", "in regevent");

        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(7500); //use a value fo about 10 to 15s for a real app
        this.locationRequest.setFastestInterval(5000);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        longitude=String.valueOf(location.getLongitude());
                        latitude=String.valueOf(location.getLatitude());
                        Log.e("loca_activity", "onsucces");
                        lion.setText(longitude);
                        lat.setText(latitude);


                       // mainInterface=(MainInterface)mainContext;
                        //mainInterface.makeToast("Lat: "+latitude + "  Long " + longitude + "");
                        Toast.makeText(LocationActivity.this,"Lat: "+latitude + "  Long " + longitude + "", Toast.LENGTH_LONG).show();

                    }
                }
            });
        } else {
            // request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    Log.e("loca_Ac", "onlocaresulytoutofid");

                    //Update UI with location data
                    if (location != null) {
                        longitude=String.valueOf(location.getLongitude());
                        latitude=String.valueOf(location.getLatitude());
                        Log.e("loca_Ac", "onloca rsesult");

                        Toast.makeText(LocationActivity.this,"Lat: "+latitude + "  Long " + longitude + "", Toast.LENGTH_LONG).show();
                        lion.setText(longitude);
                        lat.setText(latitude);


                        //   mainInterface=(MainInterface)mainContext;
                       // mainInterface.makeToast("Lat: "+latitude + "  Long " + longitude + "");


                    }
                }
            }
        };




    }
    public void registerEvents(Context context) {


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted do nothing and carry on

                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


}