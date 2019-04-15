package karunya.charles.lorry.DB;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "local_table")
public class Local {



    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int mId;

    private Double longitude;
    private Double latitude;

    public Local(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
