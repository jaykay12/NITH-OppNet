package jaykay12.nithoppnet.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import jaykay12.nithoppnet.R;

/**
 * Created by jaykay12 on 30/4/18.
 */

public class AvatarsAdapter extends BaseAdapter {

    private final Context context;
    private final Integer[] Avatars;


    public AvatarsAdapter(Context context) {
        this.context = context;
        this.Avatars = popoulateAvatarList();
    }

    @Override
    public int getCount() {
        return Avatars.length;
    }

    @Override
    public Object getItem(int position) {
        return Avatars[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_card_avatars, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.ivAvatar);
        imageView.setImageResource(Avatars[position]);

        return convertView;
    }

    public Integer[] popoulateAvatarList(){
        Integer[] avatar = new Integer[40];

        avatar[0] = R.drawable.kid1;avatar[1] = R.drawable.kid2;avatar[2] = R.drawable.kid3;avatar[3] = R.drawable.kid4;
        avatar[4] = R.drawable.kid5;avatar[5] = R.drawable.kid6;avatar[6] = R.drawable.kid7;avatar[7] = R.drawable.kid8;
        avatar[8] = R.drawable.kid9;avatar[9] = R.drawable.kid10;avatar[10] = R.drawable.kid11;avatar[11] = R.drawable.kid12;
        avatar[12] = R.drawable.kid13;avatar[13] = R.drawable.kid14;avatar[14] = R.drawable.kid15;avatar[15] = R.drawable.kid16;
        avatar[16] = R.drawable.kid17;avatar[17] = R.drawable.kid18;avatar[18] = R.drawable.kid19;avatar[19] = R.drawable.kid20;
        avatar[20] = R.drawable.kid21;avatar[21] = R.drawable.kid22;avatar[22] = R.drawable.kid23;avatar[23] = R.drawable.kid24;
        avatar[24] = R.drawable.kid25;avatar[25] = R.drawable.kid26;avatar[26] = R.drawable.kid27;avatar[27] = R.drawable.kid28;
        avatar[28] = R.drawable.kid29;avatar[29] = R.drawable.kid30;avatar[30] = R.drawable.kid31;avatar[31] = R.drawable.kid32;
        avatar[32] = R.drawable.kid33;avatar[33] = R.drawable.kid34;avatar[34] = R.drawable.kid35;avatar[35] = R.drawable.kid36;
        avatar[36] = R.drawable.kid37;avatar[37] = R.drawable.kid38;avatar[38] = R.drawable.kid39;avatar[39] = R.drawable.kid40;

        return avatar;
    }
}
