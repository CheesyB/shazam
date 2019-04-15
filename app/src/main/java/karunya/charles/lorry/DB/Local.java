package karunya.charles.lorry.DB;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.time.LocalDateTime;

import karunya.charles.lorry.DB.Converters.LocalDateTimeConverter;

@Entity(tableName = "local_table")
public class Local {



    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int mId;

    private Double longitude;
    private Double latitude;

    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime timestamp;

    public Local(double longitude, double latitude, LocalDateTime timestamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
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

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}
