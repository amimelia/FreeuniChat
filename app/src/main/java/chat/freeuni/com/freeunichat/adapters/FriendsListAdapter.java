package chat.freeuni.com.freeunichat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

import chat.freeuni.com.freeunichat.R;
import chat.freeuni.com.freeunichat.helpers.ImageConverterHelper;
import chat.freeuni.com.freeunichat.helpers.MessagesOpener;
import chat.freeuni.com.freeunichat.models.User;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {

    private List<User> mDataset;
    private int[] statusPic = {R.drawable.ic_offline_24dp, R.drawable.ic_online_24dp};
    Context context;

    public static class FriendsListViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicture;
        ImageView statusPic;
        TextView nameTextView;
        View mainView;
        public FriendsListViewHolder(View v) {
            super(v);
            profilePicture = (ImageView) v.findViewById(R.id.friend_picture);
            statusPic = (ImageView) v.findViewById(R.id.friend_status);
            nameTextView = (TextView) v.findViewById(R.id.friend_name);
            this.mainView = v;
        }
    }

    public FriendsListAdapter(Context context, List<User> myDataset) {
        mDataset = myDataset;
        this.context = context;
        // ContextCompat.getDrawable(context, R.drawable.anyone);
    }

    @Override
    public FriendsListAdapter.FriendsListViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.firends_list_item, parent, false);
        FriendsListViewHolder vh = new FriendsListViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendsListViewHolder holder, int position) {
        final User userToDisplay = mDataset.get(position);
        holder.nameTextView.setText(userToDisplay.displayName);
        int statusDrawable = userToDisplay.isActive ? statusPic[1] : statusPic[0];
        holder.statusPic.setImageDrawable(ContextCompat.getDrawable(context, statusDrawable));

        if (userToDisplay.avatarImgBitmap == null)
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_24dp));
        else
            holder.profilePicture.setImageBitmap(userToDisplay.avatarImgBitmap);/**/

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagesOpener.openChatTo(context, userToDisplay, userToDisplay.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}