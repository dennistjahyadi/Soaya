package com.dennistjahyadigotama.soaya.activities.input_penjadwalan.adapter;

/**
 * Created by Denn on 10/2/2016.
 */
public class PenjadwalanGetter {

    String id, tanggal1, tanggal2, event, organisasi, color;


    public String getOrganisasi() {
        return organisasi;
    }

    public void setOrganisasi(String organisasi) {
        this.organisasi = organisasi;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTanggal1() {
        return tanggal1;
    }

    public void setTanggal1(String tanggal1) {
        this.tanggal1 = tanggal1;
    }

    public String getTanggal2() {
        return tanggal2;
    }

    public void setTanggal2(String tanggal2) {
        this.tanggal2 = tanggal2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
