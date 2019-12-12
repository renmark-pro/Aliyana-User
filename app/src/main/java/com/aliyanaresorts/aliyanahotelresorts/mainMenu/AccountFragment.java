package com.aliyanaresorts.aliyanahotelresorts.mainMenu;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.dialog.LoadingDialog;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.account.HelpAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.account.ProfilDetailActivity;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.account.PromoAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.mainMenu.account.VoucherAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.network.AppController;
import com.aliyanaresorts.aliyanahotelresorts.tools.SPData;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.SplashActivity.MY_PERMISSIONS_REQUEST_GET_ACCESS;
import static com.aliyanaresorts.aliyanahotelresorts.network.API.keyGetUser;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setFotoUser;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Helper.setTextData;
import static com.aliyanaresorts.aliyanahotelresorts.tools.Style.setTemaAplikasi;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private LoadingDialog loadingDialog;
    private TextView nama, telpon;
    private ImageView fotoUser;
    private Activity activity;
    private SPData data;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setTemaAplikasi(getActivity(),1);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        data = SPData.getInstance(activity);
        loadingDialog = new LoadingDialog(activity);
        getPermissions();
        nama = view.findViewById(R.id.nama);
        telpon = view.findViewById(R.id.telpon);
        fotoUser = view.findViewById(R.id.imgView);
        LinearLayout promo = view.findViewById(R.id.promo);
        LinearLayout bantuan = view.findViewById(R.id.bantuan);
        LinearLayout voucher = view.findViewById(R.id.voucher);
        LinearLayout keluar = view.findViewById(R.id.keluar);
        CardView user = view.findViewById(R.id.layoutUser);

        String tlpn = "+"+data.getKeyKdTelepon()+data.getKeyTelepon();
        nama.setText(data.getKeyNama());
        telpon.setText(tlpn);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfilDetailActivity.class);
                startActivity(intent);
            }
        });

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PromoAccountActivity.class);
                startActivity(intent);
            }
        });

        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HelpAccountActivity.class);
                startActivity(intent);
            }
        });

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VoucherAccountActivity.class);
                startActivity(intent);
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.logout();
                Objects.requireNonNull(activity).finish();
            }
        });
    }

    private void getPermissions() {
        /* Check and Request permission */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_GET_ACCESS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_GET_ACCESS) {
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Ijin Di Tolak!", Toast.LENGTH_SHORT).show();

                    }
        }
    }

    private void getDetail() {
        loadingDialog.bukaDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET, keyGetUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (fotoUser.getDrawable()!=getResources().getDrawable(R.drawable.image_slider_1)){
                    loadingDialog.tutupDialog();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("user");
                    Log.e("res ", response);
                    String tlpn = "+"+result.getString("kd_negara")+result.getString("no_telepon");
                    nama.setText(setTextData(result.getString("nama")));
                    telpon.setText(setTextData(tlpn));
                    setFotoUser(result.getString("foto"), activity, fotoUser);
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
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }

    @Override
    public void onStart() {
        super.onStart();
        getDetail();
    }

}
