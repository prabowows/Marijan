package com.example.prabowo.marijan;

/**
 * Created by Prabowo on 04/05/2017.
 */

public class Pesanan {
    String nama;
    String alamat;
    String nohp;
    String uid;


    public Pesanan(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Pesanan(String nama, String alamat, String nohp, String uid){
        this.nama=nama;
        this.alamat=alamat;
        this.nohp=nohp;
        this.uid=uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}
