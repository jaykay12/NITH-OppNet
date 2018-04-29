package jaykay12.nithoppnet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by jaykay12 on 14/4/18.
 */

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "UserInfo";
    private static final String LOGIN_STATUS = "loginstatus";

    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginStatus(boolean isLogIn){
        editor.putBoolean(LOGIN_STATUS,isLogIn);
        editor.commit();
    }

    public boolean getLoginStatus(){
        return sharedPreferences.getBoolean(LOGIN_STATUS,false);
    }

    public void saveInfo(String name, String rollnum){
        editor.putString("name",name);
        editor.putString("rollnum",rollnum);
        editor.commit();
    }

    public Bundle getInfo(){
        Bundle bd = new Bundle();
        String name = sharedPreferences.getString("name","");
        String rollnum = sharedPreferences.getString("rollnum","");
        bd.putString("name",name);
        bd.putString("rollnum",rollnum);
        return bd;
    }

    public void updateCurrentCode(int code){
        editor.putInt("code",code);
        editor.commit();
    }

    public int getCurrentCode(){
        return sharedPreferences.getInt("code",0);
    }
}
