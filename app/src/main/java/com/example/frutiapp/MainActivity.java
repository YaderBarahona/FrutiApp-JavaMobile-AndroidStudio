package com.example.frutiapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    
    private EditText editTextNombre;
    private ImageView imageViewPersonaje;
    private TextView bestScore;
    private MediaPlayer mp;

    //casteamos a int la funcion ya que retorna double
    int numeroAleatorio = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#9C27B0"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        editTextNombre = (EditText) findViewById(R.id.editTextTextPersonName);
        imageViewPersonaje = (ImageView) findViewById(R.id.imageView);
        bestScore = (TextView) findViewById(R.id.textViewBestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;

        //condicional para verificar el numero aleatoria y colocar alguna imagen
        if (numeroAleatorio == 0 || numeroAleatorio == 10) {
            //almacenamos la ruta de la imagen
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            imageViewPersonaje.setImageResource(id);
        } else if (numeroAleatorio == 1 || numeroAleatorio == 9) {
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            imageViewPersonaje.setImageResource(id);
        } else if (numeroAleatorio == 2 || numeroAleatorio == 8) {
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            imageViewPersonaje.setImageResource(id);

        } else if (numeroAleatorio == 3 || numeroAleatorio == 7) {
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            imageViewPersonaje.setImageResource(id);
        }  else if (numeroAleatorio == 4 || numeroAleatorio == 5 || numeroAleatorio == 6) {
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            imageViewPersonaje.setImageResource(id);
        }

        //objeto para la conexion a la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD", null, 1);

        //objeto para abrir la db en modo lectura y escritura
        SQLiteDatabase BD = admin.getWritableDatabase();

        //consulta hacia la db
        Cursor consulta = BD.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);

        //condicional si encuentra un puntaje o no
        if(consulta.moveToFirst()){
        //guardamos lo que tenga la columna 0 (nombre) de la tabla puntaje
        String tempNombre = consulta.getString(0);
        //guardamos lo que tenga la columna 1 (score) de la tabla puntaje
        String tempScore = consulta.getString(1);

        bestScore.setText("Record: " + tempScore + " puntos de " +tempNombre);

        //cerramos la conexion
        BD.close();
        } else {
        //cerramos la conexion
        BD.close();
        bestScore.setText("Nada");
        }

        //pista para el activity de bienvenida
        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
    }

    public void Jugar(View v1){
        String nombre = editTextNombre.getText().toString().trim();

        if(!nombre.equals("")){
        mp.stop();
        //liberamos recursos
        mp.release();

        //objeto intent para pasar de activity
        Intent in = new Intent(this, MainActivity2_NivelUno.class);
        //pasamos por parametro el nombre del jugador y la llave denominada "nombre"
        in.putExtra("nombre", nombre);
        //iniciamos el nuevo activity
        startActivity(in);
        //finalizamos el activity actual
        finish();

        } else {

            Toast.makeText(this, "Primero debes ingresar tu nombre", Toast.LENGTH_SHORT).show();
            //colocar cursor en el EditText
            editTextNombre.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            //metodo para abrir automaticamente el teclado
            imm.showSoftInput(editTextNombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    //metodo para bloquear la tecla back del sistema estando dentro del juego y asi no minimizar la app
    @Override
    public void onBackPressed(){

    }
}