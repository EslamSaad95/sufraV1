package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.changestate.ChangeState;
import com.example.laptophome.sufra.data.model.restaurantcycle.restauranteditprofile.RestaurantEditProfile;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantdetails.RestaurantDetails;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutRestaurant extends Fragment {


    ApiService apiService;
    Unbinder unbinder;

    int RestaurantId;
    Bundle bundle;

    @BindView(R.id.AboutRestaurantFr_SaveBtn)
    Button AboutRestaurantFrSaveBtn;
    @BindView(R.id.AboutRestaurantFr_CityTv)
    EditText AboutRestaurantFrCityTv;
    @BindView(R.id.AboutRestaurantFr_AvailabiltyTv)
    EditText AboutRestaurantFrAvailabiltyTv;
    @BindView(R.id.AboutRestaurantFr_RegionTv)
    EditText AboutRestaurantFrRegionTv;
    @BindView(R.id.AboutRestaurantFr_MinimumChargerTv)
    EditText AboutRestaurantFrMinimumChargerTv;
    @BindView(R.id.AboutRestaurantFr_DeliveryCostTv)
    EditText AboutRestaurantFrDeliveryCostTv;
    boolean open = true;
    String Restaurant_Email, RestaurantPasswd, RestaurantName, RestaurantPhone, RestaurantImage, RestaurantRegion, Api_Token;
    int RestaurantCategory;
    @BindView(R.id.AboutRestaurantFr_Switch)
    Switch AboutRestaurantFrSwitch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_restaurant, container, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        unbinder = ButterKnife.bind(this, view);
        Api_Token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, getContext());
        bundle = getArguments();
        RestaurantId = Integer.parseInt(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File, Constant.Rest_Id, getContext()));
        GetRestaurantInformation();
        String check = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File, Constant.Validation_Key, getContext());
        if (check.equals(Constant.Client_User)) {
            AboutRestaurantFrSwitch.setVisibility(View.GONE);
            AboutRestaurantFrSaveBtn.setVisibility(View.GONE);
        } else {
            EditText[] edittextarray = {AboutRestaurantFrAvailabiltyTv, AboutRestaurantFrCityTv, AboutRestaurantFrDeliveryCostTv, AboutRestaurantFrMinimumChargerTv, AboutRestaurantFrRegionTv};
            for (int i = 0; i < edittextarray.length; i++) {
                edittextarray[i].setFocusable(false);
            }
        }


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void SetRestaurantState()
    {
        if(AboutRestaurantFrSwitch.isChecked())
        {
            apiService.ChangeState("open",Api_Token).enqueue(new Callback<ChangeState>() {
                @Override
                public void onResponse(Call<ChangeState> call, Response<ChangeState> response) {
                    try {

                        if(response.body().getStatus().equals(1))
                        {
                            Toast.makeText(getContext(),getString(R.string.res_is_open),Toast.LENGTH_LONG).show();
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
                public void onFailure(Call<ChangeState> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            apiService.ChangeState("closed",Api_Token).enqueue(new Callback<ChangeState>() {
                @Override
                public void onResponse(Call<ChangeState> call, Response<ChangeState> response) {
                    try {

                        if(response.body().getStatus().equals(1))
                        {
                            Toast.makeText(getContext(),getString(R.string.res_is_closed),Toast.LENGTH_LONG).show();
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
                public void onFailure(Call<ChangeState> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }
    }
    public void GetRestaurantInformation() {
        apiService.RestaurantDetails(RestaurantId).enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                try {


                    if (response.body().getStatus().equals(1)) {
                        AboutRestaurantFrCityTv.setHint(response.body().getData().getRegion().getCity().getName());
                        AboutRestaurantFrRegionTv.setHint(response.body().getData().getRegion().getName());
                        AboutRestaurantFrDeliveryCostTv.setHint(response.body().getData().getDeliveryCost());
                        AboutRestaurantFrMinimumChargerTv.setHint(response.body().getData().getMinimumCharger());
                        RestaurantName = response.body().getData().getName();
                        Restaurant_Email = response.body().getData().getEmail();
                        RestaurantPhone = response.body().getData().getPhone();
                        RestaurantImage = response.body().getData().getPhoto();
                        for (int i = 0; i < response.body().getData().getCategories().size(); i++) {
                            RestaurantCategory = response.body().getData().getCategories().get(i).getId();
                        }
                        RestaurantRegion = response.body().getData().getRegionId();
                        RestaurantPasswd = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File, Constant.Restaurant_Password, getContext());
                        if (response.body().getData().getAvailability().equals("open")) {
                            AboutRestaurantFrAvailabiltyTv.setHint(getString(R.string.res_open));
                            AboutRestaurantFrSwitch.setChecked(true);
                        } else {
                            AboutRestaurantFrAvailabiltyTv.setHint(getString(R.string.res_closed));
                        }

                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.AboutRestaurantFr_SaveBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.AboutRestaurantFr_SaveBtn:
              //  SetRestaurantEdit();
                 SetRestaurantState();
                break;
        }
    }

    public void SetRestaurantEdit() {

        String Api = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, getContext());
        apiService.SetRestaurantEdit(HelperMethod.convertToRequestBody(Restaurant_Email), HelperMethod.convertToRequestBody(RestaurantPasswd)
                , HelperMethod.convertToRequestBody(RestaurantPasswd), HelperMethod.convertToRequestBody(RestaurantName)
                , HelperMethod.convertToRequestBody(RestaurantPhone), HelperMethod.convertToRequestBody(RestaurantRegion)
                , HelperMethod.convertToRequestBody(String.valueOf(RestaurantCategory)), HelperMethod.convertToRequestBody(AboutRestaurantFrDeliveryCostTv.getText().toString())
                , HelperMethod.convertToRequestBody(AboutRestaurantFrMinimumChargerTv.getText().toString())
                , HelperMethod.convertToRequestBody(AboutRestaurantFrAvailabiltyTv.getText().toString())
                , HelperMethod.convertFileToMultipart(RestaurantImage, "photo"), HelperMethod.convertToRequestBody(Api))
                .enqueue(new Callback<RestaurantEditProfile>() {
                    @Override
                    public void onResponse(Call<RestaurantEditProfile> call, Response<RestaurantEditProfile> response) {
                        try {

                            if (response.body().getStatus().equals(1)) {
                                Toast.makeText(getContext(), "edit", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ee) {
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantEditProfile> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
