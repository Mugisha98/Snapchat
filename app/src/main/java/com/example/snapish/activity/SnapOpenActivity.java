package com.example.snapish.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.snapish.ITaskListener;
import com.example.snapish.R;
import com.example.snapish.model.Snap;
import com.example.snapish.repository.Repository;
import com.google.firebase.database.core.Repo;

public class SnapOpenActivity extends AppCompatActivity implements ITaskListener {

    ImageView imageViewSnap;
    Snap snap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_open);
        //her skal vi finde og dl vores image fra snap_open xml
        imageViewSnap = findViewById(R.id.imageViewSnap);
        //Intent er til for at vælge hvilken destination vi vil til, ved at declare this(objekt) fra en klasse(MyProfill)
        Intent intent = getIntent();

        snap = new Snap(intent.getStringExtra("id"));
        System.out.println(snap.getId());

        Repository.repository().downloadBitmap(snap.getId(), this);
    }

    // skal "ødelægge" det billede(objekt) som vi åbner (snap)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Repository.repository().deleteImage(snap);
    }

    @Override
    public void receive(byte[] bytes) {
        imageViewSnap.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
    public void SnapEnd(View view){
        finish();
    }

}