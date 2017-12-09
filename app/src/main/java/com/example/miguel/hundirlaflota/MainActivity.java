package com.example.miguel.hundirlaflota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, DialogBarcos.CambiaImagen, DiaConfiguracion.CambiaNivel {


    private Menu menu;
    private int rango = 5;
    private int intentos = 10;
    private int imgPeq = R.mipmap.ic_sub_small;
    private int imgMed = R.mipmap.ic_sub_medium;
    private int imgGran = R.mipmap.ic_sub_big;
    private int imgAgua = R.mipmap.ic_agua;
    private int anteriorXMediano = -1;
    private int anteriorYMediano = -1;
    private int anteriorXGrande = -1;
    private int anteriorYGrande = -1;
    private int bPeq = 3;
    private int bMed = 2;
    private int bGrand = 1;
    private int puntos = bPeq + bMed + bGrand;
    private int mediano = 0;
    private int grande = 0;
    private int ancho, alto;
    private boolean jugando = false;
    private int distribucion[][] = new int[rango][rango];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generarPartida();

    }
    //metodo que instancia los  metodos para generar la partida
    public void generarPartida() {
        ponerBarcosGrandes(bGrand, 3);
        ponerBarcosMedianos(bMed, 2);
        ponerBarcosPequeños(bPeq, 1);
        calcularTamañoBoton();
        setTablero(rango * rango);
        jugando = false;
    }
    // metodo para las pulsaciones largas
    @Override
    public boolean onLongClick(View v) {
        //comporbamos si la partida esta iniciada
        if (jugando) {
            //declaramos las variables necesarias
            Button b = (Button) v;
            int id = b.getId();
            int y = id / rango;
            int x = id % rango;
            //si hemos descubierto una casilla de barco mediano
            if (mediano > 0) {
                //comporbamos que se pulsa una casilla colindante
                if (estaJunto(x, y, anteriorXMediano, anteriorYMediano)) {
                    //comprobamos el valor de la matriz en cada casilla
                    switch (distribucion[y][x]) {
                        //si es cero mostramos agua y reducimos los intentos que quedan
                        case 0:
                            b.setBackground(getDrawable(imgAgua));
                            intentos -= 1;
                            Toast.makeText(this, getString(R.string.agua_1) + intentos
                                    + getString(R.string.agua_2), Toast.LENGTH_LONG).show();
                            break;
                        //si es 1 mostramos el barco pequeño y descontamos un barco del total
                        case 1:
                            b.setBackground(getDrawable(imgPeq));
                            puntos -= 1;
                            Toast.makeText(this, getString(R.string.hundido_1)
                                    + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                            break;
                            // si es dos hemos undido un barco mediano
                        case 2:
                            b.setBackground(getDrawable(imgMed));
                            puntos -= 1;
                            mediano = 0;
                            Toast.makeText(this, getString(R.string.hundido_1)
                                    + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                            break;
                            //si es tres estamos en un barco grande
                        case 3:
                            //Si habiamos descubierto dos casillas hemos hundido el barco
                            if (grande == 2) {
                                b.setBackground(getDrawable(imgGran));
                                puntos -= 1;
                                grande = 0;
                                Toast.makeText(this, getString(R.string.hundido_1)
                                        + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                                break;
                            //Si no hemos tocado el barco
                            } else {
                                b.setBackground(getDrawable(imgGran));
                                anteriorXGrande = x;
                                anteriorYGrande = y;
                                grande += 1;
                                Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                    // si no estamos en las casillas colindantes avisamos del error
                } else {
                    Toast.makeText(this, getString(R.string.casillalejos), Toast.LENGTH_LONG).show();
                }
            }
            //Si habianos tocado un barco grande
            if (grande > 0) {
                //comporbamos que se pulsa una casilla colindante
                if (estaJunto(x, y, anteriorXGrande, anteriorYGrande)) {
                    //comprobamos el valor de la matriz en cada casilla
                    switch (distribucion[y][x]) {
                        //si es cero mostramos agua y reducimos los intentos que quedan
                        case 0:
                            b.setBackground(getDrawable(imgAgua));
                            intentos -= 1;
                            Toast.makeText(this, getString(R.string.agua_1)
                                    + intentos + getString(R.string.agua_2), Toast.LENGTH_LONG).show();
                            break;
                        //si es 1 mostramos el barco pequeño y descontamos un barco del total
                        case 1:
                            b.setBackground(getDrawable(imgPeq));
                            puntos -= 1;
                            Toast.makeText(this, getString(R.string.hundido_1)
                                    + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            //si es dos marcamos que se ha tocado el barco ( si ya se habia tocado pasamos por el if anterior)
                            b.setBackground(getDrawable(imgMed));
                            anteriorXMediano = x;
                            anteriorYMediano = y;
                            mediano = 1;
                            Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                            break;
                        case 3:
                            //si es un barco grande y ya lo habiamos tocado una vez marcamos el segundo tocado
                            if (grande == 1) {
                                b.setBackground(getDrawable(imgGran));
                                anteriorXGrande = x;
                                anteriorYGrande = y;
                                grande = 2;
                                Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                            } else {
                                //si es la segunda vez lo hundimos
                                b.setBackground(getDrawable(imgGran));
                                puntos -= 1;
                                grande = 0;
                                Toast.makeText(this, getString(R.string.hundido_1)
                                        + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                                break;
                            }
                            break;
                    }
                } else {
                    //si no estamamos en casilla colindante avisamos al usuario
                    Toast.makeText(this, getString(R.string.casillalejos), Toast.LENGTH_LONG).show();
                }
            } else {
                //resto de los casos (no hay barco tocado)
                switch (distribucion[y][x]) {
                    //si es cero mostramos agua y descontamos un intento
                    case 0:
                        b.setBackground(getDrawable(imgAgua));
                        intentos -= 1;
                        Toast.makeText(this, getString(R.string.agua_1)
                                + intentos + getString(R.string.agua_2), Toast.LENGTH_LONG).show();
                        break;
                    //si es 1 hemos hundido un barco
                    case 1:
                        b.setBackground(getDrawable(imgPeq));
                        puntos -= 1;
                        Toast.makeText(this, getString(R.string.hundido_1)
                                + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                        break;
                    //si es dos es el primer tocado del barco
                    case 2:
                        b.setBackground(getDrawable(imgMed));
                        anteriorXMediano = x;
                        anteriorYMediano = y;
                        mediano = 1;
                        Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                        break;
                    //si es tres es el primer tocado del barco
                    case 3:
                            b.setBackground(getDrawable(imgGran));
                            anteriorXGrande = x;
                            anteriorYGrande = y;
                            grande = 1;
                            Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                        break;

                }
                //comporbamos si el usuario a ganado , perdido o la partida continua
                resultado();
            }
        }

        return true;
    }

    //metodo para descubrir casillas por pulsacion simple
    public void onClick(View v) {
        //comprobamos si se esta jugando
        if (jugando) {
            Button b = (Button) v;
            int id = b.getId();
            int y = id / rango;
            int x = id % rango;
            //comprobamos el valor de la matriz en la posicion
            switch (distribucion[y][x]) {
                //si es 0 es agua, mostramos agua y descontamos un intento
                case 0:
                    b.setBackground(getDrawable(imgAgua));
                    intentos -= 1;
                    Toast.makeText(this, getString(R.string.agua_1)
                            + intentos + getString(R.string.agua_2), Toast.LENGTH_LONG).show();
                    break;
                //si es uno hundimos un barco pequeño, mostramos el barco y reducimos un barco por hundir
                case 1:
                    b.setBackground(getDrawable(imgPeq));
                    puntos -= 1;
                    Toast.makeText(this, getString(R.string.hundido_1) + puntos + getString(R.string.hundido_2), Toast.LENGTH_LONG).show();
                    break;
                //si es dos es un barco mediano
                case 2:
                    //si habiamos descubierto una casilla del barco mediano antes pierde el jugador (pasamos intentos a 0)
                    if (mediano != 0) {
                        b.setBackground(getDrawable(imgMed));
                        intentos = 0;
                    //si no indicamos el tocado del barco y anotamos la posicion
                    } else {
                        b.setBackground(getDrawable(imgMed));
                        anteriorXMediano = x;
                        anteriorYMediano = y;
                        mediano = 1;
                        Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                    }
                    break;
                //si es tres es un barco grande
                case 3:
                    //si habiamos descubierto una casilla del barco Grande antes pierde el jugador (pasamos intentos a 0)
                    if (grande != 0) {
                        b.setBackground(getDrawable(imgGran));
                        intentos = 0;
                        break;
                    //si no indicamos el tocado del barco y anotamos la posicion
                    } else {
                        b.setBackground(getDrawable(imgGran));
                        anteriorXGrande = x;
                        anteriorYGrande = y;
                        grande = 1;
                        Toast.makeText(this, getString(R.string.tocado), Toast.LENGTH_LONG).show();
                    }
                    break;

            }
            //comporbamos si el usuario a ganado , perdido o la partida continua
            resultado();
        }
    }
    //metodo para comprobar el resultado de la partida tras cada pulsacion
    private void resultado() {
        // si se ha quedado sin intentos pierde
        if (intentos == 0) {
            jugando = false;
            Toast.makeText(this, getString(R.string.perder), Toast.LENGTH_LONG).show();
        }
        //si no quedan barcos (puntos) gana
        if (puntos == 0) {
            jugando = false;
            Toast.makeText(this, getString(R.string.ganar), Toast.LENGTH_LONG).show();
        }
    }

    //metodo bboleano apara comprobar si la casilla esta junto a la anterior
    private boolean estaJunto(int x, int y, int antiguoX, int antiguoY) {
        boolean secumple = false;
        if (y - 1 <= antiguoY & antiguoY <= y + 1) {
            secumple = true;
        }
        return secumple;
    }

    //metodo para calcular el tamaño de los botones y ajustarlo a la pantalla
    private void calcularTamañoBoton() {
        //obtenemos una instancia del display de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        //obtenemos un punto definimo al final del display
        Point size = new Point();
        display.getSize(size);
        //obtenemos el tamaño de los botones dividiendo entre el rango de la matriz
        ancho = (int) ((size.x * 0.9) / rango);
        alto = (int) ((size.y * 0.8) / rango);
    }

    /*metodo para poner los barcos grandes en la matriz, dado que entiendo que estos barcos
    * deben ser lineales he definido una matriz de 3x3 para delimitar cada tipo de barco, cada barco
    * se crea de manera aleatoria asignandosele la forma por un switch activado por un random*/
    public void ponerBarcosGrandes(int barcos, int tipo) {
        int contador = 0;
        int col, fil;
        boolean libre;
        //usamos un bucle while para generar los barcos indicados, cada vez que se genera uno sube
        // el contador hasta que iguala al valor que nos solicita el inicio del nivel de dificultad
            while (contador < barcos) {
                int barco[][] = new int[tipo][tipo];
                int forma = (int) (Math.random() * 4);
                //Switch que define las formas posibles del barco
                switch (forma) {
                    case 0:
                        barco[0][0] = tipo;
                        barco[1][1] = tipo;
                        barco[2][2] = tipo;
                        break;
                    case 1:
                        barco[0][1] = tipo;
                        barco[1][1] = tipo;
                        barco[2][1] = tipo;
                        break;
                    case 2:
                        barco[0][2] = tipo;
                        barco[1][1] = tipo;
                        barco[2][0] = tipo;
                        break;
                    case 3:
                        barco[1][0] = tipo;
                        barco[1][1] = tipo;
                        barco[1][2] = tipo;
                        break;
                }

                //generamos una posicion aleatoria separada una casilla de los bordes
                col = 1 + (int) (Math.random() * (rango - 2));
                fil = 1 + (int) (Math.random() * (rango - 2));
                //comparamos la posicion y sus casillas inmediatas con el barco.
                libre = true;
                for (int y = -1; y <= +1; y++) {
                    for (int x = -1; x <= +1; x++) {
                        if (distribucion[col + y][fil + x] != 0) {
                            libre = false;
                        }
                    }
                }
                //ponemos el barco en la matriz
                if (libre) {
                    for (int y = -1; y <= +1; y++) {
                        for (int x = -1; x <= +1; x++) {
                            distribucion[col + y][fil + x] = barco[1 + y][1 + x];
                        }
                    }
                    contador++;
                }

            }
    }

    /*metodo para poner los barcos medianos en la matriz, dado que entiendo que estos barcos
    * deben ser lineales he definido una matriz de 2x2 para delimitar cada tipo de barco, cada barco
    * se crea de manera aleatoria asignandosele la forma por un switch activado por un random*/
    public void ponerBarcosMedianos(int barcos, int tipo) {
        int contador = 0;
        int col, fil;
        //usamos un bucle while para generar los barcos indicados, cada vez que se genera uno sube
        // el contador hasta que iguala al valor que nos solicita el inicio del nivel de dificultad
            while (contador < barcos) {
                int barco[][] = new int[tipo][tipo];
                int forma = (int) (Math.random() * 4);
                //Switch que define las formas posibles del barco
                switch (forma) {
                    case 0:
                        barco[0][0] = 2;
                        barco[0][1] = 2;
                        break;
                    case 1:
                        barco[0][0] = 2;
                        barco[1][1] = 2;
                        break;
                    case 2:
                        barco[0][0] = 2;
                        barco[1][0] = 2;
                        break;
                    case 3:
                        barco[0][1] = 2;
                        barco[1][1] = 2;
                        break;
                    case 4:
                        barco[1][1] = 2;
                        barco[1][0] = 2;
                        break;
                    case 5:
                        barco[0][1] = 2;
                        barco[1][0] = 2;
                        break;
                }

                //generamos una posicion aleatoria separada una casilla del borde final
                col = (int) (Math.random() * (rango - 1));
                fil = (int) (Math.random() * (rango - 1));
                //comparamos si la posicion y sus casillas inmediatas estan vacias.
                int haybarco = 0;
                for (int y = 0; y <= 1; y++) {
                    for (int x = 0; x <= 1; x++) {
                        if (distribucion[col + y][fil + x] != 0) {
                            haybarco++;
                        }
                    }
                }
                if (haybarco == 0) {
                    for (int y = 0; y <= 1; y++) {
                        for (int x = 0; x <= 1; x++) {
                            if (barco[y][x] != 0) {
                                distribucion[col + y][fil + x] = barco[y][x];
                            }
                        }
                    }
                    contador++;
                }
            }

    }

    //metodo que genera los barcos pequeños, como son de una casilla solo, simplemente se comprueba si
    //la casilla esta libre
    public void ponerBarcosPequeños(int barcos, int tipo) {
        int contador = 0;
        int col, fil;
        while (contador < barcos) {
            //generamos una posicion aleatoria separada una casilla del borde final
            col = (int) (Math.random() * (rango));
            fil = (int) (Math.random() * (rango));
            //comprobamos si esta vacia y ponemos el barco;
            if (distribucion[col][fil] == 0) {
                distribucion[col][fil] = tipo;
                contador++;
            }
        }

    }

    //metodo para definir el tablero
    private void setTablero(int c) {
        //definimos ina instancia de nuestro GridLayout
        GridLayout g = findViewById(R.id.myGird);
        //eliminamos cualquier boton que pudiese existir
        g.removeAllViewsInLayout();
        //ponemos el padding a 0 para aprobechar la pantalla
        g.setPadding(0, 0, 0, 0);
        //definimos el numero de filas y columnas
        g.setColumnCount(rango);
        g.setRowCount(rango);
        //instanciamos un boton
        Button b;

        //creamos un bucle que genera los botones y les asigna un id predefinido (su numero de orden)
        for (int i = 0; i < c; i++) {
            //iniciamos el boton
            b = new Button(this);
            //definimos el valor de ancho y alto del boton (calculados antes)
            b.setLayoutParams(new ViewGroup.LayoutParams(ancho, alto));
            //asignamos el id al boton
            b.setId(i);
            //definimos los escuchadores
            b.setOnClickListener(this); //registro en el listener el obj. b
            b.setOnLongClickListener(this);
            //añadimos el boton al GridLayout
            g.addView(b, i);
        }
    }

    @Override
    //el metodo para crear el menu  es el estandar, pero poblamos con el el objeto menu que habiamos
    // definido para poder cambiar la imagen
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        //poblamos el objeto menu
        this.menu = menu;
        return true;
    }
    //metodo para ejecutar las distintas opciones del menu (lanzamos instancias de los dialogos)
    public boolean onOptionsItemSelected(MenuItem item) {
        //contenedor de la opcion
        int opcion = item.getItemId();

        switch (opcion) {
            case R.id.menuBarcos:
                DialogBarcos dialogBarcos = new DialogBarcos();
                dialogBarcos.show(getFragmentManager(), "Barcos");
                return true;
            case R.id.instrucciones:
                DiaInstrucciones diaInstrucciones = new DiaInstrucciones();
                diaInstrucciones.show(getFragmentManager(), "Instrucciones");
                return true;
            case R.id.configuracion:
                DiaConfiguracion dConf = new DiaConfiguracion();
                dConf.show(getFragmentManager(), "configuracion");
                return true;
            // en este caso solo definimos la variable jugando a true
            case R.id.jugar:
                jugando = true;
                return true;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //metodo que lanzamos desde el dialogo que define el nivel, en funcion de la respuesta que nos
    // envia definimos las variables que el definen la partida y finalmente lanzamos el metodo que
    // reconstruye el tablero segun los nuevos datos
    @Override
    public void setNivel(int nivel) {
        switch (nivel) {
            case 0:
                rango = 5;
                intentos = 10;
                bPeq = 3;
                bMed = 2;
                bGrand = 1;
                distribucion = new int[rango][rango];
                puntos = bPeq + bMed + bGrand;
                break;
            case 1:
                rango = 8;
                intentos = 20;
                bPeq = 5;
                bMed = 3;
                bGrand = 2;
                distribucion = new int[rango][rango];
                puntos = bPeq + bMed + bGrand;
                break;
            case 2:
                rango = 10;
                intentos = 30;
                bPeq = 7;
                bMed = 5;
                bGrand = 3;
                distribucion = new int[rango][rango];
                puntos = bPeq + bMed + bGrand;
                break;
        }
        generarPartida();
    }

    //metodo que lanzamos desde el dialogo que define los barcos, en funcion de la respuesta que nos
    // envia definimos la imagen que se muestra en el menu y las imagenes de los barcos a usar
    @Override
    public void setBarcos(String opcion) {
        switch (opcion) {
            case "velero":
                menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_medium, null));
                imgPeq = R.mipmap.ic_small;
                imgMed = R.mipmap.ic_medium;
                imgGran = R.mipmap.ic_big;
                break;
            case "submarino":
                menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_sub_medium, null));
                imgPeq = R.mipmap.ic_sub_small;
                imgMed = R.mipmap.ic_sub_medium;
                imgGran = R.mipmap.ic_sub_big;
                break;
            case "pirata":
                menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_pirata_medium, null));
                imgPeq = R.mipmap.ic_pirata;
                imgMed = R.mipmap.ic_pirata_medium;
                imgGran = R.mipmap.ic_pirata_big;
                break;
        }
    }

    //Dialogo que muestra las instrucciones, como es muy basico lo definimos como subclase
    public static class DiaInstrucciones extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Usamos la clase Builder para construir el diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //Escribimos el título
            builder.setTitle(getString(R.string.instrucciones));
            //Escribimos el texto
            builder.setMessage(getString(R.string.Instrucciones_descripcion));
            //añadimos el botón de Si y su acción asociada
            builder.setNeutralButton(getString(R.string.boton_volver), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            // Crear el AlertDialog y devolverlo
            return builder.create();
        }

    }

}
