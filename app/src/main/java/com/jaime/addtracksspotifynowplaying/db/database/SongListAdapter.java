package com.jaime.addtracksspotifynowplaying.db.database;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaime.addtracksspotifynowplaying.R;
import com.jaime.addtracksspotifynowplaying.ui.activities.SongDetails;

import java.util.List;

import static java.lang.String.valueOf;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private Context mContext;


    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView SongItemTitle;
        private final TextView SongItemSubtitle;
        //private final TextView SongItemSubTitle;


        private SongViewHolder(View itemView) {
            super(itemView);
            SongItemTitle = itemView.findViewById(R.id.Notification);
            SongItemSubtitle = itemView.findViewById(R.id.Search);
            //SongItemSubTitle = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Song currentSong = mSongs.get(getAdapterPosition());

            Intent detailIntent = new Intent(mContext, SongDetails.class);
            //public Song(String nowPlayingSong, String streamingSong, String date, String infoSearch) {

            detailIntent.putExtra("nowPlayingSong", currentSong.getNowPlayingSong());
            detailIntent.putExtra("streamingSong", currentSong.getStreamingSong());
            detailIntent.putExtra("date", currentSong.getDate());
            detailIntent.putExtra("infoSearch", currentSong.getInfoSearch());


            mContext.startActivity(detailIntent);

        }

    }

    private final LayoutInflater mInflater;
    private List<Song> mSongs; // Cached copy of Songs

    public SongListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cards_layout, parent, false);

        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        if (mSongs != null) {
            Song current = mSongs.get(position);
            holder.SongItemTitle.setText(valueOf(current.getNowPlayingSong()));
            holder.SongItemSubtitle.setText(current.getStreamingSong());
            //holder.SongItemSubTitle.setText(current.getNowPlayingSong());

        } else {
            // Covers the case of data not being ready yet.
            holder.SongItemTitle.setText("No Song");
        }
    }

    public void setSongs(List<Song> Songs) {
        mSongs = Songs;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mSongs has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mSongs != null)
            return mSongs.size();
        else return 0;
    }


}
