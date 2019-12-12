package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.info.resto;

public class RestoList {

    private String id, menu, nama, harga, catatan, foto;

    public RestoList(String id, String menu, String nama, String harga, String catatan, String foto) {
        this.id = id;
        this.menu = menu;
        this.nama = nama;
        this.harga = harga;
        this.catatan = catatan;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
