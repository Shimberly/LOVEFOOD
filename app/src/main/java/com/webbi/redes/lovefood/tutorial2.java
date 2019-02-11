package com.webbi.redes.lovefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tutorial2 extends AppCompatActivity{
    Button btn;
    Intent itemintent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_2);

    }

    public void accionPantalla(View v) {
        itemintent2 = new Intent(tutorial2.this, tutorial3.class);
        startActivity(itemintent2);
    }
}
