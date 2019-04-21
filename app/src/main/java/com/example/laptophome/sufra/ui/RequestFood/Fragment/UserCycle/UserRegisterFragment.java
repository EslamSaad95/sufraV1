package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.usercycle.userregister.UserRegister;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.HelperWebServicesMethods;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegisterFragment extends Fragment {


    @BindView(R.id.UserRegisterFr_NameEt)
    TextInputLayout UserRegisterFrNameEt;
    @BindView(R.id.UserRegisterFr_MailEt)
    TextInputLayout UserRegisterFrMailEt;
    @BindView(R.id.UserRegisterFr_PhoneEt)
    TextInputLayout UserRegisterFrPhoneEt;
    @BindView(R.id.UserRegisterFr_CitySp)
    Spinner UserRegisterFrCitySp;
    @BindView(R.id.UserRegisterFr_CitySpLayout)
    RelativeLayout UserRegisterFrCitySpLayout;
    @BindView(R.id.UserRegisterFr_RegionSp)
    Spinner UserRegisterFrRegionSp;
    @BindView(R.id.UserRegisterFr_RegionSpLayout)
    RelativeLayout UserRegisterFrRegionSpLayout;
    @BindView(R.id.UserRegisterFr_PasswordEt)
    TextInputLayout UserRegisterFrPasswordEt;
    @BindView(R.id.UserRegisterFr_RepeatPasswordEt)
    TextInputLayout UserRegisterFrRepeatPasswordEt;
    @BindView(R.id.RequestRegisterFr_InputRootLayout)
    LinearLayout RequestRegisterFrInputRootLayout;
    @BindView(R.id.UserRegisterFr_RegisterBtn)
    Button UserRegisterFrRegisterBtn;
    Unbinder unbinder;
    @BindView(R.id.UserRegisterFr_PlaceDescriptionEt)
    TextInputLayout UserRegisterFrPlaceDescriptionEt;
    ApiService apiService;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.new_account),true,getContext());
        HelperWebServicesMethods.GetCities(getContext(), UserRegisterFrCitySp);
        HelperWebServicesMethods.GetRegions(getContext(), UserRegisterFrRegionSp, UserRegisterFrCitySp);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.UserRegisterFr_RegisterBtn)
    public void onViewClicked() {
        TextInputLayout[]Textinputarray={UserRegisterFrNameEt,UserRegisterFrMailEt,UserRegisterFrPhoneEt,UserRegisterFrPasswordEt
                ,UserRegisterFrRepeatPasswordEt,UserRegisterFrPlaceDescriptionEt};
        for(int i=0;i<Textinputarray.length;i++) {
             HelperMethod.CheckTextInputEmpty(Textinputarray[i]);
        }

        UserRegisteration();
    }

    public void UserRegisteration() {
        String UserName = UserRegisterFrNameEt.getEditText().getText().toString().trim();
        String UserMail = UserRegisterFrMailEt.getEditText().getText().toString().trim();
        String UserPhone = UserRegisterFrPhoneEt.getEditText().getText().toString().trim();
        String UserPassword = UserRegisterFrPasswordEt.getEditText().getText().toString().trim();
        String UserConfirmPass = UserRegisterFrRepeatPasswordEt.getEditText().getText().toString().trim();
        String UserPlaceDetails = UserRegisterFrPlaceDescriptionEt.getEditText().getText().toString().trim();
        int RegionId= (int) UserRegisterFrRegionSp.getSelectedItemId();
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.Registeration));
        apiService.UserRegister(UserName,UserMail,UserPassword,UserConfirmPass,UserPhone,UserPlaceDetails,RegionId)
                .enqueue(new Callback<UserRegister>() {
                    @Override
                    public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                        progressDialog.dismiss();
                        try {

                            if(response.body().getStatus().equals(1))
                            {
                                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,response.body().getData().getApiToken(),getContext());
                                RestaurantList restaurantList=new RestaurantList();
                                HelperMethod.replace(restaurantList,getFragmentManager(),R.id.UserNavigation_RootLayout);

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
                    public void onFailure(Call<UserRegister> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

    }
}
