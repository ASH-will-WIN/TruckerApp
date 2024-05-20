package com.example.truckerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private EditText phoneNumberEditText;
    private Button callButton;
    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        callButton = findViewById(R.id.callButton);

        // Initialize Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").build();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    savePhoneNumber(phoneNumber);
                    makePhoneCall(phoneNumber);
                } else {
                    phoneNumberEditText.setError("Enter a phone number");
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLocation();
        }
    }

    private void savePhoneNumber(final String phoneNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.userDao().insert(new User(phoneNumber));
            }
        }).start();
    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall(phoneNumber);
        }
    }

    private void startCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            // Use the location data as needed
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                startCall(phoneNumber);
            } else {
                // Permission denied, handle appropriately
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                // Permission denied, handle appropriately
            }
        }
    }
}
