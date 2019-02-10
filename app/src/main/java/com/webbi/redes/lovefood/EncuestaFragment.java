package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class EncuestaFragment extends Fragment {
    private static final String TAG = "AsyncTaskActivity";
    public final static String PREFS_KEY = "mispreferencias";
    public final static String path = "https://lovefoodservices.herokuapp.com/GuardarRespuesta";
    public final static String pathA = "https://lovefoodservices.herokuapp.com/actualizarRespuesta";

    public EncuestaFragment() {}
    View view;
    String responseText;
    StringBuffer response;
    RadioButton interes;
    GuardarPreguntas servicioweb;
    ActualizarPreguntas servicioActualizar;
    LeerRespuestas servicioLeer;
    List<RadioGroup> listaPreguntas;
    List<RadioButton> listaRespuestas;
    List<String> llenarRespuestas;
    private ProgressBar progressBar;
    Button guardarP;
    String nombre;
    Integer idusuario;
    Boolean error;
    //private ProgressBar progressBar;

    Boolean existe=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encuesta, container, false);
        listaPreguntas = new ArrayList<RadioGroup>();
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(10);
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.preguntauno));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta2));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta3));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta4));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta5));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta6));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta7));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta8));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta9));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta10));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta11));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta12));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta13));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta14));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta15));
        listaPreguntas.add((RadioGroup) view.findViewById(R.id.pregunta16));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });
        idusuario= Integer.valueOf(leerValor(getContext(),"idusuario"));
        Log.d("LOGEADO", leerValor(getContext(),"idusuario"));

        if(isConnectedToInternet())
        {
            servicioLeer = (LeerRespuestas) new LeerRespuestas().execute();
        }
        else
        {
            Log.d("Prr", "Error Conexion");
            Bundle args = new Bundle();
            args.putString("titulo", "Advertencia");
            args.putString("texto", "No hay conexión de Internet");
            ProblemaConexion f=new ProblemaConexion();
            f.setArguments(args);
            f.show(getFragmentManager(), "ProblemaConexión");
        }

        guardarP = (Button) view.findViewById(R.id.btnGuardarP);
        //GuardarEditar.setEnabled(true);
        guardarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnectedToInternet())
                {
                    listaRespuestas = new ArrayList<RadioButton>();
                    //int selectedId = radioGroup.getCheckedRadioButtonId();
                    //interes = (RadioButton) findViewById(selectedId);
                    int count = listaPreguntas.size();
                    Log.i("MainActivity", String.valueOf(count));
                    boolean isAllFill = true;
                    for (int i = 0; i < count; i++) {
                        Log.i("MainActivity", String.valueOf(i));
                        try {
                            int selectedId = listaPreguntas.get(i).getCheckedRadioButtonId();
                            interes = (RadioButton) getActivity().findViewById(selectedId);
                            Log.i("MainActivity", String.valueOf(interes));
                            Log.i("MainActivity", String.valueOf(selectedId));
                            if (interes == null) {
                                isAllFill = false;
                                //break;

                            }else{
                                listaRespuestas.add(interes);
                            }
                        }catch (Exception e){
                        }
                    }

                    if (isAllFill) {
                        if (existe){
                            servicioActualizar = (ActualizarPreguntas) new ActualizarPreguntas().execute();
                        }else{
                            progressBar.setVisibility(View.VISIBLE);

                            progressBar.setProgress(0);
                            //progressBar.requestFocusInWindow();
                            guardarP.requestFocus();
                            //this.getFocusable(progressBar);
                            guardarP.setFocusable(true);
                            guardarP.setFocusableInTouchMode(true);
                            guardarP.requestFocus();
                            servicioweb = (GuardarPreguntas) new GuardarPreguntas().execute();
                        }

                    } else {
                        Log.i("MainActivity", "onCreate -> if -> Hay Radios vacios.");
                        Bundle args = new Bundle();
                        args.putString("titulo", "Advertencia");
                        args.putString("texto", "Completa todos los campos");
                        ProblemaConexion f=new ProblemaConexion();
                        f.setArguments(args);
                        f.show(getFragmentManager(), "ProblemaConexión");
                    }
                }
                else
                {
                    Log.d("d", "Error Conexion");
                    Bundle args = new Bundle();
                    args.putString("titulo", "Advertencia");
                    args.putString("texto", "No hay conexión de Internet");
                    ProblemaConexion f=new ProblemaConexion();
                    f.setArguments(args);
                    f.show(getFragmentManager(), "ProblemaConexión");
                }
            }
        });
        return view;
    }
    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
        //return "1";
    }

    private class LeerRespuestas extends AsyncTask<Integer, Integer, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llenarRespuestas = new ArrayList<String>();
            error=false;
        }

        @Override
        protected List<String> doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected List<String> getWebServiceResponseData() {

            try {
                URL url=new URL("https://lovefoodservices.herokuapp.com/listarRespuestas");
                Log.d(TAG, "ServerData: " + "https://lovefoodservices.herokuapp.com/listarRespuestas");
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
                JSONArray jsonarray = new JSONArray(responseText);

                for (int i=0;i<jsonarray.length();i++){
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String usuario = jsonobject.getString("idusuario");
                    if(idusuario.equals(Integer.valueOf(usuario))){
                        Log.d(TAG,"ENTRO");
                        llenarRespuestas.add(jsonobject.getString("rspverde"));
                        llenarRespuestas.add(jsonobject.getString("rspdesayunosalado"));
                        llenarRespuestas.add(jsonobject.getString("rspdesayunodulce"));
                        llenarRespuestas.add(jsonobject.getString("rspalmuerzo"));
                        llenarRespuestas.add(jsonobject.getString("rspmarisco"));
                        llenarRespuestas.add(jsonobject.getString("rspsopa"));
                        llenarRespuestas.add(jsonobject.getString("rspcena"));
                        llenarRespuestas.add(jsonobject.getString("rspcomidatipicacosta"));
                        llenarRespuestas.add(jsonobject.getString("rspcomidatipicasierra"));
                        llenarRespuestas.add(jsonobject.getString("rspcomidatipicaoriente"));
                        llenarRespuestas.add(jsonobject.getString("rspproteina"));
                        llenarRespuestas.add(jsonobject.getString("rsppostres"));
                        llenarRespuestas.add(jsonobject.getString("rspsaboresdulces"));
                        llenarRespuestas.add(jsonobject.getString("rspbebida"));
                        llenarRespuestas.add(jsonobject.getString("rspcomidaextranjera"));
                        llenarRespuestas.add(jsonobject.getString("rspcomidarapida"));

                    }else{
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                error=true;
            }
            //Call ServerData() method to call webservice and store result in response
            //  response = service.ServerData(path, postDataParams);
            Log.d(TAG, "data:" + responseText);
            return llenarRespuestas;
        }

        @Override
        protected void onPostExecute(List<String> lista) {
            super.onPostExecute(lista);
            Log.d(TAG, "onPostExecute");
            Log.d("lista", String.valueOf(lista));
            if(error){
                Toast.makeText(getActivity(), "¡Problemas con el servidor!",
                        Toast.LENGTH_LONG).show();
                servicioLeer.cancel(true);
            }else{
                if (lista.size()>1){
                    Log.d(TAG, "existe");
                    existe=true;
                    Log.d("listaPreguntas size", String.valueOf(listaPreguntas.size()));

                    //Log.d("listaPreguntas size", String.valueOf(porfa.getText()));
                    for (int i=0;i<listaPreguntas.size();i++) {
                        //Resources res = getResources();
                        //int id = res.getIdentifier("Tor", "id", getContext().getPackageName());
                        RadioButton porfa= view.findViewWithTag(lista.get(i));
                        if(porfa!=null){
                            listaPreguntas.get(i).check(porfa.getId());
                        }
                        //view.findViewsWithText(porfa,"sd",0);
                        //int count = listaPreguntas.get(i).getChildCount();
                        //ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
                    /*for (int j=0;j<count;j++) {
                        View o = listaPreguntas.get(i).getChildAt(j);
                        if (o instanceof RadioButton) {
                            Log.d("button", String.valueOf(o));
                            if(((RadioButton) o).getText().equals(llenarRespuestas.get(i))){
                                listaPreguntas.get(i).check(o.getId());
                            }
                        }
                    }*/
                    }

                    //radioGroup.check(((RadioButton)radioGroup.getChildAt(0)).getId());
                }else{
                    Log.d(TAG, "no existe");
                    existe=false;
                }
            }


        }

    }
    private class GuardarPreguntas extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            guardarP.setEnabled(false);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

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


            Log.i("MainActivity", "onCreate -> else -> Todos los EditText estan llenos.");
            stringMap.put("idusuario", String.valueOf(idusuario));
            stringMap.put("rspverde", String.valueOf(listaRespuestas.get(0).getTag()));
            stringMap.put("rspdesayunosalado", String.valueOf(listaRespuestas.get(1).getTag()));
            stringMap.put("rspdesayunodulce", String.valueOf(listaRespuestas.get(2).getTag()));
            stringMap.put("rspalmuerzo", String.valueOf(listaRespuestas.get(3).getTag()));
            stringMap.put("rspmarisco", String.valueOf(listaRespuestas.get(4).getTag()));
            stringMap.put("rspsopa", String.valueOf(listaRespuestas.get(5).getTag()));
            stringMap.put("rspcena", String.valueOf(listaRespuestas.get(6).getTag()));
            stringMap.put("rspcomidatipicacosta", String.valueOf(listaRespuestas.get(7).getTag()));
            stringMap.put("rspcomidatipicasierra", String.valueOf(listaRespuestas.get(8).getTag()));
            stringMap.put("rspcomidatipicaoriente", String.valueOf(listaRespuestas.get(9).getTag()));
            stringMap.put("rspproteina", String.valueOf(listaRespuestas.get(10).getTag()));
            stringMap.put("rsppostres", String.valueOf(listaRespuestas.get(11).getTag()));
            stringMap.put("rspsaboresdulces", String.valueOf(listaRespuestas.get(12).getTag()));
            stringMap.put("rspbebida", String.valueOf(listaRespuestas.get(13).getTag()));
            stringMap.put("rspcomidaextranjera", String.valueOf(listaRespuestas.get(14).getTag()));
            stringMap.put("rspcomidarapida", String.valueOf(listaRespuestas.get(15).getTag()));
            String requestBody = Registrarse.Utils.buildPostParameters(stringMap);
            try {
                urlConnection = (HttpURLConnection) Registrarse.Utils.makeRequest("POST", path, null, "application/x-www-form-urlencoded", requestBody);
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
            progressBar.setVisibility(View.GONE);
            if (nombre=="ok"){
                Toast.makeText(getActivity(), "¡Se guardaron correctamente tus respuestas!",
                        Toast.LENGTH_LONG).show();
                /*Fragment fragment= new MatchFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainFrame, fragment);
                fragmentTransaction.commit();*/
            }else{
                guardarP.setEnabled(true);
                Log.d(TAG, "Registro fail:" + nombre);
                Toast.makeText(getActivity(), "¡No se pudieron guardar tus respuestas!",
                        Toast.LENGTH_LONG).show();
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

    private class ActualizarPreguntas extends AsyncTask<Integer, Integer, String> {


        @Override
        protected void onPreExecute() {
            guardarP.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Integer... params) {
            progressBar.setProgress(5);

            return getWebServiceResponseData();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
        protected String getWebServiceResponseData() {
            nombre="";
            //String url = "http://10.0.2.2/api/token";
            HttpURLConnection urlConnection = null;
            Map<String, String> stringMap = new HashMap<>();


            Log.i("MainActivity", "onCreate -> else -> Todos los EditText estan llenos.");
            stringMap.put("idusuario", String.valueOf(idusuario));
            stringMap.put("rspverde", String.valueOf(listaRespuestas.get(0).getTag()));
            stringMap.put("rspdesayunosalado", String.valueOf(listaRespuestas.get(1).getTag()));
            stringMap.put("rspdesayunodulce", String.valueOf(listaRespuestas.get(2).getTag()));
            stringMap.put("rspalmuerzo", String.valueOf(listaRespuestas.get(3).getTag()));
            stringMap.put("rspmarisco", String.valueOf(listaRespuestas.get(4).getTag()));
            stringMap.put("rspsopa", String.valueOf(listaRespuestas.get(5).getTag()));
            stringMap.put("rspcena", String.valueOf(listaRespuestas.get(6).getTag()));
            stringMap.put("rspcomidatipicacosta", String.valueOf(listaRespuestas.get(7).getTag()));
            stringMap.put("rspcomidatipicasierra", String.valueOf(listaRespuestas.get(8).getTag()));
            stringMap.put("rspcomidatipicaoriente", String.valueOf(listaRespuestas.get(9).getTag()));
            stringMap.put("rspproteina", String.valueOf(listaRespuestas.get(10).getTag()));
            stringMap.put("rsppostres", String.valueOf(listaRespuestas.get(11).getTag()));
            stringMap.put("rspsaboresdulces", String.valueOf(listaRespuestas.get(12).getTag()));
            stringMap.put("rspbebida", String.valueOf(listaRespuestas.get(13).getTag()));
            stringMap.put("rspcomidaextranjera", String.valueOf(listaRespuestas.get(14).getTag()));
            stringMap.put("rspcomidarapida", String.valueOf(listaRespuestas.get(15).getTag()));
            String requestBody = Registrarse.Utils.buildPostParameters(stringMap);
            try {
                urlConnection = (HttpURLConnection) Registrarse.Utils.makeRequest("PUT", pathA, null, "application/x-www-form-urlencoded", requestBody);
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
                progressBar.setProgress(100);
                SystemClock.sleep(1000);
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

            super.onPostExecute(nombre);
            progressBar.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onPostExecute");
            if (nombre=="ok"){
                /*Bundle args = new Bundle();
                args.putString("titulo", "LOVEFOOD");
                args.putString("texto", "¡Se actualizaron correctamente tus respuestas!");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");*/
                Toast.makeText(getActivity(), "¡Se actualizaron correctamente tus respuestas!",
                        Toast.LENGTH_LONG).show();
                /*Fragment fragment= new MatchFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainFrame, fragment);
                fragmentTransaction.commit();*/

            }else{
                guardarP.setEnabled(true);
                Log.d(TAG, "Registro fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo actualizar tus respuestas");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");
                progressBar.setProgress(0);
            }


        }

    }

}