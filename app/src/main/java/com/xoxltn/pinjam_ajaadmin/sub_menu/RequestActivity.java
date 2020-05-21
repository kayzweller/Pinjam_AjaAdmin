package com.xoxltn.pinjam_ajaadmin.sub_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xoxltn.pinjam_ajaadmin.R;
import com.xoxltn.pinjam_ajaadmin.view_fragment.RequestFragment;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class RequestActivity extends AppCompatActivity {

    static final String SEND_ID_PEMINJAM = "com.xoxltn.pinjam_ajaadmin.SEND_ID_PEMINJAM";

    private DocumentReference mDocRef;
    private String mDocName, mDocPath;

    private String mIDPeminjam;

    private TextView mTextIDPinjaman, mTextIDPeminjam, mTextTanggalPinjaman,
            mTextBesarPinjaman, mTextBesarKembali, mTextResult;
    private Date mTanggalPengajuan;
    private int mNominal_pinjam;

    //-------------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // calling extra
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            mDocName = extras.getString(RequestFragment.SEND_DOCUMENT_DOC);
            mDocPath = extras.getString(RequestFragment.SEND_DOCUMENT_PATH);
        }

        // firestore setup
        FirebaseFirestore fire = FirebaseFirestore.getInstance();
        mDocRef = fire.document(mDocPath);

        // hooks
        mTextIDPinjaman = findViewById(R.id.req_id_pinjaman);
        mTextIDPeminjam = findViewById(R.id.req_id_peminjam);
        mTextTanggalPinjaman = findViewById(R.id.req_tgl_pengajuan);
        mTextBesarPinjaman = findViewById(R.id.req_dana_pinjaman);
        mTextBesarKembali = findViewById(R.id.req_dana_kembali);
        mTextResult = findViewById(R.id.text_result);

        getData();
    }

    //-------------------------------------------------------------------------------------------//

    private void getData() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc =  Objects.requireNonNull(task.getResult());

                    mIDPeminjam = doc.getString("id_peminjam");

                    mTanggalPengajuan = doc.getDate("pinjaman_tanggal_req");

                    Long call = doc.getLong("pinjaman_besar"); assert call != null;
                    mNominal_pinjam = Math.toIntExact(call);

                    setTextView();
                }
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
                String set1 = "Rp. 575.000,-";
                mTextBesarKembali.setText(set1);
                break;
            case 1000000:
                mTextBesarPinjaman.setText(R.string.pinjam_1000k);
                String set2 = "Rp. 1.150.000,-";
                mTextBesarKembali.setText(set2);
                break;
            case 1500000:
                mTextBesarPinjaman.setText(R.string.pinjam_1500k);
                String set3 = "Rp. 1.725.000,-";
                mTextBesarKembali.setText(set3);
                break;
        }
    }

    //-------------------------------------------------------------------------------------------//

    public void onClickDataPeminjam(View view) {
        Intent dataPeminjam = new Intent(this, RequestPeminjamActivity.class);
        dataPeminjam.putExtra(SEND_ID_PEMINJAM, mIDPeminjam);
        startActivity(dataPeminjam);
    }

    public void onClickSetujuiPinjaman(View view) {
        mDocRef.update("pendanaan_submit", false);
        mDocRef.update("pinjaman_status", "MENUNGGU PENDANAAN");
        mDocRef.update("pendanaan_req", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String set = "Permintaan Pinjaman Disetujui!";
                        mTextResult.setText(set);
                        mTextResult.setVisibility(View.VISIBLE);
                    }
                });
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
