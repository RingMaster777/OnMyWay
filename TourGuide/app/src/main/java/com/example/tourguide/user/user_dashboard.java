package com.example.tourguide.user;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tourguide.R;
import com.example.tourguide.admin.ShowAllData;
import com.example.tourguide.common.Login;
import com.example.tourguide.helperclass.UserInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class user_dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Button showinfobtn;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    static StorageReference propicref;

    static String staticUserName;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView menuIcon;

    private String userStatus;

    private MenuItem item;
    private ImageSlider mainSlider;


    private CardView dhakaCard, chittagongCard, sylhetCard, khulnaCard, rajshahiCard, barishalCard, rangpurCard, mymensinghCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);


        auth = FirebaseAuth.getInstance();


        // hooks :: assigning to the variabl

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu_icon);


        dhakaCard = findViewById(R.id.dhaka);
        chittagongCard = findViewById(R.id.chittagong);
        rajshahiCard = findViewById(R.id.rajshahi);
        khulnaCard = findViewById(R.id.khulna);
        barishalCard = findViewById(R.id.barishal);
        sylhetCard = findViewById(R.id.sylhet);
        rangpurCard = findViewById(R.id.rangpur);
        mymensinghCard = findViewById(R.id.mymensingh);
        mainSlider = (ImageSlider) findViewById(R.id.image_slider);


        dhakaCard.setOnClickListener(this);
        chittagongCard.setOnClickListener(this);
        rajshahiCard.setOnClickListener(this);
        khulnaCard.setOnClickListener(this);
        sylhetCard.setOnClickListener(this);
        barishalCard.setOnClickListener(this);
        mymensinghCard.setOnClickListener(this);
        rangpurCard.setOnClickListener(this);


        sliderView();

        //Navigation Drawer Function
        navigationDrawer();

        // getting userName
        showInfo();

        showinfobtn = findViewById(R.id.showinfobtn);


        showinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShowAllData.class));
                finish();

            }
        });

    }


    //Navigation Drawer Functions
    private void navigationDrawer() {

        //  navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), user_Profile.class));
                finish();
                break;


            case R.id.nav_add_new_place:
                startActivity(new Intent(getApplicationContext(), addNewPlace.class));
                finish();
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;


            case R.id.nav_About_us:
                startActivity(new Intent(getApplicationContext(), about_us.class));
                finish();
                break;

            case R.id.nav_Contact_Us:
                break;


        }


        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.dhaka:
                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Dhaka");
                startActivity(i);
                finish();
                break;
            case R.id.chittagong:
                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Chittagong");
                startActivity(i);
                finish();
                break;
            case R.id.barishal:

                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Barishal");
                startActivity(i);
                finish();
                break;
            case R.id.rajshahi:
                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Rajshahi");
                startActivity(i);
                finish();
                break;
            case R.id.sylhet:

                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Sylhet");
                startActivity(i);
                finish();
                break;
            case R.id.rangpur:
                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Rangpur");
                startActivity(i);
                finish();
                break;
            case R.id.mymensingh:

                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Mymensingh");
                startActivity(i);
                finish();
                break;
            case R.id.khulna:

                i = new Intent(new Intent(getApplicationContext(), category_places.class));

                i.putExtra("place", "Khulna");
                startActivity(i);
                finish();
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }


    }


    public void showInfo() {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageReference.child("userPicture/" + firebaseUser.getUid() + "/profile.jpg");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo ui = snapshot.getValue(UserInfo.class);
                if (ui != null) {
                    staticUserName = (ui.getUserName());
                    userStatus = ui.getUserStatus();
                    Log.d("bal", "onDataChange: " + userStatus);
                    if (userStatus.equals("admin")) {
                        showinfobtn.setVisibility(View.VISIBLE);
                    }


                } else {
                    Toast.makeText(user_dashboard.this, "No valid user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void sliderView() {
        final List<SlideModel> remoteimages = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren())
                            remoteimages.add(new SlideModel(data.child("url").getValue().toString(), data.child("title").getValue().toString(), ScaleTypes.FIT));

                        mainSlider.setImageList(remoteimages, ScaleTypes.FIT);

                        mainSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                remoteimages.get(i).getTitle().toString();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}


