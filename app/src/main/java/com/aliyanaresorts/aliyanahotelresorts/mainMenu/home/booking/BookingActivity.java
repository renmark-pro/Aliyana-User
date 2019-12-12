package com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aliyanaresorts.aliyanahotelresorts.MasukActivity;
import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.dialog.NoInetDialog;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.home.booking.roomsMenu.BookingListingActivity;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.android.material.snackbar.Snackbar;

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

import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyCekKamar;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyTipeKamar;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.closeKeyboard;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.getIntentData;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.isNetworkNotAvailable;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setTemaAplikasi;

public class BookingActivity extends AppCompatActivity {

    private TextView tanggal, malam, txt;
    private DateRangeCalendarView calendar;
    private SimpleDateFormat format, upload;
    private EditText jmlOrang;
    private Button cek;

    private String cek_in=null, cek_out=null, uCekIn, uCekOut, id_kamar;
    private String spin=null;
    private int pos;

    private LoadingDialog loadingDialog;
    private ArrayList<String> tipe;
    private NiceSpinner kamar;

    private Activity activity;
    private SPData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        activity = this;
        data = SPData.getInstance(activity);
        setTemaAplikasi(this, 1);
        loadingDialog = new LoadingDialog(this);
        getTypes();

        kamar = findViewById(R.id.kamar);
        cek = findViewById(R.id.btnCek);
        tanggal=findViewById(R.id.tanggal);
        malam=findViewById(R.id.malam);
        txt=findViewById(R.id.txt);
        calendar = findViewById(R.id.calendar);
        jmlOrang = findViewById(R.id.jml_org);
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
        upload = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        final NoInetDialog noInetDialog = new NoInetDialog(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        calendar.setFonts(typeface);

        spin=getIntentData(this,"posisi");

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard(activity);
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
                cek_in = format.format(startDate.getTime());
                uCekIn = upload.format(startDate.getTime());
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
                closeKeyboard(activity);
                if(data.isLoggedIn()){
                    String tMasuk = tanggal.getText().toString();
                    String jOrang = jmlOrang.getText().toString();

                    if (kamar.getSelectedIndex()==0){
                        kamar.requestFocus();
                        kamar.setError(getResources().getString(R.string.kolom));
                        closeKeyboard(activity);
                        Snackbar.make(v, getResources().getString(R.string.isi), Snackbar.LENGTH_SHORT).show();
                    }else if (tMasuk.equals(getResources().getString(R.string.piltgl))){
                        calendar.requestFocus();
                        closeKeyboard(activity);
                        Snackbar.make(v, getResources().getString(R.string.isi), Snackbar.LENGTH_SHORT).show();
                    }else if (jOrang.isEmpty() || Integer.parseInt(jOrang)<1){
                        jmlOrang.requestFocus();
                        jmlOrang.setError(getResources().getString(R.string.kolom));
                    }else if(isNetworkNotAvailable(getBaseContext())){
                        noInetDialog.bukaDialog();
                    } else  {
                        if (tipe.get(kamar.getSelectedIndex()).equals(getResources().getString(R.string.semua))){
                            id_kamar="0";
                        }else {
                            id_kamar = tipe.get(kamar.getSelectedIndex());
                        }
                        cekTersedia(uCekIn, uCekOut,jOrang, id_kamar);
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.logindulu);
                    builder.setPositiveButton(R.string.logy, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(activity, MasukActivity.class));
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
        loadingDialog.bukaDialog();
        RequestQueue requestQueueS = Volley.newRequestQueue(activity);

        tipe = new ArrayList<>();
        tipe.add(getResources().getString(R.string.pilih));
        tipe.add(getResources().getString(R.string.semua));

        StringRequest stringRequestS = new StringRequest(Request.Method.GET, keyTipeKamar,
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
                            loadingDialog.tutupDialog();
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

    private void cekTersedia(final String cekin, final String cekout, final String tamu, final String id){
        loadingDialog = new LoadingDialog(this);
        loadingDialog.bukaDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, keyCekKamar, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.tutupDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("kamar");
                    if (jsonArray.length()>0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("ci",cekin);
                        bundle.putString("co", cekout);
                        bundle.putString("or", tamu);
                        bundle.putString("id", id);
                        Intent i = new Intent(activity, BookingListingActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }else {
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(activity, R.style.CustomDialog);
                        LayoutInflater factory = LayoutInflater.from(activity);
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
                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.tutupDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json; charset=UTF-8");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", data.getKeyToken() );
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tgl_checkin", cekin);
                params.put("tgl_checkout", cekout);
                params.put("jml_tamu", tamu);
                params.put("tipe", id);
                Log.e("tgl_checkin", cekin);
                Log.e("tgl_checkout", cekout);
                Log.e("jml_tamu", tamu);
                Log.e("tipe", id);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
        strReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (kamar.getSelectedIndex()!=0 && !tanggal.getText().toString().equals(getResources()
                .getString(R.string.piltgl)) &&
                !jmlOrang.getText().toString().isEmpty()  &&
                Integer.parseInt(jmlOrang.getText().toString())>1) {
            cek.setBackground(getResources().getDrawable(R.drawable.bt_book));
        }else {
            cek.setBackground(getResources().getDrawable(R.drawable.bt_cek));
        }
    }

    private void destroyCache() {
        malam.setText("0");
        cek_out=null;
        cek_in=null;
        calendar.resetAllSelectedViews();
    }

}
