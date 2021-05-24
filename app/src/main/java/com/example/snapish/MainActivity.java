package com.example.snapish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.snapish.activity.MyProfileActivity;
import com.example.snapish.activity.SnapOpenActivity;
import com.example.snapish.adapter.MyAdapter;
import com.example.snapish.model.Snap;
import com.example.snapish.repository.Repository;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements IUpdateble,View.OnClickListener{
    //Storing snaps inside the Array list
    List<Snap> items = new ArrayList<>();

    //Initializing variables
    ListView listView;
    MyAdapter myAdapter;
    Button profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView1);
        myAdapter = new MyAdapter(items, this);

        profileBtn = findViewById(R.id.profil);
        profileBtn.setOnClickListener(this);

        listView.setAdapter(myAdapter);
        Repository.repository().setup(this, items);
        setupListView();
    }
    // onclick = profile destination
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profil:
                //Starting an activity and defining the destination by using intent
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
        }
    }
    //open camera
    public void TakePicturePressed(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent, 1);
        } catch (ActivityNotFoundException e){
            System.out.println("error: unable to open camera");
        }
    }
    // when listView item is clicked, id is used to find the item in database
    private void setupListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //creating an object with position from items
                Snap tempSnap = items.get(position);
                //creating an intent and defining destination
                Intent snapIntent = new Intent(MainActivity.this, SnapOpenActivity.class);
                snapIntent.putExtra("id", tempSnap.getId());
                startActivity(snapIntent);
            }
        });
    }

    //Checking if insertText is possible via requestCode
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //hvis requestCode er den samme, så kan vi kalde insertTest metoden
        if (requestCode == 1) {
            insertText((Bitmap)data.getExtras().get("data"));
        }
    }

    // Inserting text on image
    public void insertText(Bitmap image){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insert text");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((dialog, which) -> insertTextToBitmap(image, input.getText().toString())));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void insertTextToBitmap(Bitmap image, String gText) {
        Bitmap.Config bitmapConfig = image.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        image = image.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// new paint
        paint.setColor(Color.BLACK);
        paint.setTextSize((int) (16)); // text size in pixels
        canvas.drawText(gText, 10, 100, paint);
        Repository.repository().uploadBitmap(image, gText);
    }

    @Override
    public void update(Object o) {
        myAdapter.notifyDataSetChanged();
    }
}