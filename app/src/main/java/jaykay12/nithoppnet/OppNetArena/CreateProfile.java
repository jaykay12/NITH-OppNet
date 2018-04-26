package jaykay12.nithoppnet.OppNetArena;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.SharedPref;

public class CreateProfile extends AppCompatActivity {

    SharedPref sharedPref;
    EditText etName, etRollNum;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        setContentView(R.layout.create_profile);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        etName = (EditText)findViewById(R.id.etName);
        etRollNum = (EditText)findViewById(R.id.etRollNum);
    }

    public void createArena(View v){
        Log.v("Inside Function","");
        String name,rollnum;
        name = etName.getText().toString();
        rollnum = etRollNum.getText().toString();

        Intent i = new Intent(this,GenerateSeed.class);
        sharedPref.saveInfo(name,rollnum);
        Bundle bd = sharedPref.getInfo();
        i.putExtra("UserDate",bd);
        sharedPref.setLoginStatus(true);
        sharedPref.updateCurrentCode(0);
        this.startActivity(i);
        this.finish();
    }
}
