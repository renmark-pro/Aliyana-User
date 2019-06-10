package com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.RestoList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RestoListAdapter extends RecyclerView.Adapter<RestoListAdapter.RestoViewHolder> {

    private Context context;

    private List<RestoList> restoLists;

    public RestoListAdapter(List<RestoList> restoLists1, Context context){
        this.restoLists = restoLists1;
        this.context = context;
    }


    class RestoViewHolder extends RecyclerView.ViewHolder {
        //Views
        TextView namaMenu, hargaMenu;
        ImageView fotoMenu;


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
    public RestoListAdapter.RestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ex_resto,parent,false);
        return new RestoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoListAdapter.RestoViewHolder holder, int postition) {
        RestoList restoList = restoLists.get(postition);
        holder.namaMenu.setText(restoList.getNama());
        Glide.with(context).load(restoList.getFoto())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoMenu);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.hargaMenu.setText(formatRupiah.format((double)Double.valueOf(restoList.getHarga())));
    }

    @Override
    public int getItemCount() {
        return restoLists.size();
    }

}
