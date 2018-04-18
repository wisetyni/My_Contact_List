package mycontactlist.example.com.my_contact_list;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationSensorsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private LocationListener ntwkListener, gpsListener;
    private Location currentBestLocation;
    private TextView gpsLatTxt, gpsLonTxt, gpsAccuracyTxt;
    private TextView ntwkLatTxt, ntwkLonTxt, ntwkAccuracyTxt;
    private TextView bestLatTxt, bestLonTxt, bestAccuracyTxt;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = NavUtil.getBottomNav(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_sensors);

        initBottomNavigation();
        initViews();
        initLocationManager();
        initGPSSensor();
        initNtwkSensor();
    }

    public void onPause() {

        try {
            locationManager.removeUpdates(ntwkListener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
        finish();
    }

    private void initLocationManager() {
        locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
    }

    private void initViews() {
        gpsLatTxt = findViewById(R.id.gps_latitude);
        gpsLonTxt = findViewById(R.id.gps_longitude);
        gpsAccuracyTxt = findViewById(R.id.gps_accuracy);

        ntwkLatTxt = findViewById(R.id.ntwk_latitude);
        ntwkLonTxt = findViewById(R.id.ntwk_longitude);
        ntwkAccuracyTxt = findViewById(R.id.ntwk_accuracy);

        bestLatTxt = findViewById(R.id.best_latitude);
        bestLonTxt = findViewById(R.id.best_longitude);
        bestAccuracyTxt = findViewById(R.id.best_accuracy);

    }

    private void initNtwkSensor() {
        ntwkListener = displayLocationUpdates(LocationManager.NETWORK_PROVIDER);
    }

    private void initGPSSensor() {
        gpsListener = displayLocationUpdates(LocationManager.GPS_PROVIDER);

    }

    private LocationListener displayLocationUpdates(final String provider) {
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (isBetterLocation(location)) {
                     currentBestLocation = location;
                     setBestLocationViews();
                 }



                switch (provider){
                    case LocationManager.NETWORK_PROVIDER:
                        ntwkLatTxt.setText(String.valueOf(location.getLatitude()));
                        ntwkAccuracyTxt.setText(String.valueOf(location.getAccuracy()));
                        ntwkLonTxt.setText(String.valueOf(location.getLongitude()));
                        break;
                    case LocationManager.GPS_PROVIDER:
                        gpsLatTxt.setText(String.valueOf(location.getLatitude()));
                        gpsAccuracyTxt.setText(String.valueOf(location.getAccuracy()));
                        gpsLonTxt.setText(String.valueOf(location.getLongitude()));
                        break;
                    default: break;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
        };

        if (ActivityCompat.checkSelfPermission(LocationSensorsActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LocationSensorsActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            askForLocationPermissions();
        }else {
            locationManager.requestLocationUpdates(provider, 0, 0, listener);
        }

        return listener;
    }

    private void setBestLocationViews() {
        bestLatTxt.setText(String.valueOf(currentBestLocation.getLatitude()));
        bestAccuracyTxt.setText(String.valueOf(currentBestLocation.getAccuracy()));
        bestLonTxt.setText(String.valueOf(currentBestLocation.getLongitude()));

    }

    private void askForLocationPermissions() {

        //request permissions for current location
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

    }

    private boolean isBetterLocation(Location location) {
        boolean isBetter = false;
        if (currentBestLocation == null) {
            isBetter = true;
        }
        else if (location.getAccuracy() <= currentBestLocation.getAccuracy()) {
            isBetter = true;
        }
        else if (location.getTime() - currentBestLocation.getTime() > 5*60*1000) {
            isBetter = true;
        }
        return isBetter;
    }

    private void initBottomNavigation() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
