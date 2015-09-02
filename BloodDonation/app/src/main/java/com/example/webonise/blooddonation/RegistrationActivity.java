package com.example.webonise.blooddonation;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegistrationActivity extends Activity implements View.OnClickListener {
    EditText name;
    final String PREFER = "prefer";
    SharedPreferences pref;
    Button btn;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitivity);
        pref = getApplicationContext().getSharedPreferences(PREFER, MODE_PRIVATE);
        name = (EditText)findViewById(R.id.etName);
        name.setText(pref.getString("name", "Could not be restored"));
        btn= (Button)findViewById(R.id.btnRegister);
        bundle = getIntent().getExtras();
        btn.setText(bundle.getString("btnText"));
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        putNameInSharedPreferences();
      /*  Toast.makeText(RegistrationActivity.this, "Old value in Shared-Preferences" + pref.getString("name", "Could not be restored"), Toast.LENGTH_LONG).show();*/

        if(btn.getText().toString().equalsIgnoreCase("Register")){
            Toast.makeText(getApplicationContext(),"R",Toast.LENGTH_LONG).show();
        }
        else if (btn.getText().toString().equalsIgnoreCase("Update")){
            Toast.makeText(getApplicationContext(),"U",Toast.LENGTH_LONG).show();
        }
        Intent searchActivity = new Intent(this, SearchActivity.class);
        startActivity(searchActivity);
    }

    private void putNameInSharedPreferences() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", name.getText().toString());
        editor.commit();
    }
}
