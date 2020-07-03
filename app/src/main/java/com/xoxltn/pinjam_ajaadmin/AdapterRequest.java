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

public class AdapterRequest extends FirestoreRecyclerAdapter<ModelRequest,
        AdapterRequest.RequestHolder> {

    private OnItemClickListener listener;

    public AdapterRequest(@NonNull FirestoreRecyclerOptions<ModelRequest> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestHolder holder, int position,
                                    @NonNull ModelRequest model) {

        Date date = model.getPinjaman_tanggal_req();
        String date_now = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        holder.mTextViewTanggal.setText(date_now);

        int pinjaman = model.getPinjaman_besar();
        switch (pinjaman) {
            case 500000: {
                String pinjaman_now = "Rp. 500.000,-";

                holder.mTextViewNominalPinjaman.setText(pinjaman_now);
                holder.mCardRequest.setCardBackgroundColor(0xFFEDEFD3);
                break;
            }
            case 1000000: {
                String pinjaman_now = "Rp. 1.000.000,-";
                holder.mTextViewNominalPinjaman.setText(pinjaman_now);
                holder.mCardRequest.setCardBackgroundColor(0xFFD3EFEB);

                break;
            }
            case 1500000: {
                String pinjaman_now = "Rp. 1.500.000,-";
                holder.mTextViewNominalPinjaman.setText(pinjaman_now);
                holder.mCardRequest.setCardBackgroundColor(0xFFEFD3DC);
                break;
            }
        }

    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request,
                parent, false);
        return new RequestHolder(v);
    }

    //-------------------------------------------------------------------------------------------//

    class RequestHolder extends RecyclerView.ViewHolder {

        TextView mTextViewTanggal;
        TextView mTextViewNominalPinjaman;
        CardView mCardRequest;

        RequestHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTanggal = itemView.findViewById(R.id.req_tanggal_permintaan);
            mTextViewNominalPinjaman = itemView.findViewById(R.id.req_nominal_pinjaman);
            mCardRequest = itemView.findViewById(R.id.card_request);

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