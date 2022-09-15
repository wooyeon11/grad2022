package com.heelab.bebrave;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ReportAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ReportAdapter(FragmentManager fm, int pageCount) {

        super(fm);

        this.mPageCount = pageCount;

    }



    @Override
    public Fragment getItem(int position) {


        switch (position) {

            case 0:

                                                        //                SelfReport selfReport = new SelfReport();
                                                        //
                                                        //                return selfReport;
                                                        //
                                                        //
                                                        //            case 1:

                ThinkingTest selfReport2 = new ThinkingTest();

                return selfReport2;

            case 1:

                SettingTest selfReport3 = new SettingTest();

                return selfReport3;
          default:
                    return null;
        }


    }



    @Override
    public int getCount() {

        return mPageCount;

    }

}