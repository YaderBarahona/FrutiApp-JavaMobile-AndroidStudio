package com.example.frutiapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity6_NivelCinco extends AppCompatActivity {

    private TextView textViewNombre;
    private TextView textViewScore;
    private ImageView imageViewVidas, imageViewNumeroUno, imageViewNumeroDos;
    private EditText editTxtRespuesta;
    private MediaPlayer mp, mpGreat, mpBad;

    //variables globales
    int score, numeroAleatorioUno, numeroAleatorioDos, resultado, vidas = 3;
    String nombreJugador, stringScore, stringVidas;

    //array con los nombres de las imagenes de los numeros
    String numero[] = {"cero","uno","dos","tres","cuatro","cinco", "seis","siete","ocho", "nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity6_nivel_cinco);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#9C27B0"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        Toast.makeText(this, "Nivel 5: Multiplicaciones", Toast.LENGTH_SHORT).show();

        textViewNombre = (TextView) findViewById(R.id.textViewNombre);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        imageViewVidas = (ImageView) findViewById(R.id.imageViewVidas);
        imageViewNumeroUno = (ImageView) findViewById(R.id.imageViewNumeroUno);
        imageViewNumeroDos = (ImageView) findViewById(R.id.imageViewNumeroDos);
        editTxtRespuesta = (EditText) findViewById(R.id.editTextRespuesta);

        //obtenemos el nombre enviado desde el activity de bienvenida
        nombreJugador = getIntent().getStringExtra("nombre");
        textViewNombre.setText("Jugador: "+nombreJugador);

        //obtenemos el score enviado desde el activity del nivel 1
        stringScore = getIntent().getStringExtra("score");
        score = Integer.parseInt(stringScore);

        //mostramos el score actual en el TextView
        textViewScore.setText("Puntaje: "+score);

        //obtenemos las vidas enviadas desde el activity del nivel 1
        stringVidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(stringVidas);

        //mostramos las vidas actuales en el TextView
        textViewScore.setText("Manzanas restantes: "+vidas);

        //actualizamos la imagen de las vidas conforme las vidas que tenga el jugador actual
        if(vidas == 3){
            imageViewVidas.setImageResource(R.drawable.tresvidas);
        }

        if(vidas == 2){
            imageViewVidas.setImageResource(R.drawable.dosvidas);
        }

        if(vidas == 1){
            imageViewVidas.setImageResource(R.drawable.unavida);
        }

        //colocamos el icono al actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //pista de audio de fondo
        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        //pista de audio respuesta correcta
        mpGreat = MediaPlayer.create(this, R.raw.wonderful);

        //pista de audio respuesta incorrecta
        mpBad = MediaPlayer.create(this, R.raw.bad);

        //ejecutamos el metodo para generar los numeros aleatorios
        NumeroAleatorio();

    }

    public void NumeroAleatorio(){

        if(score <= 49){

            numeroAleatorioUno = (int) (Math.random() * 10);
            numeroAleatorioDos = (int) (Math.random() * 10);

            resultado = numeroAleatorioUno * numeroAleatorioDos;

            //for para recorrer el araray numero que contiene el nombre de las imagenes
            for(int i=0; i<numero.length; i++){

                //recuperamos el nombre del numero en la posicion del array
                int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());

                //condicionales para colocar la imagen del numero en el ImageView segun el iterador que salga
                if(numeroAleatorioUno == i){
                    imageViewNumeroUno.setImageResource(id);
                }

                if(numeroAleatorioDos == i){
                    imageViewNumeroDos.setImageResource(id);
                }

            }

        } else {
            //si el score es igual o mayor a 20 pasar al siguiente activity con el nombre, score y vidas
            Intent in = new Intent(this, MainActivity7_NivelSeis.class);

            //score del jugador
            stringScore = String.valueOf(score);

            //vidas del jugador
            stringVidas = String.valueOf(vidas);

            //enviamos nombre, score y vidas con su respectivo indicador o key
            in.putExtra("nombre", nombreJugador);
            in.putExtra("score", stringScore);
            in.putExtra("vidas", stringVidas);

            //iniciamos el nuevo activity
            startActivity(in);

            //finalizamos el activity actual
            finish();

            //detenemos la pista de audio de fondo actual
            mp.stop();

            //destruimos el objeto de la clase MediaPlayer para liberar recursos
            mp.release();


        }
    }

    //metodo para comprobar la respuesta
    public void ComprobarRespuesta(View v1){

        //igualamos lo que se digite en el EditText a la variable respuesta para su posterior manipulacion
        String respuesta = editTxtRespuesta.getText().toString().trim();

        //condicional para verificar que el campo de respuesta no este vacio
        if(!respuesta.equals("")){
            //parseamos a int la variable respuesta
            int respuestaJugador = Integer.parseInt(respuesta);

            //condicional para la accion de la respuesta correcta o incorrecta
            if(resultado == respuestaJugador){
                //reproducimos el sonido de respuesta correcta
                mpGreat.start();
                //aumentamos el puntaje
                score++;
                //actualizamos el puntaje en pantalla
                textViewScore.setText("Puntos: "+score);
                //limpiamos el campo de la respuesta
                editTxtRespuesta.setText(null);

                //actualizamos el registro en la BD del score
                BaseDatos();
            } else {
                //reproducimos el sonido de respuesta incorrecta
                mpBad.start();
                //decrementamos la vida
                vidas--;

                //actualizamos el registro en la BD del score
                BaseDatos();

                //estructura para controlar las vidas y realizar una accion
                switch(vidas){
                    //3 vidas
                    case 3:
                        //cambiamos la imagen a 3 manzanas
                        imageViewVidas.setImageResource(R.drawable.tresvidas);
                        break;
                    //2 vidas
                    case 2:
                        //cambiamos la imagen a 2 manzanas
                        imageViewVidas.setImageResource(R.drawable.dosvidas);
                        Toast.makeText(this, "Manzanas restantes: " + vidas, Toast.LENGTH_SHORT).show();
                        break;
                    //1 vida
                    case 1:
                        //cambiamos la imagen a 1 manzana
                        imageViewVidas.setImageResource(R.drawable.unavida);
                        Toast.makeText(this, "Manzanas restantes: " + vidas, Toast.LENGTH_SHORT).show();
                        break;
                    //0 vidas
                    case 0:
                        //Notificacion para el usuario
                        Toast.makeText(this, "Te has quedado sin manzanas.. " + vidas, Toast.LENGTH_SHORT).show();
                        //objeto para pasar de activity
                        Intent in = new Intent(this, MainActivity.class);
                        //iniciamos el nuevo activity
                        startActivity(in);
                        //finalizamos el activity actual
                        finish();
                        //detenemos el sonido de la pista
                        mp.stop();
                        //destruimos el objeto para limpiar recursos
                        mpGreat.release();
                        break;
                }

                //limpiamos el campo de respuesta
                editTxtRespuesta.setText(null);

            }

            //actualizamos los numeros de la operacion
            NumeroAleatorio();

        } else {
            Toast.makeText(this, "Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }

    }

    public void BaseDatos (){

        //creamos un objeto de la clase AdminSQLiteOpenHelper para realizar la conexion a la db
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);

        //objetod de la clase SQLiteDatabase para la apertura en escritura y lectura de los datos
        SQLiteDatabase BD = admin.getWritableDatabase();

        //objeto de la clase Cursor para realizar las consultas a la db
        //consulta anidada para seleccionar el puntaje mas alto de la tabla puntaje y asi modificar o mantener el score del jugador
        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);

        //condicional para verificar si se encontro un maximo score o no
        if(consulta.moveToFirst()){
            //recuperamos lo que se encuentre en la primera coluna (nombre) de la tabla y lo igualamos a tempNombre
            String tempNombre = consulta.getString(0);
            //recuperamos lo que se encuentre en la primera coluna (score) de la tabla y lo igualamos a tempNombre
            String tempScore = consulta.getString(1);

            //transformamos el score de string a int para realizar las comparaciones
            int bestScore = Integer.parseInt(tempScore);

            //condicional para verificar que el score del jugador actual sea mayor a cualquier score registrado y asi realizar la modificacion del mejor score
            if(score > bestScore){
                //objeto de la clase ContentValues para realizar la modificacion a la tabla
                ContentValues modificacion = new ContentValues();
                //colocamos los valores en el objeto
                modificacion.put("nombre", nombreJugador);
                modificacion.put("score", score);

                //insertamos en la db con la tabla, objeto y la clausula
                BD.update("puntaje", modificacion, "score=" + bestScore, null);

            }

            BD.close();

        } else {
            //objeto de la clase ContentValues para realizar la insercion a la tabla
            ContentValues insertar = new ContentValues();
            //colocamos los valores en el objeto
            insertar.put("nombre", nombreJugador);
            insertar.put("score", score);

            //insertamos en la db con la tabla y el objeto
            BD.insert("puntaje", null, insertar);

        }

    }

    //metodo para deshabilitar el boton back del sistema
    @Override
    public void onBackPressed(){

    }
}