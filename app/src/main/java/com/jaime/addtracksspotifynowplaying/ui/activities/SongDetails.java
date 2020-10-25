package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jaime.addtracksspotifynowplaying.R;

public class SongDetails extends AppCompatActivity {


    /**
     * Initializes the activity, filling in the data from the Intent.
     *
     * @param savedInstanceState Contains information about the saved state
     *                           of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        Toolbar mTopToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mTopToolbar);

        // Initialize the views. String nowPlayingSong, String streamingSong, String date, String infoSearch
        TextView nowPlayingSong = findViewById(R.id.notificationSong);
        TextView streamingSong = findViewById(R.id.songAdded);
        TextView date = findViewById(R.id.date);
        TextView infoSearch = findViewById(R.id.info);


        //ImageView sportsImage = findViewById(R.id.sportsImageDetail);

        // Set the text from the Intent extra.
        nowPlayingSong.setText(getIntent().getStringExtra("nowPlayingSong"));
        streamingSong.setText(getIntent().getStringExtra("streamingSong"));
        date.setText(getIntent().getStringExtra("date"));
        infoSearch.setText(getIntent().getStringExtra("infoSearch"));


        // Load the image using the Glide library and the Intent extra.
        //Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
        //        .into(sportsImage);
    }


}
