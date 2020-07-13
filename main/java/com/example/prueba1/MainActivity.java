package com.example.prueba1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prueba1.Model.Persona;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Persona> listPerson = new ArrayList<Persona>();
    ArrayAdapter<Persona> arrayAdapterPersona;

    EditText nombrePersona, apellidoPersona, correoPersona, passwordPersona;
    ListView listaPersonas;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Persona pSeleccionada;


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
        listaDatos();

        listaPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pSeleccionada = (Persona) parent.getItemAtPosition(position);
                nombrePersona.setText(pSeleccionada.getNombre());
                apellidoPersona.setText(pSeleccionada.getApellido());
                correoPersona.setText(pSeleccionada.getCorreo());
                passwordPersona.setText(pSeleccionada.getPassword());

            }
        });


    }

    private void listaDatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                listPerson.clear();
                for(DataSnapshot objSnapshot : datasnapshot.getChildren()){
                    Persona p =objSnapshot.getValue(Persona.class);
                    listPerson.add(p);

                    arrayAdapterPersona= new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);
                    listaPersonas.setAdapter(arrayAdapterPersona);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                Persona p = new Persona();
                p.setUid(pSeleccionada.getUid());
                p.setNombre(nombrePersona.getText().toString().trim());
                p.setApellido(apellidoPersona.getText().toString().trim());
                p.setCorreo(correoPersona.getText().toString().trim());
                p.setPassword(passwordPersona.getText().toString().trim());
                databaseReference.child("Persona").child(p.getUid()).setValue(p);
                Toast.makeText(this,"Guardar",Toast.LENGTH_LONG).show();
                limpiar();
                break;
            }
            case R.id.icon_delete:{
                Persona p = new Persona();
                p.setUid(pSeleccionada.getUid());
                databaseReference.child("Persona").child(p.getUid()).removeValue();
                limpiar();

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