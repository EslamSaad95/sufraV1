package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantMenuRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.RoomDB.MenuItemEntity;
import com.example.laptophome.sufra.data.local.RoomDB.RoomDao;
import com.example.laptophome.sufra.data.local.RoomDB.RoomManager;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenuData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.OnBackPressed;
import com.example.laptophome.sufra.helper.OnEndless;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantMenu extends Fragment implements OnBackPressed {


    @BindView(R.id.RestaurantMenuFr_Rv)
    RecyclerView RestaurantMenuFrRv;
    ApiService apiService;
    RestaurantMenuRvAdapter adapter;
    List<RestaurantMenuData> MenuList = new ArrayList<>();
    int maxpage;
    Bundle bundle;
    int RestaurantId;
    Unbinder unbinder;
    RoomDao roomDao;
    AlertDialog.Builder alertDialogbuilder;
    AlertDialog alertDialog;
    List<MenuItemEntity>itemEntities=new ArrayList<>();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ((UserNavigationActivity) getActivity()).setOnBackPressedListener(this);
        }
        catch (ClassCastException ee)
        {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        roomDao = RoomManager.getinstance(getContext()).roomDao();

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                itemEntities=roomDao.GetAll();
            }
        });


       // bundle = getArguments();
        RestaurantId = Integer.parseInt(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File, Constant.Rest_Id, getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RestaurantMenuFrRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxpage) {
                    GetRestauratMenu(current_page);
                }
            }
        };
        RestaurantMenuFrRv.addOnScrollListener(onEndless);
        adapter = new RestaurantMenuRvAdapter(getContext(), MenuList);
        RestaurantMenuFrRv.setAdapter(adapter);
        GetRestauratMenu(1);


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null) {
            adapter.saveStates(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (adapter != null) {
            adapter.restoreStates(savedInstanceState);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MenuList.clear();
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Client_User)) {
            ((UserNavigationActivity) getActivity()).setOnBackPressedListener(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void GetRestauratMenu(int pagenumber) {
        apiService.RestaurantMenu(RestaurantId, pagenumber).enqueue(new Callback<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenu>() {
            @Override
            public void onResponse(Call<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenu> call, Response<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenu> response) {
                try {
                    if (response.body().getStatus().equals(1)) {

                        maxpage = response.body().getData().getLastPage();
                        MenuList.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenu> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnBackPressed() {
        if (itemEntities.size() == 0) {
            ((UserNavigationActivity)getActivity()).onBackPressedobject=null;
        } else {
            alertDialogbuilder = new AlertDialog.Builder(getContext());
            View view = getLayoutInflater().inflate(R.layout.alertdialouge_checkdb, null);
            Button CkDbAlterdialougeYesBtn = view.findViewById(R.id.CkDb_alterdialouge_yesBtn);
            Button CkDbAlterdialougeNoBtn = view.findViewById(R.id.CkDb_alterdialouge_NoBtn);
            CkDbAlterdialougeYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDao.DeleteAll();
                            alertDialog.dismiss();
                            UserNavigationActivity activityinstance = (UserNavigationActivity) getContext();
                              RestaurantList list = new RestaurantList();
                            ((UserNavigationActivity)getActivity()).onBackPressedobject=null;
                              HelperMethod.replace(list, activityinstance.getSupportFragmentManager(), R.id.UserNavigation_RootLayout);
                        }
                    });

                }

            });
            CkDbAlterdialougeNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialogbuilder.setView(view);
            alertDialog = alertDialogbuilder.create();
            alertDialog.show();
        }
    }




}
