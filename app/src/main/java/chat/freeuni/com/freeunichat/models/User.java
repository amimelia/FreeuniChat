package chat.freeuni.com.freeunichat.models;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by melia on 6/14/2017.
 */

public class User {
    public static Random rdForStatus = new Random();

    public User(){
        isActive = rdForStatus.nextBoolean();
    }

    public boolean isActive = true;
    public int id;
    public String displayName;
    public int phoneNumber;
    public String avatarImgUrl;
    public String about;
    public Bitmap avatarImgBitmap = null;
}
