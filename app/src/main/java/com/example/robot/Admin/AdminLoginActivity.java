package com.example.robot.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.robot.Admin.MainAdminActivity;
import com.example.robot.ResetActivity;
import com.example.robot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminLoginActivity extends AppCompatActivity {
    TextInputLayout emailEdit,passEdit;
    AppCompatButton login;
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_admin_login );

        initialize ();
        auth=FirebaseAuth.getInstance ();

        reference=FirebaseDatabase.getInstance().getReference().child("Admin");
        user=auth.getCurrentUser();

        clickListener();

    }

    private void clickListener() {
        login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(emailEdit.getEditText().getText().toString()))
                {
                    emailEdit.setError ( "يرجي أدخال البريد الألكتروني" );
                    return;
                }
                else if (TextUtils.isEmpty(passEdit.getEditText().getText().toString()))
                {
                    passEdit.setError ( "يرجي أدخال كلمة المرور" );
                    return;
                }

                signIn(emailEdit.getEditText().getText().toString(), passEdit.getEditText().getText().toString());
            }
        } );

    }

    private void signIn(String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (email.equals("admin@gmail.com") && password.equals("admin@123")) {
                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                                startActivity(new Intent(AdminLoginActivity.this, MainAdminActivity.class));
                                finish();
                                Toast.makeText(AdminLoginActivity.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                login.setVisibility(View.VISIBLE);
                                Toast.makeText(AdminLoginActivity.this, "يوجد خطأ في البريد الألكتروني او كلمة المرور", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(AdminLoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
      }

    private void initialize()
    {
        emailEdit=findViewById ( R.id.email_et );
        passEdit=findViewById ( R.id.pa_et );
        login=findViewById ( R.id.Login );
        progressBar=findViewById ( R.id.progressBar );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}