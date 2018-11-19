package com.croowly.firming_fv;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.root.BaseActivity;
import com.croowly.firming_fv.tasks.SignTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.smartaccess.sealsignbss.SealSignBSSEventListener;
import es.smartaccess.sealsignbss.SealSignBSSView;

/**
 * Created by carlosjimz on 12/02/2018.
 */

public class SignActivity extends BaseActivity implements SignTask.ProgressListener{

    private Verification verification = null;

    @BindView(R.id.signItem1)
    ImageView signItem1;
    @BindView(R.id.signItem2)
    ImageView signItem2;
    @BindView(R.id.signItem3)
    ImageView signItem3;
    @BindView(R.id.sendBT)
    Button sendBT;
    @BindView(R.id.attemptsTV)
    TextView attemptsTV;
    @BindView(R.id.animation_view)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.signView)
    SealSignBSSView signView;

    private int attempts = 0;

    SignTask signTask = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity);
        ButterKnife.bind(this);

        signView.setOnSealSignBSSEventListener(new SealSignBSSEventListener() {
            @Override
            public void onSignatureStarted() {
                checkUI(SignTask.Modes.SIGNED);
            }

            @Override
            public void onSignatureCleared() {
                checkUI(SignTask.Modes.CLEARED);
            }

            @Override
            public void onSignatureCanceled() {

            }
        });

        processBundle();
        signView.start();
        checkUI(null);
    }


    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());

    }


    @Override
    public void progressTask(final SignTask.Modes mode) {
        checkUI(mode);
    }

    @OnClick(R.id.clearBT)
    public void clearBT(){
        signView.cleanSignature();
    }

    @OnClick(R.id.sendBT)
    public void sendBT(){
        if(attempts<3) {
            attempts=attempts+1;
            sendOperation();
        }
        else{
            startNewActivity(SignActivity.this,VerifResultActivity.class,null,true);
        }
    }

    public void sendOperation() {

        if(signTask==null) {
            signView.stop();
            saveImage(signView.getBiometricImage());

            signTask = new SignTask(this);
            signTask.execute();
            checkUI(SignTask.Modes.SENDING);
        }


    }


    public void checkUI(final SignTask.Modes mode){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mode==null) {
                    sendBT.setVisibility(View.VISIBLE);
                    sendBT.setEnabled(false);
                    sendBT.setBackgroundColor(getResources().getColor(R.color.firmingUnAccent));
                    attemptsTV.setText(getResources().getString(R.string.sign_attempts));
                    signItem1.setVisibility(View.VISIBLE);
                    signItem2.setVisibility(View.VISIBLE);
                    signItem3.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    return;
                }

                switch (mode){
                    case SIGNED:
                        sendBT.setVisibility(View.VISIBLE);
                        sendBT.setEnabled(true);
                        sendBT.setBackgroundColor(getResources().getColor(R.color.firmingAccent));
                        attemptsTV.setText(getResources().getString(R.string.sign_attempts));
                        signItem1.setVisibility(View.VISIBLE);
                        signItem2.setVisibility(View.VISIBLE);
                        signItem3.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        break;
                    case CLEARED:
                        if(attempts<3) {
                            sendBT.setVisibility(View.VISIBLE);
                            sendBT.setEnabled(false);
                            sendBT.setBackgroundColor(getResources().getColor(R.color.firmingUnAccent));
                            attemptsTV.setText(getResources().getString(R.string.sign_attempts));
                            signItem1.setVisibility(View.VISIBLE);
                            signItem2.setVisibility(View.VISIBLE);
                            signItem3.setVisibility(View.VISIBLE);
                            lottieAnimationView.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case SENDING:
                        sendBT.setVisibility(View.INVISIBLE);
                        attemptsTV.setText(getResources().getString(R.string.sign_mode_sending));
                        signItem1.setVisibility(View.INVISIBLE);
                        signItem2.setVisibility(View.INVISIBLE);
                        signItem3.setVisibility(View.INVISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        break;
                    case PROCESSING:
                        sendBT.setVisibility(View.INVISIBLE);
                        attemptsTV.setText(getResources().getString(R.string.sign_mode_processing));
                        signItem1.setVisibility(View.INVISIBLE);
                        signItem2.setVisibility(View.INVISIBLE);
                        signItem3.setVisibility(View.INVISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        break;
                    case VERIFYING:
                        sendBT.setVisibility(View.INVISIBLE);
                        attemptsTV.setText(getResources().getString(R.string.sign_mode_verifying));
                        signItem1.setVisibility(View.INVISIBLE);
                        signItem2.setVisibility(View.INVISIBLE);
                        signItem3.setVisibility(View.INVISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESSFUL:
                        signTask.cancel(true);
                        signTask = null;
                        signView.cleanSignature();


                        if(attempts==3){
                            signView.stop();
                            verification.setStep(Verification.STEP.SIGNATURE);
                            verification.setStatus(Verification.STATUS.SUCCESSFULL);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Verification.class.toString(), verification);
                            startNewActivity(SignActivity.this,VerifResultActivity.class,bundle,true);
                        }
                        else
                            signView.start();
                        sendBT.setVisibility(View.VISIBLE);
                        sendBT.setEnabled(true);
                        sendBT.setBackgroundColor(getResources().getColor(R.color.firmingAccent));
                        attemptsTV.setText(getResources().getString(R.string.sign_attempts));
                        signItem1.setVisibility(View.VISIBLE);
                        signItem2.setVisibility(View.VISIBLE);
                        signItem3.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        break;

                    case FAILED:
                        signTask.cancel(true);
                        signTask = null;
                        signView.cleanSignature();
                        signView.start();
                        sendBT.setVisibility(View.VISIBLE);
                        sendBT.setEnabled(true);
                        attemptsTV.setText(getResources().getString(R.string.sign_attempts));
                        signItem1.setVisibility(View.VISIBLE);
                        signItem2.setVisibility(View.VISIBLE);
                        signItem3.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        break;

                }

                switch (attempts){
                    case 0:
                        sendBT.setText(getResources().getString(R.string.send));
                        signItem1.setImageDrawable(getResources().getDrawable(R.drawable.signature_b));
                        signItem2.setImageDrawable(getResources().getDrawable(R.drawable.signature_b));
                        signItem3.setImageDrawable(getResources().getDrawable(R.drawable.signature_b));
                        break;
                    case 1:
                        sendBT.setText(getResources().getString(R.string.send));
                        signItem1.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                        signItem2.setImageDrawable(getResources().getDrawable(R.drawable.signature_b));
                        signItem3.setImageDrawable(getResources().getDrawable(R.drawable.signature_b));
                        break;
                    case 2:
                        sendBT.setText(getResources().getString(R.string.send));
                        signItem1.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                        signItem2.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                        signItem3.setImageDrawable(getResources().getDrawable(R.drawable.signature_b));
                        break;
                    case 3:
                        signItem1.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                        signItem2.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                        signItem3.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                        sendBT.setText(getResources().getString(R.string.next));
                        break;

                }
            }
        });


    }


    private void saveImage(byte[] bytes){

        if(bytes!=null && bytes.length>0) {
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Signature.jpg");
            try {

                if (f.createNewFile()) {
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes);
                    fo.close();
                }
            } catch (IOException e) {
                return;
            }
        }
    }

}
