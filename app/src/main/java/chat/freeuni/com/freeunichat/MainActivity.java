package chat.freeuni.com.freeunichat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chat.freeuni.com.freeunichat.chat.ChatEventListener;
import chat.freeuni.com.freeunichat.chat.TestChatTransport;
import chat.freeuni.com.freeunichat.helpers.FirstRunChecker;
import chat.freeuni.com.freeunichat.models.Message;

public class MainActivity extends AppCompatActivity implements ChatEventListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_recent_24dp,
            R.drawable.ic_people_24dp,
            R.drawable.ic_settings_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestChatTransport.getChatManager().setContext(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
        chooseSelectedTab();
    }

    private void chooseSelectedTab(){
        if (FirstRunChecker.isFirstRun(this)){
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
        }

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        RecentPageFragment recentPageFragment = new RecentPageFragment();
        FriendsListFragment fragmentFriendsList = new FriendsListFragment();
        fragmentFriendsList.addFriendsListListener(recentPageFragment);

        adapter.addFragment(recentPageFragment, "");
        adapter.addFragment(fragmentFriendsList, "");
        adapter.addFragment(new SettingsPageFragment(), "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void contactStatusChanged(Message m) {

    }

    @Override
    public void incomingMessage(Message m) {
        if (isMinimized){
            buildNotification(m);
        }
    }

    private void buildNotification(Message m){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_face_24dp)
                        .setContentTitle("New Message: ")
                        .setContentText("Click me see message");


        Intent resultIntent = new Intent(this, SingleChatActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private boolean isMinimized = false;

    @Override
    protected void onPause(){
        super.onPause();
        isMinimized = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        isMinimized = false;
    }



    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
