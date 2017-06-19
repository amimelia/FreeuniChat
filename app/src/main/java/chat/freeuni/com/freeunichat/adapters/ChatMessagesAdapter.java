package chat.freeuni.com.freeunichat.adapters;

import android.content.Context;
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

import chat.freeuni.com.freeunichat.R;
import chat.freeuni.com.freeunichat.helpers.MessagesOpener;
import chat.freeuni.com.freeunichat.models.Message;
import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/18/2017.
 */

public class ChatMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mDataset;
    Context context;
    User userChatTo;

    public static class ChatMessagesViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextTo;
        TextView messageTextFrom;
        ImageView profilePicTo;
        ImageView profilePicFrom;
        View messageTo;
        View messageFrom;
        public ChatMessagesViewHolder(View v) {
            super(v);
            messageTextTo = (TextView) v.findViewById(R.id.message_text_to);
            messageTextFrom = (TextView) v.findViewById(R.id.message_text_from);
            profilePicFrom = (ImageView) v.findViewById(R.id.profile_picture_from);
            profilePicTo = (ImageView) v.findViewById(R.id.profile_picture_to);

            messageTo = v.findViewById(R.id.message_to);
            messageFrom = v.findViewById(R.id.message_from);
        }
    }

    public static class SingleMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextTo;
        TextView messageTextFrom;
        View messageTo;
        View messageFrom;
        public SingleMessageViewHolder(View v) {
            super(v);
            messageTextTo = (TextView) v.findViewById(R.id.message_text_to);
            messageTextFrom = (TextView) v.findViewById(R.id.message_text_from);
            messageTo = v.findViewById(R.id.message_to);
            messageFrom = v.findViewById(R.id.message_from);

        }
    }

    public ChatMessagesAdapter(Context context, List<Message> myDataset, User chatTo) {
        mDataset = myDataset;
        this.context = context;
        this.userChatTo = chatTo;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataset.size() - 1)
            return 0;
        if (mDataset.get(position + 1).isSent == mDataset.get(position).isSent)
            return 1;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: return new ChatMessagesViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_header, parent, false));
            case 1: return new SingleMessageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ChatMessagesViewHolder viewHolder1 = (ChatMessagesViewHolder)holder;
                displayChatHead(viewHolder1, position);
                break;

            case 1:
                SingleMessageViewHolder viewHolder2 = (SingleMessageViewHolder)holder;
                displayChatMessage(viewHolder2, position);
                break;
        }
    }

    private void displayChatHead(ChatMessagesViewHolder vh, int position){
        Message curMessage = mDataset.get(position);
        //message came from him
        if (curMessage.isSent == 0){
            vh.messageTo.setVisibility(View.GONE);
            vh.messageTextFrom.setText(curMessage.message);
            vh.profilePicFrom.setImageBitmap(userChatTo.avatarImgBitmap);
        }else{
            vh.messageFrom.setVisibility(View.GONE);
            vh.messageTextTo.setText(curMessage.message);
            vh.profilePicTo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mypic));
        }
    }

    private void displayChatMessage(SingleMessageViewHolder vh,  int position){
        Message curMessage = mDataset.get(position);
        //message came from him
        if (curMessage.isSent == 0){
            vh.messageTo.setVisibility(View.GONE);
            vh.messageTextFrom.setText(curMessage.message);
        }else{
            vh.messageFrom.setVisibility(View.GONE);
            vh.messageTextTo.setText(curMessage.message);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}