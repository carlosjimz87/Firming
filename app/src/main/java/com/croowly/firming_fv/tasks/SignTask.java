package com.croowly.firming_fv.tasks;

import android.os.AsyncTask;

import com.croowly.firming_fv.SignActivity;

/**
 * Created by CHARS on 10/3/2016.
 */
public class SignTask extends AsyncTask<Void, Integer, Boolean> {

    public enum Modes { SIGNED, CLEARED, SENDING , PROCESSING,  VERIFYING , SUCCESSFUL, FAILED   }

    private boolean bCompleted = false;
    private SignTask.ProgressListener<String> callback;

    public SignTask(SignActivity context) {

        this.callback = context;
    }

    @Override
    protected void onPreExecute() {
        bCompleted = false;

        publishProgress(2);
//        callback.progressTask(DNIModes.SEARCHING);
    }


    @Override
    protected Boolean doInBackground(Void... arg0) {

        sleep();
        publishProgress(3);
        sleep();
        publishProgress(4);
        sleep();

        bCompleted = true;

        return true;
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        switch (values[0]){
            case 2:
                callback.progressTask(Modes.SENDING);
                break;
            case 3:
                callback.progressTask(Modes.PROCESSING);
                break;
            case 4:
                callback.progressTask(Modes.VERIFYING);
                break;
            case 5:
                callback.progressTask(Modes.SUCCESSFUL);
                break;
            case 6:
                callback.progressTask(Modes.FAILED);
                break;

        }

    }

    @Override
    protected void onPostExecute(final Boolean status) {

        if (bCompleted) {

            publishProgress(5);
        }

    }

    @Override
    protected void onCancelled() {

        publishProgress(6);
    }


    public interface ProgressListener<String>  {

        void progressTask(Modes mode);

    }


}