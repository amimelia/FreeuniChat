package chat.freeuni.com.freeunichat.downloaders;

import chat.freeuni.com.freeunichat.models.User;

/**
 * Created by melia on 6/15/2017.
 */

public interface DownloadPicturesReceiver {
    public void pictureDownloaded(User user);
}
