package com.xoxltn.pinjam_ajaadmin.sub_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.xoxltn.pinjam_ajaadmin.R;

import java.util.Objects;

public class DetailPendanaActivity extends AppCompatActivity {

    private DocumentReference docRef;

    private String mIDPendana;

    private ImageView imageview_ktp;
    private TextView text_id_pendana, text_nama, text_no_ktp, text_tgl_lahir, text_jenis_kelamin,
            text_tempat_lahir, text_alamat_pribadi, text_nama_ibu, text_pendidikan, text_status,
            text_kendaraan;
    private TextView text_email, text_no_telp;
    private TextView text_bank, text_holder, text_rekening;
    private TextView text_nama_instansi, text_jenis_pekerjaan, text_penghasilan,
            text_alamat_instansi;
    private TextView text_nama_ktk1, text_nama_ktk2, text_hub_ktk1, text_hub_ktk2,
            text_telp_ktk1, text_telp_ktk2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pendana);

        // calling extra
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            mIDPendana = extras.getString(DetailActivity.SEND_ID_PENDANA);
        }
        FirebaseFirestore fire = FirebaseFirestore.getInstance();
        docRef = fire.collection("USER").document(mIDPendana);

        // hooks
        imageview_ktp = findViewById(R.id.imageview_ktp);

        text_id_pendana = findViewById(R.id.text_id_pendana);
        text_nama = findViewById(R.id.text_nama);
        text_no_ktp = findViewById(R.id.text_no_ktp);
        text_tgl_lahir = findViewById(R.id.text_tgl_lahir);
        text_jenis_kelamin = findViewById(R.id.text_jenis_kelamin);
        text_tempat_lahir = findViewById(R.id.text_tempat_lahir);
        text_alamat_pribadi = findViewById(R.id.text_alamat_pribadi);
        text_nama_ibu = findViewById(R.id.text_nama_ibu);
        text_pendidikan = findViewById(R.id.text_pendidikan);
        text_status = findViewById(R.id.text_status);
        text_kendaraan = findViewById(R.id.text_kendaraan);

        text_email = findViewById(R.id.text_email);
        text_no_telp = findViewById(R.id.text_no_telp);

        text_bank = findViewById(R.id.text_bank);
        text_holder = findViewById(R.id.text_holder);
        text_rekening = findViewById(R.id.text_rekening);

        text_nama_instansi = findViewById(R.id.text_nama_instansi);
        text_jenis_pekerjaan = findViewById(R.id.text_jenis_pekerjaan);
        text_penghasilan = findViewById(R.id.text_penghasilan);
        text_alamat_instansi = findViewById(R.id.text_alamat_instansi);

        text_nama_ktk1 = findViewById(R.id.text_nama_ktk1);
        text_nama_ktk2 = findViewById(R.id.text_nama_ktk2);
        text_hub_ktk1 = findViewById(R.id.text_hub_ktk1);
        text_hub_ktk2 = findViewById(R.id.text_hub_ktk2);
        text_telp_ktk1 = findViewById(R.id.text_telp_ktk1);
        text_telp_ktk2 = findViewById(R.id.text_telp_ktk2);

        // call data peminjam
        callDataPendana(mIDPendana);
    }

    //-------------------------------------------------------------------------------------------//

    private void callDataPendana(final String IDPendana) {

        // get-set image view
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fireStorage = storageRef.child("KTP").child(IDPendana).child("foto_ktp");
        fireStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imageview_ktp);
            }
        });

        // get-set text view
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc =  Objects.requireNonNull(task.getResult());

                    // get data
                    String nama = doc.getString("name");
                    String no_ktp = doc.getString("noKTP");
                    String tgl_lahir = doc.getString("tanggalLahir");
                    String jenis_kelamin = doc.getString("jenisKelamin");
                    String tempat_lahir = doc.getString("tempatLahir");
                    String alamat_pribadi = doc.getString("pribadi_alamat");
                    String nama_ibu = doc.getString("pribadi_namaibu");
                    String pendidikan = doc.getString("pribadi_pendidikan");
                    String status = doc.getString("pribadi_pernikahan");
                    String kendaraan = doc.getString("pribadi_kendaraan");

                    String email = doc.getString("email");
                    String no_telp = doc.getString("phone");

                    String bank = doc.getString("rekening_namabank");
                    String holder = doc.getString("rekening_namaholder");
                    String rekening = doc.getString("rekening_rekbank");

                    String nama_instansi = doc.getString("pekerjaan_namainstansi");
                    String jenis_pekerjaan = doc.getString("pekerjaan_jenis");
                    String penghasilan = doc.getString("pekerjaan_penghasilan");
                    String alamat_instansi = doc.getString("pekerjaan_alamatinstansi");

                    String nama_ktk1 = doc.getString("kontak_Nama1");
                    String nama_ktk2 = doc.getString("kontak_Nama2");
                    String hub_ktk1 = doc.getString("kontak_Hub1");
                    String hub_ktk2 = doc.getString("kontak_Hub2");
                    String telp_ktk1 = doc.getString("kontak_NoTelp1");
                    String telp_ktk2 = doc.getString("kontak_NoTelp2");


                    // set to text view
                    text_id_pendana.setText(IDPendana);

                    text_nama.setText(nama);
                    text_no_ktp.setText(no_ktp);
                    text_tgl_lahir.setText(tgl_lahir);
                    text_jenis_kelamin.setText(jenis_kelamin);
                    text_tempat_lahir.setText(tempat_lahir);
                    text_alamat_pribadi.setText(alamat_pribadi);
                    text_nama_ibu.setText(nama_ibu);
                    text_pendidikan.setText(pendidikan);
                    text_status.setText(status);
                    text_kendaraan.setText(kendaraan);

                    text_email.setText(email);
                    text_no_telp.setText(no_telp);

                    text_bank.setText(bank);
                    text_holder.setText(holder);
                    text_rekening.setText(rekening);

                    text_nama_instansi.setText(nama_instansi);
                    text_jenis_pekerjaan.setText(jenis_pekerjaan);
                    text_penghasilan.setText(penghasilan);
                    text_alamat_instansi.setText(alamat_instansi);

                    text_nama_ktk1.setText(nama_ktk1);
                    text_nama_ktk2.setText(nama_ktk2);
                    text_hub_ktk1.setText(hub_ktk1);
                    text_hub_ktk2.setText(hub_ktk2);
                    text_telp_ktk1.setText(telp_ktk1);
                    text_telp_ktk2.setText(telp_ktk2);
                }
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
