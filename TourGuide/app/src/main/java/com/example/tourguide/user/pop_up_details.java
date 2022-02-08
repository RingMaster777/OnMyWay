package com.example.tourguide.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tourguide.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class pop_up_details extends AppCompatActivity implements LocationListener {
    private ImageView down_arrow, header_background;
    private TextView pop_up_details_title, third_rating_number, third_rated_people, about_text, type_of_view_text;
    private RatingBar pop_up_details_ratingbar;
    private ScrollView third_scrollView;
    private Button roadmap_button;
    private String desc, title, division, rating, currentLocation, placeImageUri, reference, root, rated_people;
    private Animation from_bottom;

    // @RequiresApi (api = Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_details);

        // get values from previous activity
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        division = bundle.getString("division");
        desc = bundle.getString("desc");
        rating = bundle.getString("rating");
        rated_people = bundle.getString("rated_people");
        placeImageUri = bundle.getString("placeImageUri");
        reference = bundle.getString("reference");
        root = bundle.getString("root");

        // initialize hooks
        header_background = findViewById(R.id.header_background);
        pop_up_details_title = findViewById(R.id.pop_up_details_title);
        third_rating_number = findViewById(R.id.third_rating_number);
        third_rated_people = findViewById(R.id.third_rated_people);
        about_text = findViewById(R.id.about_text);
        roadmap_button = findViewById(R.id.roadmap_button);
        pop_up_details_ratingbar = findViewById(R.id.pop_up_details_ratingbar);
        type_of_view_text = findViewById(R.id.type_of_view_text);

        // set value in the hooks uber pathao food panda
        pop_up_details_title.setText(title);
        about_text.setText(desc);
        pop_up_details_ratingbar.setRating(Float.parseFloat(rating));
        third_rating_number.setText(rating);
        third_rated_people.setText("("+ rated_people +")");
        type_of_view_text.setText(root);
        Picasso.get().load(placeImageUri).into(header_background);



        // animations
        //down_arrow = findViewById(R.id.down_arrow);
        third_scrollView = findViewById(R.id.third_scroll_view);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);

        //down_arrow.setAnimation(from_bottom);
        third_scrollView.setAnimation(from_bottom);

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

        // get current location

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(pop_up_details.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(pop_up_details.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }


        roadmap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                DisplayTract();
            }
        });

        /*down_arrow.setOnClickListener((View) -> {
            Intent intent = new Intent(pop_up_details.this,place_desc.class);

            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(down_arrow,"background_image_transition");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(pop_up_details.this,pairs);

            startActivity(intent,options.toBundle());
            finish();
        });*/


    }

    private void DisplayTract() {
        //If the device does not have a map installed, then redirect it to play store
        try {
            // When google google map is installed
            // Initialize uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + currentLocation + "/" + title);
            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            //Set package
            intent.setPackage("com.google.android.apps.maps");

            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Start Activity
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            //When google map is not installed
            //Initialize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.goole.android.apps.maps");

            //Initialize intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Start activity

            startActivity(intent);
        }
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (LocationListener) pop_up_details.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(pop_up_details.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            currentLocation = address;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}