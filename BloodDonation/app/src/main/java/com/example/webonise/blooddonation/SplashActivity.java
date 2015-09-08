package com.example.webonise.blooddonation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.webonise.blooddonation.app.Constant;

/**
 * Created by webonise on 1/9/15.
 */
public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    SharedPreferences pref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread logoTimer = new Thread() {
            public void run(){
                try{
                    int logoTimer = 0;
                    while(logoTimer < 5000){
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    pref = getApplicationContext().getSharedPreferences(Constant.PREFER, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    if(pref.getInt(getString(R.string.checkFlag),1)==1)
                    {  editor.putInt(getString(R.string.checkFlag),2);
                        editor.commit();
                        Intent register = new Intent(SplashActivity.this,RegistrationActivity.class);
                        register.putExtra(getString(R.string.btnText),getString(R.string.Register));
                        startActivity(register);

                    }
                    else {
                        Intent search = new Intent(SplashActivity.this,SearchActivity.class);
                        search.putExtra(getString(R.string.btnText),getString(R.string.Search));
                        startActivity(search);
                        }

                }

                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                finally{
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}