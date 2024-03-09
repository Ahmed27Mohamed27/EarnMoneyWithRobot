package com.example.robot.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.robot.Fragment.DepostFragment;
import com.example.robot.Fragment.WithdrawFragment;

import java.util.ArrayList;

public class PaymentAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments=new ArrayList<> (  );
    ArrayList<String> strings=new ArrayList<> (  );


    public PaymentAdapter(@NonNull FragmentManager fm) {
        super ( fm );
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new DepostFragment();
            case 1:
                return new WithdrawFragment();

            default:

                return null;

        }

    }

    @Override
    public int getCount() {
        return fragments.size ();
    }

    public void add(Fragment fr,String sr)
    {
        fragments.add ( fr );
        strings.add ( sr );
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return strings.get ( position );
    }
}

