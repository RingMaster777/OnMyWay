package com.example.tourguide.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.PostInfo;
import com.example.tourguide.user.user_dashboard;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ShowAllData extends AppCompatActivity {

    private RecyclerView Adminrecview;
    private ShowPostAdapter adapter;
    static String adminDivision = "Dhaka";
    private TextView div;
    private Spinner divisionSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_all_data);

        Adminrecview = (RecyclerView) findViewById(R.id.Adminrecview);
        Adminrecview.setLayoutManager(new LinearLayoutManager(this));
        div = findViewById(R.id.div);
        divisionSpinner = findViewById(R.id.divisionSpinner);


        String[] division = getResources().getStringArray(R.array.division);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.sample_view, R.id.textViewSampleId, division);
        divisionSpinner.setAdapter(spinnerAdapter);
        //String division = divisionSpinner.getSelectedItem().toString();

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                div.setText(division[position]);
                adminDivision = division[position];


                FirebaseRecyclerOptions<PostInfo> options = new FirebaseRecyclerOptions.Builder<PostInfo>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("places").child(division[position]), PostInfo.class)
                        .build();

                adapter = new ShowPostAdapter(options);
                Adminrecview.setAdapter(adapter);
                adapter.startListening();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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