package com.webbi.redes.lovefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import android.widget.Toast;

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
    JSONArray jsonarray;
    TextView txtNombre;
    TextView txtEdad;
    TextView txtCorreo;
    TextView txtUniversidad;
    TextView txtCiudad;
    TextView txtDescripcion;
    Button btnWa;
    Button btnIg;
    ImageView foto;
    ImageView coin;
    BottomNavigationView nav;
    View view;
    Boolean sinEncuesta;
    Boolean error;
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
        protected void onPreExecute() {
            super.onPreExecute();
            error=false;
            sinEncuesta=false;
            //nav.getMenu().getItem(1).setEnabled(false);
        }

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
                jsonarray = new JSONArray(responseText);
                Log.d("Recibiendo","ENTRO");
            } catch (Exception e) {
                e.printStackTrace();
                error=true;

            }
            try {
                listaMatch.add(jsonarray.getJSONObject(0));
                listaMatch.add(jsonarray.getJSONObject(1));
                listaMatch.add(jsonarray.getJSONObject(2));
                listaMatch.add(jsonarray.getJSONObject(3));
                listaMatch.add(jsonarray.getJSONObject(4));
            } catch (Exception e) {
                e.printStackTrace();
                sinEncuesta=true;

            }

            return listaMatch;
        }

        @Override
        protected void onPostExecute(List<JSONObject> listaMatch) {
            super.onPostExecute(listaMatch);
            Log.d("PostExecute", "onPostExecute");
            progressBar.setVisibility(View.GONE);
            if(error){
                Toast.makeText(getActivity(), "¡Problemas con el servidor!",
                        Toast.LENGTH_LONG).show();
                servicioMatch.cancel(true);

            }else {
                if (sinEncuesta) {
                    Toast.makeText(getActivity(), "¡Te falta llenar los datoa de tu perfil o la encuesta!",
                            Toast.LENGTH_LONG).show();

                }else {
                    try {

                        if (listaMatch.size() > 0) {
                            Log.d("lista", String.valueOf(listaMatch.size()));


                            for (int i = 0; i < 5; i++) {
                                txtNombre = (TextView) view.findViewById(Integer.parseInt(String.valueOf(R.id.txtNombre + (i + 1))));
                                txtEdad = (TextView) view.findViewById(Integer.parseInt(String.valueOf(R.id.txtEdad + (i + 1))));
                                txtCorreo = (TextView) view.findViewById(Integer.parseInt(String.valueOf(R.id.txtCorreo + (i + 1))));
                                txtUniversidad = (TextView) view.findViewById(Integer.parseInt(String.valueOf(R.id.txtUniversidad + (i + 1))));
                                txtCiudad = (TextView) view.findViewWithTag("txtCiudad" + (i + 1));
                                txtDescripcion = (TextView) view.findViewById(Integer.parseInt(String.valueOf(R.id.txtDescripcion + (i + 1))));
                                btnIg = (Button) view.findViewWithTag("btnIg" + (i + 1));
                                btnWa = (Button) view.findViewWithTag("btnWa" + (i + 1));
                                coin = (ImageView) view.findViewWithTag("imagematch" + (i + 1));
                                foto = (ImageView) view.findViewWithTag("foto" + (i + 1));
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
                                    } else {
                                        txtEdad.setText(String.valueOf(listaMatch.get(i).getString("fecha_nacimiento")));
                                    }
                                    txtCorreo.setText(listaMatch.get(i).getString("correo"));
                                    txtUniversidad.setText("Universidad " + listaMatch.get(i).getString("universidad"));
                                    txtCiudad.setText(listaMatch.get(i).getString("ciudad"));
                                    txtDescripcion.setText(listaMatch.get(i).getString("descripcion"));
                                    btnIg.setText(listaMatch.get(i).getString("instagram"));
                                    btnWa.setText(listaMatch.get(i).getString("numero"));
                                    if (listaMatch.get(i).getString("sexo").equals("Mujeres")) {
                                        foto.setImageResource(R.drawable.girl);
                                    } else {
                                        foto.setImageResource(R.drawable.boy);
                                    }
                                    switch (listaMatch.get(i).getString("coincidencias")) {
                                        case "1":
                                            coin.setImageResource(R.drawable.corazon1);
                                            break;
                                        case "2":
                                            coin.setImageResource(R.drawable.corazon2);
                                            break;
                                        case "3":
                                            coin.setImageResource(R.drawable.corazon3);
                                            break;
                                        case "4":
                                            coin.setImageResource(R.drawable.corazon4);
                                            break;
                                        case "5":
                                            coin.setImageResource(R.drawable.corazon5);
                                            break;
                                        case "6":
                                            coin.setImageResource(R.drawable.corazon6);
                                            break;
                                        case "7":
                                            coin.setImageResource(R.drawable.corazon7);
                                            break;
                                        case "8":
                                            coin.setImageResource(R.drawable.corazon8);
                                            break;
                                        case "9":
                                            coin.setImageResource(R.drawable.corazon9);
                                            break;
                                        case "10":
                                            coin.setImageResource(R.drawable.corazon10);
                                            break;
                                        case "11":
                                            coin.setImageResource(R.drawable.corazon11);
                                            break;
                                        case "12":
                                            coin.setImageResource(R.drawable.corazon12);
                                            break;
                                        case "13":
                                            coin.setImageResource(R.drawable.corazon13);
                                            break;
                                        case "14":
                                            coin.setImageResource(R.drawable.corazon14);
                                            break;
                                        case "15":
                                            coin.setImageResource(R.drawable.corazon15);
                                            break;
                                        case "16":
                                            coin.setImageResource(R.drawable.corazon16);
                                            break;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Bundle args = new Bundle();
                            args.putString("titulo", "¡Matchs localizados!");
                            args.putString("texto", "No tengas miedo de encontrar el amor. ¡Escríbeles!");
                            ProblemaConexion f = new ProblemaConexion();
                            f.setArguments(args);
                            f.show(getFragmentManager(), "ProblemaConexión");
                            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(800);
                            }
                        } else {
                            Log.d("Login fail", "Login fail:" + listaMatch);
                            Toast.makeText(getActivity(), "¡No se pudieron cargar los datos!",
                                    Toast.LENGTH_LONG).show();
                            servicioMatch.cancel(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //nav.getMenu().getItem(1).setEnabled(true);
                    }
                }
            }
        }
    }


}