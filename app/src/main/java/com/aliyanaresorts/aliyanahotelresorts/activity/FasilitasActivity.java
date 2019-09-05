package com.aliyanaresorts.aliyanahotelresorts.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.FasilitasList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.FasilitasListAdapter;
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
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.isNetworkAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_FASILITAS_LIST;

public class FasilitasActivity extends AppCompatActivity {

    private ArrayList<FasilitasList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas);
        setTemaAplikasi(FasilitasActivity.this, 1);
        final NoInetDialog noInetDialog = new NoInetDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
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
                if(isNetworkAvailable(getBaseContext())){
                    noInetDialog.bukaDialog();
                }else {
                    final FasilitasList produk = arrayList.get(position);
                    String id = produk.getId();
                    Intent i = new Intent(getApplicationContext(), FasilitasDetailActivity.class);
                    i.putExtra("posisi", id);
                    startActivity(i);
                }
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

        StringRequest strReq = new StringRequest(Request.Method.GET, KEY_FASILITAS_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
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
                    // JSON error
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
