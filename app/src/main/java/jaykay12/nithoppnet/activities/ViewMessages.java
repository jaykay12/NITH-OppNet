package jaykay12.nithoppnet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jaykay12.nithoppnet.adapters.MessageAdapter;
import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.database.DatabaseOperations;
import jaykay12.nithoppnet.model.MessageDTO;

public class ViewMessages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<MessageDTO> messageDTOList;
    DatabaseOperations dbobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_view);

        recyclerView = (RecyclerView)findViewById(R.id.rvMessages);
        dbobj = new DatabaseOperations(this);

        messageDTOList = new ArrayList<>();
        messageDTOList = dbobj.getAllMessages();
        messageAdapter = new MessageAdapter(this,messageDTOList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messageAdapter);

    }
}
