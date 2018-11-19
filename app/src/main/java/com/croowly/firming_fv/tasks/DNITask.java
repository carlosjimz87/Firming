package com.croowly.firming_fv.tasks;

import android.os.AsyncTask;

import com.croowly.firming_fv.DNIActivity;

/**
 * Created by carlosjimz on 07/02/2018.
 */

public class DNITask extends AsyncTask<Void, Integer, Boolean> {

    public enum Modes { SEARCHING , CLASSIFYING , VERIFYING , SUCCESSFUL, FAILED   }

    private boolean bCompleted = false;
    private ProgressListener<String> callback;

    public DNITask(DNIActivity context) {

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

        sleep(7000);
        publishProgress(1);
        sleep(4000);
        publishProgress(2);
        sleep(5000);
        publishProgress(3);
        sleep(6000);

        bCompleted = true;

        return true;
    }

    private void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        switch (values[0]){
            case 0:
                callback.progressTask(Modes.SEARCHING);
                break;
            case 1:
                callback.progressTask(Modes.CLASSIFYING);
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

            publishProgress(4);
//            callback.progressTask(DNIModes.SUCCESSFUL);
        }

    }

    @Override
    protected void onCancelled() {

        publishProgress(5);
//        callback.progressTask(DNIModes.FAILED);
    }



    public interface ProgressListener<String>  {

        void progressTask(Modes mode);

    }

}
