package com.webbi.redes.lovefood;

//import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    public final static String PREFS_KEY = "mispreferencias";
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
        txtInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtInstagram.getText()!=""){
                    String urlPage = "https://www.instagram.com/"+txtInstagram.getText();
                    Log.d("link ig",urlPage);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }

            }

        });
        idusuario= Integer.valueOf(leerValor(getContext(),"idusuario"));
        Log.d("LOGEADO", leerValor(getContext(),"idusuario"));
        pathU = "https://lovefoodservices.herokuapp.com/mostrarUsuario/"+idusuario;
        pathI = "https://lovefoodservices.herokuapp.com/mostrarInformacion/"+idusuario;
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
                list.add(jsonobjectI.getString("foto"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

       /* @RequiresApi(api = Build.VERSION_CODES.O)*/
        @Override
        protected void onPostExecute(List<String> lista) {
            super.onPostExecute(lista);
            Log.d(TAG, "onPostExecute");
            if (lista.size()>0){
                Log.d("lista", String.valueOf(lista.size()));

                txtNombre.setText(lista.get(0)+" "+lista.get(1));
                txtCorreo.setText(""+lista.get(2));
                if(lista.get(5).isEmpty()){
                    txtUniversidad.setText("Universidad");
                }else {
                    txtUniversidad.setText("Universidad: " + lista.get(5));
                }
                if(lista.get(6).isEmpty()){
                    txtCiudad.setText("Ciudad");
                }else {
                    txtCiudad.setText("Vivo en: " + lista.get(6));;
                }
                if(lista.get(7).isEmpty()){
                    txtDescripcion.setText("Escribe algo sobre ti");
                }else {
                    txtDescripcion.setText("" + lista.get(7));
                }
                if(lista.get(8).isEmpty()){
                    txtInstagram.setText("Usuario IG");
                }else {
                    txtInstagram.setText("" + lista.get(8));
                }
                if(lista.get(9).isEmpty()){
                    txtInteres.setText("Interés en ... ");
                }else {
                    txtInteres.setText("Interés en " + lista.get(9));
                }
                if(lista.get(10).isEmpty()){
                    txtNumero.setText("Número");
                }else {
                    txtNumero.setText("" + lista.get(10));
                }
                if(!lista.get(11).equals("")){
                    Picasso.get().load(lista.get(11)).into(fotoPerfil);
                }else{
                    if (lista.get(3).equals("Mujeres")){
                        fotoPerfil.setImageResource(R.drawable.girl);

                    }else{
                        fotoPerfil.setImageResource(R.drawable.boy);
                    }

                }
                Log.d("instagram",lista.get(8));
                Log.d("sexo",lista.get(3));
              /*  try {
                    AppPreferences appPreferences = null;
                    appPreferences = new AppPreferences(getContext());
                    Picasso.get().load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(fotoPerfil);
                } catch (Exception e) {
                    if (lista.get(3).equals("Mujer")){
                        fotoPerfil.setImageResource(R.drawable.girl);
                    }else{
                        fotoPerfil.setImageResource(R.drawable.boy);
                    }
                }*/


                DateTimeFormatter fmt = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaNac = LocalDate.parse(lista.get(4), fmt);
                    LocalDate ahora = LocalDate.now();

                    Period periodo = Period.between(fechaNac, ahora);
                    System.out.printf("Tu edad es: %s años, %s meses y %s días",
                            periodo.getYears(), periodo.getMonths(), periodo.getDays());
                    Log.d("edad", String.valueOf(periodo.getYears()));

                    txtEdad.setText(String.valueOf(periodo.getYears()));
                }else{
                    txtEdad.setText(String.valueOf(lista.get(4)));
                }


                }else{
                Log.d(TAG, "Login fail:" + nombre);
                Bundle args = new Bundle();
                args.putString("titulo", "Advertencia");
                args.putString("texto", "No se pudo cargar los datos");
                ProblemaConexion f=new ProblemaConexion();
                f.setArguments(args);
                f.show(getFragmentManager(), "ProblemaConexión");
                SharedPreferences settings = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }

        }

    }




}