package mycontactlist.example.com.my_contact_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

/**
 * Created by ya250083 on 4/18/18.
 */

public class NavUtil {

    public static BottomNavigationView.OnNavigationItemSelectedListener getBottomNav(final Activity activity){
        return new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if(!activity.getLocalClassName().contentEquals("ContactListActivity")){
                            Intent contact_intent = new Intent(activity.getApplicationContext(), ContactListActivity.class);
                            activity.startActivity(contact_intent);
                        }
                        return true;
                    case R.id.navigation_map:
                        if(!activity.getLocalClassName().contentEquals("ContactMapActivity")){
                            Intent map_intent = new Intent(activity.getApplicationContext(), ContactMapActivity.class);
                            activity.startActivity(map_intent);
                        }
                        return true;
                    case R.id.navigation_settings:
                        if(!activity.getLocalClassName().contentEquals("ContactSettingsActivity")){
                            Intent settings_intent = new Intent(activity.getApplicationContext(), ContactSettingsActivity.class);
                            activity.startActivity(settings_intent);
                        }
                        return true;
                    case R.id.navigation_location:
                        if(!activity.getLocalClassName().contentEquals("LocationSensorsActivity")){
                            Intent location_intent = new Intent(activity.getApplicationContext(), LocationSensorsActivity.class);
                            activity.startActivity(location_intent);
                        }
                        return true;
                }
                return false;
            }
        };
    }
}
