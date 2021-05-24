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
    // Define Attributes
    private EditText usernameEditText, nameEditText, emailEditText, bioEditText;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Profile");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //declaring the attributes

        usernameEditText = findViewById(R.id.usernameText);
        nameEditText = findViewById(R.id.nameText);
        emailEditText = findViewById(R.id.emailText);
        bioEditText = findViewById(R.id.bioText);
    }

        //Saving the profile details in a real time database
    public void SaveProfilePressed(View view){
        String username = usernameEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String bio = bioEditText.getText().toString();

        Map<String, String> profileMap = new HashMap<>();

        profileMap.put("username", username);
        profileMap.put("name", name);
        profileMap.put("email", email);
        profileMap.put("bio", bio);

        root.setValue(profileMap);

        System.out.println("done");
    }

}