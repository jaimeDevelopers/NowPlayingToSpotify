package com.jaime.addtracksspotifynowplaying.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jaime.addtracksspotifynowplaying.R;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {


    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

    public ScreenSlidePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Music.newInstance(position);
                break;
            case 1:
                fragment = Settings.newInstance(position);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}