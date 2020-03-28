package com.lpjk.pengajuan.permohonan.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lpjk.pengajuan.R;
import com.lpjk.pengajuan.permohonan.sbu.SbuFragment;
import com.lpjk.pengajuan.permohonan.skt.SktFragment;

public class TabPermohonanAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public TabPermohonanAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SbuFragment();
        } else {
            return new SktFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.sbu);
            case 1:
                return mContext.getString(R.string.skt);
            default:
                return null;
        }
    }
}
