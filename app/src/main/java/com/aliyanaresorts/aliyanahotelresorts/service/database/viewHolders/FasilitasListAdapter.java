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
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.FasilitasList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FasilitasListAdapter extends RecyclerView.Adapter<FasilitasListAdapter.FasilitasViewHolder>   {

    private final Context context;

    private final List<FasilitasList> fasilitasLists;

    public FasilitasListAdapter(List<FasilitasList> fasilitasLists1, Context context){
        this.fasilitasLists = fasilitasLists1;
        this.context = context;
    }


    class FasilitasViewHolder extends RecyclerView.ViewHolder {
        //Views
        private final  ImageView fotoFasilitas;
        final TextView namaFasilitas;


        //Initializing Views
        FasilitasViewHolder(View itemView) {

            super(itemView);
            fotoFasilitas = itemView.findViewById(R.id.fotoFasilitas);
            namaFasilitas = itemView.findViewById(R.id.namaFasilitas);

        }
    }

    @NonNull
    @Override
    public FasilitasListAdapter.FasilitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fasilitas,parent,false);
        return new FasilitasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FasilitasListAdapter.FasilitasViewHolder holder, int postition) {
        FasilitasList fasilitasList = fasilitasLists.get(postition);
        Glide.with(context).load(KEY_DOMAIN+fasilitasList.getLokasi())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoFasilitas);
        holder.namaFasilitas.setText(fasilitasList.getNama());
    }

    @Override
    public int getItemCount() {
        return fasilitasLists.size();
    }

}
