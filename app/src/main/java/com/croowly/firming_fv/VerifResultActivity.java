package com.croowly.firming_fv;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.managers.PrefsManager;
import com.croowly.firming_fv.root.BaseActivity;
import com.croowly.firming_fv.tasks.MailTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by carlosjimz on 07/02/2018.
 */

public class VerifResultActivity extends BaseActivity implements MailTask.ProgressListener{
    MailTask mailTask = null;
    @BindView(R.id.verif_result_id_image)ImageView IdImage;
    @BindView(R.id.verif_result_face_image)ImageView FaceImage;
    @BindView(R.id.verif_result_voice_image)ImageView VoiceImage;
    @BindView(R.id.verif_result_signature_image)ImageView SignatureImage;

    @BindView(R.id.verif_message) TextView messageView;
    @BindView(R.id.verif_result_title) TextView titleView;
    @BindView(R.id.sendBT) TextView sendBT;

    Class<?> nextActivity = null;
    Verification verification = null;
    static  boolean ProccessCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifresult_layout);
        ButterKnife.bind(this);

        processBundle();

    }

    private void sendEmail() {
        PrefsManager.configPrefs(this);
        String email = PrefsManager.getEmail();
        String subject = "ACCOUNT CONTRACT";
        String message = "Here is your Account Contract";

        //Creating SendMail object
        mailTask = new MailTask(this, email, subject, message, false);

        mailTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mailTask!=null)
            mailTask.cancel(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mailTask!=null)
            mailTask.cancel(true);
    }

    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());

        if(verification!=null){

            messageView.setText(R.string.verif_message1);

            switch (verification.getStep()){
                case ID:
                    titleView.setText(getResources().getString(R.string.verif_result_title));
                    sendBT.setText(getResources().getString(R.string.next_step));
                    ProccessCompleted = false;
                    if(verification.getStatus()== Verification.STATUS.SUCCESSFULL)
                        IdImage.setImageDrawable(getResources().getDrawable(R.drawable.idcard_v));
                    else
                        IdImage.setImageDrawable(getResources().getDrawable(R.drawable.idcard));

                    FaceImage.setImageDrawable(getResources().getDrawable(R.drawable.face));
                    VoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.voice));
                    SignatureImage.setImageDrawable(getResources().getDrawable(R.drawable.signature));

                    nextActivity=PreFaceActivity.class;
//                    messageView.setText();
                    break;

                case FACIAL:
                    titleView.setText(getResources().getString(R.string.verif_result_title));
                    sendBT.setText(getResources().getString(R.string.next_step));
                    ProccessCompleted = false;

                    IdImage.setImageDrawable(getResources().getDrawable(R.drawable.idcard_v));

                    if(verification.getStatus()== Verification.STATUS.SUCCESSFULL)
                        FaceImage.setImageDrawable(getResources().getDrawable(R.drawable.face_v));
                    else
                        FaceImage.setImageDrawable(getResources().getDrawable(R.drawable.face));


                    VoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.voice));
                    SignatureImage.setImageDrawable(getResources().getDrawable(R.drawable.signature));

                    nextActivity=PreVoiceActivity.class;
                    break;

                case VOICE:
                    titleView.setText(getResources().getString(R.string.verif_result_title));
                    sendBT.setText(getResources().getString(R.string.next_step));
                    ProccessCompleted = false;
                    IdImage.setImageDrawable(getResources().getDrawable(R.drawable.idcard_v));
                    FaceImage.setImageDrawable(getResources().getDrawable(R.drawable.face_v));

                    if(verification.getStatus()== Verification.STATUS.SUCCESSFULL)
                        VoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.voice_v));
                    else
                        VoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.voice));

                    nextActivity=PreSignActivity.class;

                    SignatureImage.setImageDrawable(getResources().getDrawable(R.drawable.signature));
                    break;

                case SIGNATURE:
                    titleView.setText(getResources().getString(R.string.verif_result_title2));
                    sendBT.setText(getResources().getString(R.string.finish));
                    ProccessCompleted = true;
                    IdImage.setImageDrawable(getResources().getDrawable(R.drawable.idcard_v));
                    FaceImage.setImageDrawable(getResources().getDrawable(R.drawable.face_v));
                    VoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.voice_v));

                    if(verification.getStatus()== Verification.STATUS.SUCCESSFULL)
                        SignatureImage.setImageDrawable(getResources().getDrawable(R.drawable.signature_v));
                    else
                        SignatureImage.setImageDrawable(getResources().getDrawable(R.drawable.signature));


                    messageView.setText(R.string.verif_message2);
//                    nextActivity=SignActivity.class;
                    break;

            }



        }

    }

    @OnClick(R.id.sendBT)
    public void nextButton(){
        if(ProccessCompleted) {
            sendEmail();

        }
        else{
            Bundle bundle = new Bundle();
            bundle.putSerializable(Verification.class.toString(), verification);
            startNewActivity(VerifResultActivity.this,nextActivity,bundle,true);

        }

    }


    @Override
    public void progressTask(MailTask.Modes state) {

        switch (state){

            case SUCCESSFUL:
                exitApp();
                break;

            case FAILED:
              exitApp();
                break;

        }
    }
}

