package com.heelab.bebrave;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class reportMain extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static Context context_ReportView; // context 변수 선언

    // ... 코드 계속
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportquestion_layout);
        context_ReportView = this; // onCreate에서 this 할당
        Button backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("인지검사"));
        mTabLayout.addTab(mTabLayout.newTab().setText("훈련설정"));

        mViewPager = (ViewPager) findViewById(R.id.pager_content);
        ReportAdapter mContentsPagerAdapter = new ReportAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

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
