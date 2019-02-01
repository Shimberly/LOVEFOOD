package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static com.webbi.redes.lovefood.Login.PREFS_KEY;

public class Principal extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigationView);
        nav.setOnNavigationItemSelectedListener(this);
        String flag=getIntent().getStringExtra("primero");
        //Log.d("FLAG", flag);
        if (flag!=null){
            Log.d("FLAG entro", flag);
            Bundle args = new Bundle();
            args.putString("titulo", "BIENVENIDO A LOVEFOOD");
            args.putString("texto", "¡No te olvides registrar tus datos! Muy pronto estarán todas las funcionalidades.");
            ProblemaConexion f=new ProblemaConexion();
            f.setArguments(args);
            f.show(getSupportFragmentManager(), "ProblemaConexión");
        }
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
                SharedPreferences settings = this.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void accionEditar(View v){
        if(isConnectedToInternet())
        {
            Intent intent2 = new Intent(this, EditarPerfil.class);
            startActivity(intent2);
        }
        else
        {
            Log.d("Prr", "Error Conexion");
            Bundle args = new Bundle();
            args.putString("titulo", "Advertencia");
            args.putString("texto", "No hay conexión de Internet");
            ProblemaConexion f=new ProblemaConexion();
            f.setArguments(args);
            f.show(getSupportFragmentManager(), "ProblemaConexión");
        }


    }
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
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

        try {
            String text = "This is a test";// Replace with your message.

            String toNumber = "593992937424"; // Replace with mobile phone number without +Sign or leading zeros.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void acciongeo(View v){

        Intent itemintent = new Intent(Principal.this, LocalizarAmigo.class);
        startActivity(itemintent);
    }

}
