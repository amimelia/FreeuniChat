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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.freeuni.com.freeunichat.adapters.FriendsListAdapter;
import chat.freeuni.com.freeunichat.adapters.FriendsListListener;
import chat.freeuni.com.freeunichat.adapters.IUsersListHolder;
import chat.freeuni.com.freeunichat.adapters.RecentChatAdapter;
import chat.freeuni.com.freeunichat.database.DatabaseAccess;
import chat.freeuni.com.freeunichat.models.RecentChatEntryModel;
import chat.freeuni.com.freeunichat.models.User;


public class RecentPageFragment extends Fragment implements FriendsListListener, IUsersListHolder {


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

        recentChatAdapter = new RecentChatAdapter(getActivity().getApplicationContext(),
                recentChatEntrysDataSet, this);
        recentHistoryRv.setAdapter(recentChatAdapter);/* */

        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadRecentChatList();
    }

    //downloads recent chat history and sends it for display
    private void loadRecentChatList(){
        DatabaseAccess dbAcc = new DatabaseAccess(getContext());
        dbAcc.open();
        List<RecentChatEntryModel> recChatEntrys = dbAcc.getRecentChatHistory();
        dbAcc.close();
        setRecentChatEntrys(recChatEntrys);
    }


    private void setRecentChatEntrys(List<RecentChatEntryModel> recentChatEntrys){
        recentChatEntrysDataSet.clear();
        if (usersDataSet == null || usersDataSet.isEmpty())
            return;
        recentChatEntrysDataSet.addAll(recentChatEntrys);
        mapFriendsList();
        recentChatAdapter.notifyDataSetChanged();
        //TODO call notify dataset changed
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
    @Override
    public void setFriendList(List<User> friendList){
        usersDataSet = friendList;
        mapFriendsList();
    }

    @Override
    public void friendsListUpdated() {
        mapFriendsList();
    }

    public void mapFriendsList(){
        if (usersDataSet == null || usersDataSet.isEmpty())
            return;
        if (recentChatEntrysDataSet == null || recentChatEntrysDataSet.isEmpty())
            return;
        userRecentEntrysMapping.clear();
       for (int i )

    }

    //position in friends list
    @Override
    public void singleFriendUpdated(int position, User newUser) {

    }
}
