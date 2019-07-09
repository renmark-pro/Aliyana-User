package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class ProsesDetailList {

    private  String kode_booking, no_room, tgl_checkin, tgl_checkout ,jml_tamu, tipe;

    public ProsesDetailList(String kode_booking, String no_room, String tgl_checkin, String tgl_checkout, String jml_tamu, String tipe) {
        this.kode_booking = kode_booking;
        this.no_room = no_room;
        this.tgl_checkin = tgl_checkin;
        this.tgl_checkout = tgl_checkout;
        this.jml_tamu = jml_tamu;
        this.tipe = tipe;
    }

    public String getKode_booking() {
        return kode_booking;
    }

    public void setKode_booking(String kode_booking) {
        this.kode_booking = kode_booking;
    }

    public String getNo_room() {
        return no_room;
    }

    public void setNo_room(String no_room) {
        this.no_room = no_room;
    }

    public String getTgl_checkin() {
        return tgl_checkin;
    }

    public void setTgl_checkin(String tgl_checkin) {
        this.tgl_checkin = tgl_checkin;
    }

    public String getTgl_checkout() {
        return tgl_checkout;
    }

    public void setTgl_checkout(String tgl_checkout) {
        this.tgl_checkout = tgl_checkout;
    }

    public String getJml_tamu() {
        return jml_tamu;
    }

    public void setJml_tamu(String jml_tamu) {
        this.jml_tamu = jml_tamu;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}
