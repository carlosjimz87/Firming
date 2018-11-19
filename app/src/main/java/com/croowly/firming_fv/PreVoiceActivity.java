package com.croowly.firming_fv;

import android.os.Bundle;
import android.speech.tts.Voice;

import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.root.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by carlosjimz on 05/02/2018.
 */

public class PreVoiceActivity extends BaseActivity {


    private Verification verification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prevoice_layout);
        ButterKnife.bind(this);

        processBundle();
    }


    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());

    }

    @OnClick(R.id.nextButton)
    public void nextButton(){

        verification.setStep(Verification.STEP.VOICE);
        verification.setStatus(Verification.STATUS.INIT);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Verification.class.toString(), verification);

        startNewActivity(PreVoiceActivity.this,VoiceActivity.class,bundle,true);
    }
}

