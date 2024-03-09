package com.example.robot.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robot.Adapter.Adapter;
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


public class WithdrawFragment extends Fragment {

    TextView no;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Payment_items> list;
    Adapter adapter;

    public WithdrawFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);

        no = view.findViewById(R.id.no);
        recyclerView = view.findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter(list);
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
                    no.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

}
