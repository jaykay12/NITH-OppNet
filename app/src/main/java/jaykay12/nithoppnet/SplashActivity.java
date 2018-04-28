package jaykay12.nithoppnet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import jaykay12.nithoppnet.OppNetArena.CreateProfile;
import jaykay12.nithoppnet.OppNetArena.DatabaseOperations;
import jaykay12.nithoppnet.OppNetArena.GenerateSeed;

public class SplashActivity extends Activity {
    private static final long TIME_SPLASH=10000;
    ImageView ivLoader;
    Context context;
    DatabaseOperations dbobj;
    SharedPref sharedPref;
    private String[] loaders= {
            "LOADING .", "LOADING . .", "LOADING . . .", "LOADING .", "LOADING . .", "LOADING . . .",
            "LOADING .", "LOADING . .", "LOADING . . .", "LOADING .", "LOADING . .", "LOADING . . .",
            "OPENING DATABASES", "RETRIEVING CREDENTIALS", "CHECKING PERMISSIONS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLoader = (ImageView)findViewById(R.id.ivLoader);
        Glide.with(SplashActivity.this).load(R.drawable.loader).asGif().crossFade().into(ivLoader);

        context = this;
        sharedPref = new SharedPref(context);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dbobj = new DatabaseOperations(context);
//                Toast.makeText(context,"Database Created Successfully",Toast.LENGTH_LONG).show();

                Log.v("login status:",""+sharedPref.getLoginStatus());

                if(sharedPref.getLoginStatus()==false){
                    Intent intent = new Intent(context,CreateProfile.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(context,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },TIME_SPLASH);

    }
}
