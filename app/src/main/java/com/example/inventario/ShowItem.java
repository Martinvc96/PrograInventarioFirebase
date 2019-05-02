package com.example.inventario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        Item item = (Item) getIntent().getSerializableExtra("item");

        ImageView image_view = findViewById(R.id.imageView2);

        TextView txt_nombre = findViewById(R.id.text_nombre);
        TextView txt_cant = findViewById(R.id.text_cantidad);
        TextView txt_desc = findViewById(R.id.text_desc);

        txt_nombre.setText(item.getNombre());
        txt_cant.setText(Integer.toString(item.getCantidad()) + "  unidades");
        txt_desc.setText(item.getDescripcion());
        Picasso.with(this).load(item.getURL()).fit().into(image_view);


    }
}
