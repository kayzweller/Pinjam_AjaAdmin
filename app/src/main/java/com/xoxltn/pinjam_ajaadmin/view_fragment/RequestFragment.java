package com.xoxltn.pinjam_ajaadmin.view_fragment;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.xoxltn.pinjam_ajaadmin.R;
import com.xoxltn.pinjam_ajaadmin.adapter.RequestAdapter;
import com.xoxltn.pinjam_ajaadmin.sub_menu.RequestActivity;
import com.xoxltn.pinjam_ajaadmin.model.Request;

public class RequestFragment extends Fragment {

    public static final String SEND_DOCUMENT_PATH = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_PATH";
    public static final String SEND_DOCUMENT_DOC = "com.xoxltn.pinjam_ajaadmin.SEND_DOCUMENT_DOC";

    private View mView;
    private CollectionReference mColRef;
    private RequestAdapter mAdapter;

    //-------------------------------------------------------------------------------------------//

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_request, container, false);

        mColRef = FirebaseFirestore.getInstance().collection("PEMINJAM");

        setUpRecycleView();

        return mView;
    }

    //-------------------------------------------------------------------------------------------//

    private void setUpRecycleView() {
        Query query = mColRef.whereEqualTo("pendanaan_submit", true)
                .orderBy("pinjaman_tanggal_req", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        mAdapter = new RequestAdapter(options);

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerview_fund_request);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        // ON CLICK RECYCLEVIEW ITEM HERE!!
        mAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String doc = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();

                // goto detail of load request, also sent extra!
                Intent toFundReqDetail = new Intent(getActivity(), RequestActivity.class);
                toFundReqDetail.putExtra(SEND_DOCUMENT_PATH, path);
                toFundReqDetail.putExtra(SEND_DOCUMENT_DOC, doc);
                startActivity(toFundReqDetail);
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
