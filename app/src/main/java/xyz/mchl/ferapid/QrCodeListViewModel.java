package xyz.mchl.ferapid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import xyz.mchl.ferapid.persistence.AppDatabase;
import xyz.mchl.ferapid.persistence.QRCode;

public class QrCodeListViewModel extends AndroidViewModel
{
    private final LiveData<List<QRCode>> qrCodeList;

    private AppDatabase appDatabase;

    public QrCodeListViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(this.getApplication());
        qrCodeList = appDatabase.qrCodeDao().getAllQrCodes();
    }

    public LiveData<List<QRCode>> getQrCodeList() {
        return qrCodeList;
    }

    public void deleteItem(QRCode qrCode) {
        new DeleteAsyncTask(appDatabase).execute(qrCode);
    }

    private static class DeleteAsyncTask extends AsyncTask<QRCode, Void, Void> {
        private AppDatabase appDB;

        DeleteAsyncTask(AppDatabase db) {
            this.appDB = db;
        }

        protected Void doInBackground(final QRCode... params) {
            appDB.qrCodeDao().removeQrCode(params[0]);
            return null;
        }
    }
}
