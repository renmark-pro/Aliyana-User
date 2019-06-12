package com.aliyanaresorts.aliyanahotelresorts.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.ReqHandler;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.RestoList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.RestoListAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarTransparent;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_MENU_RESTO;

public class RestoActivity extends AppCompatActivity {

    private ArrayList<RestoList> arrayList;
    private RecyclerView.Adapter adapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto);
        setStyleStatusBarTransparent(this);

        arrayList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.restoList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        adapter = new RestoListAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        collapsingToolbarLayout = findViewById(R.id.coll);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    //  on Collapse
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.kcr));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                } else {
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle("\t");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                }
            }
        });

//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                final RestoList produk = arrayList.get(position);
//                String id = produk.getId();
//                Intent i = new Intent(getApplicationContext(), FasilitasDetailActivity.class);
//                i.putExtra("posisi",id);
//                startActivity(i);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

    }

    private void getDetail() {
        @SuppressLint("StaticFieldLeak")
        class getDetail extends AsyncTask<Void, Void, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RestoActivity.this, getResources().getString(R.string.tunggu), "", false, false);
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
                return rh.sendGetRequest(KEY_MENU_RESTO);
            }
        }
        getDetail gd = new getDetail();
        gd.execute();
    }

    private void showDetail(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");
            for(int i =0;i<result.length(); i++) {
                JSONObject productObject = result.getJSONObject(i);
                arrayList.add(new RestoList(
                        productObject.getString("id"),
                        productObject.getString("menu"),
                        productObject.getString("nama"),
                        productObject.getString("harga"),
                        productObject.getString("catatan"),
                        productObject.getString("foto")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

    }
}
