package mycontactlist.example.com.my_contact_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactListActivity extends AppCompatActivity {

    private TextView mTextMessage, birthday, save;
    private ImageView mCalendar;
    private Contact mCurrentContact;
    private EditText name, streetAddress, city, zipCode, homePhone, cellPhone, email, state;
    private BottomNavigationView navigation;
    private RelativeLayout relativeLayout;
    private SharedPreferences mSharedPreferences;
    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = NavUtil.getBottomNav(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mCurrentContact = new Contact();

        initializeViews();
        initTextChangedEvents();

        int savedBgColor = mSharedPreferences.getInt("background_color", R.color.white);
        relativeLayout.setBackgroundColor(getResources().getColor(savedBgColor));
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                mCurrentContact.setContactName(name.getText().toString());
                mCurrentContact.setPhoneNumber(homePhone.getText().toString());
                mCurrentContact.setCellNumber(cellPhone.getText().toString());
                mCurrentContact.setCity(city.getText().toString());
                mCurrentContact.setState(state.getText().toString());
                mCurrentContact.setStreetAddress(streetAddress.getText().toString());
                mCurrentContact.setZipCode(zipCode.getText().toString());
                mCurrentContact.setEMail(email.getText().toString());


                ContactDataSource ds = new ContactDataSource(ContactListActivity.this);
                ds.open();

                boolean wasSuccessful = false;
                if (mCurrentContact.getContactID()==-1) {
                    wasSuccessful = ds.insertContact(mCurrentContact);
                    int newId = ds.getLastContactId();
                    mCurrentContact.setContactID(newId);
                }
                else {
                    wasSuccessful = ds.updateContact(mCurrentContact);
                }
                ds.close();

//                if (wasSuccessful) {
//                    ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
//                    editToggle.toggle();
//                    setForEditing(false);
//                }
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
        save = findViewById(R.id.save);
        mCalendar = findViewById(R.id.img_bday_calendar);

        //bottom nav
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
    }

    private void initTextChangedEvents() {

        homePhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        cellPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(streetAddress.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(city.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(state.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(zipCode.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(homePhone.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(cellPhone.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
    }

}
