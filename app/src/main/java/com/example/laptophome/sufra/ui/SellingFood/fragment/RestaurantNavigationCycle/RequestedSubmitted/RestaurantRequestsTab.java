package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RequestedSubmitted;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.laptophome.sufra.Adapter.TabAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantRequestsTab extends Fragment {


    @BindView(R.id.Item_RestaurantListRv_RestaurantIv)
    ImageView ItemRestaurantListRvRestaurantIv;
    @BindView(R.id.Item_RestaurantListRv_Root)
    LinearLayout ItemRestaurantListRvRoot;
    @BindView(R.id.RestaurantDetailsFr_TabLayout)
    TabLayout RestaurantDetailsFrTabLayout;
    @BindView(R.id.RestaurantDetailsFr_ViewPager)
    ViewPager RestaurantDetailsFrViewPager;
    TabAdapter tabAdapter;
    Unbinder unbinder;


    public RestaurantRequestsTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        unbinder = ButterKnife.bind(this, view);
         ItemRestaurantListRvRoot.setVisibility(View.GONE);
        HelperMethod.SetToolBar(getString(R.string.nav_requests_submitted),true,getContext());
        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addfragment(new NewRestaurantRequests(), getString(R.string.new_requests));
        tabAdapter.addfragment(new CurrentRestaurantRequests(), getString(R.string.current_requests));
        tabAdapter.addfragment(new CompleteRestaurantRequests(), getString(R.string.old_requests));
        RestaurantDetailsFrTabLayout.setupWithViewPager(RestaurantDetailsFrViewPager);
        RestaurantDetailsFrViewPager.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
