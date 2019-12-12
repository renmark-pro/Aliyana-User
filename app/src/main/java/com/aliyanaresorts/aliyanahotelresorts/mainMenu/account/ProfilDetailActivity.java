package com.aliyanaresorts.aliyanahotelresorts.mainMenu.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyGetUser;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyUpdateUser;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getStatusBarHeight;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isValidMail;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isValidMobile;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setFotoUser;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setTextData;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setStyleStatusBarGoldTrans;

public class ProfilDetailActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;
    private NiceSpinner jenisId;
    private EditText namaUser, nomerIdUser, emailUser, telponUser, alamatUser;
    private ImageView fotoUser;
    private CountryCodePicker codePicker;
    private Button ubah;
    private LinearLayout layEdit;
    private Activity activity;
    private SPData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_detail);

        activity = this;
        data = SPData.getInstance(activity);
        setStyleStatusBarGoldTrans(activity);
        loadingDialog = new LoadingDialog(activity);
        LinearLayout root = findViewById(R.id.layoutRoot);
        root.setPadding(16, getStatusBarHeight(activity)+16, 16 , 16 );

        jenisId=findViewById(R.id.jenisId);
        namaUser=findViewById(R.id.nama);
        nomerIdUser=findViewById(R.id.nomerId);
        emailUser=findViewById(R.id.email);
        telponUser=findViewById(R.id.telpon);
        alamatUser=findViewById(R.id.alamat);
        codePicker = findViewById(R.id.ccp);
        RelativeLayout foto = findViewById(R.id.layoutFoto);
        fotoUser = findViewById(R.id.fotoUser);
        Button simpan = findViewById(R.id.btnUpdate);
        Button batal = findViewById(R.id.btnBatal);
        ubah = findViewById(R.id.btnEdit);
        layEdit = findViewById(R.id.layEdit);
        final NoInetDialog noInetDialog = new NoInetDialog(activity);

        List<String> datajenis = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.jenisid)));
        jenisId.attachDataSource(datajenis);

        jenisId.setText(data.getKeyJenisId());
        namaUser.setText(data.getKeyNama());
        nomerIdUser.setText(data.getKeyNomerId());
        emailUser.setText(data.getKeyEmail());
        telponUser.setText(data.getKeyTelepon());
        alamatUser.setText(data.getKeyAlamat());
        codePicker.setCountryForPhoneCode(1);

        telponUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBuka();
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTutup();
            }
        });

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
                }else if (uEmail.isEmpty()) {
                    emailUser.requestFocus();
                    emailUser.setError(getResources().getString(R.string.isi));
                }else if (!isValidMail(uEmail)){
                    emailUser.requestFocus();
                    emailUser.setError(getResources().getString(R.string.imail));
                }else if (uTelpon.isEmpty()) {
                    telponUser.requestFocus();
                    telponUser.setError(getResources().getString(R.string.isi));
                }else if (!isValidMobile(uTelpon)){
                    telponUser.requestFocus();
                    telponUser.setError(getResources().getString(R.string.ihp));
                }else if (jenisId.getSelectedIndex()==0){
                    jenisId.requestFocus();
                    jenisId.setError(getResources().getString(R.string.isi));
                    closeKeyboard(activity);
                    Snackbar.make(v, getResources().getString(R.string.isi), Snackbar.LENGTH_SHORT).show();
                }else if (uNomerId.isEmpty()){
                    nomerIdUser.requestFocus();
                    nomerIdUser.setError(getResources().getString(R.string.isi));
                }else if (uAlamat.isEmpty()){
                    alamatUser.requestFocus();
                    alamatUser.setError(getResources().getString(R.string.isi));
                }else  if(isNetworkNotAvailable(getBaseContext())){
                    noInetDialog.bukaDialog();
                }else{
                    closeKeyboard(activity);
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

    private void updateProfile(final View v, final String uNama, final String uEmail,
                               final String uTelpon, final String uTipe, final String uNomerId,
                               final String uAlamat) {
        loadingDialog.bukaDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, keyUpdateUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
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
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
                setTutup();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", uNama);
                params.put("email",uEmail);
                params.put("kd_negara", codePicker.getSelectedCountryCode());
                params.put("no_telepon", uTelpon);
                params.put("tipe_identitas", uTipe);
                params.put("no_identitas", uNomerId);
                params.put("alamat", uAlamat);
                return params;
            }

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json; charset=UTF-8");
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", data.getKeyToken() );
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    private void getDetail() {
        loadingDialog.bukaDialog();
        closeKeyboard(activity);

        StringRequest strReq = new StringRequest(Request.Method.GET, keyGetUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (fotoUser.getDrawable()!=getResources().getDrawable(R.drawable.image_slider_1)){
                    loadingDialog.tutupDialog();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("user");
                    namaUser.setText(setTextData(result.getString("nama")));
                    emailUser.setText(setTextData(result.getString("email")));
                    jenisId.setSelectedIndex(setSpinnerId(result.getString("tipe_identitas")));
                    nomerIdUser.setText(setTextData(result.getString("no_identitas")));
                    codePicker.setCountryForPhoneCode(Integer.parseInt(result.getString("kd_negara")));
                    telponUser.setText(setTextData(result.getString("no_telepon")));
                    alamatUser.setText(setTextData(result.getString("alamat")));
                    setFotoUser(result.getString("foto"), activity, fotoUser);
                    data.updateBio(
                            result.getString("nama"),
                            result.getString("email"),
                            result.getString("tipe_identitas"),
                            result.getString("no_identitas"),
                            result.getString("kd_negara"),
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

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
        setTutup();
    }

    private void setTutup(){
        layEdit.setVisibility(View.GONE);
        ubah.setVisibility(View.VISIBLE);
        namaUser.setEnabled(false);
        nomerIdUser.setEnabled(false);
        emailUser.setEnabled(false);
        telponUser.setEnabled(false);
        alamatUser.setEnabled(false);
        jenisId.setEnabled(false);
        codePicker.setCcpClickable(false);
        namaUser.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        nomerIdUser.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        emailUser.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        telponUser.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        alamatUser.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        jenisId.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        codePicker.setContentColor(getResources().getColor(android.R.color.tab_indicator_text));
    }

    private void setBuka(){
        layEdit.setVisibility(View.VISIBLE);
        ubah.setVisibility(View.GONE);
        namaUser.setEnabled(true);
        nomerIdUser.setEnabled(true);
        emailUser.setEnabled(true);
        telponUser.setEnabled(true);
        alamatUser.setEnabled(true);
        jenisId.setEnabled(true);
        codePicker.setCcpClickable(true);
        namaUser.setTextColor(getResources().getColor(R.color.goldtua));
        nomerIdUser.setTextColor(getResources().getColor(R.color.goldtua));
        emailUser.setTextColor(getResources().getColor(R.color.goldtua));
        telponUser.setTextColor(getResources().getColor(R.color.goldtua));
        alamatUser.setTextColor(getResources().getColor(R.color.goldtua));
        jenisId.setTextColor(getResources().getColor(R.color.goldtua));
        codePicker.setContentColor(getResources().getColor(R.color.goldtua));
        codePicker.setCountryForPhoneCode(Integer.parseInt(data.getKeyKdTelepon()));
    }

}
