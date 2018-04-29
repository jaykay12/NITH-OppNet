package jaykay12.nithoppnet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jaykay12.nithoppnet.model.MessageDTO;

/**
 * Created by jaykay12 on 14/4/18.
 */

public class DatabaseOperations extends SQLiteOpenHelper{
    SQLiteDatabase db;
    private static String DB_Name = "oppnet.db";

    public DatabaseOperations(Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageDTO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MessageDTO.TABLE_NAME);
        onCreate(db);
    }

    public long Add_Message(String MESSAGE_ID,String SENDER_ID,String SENDER_NAME,String MESSAGE_CONTENT){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("messageid",MESSAGE_ID);
        cv.put("senderid",SENDER_ID);
        cv.put("sendername",SENDER_NAME);
        cv.put("messagecontent",MESSAGE_CONTENT);

        long r1 = db.insert(MessageDTO.TABLE_NAME,null,cv);
        db.close();
        return r1;
    }

    public List<MessageDTO> getAllMessages() {
        List<MessageDTO> messages = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + MessageDTO.TABLE_NAME + " ORDER BY " + MessageDTO.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MessageDTO message = new MessageDTO();
                message.setMessage_id(cursor.getString(cursor.getColumnIndex(MessageDTO.MESSAGE_ID)));
                message.setSender_id(cursor.getString(cursor.getColumnIndex(MessageDTO.SENDER_ID)));
                message.setSender_name(cursor.getString(cursor.getColumnIndex(MessageDTO.SENDER_NAME)));
                message.setContent(cursor.getString(cursor.getColumnIndex(MessageDTO.MESSAGE_CONTENT)));
                message.setTimestamp(cursor.getString(cursor.getColumnIndex(MessageDTO.COLUMN_TIMESTAMP)));

                messages.add(message);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return messages;
    }
}
