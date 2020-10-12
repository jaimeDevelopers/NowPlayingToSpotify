package com.jaime.addtracksspotifynowplaying;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


public class Get_Now_Playing_Notifications extends NotificationListenerService {

    public static SharedPreferences accountsPrefs;
    public static SharedPreferences namePlaylistPrefs;

    public static final String PREFERENCES_ACCOUNT_FILE_NAME = "accounts";
    public static final String PREFERENCES_NAME_PLAYLIST = "playlist";
    //public static  Spotify Source = new Spotify();

    public static int SPOTIFY_REQUEST_CODE = 1337;


    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        //this.accountsPrefs = context.getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_ACCOUNT_FILE_NAME, Context.MODE_PRIVATE);
        //this.namePlaylistPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_NAME_PLAYLIST, Context.MODE_PRIVATE);
        //this.Source.Check(Get_Now_Playing_Notifications.accountsPrefs);

        //try {
        //    this.Source.init_Cache(this.namePlaylistPrefs.getString("playlist_name", getResources().getString(R.string.PlayListName)));
        //} catch (Exception e){
        //    e.printStackTrace();
        //}

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
        //this.context = getApplicationContext();
        String title = sbn.getNotification().extras.getString("android.title");
        String text = sbn.getNotification().extras.getString("android.text");
        String package_name = sbn.getPackageName();
        Log.v("Notification title is", title);
        Log.v("Notification text is", text);
        Log.v("N. Package Name", package_name);


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

        //      }
    }


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
