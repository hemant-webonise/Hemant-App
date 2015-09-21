package com.example.webonise.blooddonation;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.webonise.blooddonation.app.Constant;
import org.json.JSONException;
import org.json.JSONObject;



public class FillRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, etEmailAddress, etNumber;
    TextView tvBlood;
    Switch privacyMode;
    SharedPreferences pref;
    Button btn;
    Bundle bundle;
    SharedPreferences.Editor editor;
    GPSTracker gps;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitivity);

        pref = getApplicationContext().getSharedPreferences(Constant.PREFER, MODE_PRIVATE);
        initialize();
        bundle = getIntent().getExtras();
        btn.setText(bundle.getString(getString(R.string.btnText)));
        if (btn.getText().toString().equalsIgnoreCase(getString(R.string.update))) {
            toolbar.setTitle(getString(R.string.update));
            toolbar.setSubtitle(getString(R.string.update_sutitle));
            setSupportActionBar(toolbar);
            retrieveStoredStates();
        }else{
            //Title and subtitle
            toolbar.setTitle(getString(R.string.register));
            toolbar.setSubtitle(getString(R.string.register_subtitle));
            setSupportActionBar(toolbar);
        }
        setClickListeners();
    }

    private void retrieveStoredStates() {
        etEmailAddress.setText(pref.getString("etEmailAddress", null));
        etNumber.setText(pref.getString("etNumber", null));
        privacyMode.setChecked(pref.getBoolean("privacy", false));
        name.setText(pref.getString("name",null));
        tvBlood.setText(pref.getString("tvBlood", null));
    }

    private void initialize() {
        etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
        etNumber = (EditText)findViewById(R.id.etNumber);
        privacyMode = (Switch) findViewById(R.id.PrivacyMode);
        name = (EditText) findViewById(R.id.name);
        tvBlood = (TextView) findViewById(R.id.tvBlood);
        btn = (Button) findViewById(R.id.btnRegister);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setClickListeners() {
        privacyMode.setOnClickListener(this);
        tvBlood.setOnClickListener(this);
        btn.setOnClickListener(this);
        name.setOnClickListener(this);
        etEmailAddress.setOnClickListener(this);
        etNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

      /*  Toast.makeText(RegistrationActivity.this, "Old value in Shared-Preferences" + pref.getString("name", "Could not be restored"), Toast.LENGTH_LONG).show();*/
        switch (view.getId()) {
            case R.id.name:
                name.setText("");
                break;
            case R.id.tvBlood:
                final CharSequence BloodGroup[] = new CharSequence[]{"O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.pick_blood))
                        .setItems(BloodGroup, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on colors[which]
                                Toast.makeText(getBaseContext(), BloodGroup[which], Toast.LENGTH_LONG).show();
                                tvBlood.setText(BloodGroup[which]);
                            }
                        })
                        .show();
                break;
            case R.id.etEmailAddress:
                etEmailAddress.setText("");
                break;
            case R.id.etNumber:
                etNumber.setText("");
                break;
            case R.id.PrivacyMode:
                if (privacyMode.isChecked()){
                    editor = pref.edit();
                   /* setImage();*/
                    try {
                        JSONObject regitration = getJsonRegistration();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editor.putBoolean("privacy", true);
                    editor.commit();
                }
                else
                {   editor = pref.edit();
                    editor.putBoolean("privacy", false);
                    editor.commit();
                }
                break;
            case R.id.btnRegister:
                if (btn.getText().toString().equalsIgnoreCase(getString(R.string.register))) {
                    if(validateFields()){
                    putInSharedPreferences();
                    /*Registration Done So now we change the flag and so it would be able to go to next screen*/

                    editor.putInt(getString(R.string.checkFlag), 2);
                    editor.commit();
                    moveToSearchActivity();
                    }
                } else if (btn.getText().toString().equalsIgnoreCase(getString(R.string.update))) {
                    if(validateFields()){
                   /* retrieveStoredStates();*/
                    putInSharedPreferences();
                    moveToSearchActivity();
                    }
                }
                break;
        }
    }

    private JSONObject getJsonRegistration() throws JSONException {
        JSONObject outer = new JSONObject();
        JSONObject user = new JSONObject();

        user.put("name",  name.getText().toString());
        user.put(Constant.TAG_bloodGroup,  tvBlood.getText().toString());
        user.put("phone",  etNumber.getText().toString());
        user.put("email", etEmailAddress.getText().toString());

        outer.put("user", user);
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        if (gpsTracker.canGetLocation()) {
            outer.put(Constant.TAG_Lat, gpsTracker.getLatitude());
            outer.put(Constant.TAG_Lng, gpsTracker.getLongitude());
        } else {
            gpsTracker.showSettingsAlert();
        }
        Toast.makeText(this, outer.toString(), Toast.LENGTH_LONG).show();
        return outer;
    }

    private void moveToSearchActivity() {
        Intent searchActivity = new Intent(this, SearchActivity.class);
        startActivity(searchActivity);
        finish();
    }

    private boolean validateFields() {
        if(isEmpty(name)){
            name.setError(getString(R.string.check_name));
        }
        else if(isEmpty(tvBlood)){

            Toast.makeText(this,getString(R.string.check_blood_group),Toast.LENGTH_LONG).show();
           /* tvBlood.setError("Please Select Blood Type.");*/
        }
        else if (isEmpty(etNumber)){
            etNumber.setError(getString(R.string.check_phone));
        }
        else if (!isValidPhoneNumber(etNumber.getText())){
            etNumber.setError(getString(R.string.check_valid_phone));
        }
        else if (isEmpty(etEmailAddress)){
            etEmailAddress.setError(getString(R.string.check_email_address));
        }
        else if (!isValidEmail(etEmailAddress.getText())){
            etEmailAddress.setError(getString(R.string.check_email_address));
            Toast.makeText(this,getString(R.string.emailFormat),Toast.LENGTH_LONG).show();
        }
        else{
            return true;
        }

        return false;
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private void putInSharedPreferences() {
        editor = pref.edit();
       /* editor.putString("privacy", privacyMode);*/
        editor.putString("name", name.getText().toString());
        editor.putString("tvBlood", tvBlood.getText().toString());
        editor.putString("etEmailAddress", etEmailAddress.getText().toString());
        editor.putString("etNumber", etNumber.getText().toString());
        editor.commit();
    }

    private boolean isEmpty(EditText input) {
        return input.getText().toString().trim().length() == 0;
    }
    private boolean isEmpty(TextView input) {
        return input.getText().toString().trim().length() == 0;
    }


}
