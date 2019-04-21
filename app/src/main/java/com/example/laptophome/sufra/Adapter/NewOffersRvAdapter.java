package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.model.offercycle.offers.OffersData;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.OffersCycle.OfferDetails;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewOffersRvAdapter extends RecyclerView.Adapter<NewOffersRvAdapter.ViewHolder> {

    private Context context;
    private List<OffersData> newOffersDataList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public NewOffersRvAdapter(Context context, List<OffersData> newOffersDataList) {
        this.context = context;
        this.newOffersDataList = newOffersDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantoffer_recycleview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        SetData(i, viewHolder);
        SetAction(i, viewHolder);
    }

    private void SetData(int i, ViewHolder viewHolder) {
        viewBinderHelper.bind(viewHolder.ItemRestaurantMenuRvSwipLayout, String.valueOf(newOffersDataList.get(i).getId()));
        viewHolder.ItemRestaurantOfferRvRestaurantNameTv.setText(newOffersDataList.get(i).getRestaurant().getName());
        viewHolder.ItemRestaurantOfferRvPriceTv.setText(context.getString(R.string.price_txt)+" "+newOffersDataList.get(i).getPrice());
        viewHolder.ItemRestaurantOfferRvOfferNameTv.setText(newOffersDataList.get(i).getName());
        Glide.with(context).load(newOffersDataList.get(i).getPhotoUrl()).into(viewHolder.ItemRestaurantOffersRvOfferIv);
        viewBinderHelper.lockSwipe(String.valueOf(newOffersDataList.get(i).getId()));
        String endin=HelperMethod.ConvertDatetoMonthFormat(newOffersDataList.get(i).getEndingAt());
        viewHolder.ItemRestaurantOfferRvOfferEndInTv.setText(context.getString(R.string.endat)+ endin);
    }

    private void SetAction(final int i, ViewHolder viewHolder) {
        viewHolder.ItemRestaurantOfferRvMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNavigationActivity instance=(UserNavigationActivity)context;
                OfferDetails offerDetails=new OfferDetails();
                Bundle bundle=new Bundle();
                bundle.putString("OfferId",String.valueOf(newOffersDataList.get(i).getId()));
                offerDetails.setArguments(bundle);
                HelperMethod.replace(offerDetails,instance.getSupportFragmentManager(),R.id.UserNavigation_RootLayout);

            }
        });
    }

    @Override
    public int getItemCount() {
        return newOffersDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_RestaurantOfferRv_SecondaryLayout)
        FrameLayout ItemRestaurantOfferRvSecondaryLayout;
        @BindView(R.id.Item_RestaurantOffersRv_OfferIv)
        ImageView ItemRestaurantOffersRvOfferIv;
        @BindView(R.id.Item_RestaurantOfferRv_offerNameTv)
        TextView ItemRestaurantOfferRvOfferNameTv;
        @BindView(R.id.Item_RestaurantOfferRv_RestaurantNameTv)
        TextView ItemRestaurantOfferRvRestaurantNameTv;
        @BindView(R.id.Item_RestaurantOfferRv_OfferEndInTv)
        TextView ItemRestaurantOfferRvOfferEndInTv;
        @BindView(R.id.Item_RestaurantOfferRv_PriceTv)
        TextView ItemRestaurantOfferRvPriceTv;
        @BindView(R.id.Item_RestaurantOfferRv_MainLayout)
        FrameLayout ItemRestaurantOfferRvMainLayout;
        @BindView(R.id.Item_RestaurantMenuRv_SwipLayout)
        SwipeRevealLayout ItemRestaurantMenuRvSwipLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

