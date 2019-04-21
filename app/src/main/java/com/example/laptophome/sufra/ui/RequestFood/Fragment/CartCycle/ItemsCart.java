package com.example.laptophome.sufra.ui.RequestFood.Fragment.CartCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.laptophome.sufra.Adapter.CartRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.RoomDB.MenuItemEntity;
import com.example.laptophome.sufra.data.local.RoomDB.RoomDao;
import com.example.laptophome.sufra.data.local.RoomDB.RoomManager;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ItemsCart extends Fragment {


    @BindView(R.id.ItemsCartFr_Rv)
    RecyclerView ItemsCartFrRv;
    @BindView(R.id.ItemsCartFr_TotalPriceTv)
    TextView ItemsCartFrTotalPriceTv;
    @BindView(R.id.ItemsCartFr_ConfirmRequestBtn)
    Button ItemsCartFrConfirmRequestBtn;
    @BindView(R.id.ItemsCartFr_AddMoreBtn)
    Button ItemsCartFrAddMoreBtn;
    CartRvAdapter adapter;
    RoomDao roomDao;
    Bundle b;
    List<MenuItemEntity> itemEntityList = new ArrayList<>();
    Unbinder unbinder;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_items_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        roomDao = RoomManager.getinstance(getContext()).roomDao();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        UserNavigationActivity userNavigationActivity = (UserNavigationActivity) getActivity();
        HelperMethod.SetToolBar(getString(R.string.cart),true,getContext());






        ItemsCartFrRv.setLayoutManager(linearLayoutManager);

        Executors.newSingleThreadExecutor().execute(new Runnable() {

            @Override
            public void run() {
                itemEntityList = roomDao.GetAll();
                adapter = new CartRvAdapter(getContext(), itemEntityList, ItemsCartFrTotalPriceTv);
                ItemsCartFrRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });


        return view;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @OnClick({R.id.ItemsCartFr_ConfirmRequestBtn, R.id.ItemsCartFr_AddMoreBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ItemsCartFr_ConfirmRequestBtn:
                b = new Bundle();
                b.putString("TotalPrice", ItemsCartFrTotalPriceTv.getText().toString());
                ConfirmPayment confirmPayment = new ConfirmPayment();
                confirmPayment.setArguments(b);
                HelperMethod.replace(confirmPayment, getFragmentManager(), R.id.UserNavigation_RootLayout);
                break;
            case R.id.ItemsCartFr_AddMoreBtn:
                RestaurantDetails details = new RestaurantDetails();
                HelperMethod.replace(details, getFragmentManager(), R.id.UserNavigation_RootLayout);
                break;
        }
    }







}
