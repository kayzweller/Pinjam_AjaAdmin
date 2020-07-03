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

public class AdapterReturn extends FirestoreRecyclerAdapter<ModelReturn, AdapterReturn.ReturnHolder>{

    private OnItemClickListener listener;

    public AdapterReturn(@NonNull FirestoreRecyclerOptions<ModelReturn> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReturnHolder holder, int position,
                                    @NonNull ModelReturn model) {

        // get database data and set to recycler view here!

        int tahap_pinjaman = model.getPinjaman_tahap();
        if (tahap_pinjaman == 0) {
            String set = "--";
            holder.mTextTahapPinjaman.setText(set);
        } else if (tahap_pinjaman == 1) {
            String set = "Cicilan Pertama";
            holder.mTextTahapPinjaman.setText(set);
        } else if (tahap_pinjaman == 2) {
            String set = "Cicilan Kedua";
            holder.mTextTahapPinjaman.setText(set);
        } else if (tahap_pinjaman == 3) {
            String set = "Cicilan Terakhir";
            holder.mTextTahapPinjaman.setText(set);
        }

        Date date = model.getPinjaman_tanggal_transfer();
        String date_now = DateFormat.getDateInstance(DateFormat.FULL).format(date);
        holder.mTextTanggalTransfer.setText(date_now);

        int pinjaman = model.getPinjaman_besar();
        switch (pinjaman) {
            case 500000: {
                String pinjaman_now = "Rp. 500.000,-";

                holder.mTextNominalPinjaman.setText(pinjaman_now);
                holder.mCardReturn.setCardBackgroundColor(0xFFEDEFD3);
                break;
            }
            case 1000000: {
                String pinjaman_now = "Rp. 1.000.000,-";
                holder.mTextNominalPinjaman.setText(pinjaman_now);
                holder.mCardReturn.setCardBackgroundColor(0xFFD3EFEB);

                break;
            }
            case 1500000: {
                String pinjaman_now = "Rp. 1.500.000,-";
                holder.mTextNominalPinjaman.setText(pinjaman_now);
                holder.mCardReturn.setCardBackgroundColor(0xFFEFD3DC);
                break;
            }
        }
    }

    @NonNull
    @Override
    public ReturnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_return,
                parent, false);
        return new ReturnHolder(v);
    }

    //-------------------------------------------------------------------------------------------//

    class ReturnHolder extends RecyclerView.ViewHolder {

        TextView mTextTahapPinjaman;
        TextView mTextTanggalTransfer;
        TextView mTextNominalPinjaman;
        CardView mCardReturn;

        ReturnHolder(@NonNull View itemView) {
            super(itemView);

            mTextTahapPinjaman = itemView.findViewById(R.id.text_tahap_pinjaman);
            mTextTanggalTransfer = itemView.findViewById(R.id.text_tanggal_bayar);
            mTextNominalPinjaman = itemView.findViewById(R.id.text_nominal_pinjaman);
            mCardReturn = itemView.findViewById(R.id.card_return);

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