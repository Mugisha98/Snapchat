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

public class SnapOpenActivity extends AppCompatActivity implements ITaskListener {

    ImageView imageViewSnap;
    Snap snap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_open);
        imageViewSnap = findViewById(R.id.imageViewSnap);

        // Intent is for choosing a class destination and to start an activity
        Intent intent = getIntent();

        snap = new Snap(intent.getStringExtra("id"));
        System.out.println(snap.getId());

        Repository.repository().downloadBitmap(snap.getId(), this);
    }

    // Image is deleted after opening
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