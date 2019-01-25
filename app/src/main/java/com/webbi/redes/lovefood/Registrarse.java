package com.webbi.redes.lovefood;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinSe);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        EditText etPlannedDate = (EditText) findViewById(R.id.txtDate);
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.txtDate:
                        showDatePickerDialog();
                        break;
                }
            }
        });

        Button IrAInicio = (Button) findViewById(R.id.btnRegistrarse);
        IrAInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(Registrarse.this, Principal.class);
                Registrarse.this.startActivity(itemintent);
            }
        });

       /* Button IrAInicio2 = (Button) findViewById(R.id.btnRegistroFb);
        IrAInicio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(Registrarse.this, Principal.class);
                Registrarse.this.startActivity(itemintent);
            }
        });

        Button IrAInicio3 = (Button) findViewById(R.id.btnRegistroGoogle);
        IrAInicio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(Registrarse.this, Principal.class);
                Registrarse.this.startActivity(itemintent);
            }
        });*/
    }
    private void showDatePickerDialog() {

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                EditText etPlannedDate = (EditText) findViewById(R.id.txtDate);
                etPlannedDate.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
