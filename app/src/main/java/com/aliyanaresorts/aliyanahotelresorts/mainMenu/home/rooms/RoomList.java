package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.rooms;

public class RoomList {

    private String id;
    private String tipe;
    private String harga;
    private String kapasitas;
    private String lokasi;

    public RoomList(String id, String tipe, String harga, String kapasitas, String lokasi) {
        this.id = id;
        this.tipe = tipe;
        this.harga = harga;
        this.kapasitas = kapasitas;
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

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}