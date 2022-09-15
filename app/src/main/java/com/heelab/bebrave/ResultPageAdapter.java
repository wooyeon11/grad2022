package com.heelab.bebrave;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ResultPageAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ResultPageAdapter(FragmentManager fm, int pageCount) {

        super(fm);

        this.mPageCount = pageCount;

    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                FinalDataFrag finalDataFrag = new FinalDataFrag();
                //((MainActivity)MainActivity.context_main).toggleGraphView(1,finalDataFrag);
                // ((MainActivity)MainActivity.context_main).myToast("1이다.");
                return finalDataFrag;



            case 1:

                TrainingResultFrag trainingResultFrag = new TrainingResultFrag();
             //   ((MainActivity)MainActivity.context_main).toggleGraphView(2,sensorLog);
                //  ((MainActivity)MainActivity.context_main).myToast("2이다.");

                return trainingResultFrag;



            default:

                return null;

        }

    }



    @Override
    public int getCount() {

        return mPageCount;

    }

}