package com.example.inventario;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ConnectionManager {

    DatabaseReference DB_ref;
    Bodega bodega;
    private long maxid = 0;

    public ConnectionManager(){}

    public void AddItem(Item item, long count){
        DB_ref = FirebaseDatabase.getInstance().getReference().child("Items");
        DB_ref.child(String.valueOf(count)).setValue(item);
        //DB_ref.push().setValue(item);
    }

    public void AddUser(Usuario user){
        DB_ref = FirebaseDatabase.getInstance().getReference().child("Users");
        DB_ref.push().setValue(user);
    }

    public Bodega GetBodega(){
        return this.bodega;
    }

    public void SetBodega(){
        bodega = new Bodega();


        DB_ref = FirebaseDatabase.getInstance().getReference().child("Items");
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
                System.out.println(bodega.GetZise());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DB_ref.addListenerForSingleValueEvent(eventListener);




    }

    public void SetItemCount(){
        DB_ref = FirebaseDatabase.getInstance().getReference().child("Items");
        DB_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    maxid = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public long getMaxid(){
        return maxid;
    }

}
