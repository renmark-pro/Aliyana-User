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
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.FasilitasList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.FasilitasListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_FASILITAS_LIST;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;

public class FasilitasActivity extends AppCompatActivity {

    ArrayList<FasilitasList> arrayList;
    private RecyclerView.Adapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas);
        setTemaAplikasi(FasilitasActivity.this, 1);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.goldtua), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        arrayList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.fasilitasList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        adapter = new FasilitasListAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final FasilitasList produk = arrayList.get(position);
                String id = produk.getId();
                Intent i = new Intent(getApplicationContext(), FasilitasDetailActivity.class);
                i.putExtra("posisi",id);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getDetail() {
        @SuppressLint("StaticFieldLeak")
        class getDetail extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FasilitasActivity.this, getResources().getString(R.string.tunggu), "", false, false);
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
                return rh.sendGetRequest(KEY_FASILITAS_LIST);
            }
        }
        getDetail gd = new getDetail();
        gd.execute();
    }

    private void showDetail(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("fasilitas");
            for(int i =0;i<result.length(); i++) {
                JSONObject productObject = result.getJSONObject(i);
                arrayList.add(new FasilitasList(
                        productObject.getString("id"),
                        productObject.getString("nama"),
                        productObject.getString("lokasi")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }
}
