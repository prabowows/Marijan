package com.example.prabowo.marijan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Pesan extends AppCompatActivity implements View.OnClickListener {
    private TextView TVinfotoko,TVsuasanatoko;
    private FirebaseAuth firebaseAuth;
    private ImageView IVhasilsuasana;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference;
    private Button BTpesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);
        TVinfotoko = (TextView) findViewById(R.id.TVinfotoko);
        TVsuasanatoko = (TextView) findViewById(R.id.TVstatustoko);
        IVhasilsuasana = (ImageView) findViewById(R.id.IVhasilsuasana);
        storage = FirebaseStorage.getInstance();
        BTpesan = (Button) findViewById(R.id.BTpesan);
        BTpesan.setOnClickListener(this);

        mStorageRef = storage.getReferenceFromUrl("gs://marijan-540c4.appspot.com/");


        Bundle extras = getIntent().getExtras();
        final String kodetoko = extras.getString("kodetoko");

        DatabaseReference ref = mRootref.child("Marker");
        firebaseAuth = FirebaseAuth.getInstance();        final FirebaseUser user = firebaseAuth.getCurrentUser();


        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TVinfotoko.setText("Selamat Datang "+ snapshot.child("Marker"+kodetoko).child("title").getValue().toString());
                TVsuasanatoko.setText(snapshot.child("Marker"+kodetoko).child("snippet").getValue().toString());
                String fotoh = snapshot.child("Marker"+kodetoko).child("foto").getValue().toString();
                StorageReference foto = mStorageRef.child(fotoh+".jpg");
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(foto)
                        .into(IVhasilsuasana);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }

    @Override
    public void onClick(View v) {

        Bundle extras = getIntent().getExtras();
        final String kodetoko = extras.getString("kodetoko");

        final DatabaseReference ref = mRootref.child("Marker");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if(v==BTpesan){
            ref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.child("Marker"+kodetoko).child("pelanggan").child(user.getUid()).getValue().toString().equals("1"))
                    {Toast.makeText(Pesan.this,"Anda Sudah Pesan",Toast.LENGTH_SHORT).show();}
                    else if (snapshot.child("Marker"+kodetoko).child("pelanggan").child(user.getUid()).getValue().toString().equals("0")){
                        ref.child("Marker" + kodetoko).child("pelanggan").child(user.getUid()).setValue("1");
                        Pesanan pesanan = new Pesanan ("Ini nama","ini alamat", "ini nomor hp",user.getUid());
                        ref.child("Marker" + kodetoko).child("pesanan").push().setValue(pesanan);
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        }

    }
}
