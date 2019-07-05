package com.aliyanaresorts.aliyanahotelresorts.activity.account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.activity.MasukActivity;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.getStatusBarHeight;
import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.setTextData;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setStyleStatusBarGoldTrans;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_GET_USER;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_UPDATE_USER;

public class ProfilDetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private NiceSpinner jenisId;
    private TextView namaUser, nomerIdUser, emailUser, telponUser, alamatUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_detail);
        setStyleStatusBarGoldTrans(this);
        LinearLayout root = findViewById(R.id.layoutRoot);
        root.setPadding(16, getStatusBarHeight(this)+16, 16 , 16 );

        jenisId=findViewById(R.id.jenisId);
        namaUser=findViewById(R.id.nama);
        nomerIdUser=findViewById(R.id.nomerId);
        emailUser=findViewById(R.id.email);
        telponUser=findViewById(R.id.telpon);
        alamatUser=findViewById(R.id.alamat);
        RelativeLayout foto = findViewById(R.id.layoutFoto);
        Button simpan = findViewById(R.id.btnUpdate);

        List<String> datajenis = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.jenisid)));
        jenisId.attachDataSource(datajenis);

        getDetail();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uNama = namaUser.getText().toString();
                String uEmail = emailUser.getText().toString();
                String uTelpon = telponUser.getText().toString();
                String uTipe = jenisId.getText().toString();
                String uNomerId = nomerIdUser.getText().toString();
                String uAlamat = alamatUser.getText().toString();

                if (uNama.isEmpty()){
                    namaUser.requestFocus();
                    namaUser.setError(getResources().getString(R.string.isi));
                }else if (uEmail.isEmpty()){
                    emailUser.requestFocus();
                    emailUser.setError(getResources().getString(R.string.isi));
                }else if (uTelpon.isEmpty()){
                    telponUser.requestFocus();
                    telponUser.setError(getResources().getString(R.string.isi));
                }else if (jenisId.getSelectedIndex()==0){
                    jenisId.requestFocus();
                    jenisId.setError(getResources().getString(R.string.isi));
                    closeKeyboard(ProfilDetailActivity.this);
                    Snackbar.make(v, getResources().getString(R.string.isi), Snackbar.LENGTH_SHORT).show();
                }else if (uNomerId.isEmpty()){
                    nomerIdUser.requestFocus();
                    nomerIdUser.setError(getResources().getString(R.string.isi));
                }else if (uAlamat.isEmpty()){
                    alamatUser.requestFocus();
                    alamatUser.setError(getResources().getString(R.string.isi));
                }else {
                    closeKeyboard(ProfilDetailActivity.this);
                    updateProfile(v, uNama, uEmail, uTelpon, uTipe, uNomerId, uAlamat);
                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilDetailActivity.this, FotoProfilAccountActivity.class));
            }
        });

    }

    private void updateProfile(final View v, final String uNama, final String uEmail, final String uTelpon, final String uTipe, final String uNomerId, final String uAlamat) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getResources().getString(R.string.tunggu));
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, KEY_UPDATE_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(MasukActivity.class.getSimpleName(), "Getting Response: "+ response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getString("msg").equals("Profil berhasil diupdate")){
                        Snackbar.make(v, R.string.bisa, Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recreate();
                            }
                        }, 2000L);
                    }else {
                        Snackbar.make(v, R.string.gagal, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MasukActivity.class.getSimpleName(), "Login Error: " + error);
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", uNama);
                params.put("email",uEmail);
                params.put("no_telepon", uTelpon);
                params.put("tipe_identitas", uTipe);
                params.put("no_identitas", uNomerId);
                params.put("alamat", uAlamat);
                return params;
            }

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", SPData.getInstance(ProfilDetailActivity.this).getKeyToken() );
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void getDetail() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage(getResources().getString(R.string.tunggu));
        pDialog.show();
        closeKeyboard(this);
        Log.e("TOKEN : ", SPData.getInstance(this).getKeyToken());

        StringRequest strReq = new StringRequest(Request.Method.GET, KEY_GET_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(MasukActivity.class.getSimpleName(), "Login Response: " + response);
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("user");
                    namaUser.setText(setTextData(result.getString("nama")));
                    emailUser.setText(setTextData(result.getString("email")));
                    jenisId.setSelectedIndex(setSpinnerId(result.getString("tipe_identitas")));
                    nomerIdUser.setText(setTextData(result.getString("no_identitas")));
                    telponUser.setText(setTextData(result.getString("no_telepon")));
                    alamatUser.setText(setTextData(result.getString("alamat")));
                    SPData.getInstance(getApplicationContext()).updateBio(
                            result.getString("nama"),
                            result.getString("email"),
                            result.getString("tipe_identitas"),
                            result.getString("no_identitas"),
                            result.getString("no_telepon"),
                            result.getString("alamat")
                    );
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MasukActivity.class.getSimpleName(), "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                pDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", SPData.getInstance(ProfilDetailActivity.this).getKeyToken() );
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private int setSpinnerId(String nama){
        int index;
        switch (nama) {
            case "KTP":
                index = 1;
                break;
            case "SIM":
                index = 2;
                break;
            case "PASSPORT":
                index = 3;
                break;
            default:
                index = 0;
                break;
        }
        return index;
    }
}
