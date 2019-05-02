package com.example.inventario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void AddUser(View view) {
        TextView correo = findViewById(R.id.editText3);
        TextView username = findViewById(R.id.editText4);
        TextView pass = findViewById(R.id.editText6);

        Usuario user = new Usuario(username.getText().toString(), correo.getText().toString(), pass.getText().toString());

        DatabaseReference DB_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        DB_ref.push().setValue(user);

        Toast.makeText(this,"Usuario registrado con exito", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
