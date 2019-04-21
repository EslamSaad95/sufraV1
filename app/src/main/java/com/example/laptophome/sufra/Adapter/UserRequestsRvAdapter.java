package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.acceptorder.AcceptOrder;
import com.example.laptophome.sufra.data.model.userRequests.UserRequestsData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRequestsRvAdapter extends RecyclerView.Adapter<UserRequestsRvAdapter.ViewHolder> {


    private Context context;
    private List<UserRequestsData> UserRequestList;
    private ApiService apiService;
    private boolean oldUserOrders;

    public UserRequestsRvAdapter(Context context, List<UserRequestsData> userRequestList, boolean olduser) {
        this.context = context;
        UserRequestList = userRequestList;
        this.oldUserOrders = olduser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_userrequests_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        SetData(viewHolder, i);
        SetAction(viewHolder, i);
    }

    private void SetData(ViewHolder viewHolder, int i) {

        if(oldUserOrders ==false)
        {
            viewHolder.ItemUserRequestsFrBtnLayout.setVisibility(View.INVISIBLE);
        }
        if(oldUserOrders ==true)
        {
            viewHolder.ItemUserRequestsFrBtnLayout.setVisibility(View.VISIBLE);
        }
        viewHolder.ItemUserRequestsFrRestaurantNameTv.setText(UserRequestList.get(i).getRestaurant().getName());
        viewHolder.ItemUserRequestsFrDeliveryTv.setText(context.getString(R.string.delivery_txt) + " "+UserRequestList.get(i).getDeliveryCost());
        viewHolder.ItemUserRequestsFrPriceTv.setText(context.getString(R.string.price) + "  "+ UserRequestList.get(i).getCost());
        viewHolder.ItemUserRequestsFrTotalTv.setText(context.getString(R.string.total_txt) + "  "+UserRequestList.get(i).getTotal());
        viewHolder.ItemUserRequestsFrRequestNumbTv.setText(context.getString(R.string.request_numb_txt) +" "+ UserRequestList.get(i).getId());
        for(int j=0;j<UserRequestList.get(i).getItems().size();j++) {
            Glide.with(context).load(UserRequestList.get(i).getItems().get(j).getPhotoUrl()).into(viewHolder.ItemUserRequestsFrRequestImageIv);
        }
    }

    private void SetAction(ViewHolder viewHolder, final int i) {
        final String Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,context);
        viewHolder.ItemUserRequestsFrAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.ConfirmUserOrderDelivery(UserRequestList.get(i).getId(),Api_Token).enqueue(new Callback<AcceptOrder>() {
                    @Override
                    public void onResponse(Call<AcceptOrder> call, Response<AcceptOrder> response) {
                        try {

                            if(response.body().getStatus().equals(1))
                            {
                                Toast.makeText(context,"The order is delivered",Toast.LENGTH_LONG).show();
                                notifyItemRemoved(i);
                            }
                            else
                            {
                                Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception ee)
                        {}
                    }

                    @Override
                    public void onFailure(Call<AcceptOrder> call, Throwable t) {
                            Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        viewHolder.ItemUserRequestsFrRefuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"The service is postponed",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_UserRequestsFr_RequestImageIv)
        ImageView ItemUserRequestsFrRequestImageIv;
        @BindView(R.id.Item_UserRequestsFr_RestaurantNameTv)
        TextView ItemUserRequestsFrRestaurantNameTv;
        @BindView(R.id.Item_UserRequestsFr_PriceTv)
        TextView ItemUserRequestsFrPriceTv;
        @BindView(R.id.Item_UserRequestsFr_DeliveryTv)
        TextView ItemUserRequestsFrDeliveryTv;
        @BindView(R.id.Item_UserRequestsFr_TotalTv)
        TextView ItemUserRequestsFrTotalTv;
        @BindView(R.id.Item_UserRequestsFr_RequestNumbTv)
        TextView ItemUserRequestsFrRequestNumbTv;

        @BindView(R.id.Item_UserRequestsFr_AcceptBtn)
        Button ItemUserRequestsFrAcceptBtn;
        @BindView(R.id.Item_UserRequestsFr_RefuseBtn)
        Button ItemUserRequestsFrRefuseBtn;
        @BindView(R.id.Item_UserRequestsFr_BtnLayout)
        LinearLayout ItemUserRequestsFrBtnLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }


    }

}
