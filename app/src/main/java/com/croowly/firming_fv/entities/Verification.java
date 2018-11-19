package com.croowly.firming_fv.entities;

import android.content.Context;

import com.croowly.firming_fv.managers.PrefsManager;
import com.heinrichreimersoftware.singleinputform.steps.TextStep;

import java.io.Serializable;

/**
 * Created by carlosjimz on 07/02/2018.
 */

public class Verification implements Serializable{

    private String id = "";
    private String email = "";
    private String birthday = "";
    private String password = "";
    private int number = 0;


    public enum STATUS {INIT, INTERMEDIATE,  SUCCESSFULL, FAILED}
    public enum STEP {NONE, ID, FACIAL, VOICE, SIGNATURE, RESULTS}

    private STATUS status;
    private STEP step;


    public Verification(Context context, String id, String email, String birthday, String password, int number, STATUS status, STEP step) {
        this.id = id;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
        this.number = number;
        this.status = status;
        this.step = step;

        PrefsManager.configPrefs(context);
        PrefsManager.saveEmail(email);
    }

    public void setStatus(STATUS status){
        this.status = status;
    }

    public STATUS getStatus() {
        return status;
    }


    public void setStep(STEP step){
        this.step = step;
    }

    public STEP getStep() {
        return step;
    }


}
