package com.xoxltn.pinjam_ajaadmin.sub_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xoxltn.pinjam_ajaadmin.R;
import com.xoxltn.pinjam_ajaadmin.view_fragment.ReturnFragment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;

public class ReturnActivity extends AppCompatActivity {

    private NumberFormat formatter;


    private String mDocName, mDocPath;
    private DocumentReference mDocRef;

    private TextView mTextIDPinjaman, mTextBesarPinjaman, mTextTotalKembali, mTextTglPendanaan,
            mTextTahapPinjaman, mTextTglJatuhTempo, mTextTglTransfer;
    private TextView mTextNamaPendana, mTextIDPendana, mTextNamaPeminjam, mTextIDPeminjam;
    private TextView mTextHolderPeminjam, mTextRekPeminjam, mTextNominalTransfer,
            mTextPokok, mTextDenda;
    private TextView mTextTotalTerbayar, mTextHolderPendana, mTextRekPendana, mTextTransferKePendana;

    private String mIDPeminjam;
    private String mIDPendana;
    private int mTahap, mTransfer, mTerbayar;

    private Date mTempo1, mTempo2;

    //-------------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        // formatting the number
        FormatRupiah();

        // calling extra
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            mDocName = extras.getString(ReturnFragment.SEND_DOCUMENT_DOC);
            mDocPath = extras.getString(ReturnFragment.SEND_DOCUMENT_PATH);
        }

        // firestore setup
        FirebaseFirestore fire = FirebaseFirestore.getInstance();
        mDocRef = fire.document(mDocPath);

        // hooks
        mTextIDPinjaman = findViewById(R.id.text_id_pinjaman);
        mTextBesarPinjaman = findViewById(R.id.text_besar_pinjaman);
        mTextTotalKembali = findViewById(R.id.text_total_kembali);
        mTextTglPendanaan = findViewById(R.id.text_tgl_pendanaan);
        mTextTahapPinjaman = findViewById(R.id.text_tahap_pinjaman);
        mTextTglJatuhTempo = findViewById(R.id.text_tgl_tempo);
        mTextTglTransfer = findViewById(R.id.text_tgl_transfer);
        mTextNamaPendana = findViewById(R.id.text_nama_pendana);
        mTextIDPendana = findViewById(R.id.text_id_pendana);
        mTextNamaPeminjam = findViewById(R.id.text_nama_peminjam);
        mTextIDPeminjam = findViewById(R.id.text_id_peminjam);
        mTextTotalTerbayar = findViewById(R.id.text_total_terbayar);
        mTextHolderPeminjam = findViewById(R.id.text_holder_peminjam);
        mTextRekPeminjam = findViewById(R.id.text_rek_peminjam);
        mTextNominalTransfer = findViewById(R.id.text_nominal_transfer);
        mTextPokok = findViewById(R.id.text_pokok_cicilan);
        mTextDenda = findViewById(R.id.text_denda);
        mTextHolderPendana = findViewById(R.id.text_holder_pendana);
        mTextRekPendana = findViewById(R.id.text_rek_pendana);
        mTextTransferKePendana = findViewById(R.id.text_ke_pendana);

        getSetData();
    }

    //-------------------------------------------------------------------------------------------//

    private void getSetData() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = Objects.requireNonNull(task.getResult());

                    // id pinjaman
                    mTextIDPinjaman.setText(mDocName);

                    // besar pinjaman
                    Long c_pinjaman = doc.getLong("pinjaman_besar");
                    assert c_pinjaman != null;
                    int besar_pinjaman = Math.toIntExact(c_pinjaman);
                    switch (besar_pinjaman) {
                        case 500000:
                            mTextBesarPinjaman.setText(R.string.pinjam_500k);
                            String load_1 = "Rp. 600.000,-";
                            mTextTotalKembali.setText(load_1);
                            String cicil_1 = "Rp200000";
                            mTextPokok.setText(cicil_1);

                            String transfer_pendana1 = "Rp191667";
                            mTextTransferKePendana.setText(transfer_pendana1);
                            break;
                        case 1000000:
                            mTextBesarPinjaman.setText(R.string.pinjam_1000k);
                            String load_2 = "Rp. 1.200.000,-";
                            mTextTotalKembali.setText(load_2);
                            String cicil_2 = "Rp400000";
                            mTextPokok.setText(cicil_2);

                            String transfer_pendana2 = "Rp383334";
                            mTextTransferKePendana.setText(transfer_pendana2);
                            break;
                        case 1500000:
                            mTextBesarPinjaman.setText(R.string.pinjam_1500k);
                            String load_3 = "Rp.1.800.000,-";
                            mTextTotalKembali.setText(load_3);
                            String cicil_3 = "Rp600000";
                            mTextPokok.setText(cicil_3);

                            String transfer_pendana3 = "Rp575000";
                            mTextTransferKePendana.setText(transfer_pendana3);
                            break;
                    }

                    // tanggal pendanaan
                    Date tgl_pendanaan = doc.getDate("pinjaman_tanggal_cair");
                    assert tgl_pendanaan != null;
                    mTextTglPendanaan.setText(DateFormat.getDateInstance(DateFormat.FULL)
                            .format(tgl_pendanaan));

                    // tahap pinjaman
                    Long c_tahap = doc.getLong("pinjaman_tahap");
                    assert c_tahap != null;
                    mTahap = Math.toIntExact(c_tahap);

                    if (mTahap == 1) {
                        String set = "Cicilan Pertama";
                        mTextTahapPinjaman.setText(set);

                        Date tempo = doc.getDate("pinjaman_tanggal_bayar_1");
                        assert tempo != null;
                        mTextTglJatuhTempo.setText(DateFormat.getDateInstance(DateFormat.FULL)
                                .format(tempo));
                    } else if (mTahap == 2) {
                        String set = "Cicilan Kedua";
                        mTextTahapPinjaman.setText(set);

                        mTempo1 = doc.getDate("pinjaman_tanggal_bayar_2");
                        assert mTempo1 != null;
                        mTextTglJatuhTempo.setText(DateFormat.getDateInstance(DateFormat.FULL)
                                .format(mTempo1));
                    } else if (mTahap == 3) {
                        String set = "Cicilan Terakhir";
                        mTextTahapPinjaman.setText(set);

                        mTempo2 = doc.getDate("pinjaman_tanggal_bayar_3");
                        assert mTempo2 != null;
                        mTextTglJatuhTempo.setText(DateFormat.getDateInstance(DateFormat.FULL)
                                .format(mTempo2));
                    }

                    // tanggal transfer
                    Date date_transfer = doc.getDate("pinjaman_tanggal_transfer");
                    assert date_transfer != null;
                    mTextTglTransfer.setText(DateFormat.getDateInstance(DateFormat.FULL)
                            .format(date_transfer));

                    // ID pendana
                    mIDPendana = doc.getString("id_pendana");
                    mTextIDPendana.setText(mIDPendana);

                    // ID Peminjam
                    mIDPeminjam = doc.getString("id_peminjam");
                    mTextIDPeminjam.setText(mIDPeminjam);

                    // pengembalian cicilan akumulasi
                    Long c_terbayar = doc.getLong("pinjaman_terbayar");
                    assert c_terbayar != null;
                    mTerbayar = Math.toIntExact(c_terbayar);
                    String l_terbayar = formatter.format(mTerbayar);
                    mTextTotalTerbayar.setText(l_terbayar);

                    // nominal transfer
                    Long c_transfer = doc.getLong("pinjaman_transfer");
                    assert c_transfer != null;
                    mTransfer = Math.toIntExact(c_transfer);
                    String l_transfer = formatter.format(mTransfer);
                    mTextNominalTransfer.setText(l_transfer);

                    // denda
                    Long c_denda = doc.getLong("pinjaman_denda");
                    assert c_denda != null;
                    int denda = Math.toIntExact(c_denda);
                    String l_denda = formatter.format(denda);
                    mTextDenda.setText(l_denda);

                    // get-set from another collection
                    getSetDataPendana(mIDPendana);
                    getSetDataPeminjam(mIDPeminjam);
                }
            }
        });
    }

    private void getSetDataPeminjam(String id) {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("USER").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = Objects.requireNonNull(task.getResult());

                    // get
                    String nama = doc.getString("name");
                    String holder = doc.getString("rekening_namaholder");
                    String rekening = doc.getString("rekening_rekbank");

                    // set
                    mTextNamaPeminjam.setText(nama);
                    mTextHolderPeminjam.setText(holder);
                    mTextRekPeminjam.setText(rekening);
                }
            }
        });
    }

    private void getSetDataPendana(String id) {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("USER").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = Objects.requireNonNull(task.getResult());

                    // get
                    String nama = doc.getString("name");
                    String holder = doc.getString("rekening_namaholder");
                    String rekening = doc.getString("rekening_rekbank");

                    // set
                    mTextNamaPendana.setText(nama);
                    mTextHolderPendana.setText(holder);
                    mTextRekPendana.setText(rekening);
                }
            }
        });
    }

    //-------------------------------------------------------------------------------------------//

    public void onClickKonfirmasiTransfer(View view) {

        // akumulasi pembayaran
        int total_terbayar = mTransfer + mTerbayar;
        mDocRef.update("pinjaman_terbayar", total_terbayar);

        // tahap --> 1 > 2 > 3 > 0 (lunas)
        if (mTahap == 1) {
            mDocRef.update("pinjaman_tahap", 2);
            mDocRef.update("pinjaman_tanggal_bayar", mTempo1);
        } else if (mTahap == 2) {
            mDocRef.update("pinjaman_tahap", 3);
            mDocRef.update("pinjaman_tanggal_bayar", mTempo2);
        } else if (mTahap == 3) {
            // update lunas (pinjaman_lunas --> true, pinjaman_aktif --> false)
            mDocRef.update("pinjaman_tanggal_bayar", null);
            mDocRef.update("pinjaman_tahap", 0);
            mDocRef.update("pinjaman_lunas", true);
            mDocRef.update("pinjaman_aktif", false);
            // -------- (USER : pinjaman_aktif --> 0, pinjaman_status = false)
            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("USER").document(mIDPeminjam);
            docRef.update("pinjaman_aktif", 0);
            docRef.update("pinjaman_status", false);
        }

        // konfirmasi pembayaran --> true
        mDocRef.update("pinjaman_konfirmasi_pembayaran", true);

        // pinjaman _tanggal_transfer --> null
        mDocRef.update("pinjaman_tanggal_transfer", null);

        // pinjaman_status_pembayaran --> DIKONFIRMASI
        mDocRef.update("pinjaman_status_pembayaran", "DIKONFIRMASI")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ReturnActivity.this,
                        "Transfer cicilan dikonfirmasi!",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
