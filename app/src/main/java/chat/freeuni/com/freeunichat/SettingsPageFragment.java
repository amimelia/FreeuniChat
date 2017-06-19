package chat.freeuni.com.freeunichat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class SettingsPageFragment extends Fragment {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings_page, container, false);
        Switch stBarNotif = (Switch)v.findViewById(R.id.status_bar_notifications);
        Switch incMessageSound = (Switch)v.findViewById(R.id.inc_message_sound);
        stBarNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enibleStatusBarNotifications(isChecked);
            }
        });

        incMessageSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enibleIncomingMessageSound(isChecked);
            }
        });
        return v;
    }

    private void enibleIncomingMessageSound(boolean isChecked){

    }

    private void enibleStatusBarNotifications(boolean isChecked){

    }
}
