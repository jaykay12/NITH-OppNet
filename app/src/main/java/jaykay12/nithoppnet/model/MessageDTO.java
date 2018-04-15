package jaykay12.nithoppnet.model;

/**
 * Created by jaykay12 on 15/4/18.
 */

public class MessageDTO {
    public static final String TABLE_NAME = "messages";

    public static final String MESSAGE_ID = "messageid";
    public static final String SENDER_ID = "senderid";
    public static final String SENDER_NAME = "sendername";
    public static final String MESSAGE_CONTENT = "messagecontent";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private String message_id;
    private String sender_id;
    private String sender_name;
    private String content;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + MESSAGE_ID + " TEXT PRIMARY KEY,"
                    + SENDER_ID + " TEXT,"
                    + SENDER_NAME + " TEXT,"
                    + MESSAGE_CONTENT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public MessageDTO() {

    }

    public MessageDTO(String message_id, String sender_id, String sender_name, String content, String timestamp) {
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.content = content;
        this.timestamp = timestamp;
    }

    public static String getMessageId() {
        return MESSAGE_ID;
    }

    public static String getSenderId() {
        return SENDER_ID;
    }

    public static String getSenderName() {
        return SENDER_NAME;
    }

    public static String getMessageContent() {
        return MESSAGE_CONTENT;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public String getMessage_id() {
        return message_id;
    }

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
}