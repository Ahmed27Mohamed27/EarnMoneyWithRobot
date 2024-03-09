package com.example.robot.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.robot.PaymentHistoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.robot.R;

import java.util.HashMap;

public class Info extends AppCompatActivity {

    TextView name, email, password, uid, coins, referCode, phone, btnWithdraw, btnDepost;
    FirebaseUser user;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtCountry);
        password = findViewById(R.id.txtId);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        referCode = findViewById(R.id.referCode);
        coins = findViewById(R.id.coins);
        uid = findViewById(R.id.uid);

        String s = getIntent().getExtras().getString("name");
        name.setText(s);
        String n = getIntent().getExtras().getString("email");
        email.setText(n);
        String k = getIntent().getExtras().getString("phone");
        phone.setText(k);
        String o = getIntent().getExtras().getString("password");
        password.setText(o);
        String a = getIntent().getExtras().getString("uid");
        uid.setText(a);
        String d = getIntent().getExtras().getString("referCode");
        referCode.setText(d);
        String x = getIntent().getExtras().getString("coins");
        coins.setText(x);
        String aa = getIntent().getExtras().getString("method");
        String i = getIntent().getExtras().getString("image_robot");

        btnDepost = findViewById(R.id.btnDepost);
        btnDepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("uid", uid.getText().toString());
                myEdit.putString("method", aa);
                myEdit.putString("image_robot", i);
                myEdit.apply();
                Intent i = new Intent(Info.this,AdminDepostActivity.class);
                startActivity(i);
            }
        });

        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("uid", uid.getText().toString());
                myEdit.putString("method", aa);
                myEdit.putString("image_robot", i);
                myEdit.apply();
                Intent i = new Intent(Info.this,AdminWithdrawActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");
                HashMap hashMap = new HashMap();
                hashMap.remove("");
                root.child(uid.getText().toString()).setValue(hashMap);
                finish();
                Toast.makeText(Info.this, "تم", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnCoins).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText coin = findViewById(R.id.EditTextCoins);
                if (coin.getText().toString().isEmpty()) {
                    coin.setError("يرجي كتابة عدد العملات");
                } else {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");
                    int coins1 = Integer.parseInt(coins.getText().toString());
                    int coin1 = Integer.parseInt(coin.getText().toString());
                    int updateCoins = coin1 + coins1;
                    HashMap map1 = new HashMap();
                    map1.put("coins", updateCoins);
                    root.child(uid.getText().toString()).child("withdraw").updateChildren(map1);
                    Toast.makeText(Info.this, "تم اضافة العملات بنجاح", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}