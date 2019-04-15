package karunya.charles.lorry.DB;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LocalDao {

    @Insert
    void insert(Local local);


    @Query("SELECT * FROM local_table ORDER BY mId ASC;")
    LiveData<List<Local>> getAllLoacals();

    @Query("DELETE FROM local_table")
    void deleteAll();

    @Query("SELECT COUNT() FROM local_table;")
    LiveData<Integer> getTotal();


    @Query("DELETE FROM local_table WHERE mId =:id;")
    void delete(int id);

    @Query("UPDATE local_table SET longitude =  :payload WHERE mId = :id")
    void updateLongitude(String payload, int id);

    @Query("UPDATE local_table SET latitude =  :payload WHERE mId = :id")
    void updateLatiutude(String payload, int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setBusy(Busy busy);

    @Query("SELECT * FROM busy_table WHERE mId = 1")
    LiveData<Busy> isBusy();



}
