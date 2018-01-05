package com.example.prabowo.marijan;

/**
 * Created by Prabowo on 26/04/2017.
 */

public class Markerku {

    private double latitude;
    private double longitude;
    private String status;
    private String snippet;
    private String title;
    private String kode;



    public Markerku(Double latitude, Double longitude, String status, String snippet, String title, String kode){
        this.latitude = latitude;
        this.longitude=longitude;
        this.status=status;
        this.snippet = snippet;
        this.title = title;
        this.kode = kode;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getKode() {
        return kode;
    }

    public String getTitle() {
        return title;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }
}
