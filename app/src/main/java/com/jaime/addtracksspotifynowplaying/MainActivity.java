package com.jaime.addtracksspotifynowplaying;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jaime.addtracksspotifynowplaying.ui.main.ScreenSlidePagerAdapter;


public class MainActivity extends AppCompatActivity {

    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2Id);
        TabLayout tabLayout = findViewById(R.id.tabs);

        FragmentStateAdapter ScreeSlide = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(ScreeSlide);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(TAB_TITLES[position]);
                    }
                }).attach();


    }
}