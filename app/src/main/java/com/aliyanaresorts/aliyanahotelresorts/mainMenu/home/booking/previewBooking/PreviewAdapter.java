package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.previewBooking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;

import java.util.List;

import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder> {

    private final Activity activity;

    private final List<PreviewList> previewListList;

    public PreviewAdapter(List<PreviewList> previewLists, Activity activity){
        this.previewListList = previewLists;
        this.activity = activity;
    }


    class PreviewViewHolder extends RecyclerView.ViewHolder {
        //Views
        final TextView tipeKamar, hargaKamar, kapasitasKamar, tamuKamar, jumlahKamar, cekIn, cekOut, subTotal;


        //Initializing Views
        PreviewViewHolder(View itemView) {

            super(itemView);
            tipeKamar = itemView.findViewById(R.id.tipeKamar);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            kapasitasKamar = itemView.findViewById(R.id.kapasitasKamar);
            tamuKamar = itemView.findViewById(R.id.tamuKamar);
            jumlahKamar = itemView.findViewById(R.id.jmlKamar);
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
        holder.tipeKamar.setText(previewList.getTipe());
        holder.hargaKamar.setText(formatingRupiah(previewList.getHarga()));
        holder.kapasitasKamar.setText(previewList.getKapasitas());
        holder.tamuKamar.setText(getIntentData(activity,"or"));
        holder.jumlahKamar.setText(previewList.getJml_kamar());
        holder.cekIn.setText(getIntentData(activity,"ci"));
        holder.cekOut.setText(getIntentData(activity,"co"));
        holder.subTotal.setText(formatingRupiah(previewList.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return previewListList.size();
    }

}
