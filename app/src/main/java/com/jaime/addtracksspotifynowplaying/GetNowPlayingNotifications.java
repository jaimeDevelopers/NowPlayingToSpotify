package com.jaime.addtracksspotifynowplaying;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.jaime.addtracksspotifynowplaying.db.database.Song;
import com.jaime.addtracksspotifynowplaying.db.database.SongDao;
import com.jaime.addtracksspotifynowplaying.db.database.SongRepository;
import com.jaime.addtracksspotifynowplaying.db.database.SongRoomDatabase;
import com.jaime.addtracksspotifynowplaying.db.database.SongViewModel;


public class GetNowPlayingNotifications extends NotificationListenerService {


    //public static  Spotify Source = new Spotify();

    public static int SPOTIFY_REQUEST_CODE = 1337;

    //private SongViewModel mSongViewModel;
    private SongRepository mRepository;

    public Context context;
    public Song prue;

    int jaime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(MyValues.PREFERENCES_NOTIFICATION, MODE_PRIVATE);
        String PackageName = "com.google.intelligence.sense";
        if (Build.VERSION.SDK_INT >= 30) {
            PackageName = "com.google.android.as";
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PackageName", PackageName);  // Saving string
        editor.apply(); // commit changes


        //this.accountsPrefs = context.getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_ACCOUNT_FILE_NAME, Context.MODE_PRIVATE);
        //this.namePlaylistPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_NAME_PLAYLIST, Context.MODE_PRIVATE);
        //this.Source.Check(Get_Now_Playing_Notifications.accountsPrefs);

        //try {
        //    this.Source.init_Cache(this.namePlaylistPrefs.getString("playlist_name", getResources().getString(R.string.PlayListName)));
        //} catch (Exception e){
        //    e.printStackTrace();
        //

        // Get a new or existing ViewModel from the ViewModelProvider.
        mRepository = new SongRepository(getApplication());

    }


    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            matchNotificationCode(sbn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        //if (packageName.equals("com.google.intelligence.sense")) {
        //if (packageName.equals("com.google.android.as")) {


        //this.context = getApplicationContext();
        String title = sbn.getNotification().extras.getString("android.title");
        String text = sbn.getNotification().extras.getString("android.text");
        String package_name = sbn.getPackageName();
        Log.v("Notification title is", title);
        Log.v("Notification text is", text);
        Log.v("N. Package Name", package_name);


        mRepository = new SongRepository(getApplication());
        mRepository.insert(new Song("text --> jaime:" + jaime, title));
        jaime++;

        prue = mRepository.getItemById(10000);


        if (prue == null) {
            System.out.println("El valor es nulo");
        } else {
            System.out.println("aaa" + prue.getCity());

        }


//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//
//                try {
//                    prue = mRepository.getItemById(79999);
//                } catch (Exception E) {
//                    System.out.println("NULOOOOOOOO" + E.getMessage());
//                }
//
//
//            }
//        };
//
//        thread.start();
//
//        thread.join();


//            //if(isNetworkAvailable()){
//            //    try {
//            //        System.out.println();
//                    //this.Source.CreatePlaylistCache(this.namePlaylistPrefs.getString("playlist_name", getResources().getString(R.string.PlayListName)), false, false, title);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                    this.accountsPrefs = context.getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_ACCOUNT_FILE_NAME, Context.MODE_PRIVATE);
//                    this.namePlaylistPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_NAME_PLAYLIST, Context.MODE_PRIVATE);
//                    Get_Now_Playing_Notifications.Source.Check(Get_Now_Playing_Notifications.accountsPrefs);
//                    matchNotificationCode(sbn);
//                }


//            }

    }
    // }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


}
