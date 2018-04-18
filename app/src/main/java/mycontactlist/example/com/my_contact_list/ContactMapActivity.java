package mycontactlist.example.com.my_contact_list;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactMapActivity extends FragmentActivity implements OnMapReadyCallback, OnRequestPermissionsResultCallback {

    private static final String TAG = ContactMapActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private GoogleMap mGoogleMap;
    private BottomNavigationView navigation;
    private RelativeLayout mapContainer;
    private ArrayList<Contact> mContacts;
    private Contact currentContact;
    private FusedLocationProviderClient mFusedLocationClient;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = NavUtil.getBottomNav(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initLocation();
        initMapType();
        initBottomNavigation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contactMap);
        mapFragment.getMapAsync(this);

        mContacts = new ArrayList<>();
        currentContact = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            currentContact = ds.getSpecificContact(extras.getInt("contactid"));
            ds.close();
        } else {
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            mContacts = ds.getContacts("contactname", "ASC");
            ds.close();
        }

    }

    private void initBottomNavigation() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initLocation() {
        final TextView locationText = findViewById(R.id.showMe);
        locationText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentSetting = locationText.getText().toString();
                if (currentSetting.equalsIgnoreCase("Location On")) {
                    locationText.setText("Location Off");
                    if (ActivityCompat.checkSelfPermission(ContactMapActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ContactMapActivity.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        askForLocationPermissions();

                    }
                    else{
                        mGoogleMap.setMyLocationEnabled(true);
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(ContactMapActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null){
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                                    builder.include(point);
                                    mGoogleMap.addMarker(new MarkerOptions().position(point).title("Current Location").snippet(point.toString()));
                                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                                }
                            }
                        });
                    }//end check permissions if/else
                } else {
                    locationText.setText("Location On");
                    mGoogleMap.setMyLocationEnabled(false);
                    mGoogleMap.clear();
                }//end check if text is Location On
            }
        });
    }

    private void askForLocationPermissions() {

        //request permissions for current location
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission" + grantResults[0]);
                }
                return;
            }
            default: break;
        }
    }

    private void initMapType() {
        final TextView satellite = findViewById(R.id.mapType);
        satellite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentSetting = satellite.getText().toString();
                if (currentSetting.equalsIgnoreCase("Satellite View")) {
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    satellite.setText("Normal View");
                } else {
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    satellite.setText("Satellite View");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        final String TAG_ERROR_DIALOG_FRAGMENT = "errorDialog";

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            //no problems just work
        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            ErrorDialogFragment.newInstance(status).show(getSupportFragmentManager(),
                    TAG_ERROR_DIALOG_FRAGMENT);
        } else {
            Toast.makeText(this, "Google Maps V2 is not available!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        static final String ARG_STATUS = "status";

        static ErrorDialogFragment newInstance(int status) {
            Bundle args = new Bundle();
            args.putInt(ARG_STATUS, status);
            ErrorDialogFragment result = new ErrorDialogFragment();
            result.setArguments(args);
            return (result);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle args = getArguments();
            return GooglePlayServicesUtil.getErrorDialog(args.getInt(ARG_STATUS),
                    getActivity(), 0);
        }

        @Override
        public void onDismiss(DialogInterface dlg) {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        int measuredWidth;
        int measuredHeight;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight() - 180;
        }


        if (mContacts.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < mContacts.size(); i++) {
                currentContact = mContacts.get(i);

                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + " " +
                        currentContact.getZipCode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                builder.include(point);

                mGoogleMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
            }
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 100));
        } else {
            if (currentContact != null) {
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + " " +
                        currentContact.getZipCode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                mGoogleMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ContactMapActivity.this).create();
                alertDialog.setTitle("No Data");
                alertDialog.setMessage("No data is available for the mapping function.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }
}
