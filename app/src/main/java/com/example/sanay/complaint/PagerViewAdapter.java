package com.example.sanay.complaint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sanay.complaint.Fragments.ComplainFragment;
import com.example.sanay.complaint.Fragments.ProfileFragment;
import com.example.sanay.complaint.Fragments.StatusFragment;

/**
 * Created by Sanay on 01/01/18.
 */

class PagerViewAdapter extends FragmentPagerAdapter{

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 1:
                ComplainFragment complainFragment = new ComplainFragment();
                return complainFragment;

            case 2:
                StatusFragment statusFragment = new StatusFragment();
                return  statusFragment;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

}