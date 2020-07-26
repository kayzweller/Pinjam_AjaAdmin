package com.xoxltn.pinjam_ajaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MenuReturnFragment extends Fragment {

    public static final String SEND_DOCUMENT_DOC = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_DOC";
    public static final String SEND_DOCUMENT_PATH = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_PATH";

    private View mView;
    private CollectionReference mColRef;
    private AdapterReturn mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_menu_return, container, false);
        
        mColRef = FirebaseFirestore.getInstance().collection("PEMINJAM");

        setUpRecycleView();

        return mView;
    }

    //-------------------------------------------------------------------------------------------//

    private void setUpRecycleView() {
        Query query = mColRef.whereEqualTo("pinjaman_konfirmasi_pembayaran", false)
                .orderBy("pinjaman_tanggal_transfer", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ModelReturn> options = new FirestoreRecyclerOptions
                .Builder<ModelReturn>()
                .setQuery(query, ModelReturn.class)
                .build();

        mAdapter = new AdapterReturn(options);

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerview_fund_return);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        // ON CLICK RECYCLEVIEW ITEM HERE!!
        mAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            String doc = documentSnapshot.getId();
            String path = documentSnapshot.getReference().getPath();

            // goto detail of load request, also sent extra!
            Intent toFundReqDetail = new Intent(getActivity(), SubmenuReturnActivity.class);
            toFundReqDetail.putExtra(SEND_DOCUMENT_DOC, doc);
            toFundReqDetail.putExtra(SEND_DOCUMENT_PATH, path);
            startActivity(toFundReqDetail);
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

