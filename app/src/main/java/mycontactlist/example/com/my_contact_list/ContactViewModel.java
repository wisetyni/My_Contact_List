package mycontactlist.example.com.my_contact_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ContactViewModel extends AndroidViewModel {


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
            }
        });

    }
}
