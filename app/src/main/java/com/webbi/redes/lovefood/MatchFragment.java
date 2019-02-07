package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import android.os.Vibrator;
import android.widget.TextView;

import javax.net.ssl.HttpsURLConnection;

public class MatchFragment extends Fragment {
    public final static String PREFS_KEY = "mispreferencias";
    java.net.URL url;
    String responseText;
    StringBuffer response;
    Match servicioMatch;
    List<JSONObject> listaMatch;
    Integer idusuario;
    String pathUserR;
    private ProgressBar progressBar;

    TextView txtNombre;
    TextView txtEdad;
    TextView txtCorreo;
    TextView txtUniversidad;
    TextView txtCiudad;
    TextView txtDescripcion;
    Button btnWa;
    Button btnIg;

    View view;
    public MatchFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_match, container, false);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(10);



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


        if(isConnectedToInternet())
        {
            // Run AsyncTask
            idusuario= Integer.valueOf(leerValor(getContext(),"idusuario"));
            Log.d("LOGEADO", leerValor(getContext(),"idusuario"));
            pathUserR = "https://lovefoodservices.herokuapp.com/match/"+idusuario;
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            listaMatch = new ArrayList<JSONObject>();
            servicioMatch = (Match) new Match().execute();
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
        return  view;
    }
    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
        //return "1";
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

    private class Match extends AsyncTask<Integer, Integer, List<JSONObject>> {

        @Override
        protected List<JSONObject> doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected List<JSONObject> getWebServiceResponseData() {

            try {
                url=new URL(pathUserR);
                Log.d("PATH RES", "ServerData: " + pathUserR);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                Log.d("Response", "Response code: " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
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
                f.show(getFragmentManager(), "ProblemaConexión");
                servicioMatch.cancel(true);
            }
            Log.d("ResponseText", "data:" + responseText);
            try {
                JSONArray jsonarray = new JSONArray(responseText);
                Log.d("Recibiendo","ENTRO");
                listaMatch.add(jsonarray.getJSONObject(0));
                listaMatch.add(jsonarray.getJSONObject(1));
                listaMatch.add(jsonarray.getJSONObject(2));
                listaMatch.add(jsonarray.getJSONObject(3));
                listaMatch.add(jsonarray.getJSONObject(4));

                /*respuestas.add(jsonobject.getString("sexo"));
                respuestas.add(jsonobject.getString("preferencia"));
                respuestas.add(jsonobject.getString("rspverde"));
                respuestas.add(jsonobject.getString("rspdesayunosalado"));
                respuestas.add(jsonobject.getString("rspdesayunodulce"));
                respuestas.add(jsonobject.getString("rspalmuerzo"));
                respuestas.add(jsonobject.getString("rspmarisco"));
                respuestas.add(jsonobject.getString("rspsopa"));
                respuestas.add(jsonobject.getString("rspcena"));
                respuestas.add(jsonobject.getString("rspcomidatipicacosta"));
                respuestas.add(jsonobject.getString("rspcomidatipicasierra"));
                respuestas.add(jsonobject.getString("rspcomidatipicaoriente"));
                respuestas.add(jsonobject.getString("rspproteina"));
                respuestas.add(jsonobject.getString("rsppostres"));
                respuestas.add(jsonobject.getString("rspsaboresdulces"));
                respuestas.add(jsonobject.getString("rspbebida"));
                respuestas.add(jsonobject.getString("rspcomidaextranjera"));
                respuestas.add(jsonobject.getString("rspcomidarapida"));*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listaMatch;
        }

        @Override
        protected void onPostExecute(List<JSONObject> listaMatch) {
            super.onPostExecute(listaMatch);
            Log.d("PostExecute", "onPostExecute");
            if (listaMatch!=null){
                Log.d("lista", String.valueOf(listaMatch.size()));
                progressBar.setVisibility(View.GONE);

                for (int i=0;i<listaMatch.size();i++){
                    txtNombre= (TextView) view.findViewWithTag("txtNombre"+(i+1));
                    txtEdad= (TextView) view.findViewWithTag("txtEdad"+(i+1));
                    txtCorreo= (TextView) view.findViewWithTag("txtCorreo"+(i+1));
                    txtUniversidad= (TextView) view.findViewWithTag("txtUniversidad"+(i+1));
                    txtCiudad= (TextView) view.findViewWithTag("txtCiudad"+(i+1));
                    txtDescripcion= (TextView) view.findViewWithTag("txtDescripcion"+(i+1));
                    btnIg=(Button) view.findViewWithTag("btnIg"+(i+1));
                    btnWa=(Button) view.findViewWithTag("btnWa"+(i+1));
                    try {
                        txtNombre.setText(listaMatch.get(i).getString("nombre"));
                        DateTimeFormatter fmt = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate fechaNac = LocalDate.parse(listaMatch.get(i).getString("fecha_nacimiento"), fmt);
                            LocalDate ahora = LocalDate.now();

                            Period periodo = Period.between(fechaNac, ahora);
                            System.out.printf("Tu edad es: %s años, %s meses y %s días",
                                    periodo.getYears(), periodo.getMonths(), periodo.getDays());
                            Log.d("edad", String.valueOf(periodo.getYears()));

                            txtEdad.setText(String.valueOf(periodo.getYears()));
                        }else{
                            txtEdad.setText(String.valueOf(listaMatch.get(i).getString("fecha_nacimiento")));
                        }
                        txtCorreo.setText(listaMatch.get(i).getString("correo"));
                        txtUniversidad.setText(listaMatch.get(i).getString("universidad"));
                        txtCiudad.setText(listaMatch.get(i).getString("ciudad"));
                        txtDescripcion.setText(listaMatch.get(i).getString("descripcion"));
                        btnIg.setHint(listaMatch.get(i).getString("instagram"));
                        btnWa.setHint(listaMatch.get(i).getString("numero"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }






                Bundle args = new Bundle();
                args.putString("titulo", "¡Matchs localizados!");
                args.putString("texto", "No tengas miedo de encontrar el amor. ¡Escríbeles!");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(600);
                }
            }else{
                Log.d("Login fail", "Login fail:"+listaMatch);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo cargar los datos");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");
            }
        }
    }
}