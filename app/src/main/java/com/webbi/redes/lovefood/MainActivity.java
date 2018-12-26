package com.webbi.redes.lovefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button IrARegistroBtn = (Button) findViewById(R.id.btnIrRegistro);
        IrARegistroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(MainActivity.this, Registrarse.class);
                MainActivity.this.startActivity(itemintent);
            }
        });

        Button IrAHome = (Button) findViewById(R.id.btnLogin);
        IrAHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(MainActivity.this, Principal.class);
                MainActivity.this.startActivity(itemintent);
            }
        });

        TextView IrAOlvidarPass = (TextView) findViewById(R.id.btnOlvidarPass);
        IrAOlvidarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogoRecuperacion().show(getSupportFragmentManager(), "SimpleDialog");
            }
        });

    }
}
