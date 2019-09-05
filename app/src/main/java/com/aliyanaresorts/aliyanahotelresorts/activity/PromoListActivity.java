package com.aliyanaresorts.aliyanahotelresorts.activity;

import android.annotation.SuppressLint;
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
import com.aliyanaresorts.aliyanahotelresorts.service.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.PromoList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.PromoListAdapter;
import com.aliyanaresorts.aliyanahotelresorts.service.mInterface.RecyclerTouchListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_PROMO_LIST;

public class PromoListActivity extends AppCompatActivity {

    private ArrayList<PromoList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_list);
        setTemaAplikasi(this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this, R.color.goldtua), PorterDuff.Mode.SRC_ATOP);
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
        adapter = new PromoListAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final PromoList produk = arrayList.get(position);
                String id = produk.getId();
                Intent i = new Intent(getApplicationContext(), PromoDetailActivity.class);
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

        StringRequest strReq = new StringRequest(Request.Method.GET, KEY_PROMO_LIST, new Response.Listener<String>() {

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
