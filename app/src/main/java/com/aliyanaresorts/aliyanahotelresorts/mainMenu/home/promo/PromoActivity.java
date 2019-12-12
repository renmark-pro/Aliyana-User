package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.promo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.mInterface.RecyclerTouchListener;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyPromoList;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setTemaAplikasi;

public class PromoActivity extends AppCompatActivity {

    private ArrayList<PromoList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        activity = this;
        setTemaAplikasi(this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat
                .getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this,
                R.color.goldtua), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final SwipeRefreshLayout swipe = findViewById(R.id.swipeLayout);
        Resources res = getResources();
        swipe.setColorSchemeColors(res.getColor(R.color.colorPrimaryDark), res.getColor(R.color.colorPrimary));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getDetail();
                swipe.setRefreshing(false);
            }
        });

        arrayList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.promoList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        adapter = new PromoAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final PromoList produk = arrayList.get(position);
                String id = produk.getId();
                Intent i = new Intent(activity, PromoDetailActivity.class);
                i.putExtra("posisi",id);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void getDetail() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.bukaDialog();
        closeKeyboard(this);

        StringRequest strReq = new StringRequest(Request.Method.GET, keyPromoList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for(int i =0;i<result.length(); i++) {
                        JSONObject productObject = result.getJSONObject(i);
                        arrayList.add(new PromoList(
                                productObject.getString("id"),
                                productObject.getString("judul"),
                                productObject.getString("deskripsi"),
                                productObject.getString("foto")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
