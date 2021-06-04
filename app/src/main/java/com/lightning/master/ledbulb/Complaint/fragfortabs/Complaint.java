package com.lightning.master.ledbulb.Complaint.fragfortabs;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lightning.master.ledbulb.R;

public class Complaint extends AppCompatActivity {
    ImageView iv_back;
    ViewPager viewPager;
    PagerAdapter adapter;
    TabLayout tabLayout;
    public  static Double final_lattitude,final_longitude ;
    String refresh="";
    public static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        init();
    }

    void init()
    {

        iv_back=(ImageView) findViewById(R.id.iv_back_services);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText("New Complaint"));

        tabLayout.addTab(tabLayout.newTab().setText("Complaints"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        onClicks();
    }

    void onClicks()
    {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Complaint.this, Home.class));
                finish();
            }
        });

        if(i==1)
        {
            i = 0;
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setCurrentItem(1);
        }
        else
        {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finish();

    }
}
