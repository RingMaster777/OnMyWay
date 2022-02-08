package com.example.tourguide.user;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.cmnHelper;
import com.example.tourguide.helperclass.countRate;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class place_desc extends AppCompatActivity {


    //StorageReference propicref;

    private ImageView post_detail_currentuser_img, second_arrow_up, placeDecImageview;
    private TextView second_title, second_subtitle, second_rating_number, second_rated_people, more_details;
    private RatingBar second_ratingbar, revRating;
    private Animation from_left, from_right, from_bottom;
    private String title, division, desc, rating, userName, current_user_image_uri, commentDate, placeImageUri, reference, root, rated_ppl;
    private RecyclerView cmntRec;
    private CommentAdapter cmntAdapter;
    private EditText post_detail_comment;
    private Button post_detail_add_comment_btn;
    private float total;
    private int ppl;
    int check = 0;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_desc);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Description Portal");
        pd.setMessage("Loading...!");


        //getting value from MyAdapter class

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("place");
        division = bundle.getString("division");
        desc = bundle.getString("desc");
        rating = bundle.getString("rating");
        placeImageUri = bundle.getString("placeImageUri");
        reference = bundle.getString("reference");
        root = bundle.getString("root");
        rated_ppl = bundle.getString("rated_ppl");


        //second_back_arrow = findViewById(R.id.second_back_arrow);
        second_arrow_up = findViewById(R.id.second_arrow_up);
        second_title = findViewById(R.id.second_title);
        second_subtitle = findViewById(R.id.second_subtitle);
        second_rating_number = findViewById(R.id.second_rating_number);
        second_rated_people = findViewById(R.id.second_rated_people);
        more_details = findViewById(R.id.more_details);
        second_ratingbar = findViewById(R.id.second_ratingbar);
        cmntRec = findViewById(R.id.recCmnt);
        post_detail_comment = findViewById(R.id.post_detail_comment);
        post_detail_add_comment_btn = findViewById(R.id.post_detail_add_comment_btn);
        userName = user_dashboard.staticUserName;
        post_detail_currentuser_img = findViewById(R.id.post_detail_currentuser_img);
        revRating = findViewById(R.id.revRating);
        placeDecImageview = findViewById(R.id.placeDecImageview);


        // Hide status bar and navigation bar at the bottom
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // Load Animations
        from_left = AnimationUtils.loadAnimation(this, R.anim.anim_from_left);
        from_right = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);

        //Set Animations
        //second_back_arrow.setAnimation(from_left);
        second_title.setAnimation(from_right);
        second_subtitle.setAnimation(from_right);
        second_ratingbar.setAnimation(from_left);
        second_rating_number.setAnimation(from_right);
        second_rated_people.setAnimation(from_right);
        second_arrow_up.setAnimation(from_bottom);
        more_details.setAnimation(from_bottom);
        post_detail_currentuser_img.setAnimation(from_bottom);
        post_detail_comment.setAnimation(from_bottom);
        post_detail_add_comment_btn.setAnimation(from_bottom);
        cmntRec.setAnimation(from_bottom);

        /// Set Place Information
        second_title.setText(title);
        second_subtitle.setText(desc);
        second_ratingbar.setRating(Float.parseFloat(rating));
        second_rating_number.setText(rating);
        second_rated_people.setText("(" + rated_ppl + ")");


        // Going to pop_up screen;
        more_details.setOnClickListener((View) -> {
            Intent intent = new Intent(place_desc.this, pop_up_details.class);

            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(second_arrow_up, "background_image_transition");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(place_desc.this, pairs);
            intent.putExtra("desc", desc);
            intent.putExtra("title", title);
            intent.putExtra("division", division);
            intent.putExtra("rating", rating);
            intent.putExtra("rated_people", rated_ppl);
            intent.putExtra("placeImageUri", placeImageUri);
            intent.putExtra("reference", reference);
            intent.putExtra("root", root);

            startActivity(intent);
        });


        // profile add
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageReference.child("userPicture/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                current_user_image_uri = uri.toString();
                Picasso.get().load(uri).into(post_detail_currentuser_img);
            }
        });


        // image of the place

        Picasso.get().load(placeImageUri).into(placeDecImageview);


        // retrieve comment
        cmntRec.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<cmnHelper> options = new FirebaseRecyclerOptions.Builder<cmnHelper>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("places").child(division).child(reference).child("comment"), cmnHelper.class)
                .build();

        cmntAdapter = new CommentAdapter(options);
        cmntRec.setAdapter(cmntAdapter);


        // add comment
        post_detail_add_comment_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                check++;

                //get date
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                commentDate = simpleDateFormat.format(calendar.getTime());

                // upload comment in database
                UploadComment(userName, post_detail_comment.getText().toString(), division, reference, current_user_image_uri, commentDate);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cmntAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cmntAdapter.stopListening();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(new Intent(getApplicationContext(), category_places.class));
        i.putExtra("place", division);
        startActivity(i);
        finish();
    }


// add comment function

    private void UploadComment(String name, String comment, String division, String reference, String current_user_image_uri, String commentDate) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(comment)) {
            DatabaseReference df = FirebaseDatabase.getInstance().getReference("places").child(division).child(reference).child("comment").child(name);


            // get rating
            String user_rating = String.valueOf(revRating.getRating());

            cmnHelper ch = new cmnHelper(name, comment, user_rating, current_user_image_uri, commentDate);
            df.setValue(ch);
            total = revRating.getRating();
            DatabaseReference cntratingref = FirebaseDatabase.getInstance().getReference("places").child(division).child(reference).child("countRating");
            cntratingref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    countRate cr = snapshot.getValue(countRate.class);
                    if (cr != null) {

                        if (check == 1) {
                            ppl = (Integer.parseInt(cr.getPpl())) + 1;
                            total += Float.parseFloat(cr.getTotalRate());
                            float fn = total / ppl;
                            second_ratingbar.setRating(fn);
                            rated_ppl = Integer.toString(ppl);
                            rating = String.format("%.2f", fn);
                            second_rating_number.setText(String.format("%.2f", fn));
                            second_rated_people.setText("(" + ppl + ")");
                            FirebaseDatabase.getInstance().getReference("places").child(division).child(reference).child("rating").setValue(String.format("%.2f", fn));
                            countRate ch = new countRate(String.valueOf(ppl), String.valueOf(total));
                            cntratingref.setValue(ch);
                        }
                        check = 0;
                    } else {
                        Toast.makeText(place_desc.this, "No valid user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();
            revRating.setRating(0);
            post_detail_comment.setText(" ");


        }

    }
}



