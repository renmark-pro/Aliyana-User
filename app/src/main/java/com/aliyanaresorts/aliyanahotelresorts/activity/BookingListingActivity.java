package com.aliyanaresorts.aliyanahotelresorts.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.Interface.RecyclerTouchListener;
import com.aliyanaresorts.aliyanahotelresorts.service.database.ReqHandler;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.BookList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.KamarList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.BookListAdapter;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.KamarListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.activity.BookingActivity.listOke;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_KAMAR_LIST;

public class BookingListingActivity extends AppCompatActivity {

    private ArrayList<BookList> arrayList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_listing);
        setTemaAplikasi(this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.goldtua), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        arrayList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.roomList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        adapter = new BookListAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getDetail() {
        @SuppressLint("StaticFieldLeak")
        class getDetail extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BookingListingActivity.this, "", getResources().getString(R.string.tunggu), false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showDetail(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                ReqHandler rh = new ReqHandler();
                return rh.sendGetRequest(KEY_KAMAR_LIST);
            }
        }
        getDetail gd = new getDetail();
        gd.execute();
    }

    private void showDetail(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("kamar");
            for(int i =0;i<listOke.size(); i++) {
                JSONObject productObject = result.getJSONObject(i);
                for(int j=0; j<result.length();j++) {
                    if (i==0){
                        arrayList.add(new BookList(
                                productObject.getString("id"),
                                productObject.getString("tipe"),
                                productObject.getString("harga"),
                                productObject.getString("kapasitas"),
                                productObject.getString("lokasi")
                        ));
                    }else{
                        if (!listOke.get(i).equals(listOke.get(i-1))){
                            arrayList.add(new BookList(
                                    productObject.getString("id"),
                                    productObject.getString("tipe"),
                                    productObject.getString("harga"),
                                    productObject.getString("kapasitas"),
                                    productObject.getString("lokasi")
                            ));
                        }
                    }


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
