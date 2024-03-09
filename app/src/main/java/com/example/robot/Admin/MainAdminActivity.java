package com.example.robot.Admin;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.MainActivity;
import com.example.robot.Model.RobotModel;
import com.example.robot.RobotActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.robot.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainAdminActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Fetch> list = new ArrayList<>();
    AdpterData adpterData;
    SearchView searchView;
    TextView textView;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return true;
            }
        });

        auth=FirebaseAuth.getInstance ();
        user=auth.getCurrentUser ();
        recyclerView = findViewById(R.id.rec);
        textView = findViewById(R.id.text55);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adpterData = new AdpterData(list,this);
        recyclerView.setAdapter(adpterData);
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        root.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Fetch fetch = new Fetch();
                    fetch.setName(snapshot1.child("name").getValue().toString());
                    fetch.setUserName(snapshot1.child("name").getValue().toString());
                    fetch.setPassword(snapshot1.child("password").getValue().toString());
                    fetch.setEmail(snapshot1.child("email").getValue().toString());
                    fetch.setMobile(snapshot1.child("phone").getValue().toString());
                    fetch.setReferCode(snapshot1.child("referCode").getValue().toString());
                    fetch.setId(snapshot1.child("uid").getValue().toString());
                    fetch.setUid(snapshot1.child("deviceID").getValue().toString());
                    fetch.setImage(snapshot1.child("image").getValue().toString());
                    fetch.setCoins(snapshot1.child("withdraw").child("coins").getValue().toString());
                    if (snapshot1.child("withdraw").child("date").exists()) {
                        fetch.setDate(snapshot1.child("withdraw").child("date").getValue().toString());
                    } else {
                        fetch.setDate("لا يوجد بيانات");
                    }
                    if (snapshot1.child("withdraw").child("method").exists()) {
                        fetch.setMethod(snapshot1.child("withdraw").child("method").getValue().toString());
                    } else {
                        fetch.setMethod("لا يوجد بيانات");
                    }
                    if (snapshot1.child("withdraw").child("money").exists()) {
                        fetch.setMoney(snapshot1.child("withdraw").child("money").getValue().toString());
                    } else {
                        fetch.setMoney("لا يوجد بيانات");
                    }
                    if (snapshot1.child("withdraw").child("none").exists()) {
                        fetch.setNone(snapshot1.child("withdraw").child("none").getValue().toString());
                    } else {
                        fetch.setNone("لا يوجد بيانات");
                    }
                    if (snapshot1.child("withdraw").child("image").exists()) {
                        fetch.setImage_robot(snapshot1.child("withdraw").child("image").getValue().toString());
                    } else {
                        fetch.setImage_robot("لا يوجد بيانات");
                    }
                    if (snapshot1.child("withdraw").child("earn_money").exists()) {
                        fetch.setEarn_money(snapshot1.child("withdraw").child("earn_money").getValue().toString());
                    } else {
                        fetch.setEarn_money("لا يوجد بيانات");
                    }
                    list.add(fetch);
                }
                adpterData.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fileList(String text) {
        List<Fetch> filteredList = new ArrayList<>();
        for (Fetch fetch : list){
            if (fetch.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(fetch);
            }
        }
        if (filteredList.isEmpty()){
            recyclerView.setVisibility(GONE);
            textView.setVisibility(VISIBLE);
        }else{
            adpterData.setFilteredList(filteredList);
            textView.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("هل تريد تسجيل الخروج؟")
                .setMessage("هل انت متأكد من انك تريد تسجيل الخروج من حساب الادمن؟")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        auth.signOut();
                        finish();
                        MainAdminActivity.super.onBackPressed();
                    }
                }).create().show();
    }

}