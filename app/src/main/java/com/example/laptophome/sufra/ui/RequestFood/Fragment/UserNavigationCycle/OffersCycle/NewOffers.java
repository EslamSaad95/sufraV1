package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.OffersCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.laptophome.sufra.Adapter.NewOffersRvAdapter;
import com.example.laptophome.sufra.R;

import com.example.laptophome.sufra.data.model.offercycle.offers.OffersData;
import com.example.laptophome.sufra.data.model.offercycle.offers.Offers;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.HelperMethod;
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
public class NewOffers extends Fragment {


    @BindView(R.id.RestaurantOffersFr_RV)
    RecyclerView NewOffersFrRV;
    @BindView(R.id.RestaurantOfferFr_AddNewOffersBtn)
    Button RestaurantOfferFrAddNewOffersBtn;
    NewOffersRvAdapter adapter;
    int maxpage;
    List<OffersData>newOffersDataList;
    ApiService apiService;
    ProgressDialog progressDialog;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_offers, container, false);
        unbinder = ButterKnife.bind(this, view);
        RestaurantOfferFrAddNewOffersBtn.setVisibility(View.GONE);
        newOffersDataList=new ArrayList<>();
        HelperMethod.SetToolBar(getString(R.string.nav_newoffers),false,getContext());
        apiService=RetrofitClient.getclient().create(ApiService.class);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        NewOffersFrRV.setLayoutManager(linearLayoutManager);
        OnEndless onEndless=new OnEndless(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {
                if(current_page<=maxpage)
                {
                    GetNewOffers(current_page);
                }
            }
        };
        NewOffersFrRV.addOnScrollListener(onEndless);
        adapter=new NewOffersRvAdapter(getContext(),newOffersDataList);
        NewOffersFrRV.setAdapter(adapter);
        GetNewOffers(1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void GetNewOffers(int pagenumber)
    {
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        apiService.GetAllOffers(maxpage).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        maxpage=response.body().getData().getLastPage();
                        newOffersDataList.addAll(response.body().getData().getData());
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
                progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}

