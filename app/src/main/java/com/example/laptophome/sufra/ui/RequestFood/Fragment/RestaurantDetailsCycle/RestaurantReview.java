package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantReviewRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.addreview.AddReview;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantreviews.RestaurantReviews;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantreviews.RestaurantReviewsData;
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


public class RestaurantReview extends Fragment {


    ApiService apiService;
    RestaurantReviewRvAdapter adapter;
    List<RestaurantReviewsData> ReviewList = new ArrayList<>();
    int maxpage;
    Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.RestaurantReviewFr_AddReviewBtn)
    Button RestaurantReviewFrAddReviewBtn;
    @BindView(R.id.RestaurantReviewFr_HeaderTv)
    LinearLayout RestaurantReviewFrHeaderTv;
    @BindView(R.id.RestaurantReviewFr_Rv)
    RecyclerView RestaurantReviewFrRv;
    int RestaurantId;
    AlertDialog.Builder alertDialogbuilder;
    AlertDialog alertDialog;
    String api_token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_review, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.reviews_comments), false, getContext());
        api_token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.User_Api_Token, getContext());
        bundle = getArguments();
        RestaurantId = Integer.parseInt(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File, Constant.Rest_Id, getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RestaurantReviewFrRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxpage) {
                    GetRestaurantReviews(current_page);
                }
            }
        };

        String check = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File, Constant.Validation_Key, getContext());
        if (check.equals(Constant.Restaurant_User)) {
            RestaurantReviewFrHeaderTv.setVisibility(View.GONE);
        }
        RestaurantReviewFrRv.addOnScrollListener(onEndless);
        adapter = new RestaurantReviewRvAdapter(getContext(), ReviewList);
        RestaurantReviewFrRv.setAdapter(adapter);
        GetRestaurantReviews(1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        ReviewList.clear();
    }

    public void GetRestaurantReviews(int pagenumber) {
        apiService.RestaurantReviews(api_token, RestaurantId, pagenumber).enqueue(new Callback<RestaurantReviews>() {
            @Override
            public void onResponse(Call<RestaurantReviews> call, Response<RestaurantReviews> response) {
                try {

                    if (response.body().getStatus().equals(1)) {
                        maxpage = response.body().getData().getLastPage();
                        ReviewList.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<RestaurantReviews> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.RestaurantReviewFr_AddReviewBtn)
    public void onViewClicked() {
        AddReview();

    }
    public void AddReview()
    {
        final int RestaurantId= Integer.parseInt(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Id,getContext()));
        alertDialogbuilder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.alertdialouge_addreview, null);
        final TextInputLayout AlertDialouge_Review_EnterComment=view.findViewById(R.id.RestaurantReviewFr_AddCommentEt);
        Button AlertDialouge_Review_AddReviewBtn=view.findViewById(R.id.RestaurantReviewFr_AddReviewBtnn);

        final RatingBar AlertDailouge_Review_RatingBar=view.findViewById(R.id.RestaurantReviewFr_RatingBar);
            AlertDailouge_Review_RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                  AlertDailouge_Review_RatingBar.setRating(rating);
                }
            });
        AlertDialouge_Review_AddReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.AddReview(Math.round(AlertDailouge_Review_RatingBar.getRating()),
                        AlertDialouge_Review_EnterComment.getEditText().getText().toString(),RestaurantId,api_token).enqueue(new Callback<AddReview>() {
                    @Override
                    public void onResponse(Call<AddReview> call, Response<AddReview> response) {
                        try {

                            if(response.body().getStatus().equals(1))
                            {
                                alertDialog.dismiss();
                                Toast.makeText(getContext(),getString(R.string.your_review_has_beensent),Toast.LENGTH_LONG).show();
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
                    public void onFailure(Call<AddReview> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



        alertDialogbuilder.setView(view);
        alertDialog = alertDialogbuilder.create();
        alertDialog.show();

    }
}
