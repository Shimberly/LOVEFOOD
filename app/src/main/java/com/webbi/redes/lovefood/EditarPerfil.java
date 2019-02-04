package com.webbi.redes.lovefood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.webbi.redes.lovefood.Login.PREFS_KEY;

public class EditarPerfil extends AppCompatActivity implements AuthenticationListener {
    Button GuardarEditar;
    private static final String TAG = "AsyncTaskActivity";

    public final static String path = "https://lovefoodservices.herokuapp.com/actualizarInformacion";
    public String pathU = null;
    public String pathI = null;
    Integer idusuario;
    java.net.URL url;
    String responseText;
    StringBuffer response;
    String fotoTag;
    String sexo;
    TextView txtNombre;
    TextView txtCorreo;
    TextView txtEdad;
    ImageView fotoPerfil;

    EditText txtUniversidad;
    EditText txtCiudad;
    EditText txtDescripcion;
    RadioGroup radioGroup;
    RadioButton interes;
    EditText txtInstagram;
    EditText txtWhatsapp;
    List<String> list;
    HttpURLConnection conn;
    String query;
    ServicioWeb servicio;
    ServicioWebLlenar servicioLlenar;
    String nombre;
    Spinner spinnerU;
    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private Button btnIG = null;
    private View info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        idusuario = Integer.valueOf(leerValor(this, "idusuario"));
        Log.d("LOGEADO", leerValor(this, "idusuario"));
        pathU = "https://lovefoodservices.herokuapp.com/mostrarUsuario/" + idusuario;
        pathI = "https://lovefoodservices.herokuapp.com/mostrarInformacion/" + idusuario;

        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtEdad = findViewById(R.id.txtEdad);
        fotoPerfil = findViewById(R.id.fotoPerfil);
        //txtUniversidad=(EditText) findViewById(R.id.txtUniversidad);
        txtCiudad = (EditText) findViewById(R.id.txtLugar);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtInstagram = (EditText) findViewById(R.id.txtIg);
        //txtInstagram.setEnabled(false);
        txtWhatsapp = (EditText) findViewById(R.id.txtWa);
        radioGroup = (RadioGroup) findViewById(R.id.radio);

        //txtUniversidad.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtCiudad.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtDescripcion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtWhatsapp.setInputType(InputType.TYPE_CLASS_NUMBER);

        spinnerU = (Spinner) findViewById(R.id.universidades);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.universidades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerU.setAdapter(adapter);
        //

