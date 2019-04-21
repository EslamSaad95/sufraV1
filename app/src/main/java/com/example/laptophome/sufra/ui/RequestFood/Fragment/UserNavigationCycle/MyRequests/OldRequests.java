package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.MyRequests;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.UserRequestsRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.userRequests.UserRequests;
import com.example.laptophome.sufra.data.model.userRequests.UserRequestsData;
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


public class OldRequests extends Fragment {
    @BindView(R.id.OldRequestsFr_Rv)
    RecyclerView OldRequestsFrRv;
    List<UserRequestsData> OldRequestList=new ArrayList<>();
    UserRequestsRvAdapter adapter;
    int maxpage;
    ApiService apiService;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_old_requests, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        OldRequestsFrRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless=new OnEndless(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {

                if(current_page<=maxpage)
                {
                    GetOldRequests(current_page);
                }
            }
        };
        OldRequestsFrRv.addOnScrollListener(onEndless);
        adapter=new UserRequestsRvAdapter(getContext(),OldRequestList,true);
        OldRequestsFrRv.setAdapter(adapter);
        GetOldRequests(1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void GetOldRequests(int pagenumber)
    {

        String apitoken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,getContext());
        apiService.GetUserRequests(apitoken,"completed",pagenumber).enqueue(new Callback<UserRequests>() {
            @Override
            public void onResponse(Call<UserRequests> call, Response<UserRequests> response) {

                try {

                    if(response.body().getStatus().equals(1)) {
                        if (response.body().getData().getData().size() == 0) {
                            Toast.makeText(getContext(),getString(R.string.no_old_requests),Toast.LENGTH_LONG).show();
                        } else {
                            OldRequestList.addAll(response.body().getData().getData());
                            maxpage = response.body().getData().getLastPage();
                            adapter.notifyDataSetChanged();
                        }
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
            public void onFailure(Call<UserRequests> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
