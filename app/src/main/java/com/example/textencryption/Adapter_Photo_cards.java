package com.example.textencryption;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class Adapter_Photo_cards extends RecyclerView.Adapter<Adapter_Photo_cards.PhotoViewHolder> {

    private Context context;
    private List<Image> images;

    public Adapter_Photo_cards(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_photos, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Image image = images.get(position);
        holder.userName.setText(image.getUserName());
        Glide.with(context).load(image.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView userName;
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.main_card_userName);
            imageView = itemView.findViewById(R.id.card_img);
        }
    }
}
