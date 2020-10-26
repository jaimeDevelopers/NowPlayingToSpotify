package com.jaime.addtracksspotifynowplaying.ui.activities.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jaime.addtracksspotifynowplaying.R;
import com.jaime.addtracksspotifynowplaying.ui.activities.Music;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {


    //@StringRes
    //private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};

    public ScreenSlidePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = Music.newInstance();

        }
        assert fragment != null;
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}