        //btnIG = findViewById(R.id.loginIG);
        info = findViewById(R.id.info);
        appPreferences = new AppPreferences(this);

        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }

        list = new ArrayList<String>();
        servicioLlenar = (ServicioWebLlenar) new ServicioWebLlenar().execute();
        GuardarEditar = (Button) findViewById(R.id.accionGuardarEditar);
        //GuardarEditar.setEnabled(true);
        GuardarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedToInternet()) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    interes = (RadioButton) findViewById(selectedId);
                    Log.i("MainActivity", String.valueOf(selectedId));
                    LinearLayout linearLayout = findViewById(R.id.camposRegistro);
                    int count = linearLayout.getChildCount();
                    boolean isAllFill = true;
                    for (int i = 0; i < count; i++) {
                        try {
                            EditText editText = (EditText) linearLayout.getChildAt(i);
                            if (editText.getText().toString().isEmpty()) {
                                isAllFill = false;
                                break;

                            }
                        } catch (Exception e) {
                        }
                    }
                    int maxLength = 10;
                    txtWhatsapp.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

                    if (isAllFill && interes != null && !txtWhatsapp.getText().toString().isEmpty() && !txtDescripcion.getText().toString().isEmpty()) {
                        // Run AsyncTask
                        if (txtWhatsapp.getText().toString().length()<10){
                            Bundle args = new Bundle();
                            args.putString("titulo", "Advertencia");
                            args.putString("texto", "Número no válido");
                            ProblemaConexion f = new ProblemaConexion();
                            f.setArguments(args);
                            f.show(getSupportFragmentManager(), "ProblemaConexión");
                        }else{
                            servicio = (ServicioWeb) new ServicioWeb().execute();
                        }

                    } else {
                        Log.i("MainActivity", "onCreate -> if -> Hay EditText vacios.");
                        Bundle args = new Bundle();
                        args.putString("titulo", "Advertencia");
                        args.putString("texto", "Completa todos los campos");
                        ProblemaConexion f = new ProblemaConexion();
                        f.setArguments(args);
                        f.show(getSupportFragmentManager(), "ProblemaConexión");
                    }
                } else {
                    Log.d("d", "Error Conexion");
                    Bundle args = new Bundle();
                    args.putString("titulo", "Advertencia");
                    args.putString("texto", "No hay conexión de Internet");
                    ProblemaConexion f = new ProblemaConexion();
                    f.setArguments(args);
                    f.show(getSupportFragmentManager(), "ProblemaConexión");
                }
            }
        });

    }


    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return preferences.getString(keyPref, "");
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
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
            nombre = "";
            //String url = "http://10.0.2.2/api/token";
            HttpURLConnection urlConnection = null;
            Map<String, String> stringMap = new HashMap<>();
            Log.d("foto", fotoTag);
            Log.d("ig", String.valueOf(txtInstagram.getText()));


            Log.i("MainActivity", "onCreate -> else -> Todos los EditText estan llenos.");
            stringMap.put("universidad", String.valueOf(spinnerU.getSelectedItem().toString()));
            stringMap.put("ciudad", String.valueOf(txtCiudad.getText()));
            stringMap.put("descripcion", String.valueOf(txtDescripcion.getText()));
            stringMap.put("instagram", String.valueOf(txtInstagram.getText()));
            stringMap.put("preferencia", String.valueOf(interes.getText()));
            stringMap.put("numero", String.valueOf(txtWhatsapp.getText()));
            stringMap.put("idusuario", String.valueOf(idusuario));
            stringMap.put("foto", String.valueOf(fotoTag));
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
                nombre = "ok";
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return nombre;
        }

        @Override
        protected void onPostExecute(String nombre) {
            //GuardarEditar.setEnabled(true);
            super.onPostExecute(nombre);
            Log.d(TAG, "onPostExecute");
            if (nombre == "ok") {
                Intent itemintent = new Intent(EditarPerfil.this, Principal.class);
                EditarPerfil.this.startActivity(itemintent);
                Log.d(TAG, "ok");
            } else {

                Log.d(TAG, "Registro fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo registrar tus datos");
                ProblemaConexion f = new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
            }

        }

    }

    public static class Utils {
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

    private class ServicioWebLlenar extends AsyncTask<Integer, Integer, List<String>> {


        @Override
        protected List<String> doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected List<String> getWebServiceResponseData() {

            try {
                url = new URL(pathU);
                nombre = "";
                Log.d(TAG, "ServerData: " + pathU);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                responseText = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Problema al conectar con el servidor de LOVEFOOD");
                ProblemaConexion f = new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
                servicio.cancel(true);
            }
            //Call ServerData() method to call webservice and store result in response
            //  response = service.ServerData(path, postDataParams);
            Log.d(TAG, "data:" + responseText);
            JSONObject jsonobjectU = null;
            try {
                JSONArray jsonarray = new JSONArray(responseText);

                jsonobjectU = jsonarray.getJSONObject(0);
                list.add(jsonobjectU.getString("nombre"));
                list.add(jsonobjectU.getString("apellido"));
                list.add(jsonobjectU.getString("correo"));
                list.add(jsonobjectU.getString("sexo"));
                list.add(jsonobjectU.getString("fecha_nacimiento"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
//INFORMACION
            try {
                url = new URL(pathI);
                nombre = "";
                Log.d(TAG, "ServerData: " + pathI);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                responseText = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Problema al conectar con el servidor de LOVEFOOD");
                ProblemaConexion f = new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
                servicio.cancel(true);
            }
            //Call ServerData() method to call webservice and store result in response
            //  response = service.ServerData(path, postDataParams);
            Log.d(TAG, "data:" + responseText);
            JSONObject jsonobjectI = null;
            try {
                JSONArray jsonarray = new JSONArray(responseText);

                jsonobjectI = jsonarray.getJSONObject(0);
                list.add(jsonobjectI.getString("universidad"));
                list.add(jsonobjectI.getString("ciudad"));
                list.add(jsonobjectI.getString("descripcion"));
                list.add(jsonobjectI.getString("instagram"));
                list.add(jsonobjectI.getString("preferencia"));
                list.add(jsonobjectI.getString("numero"));
                list.add(jsonobjectI.getString("foto"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        //@RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(List<String> lista) {
            super.onPostExecute(lista);
            Log.d(TAG, "onPostExecute");
            if (lista != null) {
                Log.d("lista", String.valueOf(lista.size()));

                txtNombre.setText(lista.get(0) + " " + lista.get(1));
                //txtCorreo.setText(""+lista.get(2));

                if (lista.get(5) != "") {
                    //txtUniversidad.setText(""+lista.get(5));
                    txtCiudad.setText("" + lista.get(6));
                    txtDescripcion.setText("" + lista.get(7));
                    txtInstagram.setText("" + lista.get(8));
                    //txtInteres.setText("Interes en "+lista.get(9));
                    if (!lista.get(10).equals("")){
                        txtWhatsapp.setText("" + lista.get(10));
                    }else{
                        txtWhatsapp.setText(getPhoneNumber());
                    }

                    if (lista.get(9).equals("Mujeres")) {
                        radioGroup.check(((RadioButton) radioGroup.getChildAt(0)).getId());
                    } else {
                        if (lista.get(9).equals("Hombres")) {
                            radioGroup.check(((RadioButton) radioGroup.getChildAt(1)).getId());
                        }
                    }

                    List<String> mTestArray;
                    mTestArray = Arrays.asList(getResources().getStringArray(R.array.universidades));
                    for (int k = 0; k < mTestArray.size(); k++) {
                        if (mTestArray.get(k).equals(lista.get(5))) {
                            spinnerU.setSelection(k);
                            break;
                        }
                    }

                }

                Log.d("sexo", lista.get(3));
                if (!lista.get(11).equals("")) {
                    Picasso.get().load(lista.get(11)).into(fotoPerfil);
                    fotoTag = lista.get(11);
                } else {
                    if (lista.get(3).equals("Mujer")) {
                        fotoPerfil.setImageResource(R.drawable.girl);

                    } else {
                        fotoPerfil.setImageResource(R.drawable.boy);
                    }
                    fotoTag = "";
                }
                sexo = lista.get(3);

            } else {
                Log.d(TAG, "Login fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo cargar los datos");
                ProblemaConexion f = new ProblemaConexion();
                f.setArguments(args);
                f.show(getSupportFragmentManager(), "ProblemaConexión");
            }

        }

    }

    public void login() {
        btnIG.setText("Desvincular");
        info.setVisibility(View.VISIBLE);
        //ImageView pic = findViewById(R.id.pic);
        //Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(fotoPerfil);
        Picasso.get().load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(fotoPerfil);
        fotoTag = appPreferences.getString(AppPreferences.PROFILE_PIC);
        Log.d("foto", AppPreferences.PROFILE_PIC);
        //TextView id = findViewById(R.id.id);
        //id.setText(appPreferences.getString(AppPreferences.USER_ID));
        TextView name = findViewById(R.id.txtIg);
        name.setText(appPreferences.getString(AppPreferences.USER_NAME));
    }

    public void logout() {
        btnIG.setText("Vincular");
        if (sexo.equals("Mujer")) {
            fotoPerfil.setImageResource(R.drawable.girl);

        } else {
            fotoPerfil.setImageResource(R.drawable.boy);
        }
        fotoTag = "";
        token = null;
        txtInstagram.setText("");
        //info.setVisibility(View.GONE);
        appPreferences.clear();
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;

        getUserInfoByAccessToken(token);
    }

    public void loginIG(View view) {
        if (token != null) {
            logout();
        } else {
            authenticationDialog = new AuthenticationDialog(this, this);
            authenticationDialog.setCancelable(true);
            authenticationDialog.show();
        }
    }

    private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI().execute();
    }
    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String getPhoneNumber() {
        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, wantPermission) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return phoneMgr.getLine1Number();

    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url=new URL(getResources().getString(R.string.get_user_info_url) + token);
                nombre="";
                Log.d(TAG, "ServerData: " + pathU);
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
                return responseText;
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

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        //сохранение данных пользователя
                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));
                        fotoTag=appPreferences.getString(AppPreferences.PROFILE_PIC);
                        //TODO: сохранить еще данные
                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Correcto!",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
