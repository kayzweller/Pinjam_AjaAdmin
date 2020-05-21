package com.xoxltn.pinjam_ajaadmin.model;

import java.util.Date;

public class Approval {

    private String pinjaman_status;
    private Date pinjaman_tanggal_bayar;
    private int pinjaman_besar;

    public Approval() {
        //empty constructor needed
    }

    public Approval(String pinjaman_status, Date pinjaman_tanggal_bayar, int pinjaman_besar) {
        this.pinjaman_status = pinjaman_status;
        this.pinjaman_tanggal_bayar = pinjaman_tanggal_bayar;
        this.pinjaman_besar = pinjaman_besar;
    }

    public String getPinjaman_status() {
        return pinjaman_status;
    }

    public Date getPinjaman_tanggal_bayar() {
        return pinjaman_tanggal_bayar;
    }

    public int getPinjaman_besar() {
        return pinjaman_besar;
    }
}
