package com.example.robot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class VipActivity extends AppCompatActivity {
    private TextView coinsTv;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_vip);

        initialize();

        loadData();

        reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );

    }
    private void loadData() {
        reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );
        FirebaseAuth auth=FirebaseAuth.getInstance ();
        FirebaseUser user=auth.getCurrentUser ();
        reference.child ( user.getUid () ).child("withdraw").addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c=snapshot.child ( "coins" ).getValue ().toString ();
                coinsTv.setText ( c );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText ( VipActivity.this, "Error"+error.getMessage (), Toast.LENGTH_SHORT ).show ();
                finish ();
            }
        } );
    }

    private void initialize() {
        coinsTv=findViewById ( R.id.coinsTv );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}