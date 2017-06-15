package chat.freeuni.com.freeunichat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import chat.freeuni.com.freeunichat.adapters.FriendsListListener;
import chat.freeuni.com.freeunichat.models.User;


public class RecentPageFragment extends Fragment implements FriendsListListener {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recent_page, container, false);
    }

    /*****************************************/
    /**********Friends List Operations *******/
    /*****************************************/
    @Override
    public void setFriendList(List<User> friendList){

    }

    @Override
    public void friendsListUpdated() {

    }

    @Override
    public void singleFriendUpdated(int position, User newUser) {

    }
}
