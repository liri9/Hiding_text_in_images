package com.example.textencryption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class uploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    public MaterialButton btn_done,btn_openGallery;
    public EditText edt_text;
    public ImageView imageView;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViews();
        initViews();

    }

    private void initViews() {
        btn_done.setOnClickListener(v->{
            if (imageView != null && edt_text.getText().toString().length() > 0) {
                encode();
            } else {
                Toast.makeText(uploadActivity.this, "Please select an image and enter text", Toast.LENGTH_SHORT).show();
            }
        });
        btn_openGallery.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(uploadActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(uploadActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            } else {
                openGallery();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    private void encode() {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(imageStream);
            String textToEncode = edt_text.getText().toString();
            Bitmap encodedBitmap = Steganography.encode(originalBitmap, textToEncode);
            String directoryName = "images";
            String fileName = "encoded_image_" + System.currentTimeMillis() + ".png";
            File directory = new File(this.getFilesDir(), directoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File imagePath = new File(directory, fileName);

            // Save the bitmap to a file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imagePath);
                encodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);  // Bitmap quality (100 means no compression)
                fos.flush();
                Log.d("ImageHandler", "Image saved to " + imagePath.getAbsolutePath());
            } catch (IOException e) {
                Log.e("ImageHandler", "Error saving image", e);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    Log.e("ImageHandler", "Error closing FileOutputStream", e);
                }
            }

            Log.d("decode",Steganography.decode(encodedBitmap));
            int count = MySP.getInstance().readInt("count",0);
            MySP.getInstance().saveInt("count",++count);
            MySP.getInstance().putString("image_path"+count, imagePath.getAbsolutePath());

        } catch (Exception e) {
            Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void findViews() {
        btn_done =findViewById(R.id.upload_btn_done);
        btn_openGallery =findViewById(R.id.upload_btn_openGallery) ;
        edt_text=findViewById(R.id.upload_edtText) ;
        imageView = findViewById(R.id.imageView);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}


