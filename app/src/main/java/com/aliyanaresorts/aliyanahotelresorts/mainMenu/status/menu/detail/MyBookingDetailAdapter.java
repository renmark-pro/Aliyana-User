package com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.menu.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;

import java.util.List;

public class MyBookingDetailAdapter extends RecyclerView.Adapter<MyBookingDetailAdapter.BookViewHolder> {

    private final List<MyBookingDetailList> bookListList;

    public MyBookingDetailAdapter(List<MyBookingDetailList> bookLists){
        this.bookListList = bookLists;
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        //Views
        final TextView noKamar, cekIn, cekOut, tamuKamar, tipeKamar;


        //Initializing Views
        BookViewHolder(View itemView) {

            super(itemView);
            noKamar = itemView.findViewById(R.id.noKamar);
            cekIn = itemView.findViewById(R.id.cekIn);
            cekOut = itemView.findViewById(R.id.cekOut);
            tamuKamar = itemView.findViewById(R.id.tamuKamar);
            tipeKamar = itemView.findViewById(R.id.tipeKamar);

        }
    }

    @NonNull
    @Override
    public MyBookingDetailAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proses_detail,parent,false);
        return new MyBookingDetailAdapter.BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBookingDetailAdapter.BookViewHolder holder, final int postition) {
        final MyBookingDetailList bookList = bookListList.get(postition);
//        holder.noKamar.setText(bookList.getNo_room());
        holder.cekIn.setText(bookList.getTgl_checkin());
        holder.cekOut.setText(bookList.getTgl_checkout());
//        holder.tamuKamar.setText(bookList.getJml_tamu());
        holder.tipeKamar.setText(bookList.getTipe());
    }

    @Override
    public int getItemCount() {
        return bookListList.size();
    }

}
