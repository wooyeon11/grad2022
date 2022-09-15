package com.heelab.bebrave;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResultActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static Context context_ResultView; // context 변수 선언
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        context_ResultView = this; // onCreate에서 this 할당
        Button backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("데이터 리포트"));
        mTabLayout.addTab(mTabLayout.newTab().setText("훈련 리포트"));

        mViewPager = (ViewPager) findViewById(R.id.pager_content);

        ResultPageAdapter mContentsPagerAdapter = new ResultPageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(mContentsPagerAdapter);

        mViewPager.addOnPageChangeListener(  new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override

            public void onTabSelected(TabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());


            }



            @Override

            public void onTabUnselected(TabLayout.Tab tab) {



            }



            @Override

            public void onTabReselected(TabLayout.Tab tab) {



            }

        });

    }

    @Override
    protected  void onStop()
    {

        super.onStop();

    }


}
