package chat.freeuni.com.freeunichat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.freeuni.com.freeunichat.adapters.FriendsListAdapter;
import chat.freeuni.com.freeunichat.adapters.FriendsListListener;
import chat.freeuni.com.freeunichat.adapters.IUsersListHolder;
import chat.freeuni.com.freeunichat.adapters.RecentChatAdapter;
import chat.freeuni.com.freeunichat.chat.ChatEventListener;
import chat.freeuni.com.freeunichat.chat.TestChatTransport;
import chat.freeuni.com.freeunichat.database.DatabaseAccess;
import chat.freeuni.com.freeunichat.models.Message;
import chat.freeuni.com.freeunichat.models.RecentChatEntryModel;
import chat.freeuni.com.freeunichat.models.User;


public class RecentPageFragment extends Fragment implements FriendsListListener, IUsersListHolder, ChatEventListener {


    List<RecentChatEntryModel> recentChatEntrysDataSet = new ArrayList<>();
    List<User> usersDataSet;
    Map<Integer, Integer> userRecentEntrysMapping = new HashMap<Integer, Integer>();

    RecyclerView recentHistoryRv;
    RecentChatAdapter recentChatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_recent_page, container, false);
        recentHistoryRv = (RecyclerView)returnView.findViewById(R.id.recent_history_rv);
        recentHistoryRv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recentHistoryRv.setLayoutManager(mLayoutManager);

        recentChatAdapter = new RecentChatAdapter(getActivity(),
                recentChatEntrysDataSet, this);
        recentHistoryRv.setAdapter(recentChatAdapter);/* */

        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TestChatTransport.getChatManager().addChatEventListener(this);

    }

    //downloads recent chat history and sends it for display
    private void loadRecentChatList(){
        Context context = getApplicationContext();
        if (context == null)
            return ;
        DatabaseAccess dbAcc = new DatabaseAccess(context);
        dbAcc.open();
        List<RecentChatEntryModel> recChatEntrys = dbAcc.getRecentChatHistory();
        dbAcc.close();
        setRecentChatEntrys(recChatEntrys);
    }

    private Context getApplicationContext(){
        if (getActivity() == null)
            return null;
        return getActivity().getApplicationContext();
    }


    private void setRecentChatEntrys(List<RecentChatEntryModel> recentChatEntrys){
        recentChatEntrysDataSet.clear();
        if (usersDataSet == null || usersDataSet.isEmpty())
            return;
        recentChatEntrysDataSet.addAll(recentChatEntrys);
        recentChatAdapter.notifyDataSetChanged();
    }


    public User getUserById(int id){
        if (usersDataSet == null || usersDataSet.isEmpty())
            return null;

        for (User curUser: usersDataSet) {
            if (curUser.id == id)
                return curUser;
        }

        return null;
    }


    /*****************************************/
    /**********Friends List Operations *******/
     /*****************************************/

    //gets called only once loads recent chat list afterwards
    @Override
    public void setFriendList(List<User> friendList){
        usersDataSet = friendList;
        loadRecentChatList();
    }

    @Override
    public void friendsListUpdated() {
        loadRecentChatList();
    }

    //position in friends list
    @Override
    public void singleFriendUpdated(int position, User newUser) {
        if (recentChatEntrysDataSet == null)
            return;

        for (int i = 0; i < recentChatEntrysDataSet.size(); i++){
            if (recentChatEntrysDataSet.get(i).profileId == newUser.id){
                recentChatAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void contactStatusChanged(Message m) {

    }

    @Override
    public void incomingMessage(Message m) {
        loadRecentChatList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        TestChatTransport.getChatManager().removeEventListener(this);

    }
}
