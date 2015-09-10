package com.example.webonise.blooddonation.model;

/**
 * Created by webonise on 10/9/15.
 */
public class History {
    public static final int ID = 0;
    private int id;
    private String Location;
    private String Date;
    private String Image;

    public static int getId() {
        return ID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public History(int id, String location, String date, String image) {

        this.id = id;
        Location = location;
        Date = date;
        Image = image;
    }

    public History() {
    }
}
