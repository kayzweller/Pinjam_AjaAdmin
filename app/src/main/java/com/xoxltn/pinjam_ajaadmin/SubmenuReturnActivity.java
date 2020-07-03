package com.xoxltn.pinjam_ajaadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;

public class SubmenuReturnActivity extends AppCompatActivity {

    private NumberFormat formatter;

    private String mDocName, mDocPath;
    private DocumentReference mDocRef;

    // set for notification
    private FirebaseFirestore mFire;
    private String mIDNotif;
    private Date mCurrentDate;
    private String cicilanConfirm;
    private String cicilanTitle;

    private TextView mTextIDPinjaman, mTextBesarPinjaman, mTextTotalKembali, mTextTglPendanaan,
            mTextTahapPinjaman, mTextTglJatuhTempo, mTextTglTransfer;
    private TextView mTextNamaPendana, mTextIDPendana, mTextNamaPeminjam, mTextIDPeminjam;
    private TextView mTextHolderPeminjam, mTextRekPeminjam, mTextNominalTransfer,
            mTextPokok, mTextDenda;
    private TextView mTextTotalTerbayar, mTextHolderPendana, mTextRekPendana, mTextTransferKePendana;

    private String mIDPeminjam;
    private String mIDPendana;
    private int mTahap, mTransfer, mTransferPendana, mTerbayar, mTerbayarPendana;

    private Long mDendaTotal, mDenda, mCalDendaTotal;

    //-------------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu_return);

        // formatting the number
        FormatRupiah();

