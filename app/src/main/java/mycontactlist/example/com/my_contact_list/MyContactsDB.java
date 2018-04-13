package mycontactlist.example.com.my_contact_list;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(version = 1, entities = Contact.class )
public abstract class MyContactsDB extends RoomDatabase {

    public static MyContactsDB sINSTANCE;
    abstract ContactDao getContactDao();

    public static MyContactsDB getDBInstance(Context context){

        if (sINSTANCE == null) {
            sINSTANCE = create(context);
        }
        return sINSTANCE;

    }

    static MyContactsDB create(Context context){
        RoomDatabase.Builder<MyContactsDB> builder =
                Room.databaseBuilder(context.getApplicationContext(),
                        MyContactsDB.class,
                        "MyContactsDB");
        return builder.build();
    }
}
