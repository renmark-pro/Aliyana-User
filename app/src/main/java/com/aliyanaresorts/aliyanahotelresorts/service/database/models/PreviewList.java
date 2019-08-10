package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class PreviewList {

    private  String id, id_tipe, subtotal, kapasitas, harga, tipe, jml_kamar;

    public PreviewList(String id, String id_tipe, String subtotal, String kapasitas, String harga, String tipe, String jml_kamar) {
        this.id = id;
        this.id_tipe = id_tipe;
        this.subtotal = subtotal;
        this.kapasitas = kapasitas;
        this.harga = harga;
        this.tipe = tipe;
        this.jml_kamar = jml_kamar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_tipe() {
        return id_tipe;
    }

    public void setId_tipe(String id_tipe) {
        this.id_tipe = id_tipe;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
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

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getJml_kamar() {
        return jml_kamar;
    }

    public void setJml_kamar(String jml_kamar) {
        this.jml_kamar = jml_kamar;
    }
}
