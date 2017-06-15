package chat.freeuni.com.freeunichat.downloaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import chat.freeuni.com.freeunichat.R;
import chat.freeuni.com.freeunichat.helpers.FirstRunChecker;
import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/15/2017.
 */

public class UsersPicturesDownloader extends AsyncTask<String, User, Void> {

    private PowerManager.WakeLock mWakeLock;
    PowerManager pm;
    List<User> users;
    Context context;
    DownloadPicturesReceiver receiver;

    public UsersPicturesDownloader (Context context, List<User> users, DownloadPicturesReceiver receiver){
        this.context = context;
        this.users = users;
        this.receiver = receiver;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
    }

    @Override
    protected Void doInBackground(String... sUrl) {
        for (User curUser : users){
            if (curUser.avatarImgBitmap == null){
                delayForTesting();
                curUser.avatarImgBitmap = downloadPicture(curUser.avatarImgUrl);
                if (curUser.avatarImgBitmap != null)//notify download
                    publishProgress(curUser);
            }
        }
        return null;
    }

    @Override
    public void onProgressUpdate(User... curUser) {
        receiver.pictureDownloaded(curUser[0]);
    }

    private void delayForTesting(){
        if (FirstRunChecker.isFirstRun(context)){
            try{
                Thread.sleep((long)context.getResources().getInteger(R.integer.download_friends_delay));
            }catch (Exception e){
            }
        }

    }

    private Bitmap downloadPicture(String src){
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap downloadedPic = BitmapFactory.decodeStream(input, null, options);
            return downloadedPic;
        } catch (IOException e) {
            Log.e("PicsDownloader", "Error Downloading pictures " + e.getMessage());
            return null;
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        mWakeLock.release();
    }
}
