package jaykay12.nithoppnet.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jaykay12.nithoppnet.database.DatabaseOperations;
import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.utils.SharedPref;

public class SplashActivity extends Activity {
    private static final long TIME_SPLASH=10000;
    ImageView ivLoader;
    Context context;
    DatabaseOperations dbobj;
    SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLoader = (ImageView)findViewById(R.id.ivLoader);
        Glide.with(SplashActivity.this)
                .load(R.drawable.loader)
                .asGif()
                .crossFade()
                .into(ivLoader);

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
                    Intent intent = new Intent(context,SignUpActivity.class);
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
