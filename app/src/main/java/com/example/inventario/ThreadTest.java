package com.example.inventario;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThreadTest {

    Bodega bodega = new Bodega();
    DatabaseReference DB_ref;
    boolean RequestNeeded;
    boolean second_lock = true;


    ThreadTest(){

    }

    public void Start(){
        new Thread(new Runnable() {
            public void run(){

                while(true){

                    if(RequestNeeded){
                        if(second_lock) {
                            second_lock = false;

                            setBodega();;

                        }
                        if(Ready()){RequestNeeded = false;}

                    }
                    else{
                        //do nothing for now
                    }

                }



            }
        }).start();
    }

    void setBodega(){


        DB_ref = FirebaseDatabase.getInstance().getReference().child("Items");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bodega vessel = new Bodega();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nombre = ds.child("nombre").getValue(String.class);
                    String desc = ds.child("descripcion").getValue(String.class);
                    int cant = ds.child("cantidad").getValue(int.class);
                    String url = ds.child("url").getValue(String.class);

                    Item item = new Item(nombre, desc, cant, url);

                    vessel.Add(item);


                }
                bodega = vessel;
                System.out.println(bodega.GetZise());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DB_ref.addListenerForSingleValueEvent(eventListener);

    }

    public void SetRequest(){
        RequestNeeded = true;
    }

    public void seeBodegaaSize(){
        System.out.println("Eureka! : "+bodega.GetZise());
    }
    public boolean Ready(){
        if(bodega.GetZise() > 0 ){return true;}
        else{return false;}
    }

    public Bodega getBodega(){
        return bodega;
    }

}
