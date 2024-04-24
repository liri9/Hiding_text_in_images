package com.example.textencryption;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Adapter_Photo_cards extends RecyclerView.Adapter<Adapter_Photo_cards.PhotoViewHolder> {

    private Context context;
    private List<Image> images;
    private String hiddenMessage;

    public Adapter_Photo_cards(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    public void updateList(List<Image> allImages) {
        images = allImages;
        notifyDataSetChanged();
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
        //Glide.with(context).load(image.getImageUrl()).into(holder.imageView);
        Bitmap bitmap = image.getImageUrl();
        holder.imageView.setImageBitmap(bitmap);
        holder.btnDownload.setOnClickListener(v -> {
            saveImage(bitmap, "downloaded_image_" + position + ".jpg");
        });

        holder.btnDecode.setOnClickListener(v -> {
            try {
                decodeImage(bitmap);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
             Toast.makeText(context, "Hidden Text: "+hiddenMessage, Toast.LENGTH_LONG).show();

        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private void downloadImage(String imageUrl) {
        // Assuming imageUrl is a direct URL to the image
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource, imageUrl.substring(imageUrl.lastIndexOf('/') + 1));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void saveImage(Bitmap image, String imageName) {

//        String imageFileName = imageName + ".jpg";
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "YourAppName");
//
//        boolean success = true;
//        if (!storageDir.exists()) {
//            success = storageDir.mkdirs();
//        }
//
//        if (success) {
//            File imageFile = new File(storageDir, imageFileName);
//            try (FileOutputStream out = new FileOutputStream(imageFile)) {
//                image.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
//                // PNG is a lossless format, the compression factor (100) is ignored
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            File f = new File(imageFile.getAbsolutePath());
//            Uri contentUri = Uri.fromFile(f);
//            mediaScanIntent.setData(contentUri);
//            this.sendBroadcast(mediaScanIntent);
//            // Notify the gallery to add your photo
//        } else {
//            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
//        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "encoded"+System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "YourAppName");

        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try (OutputStream out = context.getContentResolver().openOutputStream(uri)) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
            Log.e("Image Saving", "Exception in saving image", e);
        }
    }


    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private void decodeImage(Bitmap bitmap) throws Exception {

        hiddenMessage = Steganography.decode(bitmap);

    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView userName;
        ImageView imageView;
        MaterialButton btnDownload, btnDecode;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.main_card_userName);
            imageView = itemView.findViewById(R.id.card_img);
            btnDownload = itemView.findViewById(R.id.main_card_download);
            btnDecode = itemView.findViewById(R.id.main_card_decode);
        }
    }
}
