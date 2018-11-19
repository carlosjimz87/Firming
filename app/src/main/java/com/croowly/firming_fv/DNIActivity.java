package com.croowly.firming_fv;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.croowly.firming_fv.camera.Control;
import com.croowly.firming_fv.camera.ControlView;
import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.managers.PrefsManager;
import com.croowly.firming_fv.root.BaseActivity;
import com.croowly.firming_fv.tasks.DNITask;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.croowly.firming_fv.entities.Verification.STATUS.FAILED;
import static com.croowly.firming_fv.entities.Verification.STATUS.SUCCESSFULL;
import static com.croowly.firming_fv.entities.Verification.STEP.ID;

/**
 * Created by carlosjimz on 05/02/2018.
 */

public class DNIActivity extends BaseActivity implements View.OnClickListener, ControlView.Callback, DNITask.ProgressListener{
    public DNITask dniTask = null;

    @BindView(R.id.loading_text)
    TextView loadingText;


    private Verification verification = null;
    private CameraView camera;
    private ViewGroup controlPanel;

    private boolean mCapturingPicture;
    private boolean mCapturingVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dni_layout);
        ButterKnife.bind(this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

//        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);

        camera = findViewById(R.id.cameraView);
        camera.setFacing(Facing.BACK);

        processBundle();

//        camera.addCameraListener(new CameraListener() {
//            public void onCameraOpened(CameraOptions options) { onOpened(); }
////            public void onPictureTaken(byte[] jpeg) { onPicture(jpeg); }
//
//            @Override
//            public void onVideoTaken(File video) {
//                super.onVideoTaken(video);
////                onVideo(video);
//            }
//        });

//        findViewById(R.id.edit).setOnClickListener(this);
//        findViewById(R.id.capturePhoto).setOnClickListener(this);
//        findViewById(R.id.captureVideo).setOnClickListener(this);
//        findViewById(R.id.toggleCamera).setOnClickListener(this);

//        controlPanel = findViewById(R.id.controls);
//        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
//        Control[] controls = Control.values();
//        for (Control control : controls) {
//            ControlView view = new ControlView(this, control, this);
//            group.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//
//        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
//                b.setState(BottomSheetBehavior.STATE_HIDDEN);
//            }
//        });

        dniTask = new DNITask(this);



    }

    public void processBundle(){

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
            return;

        verification = (Verification)bundle.getSerializable(Verification.class.toString());


    }




    private void message(String content, boolean important) {
        int length = important ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(this, content, length).show();
    }

    private void onOpened() {
//        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
//        for (int i = 0; i < group.getChildCount(); i++) {
//            ControlView view = (ControlView) group.getChildAt(i);
//            view.onCameraOpened(camera);
//        }
    }

    //    private void onPicture(byte[] jpeg) {
//        mCapturingPicture = false;
//        long callbackTime = System.currentTimeMillis();
//        if (mCapturingVideo) {
//            message("Captured while taking video. Size="+mCaptureNativeSize, false);
//            return;
//        }
//
//        // This can happen if picture was taken with a gesture.
//        if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
//        if (mCaptureNativeSize == null) mCaptureNativeSize = camera.getPictureSize();
//
//        PicturePreviewActivity.setImage(jpeg);
//        Intent intent = new Intent(FaceActivity.this, PicturePreviewActivity.class);
//        intent.putExtra("delay", callbackTime - mCaptureTime);
//        intent.putExtra("nativeWidth", mCaptureNativeSize.getWidth());
//        intent.putExtra("nativeHeight", mCaptureNativeSize.getHeight());
//        startActivity(intent);
//
//        mCaptureTime = 0;
//        mCaptureNativeSize = null;
//    }
//
//    private void onVideo(File video) {
//        mCapturingVideo = false;
//        Intent intent = new Intent(CameraActivity.this, VideoPreviewActivity.class);
//        intent.putExtra("video", Uri.fromFile(video));
//        startActivity(intent);
//    }
//
    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.edit: edit(); break;
