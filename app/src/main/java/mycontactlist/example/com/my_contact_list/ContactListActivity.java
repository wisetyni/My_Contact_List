package mycontactlist.example.com.my_contact_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactListActivity extends AppCompatActivity {

    private TextView mTextMessage, birthday;
    private ImageView mCalendar;
    private Contact mCurrentContact;
    private EditText name, streetAddress, city, zipCode, homePhone, cellPhone, email, state;
    private Button saveContact;
    private BottomNavigationView navigation;
    private RelativeLayout relativeLayout;
    private SharedPreferences mSharedPreferences;
    private Toolbar toolbar;

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
                    Intent dashboard_intent = new Intent(getApplicationContext(), ContactListActivity.class);
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
        initTextChangedEvents();

        relativeLayout.setBackgroundColor(Color.parseColor(mSharedPreferences.getString("background_color", "#111111")));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment bdayPickerFragment = new BirthdayPickerFragment();
                bdayPickerFragment.show(getSupportFragmentManager(), "birthdayPicker");
            }
        });

        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentContact.setContactName(name.getText().toString());
                mCurrentContact.setPhoneNumber(homePhone.getText().toString());
                mCurrentContact.setCellNumber(cellPhone.getText().toString());
                mCurrentContact.setCity(city.getText().toString());
                mCurrentContact.setState(state.getText().toString());
                mCurrentContact.setStreetAddress(streetAddress.getText().toString());
                mCurrentContact.setZipCode(zipCode.getText().toString());
                mCurrentContact.setEmail(email.getText().toString());
                contactViewModel.saveContact(mCurrentContact);
            }
        });
    }

    private void initializeViews() {
        relativeLayout = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolbar);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        name = findViewById(R.id.editName);
        homePhone = findViewById(R.id.editHomePhone);
        cellPhone = findViewById(R.id.editCell);
        city = findViewById(R.id.editCity);
        streetAddress  = findViewById(R.id.editAddress);
        state = findViewById(R.id.editState);
        zipCode = findViewById(R.id.editZipCode);
        email = findViewById(R.id.editEMail);
        birthday = findViewById(R.id.textBirthday);
        saveContact = findViewById(R.id.saveContactBtn);
        mCalendar = findViewById(R.id.img_bday_calendar);

        //bottom nav
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
    }

    private void initTextChangedEvents() {

        homePhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        cellPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

}
