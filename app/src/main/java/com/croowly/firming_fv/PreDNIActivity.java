package com.croowly.firming_fv;

import android.os.Bundle;
import android.widget.TextView;

import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.root.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by carlosjimz on 05/02/2018.
 */

public class PreDNIActivity extends BaseActivity {


    @BindView(R.id.pre_dni_info)
    TextView dniInfo;

    @BindView(R.id.pre_dni_title)
    TextView dniTitle;

    private Verification verification = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.predni_layout);
        ButterKnife.bind(this);

    processBundle();
    }


    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());

        if(verification!=null && verification.getStatus()!= null && verification.getStatus()==Verification.STATUS.INTERMEDIATE){
            dniInfo.setText(getResources().getString(R.string.pre_dni_info2));
            dniTitle.setText(getResources().getString(R.string.pre_dni_title2));
        }
    }


    @OnClick(R.id.nextButton)
    public void nextButton(){

        verification.setStep(Verification.STEP.ID);
        verification.setStatus(verification.getStatus());

        Bundle bundle = new Bundle();
        bundle.putSerializable(Verification.class.toString(), verification);
        startNewActivity(PreDNIActivity.this,DNIActivity.class,bundle,true);
    }
}

