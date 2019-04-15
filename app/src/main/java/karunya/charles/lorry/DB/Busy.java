package karunya.charles.lorry.DB;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "busy_table")
public class Busy {

    @NonNull
    @PrimaryKey
    private int mId;
    private Boolean state;

    public Busy(Boolean state){
        mId = 1;
        this.state = state;
    }


    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean busy) {
        this.state = busy;
    }


}
