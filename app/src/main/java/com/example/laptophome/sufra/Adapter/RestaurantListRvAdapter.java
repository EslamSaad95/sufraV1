package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantListData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListRvAdapter extends RecyclerView.Adapter<RestaurantListRvAdapter.viewholder> {


    private Context context;
    private List<RestaurantListData> RestaurantList;
    ApiService apiService;

    public RestaurantListRvAdapter(Context context, List<RestaurantListData> restaurantList) {
        this.context = context;
        RestaurantList = restaurantList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantlist_recyleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new viewholder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder, int i) {
        setData(viewholder, i);
        setAction(viewholder, i);
    }

    private void setData(viewholder viewholder, int i) {
        viewholder.ItemRestaurantListRvRestaurantTitleTv.setText(RestaurantList.get(i).getName());
        String txt="";
        viewholder.ItemRestaurantListRvRatingBar.setRating(Float.valueOf(RestaurantList.get(i).getRate()));
        viewholder.ItemRestaurantListRvDeliveryCommsionTv.setText(RestaurantList.get(i).getDeliveryCost()+context.getString(R.string.saudi_real));
        viewholder.ItemRestaurantListRvMinRequestTv.setText(RestaurantList.get(i).getMinimumCharger()+context.getString(R.string.saudi_real));
        Glide.with(context).load(RestaurantList.get(i).getPhotoUrl()).into(viewholder.ItemRestaurantListRvRestaurantIv);
        if (RestaurantList.get(i).getAvailability().equals("closed")) {
            viewholder.ItemRestaurantListRvOpenTv.setVisibility(View.GONE);
        }

        for(int j=0;j<RestaurantList.get(i).getCategories().size();j++)
        {
            txt=RestaurantList.get(i).getCategories().get(j).getName();
        }
          viewholder.ItemRestaurantListRvRestaurantCategoryTv.setText(txt);

    }

    private void setAction(viewholder viewholder, final int i) {
        viewholder.ItemRestaurantListRvRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantDetails restaurantDetails=new RestaurantDetails();
                UserNavigationActivity activityinstance = (UserNavigationActivity) context;
                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Id,String.valueOf(RestaurantList.get(i).getId()),context);
                HelperMethod.replace(restaurantDetails, activityinstance.getSupportFragmentManager(), R.id.UserNavigation_RootLayout);
            }
        });

    }

    @Override
    public int getItemCount() {
        return RestaurantList.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_RestaurantListRv_RatingBar)
        RatingBar ItemRestaurantListRvRatingBar;
        @BindView(R.id.Item_RestaurantListRv_RestaurantIv)
        ImageView ItemRestaurantListRvRestaurantIv;
        @BindView(R.id.Item_RestaurantListRv_RestaurantTitleTv)
        TextView ItemRestaurantListRvRestaurantTitleTv;
        @BindView(R.id.Item_RestaurantListRv_RestaurantCategoryTv)
        TextView ItemRestaurantListRvRestaurantCategoryTv;
        @BindView(R.id.Item_RestaurantListRv_RestaurantTitleLayout)
        LinearLayout ItemRestaurantListRvRestaurantTitleLayout;
        @BindView(R.id.Item_RestaurantListRv_OpenTv)
        TextView ItemRestaurantListRvOpenTv;
        @BindView(R.id.Item_RestaurantListRv_MinRequestTxt)
        TextView ItemRestaurantListRvMinRequestTxt;
        @BindView(R.id.Item_RestaurantListRv_MinRequestTv)
        TextView ItemRestaurantListRvMinRequestTv;
        @BindView(R.id.Item_RestaurantListRv_DeliveryCommsionTxt)
        TextView ItemRestaurantListRvDeliveryCommsionTxt;
        @BindView(R.id.Item_RestaurantListRv_DeliveryCommsionTv)
        TextView ItemRestaurantListRvDeliveryCommsionTv;

        @BindView(R.id.Item_RestaurantListRv_Root)
        LinearLayout ItemRestaurantListRvRoot;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);

        }
    }
}
