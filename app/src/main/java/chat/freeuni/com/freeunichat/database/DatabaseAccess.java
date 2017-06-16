package chat.freeuni.com.freeunichat.database;

/**
 * Created by melia on 6/15/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import chat.freeuni.com.freeunichat.models.MessageStatuses;
import chat.freeuni.com.freeunichat.models.RecentChatEntryModel;
import chat.freeuni.com.freeunichat.models.User;

public class DatabaseAccess {

    public static String DateFormat = "YYYY-MM-DD HH:MM:SS";

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM users", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            friends.add(getFriendFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return friends;
    }

    private User getFriendFromCursor(Cursor cursor) {
        User c = new User();
        c.id = cursor.getInt(1);
        c.displayName = cursor.getString(2);
        c.phoneNumber = cursor.getInt(3);
        c.avatarImgUrl = cursor.getString(4);
        c.about = cursor.getString(5);
        return c;
    }

    public List<RecentChatEntryModel> getRecentChatHistory(){
        List<RecentChatEntryModel> recent = new ArrayList<>();

        Cursor cursor = database.rawQuery(getRecentChatSqlQuery(), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recent.add(getRecentChatEntry(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recent;
    }

    private RecentChatEntryModel getRecentChatEntry(Cursor cursor ){
        RecentChatEntryModel entry = new RecentChatEntryModel();

        entry.messageStatus = MessageStatuses.values()[cursor.getInt(5)];
        entry.recentMessage = cursor.getString(2);
        entry.profileId = cursor.getInt(1);

        return entry;
    }

    private String getRecentChatSqlQuery(){
        return "SELECT *\n" +
                "FROM chat_history \n" +
                "WHERE id || '-' || date in \n" +
                "(select id || '-' || max(date) as date from chat_history \n" +
                "group by chat_to) order by date desc";
    }


    public void saveNewFriend(User contact) {
        int newContactId = insertFriendToDb(contact);
    }

    /*inserts contact to db table and returns its id*/
    private int insertFriendToDb(User contact) {
        ContentValues values = new ContentValues();
        values.put("profile_id", contact.id);
        values.put("name", contact.displayName);
        values.put("mobileNumber", contact.phoneNumber);
        values.put("profilePictureUrl", contact.avatarImgUrl);
        values.put("about", contact.about);
        long retValue = database.insert("users", null, values);
        return (int) retValue;
    }
}


