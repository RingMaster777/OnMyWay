package com.example.tourguide.user;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.PostInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyAdapter extends FirebaseRecyclerAdapter<PostInfo, MyAdapter.MyViewHolder> {
    private StorageReference storageReference;
    private DatabaseReference df;

    public MyAdapter(@NonNull FirebaseRecyclerOptions<PostInfo> options) {
        super(options);
    }

    // displaying the cardViews


    protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final PostInfo model) {
        holder.title.setText(model.getTitle());
        Picasso.get().load(model.getImage_uri()).into(holder.img);
        storageReference = FirebaseStorage.getInstance().getReference();

        String place = category_places.place;


        holder.rating.setRating(Float.parseFloat(model.getRating()));

        // go to description activity

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.img.getContext(), place_desc.class);
                intent.putExtra("place", model.getTitle());
                intent.putExtra("division", category_places.place);
                intent.putExtra("desc", model.getDescription());
                intent.putExtra("rating", model.getRating());
                intent.putExtra("rated_ppl", model.countRating.getPpl());
                intent.putExtra("placeImageUri", model.getImage_uri());
                intent.putExtra("reference", model.getReference());
                intent.putExtra("root", model.getRoot());

                holder.img.getContext().startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        RatingBar rating;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.view);
            title = itemView.findViewById(R.id.textView5);
            rating = itemView.findViewById(R.id.ratingBar);


        }
    }
}
