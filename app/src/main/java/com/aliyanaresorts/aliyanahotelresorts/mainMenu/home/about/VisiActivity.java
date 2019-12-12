package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.about;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyTentangHotel;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getStatusBarHeight;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.remTagHtml;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setStyleStatusBarGoldTrans;

public class VisiActivity extends AppCompatActivity {

    private TextView visi, misi;
    private LoadingDialog loadingDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visi);

        activity = this;
        setStyleStatusBarGoldTrans(activity);
        loadingDialog = new LoadingDialog(activity);
        RelativeLayout root = findViewById(R.id.layoutRoot);
        root.setPadding(16, getStatusBarHeight(this)+16, 16 , 16 );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        visi = findViewById(R.id.txtVisi);
        misi = findViewById(R.id.txtMisi);

        getDataVisi();
}

    private void getDataVisi() {
        loadingDialog.bukaDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, keyTentangHotel,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    loadingDialog.tutupDialog();
                    visi.setText(remTagHtml(data.getString("visi")));
                    misi.setText(remTagHtml(data.getString("misi")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue
                .add(stringRequest)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
