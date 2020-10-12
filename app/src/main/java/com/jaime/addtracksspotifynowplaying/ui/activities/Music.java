package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaime.addtracksspotifynowplaying.R;

import java.util.ArrayList;

public class Music extends Fragment {


    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Sport> mSportsData;
    private SportsAdapter mAdapter;


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


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getContext(), NewWordActivity.class);
                //startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
                clearData();
                Toast.makeText(getContext(), R.string.update, Toast.LENGTH_LONG).show();


            }
        });


        // Initialize the RecyclerView.
        mRecyclerView = root.findViewById(R.id.my_recycler_view);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the ArrayList that will contain the data.
        mSportsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new SportsAdapter(getContext(), mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();


        // Helper class for creating swipe to dismiss and drag and drop
        // functionality.
//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
//                .SimpleCallback(
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
//                        ItemTouchHelper.DOWN | ItemTouchHelper.UP,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            /**
//             * Defines the drag and drop functionality.
//             *
//             * @param recyclerView The RecyclerView that contains the list items
//             * @param viewHolder The SportsViewHolder that is being moved
//             * @param target The SportsViewHolder that you are switching the
//             *               original one with.
//             * @return true if the item was moved, false otherwise
//             */
//            @Override
//            public boolean onMove(RecyclerView recyclerView,
//                                  RecyclerView.ViewHolder viewHolder,
//                                  RecyclerView.ViewHolder target) {
//                // Get the from and to positions.
//                int from = viewHolder.getAdapterPosition();
//                int to = target.getAdapterPosition();
//
//                // Swap the items and notify the adapter.
//                Collections.swap(mSportsData, from, to);
//                mAdapter.notifyItemMoved(from, to);
//                return true;
//            }
//
//            /**
//             * Defines the swipe to dismiss functionality.
//             *
//             * @param viewHolder The viewholder being swiped.
//             * @param direction The direction it is swiped in.
//             */
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder,
//                                 int direction) {
//                // Remove the item from the dataset.
//                mSportsData.remove(viewHolder.getAdapterPosition());
//                // Notify the adapter.
//                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//            }
//        });
//
//        // Attach the helper to the RecyclerView.
//        helper.attachToRecyclerView(mRecyclerView);


        // Put initial data into the word list.
        //  for (int i = 0; i < 20; i++) {
        //      mWordList.addLast("Word " + i);
        //  }


        //mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);


// Get a handle to the RecyclerView.
// Create an adapter and supply the data to be displayed.
        // mAdapter = new WordListAdapter(getContext(), mWordList);
// Connect the adapter with the RecyclerView.
        //      mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        //      mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }


    private void initializeData() {
        // Get the resources from the XML file.
        String[] sportsList = getResources()
                .getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources()
                .getStringArray(R.array.sports_info);

        // Clear the existing data (to avoid duplication).
        mSportsData.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport
        for (int i = 0; i < sportsList.length; i++) {
            mSportsData.add(new Sport(sportsList[i], sportsInfo[i]));
        }


        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }


    private void clearData() {
        // Clear the existing data (to avoid duplication).
        mSportsData.clear();


        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}