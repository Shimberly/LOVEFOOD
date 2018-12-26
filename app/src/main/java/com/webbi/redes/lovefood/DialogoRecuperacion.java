package com.webbi.redes.lovefood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class DialogoRecuperacion extends DialogFragment {

    public DialogoRecuperacion() {
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }


    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.recuperacionclave, null))

            .setPositiveButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // sign in the user ...
                }
            })
            .setNegativeButton("ENVIAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DialogoRecuperacion.this.getDialog().cancel();
                }
            });


        return builder.create();
    }
}
