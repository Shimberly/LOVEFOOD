package com.webbi.redes.lovefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);

        ImageView IrALogin = (ImageView) findViewById(R.id.irALogin);
        IrALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(itemintent);
            }
        });

        printKeyHash();
    }

    private void printKeyHash(){
       /* <activity
        android:name="com.facebook.FacebookActivity"
        android:configChanges= "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
        android:label="@string/app_name" />

        <activity
        android:name="com.facebook.CustomTabActivity"
        android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
        </activity>*/
    }

}
