package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MatchFragment extends Fragment {
    public final static String PREFS_KEY = "mispreferencias";
    java.net.URL url;
    String responseText;
    StringBuffer response;
    Match servicioMatch;
    List<JSONObject> listaMatch;
    List<String> respuestas;
    List<String> respuestasMatch;
    Integer idusuario;
    public String pathUserR = null;
    public final static String pathRes = "https://lovefoodservices.herokuapp.com/listarRespuestas";

    public MatchFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_match, container, false);

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

        /*
        if(isConnectedToInternet())
        {
            // Run AsyncTask
            idusuario= Integer.valueOf(leerValor(getContext(),"idusuario"));
            Log.d("LOGEADO", leerValor(getContext(),"idusuario"));
            pathUserR = "https://lovefoodservices.herokuapp.com/mostrarTodo/"+idusuario;

            listaMatch = new ArrayList<JSONObject>();
            respuestas = new ArrayList<String>();
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
        }*/
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

            //RESPUESTAS LOGEADO
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
            JSONObject jsonobject=null;
            try {
                JSONArray jsonarray = new JSONArray(responseText);
                jsonobject = jsonarray.getJSONObject(0);
                Log.d("Recibiendo","ENTRO");
                respuestas.add(jsonobject.getString("sexo"));
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
                respuestas.add(jsonobject.getString("rspcomidarapida"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //MATCHH
            try {
                url=new URL(pathRes);
                Log.d("PATH RES", "ServerData: " + pathRes);
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
            JSONObject jsonobjectU=null;
            try {
                JSONArray jsonarray = new JSONArray(responseText);
                for (int i = 0; i < jsonarray.length(); i++){
                    jsonobjectU = jsonarray.getJSONObject(i);
                    Integer coincidencias=0;
                    respuestasMatch = new ArrayList<String>();
                    if(idusuario!=Integer.parseInt(jsonobjectU.getString("idusuario"))){

                        //LLAMAR INFO DE CADA UNO
                        try {
                            url=new URL("https://lovefoodservices.herokuapp.com/mostrarTodo/"+jsonobjectU.getString("idusuario"));
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
                        JSONObject jsonobjectI=null;
                        try {
                            JSONArray jsonarrayI = new JSONArray(responseText);

                            jsonobjectI = jsonarrayI.getJSONObject(0);
                            Log.d("Recibiendo","ENTRO");
                            respuestasMatch.add(jsonobjectI.getString("sexo"));
                            respuestasMatch.add(jsonobjectI.getString("preferencia"));
                            respuestasMatch.add(jsonobjectI.getString("rspverde"));
                            respuestasMatch.add(jsonobjectI.getString("rspdesayunosalado"));
                            respuestasMatch.add(jsonobjectI.getString("rspdesayunodulce"));
                            respuestasMatch.add(jsonobjectI.getString("rspalmuerzo"));
                            respuestasMatch.add(jsonobjectI.getString("rspmarisco"));
                            respuestasMatch.add(jsonobjectI.getString("rspsopa"));
                            respuestasMatch.add(jsonobjectI.getString("rspcena"));
                            respuestasMatch.add(jsonobjectI.getString("rspcomidatipicacosta"));
                            respuestasMatch.add(jsonobjectI.getString("rspcomidatipicasierra"));
                            respuestasMatch.add(jsonobjectI.getString("rspcomidatipicaoriente"));
                            respuestasMatch.add(jsonobjectI.getString("rspproteina"));
                            respuestasMatch.add(jsonobjectI.getString("rsppostres"));
                            respuestasMatch.add(jsonobjectI.getString("rspsaboresdulces"));
                            respuestasMatch.add(jsonobjectI.getString("rspbebida"));
                            respuestasMatch.add(jsonobjectI.getString("rspcomidaextranjera"));
                            respuestasMatch.add(jsonobjectI.getString("rspcomidarapida"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.d("Usuario", "repetido");
                    }
                }
                listaMatch.add(jsonobjectU);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listaMatch;
        }

        @Override
        protected void onPostExecute(List<JSONObject> lista) {
            super.onPostExecute(lista);
            Log.d("PostExecute", "onPostExecute");
            if (lista!=null){
                Log.d("lista", String.valueOf(lista.size()));
            }else{
                Log.d("Login fail", "Login fail:"+lista);
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