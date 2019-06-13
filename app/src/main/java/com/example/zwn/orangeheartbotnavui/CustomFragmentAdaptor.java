package com.example.zwn.orangeheartbotnavui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomFragmentAdaptor extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
//    private final List<String> fragmentTitles = new ArrayList<>();

    public CustomFragmentAdaptor(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(Fragment fragment) {
        fragmentList.add(fragment);
//        fragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return fragmentTitles.get(position);
//    }
}
