package com.lpjk.pengajuan.permohonan;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.adapter.TabPermohonanAdapter;
import com.rohim.rohimmodule.CustomViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermohonanFragment extends Fragment {

    TabLayout tabPermohonan;
    CustomViewPager vpPermohonan;
    View v;
    Toolbar toolbar;
    TabPermohonanAdapter tabPermohonanAdapter;
    public PermohonanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_permohonan, container, false);
        initUi();
        return v;
    }

    private void initUi(){
        toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("List Permohonan");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        tabPermohonan = v.findViewById(R.id.tab_permohonan);
        vpPermohonan = v.findViewById(R.id.vp_permohonan);
        tabPermohonanAdapter = new TabPermohonanAdapter(getContext(), getChildFragmentManager());
        vpPermohonan.setAdapter(tabPermohonanAdapter);
        tabPermohonan.setupWithViewPager(vpPermohonan);
        vpPermohonan.setEnableSwipe(false);
    }

}
