package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.roomsMenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.roomsMenu.counter.BookingTmpRooms;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.rooms.RoomDetailActivity;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyAddRoom;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyDomainSistem;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyRemoveRoom;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.cekIdxTmp;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.cekJmlKamar;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BookListingAdapter extends RecyclerView.Adapter<BookListingAdapter.BookViewHolder>   {

    private final Context context;
    private final Activity activity;

    private final List<BookList> bookListList;
    private ArrayList<BookingTmpRooms> arrayList;

    public BookListingAdapter(List<BookList> bookLists, Context context, Activity activity){
        this.bookListList = bookLists;
        this.context = context;
        this.activity = activity;
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        //Views
        private final ImageView fotoKamar;
        final TextView hargaKamar, namaKamar, kapasitasKamar, order, detail;


        //Initializing Views
        BookViewHolder(View itemView) {

            super(itemView);
            fotoKamar = itemView.findViewById(R.id.fotoKamar);
            namaKamar = itemView.findViewById(R.id.namaKamar);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            kapasitasKamar = itemView.findViewById(R.id.kapasitasKamar);
            order = itemView.findViewById(R.id.txtOrder);
            detail = itemView.findViewById(R.id.txtDetail);

        }
    }

    @NonNull
    @Override
    public BookListingAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book,parent,false);
        return new BookListingAdapter.BookViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BookListingAdapter.BookViewHolder holder, final int postition) {
        arrayList = new ArrayList<>();
        final BookList bookList = bookListList.get(postition);
        Glide.with(context).load(keyDomainSistem+bookList.getFoto())
                .placeholder(R.drawable.image_slider_1)
                .thumbnail(0.5f)
                .centerCrop()
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.fotoKamar);
        holder.namaKamar.setText(bookList.getTipe());
        holder.hargaKamar.setText(formatingRupiah(bookList.getHarga()));
        holder.kapasitasKamar.setText(bookList.getKapasitas()+" "+context.getResources().getString(R.string.orang));
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RoomDetailActivity.class);
                i.putExtra("posisi",bookList.getId_tipe());
                context.startActivity(i);
            }
        });
        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.order.getText().toString().equals(context.getResources().getString(R.string.order))){
                    addRoom(bookList, v, holder, postition);
                }else {
                    removeRoom(bookList, v, holder, postition);
                }
            }
        });
    }

    private void removeRoom(BookList bookList, final View v, final BookViewHolder holder, int position) {
        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.bukaDialog();
        int tmp = cekIdxTmp(arrayList, position);
        String uTmp = String.valueOf(arrayList.get(tmp).getIdTemp());

        StringRequest strReq = new StringRequest(Request.Method.DELETE, keyRemoveRoom+uTmp,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        loadingDialog.tutupDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.getString("msg").equals("Berhasil dihapus!")){
                                Snackbar.make(v, R.string.bisa, Snackbar.LENGTH_SHORT).show();
                                holder.order.setText(R.string.order);
                                holder.order.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                            }else {
                                Snackbar.make(v, R.string.gagal, Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", SPData.getInstance(activity).getKeyToken() );
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void addRoom(final BookList bookList, final View v, final BookListingAdapter.BookViewHolder holder, final int position) {
        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.bukaDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, keyAddRoom+bookList.getId_tipe(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("msg").equals("Berhasil disimpan")){
                        arrayList.add(new BookingTmpRooms(jObj.getString("id_temp"), position));
                        Snackbar.make(v, R.string.bisa, Snackbar.LENGTH_SHORT).show();
                        holder.order.setText(R.string.hapus);
                        holder.order.setBackgroundColor(context.getResources().getColor(R.color.abang));
                    }else {
                        Snackbar.make(v, R.string.gagal, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tgl_checkin", getIntentData(activity,"ci"));
                params.put("tgl_checkout",getIntentData(activity,"co"));
                params.put("jml_kamar", String.valueOf(cekJmlKamar(Integer.parseInt(getIntentData(activity,"or")))));
                return params;
            }

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json; charset=UTF-8");
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", SPData.getInstance(activity).getKeyToken() );
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    @Override
    public int getItemCount() {
        return bookListList.size();
    }

}