package xyz.mchl.ferapid.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface QRCodeDao
{
    @Query("select * from QRCode")
    LiveData<List<QRCode>> getAllQrCodes();

    @Query("select * from QRCode where id = :id")
    QRCode getItemById(String id);

    @Insert(onConflict = REPLACE)
    void addQrCode(QRCode qrCode);

    @Delete
    void removeQrCode(QRCode qrCode);
}
