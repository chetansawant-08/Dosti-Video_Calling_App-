package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;

    private EditText emailBox, passwordBox, nameBox;
    private Button loginBtn, signupBtn;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nameBox = findViewById(R.id.nameBox);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.loginButton);
        signupBtn = findViewById(R.id.createButton);

        signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email, pass, name;
                email = emailBox.getText().toString();
                pass = passwordBox.getText().toString();
                name = nameBox.getText().toString();

                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPass(pass);

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    database.collection("Users")
                                            .document(mAuth.getCurrentUser().getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                                    finish(); // finish current activity to prevent user from returning to it
                                                }
                                            });

                                    Toast.makeText(SignupActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish(); // finish current activity to prevent user from returning to it
            }
        });
    }
}
