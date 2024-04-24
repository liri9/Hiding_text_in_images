package com.example.textencryption;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public MaterialButton main_btn_upload, main_btn_logOut;
    public RecyclerView main_lst_photos;
    private FirebaseStorage storage;
    private ArrayList<Image> allImages = new ArrayList<>();
    private Adapter_Photo_cards adapter = new Adapter_Photo_cards(this, allImages);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        storage = FirebaseStorage.getInstance();
        findViews();
        initViews();
        setPhotosFromDB();

    }

    private void updateList(ArrayList<Image> allImages) {
        adapter.updateList(allImages);

    }

    private void setPhotosFromDB() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
        }
        List<String> imagePaths = getAllSavedImagePaths();
        Log.d("ImageHandler",imagePaths.toString());
        //  String imagePath = MySP.getInstance().getString("last_saved_image_path", null);
        for (String imagePath : imagePaths) {
            Log.d("ImageHandler", "Attempting to load image from path: " + imagePath);

            if (imagePath != null) {
                try {
                    FileInputStream fis = new FileInputStream(new File(imagePath));
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    fis.close();

                    // Assuming Image is a class that holds Bitmap and description
                    Image image = new Image(bitmap, " ");

                    // Add to your list and update RecyclerView
                    allImages.add(image);
                    Log.d("imagepath",bitmap.toString());
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            } else {
            }
        }
        initList();
        updateList(allImages);
    }

    public List<String> getAllSavedImagePaths() {
        List<String> allPaths = new ArrayList<>();
        int count = MySP.getInstance().readInt("count",0);
        Log.d("ImageHandler", "count is ;  " + count);

        for (int i=0;i<=count;i++) {
            allPaths.add(MySP.getInstance().getString("image_path"+i, ""));
            Log.d("ImageHandler", "saved;  " + allPaths);
        }
        return allPaths;
    }

    private void initList() {
        main_lst_photos.setLayoutManager(new GridLayoutManager(this, 1));
        main_lst_photos.setHasFixedSize(true);
        main_lst_photos.setAdapter(adapter);
    }

    private void initViews() {
        main_btn_upload.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, uploadActivity.class);
            startActivity(intent);
        });
        main_btn_logOut.setOnClickListener(v -> {
            AppManager.getInstance().setLoggedIn(null);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();
        });

    }

    private void findViews() {
        main_btn_upload = findViewById(R.id.main_btn_upload);
        main_btn_logOut = findViewById(R.id.main_btn_logOut);
        main_lst_photos = findViewById(R.id.main_lst_photos);
    }
}