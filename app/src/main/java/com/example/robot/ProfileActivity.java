package com.example.robot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


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

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profileImage;
    ImageView camera;
    TextView username,email,vip,robot,redeemHistoryTv,coinsTv,logoutTv,contact,depost,withdraw,invite;
    Button updateBtn;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    private static final int PERMISSION=100;
    private static final int PICK_IMAGE=1;
    private Uri photoUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_profile );

        initialize ();
        setSupportActionBar ( toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setTitle ( "" );

        auth=FirebaseAuth.getInstance ();
        user=auth.getCurrentUser ();
        reference= FirebaseDatabase.getInstance ().getReference ().child ( "Users" );

        progressDialog=new ProgressDialog ( ProfileActivity.this );
        progressDialog.setTitle ( "برجاء الأنتظار..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setCancelable ( false );

        clickListener();
        getDataFromDatabase();

    }
    private void clickListener()
    {
        logoutTv.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                auth.signOut ();
                startActivity ( new Intent ( ProfileActivity.this,LoginActivity.class ) );
                finish();
            }
        } );

        vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity ( new Intent ( ProfileActivity.this,VipActivity.class ));
            }
        });

        robot.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( ProfileActivity.this,RobotActivity.class ));
            }
        } );

        redeemHistoryTv.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( ProfileActivity.this,PaymentHistoryActivity.class ));
            }
        } );

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://elshaarwy23.github.io/BKZsweatcoin/BKZ.html"));
                startActivity(intent);
            }
        });

        depost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, DepostActivity.class));
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, WithdrawActivity.class));
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, InviteActivity.class));
            }
        });

        camera.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Dexter.withContext ( ProfileActivity.this )
                        .withPermissions ( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener ( new MultiplePermissionsListener () {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted ())
                                {
                                    Intent intent=new Intent ( Intent.ACTION_PICK );
                                    intent.setType ( "image/*" );
                                    startActivityForResult ( intent,PICK_IMAGE );

                                }else
                                {
                                    Toast.makeText ( ProfileActivity.this, "برجاء أعطاء الأذونات", Toast.LENGTH_SHORT ).show ();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                            }
                        } ).check ();
            }
        } );

        updateBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        } );
    }
    private void uploadImage() {
        if (photoUri ==null)
        {
            return;
        }
        String filename=user.getUid ()+".jpg";
        final FirebaseStorage storage=FirebaseStorage.getInstance ();
        final StorageReference storageReference=storage.getReference ().child ( "Images/"+filename );

        progressDialog.show ();
        storageReference.putFile ( photoUri )
                .addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
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
                } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss ();
                Toast.makeText ( ProfileActivity.this, "Error"+e.getMessage (), Toast.LENGTH_SHORT ).show ();
            }
        } ).addOnProgressListener ( new OnProgressListener<UploadTask.TaskSnapshot> () {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                long p1=taskSnapshot.getBytesTransferred ();
                long p2=taskSnapshot.getTotalByteCount ();

                long r=(p1/1024);
                long r2=(p2/1024);

                progressDialog.setMessage ( "تحديث صورة حسابك" );

            }
        } );
    }

    private void uploadImageUrlToDatabase() {
        HashMap<String,Object> map=new HashMap<> (  );
        map.put ( "image",imageUrl );
        reference.child ( user.getUid () )
                .updateChildren ( map )
                .addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateBtn.setVisibility ( View.GONE );
                        progressDialog.dismiss ();
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
                profileImage.setImageURI ( photoUri );
                updateBtn.setVisibility ( View.VISIBLE );

            }
        }
    }

    private void initialize()
    {
        profileImage=findViewById ( R.id.profile );
        camera=findViewById ( R.id.pick );
        username=findViewById ( R.id.username );
        email=findViewById ( R.id.email );
        robot=findViewById ( R.id.robot );
        vip=findViewById ( R.id.vip );
        redeemHistoryTv=findViewById ( R.id.redeemHistory );
        coinsTv=findViewById ( R.id.coinsTv );
        logoutTv=findViewById ( R.id.logout );
        updateBtn=findViewById ( R.id.updateBtn );
        toolbar=findViewById ( R.id.toolbar );
        contact=findViewById ( R.id.contact );
        depost=findViewById ( R.id.depost );
        withdraw=findViewById ( R.id.withdraw );
        invite=findViewById ( R.id.invite );
    }

    private void getDataFromDatabase() {
        reference.child ( user.getUid () ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String n=snapshot.child ( "name" ).getValue ().toString ();
                String e=snapshot.child ( "email" ).getValue ().toString ();
                String p=snapshot.child ( "image" ).getValue ().toString ();

                username.setText ( n );
                email.setText ( e );

                Glide.with ( getApplicationContext () ).load ( p)
                        .placeholder ( R.drawable.profile )
                        .into ( profileImage );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText ( ProfileActivity.this, "Unable to fetch data"+error.getMessage (), Toast.LENGTH_SHORT ).show ();
                finish ();
            }
        } );

        reference.child(user.getUid()).child("withdraw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String c=snapshot.child ( "coins" ).getValue ().toString ();
                coinsTv.setText (c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}