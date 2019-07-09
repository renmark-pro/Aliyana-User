package com.aliyanaresorts.aliyanahotelresorts.activity.status.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.aliyanaresorts.aliyanahotelresorts.R;

import static com.aliyanaresorts.aliyanahotelresorts.service.Helper.setViewStatus;


public class RiwayatStatusFragment extends Fragment {

    private RelativeLayout kosongLayout;
    private NestedScrollView nestedLayout;

    public RiwayatStatusFragment() {
        //Construct
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_riwayat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        kosongLayout = view.findViewById(R.id.layKosong);
        nestedLayout = view.findViewById(R.id.nestedLayout);
        setViewStatus(0, nestedLayout, kosongLayout);
    }

}
