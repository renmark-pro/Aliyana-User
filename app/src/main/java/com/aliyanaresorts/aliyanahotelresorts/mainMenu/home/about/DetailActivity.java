package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.about;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

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

public class DetailActivity extends AppCompatActivity {

    private CardView lokasi , email, wa, web, phone, fax, ig, fb;
    private TextView textView;

    private Activity activity;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        activity = this;
        loadingDialog = new LoadingDialog(activity);
        RelativeLayout root = findViewById(R.id.layoutRoot);
        root.setPadding(16, getStatusBarHeight(activity)+16, 16 , 16 );
        setStyleStatusBarGoldTrans(activity);

        lokasi = findViewById(R.id.btLokasi);
        email = findViewById(R.id.btEmail);
        wa = findViewById(R.id.btWa);
        web = findViewById(R.id.btWeb);
        phone = findViewById(R.id.btPhone);
        fax = findViewById(R.id.btFax);
        ig = findViewById(R.id.btIg);
        fb = findViewById(R.id.btFb);
        textView = findViewById(R.id.textTentang);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getDataDetail();

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void getDataDetail() {
        loadingDialog.bukaDialog();

        final RequestQueue requestQueue = Volley.newRequestQueue(activity);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, keyTentangHotel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.tutupDialog();
                    final JSONObject data = new JSONObject(response);

                    final Uri uFb = Uri.parse(data.getString("facebook"));
                    final Uri uFax = Uri.parse("tel:" + data.getString("fax"));
                    final Uri uIg = Uri.parse(data.getString("instagram"));
                    final String uEmail =data.getString("mail1") + ";" +
                            data.getString("mail2");
                    final Uri uPhone = Uri.parse("tel:" + data.getString("phone"));
                    final Uri uWa = Uri.parse("https://api.whatsapp.com/send?phone="
                            + data.getString("whatsapp")
                            + "&text=" + getResources().getString(R.string.txtMail));
                    final Uri uWeb = Uri.parse(data.getString("web"));
                    textView.setText(remTagHtml(data.getString("tentang")));

                    lokasi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri location = Uri.parse("geo:0,0?q=Aliyana+Hotel+%26+Resort");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                            startActivity(mapIntent);
                        }
                    });
                    fb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent kefb = new Intent(Intent.ACTION_VIEW, uFb);
                            startActivity(kefb);
                        }
                    });
                    fax.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent faxIntent = new Intent(Intent.ACTION_DIAL, uFax);
                            startActivity(faxIntent);
                        }
                    });
                    ig.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent keig = new Intent(Intent.ACTION_VIEW, uIg);
                            startActivity(keig);
                        }
                    });
                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{uEmail}); // recipients
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.txtMail));
                            startActivity(emailIntent);
                        }
                    });
                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent faxIntent = new Intent(Intent.ACTION_DIAL, uPhone);
                            startActivity(faxIntent);
                        }
                    });
                    wa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent waIntent = new Intent(Intent.ACTION_VIEW, uWa);
                            startActivity(waIntent);
                        }
                    });
                    web.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent keweb = new Intent(Intent.ACTION_VIEW, uWeb);
                            startActivity(keweb);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue
                .add(stringRequest)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