        // calling extra
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            mDocName = extras.getString(MenuReturnFragment.SEND_DOCUMENT_DOC);
            mDocPath = extras.getString(MenuReturnFragment.SEND_DOCUMENT_PATH);
        }

        // firestore setup
        mFire = FirebaseFirestore.getInstance();
        mDocRef = mFire.document(mDocPath);

        //firestore for notification
        mCurrentDate = Calendar.getInstance().getTime();
        mIDNotif = mFire.collection("NOTIFIKASI").document().getId();

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
        mDocRef.get().addOnCompleteListener(task -> {
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
                        String cicil_1 = "Rp200.000";
                        mTextPokok.setText(cicil_1);
                        break;

                    case 1000000:
                        mTextBesarPinjaman.setText(R.string.pinjam_1000k);
                        String load_2 = "Rp. 1.200.000,-";
                        mTextTotalKembali.setText(load_2);
                        String cicil_2 = "Rp400.000";
                        mTextPokok.setText(cicil_2);
                        break;

                    case 1500000:
                        mTextBesarPinjaman.setText(R.string.pinjam_1500k);
                        String load_3 = "Rp.1.800.000,-";
                        mTextTotalKembali.setText(load_3);
                        String cicil_3 = "Rp600.000";
                        mTextPokok.setText(cicil_3);
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

                    Date load = doc.getDate("pinjaman_tanggal_bayar_1");
                    assert load != null;
                    mTextTglJatuhTempo.setText(DateFormat.getDateInstance(DateFormat.FULL)
                            .format(load));
                } else if (mTahap == 2) {
                    String set = "Cicilan Kedua";
                    mTextTahapPinjaman.setText(set);

                    Date load = doc.getDate("pinjaman_tanggal_bayar_2");
                    assert load != null;
                    mTextTglJatuhTempo.setText(DateFormat.getDateInstance(DateFormat.FULL)
                            .format(load));
                } else if (mTahap == 3) {
                    String set = "Cicilan Terakhir";
                    mTextTahapPinjaman.setText(set);

                    Date load = doc.getDate("pinjaman_tanggal_bayar_3");
                    assert load != null;
                    mTextTglJatuhTempo.setText(DateFormat.getDateInstance(DateFormat.FULL)
                            .format(load));
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

                // pembayaran cicilan akumulasi
                Long c_terbayar = doc.getLong("pinjaman_terbayar");
                assert c_terbayar != null;
                mTerbayar = Math.toIntExact(c_terbayar);
                String l_terbayar = formatter.format(mTerbayar);
                mTextTotalTerbayar.setText(l_terbayar);

                // pengembalian pendanaan akumulasi
                Long c_terbayar_pendana = doc.getLong("pinjaman_terbayar_pendana");
                assert c_terbayar_pendana != null;
                mTerbayarPendana = Math.toIntExact(c_terbayar);
                String l_terbayar_pendana = formatter.format(mTerbayarPendana);
                mTextTotalTerbayar.setText(l_terbayar_pendana);

                // nominal transfer
                Long c_transfer = doc.getLong("pinjaman_transfer");
                assert c_transfer != null;
                mTransfer = Math.toIntExact(c_transfer);
                String l_transfer = formatter.format(mTransfer);
                mTextNominalTransfer.setText(l_transfer);

                // besar transfer ke pendana
                Long c_transfer_pendana = doc.getLong("pinjaman_transfer_pendana");
                assert c_transfer_pendana != null;
                mTransferPendana = Math.toIntExact(c_transfer_pendana);
                String l_transfer_pendana = formatter.format(mTransferPendana);
                mTextTransferKePendana.setText(l_transfer_pendana);

                // denda Long
                mDendaTotal = doc.getLong("pinjaman_denda_total");
                mDenda = doc.getLong("pinjaman_denda");
                assert mDenda != null;
                int denda = Math.toIntExact(mDenda);

                mCalDendaTotal = mDendaTotal + mDenda;

                String l_denda = formatter.format(denda);
                mTextDenda.setText(l_denda);

                Log.e("mDendaTotal", String.valueOf(mDendaTotal));
                Log.e("mDenda", String.valueOf(mDenda));
                Log.e("mCalDendaTotal", String.valueOf(mCalDendaTotal));

                // get-set from another collection
                getSetDataPendana(mIDPendana);
                getSetDataPeminjam(mIDPeminjam);

            }
        });
    }

    private void getSetDataPeminjam(String id) {
        mDocRefData(id, mTextNamaPeminjam, mTextHolderPeminjam, mTextRekPeminjam);
    }

    private void getSetDataPendana(String id) {
        mDocRefData(id, mTextNamaPendana, mTextHolderPendana, mTextRekPendana);
    }

    private void mDocRefData(String id, TextView mTextNamaPendana, TextView mTextHolderPendana,
                             TextView mTextRekPendana) {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("USER").document(id);

        docRef.get().addOnCompleteListener(task -> {
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
        });
    }

    //-------------------------------------------------------------------------------------------//

    public void onClickKonfirmasiPembayaran(View view) {

        // akumulasi pembayaran
        int total_terbayar = mTransfer + mTerbayar;
        mDocRef.update("pinjaman_terbayar", total_terbayar);

        // akumulasi pengembalian
        int total_terbayar_pendana = mTransferPendana + mTerbayarPendana;
        mDocRef.update("pinjaman_terbayar_pendana", total_terbayar_pendana);

        // tahap --> 1 > 2 > 3 > 0 (lunas)
        if (mTahap == 1) {
            mDocRef.update("pinjaman_tahap", 2);
            mDocRef.update("pinjaman_status_pembayaran", "CICILAN PERTAMA TERBAYAR");
            mDocRef.update("pinjaman_denda_total", mCalDendaTotal);
            mDocRef.update("pinjaman_denda", 0);
            cicilanTitle = "Pembayaran Cicilan Dikonfirmasi!";
            cicilanConfirm = "Transfer cicilan pertama Anda telah diterima.";

        } else if (mTahap == 2) {
            mDocRef.update("pinjaman_tahap", 3);
            mDocRef.update("pinjaman_status_pembayaran", "CICILAN KEDUA TERBAYAR");
            mDocRef.update("pinjaman_denda_total", mCalDendaTotal);
            mDocRef.update("pinjaman_denda", 0);
            cicilanTitle = "Pembayaran Cicilan Dikonfirmasi!";
            cicilanConfirm = "Transfer cicilan kedua Anda telah diterima.";

        } else if (mTahap == 3) {
            // update lunas (pinjaman_lunas --> true, pinjaman_aktif --> false)
            mDocRef.update("pinjaman_tahap", 0);
            mDocRef.update("pinjaman_status_pembayaran", "CICILAN LUNAS");
            mDocRef.update("pinjaman_denda_total", mCalDendaTotal);
            mDocRef.update("pinjaman_denda", 0);
            mDocRef.update("pinjaman_lunas", true);
            mDocRef.update("pinjaman_aktif", false);
            mDocRef.update("pinjaman_status", "LUNAS");
            cicilanTitle = "Pinjaman Anda Lunas!";
            cicilanConfirm = "Transfer cicilan terakhir Anda telah diterima, " +
                    "Anda dapat mengajukan permintaan pinjaman kembali.";

            // -------- (USER : pinjaman_aktif --> 0, pinjaman_status = false)
            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("USER").document(mIDPeminjam);
            docRef.update("pinjaman_aktif", "0");
            docRef.update("pinjaman_status", false);
        }

        // pinjaman_tanggal_transfer --> null
        mDocRef.update("pinjaman_tanggal_transfer", null);

        // konfirmasi pembayaran --> true
        mDocRef.update("pinjaman_konfirmasi_pembayaran", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SubmenuReturnActivity.this,
                            "Transfer cicilan dikonfirmasi!",
                            Toast.LENGTH_SHORT).show();

                    // set the notification
                    Map<String, Object> dataNotif = new HashMap<>();
                    dataNotif.put("id_user", mIDPeminjam);
                    dataNotif.put("notif_type", true);
                    dataNotif.put("notif_date", mCurrentDate);
                    dataNotif.put("notif_title", cicilanTitle);
                    dataNotif.put("notif_detail", cicilanConfirm);
                    mFire.collection("NOTIFIKASI")
                            .document(mIDNotif).set(dataNotif);

                    finish();
                });
    }

    public void onClickBatalkanPembayaran(View view) {

        mDocRef.update("pinjaman_tanggal_transfer", null);
        mDocRef.update("pinjaman_status_pembayaran", "CICILAN BELUM DIBAYAR");
        mDocRef.update("pinjaman_konfirmasi_pembayaran", null)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SubmenuReturnActivity.this,
                            "Transfer cicilan dibatalkan!",
                            Toast.LENGTH_SHORT).show();

                    // set the notification
                    Map<String, Object> dataNotif = new HashMap<>();
                    dataNotif.put("id_user", mIDPeminjam);
                    dataNotif.put("notif_type", false);
                    dataNotif.put("notif_date", mCurrentDate);
                    dataNotif.put("notif_title", "Pembayaran Cicilan Anda Dibatalkan!");
                    dataNotif.put("notif_detail", "Transfer cicilan Anda tidak diterima oleh Admin " +
                            "dalam waktu 1x24 jam. Perhatikan kembali transaksi transfer Anda.");
                    mFire.collection("NOTIFIKASI").document(mIDNotif).set(dataNotif);

                    finish();
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
