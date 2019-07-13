package com.aliyanaresorts.aliyanahotelresorts.activity.status.fragment;

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
import com.aliyanaresorts.aliyanahotelresorts.activity.status.MyBookingDetailActivity;
import com.aliyanaresorts.aliyanahotelresorts.service.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.aliyanaresorts.aliyanahotelresorts.service.database.models.RiwayatStatusList;
import com.aliyanaresorts.aliyanahotelresorts.service.database.viewHolders.RiwayatStatusListAdapter;
import com.aliyanaresorts.aliyanahotelresorts.service.mInterface.RecyclerTouchListener;
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

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.isNetworkAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.setViewStatus;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_HISTORY_LIST;

public class RiwayatStatusFragment extends Fragment {

    private ArrayList<RiwayatStatusList> arrayList;
    private RecyclerView.Adapter adapter;
    private LoadingDialog loadingDialog;
    private NestedScrollView nestedLayout;
    private RelativeLayout kosongLayout;

    public RiwayatStatusFragment() {
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
        nestedLayout = view.findViewById(R.id.nestedLayout);
        kosongLayout = view.findViewById(R.id.layKosong);
        setViewStatus(0, nestedLayout, kosongLayout);
        arrayList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.riwayatList);
        recyclerView.setHasFixedSize(true);
        getDetail();
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        adapter = new RiwayatStatusListAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        final NoInetDialog noInetDialog = new NoInetDialog(getActivity());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isNetworkAvailable(Objects.requireNonNull(getContext()))){
                    noInetDialog.bukaDialog();
                }else {
                    final RiwayatStatusList produk = arrayList.get(position);
                    String id = produk.getKode_booking();
                    Intent i = new Intent(getContext(), MyBookingDetailActivity.class);
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
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.bukaDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, KEY_HISTORY_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("bookings");
                    if (result.length()>0){
                        for(int i =0;i<result.length(); i++) {
                            JSONObject productObject = result.getJSONObject(i);
                            arrayList.add(new RiwayatStatusList(
                                    productObject.getString("id"),
                                    productObject.getString("kode_booking"),
                                    productObject.getString("jml_kamar"),
                                    productObject.getString("total"),
                                    productObject.getString("tgl_checkin"),
                                    productObject.getString("tgl_checkout"),
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
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", SPData.getInstance(getActivity()).getKeyToken() );
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
}
