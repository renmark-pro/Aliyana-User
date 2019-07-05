package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class PreviewList {

    private  String no_room, tipe, kapasitas, harga, jml_tamu;

    public PreviewList(String no_room, String tipe, String kapasitas, String harga, String jml_tamu) {
        this.no_room = no_room;
        this.tipe = tipe;
        this.kapasitas = kapasitas;
        this.harga = harga;
        this.jml_tamu = jml_tamu;
    }

    public String getNo_room() {
        return no_room;
    }

    public void setNo_room(String no_room) {
        this.no_room = no_room;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJml_tamu() {
        return jml_tamu;
    }

    public void setJml_tamu(String jml_tamu) {
        this.jml_tamu = jml_tamu;
    }
}
