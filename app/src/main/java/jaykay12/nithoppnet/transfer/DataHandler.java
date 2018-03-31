package jaykay12.nithoppnet.transfer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import jaykay12.nithoppnet.ChatActivity;
import jaykay12.nithoppnet.FindPeerActivity;
import jaykay12.nithoppnet.database.DBAdapter;
import jaykay12.nithoppnet.model.ChatDTO;
import jaykay12.nithoppnet.model.DeviceDTO;

/**
 * Created by jaykay12 on 31/3/18.
 */

public class DataHandler{

        public static final String DEVICE_LIST_CHANGED = "device_list_updated";

        public static final String CHAT_REQUEST_RECEIVED = "chat_request_received";
        public static final String CHAT_RESPONSE_RECEIVED = "chat_response_received";
        public static final String KEY_CHAT_REQUEST = "chat_requester_or_responder";
        public static final String KEY_IS_CHAT_REQUEST_ACCEPTED = "is_chat_request_Accespter";


        private ITransferable data;
        private Context mContext;
        private String senderIP;
        private LocalBroadcastManager broadcaster;
        private DBAdapter dbAdapter = null;

        DataHandler(Context context, String senderIP, ITransferable data) {
            this.mContext = context;
            this.data = data;
            this.senderIP = senderIP;
            this.dbAdapter = DBAdapter.getInstance(mContext);
            this.broadcaster = LocalBroadcastManager.getInstance(mContext);
        }

        public void process() {
            if (data.getRequestType().equalsIgnoreCase(TransferConstants.TYPE_REQUEST)) {
                processRequest();
            } else {
                processResponse();
            }
        }

        private void processRequest() {
            switch (data.getRequestCode()) {
                case TransferConstants.CLIENT_DATA:
                    processPeerDeviceInfo();
                    DataSender.sendCurrentDeviceData(mContext, senderIP, dbAdapter.getDevice(senderIP).getPort(), false);
                    break;
                case TransferConstants.CLIENT_DATA_WD:
                    processPeerDeviceInfo();
                    Intent intent = new Intent(FindPeerActivity.FIRST_DEVICE_CONNECTED);
                    intent.putExtra(FindPeerActivity.KEY_FIRST_DEVICE_IP, senderIP);
                    broadcaster.sendBroadcast(intent);
                    break;
                case TransferConstants.CHAT_REQUEST_SENT:
                    processChatRequestReceipt();
                default:
                    break;
            }
        }

        private void processResponse() {
            switch (data.getRequestCode()) {
                case TransferConstants.CLIENT_DATA:
                case TransferConstants.CLIENT_DATA_WD:
                    processPeerDeviceInfo();
                    break;
                case TransferConstants.CHAT_DATA:
                    processChatData();
                    break;
                case TransferConstants.CHAT_REQUEST_ACCEPTED:
                    processChatRequestResponse(true);
                    break;
                case TransferConstants.CHAT_REQUEST_REJECTED:
                    processChatRequestResponse(false);
                    break;
                default:
                    break;
            }
        }

        private void processChatRequestReceipt() {
            String chatRequesterDeviceJSON = data.getData();
            DeviceDTO chatRequesterDevice = DeviceDTO.fromJSON(chatRequesterDeviceJSON);
            chatRequesterDevice.setIp(senderIP);

            Intent intent = new Intent(CHAT_REQUEST_RECEIVED);
            intent.putExtra(KEY_CHAT_REQUEST, chatRequesterDevice);
            broadcaster.sendBroadcast(intent);
        }

        private void processChatRequestResponse(boolean isRequestAccepted) {
            String chatResponderDeviceJSON = data.getData();
            DeviceDTO chatResponderDevice = DeviceDTO.fromJSON(chatResponderDeviceJSON);
            chatResponderDevice.setIp(senderIP);

            Intent intent = new Intent(CHAT_RESPONSE_RECEIVED);
            intent.putExtra(KEY_CHAT_REQUEST, chatResponderDevice);
            intent.putExtra(KEY_IS_CHAT_REQUEST_ACCEPTED, isRequestAccepted);
            broadcaster.sendBroadcast(intent);
        }

        private void processChatData() {
            String chatJSON = data.getData();
            ChatDTO chatObject = ChatDTO.fromJSON(chatJSON);
            chatObject.setFromIP(senderIP);
            //Save in db if needed here
            Intent chatReceivedIntent = new Intent(ChatActivity.ACTION_CHAT_RECEIVED);
            chatReceivedIntent.putExtra(ChatActivity.KEY_CHAT_DATA, chatObject);
            broadcaster.sendBroadcast(chatReceivedIntent);
        }

        private void processPeerDeviceInfo() {
            String deviceJSON = data.getData();
            DeviceDTO device = DeviceDTO.fromJSON(deviceJSON);
            device.setIp(senderIP);
            long rowid = dbAdapter.addDevice(device);

            if (rowid > 0) {
                Log.d("DXDX", Build.MANUFACTURER + " received: " + deviceJSON);
            } else {
                Log.e("DXDX", Build.MANUFACTURER + " can't save: " + deviceJSON);
            }

            Intent intent = new Intent(DEVICE_LIST_CHANGED);
            broadcaster.sendBroadcast(intent);
        }

}
