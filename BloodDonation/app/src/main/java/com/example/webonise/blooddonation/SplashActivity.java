package com.example.webonise.blooddonation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by webonise on 1/9/15.
 */
public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    final String PREFER = "prefer";
    SharedPreferences pref;
    int Check;
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
                    pref = getApplicationContext().getSharedPreferences(PREFER, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    if(pref.getInt("Check",1)==1)
                    {  editor.putInt("Check",2);
                        editor.commit();
                        Intent register = new Intent(SplashActivity.this,RegistrationActivity.class);
                        register.putExtra("btnText","Register");
                        startActivity(register);
                        finish();

                       /* startActivity(new Intent("com.example.webonise.blooddonation.REGISTRATIONACTIVITY"));*/
                    }
                    else {
                        Intent search = new Intent(SplashActivity.this,SearchActivity.class);
                        search.putExtra("btnText","Search");
                        startActivity(search);
                        finish();


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