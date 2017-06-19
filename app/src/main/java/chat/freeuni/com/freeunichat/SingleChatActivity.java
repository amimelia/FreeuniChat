package chat.freeuni.com.freeunichat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import junit.framework.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chat.freeuni.com.freeunichat.adapters.ChatMessagesAdapter;
import chat.freeuni.com.freeunichat.adapters.RecentChatAdapter;
import chat.freeuni.com.freeunichat.chat.ChatEventListener;
import chat.freeuni.com.freeunichat.chat.TestChatTransport;
import chat.freeuni.com.freeunichat.database.DatabaseAccess;
import chat.freeuni.com.freeunichat.helpers.ChatActivityParametPasser;
import chat.freeuni.com.freeunichat.helpers.MessagesOpener;
import chat.freeuni.com.freeunichat.models.Message;
import chat.freeuni.com.freeunichat.models.MessageStatuses;
import chat.freeuni.com.freeunichat.models.User;

public class SingleChatActivity extends AppCompatActivity implements ChatEventListener {

    private int mchatTo = 0;
    List<Message> chatMessagesDataSet = new ArrayList<>();
    ChatMessagesAdapter chatMessagesAdapter;
    Bitmap[] profilePictures;
    EditText messageTextBox;
    LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MessagesOpener.PROFILE_ID_PARAM);
        mchatTo = Integer.parseInt(message);
        messageTextBox = (EditText) findViewById(R.id.messageEdit);
        RecyclerView chatRv = (RecyclerView)findViewById(R.id.messagesContainer);
        chatRv.setHasFixedSize(true);
         mLayoutManager = new LinearLayoutManager(this);
        chatRv.setLayoutManager(mLayoutManager);

        ImageButton chatSendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButtonClicked();
            }
        });

        chatMessagesAdapter = new ChatMessagesAdapter(getApplicationContext(),
                chatMessagesDataSet, ChatActivityParametPasser.chatActivityUserParameter);
        chatRv.setAdapter(chatMessagesAdapter);/* */
        TestChatTransport.getChatManager().addChatEventListener(this);
        updateChatView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        TestChatTransport.getChatManager().removeEventListener(this);
    }

    private void sendButtonClicked(){
        Message newMessage = new Message();
        newMessage.chatTo = this.mchatTo;
        newMessage.messageStatus = MessageStatuses.Seen;
        newMessage.message = messageTextBox.getText().toString();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DatabaseAccess.DateFormat);
        String formattedDate = df.format(c.getTime());
        newMessage.date = formattedDate;
        newMessage.isSent = 1;
        TestChatTransport.getChatManager().sendMessage(newMessage);
        messageTextBox.setText("");
        addNewMessage(newMessage);
    }

    private void updateChatView(){
        DatabaseAccess dbAcc = new DatabaseAccess(this);
        dbAcc.open();
        List<Message> chatMessages = dbAcc.getChatMessagesTo(mchatTo);
        dbAcc.close();
        chatListUpdated(chatMessages);
    }

    private void chatListUpdated(List<Message> chatMessages){
        chatMessagesDataSet.clear();
        chatMessagesDataSet.addAll(chatMessages);
        chatMessagesAdapter.notifyDataSetChanged();
    }

    private void addNewMessage(Message m){
        updateChatView();
    }

    @Override
    public void incomingMessage(Message m){
        if (m.chatTo != mchatTo)
            return;
        DatabaseAccess dbAcc = new DatabaseAccess(this);
        dbAcc.open();
        //dbAcc.markMessageSeen(m);
        dbAcc.close();
        addNewMessage(m);
    }

    @Override
    public void contactStatusChanged(Message m){}


}
