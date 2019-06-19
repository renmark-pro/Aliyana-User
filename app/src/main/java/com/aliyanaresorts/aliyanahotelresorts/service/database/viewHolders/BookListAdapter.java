package com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.BookList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder>   {

    private final Context context;

    private final List<BookList> bookListList;

    public BookListAdapter(List<BookList> bookLists, Context context){
        this.bookListList = bookLists;
        this.context = context;
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        //Views
        private final ImageView fotoKamar;
        final TextView hargaKamar, namaKamar, kapasitasKamar;
        Button order;


        //Initializing Views
        BookViewHolder(View itemView) {

            super(itemView);
            fotoKamar = itemView.findViewById(R.id.fotoKamar);
            namaKamar = itemView.findViewById(R.id.namaKamar);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            kapasitasKamar = itemView.findViewById(R.id.kapasitasKamar);
            order = itemView.findViewById(R.id.btnKirim);

        }
    }

    @NonNull
    @Override
    public BookListAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book,parent,false);
        return new BookListAdapter.BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.BookViewHolder holder, int postition) {
        BookList bookList = bookListList.get(postition);
        Glide.with(context).load(KEY_DOMAIN+bookList.getLokasi())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoKamar);
        holder.namaKamar.setText(bookList.getTipe());
        holder.hargaKamar.setText(bookList.getHarga());
        holder.kapasitasKamar.setText(bookList.getKapasitas()+context.getResources().getString(R.string.orang));
    }

    @Override
    public int getItemCount() {
        return bookListList.size();
    }

}
