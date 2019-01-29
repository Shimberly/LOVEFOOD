package com.webbi.redes.lovefood;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private static final int splash=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        com.facebook.login.widget.LoginButton btnlogin;

        callbackManager = CallbackManager.Factory.create();


        ImageView IrALogin = (ImageView) findViewById(R.id.irALogin);
        IrALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(itemintent);

            }
        });

        printKeyHash();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent itemintent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(itemintent);
                finish();
            }
        },splash);
    }

    private void printKeyHash(){

    }

}
