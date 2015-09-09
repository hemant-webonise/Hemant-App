package com.example.webonise.blooddonation.model;

import java.util.List;

/**
 * Created by webonise on 7/9/15.
 */
public class Donor extends Response{

    private List<DataEntity> data;

    public Donor(List<DataEntity> data) {
        this.data = data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * id : 1234
         * name : Sandeep Rathore
         * phone : +918149325524
         * email : sandeep.rathore@weboniselab.com
         * lat : 18.540815
         * lng : 73.778901
         */

        private int id;
        private String name;
        private String phone;
        private String email;
        private double lat;
        private double lng;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}