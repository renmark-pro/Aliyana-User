package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyDomainSistem;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.formatingRupiah;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private final Context context;

    private final List<RoomList> roomListLists;

    public RoomAdapter(List<RoomList> roomLists, Context context){
        this.roomListLists = roomLists;
        this.context = context;
    }


    class RoomViewHolder extends RecyclerView.ViewHolder {
        //Views
        private final ImageView fotoKamar;
        final TextView hargaKamar,namaKamar;


        //Initializing Views
        RoomViewHolder(View itemView) {

            super(itemView);
            fotoKamar = itemView.findViewById(R.id.fotoKamar);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            namaKamar = itemView.findViewById(R.id.namaKamar);

        }
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_room,parent,false);
        return new RoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int postition) {
        RoomList roomLists = roomListLists.get(postition);
        Glide.with(context).load(keyDomainSistem+ roomLists.getLokasi())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoKamar);
        holder.hargaKamar.setText(formatingRupiah(roomLists.getHarga()));
        holder.namaKamar.setText(roomLists.getTipe());
    }

    @Override
    public int getItemCount() {
        return roomListLists.size();
    }

}
