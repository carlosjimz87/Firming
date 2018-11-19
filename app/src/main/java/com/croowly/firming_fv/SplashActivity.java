package com.croowly.firming_fv;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.croowly.firming_fv.root.BaseActivity;
import com.croowly.firming_fv.services.FirmingService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by carlosjimz on 29/01/2018.
 */

public class SplashActivity extends BaseActivity implements FirmingService.ProgressListener<String>{

    @BindView(R.id.mainLayout)
    ConstraintLayout mainLayout;

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

    }
    Thread splashThread;
    Context context;
    SplashActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        context = this;
        activity = this;
        initThread();
        animationView.setVisibility(View.VISIBLE);


    }




    @Override
    public void progressService(FirmingService.Modes mode) {

     //   if(mode == FirmingService.Modes.SUCCESSFUL) {
            startNewActivity(SplashActivity.this, StartActivity.class, null, true);
            SplashActivity.this.finish();

     /**   }
        else{

             new MaterialDialog.Builder(this)
                    .title("ERROR")
                    .content("Firming Service is not available.")
                    .positiveText("DISMISS")
                     .onAny(new MaterialDialog.SingleButtonCallback() {
                         @Override
                         public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                           exitApp();
                         }
                     })
                    .show();

        }*/

        animationView.cancelAnimation();
        animationView.setVisibility(View.INVISIBLE);
    }

    public void initThread() {
        splashThread = new Thread() {

            @Override
            public void run() {
                super.run();
                try {
                    int waited = 0;

                    while (waited < 4000) {
                        sleep(100);
                        waited += 100;
                    }

                    FirmingService.testService(context,activity);



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }


}
