package com.croowly.firming_fv;

import android.Manifest;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.croowly.firming_fv.audiorecorder.AndroidAudioRecorder;
import com.croowly.firming_fv.audiorecorder.Util;
import com.croowly.firming_fv.audiorecorder.VisualizerHandler;
import com.croowly.firming_fv.audiorecorder.model.AudioChannel;
import com.croowly.firming_fv.audiorecorder.model.AudioSampleRate;
import com.croowly.firming_fv.audiorecorder.model.AudioSource;
import com.croowly.firming_fv.entities.Utils;
import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.root.BaseActivity;
import com.croowly.firming_fv.tasks.FaceTask;
import com.croowly.firming_fv.tasks.VoiceTask;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;


public class VoiceActivity extends BaseActivity
        implements PullTransport.OnAudioChunkPulledListener, MediaPlayer.OnCompletionListener, VoiceTask.ProgressListener {
    public VoiceTask voiceTask = null;
    private Verification verification = null;

    @BindView(R.id.waveLY) RelativeLayout waveLY;
    @BindView(R.id.phrase) TextView phraseView;
    @BindView(R.id.timer) TextView timerView;
    @BindView(R.id.loading_text) TextView loadingText;
    @BindView(R.id.record) ImageButton recordView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;


    private String filePath;
    private AudioSource source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;
    private int color;
    private boolean autoStart;

    //    private MediaPlayer player;
    private Recorder recorder;
    private VisualizerHandler visualizerHandler;

    private Timer timer;
    //    private MenuItem saveMenuItem;
    private int recorderSecondsElapsed;
    private boolean isRecording;

    private GLAudioVisualizationView visualizerView;
    //    private TextView statusView;
    //    private ImageButton playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_activity);
        ButterKnife.bind(this);


        Utils.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Utils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

//        startVoiceActivity();

        boolean keepDisplayOn = false;

        filePath = AUDIO_FILE_PATH;
        source = AudioSource.MIC;
        channel =AudioChannel.STEREO;
        sampleRate = AudioSampleRate.HZ_48000;
        autoStart = false;
//        if(savedInstanceState != null) {
//            filePath = savedInstanceState.getString(AndroidAudioRecorder.EXTRA_FILE_PATH);
//            source = (AudioSource) savedInstanceState.getSerializable(AndroidAudioRecorder.EXTRA_SOURCE);
//            channel = (AudioChannel) savedInstanceState.getSerializable(AndroidAudioRecorder.EXTRA_CHANNEL);
//            sampleRate = (AudioSampleRate) savedInstanceState.getSerializable(AndroidAudioRecorder.EXTRA_SAMPLE_RATE);
//            color = savedInstanceState.getInt(AndroidAudioRecorder.EXTRA_COLOR);
//            autoStart = savedInstanceState.getBoolean(AndroidAudioRecorder.EXTRA_AUTO_START);
//            keepDisplayOn = savedInstanceState.getBoolean(AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON);
//        } else {
//            filePath = getIntent().getStringExtra(AndroidAudioRecorder.EXTRA_FILE_PATH);
//            source = (AudioSource) getIntent().getSerializableExtra(AndroidAudioRecorder.EXTRA_SOURCE);
//            channel = (AudioChannel) getIntent().getSerializableExtra(AndroidAudioRecorder.EXTRA_CHANNEL);
//            sampleRate = (AudioSampleRate) getIntent().getSerializableExtra(AndroidAudioRecorder.EXTRA_SAMPLE_RATE);
//            color = getIntent().getIntExtra(AndroidAudioRecorder.EXTRA_COLOR, Color.BLACK);
//            autoStart = getIntent().getBooleanExtra(AndroidAudioRecorder.EXTRA_AUTO_START, false);
//            keepDisplayOn = getIntent().getBooleanExtra(AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON, false);
//        }

        if(keepDisplayOn){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setElevation(0);
//            getSupportActionBar().setBackgroundDrawable(
//                    new ColorDrawable(Util.getDarkerColor(color)));
//            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.aar_ic_clear));
//        }

        color = getResources().getColor(R.color.colorPrimaryDark);

        visualizerView = new GLAudioVisualizationView.Builder(this)
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(R.dimen.aar_wave_height)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .build();

        RelativeLayout contentLayout = (RelativeLayout) findViewById(R.id.content);
//        statusView = (TextView) findViewById(R.id.status);
//        restartView = (ImageButton) findViewById(R.id.restart);
//        playView = (ImageButton) findViewById(R.id.play);

      //  contentLayout.setBackgroundColor(Util.getDarkerColor(color));
        contentLayout.addView(visualizerView, 0);
//        restartView.setVisibility(View.INVISIBLE);
//        playView.setVisibility(View.INVISIBLE);

        if(Util.isBrightColor(color)) {
//            ContextCompat.getDrawable(this, R.drawable.aar_ic_clear)
//                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//            ContextCompat.getDrawable(this, R.drawable.aar_ic_check)
//                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            //statusView.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
           // restartView.setColorFilter(Color.BLACK);
            recordView.setColorFilter(Color.BLACK);
           // playView.setColorFilter(Color.BLACK);
        }

        processBundle();
    }


    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());

    }



    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(autoStart && !isRecording){
            toggleRecording(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            visualizerView.onResume();
        } catch (Exception e){ }


    }

    @Override
    protected void onPause() {
        restartRecording(null);
        try {
            visualizerView.onPause();
        } catch (Exception e){ }
        super.onPause();


        if(voiceTask!=null)
            voiceTask.cancel(true);
    }

    @Override
    protected void onDestroy() {
        restartRecording(null);
        setResult(RESULT_CANCELED);
        try {
            visualizerView.release();
        } catch (Exception e){ }
        super.onDestroy();


        if(voiceTask!=null)
            voiceTask.cancel(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(AndroidAudioRecorder.EXTRA_FILE_PATH, filePath);
        outState.putInt(AndroidAudioRecorder.EXTRA_COLOR, color);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.aar_audio_recorder, menu);
//        saveMenuItem = menu.findItem(R.id.action_save);
//        saveMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.aar_ic_check));
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int i = item.getItemId();
//        if (i == android.R.id.home) {
//            finish();
//        } else if (i == R.id.action_save) {
//            selectAudio();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
        visualizerHandler.onDataReceived(amplitude);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        if(voiceTask!=null)
            voiceTask.execute();
    }

    private void selectAudio() {
        stopRecording();
        setResult(RESULT_OK);
        finish();
    }

    public void toggleRecording(View v) {
//        stopPlaying();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    pauseRecording();
                } else {
                    resumeRecording();
                }
            }
        });
    }

