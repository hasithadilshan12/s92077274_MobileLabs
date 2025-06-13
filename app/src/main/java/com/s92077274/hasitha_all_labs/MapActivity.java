package com.s92077274.hasitha_all_labs;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private EditText addressEditText;
    private Button showLocationButton;
    private Button goToSensorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        addressEditText = findViewById(R.id.addressEditText);
        showLocationButton = findViewById(R.id.showLocationButton);
        goToSensorButton = findViewById(R.id.goToSensorButton);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mapFragmentContainer, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressEditText.getText().toString().trim();
                if (!address.isEmpty()) {
                    geocodeAddressAndShowOnMap(address);
                } else {
                    Toast.makeText(MapActivity.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });


        goToSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // Default location: Colombo, Sri Lanka
        LatLng defaultLocation = new LatLng(6.9271, 79.8612);
        googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f));
    }

    private void geocodeAddressAndShowOnMap(String addressString) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressString, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                if (googleMap != null) {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(addressString));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    Toast.makeText(this, "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Map not ready yet. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Address not found. Try again.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
