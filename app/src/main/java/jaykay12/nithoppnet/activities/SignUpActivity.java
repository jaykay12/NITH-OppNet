package jaykay12.nithoppnet.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.adapters.SignUpPagerAdapter;
import jaykay12.nithoppnet.utils.SharedPref;

public class SignUpActivity extends AppCompatActivity {

    private ViewPager vpSignUp;
    private TabLayout tlSignUp;
    SignUpPagerAdapter signUpPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tlSignUp = (TabLayout)findViewById(R.id.tlSignUp);
        vpSignUp = (ViewPager)findViewById(R.id.vpSignUp);
        vpSignUp.setOffscreenPageLimit(3);

        signUpPagerAdapter = new SignUpPagerAdapter(getSupportFragmentManager());
        vpSignUp.setAdapter(signUpPagerAdapter);
        tlSignUp.setupWithViewPager(vpSignUp);
    }
}
