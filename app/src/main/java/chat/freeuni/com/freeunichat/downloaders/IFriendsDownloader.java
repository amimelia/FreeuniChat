package chat.freeuni.com.freeunichat.downloaders;

import java.util.List;

import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/14/2017.
 */

public interface IFriendsDownloader {
    public void friendsListDownloaded(List<User> friendsList);
}
