package com.example.inventario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    Bodega bodega;
    ArrayList<Usuario> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bodega = new Bodega();

        SetBodega();
        SetUsers();


    }

    public void onBackPressed() {
        if (true) {
            //disable back button
        }
    }

    public void verBodega(View view) {
        TextView email = findViewById(R.id.editText);
        TextView pass = findViewById(R.id.editText2);

        String correo = email.getText().toString();
        String contra = pass.getText().toString();

        boolean existe = false;

        for(Usuario user : users){
            if(user.getEmail().equals(correo)){
                if(user.getPassword().equals(contra)){
                    goToItemsViiew();
                }else{
                    Toast.makeText(this, "Contrasena incorrecta", Toast.LENGTH_LONG).show();
                }
                existe = true;
                break;
            }
        }

        if(!existe){
            Toast.makeText(this, "El usuario ingresado no existe", Toast.LENGTH_LONG).show();
        }





    }

    public void SetBodega(){
        DatabaseReference DB_ref = FirebaseDatabase.getInstance().getReference().child("Items");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nombre = ds.child("nombre").getValue(String.class);
                    String desc = ds.child("descripcion").getValue(String.class);
                    int cant = ds.child("cantidad").getValue(int.class);
                    String url = ds.child("url").getValue(String.class);

                    Item item = new Item(nombre, desc, cant, url);

                    bodega.Add(item);


                }
                System.out.println("Exito al llenar bodega");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        DB_ref.addListenerForSingleValueEvent(eventListener);

    }

    public void Registro(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void SetUsers(){
        DatabaseReference DB_ref = FirebaseDatabase.getInstance().getReference().child("Users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String correo = ds.child("email").getValue(String.class);
                    String username = ds.child("userName").getValue(String.class);
                    String pass = ds.child("password").getValue(String.class);

                    Usuario user = new Usuario(username, correo, pass);

                    users.add(user);
                }
                System.out.println("Exito al cargar usuarios");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        DB_ref.addListenerForSingleValueEvent(eventListener);

    }

    private void goToItemsViiew(){
        Intent intent = new Intent(this, ItemsDisplay.class);
        intent.putExtra("bodega", bodega);
        startActivity(intent);
    }
}
