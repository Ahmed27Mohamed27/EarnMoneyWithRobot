package com.example.robot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.robot.Admin.MainAdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        isInternetOn();

    }

    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED
        ) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (user != null) {
                        if (user.getEmail().equals("admin@gmail.com")) {
                            startActivity(new Intent(SplashActivity.this, MainAdminActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    } else {
                        Intent intent1 = new Intent(SplashActivity.this, LoginActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                    }
                    finish();

                }
            }, 3000);

            return true;
        } else if (
                cm.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        cm.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        cm.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTING ||
                        cm.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED
        ) {
            Toast.makeText(this, "برجاء التأكد من اتصال الأنترنت!!", Toast.LENGTH_SHORT).show();

            return true;
        }
        return false;
    }
}