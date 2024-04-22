package com.example.textencryption;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    public EditText register_userName;
    public MaterialButton register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViews();
        initViews();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            login();
        } else {
            checkIfUserInMyServer();
        }
    }

    private void initViews() {
        register_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser();
                openApp();
            }
        });
    }

    private void registerUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            User user = new User()
                    .setId(firebaseUser.getUid())
                    .setPhone(firebaseUser.getPhoneNumber())
                    .setUserName(register_userName.getText().toString());

            AppManager.getInstance().setLoggedIn(user);
            MyRTFB.saveNewUser(user);
        } else {
            Log.d("faillll", " firebase failed!!!");
        }
    }

    private void findViews() {
        register_userName.findViewById(R.id.register_userName);
        register_btn.findViewById(R.id.register_btn);
    }

    private void checkIfUserInMyServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        MyRTFB.getUserData(firebaseUser.getUid(), user -> {
            if (user == null) {
                Log.d("checkuser - null", "hi");
                //  registerUser();
            } else if (user.getUserName() == null || user.getUserName() == null || user.getUserName().isEmpty() || user.getUserName().isEmpty()) {
                //  registerUser();
            } else {
                Toast.makeText(LoginActivity.this, "Welcome back " + user.getUserName(), Toast.LENGTH_LONG).show();
                AppManager.getInstance().setLoggedIn(user);
                Log.d(AppManager.getInstance().getLoggedIn().getUserName(),"app manager" );
                Log.d(user.getUserName(),"bo app manager" );
                openApp();
            }
        });
    }

    private void openApp() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

        finish();
    }
    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> {
                IdpResponse response = result.getIdpResponse();

                if (result.getResultCode() == RESULT_OK) {
                    checkIfUserInMyServer();
                } else {
                    // Sign in failed
                    if (response == null) {
                        // User pressed back button
                        showSnackbar("Sign in cancelled");
                        return;
                    }

                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackbar("No internet connection");
                        return;
                    }

                    showSnackbar("Unknown error");
                }
            });

    private void login() {
        final Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAlwaysShowSignInMethodScreen(true)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()
                ))
                .setLogo(R.mipmap.ic_launcher)
                .build();


        signInLauncher.launch(signInIntent);
    }
    private void showSnackbar(String message) {
        Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();
    }
}