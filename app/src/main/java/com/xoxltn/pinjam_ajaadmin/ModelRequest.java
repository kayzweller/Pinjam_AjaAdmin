package com.xoxltn.pinjam_ajaadmin;

import java.util.Date;

public class ModelRequest {

    private Date pinjaman_tanggal_req;
    private int pinjaman_besar;

    public ModelRequest() {
        //empty constructor needed
    }

    public ModelRequest(Date pinjaman_tanggal_req, int pinjaman_besar) {
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
