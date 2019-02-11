package com.webbi.redes.lovefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tutorial1 extends AppCompatActivity{

    Button btn;
    Intent itemintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_1);

    }

    public void accionPantalla(View view){
        itemintent = new Intent(tutorial1.this, tutorial2.class);
        startActivity(itemintent);
    }
}
