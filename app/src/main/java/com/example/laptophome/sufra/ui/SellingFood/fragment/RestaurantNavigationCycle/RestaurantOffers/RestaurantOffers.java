package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantOfferRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.offercycle.offers.Offers;
import com.example.laptophome.sufra.data.model.offercycle.offers.OffersData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.OnEndless;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantOffers extends Fragment {


    @BindView(R.id.RestaurantOffersFr_RV)
    RecyclerView RestaurantOffersFrRV;
    @BindView(R.id.RestaurantOfferFr_AddNewOffersBtn)
    Button RestaurantOfferFrAddNewOffersBtn;
    List<OffersData> OfferList;
    RestaurantOfferRvAdapter adapter;
    ApiService apiService;
    int maxpage;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_offers, container, false);
        unbinder = ButterKnife.bind(this, view);
        OfferList=new ArrayList<>();
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.myoffers),true,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        RestaurantOffersFrRV.setLayoutManager(linearLayoutManager);
        OnEndless onEndless=new OnEndless(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {

                if(current_page<=maxpage)
                {
                    RestaurantOffers(current_page);
                }
            }
        };
        RestaurantOffersFrRV.addOnScrollListener(onEndless);
        adapter=new RestaurantOfferRvAdapter(getContext(),OfferList);
        RestaurantOffersFrRV.setAdapter(adapter);
        RestaurantOffers(1);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.RestaurantOfferFr_AddNewOffersBtn)
    public void onViewClicked() {
        AddNewOffer addNewOffer=new AddNewOffer();
        HelperMethod.replace(addNewOffer,getFragmentManager(),R.id.SellNavigation_RootLayout);
    }
    public void RestaurantOffers(int pagenumber)
    {String ApiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getContext());
        apiService.GetRestaurantOffers(ApiToken,pagenumber).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        maxpage=response.body().getData().getLastPage();
                        OfferList.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                    }

                    else
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ee)
                {}
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
