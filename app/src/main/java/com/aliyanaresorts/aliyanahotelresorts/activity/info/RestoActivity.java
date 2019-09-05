package com.aliyanaresorts.aliyanahotelresorts.activity.info;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.RestoList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.RestoListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarTransparent;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_MENU_RESTO;

public class RestoActivity extends AppCompatActivity {

    private ArrayList<RestoList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto);
        setStyleStatusBarTransparent(this);

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
        RecyclerView recyclerView = findViewById(R.id.restoList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new RestoListAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        collapsingToolbarLayout = findViewById(R.id.coll);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  on Collapse
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.kcr));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.putih));
                    collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.transparan));
                    collapsingToolbarLayout.setContentScrim(new ColorDrawable(getResources().getColor(R.color.hitamtrans)));
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
        loadingDialog = new LoadingDialog(this);
        loadingDialog.bukaDialog();
        closeKeyboard(this);

        StringRequest strReq = new StringRequest(Request.Method.GET, KEY_MENU_RESTO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("menu");
                    for (int i = 0; i < result.length(); i++) {
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