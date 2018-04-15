package mycontactlist.example.com.my_contact_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ContactListActivity extends AppCompatActivity {


    private BottomNavigationView navigation;
    private Contact mCurrentContact;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent contact_intent = new Intent(getApplicationContext(), ContactListActivity.class);
                    startActivity(contact_intent);
                    return true;
                case R.id.navigation_dashboard:
                    Intent dashboard_intent = new Intent(getApplicationContext(), ContactMapActivity.class);
                    startActivity(dashboard_intent);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mCurrentContact = new Contact();
        final ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        initializeViews();


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void initializeViews() {

        navigation = findViewById(R.id.navigation);
    }

}
