package com.webbi.redes.lovefood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import javax.net.ssl.HttpsURLConnection;

import static com.webbi.redes.lovefood.Login.PREFS_KEY;


public class PerfilFragment extends Fragment {
    private static final String TAG = "AsyncTaskActivity";

    public String pathU = null;
    public String pathI = null;
    private static final int MODE_PRIVATE = 1;
    java.net.URL url;
    String responseText;
    StringBuffer response;

    TextView txtNombre;
    TextView txtCorreo;
    TextView txtEdad;
    TextView txtUniversidad;
    TextView txtCiudad;
    TextView txtInteres;
    TextView txtInstagram;
    TextView txtNumero;
    TextView txtDescripcion;
    ImageView fotoPerfil;

    Integer idusuario;
    ServicioWeb servicio;
    View view;
    String nombre;
    List<String> list;
    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        txtNombre= view.findViewById(R.id.txtNombre);
        txtCorreo= view.findViewById(R.id.txtCorreo);
        txtEdad= view.findViewById(R.id.txtEdad);
        txtUniversidad= view.findViewById(R.id.txtUniversidad);
        txtCiudad= view.findViewById(R.id.txtLugar);
        txtDescripcion= view.findViewById(R.id.txtDescripcion);
        txtInteres= view.findViewById(R.id.txtPreferencia);
        txtNumero= view.findViewById(R.id.txtWa);
        txtInstagram= view.findViewById(R.id.txtIg);
        fotoPerfil=view.findViewById(R.id.fotoPerfil);

        idusuario= Integer.valueOf(leerValor(getContext(),"idusuario"));
        Log.d("LOGEADO", leerValor(getContext(),"idusuario"));
        pathU = "https://lovefoodservices.herokuapp.com/mostrarUsuario/"+idusuario;
        pathI = "https://lovefoodservices.herokuapp.com/mostrarInformacion/"+idusuario;

        list = new ArrayList<String>();
        if(isConnectedToInternet())
        {
            // Run AsyncTask
            list = new ArrayList<String>();
            servicio = (ServicioWeb) new ServicioWeb().execute();
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

        return view;
    }
    public static String leerValor(Context context, String keyPref) {
        @SuppressLint("WrongConstant") SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
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


    private class ServicioWeb extends AsyncTask<Integer, Integer, List<String>> {



        @Override
        protected List<String> doInBackground(Integer... params) {
            return getWebServiceResponseData();
        }

        protected List<String> getWebServiceResponseData() {

            try {
                url=new URL(pathU);
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
            } catch (Exception e) {
                e.printStackTrace();
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "Problema al conectar con el servidor de LOVEFOOD");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");
                servicio.cancel(true);
            }
            //Call ServerData() method to call webservice and store result in response
            //  response = service.ServerData(path, postDataParams);
            Log.d(TAG, "data:" + responseText);
            JSONObject jsonobjectU=null;
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
                url=new URL(pathI);
                nombre="";
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
                servicio.cancel(true);
            }
            //Call ServerData() method to call webservice and store result in response
            //  response = service.ServerData(path, postDataParams);
            Log.d(TAG, "data:" + responseText);
            JSONObject jsonobjectI=null;
            try {
                JSONArray jsonarray = new JSONArray(responseText);

                jsonobjectI = jsonarray.getJSONObject(0);
                list.add(jsonobjectI.getString("universidad"));
                list.add(jsonobjectI.getString("ciudad"));
                list.add(jsonobjectI.getString("descripcion"));
                list.add(jsonobjectI.getString("instagram"));
                list.add(jsonobjectI.getString("preferencia"));
                list.add(jsonobjectI.getString("numero"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(List<String> lista) {
            super.onPostExecute(lista);
            Log.d(TAG, "onPostExecute");
            if (lista!=null){
                Log.d("lista", String.valueOf(lista.size()));

                txtNombre.setText(lista.get(0)+" "+lista.get(1));
                txtCorreo.setText(""+lista.get(2));
                txtUniversidad.setText(""+lista.get(5));
                txtCiudad.setText(""+lista.get(6));
                txtDescripcion.setText(""+lista.get(7));
                txtInstagram.setText(""+lista.get(8));
                txtInteres.setText("Interes en "+lista.get(9));
                txtNumero.setText(""+lista.get(10));
                Log.d("sexo",lista.get(3));
                if (lista.get(3).equals("mujer")){
                    fotoPerfil.setImageResource(R.drawable.girl);
                }else{
                    fotoPerfil.setImageResource(R.drawable.boy);
                }

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaNac = LocalDate.parse(lista.get(4), fmt);
                LocalDate ahora = LocalDate.now();

                Period periodo = Period.between(fechaNac, ahora);
                System.out.printf("Tu edad es: %s años, %s meses y %s días",
                        periodo.getYears(), periodo.getMonths(), periodo.getDays());
                Log.d("edad", String.valueOf(periodo.getYears()));
                txtEdad.setText(String.valueOf(periodo.getYears()));
            }else{
                Log.d(TAG, "Login fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo cargar los datos");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }

        }

    }


}