package chat.freeuni.com.freeunichat.downloaders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import chat.freeuni.com.freeunichat.R;
import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/14/2017.
 */

public class FriendsListDownloader extends AsyncTask<String, String, Void> {

    List<User> users;
    Context context;
    private PowerManager.WakeLock mWakeLock;
    private boolean isDownlaodSucc = false;
    IFriendsDownloader friendsListReceiver;

    public FriendsListDownloader(Context context, IFriendsDownloader friendsListReceiver){
        this.context = context;
        this.friendsListReceiver = friendsListReceiver;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

        users = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(String... sUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            delayForTesting();
            createUsersFromJson(buffer.toString());
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void delayForTesting(){
        try{
            Thread.sleep((long)context.getResources().getInteger(R.integer.download_friends_delay));
        }catch (Exception e){
        }
    }

    private void createUsersFromJson(String jsonString){
        if (jsonString == null || jsonString.length() < 1){
            isDownlaodSucc = false;
            return;
        }

        isDownlaodSucc = true;
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONArray jArray  = object.getJSONArray("contactsList");
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                User curUser = new User();
                curUser.id = jObject.getInt("id");
                curUser.phoneNumber = jObject.getInt("phoneNumber");
                curUser.about = jObject.getString("about");
                curUser.avatarImgUrl = jObject.getString("avatarImg");
                curUser.displayName = jObject.getString("displayName");
                users.add(curUser);
            }
        } catch (JSONException e) {
            isDownlaodSucc = false;
            Log.e("JSONException", "Error: " + e.toString());
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        mWakeLock.release();
        if (!isDownlaodSucc){
            Toast.makeText(context, "Error Downloading Data.", Toast.LENGTH_LONG).show();
            users = null;
        }

        friendsListReceiver.friendsListDownloaded(users);
    }


}
