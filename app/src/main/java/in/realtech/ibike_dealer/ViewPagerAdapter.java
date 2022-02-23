package in.realtech.ibike_dealer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.security.PublicKey;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int COUNT = 3;

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FirstFragment();
                break;
            case 1:
                fragment = new SecondFragment();
                break;
            case 2:
                fragment = new ThirdFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Unused Codes";
        }else if ( position == 1) {
            return "Used Codes";
        } else if( position == 2) {
            return  "All Codes";

        }

       return "codes used";
    }
}

