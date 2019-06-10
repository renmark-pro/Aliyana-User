package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class PromoList {

    private String id;
    private String judul;
    private String deskripsi;
    private String foto;


    public PromoList(String id, String judul, String deskripsi, String foto) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
