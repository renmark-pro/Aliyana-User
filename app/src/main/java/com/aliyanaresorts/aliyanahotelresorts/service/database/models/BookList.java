package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class BookList {

    private String id_tipe;
    private String tipe;
    private String foto;
    private String kapasitas;
    private String harga;
    private String jml_kamar;

    public BookList(String id_tipe, String tipe, String foto, String kapasitas, String harga, String jml_kamar) {
        this.id_tipe = id_tipe;
        this.tipe = tipe;
        this.foto = foto;
        this.kapasitas = kapasitas;
        this.harga = harga;
        this.jml_kamar = jml_kamar;
    }

    public String getId_tipe() {
        return id_tipe;
    }

    public void setId_tipe(String id_tipe) {
        this.id_tipe = id_tipe;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getJml_kamar() {
        return jml_kamar;
    }

    public void setJml_kamar(String jml_kamar) {
        this.jml_kamar = jml_kamar;
    }
}
