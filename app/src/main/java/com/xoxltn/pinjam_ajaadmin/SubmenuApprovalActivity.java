package com.xoxltn.pinjam_ajaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.*;

public class SubmenuApprovalActivity extends AppCompatActivity {

    static final String SEND_ID_PEMINJAM = "com.xoxltn.pinjam_ajaadmin.SEND_ID_PEMINJAM";
    static final String SEND_ID_PENDANA = "com.xoxltn.pinjam_ajaadmin.SEND_ID_PENDANA";
    static final String SEND_DOCUMENT_PATH = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_PATH";
    static final String SEND_DOCUMENT_DOC = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_DOC";
    static final String SEND_LEND_NOMINAL = "com.xoxltn.pinjam_ajaadmin.SEND_LEND_NOMINAL";

    // set for notification
    private FirebaseFirestore mFire;
    private String mIDNotif;

    private Date mCurrentDate;
    private Calendar mPayDate1, mPayDate2, mPayDate3;

    private String mDocName, mDocPath;

    private DocumentReference mDocRef;

    private String mIDPeminjam;
    private String mIDPendana;

    private String mSet;

    private TextView mTextIDPinjaman, mTextIDPeminjam, mTextTanggalPinjaman,
            mTextBesarPinjaman, mTextBesarKembali, mTextResult;
    private Date mTanggalPengajuan;
    private int mNominal_pinjam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu_approval);

        // date setup
        mCurrentDate = Calendar.getInstance().getTime();
        mPayDate1 = Calendar.getInstance();
        mPayDate2 = Calendar.getInstance();
        mPayDate3 = Calendar.getInstance();

        // calling extra
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            mDocName = extras.getString(MenuApprovalFragment.SEND_DOCUMENT_DOC);
            mDocPath = extras.getString(MenuApprovalFragment.SEND_DOCUMENT_PATH);
        }

        // firestore setup
        FirebaseFirestore fire = FirebaseFirestore.getInstance();
        mDocRef = fire.document(mDocPath);

        //firestore for notification
        mCurrentDate = Calendar.getInstance().getTime();
        mFire = FirebaseFirestore.getInstance();
        mIDNotif = mFire.collection("NOTIFIKASI").document().getId();

        // hooks here
        mTextIDPinjaman = findViewById(R.id.app_id_pinjaman);
        mTextIDPeminjam = findViewById(R.id.app_id_peminjam);
        mTextTanggalPinjaman = findViewById(R.id.app_tgl_pengajuan);
        mTextBesarPinjaman = findViewById(R.id.app_dana_pinjaman);
        mTextBesarKembali = findViewById(R.id.app_dana_kembali);
        mTextResult = findViewById(R.id.text_result);

        getData();
    }

    //-------------------------------------------------------------------------------------------//

    private void getData() {
        mDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc =  Objects.requireNonNull(task.getResult());

                mIDPeminjam = doc.getString("id_peminjam");
                mIDPendana = doc.getString("id_pendana");

                mTanggalPengajuan = doc.getDate("pinjaman_tanggal_req");

                Long call = doc.getLong("pinjaman_besar"); assert call != null;
                mNominal_pinjam = Math.toIntExact(call);

                setTextView();
            }
        });
    }

    private void setTextView() {
        // ID pinjaman
        mTextIDPinjaman.setText(mDocName);
        mTextIDPeminjam.setText(mIDPeminjam);

        // tanggal permintaan pinjaman
        mTextTanggalPinjaman.setText(DateFormat.getDateInstance(DateFormat.FULL)
                .format(mTanggalPengajuan));

        // nominal pinjaman & nominal kembali
        switch (mNominal_pinjam) {
            case 500000:
                mTextBesarPinjaman.setText(R.string.pinjam_500k);
                String mSet1 = "Rp. 575.000,-";
                mTextBesarKembali.setText(mSet1);
                break;
            case 1000000:
                mTextBesarPinjaman.setText(R.string.pinjam_1000k);
                String mSet2 = "Rp. 1.150.000,-";
                mTextBesarKembali.setText(mSet2);
                break;
            case 1500000:
                mTextBesarPinjaman.setText(R.string.pinjam_1500k);
                String mSet3 = "Rp. 1.725.000,-";
                mTextBesarKembali.setText(mSet3);
                break;
        }
    }

    //-------------------------------------------------------------------------------------------//

    public void onClickDataPeminjam(View view) {
        Intent dataPeminjam = new Intent(this, SubmenuApprovalPeminjamActivity.class);
        dataPeminjam.putExtra(SEND_ID_PEMINJAM, mIDPeminjam);
        startActivity(dataPeminjam);
    }

    public void onClickDataPendana(View view) {
        Intent dataPendana = new Intent(this, SubmenuApprovalPendanaActivity.class);
        dataPendana.putExtra(SEND_ID_PENDANA, mIDPendana);
        dataPendana.putExtra(SEND_DOCUMENT_PATH, mDocPath);
        dataPendana.putExtra(SEND_DOCUMENT_DOC, mDocName);
        String extra = String.valueOf(mNominal_pinjam);
        dataPendana.putExtra(SEND_LEND_NOMINAL, extra);
        startActivity(dataPendana);
    }

    //-------------------------------------------------------------------------------------------//

    public void onClickKonfirmasiPendanaan(View view) {

        mDocRef.update("pinjaman_tanggal_cair", mCurrentDate);
        mPayDate1.add(Calendar.MONTH, 1);
        mDocRef.update("pinjaman_tanggal_bayar_1", mPayDate1.getTime());
        mPayDate2.add(Calendar.MONTH, 2);
        mDocRef.update("pinjaman_tanggal_bayar_2", mPayDate2.getTime());
        mPayDate3.add(Calendar.MONTH, 3);
        mDocRef.update("pinjaman_tanggal_bayar_3", mPayDate3.getTime());

        // pay attention to this on return pay
        mDocRef.update("pinjaman_tahap", 1);
        mDocRef.update("pinjaman_tanggal_bayar", mPayDate1.getTime()); //not used, delete later..

        mDocRef.update("pinjaman_status", "PINJAMAN AKTIF");
        mDocRef.update("pinjaman_status_pembayaran", "CICILAN BELUM DIBAYAR");
        mDocRef.update("pinjaman_aktif", true)
                .addOnSuccessListener(aVoid -> {
                    String set = "Pinjaman Aktif!";
                    mTextResult.setText(set);
                    mTextResult.setVisibility(View.VISIBLE);
                });

        // set the notification for borrower
        Map<String, Object> dataNotif = new HashMap<>();
        dataNotif.put("id_user", mIDPeminjam);
        dataNotif.put("notif_type", true);
        dataNotif.put("notif_date", mCurrentDate);
        dataNotif.put("notif_title", "Pinjaman Anda Didanai!");
        dataNotif.put("notif_detail", "Selamat, permintaan pinjaman telah didanai. " +
                "Silahkan cek saldo rekening Anda dan perhatikan tanggal pembayaran " +
                "cicilan pinjaman setiap bulannya.");
        mFire.collection("NOTIFIKASI").document(mIDNotif).set(dataNotif);

        // call the borrowing nominal
        switch (mNominal_pinjam) {
            case 500000:
                mSet = "Rp. 500.000,-";
                break;
            case 1000000:
                mSet = "Rp. 1.000.000,-";
                break;
            case 1500000:
                mSet = "Rp. 1.500.000,-";
                break;
        }

        // set the notification for lender
        Map<String, Object> dataNotif2 = new HashMap<>();
        dataNotif2.put("id_user", mIDPendana);
        dataNotif2.put("notif_type", true);
        dataNotif2.put("notif_date", mCurrentDate);
        dataNotif2.put("notif_title", "Pendanaan Pinjaman Disetujui!");
        dataNotif2.put("notif_detail", "Selamat, pendanaan pinjaman Anda dengan ID : " + mDocName +
                " dengan nilai pinjaman " + mSet + " telah disetujui.");
        mFire.collection("NOTIFIKASI").document(mIDNotif).set(dataNotif2);

        finish();
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
