package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.Adapter.TabAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle.UserLoginFragment;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle.RestaurantLoignFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetails extends Fragment {
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
    @BindView(R.id.RestaurantDetailsFr_TabLayout)
    TabLayout RestaurantDetailsFrTabLayout;
    @BindView(R.id.RestaurantDetailsFr_ViewPager)
    ViewPager RestaurantDetailsFrViewPager;
    ApiService apiService;
    TabAdapter tabAdapter;
    int RestaurantId;
    String RestaurantName;
    Unbinder unbinder;
    @BindView(R.id.Item_RestaurantListRv_RatingBar)
    RatingBar ItemRestaurantListRvRatingBar;
    @BindView(R.id.Item_RestaurantListRv_Root)
    LinearLayout ItemRestaurantListRvRoot;




    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Restaurant_User))
        { NavigationView navigationView =((RestaurantNavigationActivity)getActivity()).findViewById(R.id.nav_view);
            final View view1=navigationView.getHeaderView(0);
            TextView Nav_Header_UserName=view1.findViewById(R.id.Restaurant_Nav_Heaer_UsernameTv);
            RestaurantNavigationActivity.finished=true;
            try {
                RestaurantName=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Name,getContext());
                if(RestaurantName==null)
                {
                    Nav_Header_UserName.setText(getString(R.string.please_sign_in));
                    Nav_Header_UserName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DrawerLayout drawer = view1.findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            RestaurantLoignFragment restaurantLoignFragment=new RestaurantLoignFragment();
                            HelperMethod.replace(restaurantLoignFragment,getFragmentManager(),R.id.SellNavigation_RootLayout);
                        }
                    });

                }
                else
                {
                    Nav_Header_UserName.setText(getString(R.string.welcome)+" "+RestaurantName);
                }

            }
            catch (NullPointerException ee)
            {}



        }
        RestaurantId=Integer.parseInt(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Id,getContext()));
        GetRestaurantDetails();
        RestaurantMenu restaurantMenu = new RestaurantMenu();
       // restaurantMenu.setArguments(bundle);
        RestaurantReview restaurantReview = new RestaurantReview();
        //restaurantReview.setArguments(bundle);
        AboutRestaurant aboutRestaurant = new AboutRestaurant();
       // aboutRestaurant.setArguments(bundle);
        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addfragment(restaurantMenu, getString(R.string.restaurant_menu));
        tabAdapter.addfragment(restaurantReview, getString(R.string.reviews_comments));
        tabAdapter.addfragment(aboutRestaurant, getString(R.string.about_resaurant));
        RestaurantDetailsFrTabLayout.setupWithViewPager(RestaurantDetailsFrViewPager);
        RestaurantDetailsFrViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                switch (i)
                {
                    case 0:
                        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Client_User)) {
                            HelperMethod.SetToolBar(getString(R.string.restaurant_menu), false, getContext());
                        }
                        else
                        {
                            HelperMethod.SetToolBar(getString(R.string.restaurant_menu), true, getContext());
                        }
                        break;
                    case 1:
                        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Client_User)) {
                            HelperMethod.SetToolBar(getString(R.string.reviews_comments),false,getContext());
                        }
                        else
                        {
                            HelperMethod.SetToolBar(getString(R.string.reviews_comments),true,getContext());
                        }

                        break;
                    case 2:
                        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Client_User)) {
                            HelperMethod.SetToolBar(getString(R.string.about_resaurant),false,getContext());
                        }
                      else
                        {
                            HelperMethod.SetToolBar(getString(R.string.about_resaurant),true,getContext());
                        }
                        break;
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        RestaurantDetailsFrViewPager.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();

        return view;
    }
    public void GetRestaurantDetails() {


        apiService.RestaurantDetails(RestaurantId).enqueue(new Callback<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantdetails.RestaurantDetails>() {
            @Override
            public void onResponse(Call<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantdetails.RestaurantDetails> call, Response<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantdetails.RestaurantDetails> response) {
                try {
                    if (response.body().getStatus().equals(1)) {
                        for (int i = 0; i < response.body().getData().getCategories().size(); i++) {
                            ItemRestaurantListRvRestaurantCategoryTv.setText(response.body().getData().getCategories().get(i).getName());

                        }
                        ItemRestaurantListRvRatingBar.setRating(Float.valueOf(response.body().getData().getRate()));
                        ItemRestaurantListRvRestaurantTitleTv.setText(response.body().getData().getName());
                        ItemRestaurantListRvDeliveryCommsionTv.setText(response.body().getData().getDeliveryCost());
                        ItemRestaurantListRvMinRequestTv.setText(response.body().getData().getMinimumCharger());
                        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_DeliveryCost_Key,response.body().getData().getDeliveryCost(),getContext());
                       SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Id,String.valueOf(response.body().getData().getId()),getContext());
                        Glide.with(getContext()).load(response.body().getData().getPhotoUrl()).into(ItemRestaurantListRvRestaurantIv);
                        if (response.body().getData().getAvailability().equals("closed")) {
                            ItemRestaurantListRvOpenTv.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantdetails.RestaurantDetails> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        apiService = RetrofitClient.getclient().create(ApiService.class);
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Restaurant_User))
        {
            RestaurantNavigationActivity.finished=false;
        }
    }
}
