package mycontactlist.example.com.my_contact_list;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdayPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        getDialog().setTitle("Select Date");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c. get (Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public BirthdayPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        TextView textview = (TextView)getActivity().findViewById(R.id.textBirthday);

        textview.setText(dayOfMonth + ":" + (month+1) + ":" + year);

    }
}
