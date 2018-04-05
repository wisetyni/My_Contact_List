package mycontactlist.example.com.my_contact_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactListActivity extends AppCompatActivity {

    private TextView mTextMessage, birthday;
    private ImageView mCalendar;
    private Contact mCurrentContact;
    private EditText name, streetAddress, city, zipCode, homePhone, cellPhone, email, state;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mCalendar = findViewById(R.id.img_bday_calendar);
        mCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment bdayPickerFragment = new BirthdayPickerFragment();
                bdayPickerFragment.show(getSupportFragmentManager(), "birthdayPicker");
            }
        });

        initializeViews();

        mCurrentContact = new Contact();
    }

    private void initializeViews() {
        name = findViewById(R.id.editName);
        homePhone = findViewById(R.id.editHomePhone);
        cellPhone = findViewById(R.id.editCell);
        city = findViewById(R.id.editCity);
        streetAddress  = findViewById(R.id.editAddress);
        state = findViewById(R.id.editState);
        zipCode = findViewById(R.id.editZipCode);
        email = findViewById(R.id.editEMail);
    }

}
