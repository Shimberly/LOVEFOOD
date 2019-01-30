package com.webbi.redes.lovefood;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static com.webbi.redes.lovefood.Login.PREFS_KEY;

public class Registrarse extends AppCompatActivity {

    private static final String TAG = "AsyncTaskActivity";

    public final static String path = "https://lovefoodservices.herokuapp.com/GuardarUsuario";
    public final static String pathUltimo = "https://lovefoodservices.herokuapp.com/ultimoidusuario";
    String responseText;
    StringBuffer response;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtCorreo;
    EditText txtPass;
    RadioGroup radioGroup;
    RadioButton genero;
    EditText txtEdad;
    String idusuario;
    HttpURLConnection conn;
    String query;
    Button IrAInicio;
    ServicioWeb servicio;
    String nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtNombre=(EditText) findViewById(R.id.txtNombre);
        txtApellido=(EditText) findViewById(R.id.txtApellido);
        txtCorreo=(EditText) findViewById(R.id.txtEmail);
        txtPass=(EditText) findViewById(R.id.txtPass);
        txtEdad=(EditText) findViewById(R.id.txtDate);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

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

        IrAInicio = (Button) findViewById(R.id.btnRegistrarse);
        IrAInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validarEmail(String.valueOf(txtCorreo.getText()))){
                    txtCorreo.setError("Email no válido");
                }else{
                    if(isConnectedToInternet())
                    {
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        genero = (RadioButton) findViewById(selectedId);
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
                        if (isAllFill && genero != null) {
                        // Run AsyncTask
                            servicio = (ServicioWeb) new ServicioWeb().execute();
                        } else {
                            Log.i("MainActivity", "onCreate -> if -> Hay EditText vacios.");
                            Bundle args = new Bundle();
                            args.putString("titulo", "Advertencia");
                            args.putString("texto", "Completa todos los campos");
                            ProblemaConexion f=new ProblemaConexion();
                            f.setArguments(args);
                            f.show(getSupportFragmentManager(), "ProblemaConexión");
                        }
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
    private void showDatePickerDialog() {

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                month=month+1;
                String formattedMonth= String.valueOf(month);
                String formattedDayOfMonth= String.valueOf(day);
                if(month < 10){

                    formattedMonth = "0" + month;
                }
                if(day < 10){

                    formattedDayOfMonth = "0" + day;
                }
                final String selectedDate = formattedDayOfMonth + "/" + formattedMonth + "/" + year;
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

    private class ServicioWeb extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            IrAInicio.setEnabled(false);
        }
        @Override
        protected String doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected String getWebServiceResponseData() {
            nombre=null;
            //String url = "http://10.0.2.2/api/token";
            HttpURLConnection urlConnection = null;
            Map<String, String> stringMap = new HashMap<>();
            Log.i("MainActivity", "onCreate -> else -> Todos los EditText estan llenos.");

            stringMap.put("nombre", String.valueOf(txtNombre.getText()));
            stringMap.put("apellido", String.valueOf(txtApellido.getText()));
            stringMap.put("correo", String.valueOf(txtCorreo.getText()));
            stringMap.put("clave", String.valueOf(txtPass.getText()));
            stringMap.put("sexo", String.valueOf(genero.getText()));
            stringMap.put("fecha_nacimiento", String.valueOf(txtEdad.getText()));
            String requestBody = Utils.buildPostParameters(stringMap);
            try {
                urlConnection = (HttpURLConnection) Utils.makeRequest("POST", path, null, "application/x-www-form-urlencoded", requestBody);
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
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            try {
                URL url=new URL(pathUltimo);
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
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                responseText = response.toString();
            } catch (Exception e) {
            }
            try {
                JSONArray jsonarray = new JSONArray(responseText);

                for (int i=0;i<jsonarray.length();i++){
                    JSONObject jsonobject = jsonarray.getJSONObject(0);
                    idusuario = jsonobject.getString("id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URL url=new URL("https://lovefoodservices.herokuapp.com/generarinformacion/"+Integer.valueOf(idusuario));
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
                }
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                responseText = response.toString();
                nombre=idusuario;
            } catch (Exception e) {
            }
            return nombre;
        }

        @Override
        protected void onPostExecute(String nombre) {

            super.onPostExecute(nombre);
            Log.d(TAG, "onPostExecute");
            if (nombre!=null){
                guardarValor(Registrarse.this,"idusuario", String.valueOf(nombre));
                Intent itemintent = new Intent(Registrarse.this, Principal.class);
                itemintent.putExtra("primero","true");
                Registrarse.this.startActivity(itemintent);

            }else{
                IrAInicio.setEnabled(true);
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

    public static void guardarValor(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();

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
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
