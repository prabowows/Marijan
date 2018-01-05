package com.example.prabowo.marijan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.Random;
import java.util.UUID;
import com.firebase.ui.database.FirebaseListAdapter;

public class IsiPenjual extends AppCompatActivity implements View.OnClickListener {
    private TextView TVcoba;
    private Button BTupload,BTbuka,BTpromo,BTtutup,BTtelfon;
    private EditText ETpesan;
    private ImageView IVsuasana;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    private ListView listView;
    private FirebaseListAdapter<Pesanan> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_penjual);
        BTbuka = (Button) findViewById(R.id.BTbuka) ;
        BTbuka.setOnClickListener(this);
        BTtutup = (Button) findViewById(R.id.BTtutup) ;
        BTtutup.setOnClickListener(this);
        BTpromo = (Button) findViewById(R.id.BTpromo) ;
        BTpromo.setOnClickListener(this);


        IVsuasana = (ImageView) findViewById(R.id.IVsuasana);
        IVsuasana.setOnClickListener(this);

        TVcoba = (TextView) findViewById(R.id.TVcoba);
        BTupload = (Button) findViewById(R.id.BTupload);
        BTupload.setOnClickListener(this);
        ETpesan = (EditText) findViewById(R.id.ETstatus);




        Bundle extras = getIntent().getExtras();
        final String kode = extras.getString("kode");

        DatabaseReference ref = mRootref.child("Marker");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();



//INI BUAT LIST VIEW PESANAN
        listView = (ListView) findViewById(R.id.LVpesanantoko);
        DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("Marker").child("Marker" + kode).child("pesanan");
        mAdapter = new FirebaseListAdapter<Pesanan>(IsiPenjual.this,Pesanan.class,android.R.layout.two_line_list_item,mRef)/* Class sekarang, class dipake, layout bawaan,data yang diambil*/ {
            @Override
            protected void populateView(View v, Pesanan model, final int position) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Ini buat button telfon
                        /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setData(Uri.parse("sms:" + mAdapter.getItem(position).getNohp()));
                        startActivity(sendIntent);
                        Toast.makeText(IsiPenjual.this, mAdapter.getItem(position).getNama(), Toast.LENGTH_SHORT).show();*/

                        mRootref.child("User").child(user.getUid()).child("status").setValue("ada");
                        mRootref.child("Marker").child("Marker" + kode).child("pelanggan").child(user.getUid()).setValue("0");

                    }
                });
                ((TextView) v.findViewById(android.R.id.text1)).setText(model.getNama());
                ((TextView) v.findViewById(android.R.id.text2)).setText(model.getAlamat());




            }
        };
        listView.setAdapter(mAdapter);




        mStorageRef = storage.getReferenceFromUrl("gs://marijan-540c4.appspot.com/");


        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {


                String fotoh = snapshot.child("Marker"+kode).child("foto").getValue().toString();
                Log.d("test",fotoh);
                StorageReference foto = mStorageRef.child(fotoh+".jpg");
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(foto)
                        .into(IVsuasana);


                TVcoba.setText("Selamat Datang " + snapshot.child("Marker"+kode).child("title").getValue().toString());
                if(snapshot.child("Marker"+kode).child("status").getValue().toString().equals("1")){
                    BTbuka.setBackgroundColor(Color.parseColor("#21848e"));
                }
                if(snapshot.child("Marker"+kode).child("status").getValue().toString().equals("2")){
                    BTpromo.setBackgroundColor(Color.parseColor("#21848e"));
                }
                if(snapshot.child("Marker"+kode).child("status").getValue().toString().equals("3")){
                    BTtutup.setBackgroundColor(Color.parseColor("#21848e"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }

    @Override
    public void onClick(View v) {

        Bundle extras = getIntent().getExtras();
        final String kode = extras.getString("kode");

        if (v == BTupload) {
            mRootref.child("Marker").child("Marker" + kode).child("snippet").setValue(ETpesan.getText().toString());


            String alert1 = "Suasana Cafe Telah Diperbarui";


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(alert1);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class); //first
                            startActivity(intent);
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();


        }

        if (v == BTbuka){

            mRootref.child("Marker").child("Marker" + kode).child("status").setValue("1");
            Toast.makeText(this,"Buka",Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        if (v == BTpromo){

            mRootref.child("Marker").child("Marker" + kode).child("status").setValue("2");
            Toast.makeText(this,"Mode Promo",Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        if (v == BTtutup){
            mRootref.child("Marker").child("Marker" + kode).child("status").setValue("3");
            Toast.makeText(this,"Tutup",Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        if (v == IVsuasana) {
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
        IVsuasana = (ImageView) findViewById(R.id.IVsuasana);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://marijan-540c4.appspot.com/");
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                Bitmap mphoto = (Bitmap) data.getExtras().get("data");
                IVsuasana.setImageBitmap(mphoto);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mphoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataimage = baos.toByteArray();


                final String uuid = UUID.randomUUID().toString();

                Bundle extras = getIntent().getExtras();
                final String kode = extras.getString("kode");

                mRootref.child("Marker").child("Marker" + kode).child("foto").setValue(uuid);
                StorageReference foto = mStorageRef.child(uuid+".jpg");

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
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage(((int) progress) + "% Uploaded .... ");

                        if (progress == 100) {
                            progressDialog.hide();
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
                IVsuasana.setImageBitmap(thumbnail);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentText("Foto Telah Terupload");

                final String uuid = UUID.randomUUID().toString();

                Bundle extras = getIntent().getExtras();
                final String kode = extras.getString("kode");


                StorageReference foto = mStorageRef.child(uuid+".jpg");


                mRootref.child("Marker").child("Marker" + kode).child("foto").setValue(uuid);


                UploadTask uploadTask = foto.putFile(selectedImage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Upload Sukses", Toast.LENGTH_SHORT).show();      // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    }
                });

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage(((int) progress) + "% Uploaded .... ");

                        if (progress == 100) {
                            progressDialog.hide();
                        }
                    }
                })
                ;


            }
        }
    }
}
