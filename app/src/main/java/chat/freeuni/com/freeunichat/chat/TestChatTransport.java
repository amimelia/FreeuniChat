package chat.freeuni.com.freeunichat.chat;

import android.content.Context;
import android.os.Handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import chat.freeuni.com.freeunichat.adapters.ChatMessagesAdapter;
import chat.freeuni.com.freeunichat.database.DatabaseAccess;
import chat.freeuni.com.freeunichat.models.Message;
import chat.freeuni.com.freeunichat.models.MessageStatuses;

/**
 * Created by melia on 6/18/2017.
 */

public class TestChatTransport {

    private static int HodorProfileId = 3;

    //Singleton
    private static TestChatTransport _chatManager = new TestChatTransport();
    public static TestChatTransport getChatManager(){
        return _chatManager;
    }


    private Context context;
    private List<ChatEventListener> chatEventListeners;
    public TestChatTransport(){
        chatEventListeners = new ArrayList<>();
        startListeningChatEvents();
    }
    Random rdForChatManager = new Random();

    private void startListeningChatEvents() {


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message newMessage = new Message();
                newMessage.chatTo = rdForChatManager.nextInt(6) + 1;
                newMessage.isSent = 0;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat(DatabaseAccess.DateFormat);
                String formattedDate = df.format(c.getTime());
                newMessage.date = formattedDate;
                newMessage.message = getRandomMessage();
                newMessage.messageStatus = MessageStatuses.New;
                sendMessageToUser(newMessage);
                startListeningChatEvents();
            }
        }, 60 * 1000);
    }

    //called when message is generated for user
    public void sendMessageToUser(Message m){
        saveMessageToDb(m);
        for (ChatEventListener listener : chatEventListeners){
            listener.incomingMessage(m);
        }
    }

    //only main acitvity should call this
    public void setContext(Context context){
        this.context = context;
    }

    /**
     * გამოიძახება ყოველთვის როცა მომხმარებელი გზავნის შეტყობინებას რომელიმე მეგობართან
     */
    public void sendMessage(Message m) {
        saveMessageToDb(m);
        generateResponceMessage(m);
    }

    private void generateResponceMessage(final Message m){
        long randomMills = getRandomMillsForTimes();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message newMessage = new Message();
                newMessage.chatTo = m.chatTo;
                newMessage.isSent = 0;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat(DatabaseAccess.DateFormat);
                String formattedDate = df.format(c.getTime());
                newMessage.date = formattedDate;
                newMessage.message = getRandomMessage();
                newMessage.messageStatus = MessageStatuses.New;
                sendMessageToUser(newMessage);
            }
        }, randomMills);
    }

    private String getRandomMessage(){
        return "I am dumb, i only know one answer";
    }


    private long getRandomMillsForTimes(){
        return rdForChatManager.nextInt(1000 * 9) + 1000 * 1L;
    }


    private void saveMessageToDb(Message m){
        if (context == null)
            return;
        DatabaseAccess dbAcc = new DatabaseAccess(context);
        dbAcc.open();
        dbAcc.saveMessage(m);
        dbAcc.close();
    }

    public void addChatEventListener(ChatEventListener chatEventListener) {
        chatEventListeners.add(chatEventListener);
    }

    public void removeEventListener(ChatEventListener chatEventListener){
        chatEventListeners.remove(chatEventListener);
    }
}
