package com.example.robot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.robot.Adapter.PaymentAdapter;
import com.example.robot.Fragment.DepostFragment;
import com.example.robot.Fragment.WithdrawFragment;
import com.google.android.material.tabs.TabLayout;

public class PaymentHistoryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_payment_history );

        tabLayout=findViewById ( R.id.tabLayout );
        viewPager=findViewById ( R.id.viewPager );

        PaymentAdapter adapter=new PaymentAdapter ( getSupportFragmentManager () );
        adapter.add ( new DepostFragment(),"سجل الأيداع" );
        adapter.add ( new WithdrawFragment(),"سجل السحب" );
        viewPager.setAdapter ( adapter );
        tabLayout.setupWithViewPager ( viewPager );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}