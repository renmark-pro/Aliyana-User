package com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.activity.MasukActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.RoomDetailActivity;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.BookList;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.formatingRupiah;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.getIntentData;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_ADD_ROOM;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_REMOVE_ROOM;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BookListingAdapter extends RecyclerView.Adapter<BookListingAdapter.BookViewHolder>   {

    private final Context context;
    private Activity activity;

    private final List<BookList> bookListList;

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
        final BookList bookList = bookListList.get(postition);
        Glide.with(context).load(KEY_DOMAIN+bookList.getFoto())
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
                    addRoom(bookList, v, holder);
                }else {
                    removeRoom(bookList, v, holder);
                }
            }
        });
    }

    private void removeRoom(BookList bookList, final View v, final BookViewHolder holder) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(context.getResources().getString(R.string.tunggu));
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.DELETE, KEY_REMOVE_ROOM+bookList.getNo_room(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(MasukActivity.class.getSimpleName(), "Getting Response: "+ response);
                        pDialog.dismiss();
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
                pDialog.dismiss();

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

    private void addRoom(final BookList bookList, final View v, final BookListingAdapter.BookViewHolder holder) {
        Log.e("ci ", getIntentData(activity,"ci"));
        Log.e("co ", getIntentData(activity,"co"));
        Log.e("no_room ", bookList.getNo_room());
        Log.e("harga ", bookList.getHarga());
        Log.e("jml_tamu ", getIntentData(activity,"or"));
        Log.e("auth", SPData.getInstance(activity).getKeyToken());
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(context.getResources().getString(R.string.tunggu));
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, KEY_ADD_ROOM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(MasukActivity.class.getSimpleName(), "Getting Response: "+ response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("msg").equals("Berhasil disimpan")){
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
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tgl_checkin", getIntentData(activity,"ci"));
                params.put("tgl_checkout",getIntentData(activity,"co"));
                params.put("no_room", bookList.getNo_room());
                params.put("harga", bookList.getHarga());
                params.put("jml_tamu", getIntentData(activity,"or"));
                return params;
            }

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

    @Override
    public int getItemCount() {
        return bookListList.size();
    }

}