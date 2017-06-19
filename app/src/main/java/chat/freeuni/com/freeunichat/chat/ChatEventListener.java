package chat.freeuni.com.freeunichat.chat;

import chat.freeuni.com.freeunichat.models.Message;

/**
 * Created by melia on 6/18/2017.
 */

public interface ChatEventListener {

    /**
     * იძახება როდესაც იცვლება რომელიმე მეგობრის სტატუსი
     */
    public void contactStatusChanged(Message m);
    /**
     * იძახება როდესაც ვიღებთ შემომავალ შეტყობინებას რომელიმე მეგობრისგან
     */
    public void incomingMessage(Message m);

}
