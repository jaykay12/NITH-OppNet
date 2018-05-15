package jaykay12.nithoppnet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.activities.GenerateSeed;
import jaykay12.nithoppnet.activities.MainActivity;
import jaykay12.nithoppnet.utils.SharedPref;

/**
 * Created by jaykay12 on 29/4/18.
 */

public class ProfileFragment extends Fragment{

    SharedPref sharedPref;
    EditText etName, etRollNum;
    Button btnCreate;

    public ProfileFragment() {
        //Required
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        btnCreate = (Button)view.findViewById(R.id.btnCreate);
        etName = (EditText)view.findViewById(R.id.etName);
        etRollNum = (EditText)view.findViewById(R.id.etRollNum);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Inside Function","");
                String name,rollnum;
                name = etName.getText().toString();
                rollnum = etRollNum.getText().toString();

                sharedPref.saveInfo(name,rollnum);
                sharedPref.setLoginStatus(true);
                sharedPref.updateCurrentCode(0);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public static ProfileFragment get(){
        return new ProfileFragment();
    }

}
