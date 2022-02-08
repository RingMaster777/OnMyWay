package com.example.tourguide.common;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.tourguide.helperclass.Update;
import com.example.tourguide.helperclass.UserInfo;
import com.example.tourguide.user.user_dashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText registerFullName, registerEmail, registerPassword, registerConfirmPassword;
    private Button registerUserBtn;
    private TextView redirectToLogin, logo_name;
    private ImageView logo;
    private FirebaseAuth fAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase root;
    private Animation logo_name_anim, logo_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        logo_name = findViewById(R.id.topText);
        logo = findViewById(R.id.imageView);

        registerFullName = findViewById(R.id.inputName);
        registerEmail = findViewById(R.id.inputEmail);
        registerPassword = findViewById(R.id.inputPassword);
        registerConfirmPassword = findViewById(R.id.inputConfirmPassword);
        registerUserBtn = findViewById(R.id.btnRegister);
        redirectToLogin = findViewById(R.id.goToLogin);

        //firebase
        fAuth = FirebaseAuth.getInstance();


        logo_name_anim = AnimationUtils.loadAnimation(this, R.anim.logo_and_name_anim);
        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_and_name_anim);
        logo.setAnimation(logo_anim);
        logo_name.setAnimation(logo_name_anim);

        redirectToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // extract the data from the user

                String fullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confirmPassword = registerConfirmPassword.getText().toString();

                // data validation

                if (fullName.isEmpty()) {
                    registerFullName.setError("Full name is required");
                    return;
                }
                if (email.isEmpty()) {
                    registerEmail.setError("Email is required");
                    return;
                }
                if (password.isEmpty()) {
                    registerPassword.setError("Password is required");
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    registerConfirmPassword.setError("Confirm Password is required");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    registerConfirmPassword.setError("Password do not match");
                    return;
                }
                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        root = FirebaseDatabase.getInstance();
                        databaseReference = root.getReference("users");
                        UserInfo userInfo = new UserInfo(fullName, email, password, "user");

                        Update update = new Update(" ", " ", " ", " ");
                        databaseReference.child(firebaseUser.getUid()).setValue(userInfo);
                        databaseReference.child(firebaseUser.getUid()).child("details").setValue(update);


                        // send to Next page
                        startActivity((new Intent(getApplicationContext(), user_dashboard.class)));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });
    }
}