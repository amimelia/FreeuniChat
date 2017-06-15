package chat.freeuni.com.freeunichat.adapters;

import java.util.List;

import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/16/2017.
 */

public interface FriendsListListener {
    public void setFriendList(List<User> friendList);
    public void friendsListUpdated();
    public void singleFriendUpdated(int position, User newUser);

}
