package com.aliyanaresorts.aliyanahotelresorts.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.aliyanaresorts.aliyanahotelresorts.service.database.AppController;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.android.material.textfield.TextInputEditText;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_CEK_KAMAR;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_TIPE_KAMAR;

public class BookingActivity extends AppCompatActivity{

    private TextView tanggal, malam, txt;
    private DateRangeCalendarView calendar;
    private SimpleDateFormat format, upload;
    private EditText jmlOrang;
    private TextInputEditText penggunaKamar;

    public static ArrayList <String> listOke;

    String cek_in=null, cek_out=null, uCekIn, uCekOut, id_kamar;
    String spin=null;
    int pos;

    ProgressDialog pDialog;
    ArrayList<String> tipe;
    NiceSpinner kamar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTemaAplikasi(this, 1);

        getTypes();

        kamar = findViewById(R.id.kamar);

        Button cek = findViewById(R.id.btnCek);
        tanggal=findViewById(R.id.tanggal);
        malam=findViewById(R.id.malam);
        txt=findViewById(R.id.txt);
        calendar = findViewById(R.id.calendar);
        penggunaKamar = findViewById(R.id.nama);
        jmlOrang = findViewById(R.id.jml_org);
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
        upload = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        calendar.setFonts(typeface);

        spin=getIntent().getStringExtra("posisi");

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(BookingActivity.this);
                if (calendar.isShown()){
                    calendar.setVisibility(View.GONE);
                    tanggal.setText(R.string.piltgl);
                    if (cek_in==null||cek_out==null){
                        tanggal.setText(R.string.piltgl);
                    }else {
                        String tgl = cek_in+" - "+cek_out;
                        tanggal.setText(tgl);
                    }
                }else {
                    destroyCache();
                    calendar.setVisibility(View.VISIBLE);
                    tanggal.setText(R.string.simpan);
                }
            }
        });

        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                cek_in = format.format(startDate.getTime());
                uCekIn = upload.format(startDate.getTime());
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                cek_out = format.format(endDate.getTime());
                uCekOut = upload.format(endDate.getTime());
                long startDateMillis = startDate.getTimeInMillis();
                long endDateMillis = endDate.getTimeInMillis();
                long differenceMillis = endDateMillis - startDateMillis;
                int daysDifference = (int) (differenceMillis / (1000 * 60 * 60 * 24));
                String swap;
                if (startDateMillis>endDateMillis){
                    swap = cek_in;
                    cek_in = cek_out;
                    cek_out = swap;
                    uCekOut = cek_out;
                }
                if (daysDifference>1){
                    txt.setText(R.string.malams);
                }else {
                    txt.setText(R.string.malam);
                }
                malam.setText(String.valueOf(daysDifference));
            }

        });

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -2);
        Calendar later = (Calendar) now.clone();
        later.add(Calendar.MONTH, 3);

        calendar.setVisibleMonthRange(now,later);

        Calendar startSelectionDate = Calendar.getInstance();
        startSelectionDate.add(Calendar.DAY_OF_MONTH, 0);
        Calendar endSelectionDate = (Calendar) startSelectionDate.clone();
        endSelectionDate.add(Calendar.DATE, 0);

        calendar.setSelectedDateRange(startSelectionDate, endSelectionDate);

        Calendar current = Calendar.getInstance();
        calendar.setCurrentMonth(current);

        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(BookingActivity.this);
                if(SPData.getInstance(BookingActivity.this).isLoggedIn()){
                    String nPengguna = Objects.requireNonNull(penggunaKamar.getText()).toString();
//                    String nPemesan = SPData.getInstance(getBaseContext()).getKeyNama();
//                    String tKamar = kamar.getText().toString();
                    String tMasuk = tanggal.getText().toString();
//                    String jMalam = malam.getText().toString();
                    String jOrang = jmlOrang.getText().toString();

                    if (nPengguna.isEmpty()){
                        penggunaKamar.setError(getResources().getString(R.string.kolom));
                        penggunaKamar.requestFocus();
                    }else if (kamar.getSelectedIndex()==0){
                        kamar.requestFocus();
                    }else if (tMasuk.equals(getResources().getString(R.string.piltgl))){
                        calendar.requestFocus();
                    }else if (jOrang.isEmpty()){
                        jmlOrang.requestFocus();
                        jmlOrang.setError(getResources().getString(R.string.kolom));
                    }else {
                        if (tipe.get(kamar.getSelectedIndex()).equals(getResources().getString(R.string.semua))){
                            id_kamar="0";
                        }else {
                            id_kamar = tipe.get(kamar.getSelectedIndex());
                        }
                        cekTersedia(uCekIn, uCekOut,jOrang, id_kamar);
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.logindulu);
                    builder.setPositiveButton(R.string.logy, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(BookingActivity.this, MasukActivity.class));
                        }
                    });
                    builder.setNegativeButton(R.string.logn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

    }

    private void getTypes() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getResources().getString(R.string.tunggu));
        pDialog.show();

        RequestQueue requestQueueS = Volley.newRequestQueue(BookingActivity.this);

        tipe = new ArrayList<>();
        tipe.add(getResources().getString(R.string.pilih));
        tipe.add(getResources().getString(R.string.semua));

        StringRequest stringRequestS = new StringRequest(Request.Method.GET, KEY_TIPE_KAMAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("tipe_kamar");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject json = jsonArray.getJSONObject(a);
                                tipe.add(json.getString("tipe"));
                            }
                            List<String> datakamar = new LinkedList<>(tipe);
                            kamar.attachDataSource(datakamar);
                            for (int z=0; z<datakamar.size(); z++){
                                if (datakamar.get(z).equals(spin)){
                                    pos = z;
                                }
                            }
                            if (spin==null){
                                kamar.setSelectedIndex(0);
                            }else{
                                kamar.setSelectedIndex(pos);
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueueS
                .add(stringRequestS)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void cekTersedia(final String cekin, final String cekout, final String tamu, final String id){
        Log.e("cekin", cekin);
        Log.e("cekout", cekout);
        Log.e("tamu", tamu);
        Log.e("id", id);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getResources().getString(R.string.tunggu));
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, KEY_CEK_KAMAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(MasukActivity.class.getSimpleName(), "Getting Response: "+ id + response);
                pDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("kamar");
                    if (jsonArray.length()>0) {
                        Log.e("hasil : ", String.valueOf(jsonArray.length()));
                        startActivity(new Intent(BookingActivity.this, BookingListingActivity.class));
                        listOke = new ArrayList<>();
                        for (int a=0; a<jsonArray.length();a++){
                            JSONObject object = jsonArray.getJSONObject(a);
                            listOke.add(object.getString("id_tipe"));
                            Log.e("Tipe : ", listOke.get(a));
                        }
                    }else {
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(BookingActivity.this, R.style.CustomDialog);
                        LayoutInflater factory = LayoutInflater.from(BookingActivity.this);
                        @SuppressLint("InflateParams") final View view = factory.inflate(R.layout.layout_tidak_tersedia, null);
                        alertadd.setView(view);
                        alertadd.show();
                    }
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
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("tgl_checkin", cekin);
                params.put("tgl_checkout",cekout);
                params.put("jml_tamu", tamu);
                params.put("tipe", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");

    }

    private void destroyCache() {
        malam.setText("0");
        cek_out=null;
        cek_in=null;
        calendar.resetAllSelectedViews();
    }

}
