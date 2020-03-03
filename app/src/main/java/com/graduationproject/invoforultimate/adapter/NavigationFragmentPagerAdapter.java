package com.graduationproject.invoforultimate.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.graduationproject.invoforultimate.ui.fragment.NavigationSearchFragment;

import java.util.ArrayList;

/**
 * Created by INvo
 * on 2020-03-02.
 */
public class NavigationFragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private ArrayList<NavigationSearchFragment> arrayList;

    public NavigationFragmentPagerAdapter(@NonNull FragmentManager fm, ArrayList<NavigationSearchFragment> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
