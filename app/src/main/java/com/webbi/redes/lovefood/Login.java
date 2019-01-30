package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity {
    private static final String TAG = "AsyncTaskActivity";
    public final static String PREFS_KEY = "mispreferencias";

    public final static String path = "https://lovefoodservices.herokuapp.com/listarUsuarios";
    java.net.URL url;
    String responseText;
    StringBuffer response;
    TextView txtCorreo;
    TextView txtPass;
    ServicioWeb servicio;
    String nombre;
    Button Logear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txtCorreo=(TextView) findViewById(R.id.txtCorreo);
        txtPass=(TextView) findViewById(R.id.txtPass);

        TextView IrARegistroBtn = (TextView) findViewById(R.id.btnIrRegistro);
        IrARegistroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemintent = new Intent(Login.this, Registrarse.class);
                Login.this.startActivity(itemintent);
            }
        });

        TextView IrAOlvidarPass = (TextView) findViewById(R.id.btnOlvidarPass);
        IrAOlvidarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogoRecuperacion().show(getSupportFragmentManager(), "SimpleDialog");
            }
        });

        Logear = (Button) findViewById(R.id.btnLogin);
        Logear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validarEmail(String.valueOf(txtCorreo.getText()))){
                    txtCorreo.setError("Email no válido");
                }else{
                    if(isConnectedToInternet())
                    {
                        // Run AsyncTask
                        servicio = (ServicioWeb) new ServicioWeb().execute();
                    }
                    else
                    {
                        Log.d(TAG, "Error Conexion");
                        Bundle args = new Bundle();
                        args.putString("titulo", "Advertencia");
                        args.putString("texto", "No hay conexión de Internet");
                        ProblemaConexion f=new ProblemaConexion();
                        f.setArguments(args);
                        f.show(getSupportFragmentManager(), "ProblemaConexión");
                    }
                }
            }
        });
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

    private class ServicioWeb extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            Logear.setEnabled(false);
        }
        @Override
        protected String doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected String getWebServiceResponseData() {

            try {
                url=new URL(path);
                nombre=null;
                Log.d(TAG, "ServerData: " + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                Log.d(TAG, "Response code: " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Reading response from input Stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String output;
                    response = new StringBuffer();

                    while ((output = in.readLine()) != null) {
                        response.append(output);
                    }
                    in.close();
                }}
            catch(Exception e){
                e.printStackTrace();
            }

            try {
                responseText = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Problema al conectar con el servidor de LOVEFOOD");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
                servicio.cancel(true);
            }
            //Call ServerData() method to call webservice and store result in response
            //  response = service.ServerData(path, postDataParams);
            Log.d(TAG, "data:" + responseText);
            try {
                JSONArray jsonarray = new JSONArray(responseText);

                for (int i=0;i<jsonarray.length();i++){
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String mail = jsonobject.getString("correo");
                    String pass=jsonobject.getString("clave");
                    if(String.valueOf(txtCorreo.getText()).equals(String.valueOf(mail))){
                        Log.d(TAG,"ENTRO");
                        if (String.valueOf(txtPass.getText()).equals(String.valueOf(pass))){
                            nombre=jsonobject.getString("id");;
                            //Log.d(TAG, "OK:" + "ok");
                        }else{
                            //.d(TAG, "Error Pass 1:" + pass+".");
                            //Log.d(TAG, "Error Pass 2:" + txtPass.getText()+".");
                        }
                    }else{
                        //Log.d(TAG, "Error Correo 1:" + mail+".");
                        //Log.d(TAG, "Error Correo 2:" + txtCorreo.getText()+".");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return nombre;
        }

        @Override
        protected void onPostExecute(String nombre) {
            super.onPostExecute(nombre);
            Logear.setEnabled(true);
            Log.d(TAG, "onPostExecute");
            if (nombre!=null){
                Intent itemintent = new Intent(Login.this, Principal.class);
                Login.this.startActivity(itemintent);
                guardarValor(Login.this,"idusuario", String.valueOf(nombre));


            }else{
                Log.d(TAG, "Login fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Usuario o contraseña incorrecta");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
            }

        }

    }
    public static void guardarValor(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }

    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
