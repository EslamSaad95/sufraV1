package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.MyRequests;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptophome.sufra.Adapter.TabAdapter;
import com.example.laptophome.sufra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyRequests extends Fragment {


    @BindView(R.id.UserRequestsFr_TabLayout)
    TabLayout UserRequestsFrTabLayout;
    @BindView(R.id.UserRequestsFr_ViewPager)
    ViewPager UserRequestsFrViewPager;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_requests, container, false);
        unbinder = ButterKnife.bind(this, view);
        TabAdapter tabAdapter=new TabAdapter(getChildFragmentManager());
        tabAdapter.addfragment(new CurrentRequests(),getString(R.string.old_requests));
        tabAdapter.addfragment(new OldRequests(),getString(R.string.current_requests));
        UserRequestsFrTabLayout.setupWithViewPager(UserRequestsFrViewPager);
        UserRequestsFrViewPager.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
