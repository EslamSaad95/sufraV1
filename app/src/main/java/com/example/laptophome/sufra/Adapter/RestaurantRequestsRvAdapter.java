package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.acceptorder.AcceptOrder;
import com.example.laptophome.sufra.data.model.restaurantrequests.RestaurantRequestsData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RequestedSubmitted.RequestDetails;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRequestsRvAdapter extends RecyclerView.Adapter<RestaurantRequestsRvAdapter.Viewholder> {



    private Context context;
    private List<RestaurantRequestsData> RestaurantRequest_List;
    private ApiService apiService;
    private String State;
    private String Api_Token;

    public RestaurantRequestsRvAdapter(Context context, List<RestaurantRequestsData> restaurantRequest_List, String status) {
        this.context = context;
        RestaurantRequest_List = restaurantRequest_List;
        this.State = status;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantrequests_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        SetData(viewholder, i);
        SetAction(viewholder, i);

    }

    private void SetData(Viewholder viewholder, int i) {
        viewholder.ItemRestaurantRequestsRVClientNameTv.setText(RestaurantRequest_List.get(i).getClient().getName());
        viewholder.ItemRestaurantRequestsRvPriceTv.setText(context.getString(R.string.price_txt) + RestaurantRequest_List.get(i).getCost());
        viewholder.ItemRestaurantRequestsRvDeliveryCommisonTv.setText(context.getString(R.string.delivery_commision) + RestaurantRequest_List.get(i).getDeliveryCost());
        viewholder.ItemRestaurantRequestsRvTotalTv.setText(context.getString(R.string.total) + RestaurantRequest_List.get(i).getTotal());
        viewholder.ItemRestaurantRequestsRvRequestNumTv.setText(context.getString(R.string.request_num) + RestaurantRequest_List.get(i).getId());
        viewholder.ItemRestaurantRequestsRvCallBtn.setText(RestaurantRequest_List.get(i).getClient().getPhone());
        viewholder.ItemRestaurantRequestsRvAddressTv.setText(context.getString(R.string.address) + RestaurantRequest_List.get(i).getClient().getAddress());

        if (State.equals("current")) {

            viewholder.ItemRestaurantRequestsRvRefusedBtn.setVisibility(View.GONE);
            viewholder.ItemRestaurantRequestsRVAcceptBtn.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.CurrentRequestBtn_Width);
            viewholder.ItemRestaurantRequestsRVAcceptBtn.setText(context.getString(R.string.delivery_confirmation));
            viewholder.ItemRestaurantRequestsRVAcceptBtn.setPaddingRelative((int) context.getResources().getDimension(R.dimen.dimen_M), 0, (int) context.getResources().getDimension(R.dimen.dimen_M), 0);

            viewholder.ItemRestaurantRequestsRvCallBtn.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.CurrentRequestBtn_Width);
        }
        if (State.equals("completed")) {

            viewholder.ItemRestaurantRequestsRvButtonLayout.setVisibility(View.GONE);
            viewholder.ItemRestaurantRequestsRvCompletedOrderTv.setVisibility(View.VISIBLE);

        }
    }

    private void SetAction(Viewholder viewholder, final int i) {
        Api_Token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, context);

        viewholder.ItemRestaurantRequestsRVAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.AcceptOrder(Api_Token, RestaurantRequest_List.get(i).getId()).enqueue(new Callback<AcceptOrder>() {
                    @Override
                    public void onResponse(Call<AcceptOrder> call, Response<AcceptOrder> response) {
                        try {

                            if (response.body().getStatus().equals(1)) {
                                Toast.makeText(context, context.getString(R.string.accept_order), Toast.LENGTH_LONG).show();
                                notifyItemRemoved(i);
                            } else {
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ee) {
                        }
                    }

                    @Override
                    public void onFailure(Call<AcceptOrder> call, Throwable t) {

                    }
                });
            }
        });

        viewholder.ItemRestaurantRequestsRvRefusedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.RejectOrder(Api_Token, RestaurantRequest_List.get(i).getId()).enqueue(new Callback<AcceptOrder>() {
                    @Override
                    public void onResponse(Call<AcceptOrder> call, Response<AcceptOrder> response) {
                        try {
                            if (response.body().getStatus().equals(1)) {
                                notifyItemRemoved(i);
                            } else {
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ee) {
                        }
                    }

                    @Override
                    public void onFailure(Call<AcceptOrder> call, Throwable t) {

                    }
                });
            }
        });
        if (State.equals("current")) {
            viewholder.ItemRestaurantRequestsRVAcceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiService.ConfirmOrderDelivery(RestaurantRequest_List.get(i).getId(), Api_Token).enqueue(new Callback<AcceptOrder>() {
                        @Override
                        public void onResponse(Call<AcceptOrder> call, Response<AcceptOrder> response) {
                            try {

                                if (response.body().getStatus().equals(1)) {
                                    notifyItemRemoved(i);
                                } else {
                                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ee) {
                            }
                        }

                        @Override
                        public void onFailure(Call<AcceptOrder> call, Throwable t) {

                        }
                    });
                }
            });
        }
        viewholder.RootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestDetails requestDetails=new RequestDetails();
                Bundle b=new Bundle();
                RestaurantNavigationActivity activityinstance = (RestaurantNavigationActivity) context;
                b.putSerializable("RequestsDetailsList", (Serializable) RestaurantRequest_List.get(i).getItems());
                requestDetails.setArguments(b);
                HelperMethod.replace(requestDetails,activityinstance.getSupportFragmentManager(),R.id.SellNavigation_RootLayout);
            }
        });


    }

    @Override
    public int getItemCount() {
        return RestaurantRequest_List.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder

    {
        private View view;
        @BindView(R.id.Item_RestaurantRequestsRv_ButtonLayout)
        RelativeLayout ItemRestaurantRequestsRvButtonLayout;
        @BindView(R.id.Item_RestaurantRequestsRv_CompletedOrder_Tv)
        TextView ItemRestaurantRequestsRvCompletedOrderTv;
        @BindView(R.id.Item_RestaurantRequestsRv_AddressTv)
        TextView ItemRestaurantRequestsRvAddressTv;
        @BindView(R.id.Item_RestaurantRequestsRV_ClientNameTv)
        TextView ItemRestaurantRequestsRVClientNameTv;
        @BindView(R.id.Item_RestaurantRequestsRv_RequestNumTv)
        TextView ItemRestaurantRequestsRvRequestNumTv;
        @BindView(R.id.Item_RestaurantRequestsRv_PriceTv)
        TextView ItemRestaurantRequestsRvPriceTv;
        @BindView(R.id.Item_RestaurantRequestsRv_DeliveryCommisonTv)
        TextView ItemRestaurantRequestsRvDeliveryCommisonTv;
        @BindView(R.id.Item_RestaurantRequestsRv_TotalTv)
        TextView ItemRestaurantRequestsRvTotalTv;
        @BindView(R.id.Item_RestaurantRequestsRv_CallBtn)
        Button ItemRestaurantRequestsRvCallBtn;
        @BindView(R.id.Item_RestaurantRequestsRV_AcceptBtn)
        Button ItemRestaurantRequestsRVAcceptBtn;
        @BindView(R.id.Item_RestaurantRequestsRv_RefusedBtn)
        Button ItemRestaurantRequestsRvRefusedBtn;
        @BindView(R.id.RootLayout)
        LinearLayout RootLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);

        }
    }
}
