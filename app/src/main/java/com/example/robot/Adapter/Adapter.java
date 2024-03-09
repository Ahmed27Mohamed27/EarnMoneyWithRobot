package com.example.robot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.robot.Model.Payment_items;
import com.example.robot.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {

    private List<Payment_items> list;

    public Adapter(ArrayList<Payment_items> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_depost, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
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
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            money = itemView.findViewById(R.id.money);
            earn_money = itemView.findViewById(R.id.earn_money);
            none = itemView.findViewById(R.id.none);
            method = itemView.findViewById(R.id.method);
            userName = itemView.findViewById(R.id.userName);
            numberWallet = itemView.findViewById(R.id.numberWallet);
        }
    }
}