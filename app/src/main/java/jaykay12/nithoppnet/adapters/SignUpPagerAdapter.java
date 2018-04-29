package jaykay12.nithoppnet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import jaykay12.nithoppnet.fragments.ChooseAvatarFragment;
import jaykay12.nithoppnet.fragments.ProfileFragment;

/**
 * Created by jaykay12 on 29/4/18.
 */

public class SignUpPagerAdapter extends FragmentStatePagerAdapter {

    public SignUpPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String string="";
        switch(position){
            case 0: string="Provide Details";
                    break;
            case 1: string="Choose an Avatar";
                    break;
            default: string="Provide Details";
        }

        return string;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0: fragment = ProfileFragment.get();
                    break;
            case 1: fragment = ChooseAvatarFragment.get();
                    break;
            default: fragment = ProfileFragment.get();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
