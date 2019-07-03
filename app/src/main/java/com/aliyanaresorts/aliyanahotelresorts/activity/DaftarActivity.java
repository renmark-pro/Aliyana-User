package com.aliyanaresorts.aliyanahotelresorts.activity;

import androidx.appcompat.app.AppCompatActivity;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.isValidMail;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.isValidMobile;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarGoldTrans;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DAFTAR;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_SLIDE_HOME;

public class DaftarActivity extends AppCompatActivity {

    private FlipperLayout flipper;
    private EditText mnama, memail, mtelepon,mpassword, mcpassword;
    private ConnectivityManager conMgr;
    private ArrayList<HashMap<String, String>> list_dataS;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        setStyleStatusBarGoldTrans(this);
        mnama = findViewById(R.id.nama);
        memail = findViewById(R.id.email);
        mtelepon = findViewById(R.id.telpon);
        mpassword = findViewById(R.id.password);
        mcpassword = findViewById(R.id.kpassword);
        Button masuk = findViewById(R.id.btnMasuk);
        Button daftar = findViewById(R.id.btnDaftar);
        flipper = findViewById(R.id.flipper);

        setSlide();

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (Objects.requireNonNull(conMgr).getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(DaftarActivity.this);
                String nama = mnama.getText().toString();
                String email = memail.getText().toString();
                String telepon = mtelepon.getText().toString();
                String password = mpassword.getText().toString();
                String cpassword = mcpassword.getText().toString();

                if (mnama.getText().toString().isEmpty()){
                    mnama.requestFocus();
                    mnama.setError(getResources().getString(R.string.isi));
                }else if(memail.getText().toString().isEmpty()){
                    memail.requestFocus();
                    memail.setError(getResources().getString(R.string.isi));
                }else if(!isValidMail(memail.getText().toString())){
                    memail.requestFocus();
                    memail.setError(getResources().getString(R.string.imail));
                }else if(mtelepon.getText().toString().isEmpty()){
                    mtelepon.requestFocus();
                    mtelepon.setError(getResources().getString(R.string.isi));
                }else if(!isValidMobile(mtelepon.getText().toString())){
                    mtelepon.requestFocus();
                    mtelepon.setError(getResources().getString(R.string.ihp));
                }else if(mpassword.getText().toString().isEmpty()){
                    mpassword.requestFocus();
                    mpassword.setError(getResources().getString(R.string.isi));
                }else if(mpassword.getText().toString().length()<6){
                    mpassword.requestFocus();
                    mpassword.setError(getResources().getString(R.string.ipw));
                }else if(mcpassword.getText().toString().isEmpty()){
                    mcpassword.requestFocus();
                    mcpassword.setError(getResources().getString(R.string.isi));
                }else if (!cpassword.equals(password)){
                    Snackbar.make(v, R.string.passalah, Snackbar.LENGTH_SHORT).show();
                }else {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        closeKeyboard(DaftarActivity.this);
                        checkRegister(nama, email, telepon, password, cpassword, v);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkRegister( final String nama, final String email, final String telepon, final String password, final String cpassword, final View view) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, KEY_DAFTAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(DaftarActivity.class.getSimpleName(), "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("msg").equals("Registrasi berhasil!")) {
                        Snackbar.make(view, R.string.bdaftar, Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }else{
                        Snackbar.make(view, R.string.xdaftar, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(DaftarActivity.class.getSimpleName(), "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("no_telepon", telepon);
                params.put("password", password);
                params.put("password_confirmation", cpassword);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void setSlide() {
        RequestQueue requestQueueS = Volley.newRequestQueue(DaftarActivity.this);

        list_dataS = new ArrayList<>();

        StringRequest stringRequestS = new StringRequest(Request.Method.GET, KEY_SLIDE_HOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("slideshow");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", json.getString("id"));
                                map.put("foto", json.getString("foto"));
                                map.put("judul", json.getString("judul"));
                                list_dataS.add(map);
                                FlipperView view = new FlipperView(getBaseContext());
                                view.setImageUrl(KEY_DOMAIN+list_dataS.get(a).get("foto"))
                                        .setDescription(list_dataS.get(a).get("judul"))
                                        .setDescriptionBackgroundColor(getResources()
                                                .getColor(R.color.hitamtrans));
                                flipper.setCircularIndicatorLayoutParams(0,0);
                                flipper.addFlipperView(view);
                                final int finalA = a;
                                view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                                    @Override
                                    public void onFlipperClick(FlipperView flipperView) {
                                        Snackbar.make(flipper, Objects.requireNonNull(list_dataS.get(finalA).get("judul")), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaftarActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueS
                .add(stringRequestS)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
