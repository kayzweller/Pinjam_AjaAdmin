package com.xoxltn.pinjam_ajaadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.util.Date;

public class AdapterApproval extends FirestoreRecyclerAdapter<ModelApproval,
        AdapterApproval.ApprovalHolder> {

    private OnItemClickListener listener;

    public AdapterApproval(@NonNull FirestoreRecyclerOptions<ModelApproval> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ApprovalHolder holder, int position,
                                    @NonNull ModelApproval model) {

        // call status
        String status = model.getPinjaman_status();
        holder.mTextStatusPinjaman.setText(status);

        // call date
        Date date = model.getPinjaman_tanggal_bayar();
        if (date != null) {
            String date_now = DateFormat.getDateInstance(DateFormat.FULL).format(date);
            holder.mTextTanggalBayar.setText(date_now);
        } else {
            String date_now = "--";
            holder.mTextTanggalBayar.setText(date_now);
        }

        // call nominal
        int pinjaman = model.getPinjaman_besar();
        switch (pinjaman) {
            case 500000: {
                String pinjaman_now = "Rp. 500.000,-";

                holder.mTextNominalPinjaman.setText(pinjaman_now);
                holder.mCardRequest.setCardBackgroundColor(0xFFEDEFD3);
                break;
            }
            case 1000000: {
                String pinjaman_now = "Rp. 1.000.000,-";
                holder.mTextNominalPinjaman.setText(pinjaman_now);
                holder.mCardRequest.setCardBackgroundColor(0xFFD3EFEB);

                break;
            }
            case 1500000: {
                String pinjaman_now = "Rp. 1.500.000,-";
                holder.mTextNominalPinjaman.setText(pinjaman_now);
                holder.mCardRequest.setCardBackgroundColor(0xFFEFD3DC);
                break;
            }
        }

    }

    @NonNull
    @Override
    public ApprovalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_approval,
                parent, false);
        return new ApprovalHolder(v);
    }

    //-------------------------------------------------------------------------------------------//

    class ApprovalHolder extends RecyclerView.ViewHolder {
        TextView mTextStatusPinjaman;
        TextView mTextTanggalBayar;
        TextView mTextNominalPinjaman;
        CardView mCardRequest;

        ApprovalHolder(@NonNull View itemView) {
            super(itemView);
            mTextStatusPinjaman = itemView.findViewById(R.id.text_status_pinjaman);
            mTextTanggalBayar = itemView.findViewById(R.id.text_tanggal_bayar);
            mTextNominalPinjaman = itemView.findViewById(R.id.text_nominal_pinjaman);
            mCardRequest = itemView.findViewById(R.id.card_approval);

            // ON CLICK LISTENER
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    // sent on click to activity
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
