package com.xoxltn.pinjam_ajaadmin;

import java.util.Date;

public class ModelApproval {

    private String pinjaman_status;
    private Date pinjaman_tanggal_bayar;
    private int pinjaman_besar;

    public ModelApproval() {
        //empty constructor needed
    }

    public ModelApproval(String pinjaman_status, Date pinjaman_tanggal_bayar, int pinjaman_besar) {
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
