package com.webbi.redes.lovefood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.RadioButton;

public class Principal extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigationView);
        nav.setOnNavigationItemSelectedListener(this);

        setInitialFragment();
    }
    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.match:
                fragment= new MatchFragment();
                break;
            case R.id.encuesta:
                fragment= new EncuestaFragment();
                break;
            case R.id.perfil:
                fragment= new PerfilFragment();
                break;

        };
        replaceFragment(fragment);
        return true;
    }

    private void setInitialFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainFrame, new PerfilFragment());
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menusuperior, menu);

        return true;
    }

            // You can also use something like:
            // menu.findItem(R.id.example_foobar).setEnabled(false);


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){

            case R.id.salir:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void accionEditar(View v){
        Intent intent2 = new Intent(this, EditarPerfil.class);
        startActivity(intent2);

    }
    public void accionfb(View v){

        String urlPage = "https://www.facebook.com/lobesna?lang=es";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));

    }

    public void acciontw(View v){

        String urlPage = "https://twitter.com/YANDERSANTANA96?lang=es";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
    }

    public void accionig(View v){

        String urlPage = "https://www.instagram.com/webbi_ec/?hl=es-la";

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
    }
    public void accionmsj(View v){

    }
    public void acciongeo(View v){

        Intent itemintent = new Intent(Principal.this, LocalizarAmigo.class);
        startActivity(itemintent);
    }
    public void desayunoSaladoClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_ninjas:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

}
