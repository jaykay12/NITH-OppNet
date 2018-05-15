package jaykay12.nithoppnet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.adapters.AvatarsAdapter;
import jaykay12.nithoppnet.utils.SharedPref;

/**
 * Created by jaykay12 on 29/4/18.
 */

public class ChooseAvatarFragment extends Fragment {

    SharedPref sharedPref;

    public ChooseAvatarFragment() {
        //Required
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooseavatar,container,false);

        GridView gvAvatars = (GridView)view.findViewById(R.id.gvAvatars);
        final AvatarsAdapter avatarsAdapter = new AvatarsAdapter(getContext());
        gvAvatars.setAdapter(avatarsAdapter);

        sharedPref = new SharedPref(getContext());

        gvAvatars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int avatarid = avatarsAdapter.Avatars[position];
                sharedPref.setAvatar(avatarid);
                TabLayout tlsignup = (TabLayout)getActivity().findViewById(R.id.tlSignUp);
                tlsignup.getTabAt(1).select();

//                ProfileFragment profileFragment = new ProfileFragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.layout_choose,profileFragment,"")
//                        .addToBackStack(null)
//                        .commit();
                Log.v("done","done");
            }
        });

        return view;
    }

    public static ChooseAvatarFragment get(){
        return new ChooseAvatarFragment();
    }



}
