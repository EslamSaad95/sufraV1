package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RequestedSubmitted;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantRequestsDetailsRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.model.restaurantrequests.Item;
import com.example.laptophome.sufra.helper.HelperMethod;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RequestDetails extends Fragment {


    @BindView(R.id.Res_RequestsDetailsFr_Rv)
    RecyclerView ResRequestsDetailsFrRv;
    private RestaurantRequestsDetailsRvAdapter adapter;
    private List<Item>RestaurantRequestsDetailsList;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        HelperMethod.SetToolBar(getString(R.string.requestdetails),true,getContext());
        RestaurantRequestsDetailsList=new ArrayList<>();
        RestaurantRequestsDetailsList=(ArrayList<Item>)getArguments().getSerializable("RequestsDetailsList");
        adapter=new RestaurantRequestsDetailsRvAdapter(getContext(),RestaurantRequestsDetailsList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        ResRequestsDetailsFrRv.setLayoutManager(linearLayoutManager);
        ResRequestsDetailsFrRv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
