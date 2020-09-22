package com.jaime.addtracksspotifynowplaying.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaime.addtracksspotifynowplaying.R;

public class Music extends Fragment {


    public Music() {

    }

    public static Music newInstance(int index) {
        Music fragment = new Music();
        Bundle bundle = new Bundle();
        bundle.putInt("HOLAAAAAAAAAERAEAASDASDADADJSDKLJKLADJKLSKJLKSDJLKJLDSJKLDSKJJKLS", index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}