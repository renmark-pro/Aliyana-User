package com.aliyanaresorts.aliyanahotelresorts.activity.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyanaresorts.aliyanahotelresorts.activity.AboutActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.BookingActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.FasilitasActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.InfoHotelActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.PromoListActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.RoomDetailActivity;
import com.aliyanaresorts.aliyanahotelresorts.activity.RoomListActivity;
import com.aliyanaresorts.aliyanahotelresorts.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_DOMAIN;
import static com.aliyanaresorts.aliyanahotelresorts.service.database.API.KEY_SLIDE_HOME;
import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FlipperLayout flipper;

    private ArrayList<HashMap<String, String>> list_dataS;

    private TextView selamat;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setTemaAplikasi(getActivity(),0);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ImageView rooms = view.findViewById(R.id.bt_rooms);
        ImageView book = view.findViewById(R.id.bt_book);
        ImageView about = view.findViewById(R.id.bt_about);
        ImageView promo = view.findViewById(R.id.bt_promo);
        ImageView galeri = view.findViewById(R.id.bt_galleries);
        ImageView fasilitas = view.findViewById(R.id.bt_facilities);
        flipper = view.findViewById(R.id.flipper);
        selamat = view.findViewById(R.id.textWelcome);
        flipper.setCircularIndicatorLayoutParams(0, 0);
//        TextView nama = view.findViewById(R.id.textNama);

        Typeface face = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(),
                "JosefinSans-Regular.ttf");
        selamat.setTypeface(face);

        final Animation animation1 =  AnimationUtils.loadAnimation(getActivity(),R.anim.coba);

        setSlide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selamat.setAnimation(animation1);
                selamat.setVisibility(View.VISIBLE);
            }
        }, 1500);

        rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoomListActivity.class);
                startActivity(intent);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PromoListActivity.class);
                startActivity(intent);
            }
        });

        galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoHotelActivity.class);
                startActivity(intent);
            }
        });

        fasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FasilitasActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setSlide() {
        RequestQueue requestQueueS = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        list_dataS = new ArrayList<>();

        StringRequest stringRequestS = new StringRequest(Request.Method.GET, KEY_SLIDE_HOME,
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
                                map.put("judul", json.getString("judul"));
                                map.put("foto", json.getString("foto"));
                                list_dataS.add(map);
                                @NonNull
                                FlipperView view = new FlipperView(Objects.requireNonNull(getActivity()).getBaseContext());
                                view.setImageUrl(KEY_DOMAIN+list_dataS.get(a).get("foto"))
                                        .setDescription(list_dataS.get(a).get("judul"))
                                        .setDescriptionBackgroundColor(getResources()
                                                .getColor(R.color.hitamtrans));
                                if (!Objects.requireNonNull(list_dataS.get(a).get("foto")).isEmpty()) {
                                    flipper.addFlipperView(view);
                                    final int finalA = a;
                                    view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                                        @Override
                                        public void onFlipperClick(FlipperView flipperView) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("posisi", list_dataS.get(finalA).get("id"));
                                            Intent intent = new Intent(getActivity(), RoomDetailActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Objects.requireNonNull(getActivity()).recreate();
            }
        });
        requestQueueS
                .add(stringRequestS)
                .setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
