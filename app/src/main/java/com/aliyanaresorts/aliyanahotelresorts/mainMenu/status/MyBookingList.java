package com.aliyanaresorts.aliyanahotelresorts.mainMenu.status;

public class MyBookingList {

    private String id, kode_booking, tipe, jml_kamar, tgl_checkin, tgl_checkout, total_tagihan,
            terbayarkan, kekurangan, status;

    public MyBookingList(String id, String kode_booking, String tipe, String jml_kamar, String tgl_checkin, String tgl_checkout, String total_tagihan, String terbayarkan, String kekurangan, String status) {
        this.id = id;
        this.kode_booking = kode_booking;
        this.tipe = tipe;
        this.jml_kamar = jml_kamar;
        this.tgl_checkin = tgl_checkin;
        this.tgl_checkout = tgl_checkout;
        this.total_tagihan = total_tagihan;
        this.terbayarkan = terbayarkan;
        this.kekurangan = kekurangan;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode_booking() {
        return kode_booking;
    }

    public void setKode_booking(String kode_booking) {
        this.kode_booking = kode_booking;
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

    public String getTgl_checkin() {
        return tgl_checkin;
    }

    public void setTgl_checkin(String tgl_checkin) {
        this.tgl_checkin = tgl_checkin;
    }

    public String getTgl_checkout() {
        return tgl_checkout;
    }

    public void setTgl_chekout(String tgl_checkout) {
        this.tgl_checkout = tgl_checkout;
    }

    public String getTotal_tagihan() {
        return total_tagihan;
    }

    public void setTotal_tagihan(String total_tagihan) {
        this.total_tagihan = total_tagihan;
    }

    public String getTerbayarkan() {
        return terbayarkan;
    }

    public void setTerbayarkan(String terbayarkan) {
        this.terbayarkan = terbayarkan;
    }

    public String getKekurangan() {
        return kekurangan;
    }

    public void setKekurangan(String kekurangan) {
        this.kekurangan = kekurangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
