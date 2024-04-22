package com.example.textencryption;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
            imageView.setImageBitmap(encodedBitmap); // Show the encoded image

            MyRTFB.saveNewImage(imageUri, String.valueOf(imageView.getId()), new MyRTFB.CB_Image() {
                @Override
                public void onData(String imageUrl) {
                    Toast.makeText(uploadActivity.this, "Image uploaded and saved successfully", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(uploadActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                }
            });

          //  String decodedMessage = Steganography.decode(encodedBitmap);
            //Toast.makeText(this, "Decoded Message: " + decodedMessage, Toast.LENGTH_LONG).show();
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


