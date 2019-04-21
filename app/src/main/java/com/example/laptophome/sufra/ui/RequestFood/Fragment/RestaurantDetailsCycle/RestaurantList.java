package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.Adapter.RestaurantListRvAdapter;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantListData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.HelperWebServicesMethods;
import com.example.laptophome.sufra.helper.OnBackPressed;
import com.example.laptophome.sufra.helper.OnEndless;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle.UserEditProfile;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle.UserLoginFragment;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantList extends Fragment implements OnBackPressed {


    @BindView(R.id.RestaurantListFr_CitySp)
    Spinner RestaurantListFrCitySp;
    @BindView(R.id.RestaurantListFr_Searchview)
    SearchView RestaurantListFrSearchview;
    @BindView(R.id.RestaurantListFr_SearchBarLayout)
    LinearLayout RestaurantListFrSearchBarLayout;
    @BindView(R.id.RestaurantListFr_RestaurantListRv)
    RecyclerView RestaurantListFrRestaurantListRv;
    List<RestaurantListData> RestaurantListData=new ArrayList<>();
    ApiService apiService;
    RestaurantListRvAdapter adapter;
    ProgressDialog progressDialog;
    String UserName;



    int maxpage;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.make_order),true,getContext());
        NavigationView navigationView =((UserNavigationActivity)getActivity()).findViewById(R.id.nav_view);
        final View view1=navigationView.getHeaderView(0);
        TextView Nav_Header_UserName=view1.findViewById(R.id.Restaurant_Nav_Heaer_UsernameTv);

        try {
            UserName=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Name_Key,getContext());
            if(UserName==null)
            {
                Nav_Header_UserName.setText(getString(R.string.please_sign_in));
                Nav_Header_UserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = view1.findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        UserLoginFragment userLogin=new UserLoginFragment();
                        HelperMethod.replace(userLogin,getFragmentManager(),R.id.UserNavigation_RootLayout);
                    }
                });

            }
            else
            {
                Nav_Header_UserName.setText(getString(R.string.welcome)+" "+UserName);
            }

        }
        catch (NullPointerException ee)
        {}
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        RestaurantListFrRestaurantListRv.setLayoutManager(layoutManager);
        OnEndless onEndless=new OnEndless(layoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {
                if(current_page<=maxpage) {
                    GetRestaurants(current_page);
                }
            }
        };
        RestaurantListFrRestaurantListRv.addOnScrollListener(onEndless);
        adapter = new RestaurantListRvAdapter(getContext(), RestaurantListData);
        RestaurantListFrRestaurantListRv.setAdapter(adapter);
        GetRestaurants(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Override
    public void onPause() {
        super.onPause();
        RestaurantListData.clear();
    }

    public void GetRestaurants(int pagenumber)
    {
        apiService.RestaurantList(pagenumber).enqueue(new Callback<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantList>() {
            @Override
            public void onResponse(Call<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantList> call, Response<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantList> response) {
                        if(progressDialog!=null&&progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        RestaurantListData.addAll(response.body().getData().getData());
                        maxpage=response.body().getData().getLastPage();

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
            public void onFailure(Call<com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantList> call, Throwable t) {
                  progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ((UserNavigationActivity) getActivity()).setOnBackPressedListener(this);
        }
        catch (ClassCastException ee)
        {}
    }

    @Override
    public void OnBackPressed() {
        ((UserNavigationActivity)getActivity()).finish();
    }
}
