package com.example.daale.tachosservapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnIniciado,btnCerrado; //Creas los objetos de los botones
    TextView tvEmail,tvPassword;   //Creas los objetos de los textfield
    ProgressBar pbProgreso;        //Creas el obejto de la progress bar
    FirebaseAuth mAuth;            //Creas el objeto para el servicio autentication de firebase
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializas los objetos con los componentes del xml de diseño ps prro
        btnIniciado = findViewById(R.id.btningresar);
        btnCerrado = findViewById(R.id.btncerrar);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);

        pbProgreso = (ProgressBar) findViewById(R.id.pbProgress);
        pbProgreso.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        //se inicializa el listener este es un escuchador de estado
        //implementa un metodo que se va a ejecutar cada vez que sucedan cambios, como cuando se logea y cuando cierra la sesion
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    //no esta logeado
                    btnCerrado.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "No esta logueado", Toast.LENGTH_LONG).show();
                }else{
                    //esta logeado
                    btnCerrado.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Esta Logueado", Toast.LENGTH_LONG).show();
                }
            }
        };


        btnIniciado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"Ingresando", Toast.LENGTH_SHORT).show();
                // Iniciando sesión llamando a Ingresar();
                Ingresar();


            }
        });
        btnCerrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Cerrando Sesión", Toast.LENGTH_SHORT).show();
                //Cerrando sesión
                mAuth.signOut();

            }
        });
    }
    private void Ingresar() {
        //Tomamos los valores de los textfield y los pasamos a variables
        String email = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();
        //Verificamos si estan llenos los campos
        if(!email.isEmpty() && !password.isEmpty()){
            //Activamos la barra o circulo de progreso
            pbProgreso.setVisibility(View.VISIBLE);
            //Entramos al servicio de firebase autentication por medio del objeto mAuth y le pedimos logearnos por email y contraseña
            //le pasamos las variables email y password con los datos
            //se añade un escuchador el listenner para cuando esta tarea finalize
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                //Se adiere al listenner un Task o un objeto tarea y se crea el metodo onComplete
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //que va a verificar si la tarea se completo o no (es donde se verifican las credenciales )
                    if(task.isSuccessful()){
                        //Toast.makeText(getApplicationContext(), "Correcto", Toast.LENGTH_LONG).show();
                        Intent ir_actMenuUsuarios = new Intent(MainActivity.this, menu1.class); //Me manda al Activity_menu_usuarios
                        startActivity(ir_actMenuUsuarios); //Inicio el Intent
                    }else {
                        Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_LONG).show();
                    }
                    pbProgreso.setVisibility(View.INVISIBLE);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Campos vacios :/", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null){
            mAuth.removeAuthStateListener(listener);
        }
    }
}
