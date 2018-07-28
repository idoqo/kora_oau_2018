package xyz.mchl.ferapid;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import xyz.mchl.ferapid.persistence.AppDatabase;
import xyz.mchl.ferapid.persistence.QRCode;

public class AddQrCodeViewModel extends AndroidViewModel
{
    private AppDatabase appDB;

    public AddQrCodeViewModel(Application application) {
        super(application);
        appDB = AppDatabase.getDatabase(this.getApplication());
    }

    public void addQrCode(final QRCode qrCode) {
        new AddAsyncTask(appDB).execute(qrCode);
    }

    private static class AddAsyncTask extends AsyncTask<QRCode, Void, Void>
    {
        private AppDatabase appDb;
        AddAsyncTask(AppDatabase database) {
            appDb = database;
        }

        @Override
        protected Void doInBackground(QRCode... qrCodes) {
            appDb.qrCodeDao().addQrCode(qrCodes[0]);
            return null;
        }
    }
}
