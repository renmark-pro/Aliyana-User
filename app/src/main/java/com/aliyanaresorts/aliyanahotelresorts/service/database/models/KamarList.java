package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class KamarList {

    private String id;
    private String tipe;
    private String harga;
    private String lokasi;

    public KamarList(String id, String tipe, String harga, String lokasi) {
        this.id = id;
        this.tipe = tipe;
        this.harga = harga;
        this.lokasi = lokasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}