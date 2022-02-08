package com.example.tourguide.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.PostInfo;
import com.example.tourguide.helperclass.Update;
import com.example.tourguide.helperclass.UserInfo;
import com.example.tourguide.helperclass.countRate;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_Profile extends AppCompatActivity {

    public static StorageReference propicref;
    private TextView username, useremail, userpassword, userJob, userLoaction, userbio, userContact,popupTitle, popupDescription;
    private FloatingActionButton post, chat;
    private Dialog popAddpost;
    private ImageView popupUserImg, popupPostImg, popupAddBtn;
    private ProgressBar popupProgressbar;
    private CircleImageView setupImg;
    private Button editBtn;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference updateValue, databaseReference;
    private StorageReference storageReference;

    private Uri postImgUri = null;
    static String usrName;

    private String[] div;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);


        //hooks

        Intent intent = getIntent();

        username = findViewById(R.id.proName);
        useremail = findViewById(R.id.proEmail);
        userpassword = findViewById(R.id.proPass);
        userLoaction = findViewById(R.id.proCity);
        userContact = findViewById(R.id.proContact);
        userJob = findViewById(R.id.proJob);
        userbio = findViewById(R.id.aboutme);

        setupImg = findViewById(R.id.profile_image);
        editBtn = findViewById(R.id.editProfile);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        showInfo();
        // upload profile picture


        //popup
        //popup();

        /*post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddpost.show();
            }
        });*/

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), profile_update.class));
                finish();
            }
        });


    }


    // to get image

    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            postImgUri = data.getData();
            popupPostImg.setImageURI(postImgUri);

        }
    }


     */


    // upload in to database


    public void showInfo() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        updateValue = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("details");

        StorageReference profilePicRef = storageReference.child("userPicture/" + firebaseUser.getUid() + "/profile.jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(setupImg);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo ui = snapshot.getValue(UserInfo.class);
                if (ui != null ) {
                    usrName = ui.getUserName();
                    username.setText(ui.getUserName());
                    useremail.setText(ui.getUserEmail());
                    userpassword.setText(ui.getUserPassword());

                } else {
                    Toast.makeText(user_Profile.this, "No valid user", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        updateValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                Update up = snapshot.getValue(Update.class);
                assert up != null;
                userContact.setText(up.getContact());
                userJob.setText(up.getJob());
                userLoaction.setText(up.getLocation());
                userbio.setText(up.getBio());

            }
            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

    }

/*
    // getting uploading interface
    private void popup() {
        popAddpost = new Dialog(this);
        popAddpost.setContentView(R.layout.popup_add_post);
        popAddpost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddpost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddpost.getWindow().getAttributes().gravity = Gravity.BOTTOM;


        // popup widget

        popupUserImg = popAddpost.findViewById(R.id.poppup_user_img);
        popupPostImg = popAddpost.findViewById(R.id.popup_img);
        popupTitle = popAddpost.findViewById(R.id.popup_title);
        popupDescription = popAddpost.findViewById(R.id.popup_description);
        popupAddBtn = popAddpost.findViewById(R.id.popup_add);
        popupProgressbar = popAddpost.findViewById(R.id.popup_progressBar);
        sp = popAddpost.findViewById(R.id.spinner_items);
        div = getResources().getStringArray(R.array.division);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSampleId, div);
        sp.setAdapter(adapter);

        // converting into String


        // upload image to a post
        popupPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 3);

            }
        });

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = sp.getSelectedItem().toString();
                String title = popupTitle.getText().toString();
                String desc = popupDescription.getText().toString();
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupProgressbar.setVisibility(View.VISIBLE);
                uploadPost(title, desc, value);
            }
        });

    }


    //



    //upload a post

    private void uploadPost(String title, String desc, String div) {

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && postImgUri != null && !TextUtils.isEmpty(div)) {
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

            DatabaseReference df = mDatabase.getReference("places");

            DatabaseReference dfc = df.child(div).child(title);
            countRate cr = new countRate("0","0");
            //PostInfo pi = new PostInfo(title, desc, "0",cr,"pending",);
            //dfc.setValue(pi);
            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();

            // place's images add

            StorageReference fileref = storageReference.child("posts_images/" + div + "/" + title + "/image.jpg");

            fileref.putFile(postImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            popupProgressbar.setVisibility(View.INVISIBLE);
                            popupAddBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(user_Profile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            popAddpost.dismiss();
                            popupPostImg.setImageURI(null);
                            popupTitle.setText("");
                            popupDescription.setText("");
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(user_Profile.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }*/


    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), user_dashboard.class));
        finish();
    }
}
