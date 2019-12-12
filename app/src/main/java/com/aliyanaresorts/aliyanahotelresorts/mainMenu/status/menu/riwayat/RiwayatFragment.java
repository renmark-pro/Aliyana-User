package com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.menu.riwayat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.mInterface.RecyclerTouchListener;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.MyBookingList;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.status.menu.detail.MyBookingDetailActivity;
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
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyHistoryList;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setViewStatus;

public class RiwayatFragment extends Fragment {

    private ArrayList<MyBookingList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;
    private NestedScrollView nestedLayout;
    private RelativeLayout kosongLayout;
    private Activity activity;
    private SPData data;

    public RiwayatFragment() {
        //Construct
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_riwayat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        data = SPData.getInstance(activity);
        loadingDialog = new LoadingDialog(activity);
        nestedLayout = view.findViewById(R.id.nestedLayout);
        kosongLayout = view.findViewById(R.id.layKosong);
        setViewStatus(0, nestedLayout, kosongLayout);
        arrayList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.riwayatList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        adapter = new RiwayatAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        final NoInetDialog noInetDialog = new NoInetDialog(getActivity());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(isNetworkNotAvailable(Objects.requireNonNull(getContext()))){
                    noInetDialog.bukaDialog();
                }else {
                    final MyBookingList produk = arrayList.get(position);
                    String id = produk.getKode_booking();
                    Intent i = new Intent(activity, MyBookingDetailActivity.class);
                    i.putExtra("kode", id);
                    startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getDetail() {
        loadingDialog.bukaDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET, keyHistoryList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("bookings");
                    if (result.length()>0){
                        for(int i =0;i<result.length(); i++) {
                            JSONObject productObject = result.getJSONObject(i);
                            arrayList.add(new MyBookingList(
                                    productObject.getString("id"),
                                    productObject.getString("kode_booking"),
                                    productObject.getString("tipe"),
                                    productObject.getString("jml_kamar"),
                                    productObject.getString("tgl_checkin"),
                                    productObject.getString("tgl_checkout"),
                                    productObject.getString("total_tagihan"),
                                    productObject.getString("terbayarkan"),
                                    productObject.getString("kekurangan"),
                                    productObject.getString("status")
                            ));
                        }
                    }
                    setViewStatus(result.length(), nestedLayout, kosongLayout);
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
