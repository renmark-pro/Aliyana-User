package com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.MyBookingList;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.setWarnaStatus;

public class RiwayatStatusListAdapter extends RecyclerView.Adapter<RiwayatStatusListAdapter.BookViewHolder> {

    private final Activity activity;
    private final List<MyBookingList> bookListList;

    public RiwayatStatusListAdapter(List<MyBookingList> bookLists, Activity activity){
        this.bookListList = bookLists;
        this.activity = activity;
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        //Views
        final TextView kodeBooking, jmlKamar, totalHarga, cekIn, cekOut, statusBayar;


        //Initializing Views
        BookViewHolder(View itemView) {

            super(itemView);
            kodeBooking = itemView.findViewById(R.id.kodeBook);
            jmlKamar = itemView.findViewById(R.id.jmlKamar);
            totalHarga = itemView.findViewById(R.id.totalHarga);
            cekIn = itemView.findViewById(R.id.cekIn);
            cekOut = itemView.findViewById(R.id.cekOut);
            statusBayar = itemView.findViewById(R.id.statusBayar);

        }
    }

    @NonNull
    @Override
    public RiwayatStatusListAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat_status,parent,false);
        return new RiwayatStatusListAdapter.BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RiwayatStatusListAdapter.BookViewHolder holder, final int postition) {
        final MyBookingList bookList = bookListList.get(postition);
        holder.kodeBooking.setText(bookList.getKode_booking());
        holder.jmlKamar.setText(bookList.getJml_kamar());
        holder.totalHarga.setText(formatingRupiah(bookList.getTotal_tagihan()));
        holder.cekIn.setText(bookList.getTgl_checkin());
        holder.cekOut.setText(bookList.getTgl_checkout());
        holder.statusBayar.setText(bookList.getStatus());
        setWarnaStatus(activity, holder.statusBayar);
    }

    @Override
    public int getItemCount() {
        return bookListList.size();
    }
}
