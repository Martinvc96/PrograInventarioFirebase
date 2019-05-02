package com.example.inventario;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class NewItem extends AppCompatActivity {
    ConnectionManager DB_Connection;
    ThreadTest T = new ThreadTest();
    Bodega bodega;
    boolean flag = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageview;
    private ProgressBar progressbar;
    private Uri image_uri;
    private String image_url;
    private StorageReference storage;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);



        DB_Connection = new ConnectionManager();

        storage = FirebaseStorage.getInstance().getReference("uploads");

        bodega = (Bodega) getIntent().getSerializableExtra("bodega");

        DB_Connection.SetItemCount();

        imageview = findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

    }

    @Override
    public void onBackPressed() {
        if (true) {
            //disable back button
        } else {
            super.onBackPressed();
        }
    }

    public void AddItem(View view) throws InterruptedException {
        uploadImage();
    }


    public void Volver(View view){
        if(flag) {
            Bodega bodega1 = T.getBodega();
            Intent intent = new Intent(this, ItemsDisplay.class);
            intent.putExtra("bodega", bodega1);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ItemsDisplay.class);
            intent.putExtra("bodega", bodega);
            startActivity(intent);
        }

    }


    public void OpenImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image_uri = data.getData();
            Picasso.with(this).load(image_uri).into(imageview);
            //imageview.setImageURI(image_uri);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage(){
        if (image_uri != null){
            final StorageReference mystorageRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(image_uri));

            mystorageRef.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri downloadUrl = uriTask.getResult();
                    image_url = downloadUrl.toString();
                    addItem();
                    Toast.makeText(NewItem.this, "Item agregado con exito", Toast.LENGTH_LONG).show();

                }
            });
        }else{
            Toast.makeText(this, "No ha seleccionado una imagen", Toast.LENGTH_LONG).show();
        }
    }

    public void addItem(){
        TextView nombre = findViewById(R.id.Nombre);
        TextView cantidad = findViewById(R.id.Cantidad);
        TextView desc = findViewById(R.id.Descripcion);

        Item item = new Item(nombre.getText().toString(),desc.getText().toString(),Integer.parseInt(cantidad.getText().toString()), image_url);
        long count = DB_Connection.getMaxid();
        DB_Connection.AddItem(item, count);

        T.Start();
        T.SetRequest();
        flag = true;

    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
    }
}
