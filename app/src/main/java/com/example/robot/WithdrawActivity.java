package com.example.robot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class WithdrawActivity extends AppCompatActivity {
    TextView coinsTv;
    TextInputEditText password;
    EditText numberWallet, withdraw_coins;
    Button done;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userName;
    int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_withdraw);

        auth=FirebaseAuth.getInstance ();
        user=auth.getCurrentUser ();
        reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );

        initialize();
        getData();
        onClick();

    }
    private void initialize() {
        done=findViewById ( R.id.done );
        coinsTv=findViewById ( R.id.coinsTv );
        password=findViewById ( R.id.password );
        withdraw_coins=findViewById ( R.id.withdraw_coins );
        numberWallet=findViewById ( R.id.numberWallet );
    }
    public void getData() {
        reference.child ( user.getUid () ).child("withdraw").addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c=snapshot.child ( "coins" ).getValue ().toString ();
                coinsTv.setText ( c );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText ( WithdrawActivity.this, "Error"+error.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        });
    }
    private void onClick() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int s = Integer.parseInt(coinsTv.getText().toString());
                        if (numberWallet.getText().toString().isEmpty()) {
                            numberWallet.setError("يرجي ادخال رابط محفظتك");
                        } else if (withdraw_coins.getText().toString().isEmpty()) {
                            withdraw_coins.setError("يرجي ادخال عملاتك بشكل صحيح");
                        } else if (snapshot.child("withdraw").child("withdraw").child("none").exists()) {
                            Toast.makeText(WithdrawActivity.this, "لا يمكنك طلب ايداع الا عندما يتم تنفيذ الطلب الاول", Toast.LENGTH_SHORT).show();
                        } else {
                            coins = Integer.parseInt(withdraw_coins.getText().toString());
                            if (coins > s) {
                                withdraw_coins.setError("لا يمكنك سحب اكثر من عملاتك");
                            } else if (coins < 5) {
                                withdraw_coins.setError("لا يمكنك سحب اقل من الحد الادني 5");
                            } else {
                                String c = snapshot.child("password").getValue().toString();
                                userName = snapshot.child("name").getValue().toString();
                                if (password.getText().toString().equals(c)
                                        && !password.getText().toString().isEmpty()) {
                                    setWithdraw();
                                } else {
                                    password.setError("كلمة المرور خطأ");
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void setWithdraw() {
        String money = withdraw_coins.getText().toString();
        int value = Integer.parseInt(money);
        double percentage = 0.14;
        double deduction = value * percentage;
        double result = value - deduction;
        int price = (int) result;
        String f = String.valueOf(price);

        int coins1 = Integer.parseInt(money);
        int coin1 = Integer.parseInt(coinsTv.getText().toString());
        int updateCoins = coin1 - coins1;

        HashMap map1 = new HashMap();
        map1.put("coins", updateCoins);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);
        HashMap<String,Object> map=new HashMap<> (  );
        map.put("none", "جاري التنفيذ");
        map.put("date", formattedDateTime);
        map.put("userName", userName);
        map.put("method", "سحب");
        map.put("money", f);
        map.put("earn_money", f);
        map.put("numberWallet", numberWallet.getText().toString());

        reference.child(user.getUid()).child("withdraw").child("withdraw").setValue(map);
        reference.child(user.getUid()).child("withdraw").updateChildren(map1);
        Toast.makeText(this, "تم ارسال طلبك وجاري العمل عليه", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}


