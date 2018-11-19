package com.croowly.firming_fv.audiorecorder.model;

import android.media.AudioFormat;

/**
 * Created by carlosjimz on 11/02/2018.
 */

public enum AudioChannel {
    STEREO,
    MONO;

    public int getChannel(){
        switch (this){
            case MONO:
                return AudioFormat.CHANNEL_IN_MONO;
            default:
                return AudioFormat.CHANNEL_IN_STEREO;
        }
    }
}