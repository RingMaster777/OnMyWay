package com.example.tourguide.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.PostInfo;
import com.example.tourguide.helperclass.countRate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class addNewPlace extends AppCompatActivity {


    private EditText placeTittle, placeDescription, placeRoute;
    private ImageView placeImage, imageTag;
    private Button addPlace;
    private Uri postImgUri = null;
    private StorageReference storageReference;
    private String place_image_uri;

    String[] div;
    private Spinner divisionSpinner;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_place);

        //hooks
        placeTittle = findViewById(R.id.placeTittle);
        placeDescription = findViewById(R.id.placeDescription);
        placeRoute = findViewById(R.id.placeRoute);
        addPlace = findViewById(R.id.addPlace);
        placeImage = findViewById(R.id.placeImage);
        divisionSpinner = findViewById(R.id.divisionSpinner);
        imageTag = findViewById(R.id.imageTag);

        div = getResources().getStringArray(R.array.division);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSampleId, div);
        divisionSpinner.setAdapter(adapter);


        // upload image to a post
        imageTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 3);
            }
        });


        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String division = divisionSpinner.getSelectedItem().toString();
                String title = placeTittle.getText().toString();
                String desc = placeDescription.getText().toString();
                String root = placeRoute.getText().toString();
                uploadPost(title, desc, division, root);

            }
        });

    }


    // to get image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            postImgUri = data.getData();
            placeImage.setImageURI(postImgUri);

        }
    }


    private void uploadPost(String title, String desc, String div, String root) {

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && postImgUri != null && !TextUtils.isEmpty(div)) {
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference dfc = FirebaseDatabase.getInstance().getReference("places").child(div).child(title + "_" + userUid);

            // add place images
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference fileReference = FirebaseStorage.getInstance().getReference().child("posts_images/" + div + "/" + title + "_" + userUid + "/image.jpg");

            fileReference.putFile(postImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            place_image_uri = uri.toString();
                            Log.d("place_image_uri ", "onSuccess: " + place_image_uri);
                            Picasso.get().load(uri).into(placeImage);
                            Toast.makeText(addNewPlace.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            // initialize new place
                            countRate cr = new countRate("0", "0");
                            PostInfo pi = new PostInfo(title, desc, "0", cr, "pending", place_image_uri, root, title + "_" + userUid);
                            dfc.setValue(pi);
                            placeImage.setImageURI(null);
                            placeTittle.setText(" ");
                            placeDescription.setText(" ");
                            placeRoute.setText(" ");
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(addNewPlace.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            });


        }


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(new Intent(getApplicationContext(), user_dashboard.class));
        startActivity(i);
        finish();
    }
}
