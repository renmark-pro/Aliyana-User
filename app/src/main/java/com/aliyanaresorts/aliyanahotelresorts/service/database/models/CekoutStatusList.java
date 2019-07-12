package com.aliyanaresorts.aliyanahotelresorts.service.database.models;

public class CekoutStatusList {

    private String id, kode_booking, jml_kamar, total, tgl_checkin, tgl_checkout, status;

    public CekoutStatusList(String id, String kode_booking, String jml_kamar, String total, String tgl_checkin, String tgl_checkout, String status) {
        this.id = id;
        this.kode_booking = kode_booking;
        this.jml_kamar = jml_kamar;
        this.total = total;
        this.tgl_checkin = tgl_checkin;
        this.tgl_checkout = tgl_checkout;
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

    public String getJml_kamar() {
        return jml_kamar;
    }

    public void setJml_kamar(String jml_kamar) {
        this.jml_kamar = jml_kamar;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
