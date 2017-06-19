package chat.freeuni.com.freeunichat.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chat.freeuni.com.freeunichat.R;
import chat.freeuni.com.freeunichat.helpers.MessagesOpener;
import chat.freeuni.com.freeunichat.models.Message;
import chat.freeuni.com.freeunichat.models.MessageStatuses;
import chat.freeuni.com.freeunichat.models.RecentChatEntryModel;
import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by User on 16/06/2017.
 */

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.RecentEntryViewHolder> {

    private List<RecentChatEntryModel> mDataset;
    private int[] statusPic = {R.drawable.ic_offline_24dp, R.drawable.ic_online_24dp};
    Context context;
    IUsersListHolder usersHolder;

    public static class RecentEntryViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicture;
        ImageView statusPic;
        TextView nameTextView;
        TextView lastMessageTextView;
        View holderView;

        public RecentEntryViewHolder(View v) {
            super(v);
            profilePicture = (ImageView) v.findViewById(R.id.friend_picture);
            statusPic = (ImageView) v.findViewById(R.id.friend_status);
            nameTextView = (TextView) v.findViewById(R.id.friend_name);
            lastMessageTextView = (TextView) v.findViewById(R.id.message_text);
            this.holderView = v;
        }
    }

    public RecentChatAdapter(Context context, List<RecentChatEntryModel> myDataset, IUsersListHolder usersHolder) {
        mDataset = myDataset;
        this.context = context;
        this.usersHolder = usersHolder;
    }

    @Override
    public RecentChatAdapter.RecentEntryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_hisotry_item, parent, false);
        RecentEntryViewHolder vh = new RecentEntryViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecentEntryViewHolder holder, int position) {

        final RecentChatEntryModel curModel = mDataset.get(position);
        final User userToDisplay = usersHolder.getUserById(curModel.profileId);

        holder.nameTextView.setText(userToDisplay.displayName);
        int statusDrawable = userToDisplay.isActive ? statusPic[1] : statusPic[0];
        holder.statusPic.setImageDrawable(ContextCompat.getDrawable(context, statusDrawable));

        if (userToDisplay.avatarImgBitmap == null)
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_24dp));
        else
            holder.profilePicture.setImageBitmap(userToDisplay.avatarImgBitmap);/* */
        holder.lastMessageTextView.setText(curModel.recentMessage);

        if (curModel.messageStatus == MessageStatuses.New){
            holder.holderView.setBackgroundColor(context.getResources().getColor(R.color.newMessageHistoryColor));
        }else{

        }

        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagesOpener.openChatTo(context, userToDisplay, curModel.profileId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}