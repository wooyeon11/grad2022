package com.heelab.bebrave;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class GraphPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public GraphPagerAdapter(FragmentManager fm, int pageCount) {

        super(fm);

        this.mPageCount = pageCount;

    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                ecgView ECGFragment = new ecgView();
                ((MainActivity)MainActivity.context_main).toggleGraphView(1,ECGFragment);
               // ((MainActivity)MainActivity.context_main).myToast("1이다.");
                return ECGFragment;



            case 1:

                SensorLog sensorLog = new SensorLog();
                ((MainActivity)MainActivity.context_main).toggleGraphView(2,sensorLog);
              //  ((MainActivity)MainActivity.context_main).myToast("2이다.");

                return sensorLog;



            default:

                return null;

        }

    }



    @Override
    public int getCount() {

        return mPageCount;

    }

}