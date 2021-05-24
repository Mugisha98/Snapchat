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
    // skal have billeder fra db ind i denne liste
    List<Snap> items = new ArrayList<>();

    ListView listView;
    MyAdapter myAdapter;
    Button profil;
    //Initialize variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vi skal lave en forbindelse til listView her.
        //R kompiler hele tiden, så f.eks hvergang der bliver lavet en knap rekompiler den klassen.
        //Hver gang vi laver noget nyt, bliver et nyt nr(id) klar
        listView = findViewById(R.id.listView1);
        // vi impotere adapteren og vælger listen som parameter og "this" er context vi vælger som er den mest alm.
        myAdapter = new MyAdapter(items, this);

        profil = findViewById(R.id.profil);
        profil.setOnClickListener(this);

        listView.setAdapter(myAdapter);
        Repository.repository().setup(this, items);
        setupListView();


    }

    //_____________________________ METODER

    @Override
    //Denne knap skal fører dig til din profil.
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profil:
                //Intent er til for at vælge hvilken destination vi vil til, ved at declare this(objekt) fra en klasse(MyProfill)
                //når vi kalder på startActivity, vil vi starte en ny aktivitet med den intent som vi lavede ovenover
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
        }

    }

//    //Denne knap skal fører dig til din profil.
//    public void MyProfilPressed(View view){
//        System.out.println("MyProfil Is Pressed");
//        //Intent er til for at vælge hvilken destination vi vil til, ved at declare this(objekt) fra en klasse(MyProfill)
//        Intent intent = new Intent(this, MyProfil.class);
//        //når vi kalder på startActivity, vil vi starte en ny aktivitet med den intent som vi lavede ovenover
//        startActivity(intent);
//    }

    //Denne knap skal fører dig til din tagbillede.
    public void TakePicturePressed(View view){
        // vi laver her en Intent med en action, så vi kan åbne cameraet og tage et billede som skal retunere det.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent, 1);
        } catch (ActivityNotFoundException e){
            System.out.println("error: du kan ikke tage billede pt");
        }
    }

//____________________________________________________________________



    private void setupListView(){
        //når man klikker vil vi gerne have den tager et "item" med et id fra listviewet fra db
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Her opretter vi et opbjekt med postisiton fra items
                Snap tempSnap = items.get(position);

                // her bliver der oprettet et Intet, hvor vi skal bruge
                Intent snapIntent = new Intent(MainActivity.this, SnapOpenActivity.class);

                //extra tildeler udvidet data til intent og navn giver "id" som vi benytter i snapopen klassen.
                snapIntent.putExtra("id", tempSnap.getId());

                startActivity(snapIntent);


            }
        });
    }


    //________________ Ting til at capture picture og indsætte text
    // Skal måske flyttes til repo


    @Override
    // denne skal tjekke om der er en requestCode for en aktivitet som er startet
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //hvis requestCode er den samme, så kan vi kalde insertTest metoden
        if (requestCode == 1) {
            insertText((Bitmap)data.getExtras().get("data"));
        }
    }

    // denne metode er til for at lave tekst på et billede
    public void insertText(Bitmap image){
        //Laver pop op
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Indsæt text");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((dialog, which) -> insertTextToBitmap(image, input.getText().toString())));
        builder.setNegativeButton("Annuller", (dialog, which) -> dialog.cancel());

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
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// new antialised Paint
        paint.setColor(Color.rgb(161, 161, 161));
        paint.setTextSize((int) (20)); // text size in pixels
        paint.setShadowLayer(1f, 0f, 1f, Color.RED); // text shadow
        canvas.drawText(gText, 10, 100, paint);
        Repository.repository().uploadBitmap(image, gText);
    }

    //_________________________________________________

    @Override
    public void update(Object o) {
        myAdapter.notifyDataSetChanged();
    }
}
