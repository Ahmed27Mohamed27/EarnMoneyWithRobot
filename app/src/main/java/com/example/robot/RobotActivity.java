package com.example.robot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RobotActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    CardView card;
    private TextView mTextViewCountDown;
    TextView coinsTv, no, info, time_robot, price_robot, daily_price, earn_price, cancel1;
    ProgressBar progressBar;
    DatabaseReference reference;
    String coin;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "countdown_prefs";
    private static final String TIME_LEFT_KEY = "time_left";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

        initialize();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();
        clickListener();

        reference.child(user.getUid()).child("Robot").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String time = snapshot.child("time_now").getValue().toString();
                    String price = snapshot.child("price_plus").getValue().toString();
                    int s = Integer.parseInt(price);
                    int c = Integer.parseInt(coinsTv.getText().toString());
                    LocalDate currentDate = LocalDate.now();
                    DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                    if (time.equals(dayOfWeek.toString())) {
                        int money = c + s;
                        setCoins(money);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                Toast.makeText(RobotActivity.this, "تم الأنتهاء من تفعيل الروبوت واستلام الارباح", Toast.LENGTH_SHORT).show();
                                reference.child(user.getUid()).child("Robot").getRef().removeValue();
                            }
                        }, 2000);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//
//        timeLeftInMillis = prefs.getLong(TIME_LEFT_KEY, 0);
//
//        if (timeLeftInMillis > 0) {
//            startCountdown(timeLeftInMillis);
//        } else {
//            // If no saved time, start a new countdown timer
//            long countdownTimeInMillis = 24 * 60 * 60 * 1000;
//            startCountdown(countdownTimeInMillis);
//        }

    }

    private void getDataFromDatabase() {
        reference.child(user.getUid()).child("withdraw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c = snapshot.child("coins").getValue().toString();
                coin = snapshot.child("coins").getValue().toString();
                coinsTv.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RobotActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
//        mTextViewCountDown = findViewById(R.id.counter);
        progressBar = findViewById(R.id.progress);
        coinsTv = findViewById(R.id.coinsTv);
        no = findViewById(R.id.no);
        info = findViewById(R.id.info);
        card = findViewById(R.id.card);
        earn_price = findViewById(R.id.earn_price);
        daily_price = findViewById(R.id.daily_price);
        price_robot = findViewById(R.id.price_robot);
        time_robot = findViewById(R.id.time_robot);
        cancel1 = findViewById(R.id.cancel);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void clickListener() {
        reference.child(user.getUid()).child("Robot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String a = snapshot.child("price_robot").getValue().toString();
//                    String s = snapshot.child("time_robot").getValue().toString();
                    String d = snapshot.child("daily_price").getValue().toString();
                    String f = snapshot.child("earn_price").getValue().toString();
                    price_robot.setText(a);
//                    time_robot.setText(s);
                    daily_price.setText(d);
                    earn_price.setText(f);
                } else {
                    progressBar.setVisibility(View.GONE);
                    card.setVisibility(View.GONE);
                    info.setVisibility(View.GONE);
                    no.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(user.getUid()).child("Robot").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(RobotActivity.this, "جاري أيقاف الروبوت وأسترجاع العملات", Toast.LENGTH_SHORT).show();
                            String money = snapshot.child("price").getValue().toString();

                            int coins1 = Integer.parseInt(money);
                            int coin1 = Integer.parseInt(coin);
                            int updateCoins = coin1 + coins1;

                            HashMap map = new HashMap();
                            map.put("coins", updateCoins);

                            reference.child(user.getUid()).child("withdraw").updateChildren(map);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    Toast.makeText(RobotActivity.this, "تم استرجاع العملات بنجاح وأيقاف الروبوت", Toast.LENGTH_SHORT).show();
                                    reference.child(user.getUid()).child("Robot").getRef().removeValue();
                                }
                            }, 2000);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putLong(TIME_LEFT_KEY, timeLeftInMillis);
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putLong(TIME_LEFT_KEY, timeLeftInMillis);
//        editor.apply();
//
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//    }

//    private void startCountdown(long countdownTimeInMillis) {
//        new CountDownTimer(countdownTimeInMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftInMillis = millisUntilFinished;
//                updateCountdownText();
//            }
//
//            @Override
//            public void onFinish() {
//                timeLeftInMillis = 0;
//                updateCountdownText();
//                mTextViewCountDown.setText("تم أنتهاء الوقت!");
//            }
//        }.start();
//    }

//    private void updateCountdownText() {
//        int hours = (int) (timeLeftInMillis / 1000) / 3600;
//        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
//        int seconds = (int) (timeLeftInMillis / 1000) % 60;
//
//        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
//        mTextViewCountDown.setText(timeLeftFormatted);
//    }

    private void setCoins(int coins) {
        HashMap map = new HashMap();
        map.put("coins", coins);
        reference.child(user.getUid()).child("withdraw").updateChildren(map);
    }

}