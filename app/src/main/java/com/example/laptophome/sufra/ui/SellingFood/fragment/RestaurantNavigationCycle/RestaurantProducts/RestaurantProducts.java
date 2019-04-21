package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantProducts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantMenuRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenu;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenuData;
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


public class RestaurantProducts extends Fragment {


    @BindView(R.id.RestaurantProducts_Rv)
    RecyclerView RestaurantProductsRv;
    @BindView(R.id.RestaurantProducts_AddMenuItemBtn)
    Button RestaurantProductsAddMenuItemBtn;
    ApiService apiService;
    RestaurantMenuRvAdapter adapter;
    List<RestaurantMenuData> RestaurantMenuList;
    int maxPage;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_products, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.myproducts),true,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        RestaurantMenuList=new ArrayList<>();
        RestaurantProductsRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless=new OnEndless(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {
                    if(current_page<=maxPage)
                    {
                        GetRestaurantItem(current_page);
                    }
            }
        };
        RestaurantProductsRv.addOnScrollListener(onEndless);
        adapter=new RestaurantMenuRvAdapter(getContext(),RestaurantMenuList);
        RestaurantProductsRv.setAdapter(adapter);
        GetRestaurantItem(1);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.RestaurantProducts_AddMenuItemBtn)
    public void onViewClicked() {
        AddNewItem addNewItem=new AddNewItem();
        HelperMethod.replace(addNewItem,getFragmentManager(),R.id.SellNavigation_RootLayout);
    }
    public void GetRestaurantItem(int pagenumber)
    {   String apiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getContext());
      apiService.RestaurantProducts(apiToken,pagenumber).enqueue(new Callback<RestaurantMenu>() {
          @Override
          public void onResponse(Call<RestaurantMenu> call, Response<RestaurantMenu> response) {
              try {
              if(response.body().getStatus().equals(1))
              {
                  RestaurantMenuList.addAll(response.body().getData().getData());
                  maxPage=response.body().getData().getLastPage();
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
          public void onFailure(Call<RestaurantMenu> call, Throwable t) {

          }
      });
    }
}
