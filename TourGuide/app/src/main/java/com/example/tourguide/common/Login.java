package com.example.tourguide.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourguide.R;
import com.example.tourguide.user.user_dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText userEmail, password;
    private TextView createAccountBtn, forgotPass, logo_name;
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder reset_alert;
    private LayoutInflater inflater;
    private ImageView logo;
    private Animation logo_name_anim, logo_anim;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        logo = findViewById(R.id.imageView);
        logo_name = findViewById(R.id.topText);
        createAccountBtn = findViewById(R.id.goToRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();


        logo_name_anim = AnimationUtils.loadAnimation(this, R.anim.logo_and_name_anim);
        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_and_name_anim);
        logo.setAnimation(logo_anim);
        logo_name.setAnimation(logo_name_anim);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();

            }
        });

        userEmail = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        login = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forgotPassword);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertdialog
                View view = inflater.inflate(R.layout.reset_pop, null);

                reset_alert.setTitle("Reset Forgot Password").setMessage("Enter Your Email to get Password Reset link")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Validate the email address
                                EditText email = view.findViewById(R.id.reset_email_pop);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Required Email");
                                    return;
                                }
                                // send reset link
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Login.this, "Reset Email sent", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                                    }
                                });

                                // send the reset link

                            }
                        }).setNegativeButton("cancel", null).setView(view).create().show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // extreact data
                if (userEmail.getText().toString().isEmpty()) {
                    userEmail.setError("Email is required");
                    return;
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Password is required");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Log in successful
                        Intent intent = new Intent(getApplicationContext(), user_dashboard.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    // if user already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), user_dashboard.class));
            finish();
        }
    }


}