package jaykay12.nithoppnet.transfer;

/**
 * Created by jaykay12 on 31/3/18.
 */

public interface TransferConstants {

    int INITIAL_DEFAULT_PORT = 8998;

    int CLIENT_DATA = 3001;
    int CLIENT_DATA_WD = 3003;
    int CLIENT_LOST = 3002;
    int CHAT_DATA = 3004;
    int CHAT_REQUEST_SENT = 3011;
    int CHAT_REQUEST_ACCEPTED = 3012;
    int CHAT_REQUEST_REJECTED = 3013;

    String TYPE_REQUEST = "request";
    String TYPE_RESPONSE = "response";

    String KEY_MY_IP = "myip";
    String KEY_BUDDY_NAME = "buddyname";
    String KEY_PORT_NUMBER = "portnumber";
    String KEY_DEVICE_STATUS = "devicestatus";
    String KEY_USER_NAME = "username";
    String KEY_WIFI_IP = "wifiip";
}