//            case R.id.capturePhoto: capturePhoto(); break;
//            case R.id.captureVideo: captureVideo(); break;
//            case R.id.toggleCamera: toggleCamera(); break;
//        }
    }
//
//    @Override
//    public void onBackPressed() {
//        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
//        if (b.getState() != BottomSheetBehavior.STATE_HIDDEN) {
//            b.setState(BottomSheetBehavior.STATE_HIDDEN);
//            return;
//        }
//        super.onBackPressed();
//    }
//
//    private void edit() {
//        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
//        b.setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }

//    private void capturePhoto() {
//        if (mCapturingPicture) return;
//        mCapturingPicture = true;
//        long mCaptureTime = System.currentTimeMillis();
//        Size mCaptureNativeSize = camera.getPictureSize();
//        message("Capturing picture...", false);
//        camera.capturePicture();
//    }

//    private void captureVideo() {
//        if (camera.getSessionType() != SessionType.VIDEO) {
//            message("Can't record video while session type is 'picture'.", false);
//            return;
//        }
//        if (mCapturingPicture || mCapturingVideo) return;
//        mCapturingVideo = true;
//        message("Recording for 8 seconds...", true);
//        camera.startCapturingVideo(null, 8000);
//    }

//    private void toggleCamera() {
//        if (mCapturingPicture) return;
//        switch (camera.toggleFacing()) {
//            case BACK:
//                message("Switched to back camera!", false);
//                break;
//
//            case FRONT:
//                message("Switched to front camera!", false);
//                break;
//        }
//    }

    @Override
    public boolean onValueChanged(Control control, Object value, String name) {
        if (!camera.isHardwareAccelerated() && (control == Control.WIDTH || control == Control.HEIGHT)) {
            if ((Integer) value > 0) {
                message("This device does not support hardware acceleration. " +
                        "In this case you can not change width or height. " +
                        "The view will act as WRAP_CONTENT by default.", true);
                return false;
            }
        }
        control.applyValue(camera, value);
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_HIDDEN);
        message("Changed " + control.getName() + " to " + name, false);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        camera.start();

        if(dniTask!=null)
            dniTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();

        if(dniTask!=null)
            dniTask.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();

        if(dniTask!=null)
            dniTask.cancel(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isStarted()) {
            camera.start();
        }
    }



    @Override
    public void progressTask(DNITask.Modes state) {


        switch (state){
            case SEARCHING:

                loadingText.setText(getResources().getString(R.string.searching_dni));
                loadingText.setTextColor(getResources().getColor(R.color.firmingWhite));
                break;


            case CLASSIFYING:
                loadingText.setText(getResources().getString(R.string.classifying_dni));
                loadingText.setTextColor(getResources().getColor(R.color.firmingWhite));
                break;

            case VERIFYING:
                loadingText.setText(getResources().getString(R.string.verifying_dni));
                loadingText.setTextColor(getResources().getColor(R.color.firmingWhite));
                break;

            case SUCCESSFUL:
                loadingText.setTextColor(getResources().getColor(R.color.firmingAccent));
                Class<?> nextActivity = null;
                if(verification!=null) {
                    verification.setStep(Verification.STEP.ID);
                    if(verification.getStatus()==Verification.STATUS.INIT) {
                        verification.setStatus(Verification.STATUS.INTERMEDIATE);
                        nextActivity = PreDNIActivity.class;
                    }
                    else{
                        verification.setStatus(Verification.STATUS.SUCCESSFULL);
                        nextActivity = VerifResultActivity.class;
                    }

                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(Verification.class.toString(), verification);
                startNewActivity(DNIActivity.this,nextActivity,bundle,true);
                break;

            case FAILED:
                loadingText.setTextColor(getResources().getColor(R.color.firmingAccent));
                if(verification!=null)
                    verification.setStatus(FAILED);
                break;

            default:
                if(verification!=null)
                    verification.setStep(ID);
                break;

        }


    }




}
