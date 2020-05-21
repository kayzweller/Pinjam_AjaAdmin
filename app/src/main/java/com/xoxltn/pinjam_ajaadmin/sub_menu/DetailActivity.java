package com.xoxltn.pinjam_ajaadmin.sub_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xoxltn.pinjam_ajaadmin.R;
import com.xoxltn.pinjam_ajaadmin.view_fragment.ApprovalFragment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {

    static final String SEND_ID_PEMINJAM = "com.xoxltn.pinjam_ajaadmin.SEND_ID_PEMINJAM";
    static final String SEND_ID_PENDANA = "com.xoxltn.pinjam_ajaadmin.SEND_ID_PENDANA";

    private NumberFormat formatter;

    private String mIDPeminjam;
    private String mIDPendana;

    private String mDocName, mDocPath;
    private DocumentReference mDocRef;

    private TextView text_id_pinjaman, text_tgl_pendanaan, text_status, text_tahap, text_cicil_1,
            text_cicil_2, text_cicil_3, text_sisa_tenor, text_besar_pinjaman;
    private TextView text_total_kembali, text_denda, text_terbayar;

    private int tahap, besar_pinjaman, denda, terbayar;
    private String status;
    private Date tgl_pendanaan, cicil_1, cicil_2, cicil_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FormatRupiah(); //FORMATTING INT TO RUPIAH //

        // calling extra
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            mDocName = extras.getString(ApprovalFragment.SEND_DOCUMENT_DOC);
            mDocPath = extras.getString(ApprovalFragment.SEND_DOCUMENT_PATH);
        }

        // firestore setup
        FirebaseFirestore fire = FirebaseFirestore.getInstance();
        mDocRef = fire.document(mDocPath);

        // hooks
        text_id_pinjaman = findViewById(R.id.text_id_pinjaman);
        text_tgl_pendanaan = findViewById(R.id.text_tgl_pendanaan);
        text_status = findViewById(R.id.text_status);
        text_tahap = findViewById(R.id.text_tahap);
        text_cicil_1 = findViewById(R.id.text_cicil_1);
        text_cicil_2 = findViewById(R.id.text_cicil_2);
        text_cicil_3 = findViewById(R.id.text_cicil_3);
        text_sisa_tenor = findViewById(R.id.text_sisa_tenor);
        text_besar_pinjaman = findViewById(R.id.text_besar_pinjaman);
        text_total_kembali = findViewById(R.id.text_total_kembali);
        text_denda = findViewById(R.id.text_denda);
        text_terbayar = findViewById(R.id.text_terbayar);

        getSetData();
    }

    //-------------------------------------------------------------------------------------------//

    private void getSetData() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc =  Objects.requireNonNull(task.getResult());

                    // get
                    mIDPeminjam = doc.getString("id_peminjam");
                    mIDPendana = doc.getString("id_pendana");

                    tgl_pendanaan = doc.getDate("pinjaman_tanggal_req");
                    cicil_1 = doc.getDate("pinjaman_tanggal_bayar_1");
                    cicil_2 = doc.getDate("pinjaman_tanggal_bayar_2");
                    cicil_3 = doc.getDate("pinjaman_tanggal_bayar_3");

                    status = doc.getString("pinjaman_status");

                    Long c_tahap = doc.getLong("pinjaman_tahap");
                    assert c_tahap != null;
                    tahap = Math.toIntExact(c_tahap);

                    Long c_pinjaman = doc.getLong("pinjaman_besar");
                    assert c_pinjaman != null;
                    besar_pinjaman = Math.toIntExact(c_pinjaman);

                    Long c_denda = doc.getLong("pinjaman_denda_total");
                    assert c_denda != null;
                    denda = Math.toIntExact(c_denda);

                    Long c_terbayar = doc.getLong("pinjaman_terbayar");
                    assert c_terbayar != null;
                    terbayar = Math.toIntExact(c_terbayar);

                    // call setTextView
                    setTextView();
                }
            }
        });
    }

    private void setTextView() {

        // id pinjaman
        text_id_pinjaman.setText(mDocName);

        // tanggal pendanaan
        text_tgl_pendanaan.setText(DateFormat.getDateInstance(DateFormat.FULL)
                .format(tgl_pendanaan));

        // status pinjaman
        text_status.setText(status);

        // tahap pinjaman
        if (tahap == 0) {
            String set = "--";
            text_tahap.setText(set);
        } else if (tahap == 1) {
            String set = "Cicilan Pertama";
            text_tahap.setText(set);
        } else if (tahap == 2) {
            String set = "Cicilan Kedua";
            text_tahap.setText(set);
        } else if (tahap == 3) {
            String set = "Cicilan Terakhir";
            text_tahap.setText(set);
        }

        // tanggal cicilan pertama s/d terakhir
        text_cicil_1.setText(DateFormat.getDateInstance(DateFormat.FULL)
                .format(cicil_1));
        text_cicil_2.setText(DateFormat.getDateInstance(DateFormat.FULL)
                .format(cicil_2));
        text_cicil_3.setText(DateFormat.getDateInstance(DateFormat.FULL)
                .format(cicil_3));

        // masa tenor
        Date currentTime = Calendar.getInstance().getTime();
        long diff = cicil_3.getTime() - currentTime.getTime();
        long sisa_tenor = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        String l_sisa_tenor = sisa_tenor + " Hari";
        text_sisa_tenor.setText(l_sisa_tenor); //sisa waktu pinjaman;

        // besar pinjaman
        switch (besar_pinjaman) {
            case 500000:
                text_besar_pinjaman.setText(R.string.pinjam_500k);
                String load_1 = "Rp. 600.000,-";
                text_total_kembali.setText(load_1);
                break;
            case 1000000:
                text_besar_pinjaman.setText(R.string.pinjam_1000k);
                String load_2 = "Rp. 1.200.000,-";
                text_total_kembali.setText(load_2);
                break;
            case 1500000:
                text_besar_pinjaman.setText(R.string.pinjam_1500k);
                String load_3 = "Rp.1.800.000,-";
                text_total_kembali.setText(load_3);
                break;
        }

        // akumulasi denda
        String l_denda = formatter.format(denda);
        text_denda.setText(l_denda);

        // pinjaman sudah terbayar
        String l_terbayar = formatter.format(terbayar);
        text_terbayar.setText(l_terbayar);
    }

    //-------------------------------------------------------------------------------------------//

    public void onClickDataPeminjam(View view) {
        Intent dataPeminjam = new Intent(this, DetailPeminjamActivity.class);
        dataPeminjam.putExtra(SEND_ID_PEMINJAM, mIDPeminjam);
        startActivity(dataPeminjam);
    }

    public void onClickDataPendana(View view) {
        Intent dataPendana = new Intent(this, DetailPendanaActivity.class);
        dataPendana.putExtra(SEND_ID_PENDANA, mIDPendana);
        startActivity(dataPendana);
    }

    //-------------------------------------------------------------------------------------------//

    private void FormatRupiah() {
        formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(0);
        formatter.setCurrency(Currency.getInstance("IDR"));
    }

    //-------------------------------------------------------------------------------------------//

    public void buttonBackArrow (View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