//    public void togglePlaying(View v){
//        pauseRecording();
//        Util.wait(100, new Runnable() {
//            @Override
//            public void run() {
//                if(isPlaying()){
//                    stopPlaying();
//                } else {
//                    startPlaying();
//                }
//            }
//        });
//    }

    public void restartRecording(View v){
        if(isRecording) {
            stopRecording();
        }
//        else if(isPlaying()) {
//            stopPlaying();
//        }
        else {
            visualizerHandler = new VisualizerHandler();
            visualizerView.linkTo(visualizerHandler);
            visualizerView.release();
            if(visualizerHandler != null) {
                visualizerHandler.stop();
            }
        }
//        saveMenuItem.setVisible(false);
//        statusView.setVisibility(View.INVISIBLE);
//        restartView.setVisibility(View.INVISIBLE);
//        playView.setVisibility(View.INVISIBLE);
//        recordView.setImageResource(R.drawable.aar_ic_rec);
        timerView.setText(getResources().getString(R.string.voice_timer));
        recorderSecondsElapsed = 0;
        int playerSecondsElapsed = 0;
    }

    private void resumeRecording() {
        isRecording = true;
//        saveMenuItem.setVisible(false);
//        statusView.setText(R.string.aar_recording);
//        statusView.setVisibility(View.VISIBLE);
//        restartView.setVisibility(View.INVISIBLE);
//        playView.setVisibility(View.INVISIBLE);
//        recordView.setImageResource(R.drawable.aar_ic_pause);
//        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerHandler = new VisualizerHandler();
        visualizerView.linkTo(visualizerHandler);

        if(recorder == null) {
            timerView.setText(getResources().getString(R.string.voice_timer));

            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(source, channel, sampleRate), VoiceActivity.this),
                    new File(filePath));
        }
        recorder.resumeRecording();

        startTimer();
    }

    private void pauseRecording() {
        isRecording = false;
//        if(!isFinishing()) {
//            saveMenuItem.setVisible(true);
//        }
//        statusView.setText(R.string.aar_paused);
//        statusView.setVisibility(View.VISIBLE);
//        restartView.setVisibility(View.VISIBLE);
//        playView.setVisibility(View.VISIBLE);
//        recordView.setImageResource(R.drawable.aar_ic_rec);
//        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if(visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (recorder != null) {
            recorder.pauseRecording();
        }

        stopTimer();
    }

    private void stopRecording(){
        visualizerView.release();
        if(visualizerHandler != null) {
            visualizerHandler.stop();
        }

        recorderSecondsElapsed = 0;
        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
        }

        stopTimer();
        voiceTask = new VoiceTask(this);
        voiceTask.execute();
    }
    private void startTimer(){
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);

    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isRecording) {
                    recorderSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(recorderSecondsElapsed));
                    if(recorderSecondsElapsed==5)
                        stopRecording();
                }
