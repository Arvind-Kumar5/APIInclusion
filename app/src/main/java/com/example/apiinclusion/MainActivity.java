package com.example.apiinclusion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDestinationLatLng = new LatLng(43.0752942, -89.4056331);
    private LatLng mLatLng;
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;

            mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Bascom Hill"));
            //Log.i("TAG", "CHECK");
            displayMyLocation();
            //Log.i("TAG", "DENIED");
            //googleMap.addMarker(new MarkerOptions().position(mLatLng).title("My location"));
        });

    }

    private void displayMyLocation() {
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_DENIED) {

            Log.i("TAG", "DENIEDCHECKING");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        else{
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task->{

                //System.out.println("Test Debugging HERE");
                Log.i("TAG", "CHECKING LOCATION");
                Location mLastKnownLocation = task.getResult();

                if(task.isSuccessful() && mLastKnownLocation != null){
                    Log.i("TAG", "INSIDE TASK SUCCESS");
                    mMap.addPolyline(new PolylineOptions().add(
                            new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), mDestinationLatLng)
                    );

                    mLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(mLatLng).title("My Location"));
                    //mMap.addMarker(new MarkerOptions().position(mLatLng).title("My Location"));
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults

        );
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }
}