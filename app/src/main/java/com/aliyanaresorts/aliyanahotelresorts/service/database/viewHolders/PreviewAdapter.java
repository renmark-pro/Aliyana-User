package com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.PreviewList;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.getIntentData;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder> {

    private final Activity activity;

    private final List<PreviewList> previewListList;

    public PreviewAdapter(List<PreviewList> previewLists, Activity activity){
        this.previewListList = previewLists;
        this.activity = activity;
    }


    class PreviewViewHolder extends RecyclerView.ViewHolder {
        //Views
        final TextView noKamar,tipeKamar, kapasitasKamar, tamuKamar, cekIn, cekOut, subTotal;


        //Initializing Views
        PreviewViewHolder(View itemView) {

            super(itemView);
            noKamar = itemView.findViewById(R.id.noKamar);
            tipeKamar = itemView.findViewById(R.id.tipeKamar);
            kapasitasKamar = itemView.findViewById(R.id.kapasitasKamar);
            tamuKamar = itemView.findViewById(R.id.tamuKamar);
            cekIn = itemView.findViewById(R.id.cekIn);
            cekOut = itemView.findViewById(R.id.cekOut);
            subTotal = itemView.findViewById(R.id.subTotal);

        }
    }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_preview_book,parent,false);
        return new PreviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder holder, int postition) {
        PreviewList previewList = previewListList.get(postition);
        holder.noKamar.setText(previewList.getNo_room());
        holder.tipeKamar.setText(previewList.getTipe());
        holder.kapasitasKamar.setText(previewList.getKapasitas());
        holder.tamuKamar.setText(previewList.getJml_tamu());
        holder.cekIn.setText(getIntentData(activity,"ci"));
        holder.cekOut.setText(getIntentData(activity,"co"));
        holder.subTotal.setText(formatingRupiah(previewList.getHarga()));
    }

    @Override
    public int getItemCount() {
        return previewListList.size();
    }

}
