package com.example.webonise.blooddonation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationActivity extends Activity implements View.OnClickListener {
    EditText name, etEmailAddress, etNumber;
    TextView tvBlood;
    Switch privacyMode;
    final String PREFER = "prefer";
    SharedPreferences pref;
    Button btn;
    Bundle bundle;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitivity);
        pref = getApplicationContext().getSharedPreferences(PREFER, MODE_PRIVATE);
        privacyMode = (Switch) findViewById(R.id.PrivacyMode);
        privacyMode.setChecked(pref.getBoolean("privacy", false));
        privacyMode.setOnClickListener(this);
        name = (EditText) findViewById(R.id.name);
        name.setText(pref.getString("name", "Could not be restored"));
        tvBlood = (TextView) findViewById(R.id.tvBlood);
        tvBlood.setText(pref.getString("tvBlood", "Could not be restored"));
        btn = (Button) findViewById(R.id.btnRegister);
        bundle = getIntent().getExtras();
        btn.setText(bundle.getString("btnText"));
        tvBlood.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        putNameInSharedPreferences();
      /*  Toast.makeText(RegistrationActivity.this, "Old value in Shared-Preferences" + pref.getString("name", "Could not be restored"), Toast.LENGTH_LONG).show();*/
        switch (view.getId()) {
            case R.id.tvBlood:
                final CharSequence BloodGroup[] = new CharSequence[]{"O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+",};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pick a color")
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
            case R.id.PrivacyMode:
                if (privacyMode.isChecked()){
                    editor.putBoolean("privacy", true);
                    editor.commit();
                }
                else
                {   editor.putBoolean("privacy", false);
                    editor.commit();
                }

                break;
            case R.id.btnRegister:
                if (btn.getText().toString().equalsIgnoreCase("Register")) {
                    Toast.makeText(getApplicationContext(), "You have registered properly !", Toast.LENGTH_LONG).show();
                } else if (btn.getText().toString().equalsIgnoreCase("Update")) {
                    Toast.makeText(getApplicationContext(), "You hae Updated properly !", Toast.LENGTH_LONG).show();
                }
                Intent searchActivity = new Intent(this, SearchActivity.class);
                startActivity(searchActivity);
                finish();
                break;
        }
    }

    private void putNameInSharedPreferences() {
        editor = pref.edit();
       /* editor.putString("privacy", privacyMode);*/
        editor.putString("name", name.getText().toString());
        editor.putString("tvBlood", tvBlood.getText().toString());
        editor.commit();
    }


}
