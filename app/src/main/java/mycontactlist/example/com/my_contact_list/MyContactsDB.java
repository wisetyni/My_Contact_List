package mycontactlist.example.com.my_contact_list;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(version = 1, entities = Contact.class )
public abstract class MyContactsDB extends RoomDatabase {
    abstract ContactDao getContactDao();
}
