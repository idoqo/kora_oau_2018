package xyz.mchl.ferapid.junk;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.mchl.ferapid.MoneywaveService;
import xyz.mchl.ferapid.models.AuthRequest;
import xyz.mchl.ferapid.models.AuthToken;

public class Utils
{
    //todo terrible practice, move to config and REMOVE FROM REPO
    public final static String MWV_API_KEY = "ts_VXOAU0Q9GSQUVSXPUKH0";
    public final static String MWV_API_SECRET = "ts_2LAAFHL6QPA5R9ULCWWFQW0YVO1OXL";

    private String authToken;

    public static HashMap<String, String> fetchBankList() {
        HashMap<String, String> bankList = new HashMap<>();
        bankList.put("057", "ZENITH BANK PLC");
        bankList.put("035", "WEMA BANK PLC");
        bankList.put("033", "UNITED BANK FOR AFRICA PLC");
        bankList.put("032", "UNION BANK OF NIGERIA PLC");
        bankList.put("068", "STANDARD CHARTERED BANK NIGERIA LIMITED");
        bankList.put("076", "SKYE BANK PLC");
        bankList.put("082", "KEYSTONE BANK PLC");
        bankList.put("030", "HERITAGE BANK");
        bankList.put("058", "GTBANK PLC");
        bankList.put("214", "FCMB PLC");
        bankList.put("011", "UNITY BANK PLC");
        bankList.put("221", "STANBIC IBTC BANK PLC");
        bankList.put("304", "STERLING BANK PLC");
        bankList.put("044", "ACCESS BANK PLC");

        return bankList;
    }

    public static File getQrImagesFolder(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        return cw.getDir("qr_imgs", Context.MODE_PRIVATE);
    }

    public static String saveBitmapToStorage(Context context, Bitmap bitmap) {
        String filename = UUID.randomUUID().toString().replace("-", "_")+".png";

        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        }
        catch (Exception e) {
            Log.wtf("SavingFile", e.getMessage());
        }
        //return filename to store in database
        return filename;
    }

    public static Bitmap loadImageFromStorage(Context context, String filename) {
        File cachePath = new File(context.getCacheDir(), "images");
        File imageFile = new File(cachePath, filename);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (FileNotFoundException e) {
            //todo return default drawable
            return null;
        }
    }

    public static void shareImage(Context context, Uri contentUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        shareIntent.setDataAndType(contentUri,
                context.getContentResolver().getType(contentUri));
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
    }

    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }
}
