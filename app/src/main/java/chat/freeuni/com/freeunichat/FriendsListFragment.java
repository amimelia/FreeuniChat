package chat.freeuni.com.freeunichat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import chat.freeuni.com.freeunichat.downloaders.FriendsListDownloader;
import chat.freeuni.com.freeunichat.downloaders.IFriendsDownloader;
import chat.freeuni.com.freeunichat.models.User;


public class FriendsListFragment extends Fragment implements IFriendsDownloader {

    FriendsListDownloader downloader;
    View downloaderProgressBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_friends_list, container, false);
        downloaderProgressBar = returnView.findViewById(R.id.progress_bar_displayer);
        return returnView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        startFriendsListDownloading();

    }


    private void startFriendsListDownloading(){
        downloaderProgressBar.setVisibility(View.VISIBLE);
        downloader = new FriendsListDownloader(getContext(), this);
        downloader.execute(getResources().getString(R.string.friends_list_json_url));
    }


    @Override
    public void friendsListDownloaded(List<User> friendsList) {
        downloaderProgressBar.setVisibility(View.GONE);
        if (friendsList == null)
            return;
        Toast.makeText(getContext(), "Found + " + friendsList.size(), Toast.LENGTH_SHORT).show();
    }
}
