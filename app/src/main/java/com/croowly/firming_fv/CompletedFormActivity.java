package com.croowly.firming_fv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.root.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by carlosjimz on 05/02/2018.
 */

public class CompletedFormActivity extends BaseActivity {

    Verification verification = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_completed_layout);
        ButterKnife.bind(this);

        processBundle();
    }

    @OnClick(R.id.proceedVerificationBT)
    public void proceedVerificationBT(View view) {

        verification.setStep(Verification.STEP.ID);
        verification.setStatus(Verification.STATUS.INIT);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Verification.class.toString(), verification);
        startNewActivity(CompletedFormActivity.this,PreDNIActivity.class,bundle,true);
    }


    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());

    }


}
