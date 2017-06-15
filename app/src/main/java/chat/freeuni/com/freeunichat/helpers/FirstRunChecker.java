package chat.freeuni.com.freeunichat.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by melia on 6/16/2017.
 */

public class FirstRunChecker {

    //checks if its first run if its sets isFirstRun - false
    public static boolean isFirstRun(Context context){
        SharedPreferences prefs = context.getSharedPreferences("chat.freeuni.com.freeunichat", Context.MODE_PRIVATE);
        if (prefs.getBoolean("isFirstRun", true)) {

            return true;
        }
        return false;
    }

    public static void removeFirstRunFlag(Context context){
        SharedPreferences prefs = context.getSharedPreferences("chat.freeuni.com.freeunichat", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("isFirstRun", false).commit();
    }
}
