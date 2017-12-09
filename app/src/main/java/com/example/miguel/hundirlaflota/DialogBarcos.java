package com.example.miguel.hundirlaflota;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class DialogBarcos extends DialogFragment {
    //definimos los arrays de datos
    String[] nombres = {"velero", "submarino", "pirata"};
    int imagenes[] = {R.mipmap.ic_medium, R.mipmap.ic_sub_medium,R.mipmap.ic_pirata_medium};
    //definimos un textview para almacenar el valor de nuestro spinner
    TextView tv;
    //String para almacenar la respuesta
    String respuesta;
    //instanciamos la interface para poder invocar el metodo de respuesta
    CambiaImagen cI;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //construimos el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //generamos el inflador
        View seleccion = getActivity().getLayoutInflater().inflate(R.layout.menu_barcos,null);
        final Spinner spinner = (Spinner) seleccion.findViewById(R.id.spinMenu);
        spinner.setAdapter(new AdaptadorBarcos(getActivity(),R.layout.menu_barcos, this.nombres));
        //definimo los escuchadores para la seleccion de los items
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //recogemos el valor del spinner en la variable respuesta
                tv = spinner.findViewById(R.id.tVBarco);
                respuesta = (String)tv.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ///recogemos el valor del spinner en la variable respuesta
                tv = spinner.findViewById(R.id.tVBarco);
                respuesta = (String)tv.getText();
            }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.menu_barcos,null);
        final TextView tv = v.findViewById(R.id.tVBarco);
        builder.setView(seleccion);
        //añadimos un boton volver para enviar la respuesta
        builder.setNegativeButton(getString(R.string.boton_volver), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //enviamos la respuesta a la actividada
                cI.setBarcos(respuesta);
            }
        });
        return builder.create();
    }

    //Subclase adaptador para insertar el spinner personalizado
    public class AdaptadorBarcos extends ArrayAdapter<String> {
        public AdaptadorBarcos(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return cFBarco(position, convertView, parent);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return cFBarco(position, convertView, parent);
        }

        public View cFBarco(int position, View convertView, ViewGroup parent) {
            View miFila = DialogBarcos.this.getActivity().getLayoutInflater().inflate(R.layout.spinner_barcos, parent, false);
            ((TextView) miFila.findViewById(R.id.tVBarco)).setText(DialogBarcos.this.nombres[position]);
            ((ImageView) miFila.findViewById(R.id.iVBarco)).setImageResource(DialogBarcos.this.imagenes[position]);
            return miFila;
        }

    }
    //interface para enviar la respuesta a la actividad
    public interface CambiaImagen{
        ///metodo que sobreescribiremos en la clase MainActivity
        void setBarcos(String opcion);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cI=(DialogBarcos.CambiaImagen)activity;
    }

}
