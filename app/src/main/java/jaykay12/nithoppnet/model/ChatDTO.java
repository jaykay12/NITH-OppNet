package jaykay12.nithoppnet.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by jaykay12 on 31/3/18.
 */

public class ChatDTO implements Serializable {

    private String message;
    private String sentBy;
    private long localTimestamp;
    private String fromIP;
    private int port;
    private boolean isMyChat = false;

    public boolean isMyChat() {
        return isMyChat;
    }

    public void setMyChat(boolean myChat) {
        isMyChat = myChat;
    }

    public String getMessage() {
        return message;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public long getLocalTimestamp() {
        return localTimestamp;
    }

    public void setLocalTimestamp(long localTimestamp) {
        this.localTimestamp = localTimestamp;
    }

    public String getFromIP() {
        return fromIP;
    }

    public void setFromIP(String fromIP) {
        this.fromIP = fromIP;
    }

    @Override
    public String toString() {
        String stringRep = (new Gson()).toJson(this);
        return stringRep;
    }

    public static ChatDTO fromJSON(String jsonRep) {
        Gson gson = new Gson();
        ChatDTO chatObject = gson.fromJson(jsonRep, ChatDTO.class);
        return chatObject;
    }
}
