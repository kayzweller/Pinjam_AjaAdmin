package com.xoxltn.pinjam_ajaadmin.model;

import java.util.Date;

public class Request {

    private Date pinjaman_tanggal_req;
    private int pinjaman_besar;

    public Request() {
        //empty constructor needed
    }

    public Request(Date pinjaman_tanggal_req, int pinjaman_besar) {
        this.pinjaman_tanggal_req = pinjaman_tanggal_req;
        this.pinjaman_besar = pinjaman_besar;
    }

    public Date getPinjaman_tanggal_req() {
        return pinjaman_tanggal_req;
    }

    public int getPinjaman_besar() {
        return pinjaman_besar;
    }
}
