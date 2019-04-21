package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantreviews.RestaurantReviewsData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantReviewRvAdapter extends RecyclerView.Adapter<RestaurantReviewRvAdapter.Viewholder> {


    private Context context;
    private List<RestaurantReviewsData> ReviewList;
    private ApiService apiService;

    public RestaurantReviewRvAdapter(Context context, List<RestaurantReviewsData> reviewList) {
        this.context = context;
        ReviewList = reviewList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantreview_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        SetData(viewholder, i);
        SetAction(viewholder, i);
    }

    private void SetData(Viewholder viewholder, int i) {
        viewholder.ItemRestaurantReviewRvUserNameTv.setText(ReviewList.get(i).getClient().getName());
        viewholder.ItemRestaurantReviewRvReviewDetailTv.setText(ReviewList.get(i).getComment());
        viewholder.ItemRestaurantReviewRvRatingBar.setRating(Float.valueOf(ReviewList.get(i).getRate()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        SimpleDateFormat month_date = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        try {
            Date date = sdf.parse(ReviewList.get(i).getCreatedAt());
            String month_name = month_date.format(date);

            viewholder.ItemRestaurantReviewRvDateTv.setText(month_name);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void SetAction(Viewholder viewholder, int i) {
    }

    @Override
    public int getItemCount() {
        return ReviewList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_RestaurantReviewRv_UserNameTv)
        TextView ItemRestaurantReviewRvUserNameTv;
        @BindView(R.id.Item_RestaurantReviewRv_RatingBar)
        RatingBar ItemRestaurantReviewRvRatingBar;
        @BindView(R.id.Item_RestaurantReviewRv_DateTv)
        TextView ItemRestaurantReviewRvDateTv;
        @BindView(R.id.ItemRestaurantReviewRv_ReviewDetailTv)
        TextView ItemRestaurantReviewRvReviewDetailTv;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
