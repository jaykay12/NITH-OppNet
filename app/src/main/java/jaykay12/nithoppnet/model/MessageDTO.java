package jaykay12.nithoppnet.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by jaykay12 on 15/4/18.
 */

public class MessageDTO implements Serializable{
    public static final String TABLE_NAME = "messages";

    public static final String MESSAGE_ID = "messageid";
    public static final String SENDER_ID = "senderid";
    public static final String SENDER_NAME = "sendername";
    public static final String MESSAGE_CONTENT = "messagecontent";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String SENDER_AVATAR = "senderavatar";

    private String message_id;
    private String sender_id;
    private String sender_name;
    private String content;
    private String timestamp;
    private Integer sender_avatar;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + MESSAGE_ID + " TEXT PRIMARY KEY,"
                    + SENDER_ID + " TEXT,"
                    + SENDER_NAME + " TEXT,"
                    + MESSAGE_CONTENT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + SENDER_AVATAR + " INTEGER"
                    + ")";

    public MessageDTO() {

    }

    public MessageDTO(String message_id, String sender_id, String sender_name, String content, String timestamp, Integer sender_avatar) {
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.content = content;
        this.timestamp = timestamp;
        this.sender_avatar = sender_avatar;
    }

    public Integer getSender_avatar() { return sender_avatar; }

    public String getMessage_id() {
        return message_id;
    }

    public void setSender_avatar(Integer sender_avatar){ this.sender_avatar = sender_avatar; }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        String stringRep = (new Gson()).toJson(this);
        return stringRep;
    }

    public static MessageDTO fromJSON(String jsonRep) {
        Gson gson = new Gson();
        MessageDTO messageObject = gson.fromJson(jsonRep, MessageDTO.class);
        return messageObject;
    }
}