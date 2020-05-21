package com.xoxltn.pinjam_ajaadmin.view_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.xoxltn.pinjam_ajaadmin.R;
import com.xoxltn.pinjam_ajaadmin.adapter.ApprovalAdapter;
import com.xoxltn.pinjam_ajaadmin.sub_menu.ApprovalActivity;
import com.xoxltn.pinjam_ajaadmin.sub_menu.DetailActivity;
import com.xoxltn.pinjam_ajaadmin.model.Approval;

import java.util.Objects;

public class ApprovalFragment extends Fragment {

    public static final String SEND_DOCUMENT_PATH = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_PATH";
    public static final String SEND_DOCUMENT_DOC = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_DOC";

    private View mView;
    private CollectionReference mColRef;
    private ApprovalAdapter mAdapter;

    private String mDoc, mPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_approval, container, false);

        mColRef = FirebaseFirestore.getInstance().collection("PEMINJAM");

        setUpRecycleView();

        return mView;
    }

    private void setUpRecycleView() {
        Query query = mColRef.whereEqualTo("pendanaan_status", true)
                .orderBy("pinjaman_tanggal_req", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Approval> options = new FirestoreRecyclerOptions
                .Builder<Approval>()
                .setQuery(query, Approval.class)
                .build();

        mAdapter = new ApprovalAdapter(options);

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerview_fund_approval);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        // ON CLICK RECYCLEVIEW ITEM HERE!!
        mAdapter.setOnItemClickListener(new ApprovalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                mDoc = documentSnapshot.getId();
                mPath = documentSnapshot.getReference().getPath();

                // call pinjaman status here
                DocumentReference docRef = FirebaseFirestore.getInstance().document(mPath);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = Objects.requireNonNull(task.getResult());
                            boolean pinjaman_aktif = doc.getBoolean("pinjaman_aktif");
                            boolean pinjaman_lunas = doc.getBoolean("pinjaman_lunas");

                            if (!pinjaman_lunas) {
                                if (!pinjaman_aktif) {
                                    // goto detail for accepting funding request
                                    Intent toFundAprroval = new Intent(getActivity(),
                                            ApprovalActivity.class);
                                    toFundAprroval.putExtra(SEND_DOCUMENT_PATH, mPath);
                                    toFundAprroval.putExtra(SEND_DOCUMENT_DOC, mDoc);
                                    startActivity(toFundAprroval);
                                } else {
                                    // goto detail to see data
                                    Intent toFundDetail = new Intent(getActivity(),
                                            DetailActivity.class);
                                    toFundDetail.putExtra(SEND_DOCUMENT_PATH, mPath);
                                    toFundDetail.putExtra(SEND_DOCUMENT_DOC, mDoc);
                                    startActivity(toFundDetail);
                                }
                            } else {
                                Toast.makeText(getActivity(), "PINJAMAN LUNAS",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
    }

    //-------------------------------------------------------------------------------------------//

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
