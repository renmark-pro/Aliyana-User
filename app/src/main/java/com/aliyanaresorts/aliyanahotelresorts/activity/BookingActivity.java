package com.aliyanaresorts.aliyanahotelresorts.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;

public class BookingActivity extends AppCompatActivity{

    private TextView tanggal, malam, txt;
    private DateRangeCalendarView calendar;
    private SimpleDateFormat format;
    private EditText penggunaKamar, jmlOrang;

    private CardView layDetail;

    private EditText namaPengguna, namaPemesan, tipeKamar, tanggalMasuk, jumlahOrang, jumlahMalam;
    private Button cek, kirim;

    String cek_in=null, cek_out=null;
    String spin=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTemaAplikasi(this, 1);

        final NiceSpinner kamar = findViewById(R.id.kamar);
        List<String> datakamar = new LinkedList<>(Arrays.asList(getResources().getString(R.string.pilih),
                "Aliyana Suite", "Superior Balcony", "Superior", "Grand Deluxe", "Deluxe"));
        kamar.attachDataSource(datakamar);

        namaPemesan = findViewById(R.id.namaPemesan);
        namaPengguna = findViewById(R.id.namaPengguna);
        tipeKamar = findViewById(R.id.tipeKamar);
        tanggalMasuk = findViewById(R.id.tanggalMasuk);
        jumlahMalam = findViewById(R.id.jumlahMalam);
        jumlahOrang = findViewById(R.id.jumlahOrang);

        namaPemesan.setEnabled(false);
        namaPengguna.setEnabled(false);
        tipeKamar.setEnabled(false);
        tanggalMasuk.setEnabled(false);
        jumlahMalam.setEnabled(false);
        jumlahOrang.setEnabled(false);

        cek = findViewById(R.id.btnCek);
        kirim = findViewById(R.id.btnKirim);
        layDetail = findViewById(R.id.layDetail);

        tanggal=findViewById(R.id.tanggal);
        malam=findViewById(R.id.malam);
        txt=findViewById(R.id.txt);
        calendar = findViewById(R.id.calendar);
        penggunaKamar = findViewById(R.id.nama);
        jmlOrang = findViewById(R.id.jml_org);
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        calendar.setFonts(typeface);

        spin=getIntent().getStringExtra("posisi");

        if (spin!=null){
            kamar.setSelectedIndex(Integer.parseInt(spin));
        }else {
            kamar.setSelectedIndex(0);
        }

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                cek_out = format.format(endDate.getTime());
                long startDateMillis = startDate.getTimeInMillis();
                long endDateMillis = endDate.getTimeInMillis();
                long differenceMillis = endDateMillis - startDateMillis;
                int daysDifference = (int) (differenceMillis / (1000 * 60 * 60 * 24));
                String swap;
                if (startDateMillis>endDateMillis){
                    swap = cek_in;
                    cek_in = cek_out;
                    cek_out = swap;
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
                if(SPData.getInstance(BookingActivity.this).isLoggedIn()){
                    String nPengguna = penggunaKamar.getText().toString();
                    String nPemesan = SPData.getInstance(getBaseContext()).getKeyNama();
                    String tKamar = kamar.getText().toString();
                    String tMasuk = tanggal.getText().toString();
                    String jMalam = malam.getText().toString();
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
                        layDetail.setVisibility(View.VISIBLE);
                        cek.setVisibility(View.GONE);
                        kirim.setVisibility(View.VISIBLE);

                        namaPemesan.setText(nPemesan);
                        namaPengguna.setText(nPengguna);
                        tipeKamar.setText(tKamar);
                        String tgl = cek_in+" - "+cek_out;
                        tanggalMasuk.setText(tgl);
                        jumlahOrang.setText(jOrang);
                        jumlahMalam.setText(jMalam);
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

    private void destroyCache() {
        malam.setText("0");
        cek_out=null;
        cek_in=null;
        calendar.resetAllSelectedViews();
    }

}
