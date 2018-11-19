package com.croowly.firming_fv.root;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.croowly.firming_fv.R;
import com.croowly.firming_fv.audiorecorder.AndroidAudioRecorder;
import com.croowly.firming_fv.audiorecorder.model.AudioChannel;
import com.croowly.firming_fv.audiorecorder.model.AudioSampleRate;
import com.croowly.firming_fv.audiorecorder.model.AudioSource;

/**
 * Created by carlosjimz on 29/01/2018.
 */

public class BaseActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private BaseActivity activity;
    public static final int REQUEST_RECORD_AUDIO = 0;
    public static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";


    public void startNewActivity(Context fromActivity, Class<?> toActivityClass, Bundle extras, boolean anim) {
        Intent intent = new Intent(fromActivity, toActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (extras != null)
            intent.putExtras(extras);
        startActivity(intent);
       // if (anim)
        //    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void exitApp(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

        android.os.Process.killProcess(android.os.Process.myPid());
    }



    @Override
    public void onBackPressed() {
        activity = this;

        if (doubleBackToExitPressedOnce)
            exit();


        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void exit(){

        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.exit_title))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        finish();

                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no_btn), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                })
                .show();

    }


}
