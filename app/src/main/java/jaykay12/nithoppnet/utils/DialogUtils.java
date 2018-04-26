package jaykay12.nithoppnet.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import jaykay12.nithoppnet.ChatActivity;
import jaykay12.nithoppnet.OppNetArena.DatabaseOperations;
import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.model.ChatDTO;
import jaykay12.nithoppnet.model.DeviceDTO;
import jaykay12.nithoppnet.model.MessageDTO;
import jaykay12.nithoppnet.transfer.DataSender;

import static jaykay12.nithoppnet.ChatActivity.KEY_CHATTING_WITH;

/**
 * Created by jaykay12 on 31/3/18.
 */

public class DialogUtils {

    public static final int CODE_PICK_IMAGE = 21;

    public static AlertDialog getServiceSelectionDialog(final Activity activity, final DeviceDTO selectedDevice) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(selectedDevice.getDeviceName());
        String[] types = {"Act as Helper", "Chat"};
        alertDialog.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        String message = "TESTING";

                        List<MessageDTO> messages= new DatabaseOperations(activity).getAllMessages();

                        ChatDTO myChat = new ChatDTO();
                        myChat.setPort(ConnectionUtils.getPort(activity));
                        myChat.setFromIP(Utility.getString(activity, "myip"));
                        myChat.setLocalTimestamp(System.currentTimeMillis());
                        myChat.setMessages(messages);

                        Log.v("### Send",message);

//                        myChat.setSentBy(chattingWith);
                        myChat.setMyChat(true);
                        DataSender.sendChatInfo(activity, selectedDevice.getIp(), selectedDevice.getPort(), myChat);

                        break;
                    case 1:
                        DataSender.sendChatRequest(activity, selectedDevice.getIp(), selectedDevice.getPort());
                        Toast.makeText(activity, "chat request " + "sent",Toast.LENGTH_LONG).show();
                        break;
                }
            }

        });

        return (alertDialog.create());
    }

    public static AlertDialog getChatRequestDialog(final Activity activity, final DeviceDTO requesterDevice) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        String chatRequestTitle = activity.getString(R.string.chat_request_title);
        chatRequestTitle = String.format(chatRequestTitle, requesterDevice.getPlayerName() + "(" + requesterDevice.getDeviceName() + ")");
        alertDialog.setTitle(chatRequestTitle);
        String[] types = {"Accept", "Reject"};
        alertDialog.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    //Request accepted
                    case 0:
                        openChatActivity(activity, requesterDevice);
                        Toast.makeText(activity, "Chat request " + "accepted",Toast.LENGTH_LONG).show();
                        DataSender.sendChatResponse(activity, requesterDevice.getIp(), requesterDevice.getPort(), true);
                        break;
                    // Request rejected
                    case 1:
                        DataSender.sendChatResponse(activity, requesterDevice.getIp(), requesterDevice.getPort(), false);
                        Toast.makeText(activity, "Chat request " + "rejected",Toast.LENGTH_LONG).show();
                        break;
                }
            }

        });

        return (alertDialog.create());
    }

    public static void openChatActivity(Activity activity, DeviceDTO device) {
        Intent chatIntent = new Intent(activity, ChatActivity.class);
        chatIntent.putExtra(ChatActivity.KEY_CHAT_IP, device.getIp());
        chatIntent.putExtra(ChatActivity.KEY_CHAT_PORT, device.getPort());
        chatIntent.putExtra(KEY_CHATTING_WITH, device.getPlayerName());
        activity.startActivity(chatIntent);
    }

}
