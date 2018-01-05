package com.example.prabowo.marijan;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseReference;
    private List<Markerku> mListItemMarker = new ArrayList<>();
    private Location mLastLocation;
    private Button BTtoko;
    private FirebaseAuth firebaseAuth;
    LocationManager locationManager;
    LocationListener locationListener;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        DatabaseReference ref = mRootref.child("User");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if ((snapshot.child(user.getUid()).child("status").getValue().equals("ada"))){


                    String alert1 = "Pesanan Anda Telah Selesai";
                    String alert2 = "Klik OKAY Untuk Mengakhiri";

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(alert1 +"\n"+"\n" + alert2);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OKAY",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mRootref.child("User").child(user.getUid()).child("status").setValue("0");;
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                    //Di bawah ini buat notif
                    notification.setSmallIcon(R.mipmap.ic_launcher);
                    notification.setTicker("This is the ticker");
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("Here is the title");
                    notification.setContentText("I am the body text of your notification");

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    //Builds notification and issues it
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(uniqueID, notification.build());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });





        BTtoko= (Button) findViewById(R.id.BTtoko);
        BTtoko.setOnClickListener(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        //DatabaseReference ref = mRootref.child("User");
        firebaseAuth = FirebaseAuth.getInstance();
        //final FirebaseUser user = firebaseAuth.getCurrentUser();

        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     *
     */

    public Marker createMarker(double latitude, double longitude, String title, String snippet) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mmanbaru)));


    }

    private int getCategoryPos(String category) {
        return mListItemMarker.indexOf(category);
    }

    @Override
    public void onConnected(Bundle connectionHint) {



        DatabaseReference ref = mRootref.child("User");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if ((snapshot.child(user.getUid()).child("kodetoko").getValue().equals("0"))){
                    BTtoko.setVisibility(View.GONE);
                }
                if ((snapshot.child(user.getUid()).child("status").getValue().equals("1"))){
                    notification.setSmallIcon(R.mipmap.ic_launcher);
                    notification.setTicker("This is the ticker");
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("Here is the title");
                    notification.setContentText("I am the body text of your notification");

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    //Builds notification and issues it
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(uniqueID, notification.build());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));



            ref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    //LatLng userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                    if(mLastLocation!=null){
                        mRootref.child("Marker").child("Marker"+snapshot.child(user.getUid()).child("kodetoko").getValue().toString()).child("longitude").setValue(mLastLocation.getLongitude());
                        mRootref.child("Marker").child("Marker"+snapshot.child(user.getUid()).child("kodetoko").getValue().toString()).child("latitude").setValue(mLastLocation.getLatitude());
                        mRootref.child("Marker").child("Marker"+snapshot.child(user.getUid()).child("kodetoko").getValue().toString()).child("status").setValue("buka");}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {


                    for(int i = 0 ; i < mListItemMarker.size() ; i++ ) {

                        if(mListItemMarker.get(i).getTitle().equals(marker.getTitle())){
                            Intent intent = new Intent(getApplicationContext(), Pesan.class);
                            intent.putExtra("kodetoko", mListItemMarker.get(i).getKode());
                            startActivity(intent);
                        }

                    }


                    /*String alert1 = "SELAMAT ANDA MENDAPAT ";
                    String alert2 = "Kode hadiah: ";
                    //String alert3 = "Kode user: " + user.getUid().toString();
                    String alert4 = "Isi IDENTITAS ANDA dan BUKTI ini, dengan klik KIRIM";

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage(alert1 + "\n" + "\n" + alert2 + "\n" + "\n" + alert4);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();*/

                }
            });

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            } else {
                mMap.setMyLocationEnabled(true);
            }


            //mMap.addMarker(new MarkerOptions().position(klebengan).title("Marker in kost").snippet("Huweeeee").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.clear();

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( klebengan, 18));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        final int filter = 1;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                DatabaseReference ref = mRootref.child("User");
                firebaseAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                ref.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        //LatLng userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                        if(mLastLocation!=null){
                            mRootref.child("Marker").child("Marker"+snapshot.child(user.getUid()).child("kodetoko").getValue().toString()).child("longitude").setValue(mLastLocation.getLongitude());
                            mRootref.child("Marker").child("Marker"+snapshot.child(user.getUid()).child("kodetoko").getValue().toString()).child("latitude").setValue(mLastLocation.getLatitude());
                            mRootref.child("Marker").child("Marker"+snapshot.child(user.getUid()).child("kodetoko").getValue().toString()).child("status").setValue("buka");}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });






        DatabaseReference ref = mRootref.child("Marker");

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    mListItemMarker.add(new Markerku
                            (Double.parseDouble(postSnapshot.child("latitude").getValue().toString()),
                                    Double.parseDouble(postSnapshot.child("longitude").getValue().toString()),
                                    postSnapshot.child("status").getValue().toString(),
                                    postSnapshot.child("snippet").getValue().toString(),
                                    postSnapshot.child("title").getValue().toString(),
                                    postSnapshot.child("kode").getValue().toString()));

                }
                for(int i = 0 ; i < mListItemMarker.size() ; i++ ) {

                    createMarker(mListItemMarker.get(i).getLatitude(), mListItemMarker.get(i).getLongitude(), mListItemMarker.get(i).getTitle(), mListItemMarker.get(i).getSnippet());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        for(int i = 0 ; i < mListItemMarker.size() ; i++ ) {

            createMarker(mListItemMarker.get(i).getLatitude(), mListItemMarker.get(i).getLongitude(), mListItemMarker.get(i).getTitle(), mListItemMarker.get(i).getSnippet());

        }


        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-33.86997, 151.208);
        LatLng sydney2 = new LatLng(-33.86997, 151.207);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").snippet("Huweeeee").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Solo").snippet("Huweeeee").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.addPolyline(new PolylineOptions().geodesic(true).add(sydney).add(sydney2)); //path*/
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney),18);

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {

        if(v==BTtoko){

            DatabaseReference ref = mRootref.child("User");
            firebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = firebaseAuth.getCurrentUser();

            ref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Intent intent = new Intent(MainActivity.this,IsiPenjual.class);
                    intent.putExtra("kode",snapshot.child(user.getUid()).child("kodetoko").getValue().toString());
                    startActivity(intent);



                   /* Intent intent = new Intent(MainActivity.this,ProfileTestFragment.class);
                    startActivity(intent);*/




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

        }

    }

    @Override
    public void onResume(){
        super.onResume();

        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();

            // add markers from database to the map
        }}

    public boolean onMarkerClick(Marker marker) {
        for(int i = 0 ; i < mListItemMarker.size() ; i++ ) {

            if(mListItemMarker.get(i).getTitle().equals(marker.getTitle())){
                Intent intent = new Intent(this, IsiPenjual.class);
                intent.putExtra("kodetoko", mListItemMarker.get(i).getKode());
                startActivity(intent);
            }

        }
        Toast.makeText(this,marker.getTitle(),Toast.LENGTH_LONG).show();

        return false;
    }

}
