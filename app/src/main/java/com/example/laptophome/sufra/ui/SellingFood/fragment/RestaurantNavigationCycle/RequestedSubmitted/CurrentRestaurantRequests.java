package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RequestedSubmitted;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantRequestsRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantrequests.RestaurantRequests;
import com.example.laptophome.sufra.data.model.restaurantrequests.RestaurantRequestsData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.OnEndless;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentRestaurantRequests extends Fragment {
    @BindView(R.id.RestaurantRequestsFr_Rv)
    RecyclerView RestaurantRequestsFrRv;
    Unbinder unbinder;
    private ApiService apiService;
    private List<RestaurantRequestsData> CurrentOrderList;
    private String Api_Token;
    private int maxpage;
    private RestaurantRequestsRvAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__requests, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        CurrentOrderList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RestaurantRequestsFrRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless=new OnEndless(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {

                if(current_page<=maxpage)
                {
                    GetCurrentOrders(current_page);
                }
            }
        };
        RestaurantRequestsFrRv.addOnScrollListener(onEndless);
        adapter=new RestaurantRequestsRvAdapter(getContext(),CurrentOrderList,"current");
        RestaurantRequestsFrRv.setAdapter(adapter);
        GetCurrentOrders(1);
        return view;
    }

    public void GetCurrentOrders(int pagenumber) {
        Api_Token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, getContext());

        apiService.GetNewRestaurantRequest(Api_Token, "current", pagenumber).enqueue(new Callback<RestaurantRequests>() {
            @Override
            public void onResponse(Call<RestaurantRequests> call, Response<RestaurantRequests> response) {
                try {

                    if (response.body().getStatus().equals(1)) {
                        CurrentOrderList.addAll(response.body().getData().getData());
                        maxpage = response.body().getData().getLastPage();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<RestaurantRequests> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
