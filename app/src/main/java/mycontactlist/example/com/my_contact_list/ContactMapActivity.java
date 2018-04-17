package mycontactlist.example.com.my_contact_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactMapActivity extends FragmentActivity implements OnMapReadyCallback {

	private static final String TAG = ContactMapActivity.class.getSimpleName();
	GoogleMap mGoogleMap;
	private BottomNavigationView navigation;

	ArrayList<Contact> mContacts;
	Contact currentContact;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_home:
					Intent contact_intent = new Intent(getApplicationContext(), ContactListActivity.class);
					startActivity(contact_intent);
					return true;
				case R.id.navigation_map:
					//do nothing since you are already on the map screen
					return true;
				case R.id.navigation_settings:
					Intent settings_intent = new Intent(getApplicationContext(), ContactSettingsActivity.class);
					startActivity(settings_intent);
					return true;
			}
			return false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_map);
		Log.d(TAG, "onCreate: inside");

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
					if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					mGoogleMap.setMyLocationEnabled(true);
				}
	            else {
	                locationText.setText("Location On");
	                mGoogleMap.setMyLocationEnabled(false);
	            }   
	         }
	    }); 
	}  
	    
	private void initMapType() {
	    final TextView satelite = findViewById(R.id.mapType);
	    satelite.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	            String currentSetting = satelite.getText().toString();
	            if (currentSetting.equalsIgnoreCase("Satellite View")) {
	                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	                satelite.setText("Normal View");
	            }
	            else {
	                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	                satelite.setText("Satellite View");
	            }
	        }
	    }); 
	}  
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu){


    	return true;
    } 

    public void onPause() {
    	   super.onPause();
    	   finish();
    	}
    	    
	@Override
	public void onResume() {
		super.onResume();
		final String TAG_ERROR_DIALOG_FRAGMENT="errorDialog";

		int status=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (status == ConnectionResult.SUCCESS) {
						  //no problems just work
		}
		else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
			ErrorDialogFragment.newInstance(status).show(getSupportFragmentManager(),
												TAG_ERROR_DIALOG_FRAGMENT);
		}
		else {
			Toast.makeText(this, "Google Maps V2 is not available!",
								 Toast.LENGTH_LONG).show();
			finish();
		}
	}

	public static class ErrorDialogFragment extends DialogFragment {
		static final String ARG_STATUS="status";

		static ErrorDialogFragment newInstance(int status) {
			Bundle args=new Bundle();
			args.putInt(ARG_STATUS, status);
			ErrorDialogFragment result=new ErrorDialogFragment();
			result.setArguments(args);
			return(result);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Bundle args=getArguments();
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

		Log.d(TAG, "onMapReady: inside");

		mGoogleMap = googleMap;
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		int measuredWidth = 0;
		int measuredHeight = 0;
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

				googleMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
			}
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 100));
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

				googleMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
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
