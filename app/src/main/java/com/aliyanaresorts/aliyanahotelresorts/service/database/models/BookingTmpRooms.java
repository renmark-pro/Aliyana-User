package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class BookingTmpRooms {
    private String idTemp;
    private int posisi;

    public BookingTmpRooms(String idTemp, int posisi) {
        this.idTemp = idTemp;
        this.posisi = posisi;
    }

    public String getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(String idTemp) {
        this.idTemp = idTemp;
    }

    public int getPosisi() {
        return posisi;
    }

    public void setPosisi(int posisi) {
        this.posisi = posisi;
    }
}
