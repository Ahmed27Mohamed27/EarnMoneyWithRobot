package com.example.robot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static java.lang.System.in;
import static java.lang.System.out;

public class InviteActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    String oppositeUID;
    Toolbar toolbar;
    TextView referCodeTv, coinsTv;
    Button redeemBtn;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();
        clickListener();
        redeemValibility();

    }

    private void redeemValibility() {

        reference.child(user.getUid()).child("withdraw")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChild("redeemed")) {
                            boolean isAvailable = snapshot.child("redeemed").getValue(Boolean.class);

                            if (isAvailable) {
                                redeemBtn.setVisibility(View.GONE);
                                redeemBtn.setEnabled(false);
                            } else {
                                redeemBtn.setVisibility(View.VISIBLE);
                                redeemBtn.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InviteActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getDataFromDatabase() {
        reference.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String referCode = snapshot.child("referCode").getValue().toString();
                        referCodeTv.setText(referCode);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(InviteActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        reference.child(user.getUid()).child("withdraw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c = snapshot.child("coins").getValue().toString();
                coinsTv.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InviteActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initialize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        referCodeTv = findViewById(R.id.referCodeTv);
        redeemBtn = findViewById(R.id.redeemBtn);
        coinsTv = findViewById(R.id.coinsTv);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void clickListener() {

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editText = new EditText(InviteActivity.this);
                editText.setHint("اكتب كود الدعوة");

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(layoutParams);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InviteActivity.this);
                alertDialog.setTitle("كود الدعوة");
                alertDialog.setView(editText);

                alertDialog.setPositiveButton("أستيراد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputCode = editText.getText().toString();
                        if (TextUtils.isEmpty(inputCode)) {
                            Toast.makeText(InviteActivity.this, "أدخل رمزًا صالحًا", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (inputCode.equals(referCodeTv.getText().toString())) {
                            Toast.makeText(InviteActivity.this, "لا يمكنك إدخال الرمز الخاص", Toast.LENGTH_SHORT).show();
                        }
                        redeemQuery(inputCode, dialog);

                    }
                }).setNegativeButton("ألغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });


    }

    private void redeemQuery(String inputCode, final DialogInterface dialog) {
        Query query = reference.orderByChild("referCode").equalTo(inputCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    oppositeUID = dataSnapshot.getKey();
//w
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //1
                            String model = snapshot.child(oppositeUID).child("withdraw").child("coins").getValue().toString();
                            int coins = Integer.parseInt(model);
                            int updateCoins = coins + 5;

                            //2
                            String MyModel = snapshot.child(user.getUid()).child("withdraw").child("coins").getValue().toString();
                            int m1 = Integer.parseInt(MyModel);
                            int updateM1 = m1 + 5;

                            //1
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("coins", updateCoins);

                            //2
                            HashMap<String, Object> myMap = new HashMap<>();
                            myMap.put("coins", updateM1);
                            myMap.put("redeemed", true);

                            reference.child(oppositeUID).child("withdraw").updateChildren(map);
                            reference.child(user.getUid()).child("withdraw").updateChildren(myMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog.dismiss();
                                            Toast.makeText(InviteActivity.this, "تمت إضافة العملات بنجاح", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(InviteActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}