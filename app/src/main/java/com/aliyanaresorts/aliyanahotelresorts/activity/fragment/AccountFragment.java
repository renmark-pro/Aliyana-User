package com.aliyanaresorts.aliyanahotelresorts.activity.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyanaresorts.aliyanahotelresorts.activity.account.HelpAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.account.ProfilAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.account.PromoAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.account.VoucherAccountActivity;
import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.service.SPData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;
import static com.aliyanaresorts.aliyanahotelresorts.SplashActivity.MY_PERMISSIONS_REQUEST_GET_ACCESS;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

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
        TextView nama = view.findViewById(R.id.nama);
        TextView alamat = view.findViewById(R.id.alamat);
        TextView telpon = view.findViewById(R.id.telpon);
        LinearLayout promo = view.findViewById(R.id.promo);
        LinearLayout bantuan = view.findViewById(R.id.bantuan);
        LinearLayout voucher = view.findViewById(R.id.voucher);
        LinearLayout keluar = view.findViewById(R.id.keluar);
        CircleImageView foto = view.findViewById(R.id.imgView);

        getPermissions();

        nama.setText(SPData.getInstance(getActivity()).getKeyNama());
        alamat.setText(SPData.getInstance(getActivity()).getKeyAlamat());
        telpon.setText(SPData.getInstance(getActivity()).getKeyTelepon());

        if (!SPData.getInstance(getActivity()).getKeyFoto().equals("http://aliyanaresorts.com/app/user/foto/")){
            Glide.with(Objects.requireNonNull(getContext())).load(SPData.getInstance(getActivity()).getKeyFoto())
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.ic_fragment_account)
                    .transition(withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(foto);
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfilAccountActivity.class);
                startActivity(intent);
            }
        });

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PromoAccountActivity.class);
                startActivity(intent);
            }
        });

        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpAccountActivity.class);
                startActivity(intent);
            }
        });

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VoucherAccountActivity.class);
                startActivity(intent);
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPData.getInstance(getActivity()).logout();
                Objects.requireNonNull(getActivity()).finish();
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
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Kosong
            } else {
                Toast.makeText(getActivity(), "Ijin Di Tolak!", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
