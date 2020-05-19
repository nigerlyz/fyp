package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ViewProfile extends AppCompatActivity {

    EditText name, email, points;
    FirebaseAuth vPAuth;
    FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        name = findViewById(R.id.vFName);
        email = findViewById(R.id.vEmail);
        points = findViewById(R.id.vPoints);

        vPAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // using user id to retrieve details of a user and display
        userID = vPAuth.getCurrentUser().getUid();
        //retrieve user's profile details from database using their user id
        final DocumentReference docRef = db.collection("users").document(userID);
        docRef.addSnapshotListener(this, new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("FullName"));
                email.setText(documentSnapshot.getString("Email"));
                points.setText(documentSnapshot.getString("Points"));

            }
        });
    }
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
