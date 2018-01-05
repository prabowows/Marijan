package com.example.prabowo.marijan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileTestFragment extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference databaseReference;
    private TextView userEmail;
    private TextView userName;
    private TextView userPoint;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    private ImageView fotoProfil;
    private String nama;
    private String UID;
    private TextView TVpangkat;
    private ImageView BTfoto;
    private ProgressBar pangkat;
    public static int points,pangkats;
    private static String pointuser;
    private RelativeLayout ringkasan,riwayat,bantuan,info,tpoin;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_test_fragment);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();


       /* if (user == null) {
            finish();

            Intent intent = new Intent(ProfileTestFragment.this,LoginActivity.class);
            startActivity(intent);

        }*/
        pangkat = (ProgressBar) findViewById(R.id.PBpangkat);
        TVpangkat = (TextView) findViewById(R.id.TVpangkat);
        userEmail = (TextView) findViewById(R.id.email_user);
        userEmail.setText(user.getEmail());
        userName = (TextView) findViewById(R.id.user_profile_name);
        BTfoto = (ImageView) findViewById(R.id.user_profile_photo);
        BTfoto.setOnClickListener(this);
        tpoin = (RelativeLayout) findViewById(R.id.point_user);
        tpoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileTestFragment.this, ListHadiahPoint.class));
            }
        });
        userPoint = (TextView) findViewById(R.id.TV_point);
        info = (RelativeLayout) findViewById(R.id.infoTutor);
        riwayat = (RelativeLayout) findViewById(R.id.riwayatTes);
        riwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(),NilaiActivity.class));
            }
        });

        bantuan = (RelativeLayout) findViewById(R.id.tutorial);
        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getActivity(),IntroActivity.class));
            }
        });






        /*keluar = (RelativeLayout) getActivity().findViewById(R.id.keluar);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });*/





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);








        UID = user.getUid().toString();

        DatabaseReference refpoin = mRootref.child("User").child(user.getUid()).child("point");
        refpoin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                points = dataSnapshot.getValue().hashCode();
                pointuser = String.valueOf(points);
                userPoint.setText(pointuser);
                userPoint.append(" TPoin");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference refpangkat = mRootref.child("User").child(user.getUid()).child("pangkat");
        refpangkat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pangkats = dataSnapshot.getValue().hashCode();

                if(pangkats<100){
                    TVpangkat.setText("Pendatang Baru");
                    float total = 100;
                    float aa = (pangkats/total);
                    int bb= Math.round(aa*100);
                    pangkat.setProgress(bb);}
                if (pangkats>100){
                    TVpangkat.setText("Pejuang");
                    float total = 250;
                    float aa = (pangkats/total);
                    int bb= Math.round(aa*100);
                    pangkat.setProgress(bb);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*final ProgressDialog Dialog = new ProgressDialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        Dialog.setMessage("Loading Data .... ");
        Dialog.show();*/
        DatabaseReference ref = mRootref.child("User").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("nama").getValue().toString());
                nama = dataSnapshot.child("nama").getValue().toString();
                storage = FirebaseStorage.getInstance();

                mStorageRef = storage.getReferenceFromUrl("gs://marijan-540c4.appspot.com/");
                StorageReference foto = mStorageRef.child(UID + "PP"+".jpg");

                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(foto)
                        .signature(new StringSignature(Long.toString(System.currentTimeMillis()))).centerCrop()
                        .into(BTfoto);
                //Dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(View v) {


        if (v == BTfoto) {
            System.out.println("1");
            final CharSequence[] options = {"Ambil Foto", "Pilih dari Galeri", "Kembali"};


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Ganti foto profil dari :");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Ambil Foto"))

                    {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        startActivityForResult(intent, 1);

                    } else if (options[item].equals("Pilih dari Galeri"))

                    {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, 2);


                    } else if (options[item].equals("Kembali")) {

                        dialog.dismiss();

                    }

                }

            });

            builder.show();
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BTfoto = (ImageView) findViewById(R.id.user_profile_photo);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://marijan-540c4.appspot.com//FotoProfil/");
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                Bitmap mphoto = (Bitmap) data.getExtras().get("data");
                BTfoto.setImageBitmap(mphoto);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mphoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataimage = baos.toByteArray();


                StorageReference foto = mStorageRef.child(UID +"PP"+ ".jpg");

                UploadTask uploadTask = foto.putBytes(dataimage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    }
                });
                /*final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();*/

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        //progressDialog.setMessage(((int) progress ) + "% Uploaded .... ");

                        if(progress==100){
                           // progressDialog.hide();
                        }
                    }
                })
                ;

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();


                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);

                Log.w("Gambar dari Galeri", picturePath + "");
                BTfoto.setImageBitmap(thumbnail);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentText("Foto Telah Terupload");



                StorageReference foto = mStorageRef.child(UID+"PP"+".jpg");

                UploadTask uploadTask = foto.putFile(selectedImage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Upload Sukses", Toast.LENGTH_SHORT).show();      // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    }
                });

               /*final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();*/

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        //progressDialog.setMessage(((int) progress ) + "% Uploaded .... ");

                        if(progress==100){
                            //progressDialog.hide();
                        }
                    }
                })
                ;


            }


        }
    }


}

