package com.example.webonise.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_acitvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner bloodSelection=(Spinner) findViewById(R.id.spinner);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        bloodSelection.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_acitvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_LONG).show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.privacy) {
            Toast.makeText(getApplicationContext(),"Privacy",Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.editProfile) {
            Toast.makeText(getApplicationContext(),"Update Profile",Toast.LENGTH_LONG).show();
            startActivity(new Intent("com.example.webonise.blooddonation.REGISTRATIONACTIVITY").putExtra("btnText","Update"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
       switch(view.getId()){
            case R.id.btnSearch :
                startActivity(new Intent(SearchActivity.this,PagerActivity.class)/*.putExtra("btnText","Update")*/);
                break;

        }
    }

    private class CustomOnItemSelectedListener implements  AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(adapterView.getContext(),
                    "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
