package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    String userID;
    EditText rFullName, rPassword, rEmail;
    Button rRegistrationBtn;
    TextView loginPageText;
    ProgressBar rProgressBar;
    FirebaseAuth rAuth;
    int points;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        points = 10;
        rFullName = findViewById(R.id.fName);
        rPassword = findViewById(R.id.rPassword);
        rEmail = findViewById(R.id.email);
        rRegistrationBtn = findViewById(R.id.registrationBtn);
        loginPageText = findViewById(R.id.linkToLogin);
        rAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        rProgressBar = findViewById(R.id.rProgressBar);

        //To check if user is already logged in or has an account created
        if(rAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();
        }

        rRegistrationBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String rFName = rFullName.getText().toString();
                final String password = rPassword.getText().toString().trim();
                final String email = rEmail.getText().toString().trim();

                //validates the textfields of name password and email
                if(TextUtils.isEmpty(rFullName.getText().toString().trim())){
                    rFullName.setError("Please enter name.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Please enter password.");
                    return;
                }
                if(password.length()<8){
                    rPassword.setError("Please enter at least 8 characters.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Please enter email.");
                    return;
                }
                //displays a bar to show user credentials are being processed in case they think lag
                rProgressBar.setVisibility(View.VISIBLE);

                //registers user using provided email and password
                rAuth.createUserWithEmailAndPassword(password, email).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //registration success
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "User Account Created Successfully.", Toast.LENGTH_SHORT).show();
                           // creates a user object and store to firestore db
                            userID = rAuth.getCurrentUser().getUid();
                            DocumentReference docRef = db.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("FullName", rFName);
                            user.put("Email", email);
                            user.put("Password", password);
                            user.put("Points", points);
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        }
                        //registration failure will display error to user
                        else {
                            Toast.makeText(Registration.this, "Error." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            rProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
                //user clicks on login word to go login page
                loginPageText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                });


            }
        });


    }
}
