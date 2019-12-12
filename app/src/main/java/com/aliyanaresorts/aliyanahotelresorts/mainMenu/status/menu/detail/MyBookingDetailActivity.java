package com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.menu.detail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyBookingDetail;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setTemaAplikasi;

public class MyBookingDetailActivity extends AppCompatActivity {

    private ArrayList<MyBookingDetailList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;
    private TextView kodeBooking;
    private Activity activity;
    private SPData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_detail);
        setTemaAplikasi(this, 1);

        activity = this;
        data = SPData.getInstance(activity);
        loadingDialog = new LoadingDialog(activity);
        kodeBooking = findViewById(R.id.kodeBook);
        arrayList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.prosesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getBaseContext());
        adapter = new MyBookingDetailAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getDetail() {
        loadingDialog.bukaDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET, keyBookingDetail +
                getIntentData(this, "kode"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("booking");
                    for(int i =0;i<result.length(); i++) {
                        JSONObject productObject = result.getJSONObject(i);
                        arrayList.add(new MyBookingDetailList(
                                productObject.getString("kode_booking"),
                                productObject.getString("tipe"),
                                productObject.getString("jml_kamar"),
                                productObject.getString("tgl_checkin"),
                                productObject.getString("tgl_checkout"),
                                productObject.getString("total_tagihan"),
                                productObject.getString("terbayarkan"),
                                productObject.getString("hutang"),
                                productObject.getString("status")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                kodeBooking.setText(arrayList.get(0).getKode_booking());
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", data.getKeyToken() );
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
