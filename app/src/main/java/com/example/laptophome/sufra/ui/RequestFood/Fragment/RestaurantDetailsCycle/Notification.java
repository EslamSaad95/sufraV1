package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.NotificationListRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.notificationlist.NotificationList;
import com.example.laptophome.sufra.data.model.notificationlist.NotificationListData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Notification extends Fragment {


    @BindView(R.id.NotificationFr_Rv)
    RecyclerView NotificationFrRv;
    Unbinder unbinder;
    ApiService apiService;
    NotificationListRvAdapter adapter;
    List<NotificationListData>NotificationList;
    String Api_Token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        NotificationList=new ArrayList<>();
        HelperMethod.SetToolBar("Notification",true,getContext());
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Restaurant_User)) {
            Api_Token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, getContext());
                GetRestaurantNotification();
        }
        else
        {

            Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,getContext());
            GetClientNotification();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void GetRestaurantNotification()
    {
        apiService.GetRestaurantNotification(Api_Token).enqueue(new Callback<com.example.laptophome.sufra.data.model.notificationlist.NotificationList>() {
            @Override
            public void onResponse(Call<com.example.laptophome.sufra.data.model.notificationlist.NotificationList> call, Response<com.example.laptophome.sufra.data.model.notificationlist.NotificationList> response) {

                try {

                    if(response.body().getStatus().equals(1))
                    {
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                        NotificationFrRv.setLayoutManager(linearLayoutManager);
                        NotificationList.addAll(response.body().getData().getData());
                        adapter=new NotificationListRvAdapter(getContext(),NotificationList);
                        NotificationFrRv.setAdapter(adapter);
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
            public void onFailure(Call<com.example.laptophome.sufra.data.model.notificationlist.NotificationList> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void GetClientNotification()
    {
        apiService.GetClientNotification(Api_Token).enqueue(new Callback<com.example.laptophome.sufra.data.model.notificationlist.NotificationList>() {
            @Override
            public void onResponse(Call<com.example.laptophome.sufra.data.model.notificationlist.NotificationList> call, Response<com.example.laptophome.sufra.data.model.notificationlist.NotificationList> response) {
                try {

                    if(response.body().getStatus().equals(0))
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ee)
                {}
            }

            @Override
            public void onFailure(Call<com.example.laptophome.sufra.data.model.notificationlist.NotificationList> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
