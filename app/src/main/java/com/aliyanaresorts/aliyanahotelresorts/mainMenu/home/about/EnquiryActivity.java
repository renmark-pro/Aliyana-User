package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.about;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyEnq;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getStatusBarHeight;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isValidMail;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setStyleStatusBarGoldTrans;

public class EnquiryActivity extends AppCompatActivity {

    private EditText mNama, mEmail, mIsi;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);

        activity = this;
        setStyleStatusBarGoldTrans(activity);
        RelativeLayout root = findViewById(R.id.layoutRoot);
        root.setPadding(16, getStatusBarHeight(this)+16, 16 , 16 );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mNama = findViewById(R.id.nama);
        mEmail = findViewById(R.id.email);
        mIsi = findViewById(R.id.isi);
        Button submit = findViewById(R.id.btnKirim);
        final NoInetDialog noInetDialog = new NoInetDialog(activity);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(activity);

                String nama = mNama.getText().toString();
                String email = mEmail.getText().toString();
                String isi = mIsi.getText().toString();

                if (nama.isEmpty()){
                    mNama.requestFocus();
                    mNama.setError(getResources().getString(R.string.kolom));
                }else if (email.isEmpty()){
                    mEmail.requestFocus();
                    mEmail.setError(getResources().getString(R.string.kolom));
                }else if (!isValidMail(email)){
                    mEmail.requestFocus();
                    mEmail.setError(getResources().getString(R.string.imail));
                }else if (isi.isEmpty()){
                    mIsi.requestFocus();
                    mIsi.setError(getResources().getString(R.string.kolom));
                }else if(isNetworkNotAvailable(getBaseContext())){
                    noInetDialog.bukaDialog();
                }else {
                    kirim(v, nama, email, isi);
                }
            }
        });

    }

    private void kirim(final View v, final String nama, final String email, final String isi) {
        final LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.bukaDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, keyEnq,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int success = jObj.getInt("success");
                            if (success == 1) {
                                Snackbar.make(v, R.string.bisa, Snackbar.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mNama.setText("");
                                        mEmail.setText("");
                                        mIsi.setText("");
                                    }
                                }, 1500);
                            }else{
                                Snackbar.make(v, R.string.gagal, Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDialog.tutupDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.tutupDialog();
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("nama", nama);
                params.put("email",email);
                params.put("isi",isi);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "json_obj_req");
    }
}
