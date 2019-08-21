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
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.KamarList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN_SISTEM;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class KamarListAdapter extends RecyclerView.Adapter<KamarListAdapter.KamarViewHolder> {

    private final Context context;

    private final List<KamarList> kamarListList;

    public KamarListAdapter(List<KamarList> kamarLists, Context context){
        this.kamarListList = kamarLists;
        this.context = context;
    }


    class KamarViewHolder extends RecyclerView.ViewHolder {
        //Views
        private final ImageView fotoKamar;
        final TextView hargaKamar,namaKamar;


        //Initializing Views
        KamarViewHolder(View itemView) {

            super(itemView);
            fotoKamar = itemView.findViewById(R.id.fotoKamar);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            namaKamar = itemView.findViewById(R.id.namaKamar);

        }
    }

    @NonNull
    @Override
    public KamarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_room,parent,false);
        return new KamarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KamarViewHolder holder, int postition) {
        KamarList kamarList = kamarListList.get(postition);
        Glide.with(context).load(KEY_DOMAIN_SISTEM+kamarList.getLokasi())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoKamar);
        holder.hargaKamar.setText(formatingRupiah(kamarList.getHarga()));
        holder.namaKamar.setText(kamarList.getTipe());
    }

    @Override
    public int getItemCount() {
        return kamarListList.size();
    }

}
