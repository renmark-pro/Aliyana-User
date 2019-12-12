package com.aliyanaresorts.aliyanahotelresorts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
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

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyDomainSistem;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyMasuk;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keySlideHome;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isValidMail;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setStyleStatusBarGoldTrans;

public class MasukActivity extends AppCompatActivity {

    private FlipperLayout flipper;
    private ArrayList<HashMap<String, String>> list_dataS;
    private EditText mEmail, mpassword;
    private LoadingDialog loadingDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        activity = this;
        setStyleStatusBarGoldTrans(this);
        loadingDialog = new LoadingDialog(this);

        flipper = findViewById(R.id.flipper);
        Button masuk = findViewById(R.id.btnMasuk);
        Button daftar = findViewById(R.id.btnDaftar);
        mEmail= findViewById(R.id.email);
        mpassword= findViewById(R.id.password);
        final NoInetDialog noInetDialog = new NoInetDialog(this);

        setSlide();

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(activity);

                String email = mEmail.getText().toString();
                String password = mpassword.getText().toString();

                if (email.isEmpty()) {
                    mEmail.requestFocus();
                    mEmail.setError(getResources().getString(R.string.isi));
                } else if (!isValidMail(email)){
                    mEmail.requestFocus();
                    mEmail.setError(getResources().getString(R.string.imail));
                } else if (password.isEmpty()) {
                    mpassword.setError(getResources().getString(R.string.isi));
                    mpassword.requestFocus();
                }else if(isNetworkNotAvailable(getBaseContext())){
                    noInetDialog.bukaDialog();
                }else {
                    checkLogin(email, password, v);
                }
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DaftarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkLogin(final String email, final String password, final View view) {
        loadingDialog.bukaDialog();
        closeKeyboard(this);

        StringRequest strReq = new StringRequest(Request.Method.POST, keyMasuk, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("msg").equals("1")) {
                        JSONObject child= jObj.getJSONObject("user");
                        SPData.getInstance(getApplicationContext()).userLogin(
                                child.getString("id"),
                                child.getString("nama"),
                                child.getString("email"),
                                child.getString("tipe_identitas"),
                                child.getString("no_identitas"),
                                child.getString("kd_negara"),
                                child.getString("no_telepon"),
                                child.getString("alamat"),
                                child.getString("foto"),
                                jObj.getString("access_token")
                        );
                        Snackbar.make(view, R.string.bmasuk, Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    } else {
                        Snackbar.make(view, R.string.xmasuk, Snackbar.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json; charset=UTF-8");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void setSlide() {
        RequestQueue requestQueueS = Volley.newRequestQueue(this);
        list_dataS = new ArrayList<>();
        StringRequest stringRequestS = new StringRequest(Request.Method.GET, keySlideHome,
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
                                view.setImageUrl(keyDomainSistem+list_dataS.get(a).get("foto"))
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
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueS
                .add(stringRequestS)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
