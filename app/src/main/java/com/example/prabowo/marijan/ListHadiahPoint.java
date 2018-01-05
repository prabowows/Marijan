package com.example.prabowo.marijan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListHadiahPoint extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();

    private ImageView IVhadiah1, IVhadiah2, IVhadiah3, IVhadiah4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hadiah_point);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        DatabaseReference ref = mRootref.child("User").child(user.getUid()).child("point");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PointActivity.points = dataSnapshot.getValue().hashCode();
//                TVpoint.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        IVhadiah1 = (ImageView) findViewById(R.id.IVhadiah1);
        IVhadiah1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHadiahPoint.this, PointActivity.class);
                intent.putExtra("Point", "1500");
                intent.putExtra("Hadiah", "hood");
                startActivity(intent);
            }
        });

        IVhadiah2 = (ImageView) findViewById(R.id.IVhadiah2);
        IVhadiah2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHadiahPoint.this, PointActivity.class);
                intent.putExtra("Point", "1400");
                intent.putExtra("Hadiah", "mouse");
                startActivity(intent);

    }
});

        IVhadiah3 = (ImageView) findViewById(R.id.IVhadiah3);
        IVhadiah3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHadiahPoint.this, PointActivity.class);
                intent.putExtra("Point", "1300");
                intent.putExtra("Hadiah", "shirt");
                startActivity(intent);

            }
        });

        IVhadiah4 = (ImageView) findViewById(R.id.IVhadiah4);
        IVhadiah4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHadiahPoint.this, PointActivity.class);
                intent.putExtra("Point", "1200");
                intent.putExtra("Hadiah", "fd");
                startActivity(intent);

            }
        });



    }


}


