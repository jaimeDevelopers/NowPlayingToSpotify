package com.jaime.addtracksspotifynowplaying.db.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jaime.addtracksspotifynowplaying.R;

import java.util.List;

import static java.lang.String.valueOf;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    class SongViewHolder extends RecyclerView.ViewHolder {
        private final TextView SongItemTitle;
        private final TextView SongItemView;
        private final TextView SongItemSubTitle;


        private SongViewHolder(View itemView) {
            super(itemView);
            SongItemTitle = itemView.findViewById(R.id.title);
            SongItemView = itemView.findViewById(R.id.textView);
            SongItemSubTitle = itemView.findViewById(R.id.subTitle);
        }
    }

    private final LayoutInflater mInflater;
    private List<Song> mSongs; // Cached copy of Songs

    public SongListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cards_layout, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        if (mSongs != null) {
            Song current = mSongs.get(position);
            holder.SongItemTitle.setText(valueOf(current.getId()));
            holder.SongItemView.setText(current.getCity());
            holder.SongItemSubTitle.setText(current.getName());

        } else {
            // Covers the case of data not being ready yet.
            holder.SongItemSubTitle.setText("No Song");
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
