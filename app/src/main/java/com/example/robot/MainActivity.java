package com.example.robot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.robot.Adapter.SliderAdapter;
import com.example.robot.Model.RobotModel;
import com.example.robot.Model.SliderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView coinsTv, daily_price1, earn_price1, daily_price2, earn_price2, daily_price3, earn_price3, daily_price4,
            earn_price4, daily_price5, earn_price5, daily_price6, earn_price6, robot1, robot2, robot3, robot4, robot5, robot6;
    DatabaseReference reference;
    FirebaseUser user;
    SliderView sliderView;
    SliderAdapter adapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        TextView sliderText = findViewById(R.id.sliderText);
        sliderText.setSelected(true);

        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        renewItems();

        getMoney();

        clickListener();

    }

    private void clickListener() {
        robot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(coinsTv.getText().toString());
                if (c >= 100) {
                    dialog(daily_price1, earn_price1, "سعر التعدين: 100", 100, 102);
                } else {
                    Toast.makeText(MainActivity.this, "ليس لديك أموال كافية", Toast.LENGTH_SHORT).show();
                }
            }
        });
        robot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(coinsTv.getText().toString());
                if (c >= 300) {
                    dialog(daily_price2, earn_price2, "سعر التعدين: 300", 300, 306);
                } else {
                    Toast.makeText(MainActivity.this, "ليس لديك أموال كافية", Toast.LENGTH_SHORT).show();
                }
            }
        });
        robot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(coinsTv.getText().toString());
                if (c >= 500) {
                    dialog(daily_price3, earn_price3, "سعر التعدين: 500", 500, 510);
                } else {
                    Toast.makeText(MainActivity.this, "ليس لديك أموال كافية", Toast.LENGTH_SHORT).show();
                }
            }
        });
        robot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(coinsTv.getText().toString());
                if (c >= 800) {
                    dialog(daily_price4, earn_price4, "سعر التعدين: 800", 800, 816);
                } else {
                    Toast.makeText(MainActivity.this, "ليس لديك أموال كافية", Toast.LENGTH_SHORT).show();
                }
            }
        });
        robot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(coinsTv.getText().toString());
                if (c >= 1000) {
                    dialog(daily_price5, earn_price5, "سعر التعدين: 1000", 1000, 1020);
                } else {
                    Toast.makeText(MainActivity.this, "ليس لديك أموال كافية", Toast.LENGTH_SHORT).show();
                }
            }
        });
        robot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(coinsTv.getText().toString());
                if (c >= 1500) {
                    dialog(daily_price6, earn_price6, "سعر التعدين: 1500", 1500, 1530);
                } else {
                    Toast.makeText(MainActivity.this, "ليس لديك أموال كافية", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.robot:
                        startActivity(new Intent(MainActivity.this, RobotActivity.class));
                        return true;

                    case R.id.home:

                        return true;

                    case R.id.account:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return true;

                    case R.id.vip:
                        startActivity(new Intent(MainActivity.this, VipActivity.class));
                        return true;
                }
                return false;
            }
        });

    }

    private void initialize() {
        bottomNavigationView = findViewById(R.id.navigation_bar);
        progressBar = findViewById(R.id.progress);
        sliderView = findViewById(R.id.imageSlider);
        coinsTv = findViewById(R.id.coinsTv);
        daily_price1 = findViewById(R.id.daily_price1);
        earn_price1 = findViewById(R.id.earn_price1);
        daily_price2 = findViewById(R.id.daily_price2);
        earn_price2 = findViewById(R.id.earn_price2);
        daily_price3 = findViewById(R.id.daily_price3);
        earn_price3 = findViewById(R.id.earn_price3);
        daily_price4 = findViewById(R.id.daily_price4);
        earn_price4 = findViewById(R.id.earn_price4);
        daily_price5 = findViewById(R.id.daily_price5);
        earn_price5 = findViewById(R.id.earn_price5);
        daily_price6 = findViewById(R.id.daily_price6);
        earn_price6 = findViewById(R.id.earn_price6);
        robot1 = findViewById(R.id.robot1);
        robot2 = findViewById(R.id.robot2);
        robot3 = findViewById(R.id.robot3);
        robot4 = findViewById(R.id.robot4);
        robot5 = findViewById(R.id.robot5);
        robot6 = findViewById(R.id.robot6);
    }

    public void getMoney() {
        reference.child(user.getUid()).child("withdraw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c = snapshot.child("coins").getValue().toString();
                coinsTv.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i == 0) {
                sliderItem.setImageUrl("https://i.postimg.cc/mZXCjXTW/image-Slider1.jpg");
            } else if (i == 1) {
                sliderItem.setImageUrl("https://i.postimg.cc/nLhQqBNh/image-Slider2.jpg");
            } else {
                sliderItem.setImageUrl("https://i.postimg.cc/CK2dDtjc/image-Slider3.jpg");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    private void dialog(TextView daily_price, TextView earn_price, String price, int price_s, int price_plus) {
        new AlertDialog.Builder(this)
                .setTitle("شراء الروبوت")
                .setMessage("هل انت متأكد من انك تريد شراء الروبوت؟")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        progressBar.setVisibility(View.VISIBLE);
                        reference.child(user.getUid()).child("Robot").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Toast.makeText(MainActivity.this, "لا يمكنك شراء اكثر من روبوت في اليوم الواحد", Toast.LENGTH_SHORT).show();
                                } else {
                                    LocalDate currentDate = LocalDate.now();
                                    LocalDate yesterdayDate = currentDate.plusDays(1);
                                    DayOfWeek yesterdayDayOfWeek = yesterdayDate.getDayOfWeek();

                                    RobotModel r = new RobotModel();
                                    r.setDaily_price(daily_price.getText().toString());
                                    r.setEarn_price(earn_price.getText().toString());
                                    r.setPrice_robot(price);
                                    r.setPrice(price_s);
                                    r.setPrice_plus(price_plus);
                                    r.setTime_now(yesterdayDayOfWeek.toString());
                                    reference.child(user.getUid()).child("Robot").setValue(r)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    int c = Integer.parseInt(coinsTv.getText().toString());
                                                    int total_price = c - price_s;

                                                    HashMap map = new HashMap();
                                                    map.put("coins", total_price);

                                                    reference.child(user.getUid()).child("withdraw").updateChildren(map);
                                                    progressBar.setVisibility(View.GONE);
                                                    startActivity(new Intent(MainActivity.this, RobotActivity.class));
                                                    Toast.makeText(MainActivity.this, "تم الأشتراك في الروبوت بنجاح", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).create().show();
    }

}