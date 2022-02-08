package com.example.tourguide.user;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;
import com.example.tourguide.helperclass.cmnHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends FirebaseRecyclerAdapter<cmnHelper, CommentAdapter.MyViewHolder2> {


    public CommentAdapter(@NonNull FirebaseRecyclerOptions<cmnHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder2 holder, int position, @NonNull cmnHelper cmnHelper) {


        holder.name.setText(cmnHelper.getComment_username());
        holder.comment.setText(cmnHelper.getComment_content());
        holder.rating.setText(cmnHelper.getComment_rating() + "/5");
        holder.date.setText(cmnHelper.getComment_date());
        //String imageUri = cmnHelper.getComment_user_imageUri();
        Picasso.get().load(cmnHelper.getComment_user_imageUri()).into(holder.img);

    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        return new CommentAdapter.MyViewHolder2(view);
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, comment, rating, date;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.comment_user_img);
            name = itemView.findViewById(R.id.comment_username);
            comment = itemView.findViewById(R.id.comment_content);
            rating = itemView.findViewById(R.id.comment_rating);
            date = itemView.findViewById(R.id.comment_date);

        }
    }

}
