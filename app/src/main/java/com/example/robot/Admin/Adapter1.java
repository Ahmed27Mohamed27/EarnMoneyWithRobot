package com.example.robot.Admin;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.robot.Model.Payment_items;
import com.example.robot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.AdapterViewHolder> {

    private List<Payment_items> list;

    private Context mContext;

    public Adapter1(ArrayList<Payment_items> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_depost1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Glide.with(mContext).load(list.get(position).getImage()).into(holder.image);
        holder.date.setText(list.get(position).getDate());
        holder.money.setText(list.get(position).getMoney());
        holder.earn_money.setText(" المال" + ": " + list.get(position).getEarn_money());
        holder.none.setText(list.get(position).getNone());
        holder.method.setText(" حالة الطلب" + ": " + list.get(position).getMethod());
        holder.userName.setText(list.get(position).getUserName() + "  :" + " الأسم");
        holder.numberWallet.setText(list.get(position).getNumberWallet() + "  :" + " رابط المحفظة");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Payment_items> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView date, money, earn_money, none, method, userName, numberWallet;
        ImageView image;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            money = itemView.findViewById(R.id.money);
            earn_money = itemView.findViewById(R.id.earn_money);
            none = itemView.findViewById(R.id.none);
            method = itemView.findViewById(R.id.method);
            userName = itemView.findViewById(R.id.userName);
            image = itemView.findViewById(R.id.Image);
            numberWallet = itemView.findViewById(R.id.numberWallet);
        }
    }
}