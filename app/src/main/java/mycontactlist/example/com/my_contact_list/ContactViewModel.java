package mycontactlist.example.com.my_contact_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ContactViewModel extends AndroidViewModel {


    private static final String TAG = ContactViewModel.class.getSimpleName();
    Executor executor;
    ContactDao mContactDao;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        executor = Executors.newSingleThreadExecutor();

        mContactDao = MyContactsDB.getDBInstance(application).getContactDao();
    }

    public void saveContact(final Contact contact){
        executor.execute(new Runnable() {
            @Override
            public void run() {
               Long id =  mContactDao.insertContact(contact);
                Log.d(TAG, "contact saved: id -  " + id);
            }
        });

    }
}
