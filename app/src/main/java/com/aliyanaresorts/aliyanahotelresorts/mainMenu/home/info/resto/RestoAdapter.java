package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.info.resto;

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

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.RestoViewHolder> {

    private final Context context;

    private final List<RestoList> restoLists;

    public RestoAdapter(List<RestoList> restoLists1, Context context){
        this.restoLists = restoLists1;
        this.context = context;
    }


    class RestoViewHolder extends RecyclerView.ViewHolder {
        //Views
        final TextView namaMenu, hargaMenu;
        final ImageView fotoMenu;


        //Initializing Views
        RestoViewHolder(View itemView) {

            super(itemView);
            namaMenu = itemView.findViewById(R.id.namaMenu);
            hargaMenu = itemView.findViewById(R.id.hargaMenu);
            fotoMenu = itemView.findViewById(R.id.fotoMenu);

        }
    }

    @NonNull
    @Override
    public RestoAdapter.RestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resto,parent,false);
        return new RestoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoAdapter.RestoViewHolder holder, int postition) {
        RestoList restoList = restoLists.get(postition);
        holder.namaMenu.setText(restoList.getNama());
        Glide.with(context).load(keyDomainSistem+restoList.getFoto())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoMenu);
        holder.hargaMenu.setText(formatingRupiah(restoList.getHarga()));
    }

    @Override
    public int getItemCount() {
        return restoLists.size();
    }

}
