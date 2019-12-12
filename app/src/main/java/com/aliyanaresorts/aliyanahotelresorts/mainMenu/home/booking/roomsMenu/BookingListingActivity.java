package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.roomsMenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.mInterface.RecyclerTouchListener;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.previewBooking.PreviewBookingActivity;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyAddRoomCount;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyCekKamar;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyDropTmp;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setTemaAplikasi;

public class BookingListingActivity extends AppCompatActivity {

    private ArrayList<BookList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog, dialog;
    private TextView hitungan;
    private LinearLayout proses;
    private int counter;
    private String ci, co, or, id;
    private SwipeRefreshLayout swipe;
    private Activity activity;
    private SPData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_listing);
        
        activity = this;
        data = SPData.getInstance(activity);
        setTemaAplikasi(this, 1);

        proses = findViewById(R.id.layoutProses);
        hitungan=findViewById(R.id.txtCount);
        swipe = findViewById(R.id.swipeLayout);
        final NoInetDialog noInetDialog = new NoInetDialog(this);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        @SuppressLint("PrivateResource") final Drawable upArrow = ContextCompat
                .getDrawable(this, R.drawable.abc_ic_ab_back_material);
        Objects.requireNonNull(upArrow).setColorFilter(ContextCompat.getColor(this,
                R.color.goldtua), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remData();
                finish();
            }
        });

        ci = getIntentData(this,"ci");
        co = getIntentData(this,"co");
        or = getIntentData(this,"or");
        id = getIntentData(this,"id");

        Resources res = getResources();
        swipe.setColorSchemeColors(res.getColor(R.color.colorPrimaryDark), res.getColor(R.color.colorPrimary));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getData(ci, co, or, id);
                swipe.setRefreshing(false);
            }
        });

        arrayList = new ArrayList<>();
        final RecyclerView recyclerView = findViewById(R.id.roomLists);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        getData(ci, co, or, id);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        adapter = new BookListingAdapter(arrayList, this, activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if(isNetworkNotAvailable(activity)){
                    noInetDialog.bukaDialog();
                }else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addCount();
                        }
                    }, 500L);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hitungan.getText().toString().isEmpty()||counter<1){
                    Snackbar.make(v, R.string.blmpesan, Snackbar.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("ci",getIntentData(activity,"ci"));
                    bundle.putString("co", getIntentData(activity,"co"));
                    bundle.putString("or", getIntentData(activity,"or"));
                    Intent i = new Intent(activity, PreviewBookingActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void addCount() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.bukaDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, keyAddRoomCount, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    hitungan.setText(" "+jsonObject.getString("jumlah")+" ");
                    counter = Integer.parseInt(jsonObject.getString("jumlah"));
                    if (counter!=0) {
                        proses.setBackground(getResources().getDrawable(R.drawable.bt_book));
                    }else {
                        proses.setBackground(getResources().getDrawable(R.drawable.bt_cek));
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", data.getKeyToken() );
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void getData(final String cekin, final String cekout, final String tamu, final String id){

        dialog = new LoadingDialog(this);
        dialog.bukaDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, keyCekKamar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("kamar");
                    for(int i =0;i<result.length(); i++) {
                        JSONObject productObject = result.getJSONObject(i);
                        arrayList.add(new BookList(
                                productObject.getString("id_tipe"),
                                productObject.getString("tipe"),
                                productObject.getString("foto"),
                                productObject.getString("kapasitas"),
                                productObject.getString("harga"),
                                productObject.getString("jml_kamar")
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
                dialog.tutupDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json; charset=UTF-8");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", data.getKeyToken());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tgl_checkin", cekin);
                params.put("tgl_checkout",cekout);
                params.put("jml_tamu", tamu);
                params.put("tipe", id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        remData();
    }

    private void remData() {
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.bukaDialog();

        StringRequest strReq = new StringRequest(Request.Method.DELETE, keyDropTmp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.tutupDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.tutupDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", data.getKeyToken() );
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
