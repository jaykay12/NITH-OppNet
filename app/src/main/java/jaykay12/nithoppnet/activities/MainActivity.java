package jaykay12.nithoppnet.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.database.DBAdapter;
import jaykay12.nithoppnet.transfer.TransferConstants;
import jaykay12.nithoppnet.utils.ConnectionUtils;
import jaykay12.nithoppnet.utils.SharedPref;
import jaykay12.nithoppnet.utils.Utility;

public class MainActivity extends AppCompatActivity {

    public static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int WRITE_PERM_REQ_CODE = 19;

    TextView tvPortNumber;
    ImageView ivDisplayPic;
    TextView tvDisplayName;
    SharedPref sharedPref;
    Bundle bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = new SharedPref(this);
        bd = new Bundle();
        bd = sharedPref.getInfo();

        tvPortNumber = (TextView) findViewById(R.id.tvPortInfo);
        ivDisplayPic = (ImageView)findViewById(R.id.ivDisplayPic);
        tvDisplayName = (TextView)findViewById(R.id.tvDisplayName);

        ivDisplayPic.setImageResource(sharedPref.getAvatar());
        tvDisplayName.setText(bd.getString("name"));

        checkWritePermission();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(MainActivity.this, "This permission is needed for " + "file sharing." +
                    " But Whatever, if that's what you want...!!!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBAdapter.getInstance(MainActivity.this).clearDatabase();
        tvPortNumber.setText(String.format("Requests listening on %s", ConnectionUtils.getPort
                (MainActivity.this)));
    }

    private void saveUsername() {
        bd = sharedPref.getInfo();
        String userName = bd.getString("name")+" - "+Build.MANUFACTURER;
        if (userName != null && userName.trim().length() > 0) {
            Utility.saveString(MainActivity.this, TransferConstants.KEY_USER_NAME, userName);
        }
    }

    private void checkWritePermission() {
        boolean isGranted = Utility.checkPermission(WRITE_PERMISSION, this);
        if (!isGranted) {
            Utility.requestPermission(WRITE_PERMISSION, WRITE_PERM_REQ_CODE, this);
        }
    }

    public void startWiFiDirect(View v) {
        if (Utility.isWiFiEnabled(MainActivity.this)) {
            saveUsername();
            Intent wifiDirectIntent = new Intent(MainActivity.this, FindPeerActivity.class);
            startActivity(wifiDirectIntent);
            finish();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string
                    .wifi_not_enabled_error),Toast.LENGTH_LONG).show();
        }
    }

    public void startOppNet(View v){
        Intent i = new Intent(MainActivity.this, GenerateSeed.class);
        startActivity(i);
    }

    public void projectInfo(View v){
        Intent i = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(i);
    }

}
