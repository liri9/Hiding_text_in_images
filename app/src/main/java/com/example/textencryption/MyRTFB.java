package com.example.textencryption;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyRTFB {
    public interface CB_User {
        void data(User user);
    }
    public interface CB_Image {
        void onData(String imageUrl);
        void onError(String error);
    }
    public static void saveNewUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("USERS");
        ref.child(user.getId()).setValue(user.userAsHashmap());
    }

    public static void getUserData(String id, CB_User cb_user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("USERS");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    User user = snapshot.getValue(User.class);
                    cb_user.data(user);
                } catch (Exception ex) {
                    cb_user.data(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cb_user.data(null);
            }
        });
    }

    public static void saveNewImage(Uri fileUri, String imageId, CB_Image cb_image) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("IMAGES/" + imageId);
        storageRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("IMAGES");
                            ref.child(imageId).setValue(uri.toString())
                                    .addOnSuccessListener(aVoid -> cb_image.onData(uri.toString()))
                                    .addOnFailureListener(e -> cb_image.onError("Failed to save image reference in database"));
                        })
                        .addOnFailureListener(e -> cb_image.onError("Failed to get download URL")))
                .addOnFailureListener(e -> cb_image.onError("Failed to upload image to storage"));
    }

    public static void getImageData(String imageId, CB_Image cb_image) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("IMAGES");
        ref.child(imageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cb_image.onData(snapshot.getValue(String.class));
                } else {
                    cb_image.onError("Image does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cb_image.onError("Failed to retrieve image data");
            }
        });
    }

}