//                else if(isPlaying()){
//                    playerSecondsElapsed++;
//                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));
//                }
            }
        });
    }

//    private void startPlaying(){
//        try {
//            stopRecording();
//            player = new MediaPlayer();
//            player.setDataSource(filePath);
//            player.prepare();
//            player.start();
//
//            visualizerView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, player));
//            visualizerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    player.setOnCompletionListener(AudioRecorderActivity.this);
//                }
//            });
//
//            timerView.setText("00:00:00");
//            statusView.setText(R.string.aar_playing);
//            statusView.setVisibility(View.VISIBLE);
////            playView.setImageResource(R.drawable.aar_ic_stop);
//
//            playerSecondsElapsed = 0;
//            startTimer();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void stopPlaying(){
//        statusView.setText("");
//        statusView.setVisibility(View.INVISIBLE);
////        playView.setImageResource(R.drawable.aar_ic_play);
//
//        visualizerView.release();
//        if(visualizerHandler != null) {
//            visualizerHandler.stop();
//        }
//
//        if(player != null){
//            try {
//                player.stop();
//                player.reset();
//            } catch (Exception e){ }
//        }
//
//        stopTimer();
//    }

//    private boolean isPlaying(){
//        try {
//            return player != null && player.isPlaying() && !isRecording;
//        } catch (Exception e){
//            return false;
//        }
//    }


    @Override
    public void progressTask(VoiceTask.Modes state) {

        switch (state){
            case SENDING:
                loadingText.setText(getResources().getString(R.string.voice_mode_sending));
                loadingText.setVisibility(View.VISIBLE);
                waveLY.setVisibility(View.INVISIBLE);
                timerView.setVisibility(View.INVISIBLE);
                phraseView.setVisibility(View.INVISIBLE);
                recordView.setVisibility(View.INVISIBLE);
                animationView.setVisibility(View.VISIBLE);
                break;


            case PROCESSING:
                loadingText.setText(getResources().getString(R.string.voice_mode_processing));
                loadingText.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.INVISIBLE);
                phraseView.setVisibility(View.INVISIBLE);
                timerView.setVisibility(View.INVISIBLE);
                waveLY.setVisibility(View.INVISIBLE);
                animationView.setVisibility(View.VISIBLE);
                break;


            case VERIFYING:
                loadingText.setText(getResources().getString(R.string.voice_mode_verifying));
                loadingText.setVisibility(View.VISIBLE);
                timerView.setVisibility(View.INVISIBLE);
                phraseView.setVisibility(View.INVISIBLE);
                recordView.setVisibility(View.INVISIBLE);
                waveLY.setVisibility(View.INVISIBLE);
                animationView.setVisibility(View.VISIBLE);
                break;

            case SUCCESSFUL:
                loadingText.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.VISIBLE);
                waveLY.setVisibility(View.INVISIBLE);
                phraseView.setVisibility(View.INVISIBLE);
                recordView.setVisibility(View.INVISIBLE);
                timerView.setVisibility(View.INVISIBLE);
                verification.setStep(Verification.STEP.VOICE);
                verification.setStatus(Verification.STATUS.SUCCESSFULL);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Verification.class.toString(), verification);
                startNewActivity(VoiceActivity.this,VerifResultActivity.class,bundle,true);
                voiceTask.cancel(true);
                voiceTask=null;
                break;

            case FAILED:
                loadingText.setVisibility(View.INVISIBLE);
                animationView.setVisibility(View.INVISIBLE);
                phraseView.setVisibility(View.VISIBLE);
                recordView.setVisibility(View.VISIBLE);
                waveLY.setVisibility(View.VISIBLE);
                voiceTask.cancel(true);
                voiceTask=null;
//                startNewActivity(FaceActivity.this,FaceActivity.class,null,true);
                break;

        }




    }

}
