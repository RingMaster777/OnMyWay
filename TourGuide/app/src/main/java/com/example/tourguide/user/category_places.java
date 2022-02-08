package com.example.tourguide.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.PostInfo;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class category_places extends AppCompatActivity {
    private RecyclerView recview;
    private MyAdapter adapter;
    static String place;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_places);

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);

        Bundle bundle = getIntent().getExtras();
        place = bundle.getString("place");

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("places")
                .child(place)
                .orderByChild("state")
                .equalTo("approved");

        FirebaseRecyclerOptions<PostInfo> options = new FirebaseRecyclerOptions.Builder<PostInfo>()
                .setQuery(query, PostInfo.class)
                .build();

        adapter = new MyAdapter(options);
        recview.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

    }


    private void search(String s) {

        FirebaseRecyclerOptions<PostInfo> options = new FirebaseRecyclerOptions.Builder<PostInfo>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("places").child(place).orderByChild("title").startAt(s).endAt(s + "\uf8ff"), PostInfo.class)
                .build();

        adapter = new MyAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(new Intent(getApplicationContext(), user_dashboard.class));
        startActivity(i);
        finish();
    }
}
