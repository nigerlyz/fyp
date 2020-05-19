package com.example.fyp;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button loginBtn;
    TextView regPageBtn, forgetPw;
    ProgressBar lProgressBar;
    FirebaseAuth lAuth;
    EditText loginName, lPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginName = findViewById(R.id.lUsername);
        lPassword = findViewById(R.id.lPassword);
        loginBtn = findViewById(R.id.loginBtn);
        regPageBtn = findViewById(R.id.linkToRegister);
        lAuth = FirebaseAuth.getInstance();
        lProgressBar = findViewById(R.id.loginProgressBar);

        //To check if user is already logged in or has an account created
        if(lAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();
        }
        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = loginName.getText().toString().trim();
                String password = lPassword.getText().toString().trim();

                //validates the textfields of name password
                if(TextUtils.isEmpty(loginName.getText().toString().trim())){
                    loginName.setError("Please enter name.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    lPassword.setError("Please enter password.");
                    return;
                }
                if(password.length()<8){
                    lPassword.setError("Please enter at least 8 characters.");
                    return;
                }

                //displays a bar to show user credentials are being processed in case they think lag
                lProgressBar.setVisibility(View.VISIBLE);

                //login user using provided email and password
                lAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //registration success will direct to homepage
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        }
                        //registration failure shows message still on same page
                        else {
                            Toast.makeText(Login.this, "Error." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            lProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
                //user clicks on login word to go login page
                regPageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                });

            }
        });

        forgetPw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final EditText eT = new EditText(v.getContext());
                AlertDialog.Builder pwReset = new AlertDialog.Builder(v.getContext());
                pwReset.setMessage("Enter Email to reset your password.");
                pwReset.setView(eT);
                pwReset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = eT.getText().toString();
                        lAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link has been sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                pwReset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close message
                    }
                });
                pwReset.create().show();
            }
        });
    }
}
