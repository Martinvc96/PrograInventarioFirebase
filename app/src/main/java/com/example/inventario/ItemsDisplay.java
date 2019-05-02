package com.example.inventario;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemsDisplay extends AppCompatActivity {

    Bodega bodega;
    boolean longpress = true;
    SimpleAdapter adapter;
    List<HashMap<String, String>> listItems;
    ListView resultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_display);

        bodega = (Bodega) getIntent().getSerializableExtra("bodega");

        resultsListView = findViewById(R.id.items_listview);

        listItems = new ArrayList<>();

        for (int i = 0; i < bodega.GetZise(); i++){
            System.out.println(bodega.GetItem(i).getNombre());
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("First Line", bodega.GetItem(i).getNombre());
            resultsMap.put("Second Line", "Cantidad:  " + bodega.GetItem(i).getCantidad() + " unidades");
            listItems.add(resultsMap);

        }




        adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});

        resultsListView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goAddItem();
            }
        });


        resultsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {

                longpress = false;
                DeletePopUp(arg2);
                return false;
            }
        });


        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if(longpress){
                    goShowItem(arg2);
                }
                longpress = true;


            }


        });

    }

    @Override
    public void onBackPressed() {
        if (true) {
            //disable back button
        } else {
            super.onBackPressed();
        }
    }

    void goAddItem(){
        Intent intent = new Intent(this, NewItem.class);
        intent.putExtra("bodega", bodega);
        startActivity(intent);
    }


    public void goShowItem(int index){
        Item item = bodega.GetItem(index);

        Intent intent = new Intent(this, ShowItem.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    public void updateListview(){
        adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});

        resultsListView.setAdapter(adapter);
    }

    private void DeletePopUp(final int index)
    {

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Eliminar")
                .setMessage("Seguro que desea elminar este item?")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DatabaseReference DB_ref = FirebaseDatabase.getInstance().getReference("Items");
                        DB_ref.child(Integer.toString(index)).removeValue();
                        listItems.remove(index);
                        bodega.Remove(index);
                        for(int i = index+1; i <= bodega.GetZise()+1; i++){
                            DB_ref.child(Integer.toString(i)).removeValue();
                        }
                        for(int i = index; i < bodega.GetZise(); i++){
                            DB_ref.child(String.valueOf(i)).setValue(bodega.GetItem(i));
                        }

                        updateListview();
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();
        myQuittingDialogBox.show();

    }

    public void LogOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
