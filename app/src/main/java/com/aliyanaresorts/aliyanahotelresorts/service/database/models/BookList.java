package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class BookList {

    private String id;
    private String no_room;
    private String foto;
    private String id_tipe;
    private String tipe;
    private String kapasitas;
    private String harga;

    public BookList(String id, String no_room, String foto, String id_tipe, String tipe, String kapasitas, String harga) {
        this.id = id;
        this.no_room = no_room;
        this.foto = foto;
        this.id_tipe = id_tipe;
        this.tipe = tipe;
        this.kapasitas = kapasitas;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo_room() {
        return no_room;
    }

    public void setNo_room(String no_room) {
        this.no_room = no_room;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
}
