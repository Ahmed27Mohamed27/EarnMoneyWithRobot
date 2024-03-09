package com.example.robot.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robot.Model.Payment_items;
import com.example.robot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminWithdrawActivity extends AppCompatActivity {

    TextView no, done, cancel;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Payment_items> list;
    Adapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_withdraw);

        no=findViewById(R.id.no);
        recyclerView=findViewById(R.id.recyclerView);
        cancel=findViewById(R.id.cancel);
        done=findViewById(R.id.done);
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String uid = sharedPreferences.getString("uid", "");

        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("withdraw").child("withdraw").child("none").removeValue();
                Toast.makeText(AdminWithdrawActivity.this, "تم", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap map = new HashMap();
                map.put("none", "ملغي");
                reference.child("withdraw").child("withdraw").updateChildren(map);
                Toast.makeText(AdminWithdrawActivity.this, "تم", Toast.LENGTH_SHORT).show();
            }
        });

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter1(list, this);
        recyclerView.setAdapter(adapter);

        reference.child("withdraw").child("withdraw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Payment_items item = new Payment_items();
                    item.setMoney(snapshot.child("money").getValue().toString());
                    item.setDate(snapshot.child("date").getValue().toString());
                    item.setMethod(snapshot.child("method").getValue().toString());
                    if (snapshot.child("none").exists()) {
                        item.setNone(snapshot.child("none").getValue().toString());
                    } else {
                        item.setNone("تمت العملية");
                    }
                    item.setEarn_money(snapshot.child("earn_money").getValue().toString());
                    item.setUserName(snapshot.child("userName").getValue().toString());
                    item.setNumberWallet(snapshot.child("numberWallet").getValue().toString());
                    list.add(item);
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                    no.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}