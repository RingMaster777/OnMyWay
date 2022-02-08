package com.example.tourguide.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.Update;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class profile_update extends AppCompatActivity {

    private EditText ocupation, location, contact, bio;
    private Button save;
    private ImageView profileImage;


    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);


        ocupation = findViewById(R.id.editjob);
        location = findViewById(R.id.editcity);
        contact = findViewById(R.id.editPhone);
        bio = findViewById(R.id.bio);
        save = findViewById(R.id.editsave);
        profileImage = findViewById(R.id.proImage);
        storageReference = FirebaseStorage.getInstance().getReference();



        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(profile_update.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(profile_update.this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(profile_update.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                    } else {
                        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openGallery, 1);
                        Toast.makeText(profile_update.this, "You have already permission", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bio!=null || location!=null || ocupation!=null || contact!=null ) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    Update up = new Update(bio.getText().toString(), location.getText().toString(), ocupation.getText().toString(), contact.getText().toString());
                    databaseReference.child(firebaseUser.getUid()).child("details").setValue(up);
                    Toast.makeText(profile_update.this, "Saved", Toast.LENGTH_SHORT).show();
                    bio.setText(" ");
                    location.setText(" ");
                    contact.setText(" ");
                    ocupation.setText(" ");
                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadImageToFirebaseStorage(data.getData());

        }
    }


    // upload in to database
    private void uploadImageToFirebaseStorage(Uri imageUri) {


        StorageReference fileReference = storageReference.child("userPicture/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
                Toast.makeText(profile_update.this, "Uploaded", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(profile_update.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), user_Profile.class));
        finish();
    }

}