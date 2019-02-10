package com.webbi.redes.lovefood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProblemaConexion extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String titulo = getArguments().getString("titulo");
        String texto = getArguments().getString("texto");
        return createSimpleDialog(titulo,texto);
    }


    public AlertDialog createSimpleDialog(String titulo, String texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomDialogTheme);

        builder.setTitle(titulo)
                .setMessage(texto)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones
                            }
                        });

        return builder.create();
    }
}
