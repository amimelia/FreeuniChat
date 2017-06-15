package chat.freeuni.com.freeunichat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import chat.freeuni.com.freeunichat.adapters.FriendsListAdapter;
import chat.freeuni.com.freeunichat.adapters.FriendsListListener;
import chat.freeuni.com.freeunichat.database.DatabaseAccess;
import chat.freeuni.com.freeunichat.downloaders.DownloadPicturesReceiver;
import chat.freeuni.com.freeunichat.downloaders.FriendsListDownloader;
import chat.freeuni.com.freeunichat.downloaders.IFriendsDownloader;
import chat.freeuni.com.freeunichat.downloaders.UsersPicturesDownloader;
import chat.freeuni.com.freeunichat.helpers.FirstRunChecker;
import chat.freeuni.com.freeunichat.models.User;


public class FriendsListFragment extends Fragment implements IFriendsDownloader, DownloadPicturesReceiver {

    FriendsListDownloader downloader;
    View downloaderProgressBar;
    RecyclerView mfriendsListRecyclerView;
    FriendsListAdapter friendsListAdapter;
    List<User> usersDataSet = new ArrayList<>();
    List<FriendsListListener> friendsListListeners = new ArrayList<>();

    public void addFriendsListListener(FriendsListListener listener){
        friendsListListeners.add(listener);
        listener.setFriendList(usersDataSet);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_friends_list, container, false);
        downloaderProgressBar = returnView.findViewById(R.id.progress_bar_displayer);
        mfriendsListRecyclerView = (RecyclerView) returnView.findViewById(R.id.friends_list);
        mfriendsListRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mfriendsListRecyclerView.setLayoutManager(mLayoutManager);

        friendsListAdapter = new FriendsListAdapter(getActivity().getApplicationContext(), usersDataSet);
        mfriendsListRecyclerView.setAdapter(friendsListAdapter);/* */

        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadFriendsList();


    }

    private void startFriendsListDownloading(){
        downloaderProgressBar.setVisibility(View.VISIBLE);
        downloader = new FriendsListDownloader(getContext(), this);
        downloader.execute(getResources().getString(R.string.friends_list_json_url));
    }

    private void loadFriendsList(){
        if (checkFirstRun()){
            startFriendsListDownloading();
        }else{
            loadFriendsListFromDb();
            downloadFriendsPictures();
        }
    }

    //checks if its first run if its sets isFirstRun - false
    private boolean checkFirstRun(){
        return FirstRunChecker.isFirstRun(getContext());
    }

    private void loadFriendsListFromDb(){
        DatabaseAccess dbAcc = new DatabaseAccess(getContext());
        dbAcc.open();
        List<User> friendsList = dbAcc.getFriends();
        dbAcc.close();
        updateUsersDataSet(friendsList);
    }

    @Override
    public void friendsListDownloaded(List<User> friendsList) {
        downloaderProgressBar.setVisibility(View.GONE);
        if (friendsList == null)
            return;
        addFriendsListToDbAsync(friendsList);
        FirstRunChecker.removeFirstRunFlag(getContext());
        updateUsersDataSet(friendsList);
        downloadFriendsPictures();
    }

    private void addFriendsListToDbAsync(final List<User> friendsList){
        final Context curContext = getContext();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                addFriendsListToDb(friendsList, curContext);
            }
        });

    }

    private void addFriendsListToDb(List<User> friendsList, final Context context){
        DatabaseAccess dbAcc = new DatabaseAccess(context);
        dbAcc.open();
        for (User curUser : friendsList){
            dbAcc.saveNewFriend(curUser);
        }
        dbAcc.close();
    }

    private void updateUsersDataSet(List<User> friendsList){
        usersDataSet.clear();
        usersDataSet.addAll(friendsList);
        dataSetChanged();
    }

    private void dataSetChanged(){
        for (FriendsListListener listener: friendsListListeners)
            listener.friendsListUpdated();
        friendsListAdapter.notifyDataSetChanged();
    }

    //starts pictures downloader
    private void downloadFriendsPictures(){
        UsersPicturesDownloader picsDownloader = new UsersPicturesDownloader(getContext(), usersDataSet, this);
        picsDownloader.execute("");
    }

    @Override
    public void pictureDownloaded(User user){
        //called when picture is downloaded for user
        int indexOfUser = usersDataSet.indexOf(user);
        friendsListAdapter.notifyItemChanged(indexOfUser);
        for (FriendsListListener listener: friendsListListeners)
            listener.singleFriendUpdated(indexOfUser, user);
    }
}
