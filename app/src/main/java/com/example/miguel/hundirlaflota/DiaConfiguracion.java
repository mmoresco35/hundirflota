package com.example.miguel.hundirlaflota;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

public class DiaConfiguracion extends DialogFragment{
    DiaConfiguracion.CambiaNivel cN;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Usamos la clase Builder para construir el diálogo
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //obtenemos inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v =inflater.inflate(R.layout.configuracion_layout, null);
        final RadioGroup rG = v.findViewById(R.id.rBGNivel);
        builder.setView(v);
        //Escribimos el título
        builder.setTitle(R.string.titulo_configuracion);

        //añadimos el botón de No y su acción asociada
        builder.setNegativeButton(R.string.boton_volver, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int rbChecked = rG.getCheckedRadioButtonId();
                // con el switch envianos distintas respuestas en funcion del boton seleccionado
                switch (rbChecked){
                    case R.id.rBPrincipiante:
                        cN.setNivel(0);
                        break;
                    case R.id.rBIntermedio:
                        cN.setNivel(1);
                        break;
                    case R.id.rBAvanzado:
                        cN.setNivel(2);
                        break;
                }

            }
        });
        // Crear el AlertDialog y devolverlo
        return builder.create();
    }
    //interface que nos permite comunicarnos con la actividad
    public interface CambiaNivel{
        //metodo que sobreescribiremos en la clase MainActivity
        void setNivel(int nivel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cN =(DiaConfiguracion.CambiaNivel)activity;
    }


}