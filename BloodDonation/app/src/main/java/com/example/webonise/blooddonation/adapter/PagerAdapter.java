package com.example.webonise.blooddonation.adapter;

/**
 * Created by webonise on 1/9/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.webonise.blooddonation.Fragments.ListDisplayFragment;
import com.example.webonise.blooddonation.Fragments.MapFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ListDisplayFragment tab1 = new ListDisplayFragment();
                return tab1;
            case 1:
              /*  BlankFragment tab2 = new BlankFragment();*/
                MapFragment tab2 = new MapFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}