package com.xoxltn.pinjam_ajaadmin;

import java.util.Date;

public class ModelReturn {

    private int pinjaman_tahap;
    private Date pinjaman_tanggal_transfer;
    private int pinjaman_besar;

    public ModelReturn() {
        //empty constructor needed
    }

    public ModelReturn(int pinjaman_tahap, Date pinjaman_tanggal_transfer, int pinjaman_besar) {
        this.pinjaman_tahap = pinjaman_tahap;
        this.pinjaman_tanggal_transfer = pinjaman_tanggal_transfer;
        this.pinjaman_besar = pinjaman_besar;
    }

    public int getPinjaman_tahap() {
        return pinjaman_tahap;
    }

    public Date getPinjaman_tanggal_transfer() {
        return pinjaman_tanggal_transfer;
    }

    public int getPinjaman_besar() {
        return pinjaman_besar;
    }
}

