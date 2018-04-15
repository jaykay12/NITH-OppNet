package jaykay12.nithoppnet.OppNetArena;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jaykay12.nithoppnet.MainActivity;
import jaykay12.nithoppnet.R;

public class GenerateSeed extends AppCompatActivity {

    SharedPref sharedPref;
    TextView tvName,tvRollNum;
    EditText etMessage;
    DatabaseOperations dbobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        dbobj = new DatabaseOperations(this);

        Log.v("login status:",""+sharedPref.getLoginStatus());

        if(sharedPref.getLoginStatus()==false){
            Intent intent = new Intent(GenerateSeed.this,CreateProfile.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.generate_seed);

        tvName = (TextView)findViewById(R.id.tvName);
        tvRollNum = (TextView)findViewById(R.id.tvRollNum);

        Bundle bd = new Bundle();
        bd = sharedPref.getInfo();

        tvName.setText(bd.getString("name"));
        tvRollNum.setText(bd.getString("rollnum"));
    }

    public void logout(View v){
        sharedPref.setLoginStatus(false);
        Intent intent = new Intent(GenerateSeed.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void publishMessage(View v){
        String messageid, senderid, sendername, messagecontents;

        tvRollNum = (TextView)findViewById(R.id.tvRollNum);
        tvName = (TextView)findViewById(R.id.tvName);
        etMessage = (EditText)findViewById(R.id.etInputMessage);

        senderid = tvRollNum.getText().toString();
        sendername = tvName.getText().toString();
        messagecontents = etMessage.getText().toString();

        int current_code = sharedPref.getCurrentCode();
        messageid = senderid+current_code;
        sharedPref.updateCurrentCode(current_code+1);

        long result = dbobj.Add_Message(messageid,senderid,sendername,messagecontents);
        if(result>0)
        {
            Toast.makeText(this,"Success: Message Published",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this,MainActivity.class);
            this.startActivity(i);
            this.finish();
        }
    }
}
