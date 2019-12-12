package com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.menu.proses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.MyBookingList;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.menu.detail.MyBookingDetailActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setWarnaButtonProses;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setWarnaStatus;

public class ProsesStatusListAdapter extends RecyclerView.Adapter<ProsesStatusListAdapter.BookViewHolder> {

    private final Activity activity;
    private final Context context;

    private final List<MyBookingList> bookListList;

    public ProsesStatusListAdapter(List<MyBookingList> bookLists, Activity activity, Context context){
        this.bookListList = bookLists;
        this.activity = activity;
        this.context = context;
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        //Views
        final TextView kodeBooking, jmlKamar, totalHarga, cekIn, cekOut, statusBayar, btnProses;
        final LinearLayout layoutDetail;


        //Initializing Views
        BookViewHolder(View itemView) {

            super(itemView);
            kodeBooking = itemView.findViewById(R.id.kodeBook);
            jmlKamar = itemView.findViewById(R.id.jmlKamar);
            totalHarga = itemView.findViewById(R.id.totalHarga);
            cekIn = itemView.findViewById(R.id.cekIn);
            cekOut = itemView.findViewById(R.id.cekOut);
            statusBayar = itemView.findViewById(R.id.statusBayar);
            btnProses = itemView.findViewById(R.id.btnProses);
            layoutDetail = itemView.findViewById(R.id.layoutDetail);

        }
    }

    @NonNull
    @Override
    public ProsesStatusListAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses_status,parent,false);
        return new ProsesStatusListAdapter.BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProsesStatusListAdapter.BookViewHolder holder, final int postition) {
        final NoInetDialog noInetDialog = new NoInetDialog(activity);
        final MyBookingList bookList = bookListList.get(postition);
        holder.kodeBooking.setText(bookList.getKode_booking());
        holder.jmlKamar.setText(bookList.getJml_kamar());
        holder.totalHarga.setText(formatingRupiah(bookList.getTotal_tagihan()));
        holder.cekIn.setText(bookList.getTgl_checkin());
        holder.cekOut.setText(bookList.getTgl_checkout());
        holder.statusBayar.setText(bookList.getStatus());
        setWarnaStatus(activity, holder.statusBayar);
        setWarnaButtonProses(activity, bookList.getStatus(), holder.btnProses);
        holder.btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Oke", Snackbar.LENGTH_SHORT).show();
            }
        });
        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkNotAvailable(context)){
                    noInetDialog.bukaDialog();
                }else {
                    Intent i = new Intent(context, MyBookingDetailActivity.class);
                    i.putExtra("kode", bookList.getKode_booking());
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookListList.size();
    }
}
