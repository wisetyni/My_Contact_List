package mycontactlist.example.com.my_contact_list;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;


@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertContact(Contact contact);

    @Delete
    int deleteContact(Contact contact);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateContact(Contact contact);
}
