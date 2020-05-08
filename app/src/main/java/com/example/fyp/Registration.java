package com.example.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    EditText rFullName, rPassword, rEmail;
    Button rRegistrationBtn;
    TextView rLoginBtn;
    ProgressBar rProgressBar;
    FirebaseAuth rAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        rFullName = findViewById(R.id.fName);
        rPassword = findViewById(R.id.rPassword);
        rEmail = findViewById(R.id.email);
        rRegistrationBtn = findViewById(R.id.registrationBtn);
        rLoginBtn = findViewById(R.id.linkToLogin);

        rAuth = FirebaseAuth.getInstance();
        rProgressBar = findViewById(R.id.rProgressBar);

        //To check if user is already logged in or has an account created
        if(rAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();
        }

        rRegistrationBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String password = rPassword.getText().toString().trim();
                String email = rEmail.getText().toString().trim();

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
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        }
                        //registration failure
                        else {
                            Toast.makeText(Registration.this, "Error." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}
