package com.example.textencryption;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public MaterialButton main_btn_upload, main_btn_logOut;
    public RecyclerView main_lst_photos;

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
        findViews();
        initViews();

    }

    private void initViews() {
        main_btn_upload.setOnClickListener(v->{
            Intent intent = new Intent( MainActivity.this, uploadActivity.class);
            startActivity(intent);
            finish();
        });
        main_btn_logOut.setOnClickListener(v->{
            AppManager.getInstance().setLoggedIn(null);
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
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