package jaykay12.nithoppnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jaykay12.nithoppnet.model.ChatDTO;
import jaykay12.nithoppnet.transfer.DataSender;
import jaykay12.nithoppnet.utils.ConnectionUtils;
import jaykay12.nithoppnet.utils.Utility;

public class ChatActivity extends AppCompatActivity {

    public static final String ACTION_CHAT_RECEIVED = "jaykay12.nithoppnet.chatreceived";
    public static final String KEY_CHAT_DATA = "chat_data_key";

    public static final String KEY_CHATTING_WITH = "chattingwith";
    public static final String KEY_CHAT_IP = "chatterip";
    public static final String KEY_CHAT_PORT = "chatterport";

    EditText etChat;
    RecyclerView chatListHolder;
    private List<ChatDTO> chatList;
    private ChatListAdapter chatListAdapter;

    private String chattingWith;
    private String destIP;
    private int destPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initialize();

        chatListHolder = (RecyclerView) findViewById(R.id.rvChatList);
        etChat = (EditText) findViewById(R.id.etChatBox);

        chatList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatList);
        chatListHolder.setAdapter(chatListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        chatListHolder.setLayoutManager(linearLayoutManager);
    }

    private void initialize() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHAT_RECEIVED);
        LocalBroadcastManager.getInstance(ChatActivity.this).registerReceiver(chatReceiver, filter);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(ChatActivity.this, "Invalid arguments to open chat",Toast.LENGTH_LONG);
            finish();
        }

        chattingWith = extras.getString(KEY_CHATTING_WITH);
        destIP = extras.getString(KEY_CHAT_IP);
        destPort = extras.getInt(KEY_CHAT_PORT);

        setToolBarTitle("Chat with " + chattingWith);
    }

    public void SendChatInfo(View v) {
        String message = etChat.getText().toString();

        ChatDTO myChat = new ChatDTO();
        myChat.setPort(ConnectionUtils.getPort(ChatActivity.this));
        myChat.setFromIP(Utility.getString(ChatActivity.this, "myip"));
        myChat.setLocalTimestamp(System.currentTimeMillis());
        myChat.setMessage(message);
        myChat.setSentBy(chattingWith);
        myChat.setMyChat(true);
        DataSender.sendChatInfo(ChatActivity.this, destIP, destPort, myChat);

        etChat.setText("");
//        chatListHolder.smoothScrollToPosition(chatList.size() - 1);
        updateChatView(myChat);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_CHAT_RECEIVED:
                    ChatDTO chat = (ChatDTO) intent.getSerializableExtra(KEY_CHAT_DATA);
                    chat.setMyChat(false);
                    updateChatView(chat);
                    break;
                default:
                    break;
            }
        }
    };

    private void updateChatView(ChatDTO chatObj) {
        chatList.add(chatObj);
        chatListAdapter.notifyDataSetChanged();
        chatListHolder.smoothScrollToPosition(chatList.size() - 1);
    }

    private class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatHolder> {

        private List<ChatDTO> chatList;

        ChatListAdapter(List<ChatDTO> chatList) {
            this.chatList = chatList;
        }

        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_mine,
                        parent, false);
                return new ChatHolder(itemView);
            } else {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_others,
                        parent, false);
                return new ChatHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(ChatHolder holder, int position) {
            holder.bind(chatList.get(position));
        }

        @Override
        public int getItemCount() {
            return chatList == null ? 0 : chatList.size();
        }

        @Override
        public int getItemViewType(int position) {
            ChatDTO chatObj = chatList.get(position);
            return (chatObj.isMyChat() ? 0 : 1);
        }

        class ChatHolder extends RecyclerView.ViewHolder {
            TextView tvChatMessage;

            public ChatHolder(View itemView) {
                super(itemView);
                tvChatMessage = (TextView) itemView.findViewById(R.id.tvChatMessage);
            }

            public void bind(ChatDTO singleChat) {
                tvChatMessage.setText(singleChat.getMessage());
            }
        }
    }

    private void setToolBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }
}
