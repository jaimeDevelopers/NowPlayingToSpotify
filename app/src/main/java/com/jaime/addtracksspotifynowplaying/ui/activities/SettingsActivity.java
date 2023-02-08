package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.jaime.addtracksspotifynowplaying.MyValues;
import com.jaime.addtracksspotifynowplaying.R;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();


        Toolbar mTopToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mTopToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        public Context context;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.fragment_settings, rootKey);

            NotificationPermission notificationPermission = new NotificationPermission(context);

            context = getActivity();

            Preference button_notification = findPreference("enable");
            button_notification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);

                    boolean spotify_enabled = pref.getBoolean("SPOTIFY_ENABLED", false);         // getting String

                    if (spotify_enabled) {

                        if (!notificationPermission.isNotificationServiceEnabled(context)) {
                            Toast.makeText(getContext(), "Permission is not GRANTED", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Permission already GRANTED", Toast.LENGTH_SHORT).show();
                        }
                        context.startActivity(new Intent(MyValues.ACTION_NOTIFICATION_LISTENER_SETTINGS));

                    } else {
                        Toast.makeText(getContext(), "Enable a music service", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });


            Preference button_spotify = findPreference("spotify");
            button_spotify.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //request_permissions(getContext());
                    //report_log();
                    context.startActivity(new Intent(context, LoginSpotify.class));
                    return true;
                }
            });

            // https://docs.google.com/spreadsheets/d/1hl9duIPj88b3Hw4ka5r90SH9epZHtADkxqRTEr08XCw/edit?usp=sharing


            Preference button_bounty = findPreference("song_bounty");
            button_bounty.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String urlString = "https://docs.google.com/spreadsheets/d/1Xy89xNbtkOzrlh80kp72ZpUgAOQhqpc5c2sQ-iavAkg/edit?usp=sharing";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        context.startActivity(intent);
                    }
                    return true;
                }
            });

            Preference button_coffe = findPreference("paypal");
            button_coffe.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String urlString = "https://www.paypal.com/paypalme/jaimedevelopers";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        context.startActivity(intent);
                    }
                    return true;
                }
            });

            Preference github = findPreference("githubCode");
            github.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String urlString = "https://github.com/jaimeDevelopers/NowPlayingToSpotify";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        context.startActivity(intent);
                    }
                    return true;
                }
            });


            Preference button_email = findPreference("emailme");
            button_email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "jaimedevelopers@gmail.com", null));

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        emailIntent.setPackage(null);
                        context.startActivity(emailIntent);
                    }
                    return true;
                }
            });


            Preference button_review = findPreference("review");
            button_review.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                    System.out.println(appPackageName);
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                    return true;
                }
            });


//            Preference button_report = findPreference("report");
//            button_report.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    request_permissions(getContext());
//                    report_log();
//                    return true;
//                }
//            });


        }

//
//        private void request_permissions(Context context) {
//            if (ContextCompat.checkSelfPermission(
//                    context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
//                    PackageManager.PERMISSION_GRANTED) {
//                // You can use the API that requires the permission.
//                System.out.println("Garantizado ya");
//            } else {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestStoragePermission(context);
//            }
//
//        }
//
////
//        private void requestStoragePermission(Context context) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Permission needed")
//                        .setMessage("This permission is needed because of this and that")
//                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ActivityCompat.requestPermissions(requireActivity(),
//                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//                            }
//                        })
//                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .create().show();
//            } else {
//                ActivityCompat.requestPermissions(requireActivity(),
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//            }
//        }
//
//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//            if (requestCode == STORAGE_PERMISSION_CODE) {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//
//        private void report_log() {
//            if (isExternalStorageWritable()) {
//
//                File appDirectory = new File(Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder");
//                File logDirectory = new File(appDirectory + "/logs");
//                File logFile = new File(logDirectory, "logcat_" + System.currentTimeMillis() + ".txt");
//
//                // create app folder
//                if (!appDirectory.exists()) {
//                    appDirectory.mkdir();
//                }
//
//                // create log folder
//                if (!logDirectory.exists()) {
//                    logDirectory.mkdir();
//                }
//
//                // clear the previous logcat and then write the new one to the file
//                try {
//                    Process process = Runtime.getRuntime().exec("logcat -c");
//                    process = Runtime.getRuntime().exec("logcat -f " + logFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (isExternalStorageReadable()) {
//                // only readable
//            } else {
//                // not accessible
//            }
//        }
//
//        /* Checks if external storage is available for read and write */
//        public boolean isExternalStorageWritable() {
//            String state = Environment.getExternalStorageState();
//            return Environment.MEDIA_MOUNTED.equals(state);
//        }
//
//        /* Checks if external storage is available to at least read */
//        public boolean isExternalStorageReadable() {
//            String state = Environment.getExternalStorageState();
//            return Environment.MEDIA_MOUNTED.equals(state) ||
//                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
//        }
//


    }


}