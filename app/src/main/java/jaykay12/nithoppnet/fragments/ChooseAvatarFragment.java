package jaykay12.nithoppnet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import jaykay12.nithoppnet.R;
import jaykay12.nithoppnet.adapters.AvatarsAdapter;

/**
 * Created by jaykay12 on 29/4/18.
 */

public class ChooseAvatarFragment extends Fragment {

    public ChooseAvatarFragment() {
        //Required
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooseavatar,container,false);

        GridView gvAvatars = (GridView)view.findViewById(R.id.gvAvatars);
        AvatarsAdapter avatarsAdapter = new AvatarsAdapter(getContext());
        gvAvatars.setAdapter(avatarsAdapter);

        return view;
    }

    public static ChooseAvatarFragment get(){
        return new ChooseAvatarFragment();
    }



}
