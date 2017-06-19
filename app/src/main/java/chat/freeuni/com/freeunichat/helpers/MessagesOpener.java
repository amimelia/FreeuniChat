package chat.freeuni.com.freeunichat.helpers;

import android.content.Context;
import android.content.Intent;

import chat.freeuni.com.freeunichat.SingleChatActivity;
import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/18/2017.
 */

public class MessagesOpener {

    public static String PROFILE_ID_PARAM = "ProfileIdParam";

    public static void openChatTo(Context context, User user, int profileId){
        ChatActivityParametPasser.chatActivityUserParameter = user;
        Intent intent = new Intent(context, SingleChatActivity.class);
        String message = "" + profileId;
        intent.putExtra(PROFILE_ID_PARAM, message);
        context.startActivity(intent);
    }
}
