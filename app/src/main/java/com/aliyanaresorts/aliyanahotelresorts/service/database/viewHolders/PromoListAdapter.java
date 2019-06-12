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
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.PromoList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PromoListAdapter extends RecyclerView.Adapter<PromoListAdapter.PromoViewHolder> {

    private final Context context;

    private final List<PromoList> promoLists;

    public PromoListAdapter(List<PromoList> promoLists1, Context context){
        this.promoLists = promoLists1;
        this.context = context;
    }


    class PromoViewHolder extends RecyclerView.ViewHolder {
        //Views
        private final ImageView fotoPromo;
        final TextView namaPromo,deskPromo;


        //Initializing Views
        PromoViewHolder(View itemView) {

            super(itemView);
            fotoPromo = itemView.findViewById(R.id.fotoPromo);
            namaPromo = itemView.findViewById(R.id.namaPromo);
            deskPromo = itemView.findViewById(R.id.deskPromo);

        }
    }

    @NonNull
    @Override
    public PromoListAdapter.PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_promo,parent,false);
        return new PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoListAdapter.PromoViewHolder holder, int postition) {
        PromoList promoList = promoLists.get(postition);
        Glide.with(context).load(promoList.getFoto())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoPromo);
        holder.namaPromo.setText(promoList.getJudul());
        holder.deskPromo.setText(promoList.getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return promoLists.size();
    }

}
