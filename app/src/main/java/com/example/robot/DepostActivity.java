package com.example.robot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class DepostActivity extends AppCompatActivity {
    TextView coinsTv, copy, wallet;
    ImageView depost_image;
    EditText coinsEdit;
    Button complete;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    private static final int PICK_IMAGE=1;
    private Uri photoUri;
    private String imageUrl;
    String userName;
    ProgressBar pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_depost);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");

        initialize ();
        getData();
        clickListner();

    }
    private void getData() {
        reference.child(user.getUid()).child("withdraw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c=snapshot.child("coins").getValue().toString();
                coinsTv.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 userName = snapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void clickListner() {
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = wallet.getText().toString();

                // Copy the text to the clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);

                // Notify the user that the text has been copied
                Toast.makeText(DepostActivity.this, "تم نسخ النص", Toast.LENGTH_SHORT).show();
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(user.getUid()).child("withdraw").child("depost").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (photoUri == null) {
                                Toast.makeText(DepostActivity.this, "يرجي تحديد صورة الأيداع", Toast.LENGTH_SHORT).show();
                            } else if (coinsEdit.getText().toString().isEmpty()
                                    || coinsEdit.getText().toString().startsWith("0")) {
                                coinsEdit.setError("برجاء تحديد عدد العملات");
                            } else {
                                if (dataSnapshot.child("none").exists()) {
                                    Toast.makeText(DepostActivity.this, "لا يمكنك طلب ايداع الا عندما يتم تنفيذ الطلب الاول", Toast.LENGTH_SHORT).show();
                                } else {
                                    pro.setVisibility(View.VISIBLE);
                                    uploadImage();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        depost_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext ( DepostActivity.this )
                        .withPermissions ( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener ( new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted ())
                                {
                                    Intent intent=new Intent ( Intent.ACTION_PICK );
                                    intent.setType ( "image/*" );
                                    startActivityForResult ( intent,PICK_IMAGE );

                                }else
                                {
                                    Toast.makeText ( DepostActivity.this, "برجاء أعطاء الأذونات", Toast.LENGTH_SHORT ).show ();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            }
                        } ).check ();
            }
        });

    }
    private void uploadImage() {
        if (photoUri == null)
        {
            return;
        }
        String filename=user.getUid ()+".jpg";
        final FirebaseStorage storage=FirebaseStorage.getInstance ();
        final StorageReference storageReference=storage.getReference ().child ( "Images/"+filename );

        storageReference.putFile ( photoUri )
                .addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl=uri.toString ();

                                uploadImageUrlToDatabase();
                            }
                        } );
                    }
                } ).addOnFailureListener ( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText ( DepostActivity.this, "Error"+e.getMessage (), Toast.LENGTH_SHORT ).show ();
                    }
                } ).addOnProgressListener ( new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        long p1=taskSnapshot.getBytesTransferred ();
                        long p2=taskSnapshot.getTotalByteCount ();

                        long r=(p1/1024);
                        long r2=(p2/1024);

                    }
                } );
    }
    private void uploadImageUrlToDatabase() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);
        HashMap<String,Object> map=new HashMap<> (  );
        map.put("image", imageUrl);
        map.put("none", "جاري التنفيذ");
        map.put("date", formattedDateTime);
        map.put("userName", userName);
        map.put("method", "أيداع");
        map.put("money", coinsEdit.getText().toString());
        map.put("earn_money", coinsEdit.getText().toString());
        reference.child ( user.getUid () )
                .child("withdraw")
                .child("depost")
                .setValue ( map )
                .addOnCompleteListener ( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pro.setVisibility(View.GONE);
                        Toast.makeText(DepostActivity.this, "تم الأرسال وجاري العمل علي طلبك", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            if (data !=null)
            {
                photoUri=data.getData ();
                depost_image.setImageURI ( photoUri );
            }
        }
    }
    private void initialize() {
        coinsTv=findViewById(R.id.earning_coins);
        complete=findViewById(R.id.complete);
        wallet=findViewById(R.id.wallet);
        copy=findViewById(R.id.copy);
        depost_image=findViewById(R.id.depost_image);
        pro=findViewById(R.id.pro);
        coinsEdit=findViewById(R.id.coinsEdit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}