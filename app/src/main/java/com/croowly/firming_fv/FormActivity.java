package com.croowly.firming_fv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.Gravity;

import com.croowly.firming_fv.entities.Verification;
import com.croowly.firming_fv.managers.PrefsManager;
import com.heinrichreimersoftware.singleinputform.SingleInputFormActivity;
import com.heinrichreimersoftware.singleinputform.steps.CheckBoxStep;
import com.heinrichreimersoftware.singleinputform.steps.DateStep;
import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.heinrichreimersoftware.singleinputform.steps.TextStep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by carlosjimz on 29/01/2018.
 */

public class FormActivity extends SingleInputFormActivity {

    private static final String DATA_KEY_EULA = "eula";
    private static final String DATA_KEY_ID = "ID";
    private static final String DATA_KEY_PASSWORD = "password";
    private static final String DATA_KEY_EMAIL = "email";
    private static final String DATA_PHONE_NUMBER = "phone";
    private static final String DATA_KEY_BIRTHDAY = "birthday";
    private static final int LegalAge = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/avenir_light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    protected void onResume() {
        super.onResume();

//        this.setContentView(R.layout.form_layout);
    }

    @Override
    protected List<Step> onCreateSteps(){
        List<Step> steps = new ArrayList<>();

        setInputGravity(Gravity.CENTER);

        steps.add(new CheckBoxStep.Builder(this, DATA_KEY_EULA)
                .titleResId(R.string.eula_title)
                .errorResId(R.string.eula_error)
                .detailsResId(R.string.eula_details)
                .textResId(R.string.eula)
                .validator(new CheckBoxStep.Validator() {
                    @Override
                    public boolean validate(boolean input) {
                        return input;
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_ID)
                .titleResId(R.string.id_title)
                .errorResId(R.string.id_error)
                .detailsResId(R.string.id_details)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_PASSWORD)
                .titleResId(R.string.password)
                .errorResId(R.string.password_error)
                .detailsResId(R.string.password_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return input.length() >= 5;
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_EMAIL)
                .titleResId(R.string.email)
                .errorResId(R.string.email_error)
                .detailsResId(R.string.email_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
                    }
                })
                .build());


        steps.add(new TextStep.Builder(this, DATA_PHONE_NUMBER)
                .titleResId(R.string.phone_title)
                .errorResId(R.string.phone_error)
                .detailsResId(R.string.phone_details)
                .inputType(InputType.TYPE_CLASS_PHONE)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return Patterns.PHONE.matcher(input).matches();
                    }
                })
                .build());


        steps.add(new DateStep.Builder(this, DATA_KEY_BIRTHDAY)
                .titleResId(R.string.birthdate)
                .errorResId(R.string.birthdate_error)
                .detailsResId(R.string.birthdate_details)
                .validator(new DateStep.Validator() {
                    @Override
                    public boolean validate(int year, int month, int day) {
                        Calendar today = new GregorianCalendar();
                        Calendar birthday = new GregorianCalendar(year, month, day);
                        today.add(Calendar.YEAR, -LegalAge);
                        return today.after(birthday);
                    }
                })
                .build());



        return steps;
    }

//    @Override
//    protected View onCreateFinishedView(LayoutInflater inflater, ViewGroup parent) {
////        return inflater.inflate(R.layout.form_completed_layout, parent, true);
//    }

    public void startNewActivity(Context fromActivity, Class<?> toActivityClass, Bundle extras, boolean anim) {
        Intent intent = new Intent(fromActivity, toActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (extras != null)
            intent.putExtras(extras);
        startActivity(intent);
        if (anim)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    protected void onFormFinished(Bundle data) {

        Verification verification = new Verification(this,
                TextStep.text(data,DATA_KEY_ID),
                TextStep.text(data,DATA_KEY_EMAIL),
                DateStep.text(data,DATA_KEY_BIRTHDAY),
                TextStep.text(data,DATA_KEY_PASSWORD),
                Integer.parseInt(TextStep.text(data,DATA_PHONE_NUMBER)), Verification.STATUS.INIT,Verification.STEP.NONE);

        PrefsManager.configPrefs(this);
        PrefsManager.saveEmail(TextStep.text(data,DATA_KEY_EMAIL));
        Bundle bundle = new Bundle();
        bundle.putSerializable(Verification.class.toString(), verification);
        startNewActivity(FormActivity.this, CompletedFormActivity.class, bundle, true);
        FormActivity.this.finish();

    }
}
