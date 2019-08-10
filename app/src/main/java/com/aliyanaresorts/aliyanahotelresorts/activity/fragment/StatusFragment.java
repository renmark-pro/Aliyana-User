package com.aliyanaresorts.aliyanahotelresorts.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aliyanaresorts.aliyanahotelresorts.R;
import com.aliyanaresorts.aliyanahotelresorts.activity.status.fragment.InhouseStatusFragment;
import com.aliyanaresorts.aliyanahotelresorts.activity.status.fragment.ProsesStatusFragment;
import com.aliyanaresorts.aliyanahotelresorts.activity.status.fragment.RiwayatStatusFragment;
import com.aliyanaresorts.aliyanahotelresorts.service.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import static com.aliyanaresorts.aliyanahotelresorts.service.Style.setTemaAplikasi;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setTemaAplikasi(getActivity(),1);
        return inflater.inflate(R.layout.fragment_status, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        TabAdapter tabAdapter = new TabAdapter(getFragmentManager());
        tabAdapter.addFragment(new ProsesStatusFragment(), getResources().getString(R.string.prosestok));
        tabAdapter.addFragment(new InhouseStatusFragment(), getResources().getString(R.string.inhouse));
        tabAdapter.addFragment(new RiwayatStatusFragment(), getResources().getString(R.string.riwayat));

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
