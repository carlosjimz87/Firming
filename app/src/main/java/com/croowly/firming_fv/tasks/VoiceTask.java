package com.croowly.firming_fv.tasks;

import android.os.AsyncTask;

import com.croowly.firming_fv.FaceActivity;
import com.croowly.firming_fv.VoiceActivity;

/**
 * Created by carlosjimz on 07/02/2018.
 */

public class VoiceTask extends AsyncTask<Void, Integer, Boolean> {

    public enum Modes { SENDING , PROCESSING , VERIFYING , SUCCESSFUL, FAILED   }

    private boolean bCompleted = false;
    private VoiceTask.ProgressListener<String> callback;

    public VoiceTask(VoiceActivity context) {

        this.callback = context;
    }

    @Override
    protected void onPreExecute() {
        bCompleted = false;

        publishProgress(0);
//        callback.progressTask(DNIModes.SEARCHING);
    }


    @Override
    protected Boolean doInBackground(Void... arg0) {

        sleep();
        publishProgress(1);
        sleep();
        publishProgress(2);
        sleep();

        bCompleted = true;

        return true;
    }

    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        switch (values[0]){
            case 0:
                callback.progressTask(Modes.SENDING);
                break;
            case 1:
                callback.progressTask(Modes.PROCESSING);
                break;
            case 2:
                callback.progressTask(Modes.VERIFYING);
                break;
            case 3:
                callback.progressTask(Modes.SUCCESSFUL);
                break;
            case 4:
                callback.progressTask(Modes.FAILED);
                break;

        }

    }

    @Override
    protected void onPostExecute(final Boolean status) {

        if (bCompleted) {

            publishProgress(3);
        }

    }

    @Override
    protected void onCancelled() {

        publishProgress(4);
    }



    public interface ProgressListener<String>  {

        void progressTask(VoiceTask.Modes mode);

    }

}
