package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditarPerfil extends AppCompatActivity {
    Button GuardarEditar;
    private static final String TAG = "AsyncTaskActivity";

    public final static String path = "https://lovefoodservices.herokuapp.com/actualizarInformacion";

    EditText txtUniversidad;
    EditText txtCiudad;
    EditText txtDescripcion;
    RadioGroup radioGroup;
    RadioButton interes;
    EditText txtInstagram;
    EditText txtWhatsapp;

    HttpURLConnection conn;
    String query;
    ServicioWeb servicio;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtUniversidad=(EditText) findViewById(R.id.txtUniversidad);
        txtCiudad=(EditText) findViewById(R.id.txtLugar);
        txtDescripcion=(EditText) findViewById(R.id.txtDescripcion);
        txtInstagram=(EditText) findViewById(R.id.txtIg);
        txtWhatsapp=(EditText) findViewById(R.id.txtWa);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        GuardarEditar = (Button) findViewById(R.id.accionGuardarEditar);
        //GuardarEditar.setEnabled(true);
        GuardarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(isConnectedToInternet())
                    {
                        // Run AsyncTask
                        servicio = (ServicioWeb) new ServicioWeb().execute();
                    }
                    else
                    {
                        Log.d("d", "Error Conexion");
                        Bundle args = new Bundle();
                        args.putString("titulo", "Advertencia");
                        args.putString("texto", "No hay conexión de Internet");
                        ProblemaConexion f=new ProblemaConexion();
                        f.setArguments(args);
                        f.show(getSupportFragmentManager(), "ProblemaConexión");
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class ServicioWeb extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            //GuardarEditar.setEnabled(false);
        }
        @Override
        protected String doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected String getWebServiceResponseData() {
            nombre="";
            //String url = "http://10.0.2.2/api/token";
            HttpURLConnection urlConnection = null;
            Map<String, String> stringMap = new HashMap<>();
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            interes = (RadioButton) findViewById(selectedId);
            // Obtienes el layout que contiene los EditText
            LinearLayout linearLayout = findViewById(R.id.camposRegistro);

            // Obtiene el numero de EditText que contiene el layout
            int count = linearLayout.getChildCount();

            // Recorres todos los editText y si hay alguno vacio cambias el valor de la
            // variable isAllFill a false, lo que indica que aun hay editText vacios.
            boolean isAllFill = true;
            for (int i = 0; i < count; i++) {

                // En cada iteración obtienes uno de los editText que se encuentran el
                // layout.
                try {
                    EditText editText = (EditText) linearLayout.getChildAt(i);
                    // Compruebas su el editText esta vacio.
                    if (editText.getText().toString().isEmpty()) {
                        isAllFill = false;
                        break;
                    }
                }catch (Exception e){

                }



            }

            if (isAllFill && interes != null) {

                Log.i("MainActivity", "onCreate -> else -> Todos los EditText estan llenos.");
                Integer idusuario=1;
                stringMap.put("universidad", String.valueOf(txtUniversidad.getText()));
                stringMap.put("ciudad", String.valueOf(txtCiudad.getText()));
                stringMap.put("descripcion", String.valueOf(txtDescripcion.getText()));
                stringMap.put("instagram", String.valueOf(txtInstagram.getText()));
                stringMap.put("preferencia", String.valueOf(interes.getText()));
                stringMap.put("numero", String.valueOf(txtWhatsapp.getText()));
                stringMap.put("idusuario", String.valueOf(idusuario));
                String requestBody = Registrarse.Utils.buildPostParameters(stringMap);
                try {
                    urlConnection = (HttpURLConnection) Registrarse.Utils.makeRequest("PUT", path, null, "application/x-www-form-urlencoded", requestBody);
                    InputStream inputStream;
                    // get stream
                    if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        inputStream = urlConnection.getInputStream();
                    } else {
                        inputStream = urlConnection.getErrorStream();
                    }
                    // parse stream
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp, response = "";
                    while ((temp = bufferedReader.readLine()) != null) {
                        response += temp;
                    }
                    nombre="ok";
                    return nombre;
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.toString();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } else {
                Log.i("MainActivity", "onCreate -> if -> Hay EditText vacios.");
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Completa todos los campos");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
                servicio.cancel(true);
            }
            return nombre;
        }

        @Override
        protected void onPostExecute(String nombre) {
            //GuardarEditar.setEnabled(true);
            super.onPostExecute(nombre);
            Log.d(TAG, "onPostExecute");
            if (nombre=="ok"){
                Intent itemintent = new Intent(EditarPerfil.this, Principal.class);
                EditarPerfil.this.startActivity(itemintent);
                Log.d(TAG, "ok");
            }else{

                Log.d(TAG, "Registro fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo registrar tus datos");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
            }

        }

    }

    public static class Utils{
        public static String buildPostParameters(Object content) {
            String output = null;
            if ((content instanceof String) ||
                    (content instanceof JSONObject) ||
                    (content instanceof JSONArray)) {
                output = content.toString();
            } else if (content instanceof Map) {
                Uri.Builder builder = new Uri.Builder();
                HashMap hashMap = (HashMap) content;
                if (hashMap != null) {
                    Iterator entries = hashMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                        entries.remove(); // avoids a ConcurrentModificationException
                    }
                    output = builder.build().getEncodedQuery();
                }
            }

            return output;
        }

        public static URLConnection makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody) throws IOException {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(apiAddress);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(!method.equals("GET"));
                urlConnection.setRequestMethod(method);

                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                urlConnection.setRequestProperty("Content-Type", mimeType);
                OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                writer.write(requestBody);
                writer.flush();
                writer.close();
                outputStream.close();

                urlConnection.connect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return urlConnection;
        }
    }
}
