package com.webbi.redes.lovefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tutorial1 extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    Intent itemintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_1);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        itemintent = new Intent(tutorial1.this, tutorial3.class);
        startActivity(itemintent);
    }
}
