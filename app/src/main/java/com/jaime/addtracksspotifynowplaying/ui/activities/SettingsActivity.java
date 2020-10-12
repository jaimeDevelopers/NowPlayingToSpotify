package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.jaime.addtracksspotifynowplaying.R;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();


        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(mTopToolbar);


    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private int STORAGE_PERMISSION_CODE = 1;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.fragment_settings, rootKey);

            Preference button_report = findPreference("report");
            button_report.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    request_permissions(getContext());
                    report_log();
                    return true;
                }
            });
        }


        private void request_permissions(Context context) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                System.out.println("Garantizado ya");
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestStoragePermission(context);
            }

        }


        private void requestStoragePermission(Context context) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(context)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed because of this and that")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == STORAGE_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
            }
        }


        private void report_log() {
            if (isExternalStorageWritable()) {

                File appDirectory = new File(Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder");
                File logDirectory = new File(appDirectory + "/logs");
                File logFile = new File(logDirectory, "logcat_" + System.currentTimeMillis() + ".txt");

                // create app folder
                if (!appDirectory.exists()) {
                    appDirectory.mkdir();
                }

                // create log folder
                if (!logDirectory.exists()) {
                    logDirectory.mkdir();
                }

                // clear the previous logcat and then write the new one to the file
                try {
                    Process process = Runtime.getRuntime().exec("logcat -c");
                    process = Runtime.getRuntime().exec("logcat -f " + logFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (isExternalStorageReadable()) {
                // only readable
            } else {
                // not accessible
            }
        }

        /* Checks if external storage is available for read and write */
        public boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        /* Checks if external storage is available to at least read */
        public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
        }

    }
}