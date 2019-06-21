package com.vincis.beTraDict;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class PagerViewAdapter extends FragmentPagerAdapter{
    Bundle bd;
    public PagerViewAdapter(FragmentManager fm, Bundle b) {
        super(fm);
        bd=b;
    }


    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        switch (i)
        {
            case 0:
                   fragment= new frag4();
                   fragment.setArguments(bd);
                   break;


                case 1:
                   fragment=new frag3();
                   fragment.setArguments(bd);
                   break;
            case 2:
                fragment=new frag2();
                fragment.setArguments(bd);
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
