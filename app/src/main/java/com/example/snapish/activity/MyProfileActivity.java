package com.example.snapish.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.snapish.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    //Viser tekst
    //private TextView usernameText, nameText, emailText, bioText;

    //redigere i tekst
    private EditText usernameEditText, nameEditText, emailEditText, bioEditText;
    private Button saveBtn;


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Profil");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //root.setValue("hey there, its a test");

        usernameEditText = findViewById(R.id.usernameText);
        //usernameEditText = findViewById(R.id.usernameText);

        nameEditText = findViewById(R.id.nameText);
        //nameEditText = findViewById(R.id.nameText);

        emailEditText = findViewById(R.id.emailText);
        //emailEditText = findViewById(R.id.emailText);

        bioEditText = findViewById(R.id.bioText);
        //bioEditText = findViewById(R.id.bioText);


    }

    public void SaveProfilPressed(View view){
        String username = usernameEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String biografi = bioEditText.getText().toString();

        Map<String, String> profilMap = new HashMap<>();

        profilMap.put("username", username);
        profilMap.put("name", name);
        profilMap.put("email", email);
        profilMap.put("biografi", biografi);

        root.setValue(profilMap);

        System.out.println("færdig");
    }

//    @Override
//    public void update(Object o) {
//    }
}