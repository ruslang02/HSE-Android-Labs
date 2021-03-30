package xyz.ruslang.task20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager = null;
    Location curLocation = null;
    boolean gpsEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        registerLocationUpdater();
    }

    private void registerLocationUpdater() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 100);
            return;
        }
        curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        new android.os.Handler().postDelayed(this::updateGpsView,500);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                curLocation = location;
                updateGpsView();
            }
        });
        locationManager.addGpsStatusListener(new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                gpsEnabled = event != 2;
                updateGpsView();
            }
        });
    }

    private void updateGpsView() {
        TextView gpsView = (TextView) findViewById(R.id.gpsView);
        if (curLocation == null) {
            gpsView.setText("GPS status: " + (gpsEnabled ? "Enabled" : "Disabled"));
        } else {
            gpsView.setText("Latitude: " + curLocation.getLatitude() +
                    "\nLongitude: " + curLocation.getLongitude() +
                    "\nGPS status: " + (gpsEnabled ? "Enabled" : "Disabled"));
        }
        new android.os.Handler().postDelayed(this::updateGpsView,500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            registerLocationUpdater();
        }
    }
}