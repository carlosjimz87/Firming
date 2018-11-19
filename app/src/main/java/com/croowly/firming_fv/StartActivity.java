package com.croowly.firming_fv;

import android.os.Bundle;
import android.view.View;

import com.croowly.firming_fv.root.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class StartActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.startVerificationBT)
    public void startVerificationBT(View view) {
        startNewActivity(getApplicationContext(),FormActivity.class,null,true);
    }


}
