package com.example.tourguide.admin;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.PostInfo;
import com.example.tourguide.helperclass.sliderInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShowPostAdapter extends FirebaseRecyclerAdapter<PostInfo, ShowPostAdapter.MyViewHolder> {
    private StorageReference storageReference;
    private DatabaseReference df;
    private String place = ShowAllData.adminDivision;
    private List<sliderInfo> sliderArray = new ArrayList<>();


    public ShowPostAdapter(@NonNull FirebaseRecyclerOptions<PostInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PostInfo model) {

        String visibility = model.getState();
        holder.state.setText(model.getState());
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        Picasso.get().load(model.getImage_uri()).into(holder.img);

        if (!visibility.equals("approved")) {
            holder.approve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    df = FirebaseDatabase.getInstance().getReference("places").child(place).child(model.getReference()).child("state");
                    df.setValue("approved");
                    sliderInfo si = new sliderInfo(place, model.getTitle(), model.getReference(), model.getImage_uri());
                    addToSlider(si);
                }
            });

        } else {
            holder.approve_btn.setVisibility(View.INVISIBLE);
            holder.decline_btn.setText("Delete");
        }


        holder.decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                df = FirebaseDatabase.getInstance().getReference("places").child(place).child(model.getReference());
                df.removeValue();
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImage_uri());
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        System.out.println("DELETED");
                    }
                });

            }
        });







        /*storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageReference.child("posts_images/"+place+ "/" + holder.title.getText().toString() + "/image.jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        });*/


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminsinglerow, parent, false);
        return new ShowPostAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, desc, state;
        Button approve_btn, decline_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.adminView);
            title = itemView.findViewById(R.id.adminTitle);
            desc = itemView.findViewById(R.id.adminDesc);
            approve_btn = itemView.findViewById(R.id.approve_btn);
            decline_btn = itemView.findViewById(R.id.decline_btn);
            state = itemView.findViewById(R.id.state);

        }
    }

    private void addToSlider(sliderInfo si) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("slider");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x = 1;
                sliderInfo temp = si;
                for (DataSnapshot data : snapshot.getChildren()) {

                    sliderArray.add(new sliderInfo(data.child("division").getValue().toString(), data.child("title").getValue().toString(), data.child("reference").getValue().toString(), data.child("url").getValue().toString()));

                }

                for (int i = 0; i < sliderArray.size(); i++) {
                    String str = "slide" + x;
                    databaseReference.child(str).setValue(temp);
                    sliderInfo temp2 = sliderArray.get(i);
                    temp = temp2;
                    x++;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
