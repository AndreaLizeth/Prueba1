package com.example.prueba1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.prueba1.Model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText nombrePersona, apellidoPersona, correoPersona, passwordPersona;
    ListView listaPersonas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombrePersona= findViewById(R.id.nombrePersona);
        apellidoPersona= findViewById(R.id.apellidoPersona);
        correoPersona= findViewById(R.id.correoPersona);
        passwordPersona= findViewById(R.id.passwordPersona);

        listaPersonas=findViewById(R.id.listaPersonas);

        inicializarFirebase();
    }

   private void inicializarFirebase(){
       FirebaseApp.initializeApp(this);
       firebaseDatabase=FirebaseDatabase.getInstance();
       databaseReference= firebaseDatabase.getReference();
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String nombre= nombrePersona.getText().toString();
        String apellido= apellidoPersona.getText().toString();
        String correo= correoPersona.getText().toString();
        String password= passwordPersona.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if(nombre.equals("")||apellido.equals("")||correo.equals("")||password.equals("")){
                    validacion();
                }else
                {
                    Persona p= new Persona();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(apellido);
                    p.setCorreo(correo);
                    p.setPassword(password);
                    databaseReference.child("Persona").child(p.getUid()).setValue(p);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                    limpiar();
                }
                break;
            }
            case R.id.icon_save:{
                Toast.makeText(this,"Guardar",Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete:{
                Toast.makeText(this, "Borrar", Toast.LENGTH_LONG).show();
                break;
            }
            default:break;
        }
        return true;
    }

    private void limpiar(){
        nombrePersona.setText("");
        apellidoPersona.setText("");
        correoPersona.setText("");
        passwordPersona.setText("");
    }

    private void validacion(){
        String nombre= nombrePersona.getText().toString();
        String apellido= apellidoPersona.getText().toString();
        String correo= correoPersona.getText().toString();
        String password= passwordPersona.getText().toString();

        if(nombre.equals("")){
            nombrePersona.setError("Requerido");
        }
        else if(apellido.equals("")){
            apellidoPersona.setError("Requerido");
        }
        else if(correo.equals("")){
            correoPersona.setError("Requerido");
        }
        else if(password.equals("")){
            passwordPersona.setError("Requerido");
        }
    }
}