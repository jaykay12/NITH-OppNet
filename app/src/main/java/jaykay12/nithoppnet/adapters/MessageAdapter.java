package jaykay12.nithoppnet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.model.MessageDTO;

/**
 * Created by jaykay12 on 16/4/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private Context mContext;
    private List<MessageDTO> messageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView messageID, senderName, messageContents;
        public ImageView senderAvatar;

        public MyViewHolder(View view){
            super(view);
            messageID = (TextView) view.findViewById(R.id.tvMessageID);
            senderName = (TextView) view.findViewById(R.id.tvSenderName);
            messageContents = (TextView) view.findViewById(R.id.tvMessageContents);
            senderAvatar = (ImageView)view.findViewById(R.id.ivAvatarDisplay);
        }
    }

    public MessageAdapter(Context mContext, List<MessageDTO> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageDTO message = messageList.get(position);
        holder.messageID.setText(message.getMessage_id());
        holder.senderName.setText(message.getSender_name());
        holder.messageContents.setText(message.getContent());
        holder.senderAvatar.setImageResource(message.getSender_avatar());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
