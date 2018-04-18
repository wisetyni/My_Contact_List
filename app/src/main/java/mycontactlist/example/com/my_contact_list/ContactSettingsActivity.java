package mycontactlist.example.com.my_contact_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

public class ContactSettingsActivity extends Activity {
    private RadioButton rbName, rbCity, rbBirthDay, rbAscending, rbDescending;
    private RadioGroup rgSortBy, rgSortOrder, rgBackgroundColor;
    private RadioButton rbWhite, rbPink, rbOrange, rbPurple, rbCyan;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = NavUtil.getBottomNav(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_settings);

        initSettings();
        initSortByClick();
        initSortOrderClick();
        initBackgroundColorClick();
        initScrollViewBackground();
        initBottomNavigation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_settings, menu);
        return true;
    }

    private void initBottomNavigation() {
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        int backgroundColor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getInt("backgroundColor", R.color.orange);

        initSortBySettings(sortBy);

        initSortOrderSettings(sortOrder);

        initBgColorSettings(backgroundColor);


    }

    private void initBgColorSettings(int backgroundColor) {
        rbWhite = findViewById(R.id.radioWhite);
        rbPink = findViewById(R.id.radioPink);
        rbOrange = findViewById(R.id.radioOrange);
        rbPurple = findViewById(R.id.radioPurple);
        rbCyan = findViewById(R.id.radioCyan);

        if(backgroundColor == R.color.white){
            rbWhite.setChecked(true);
        }else if (backgroundColor == R.color.orange){
            rbOrange.setChecked(true);
        }else if (backgroundColor == R.color.pink){
            rbPink.setChecked(true);
        }else if (backgroundColor == R.color.purple){
            rbPurple.setChecked(true);
        }else{
            rbCyan.setChecked(true);
        }
    }

    private void initSortOrderSettings(String sortOrder) {
        rbAscending = findViewById(R.id.radioAscending);
        rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortBySettings(String sortBy) {
        rbName = findViewById(R.id.radioName);
        rbCity = findViewById(R.id.radioCity);
        rbBirthDay = findViewById(R.id.radioBirthday);
        if (sortBy.equalsIgnoreCase("contactname")) {
            rbName.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("city")) {
            rbCity.setChecked(true);
        }
        else {
            rbBirthDay.setChecked(true);
        }
    }

    private void initSortByClick() {
        rgSortBy = findViewById(R.id.radioGroup1);
        rgSortBy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {

                if (rbName.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "contactname").commit();
                }
                else if (rbCity.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "city").commit();
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "birthday").commit();
                }
            }
        });
    }

    private void initSortOrderClick() {
        rgSortOrder = findViewById(R.id.radioGroup2);
        rgSortOrder.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").commit();
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").commit();
                }
            }
        });
    }

    private void initBackgroundColorClick() {
        rgBackgroundColor = findViewById(R.id.radioGroup3);
        rgBackgroundColor.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (rbOrange.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putInt("backgroundColor", R.color.orange).commit();
                }
                else if (rbPink.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putInt("backgroundColor", R.color.pink).commit();
                }
                else if (rbPurple.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putInt("backgroundColor", R.color.purple).commit();
                }
                else if (rbWhite.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putInt("backgroundColor", R.color.white).commit();
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putInt("backgroundColor", R.color.cyan).commit();
                }
            }
        });
    }

    private void initScrollViewBackground() {
        ScrollView scrollView = findViewById(R.id.scrollView2);
        int savedBgColor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getInt("backgroundColor", R.color.orange);
        scrollView.setBackgroundColor(getResources().getColor(savedBgColor));
    }

//    private void initListButton() {
//        ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
//        list.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(ContactSettingsActivity.this, ContactListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void initSettingsButton() {
//        ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
//        list.setEnabled(false);
//    }
//
//    private void initMapButton() {
//        ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
//        list.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(ContactSettingsActivity.this, ContactMapActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//    }

}