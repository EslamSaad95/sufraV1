package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.offercycle.deleteoffer.DeleteOffer;
import com.example.laptophome.sufra.data.model.offercycle.offers.OffersData;

import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers.AddNewOffer;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers.EditOffer;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantOfferRvAdapter extends RecyclerView.Adapter<RestaurantOfferRvAdapter.Viewholder> {


    private Context context;
    private List<OffersData> OffersList;
    private ApiService apiService;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private  String ApiToken;

    public RestaurantOfferRvAdapter(Context context, List<OffersData> offersList) {
        this.context = context;
        OffersList = offersList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantoffer_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        SetData(viewholder, i);
        SetAction(viewholder, i);
    }

    private void SetData(Viewholder viewholder, int i) {
        viewBinderHelper.bind(viewholder.ItemRestaurantMenuRvSwipLayout, String.valueOf(OffersList.get(i).getId()));
        viewholder.ItemRestaurantOfferRvRestaurantNameTv.setText(OffersList.get(i).getRestaurant().getName());
        viewholder.ItemRestaurantOfferRvPriceTv.setText(context.getString(R.string.price_txt)+OffersList.get(i).getPrice());
        viewholder.ItemRestaurantOfferRvOfferNameTv.setText(OffersList.get(i).getName());
        Glide.with(context).load(OffersList.get(i).getPhotoUrl()).into(viewholder.ItemRestaurantOffersRvOfferIv);
        String DateEnd = HelperMethod.ConvertDatetoMonthFormat(OffersList.get(i).getEndingAt());
        viewholder.ItemRestaurantOfferRvOfferEndInTv.setText(context.getString(R.string.endat)+DateEnd);

        viewholder.ItemRestaurantOfferRvSwapOfferNameTv.setText(OffersList.get(i).getName());
        viewholder.ItemRestaurantOfferRvSwapRestNameTv.setText(OffersList.get(i).getRestaurant().getName());
        viewholder.ItemRestaurantOfferRvSwapOfferDateEnd.setText(context.getString(R.string.endat)+HelperMethod.ConvertDatetoMonthFormat(OffersList.get(i).getEndingAt()));
    }


    private void SetAction(Viewholder viewholder, final int i) {
        ApiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,context);
        viewholder.ItemRestaurantOfferRvSwipDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.DeleteOffer((OffersList.get(i).getId()),ApiToken).enqueue(new Callback<DeleteOffer>() {
                    @Override
                    public void onResponse(Call<DeleteOffer> call, Response<DeleteOffer> response) {
                        try {

                            if(response.body().getStatus().equals(1))
                            {
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
                    public void onFailure(Call<DeleteOffer> call, Throwable t) {
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        viewholder.ItemRestaurantOfferRvSwipEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantNavigationActivity activityinstance=(RestaurantNavigationActivity)context;
                Bundle b=new Bundle();
                b.putString("OfferName",OffersList.get(i).getName());
                b.putString("offerprice",OffersList.get(i).getPrice());
                b.putString("OfferStartAt",HelperMethod.ConvertDatetoMonthFormat(OffersList.get(i).getStartingAt()));
                b.putString("OfferEndAt",HelperMethod.ConvertDatetoMonthFormat(OffersList.get(i).getEndingAt()));
                b.putString("OfferId",String.valueOf(OffersList.get(i).getId()));
                b.putString("OfferDescr",OffersList.get(i).getDescription());
                b.putString("PhotoUrl",OffersList.get(i).getPhoto());
               EditOffer editOffer=new EditOffer();
                editOffer.setArguments(b);
               HelperMethod.replace(editOffer,activityinstance.getSupportFragmentManager(),R.id.SellNavigation_RootLayout);
            }
        });

        viewholder.ItemRestaurantOfferRvSwipDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    apiService.DeleteOffer(OffersList.get(i).getId(),ApiToken).enqueue(new Callback<DeleteOffer>() {
                        @Override
                        public void onResponse(Call<DeleteOffer> call, Response<DeleteOffer> response) {
                            try {

                                if(response.body().getStatus().equals(1))
                                {
                                    Toast.makeText(context,context.getString(R.string.offer_is_removed),Toast.LENGTH_LONG).show();
                                    OffersList.remove(i);
                                    notifyItemRangeChanged(i,OffersList.size());
                                    notifyItemRemoved(i);
                                    notifyDataSetChanged();
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
                        public void onFailure(Call<DeleteOffer> call, Throwable t) {
                                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
            }
        });
    }

    @Override
    public int getItemCount() {
        return OffersList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
           @BindView(R.id.Item_RestaurantOfferRv_Swap_OfferNameTv)
           TextView ItemRestaurantOfferRvSwapOfferNameTv;
           @BindView(R.id.Item_RestaurantOfferRv_Swap_RestNameTv)
           TextView ItemRestaurantOfferRvSwapRestNameTv;
           @BindView(R.id.Item_RestaurantOfferRv_Swap_OfferDateEnd)
           TextView ItemRestaurantOfferRvSwapOfferDateEnd;
           @BindView(R.id.Item_RestaurantOfferRv_Swip_EditBtn)
           Button ItemRestaurantOfferRvSwipEditBtn;
           @BindView(R.id.Item_RestaurantOfferRv_Swip_DeleteBtn)
           Button ItemRestaurantOfferRvSwipDeleteBtn;

        @BindView(R.id.Item_RestaurantOfferRv_offerNameTv)
        TextView ItemRestaurantOfferRvOfferNameTv;
        @BindView(R.id.Item_RestaurantOfferRv_RestaurantNameTv)
        TextView ItemRestaurantOfferRvRestaurantNameTv;
        @BindView(R.id.Item_RestaurantOfferRv_OfferEndInTv)
        TextView ItemRestaurantOfferRvOfferEndInTv;
        @BindView(R.id.Item_RestaurantOfferRv_PriceTv)
        TextView ItemRestaurantOfferRvPriceTv;
        @BindView(R.id.Item_RestaurantMenuRv_SwipLayout)
        SwipeRevealLayout ItemRestaurantMenuRvSwipLayout;
        @BindView(R.id.Item_RestaurantOffersRv_OfferIv)
        ImageView ItemRestaurantOffersRvOfferIv;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
