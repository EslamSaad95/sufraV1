package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.model.usercycle.userresetpassword.UserResetPassword;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPasswordStep2Fragment extends Fragment {


    @BindView(R.id.ForgetPasswordStep2Fr_CodeEt)
    TextInputLayout ForgetPasswordStep2FrCodeEt;
    @BindView(R.id.ForgetPasswordStep2Fr_enterpasswordTv)
    TextView ForgetPasswordStep2FrEnterpasswordTv;
    @BindView(R.id.ForgetPasswordStep2Fr_NewPasswordEt)
    TextInputLayout ForgetPasswordStep2FrNewPasswordEt;
    @BindView(R.id.ForgetPasswordStep2Fr_RepeatNewPasswordEt)
    TextInputLayout ForgetPasswordStep2FrRepeatNewPasswordEt;
    @BindView(R.id.ForgetPasswordStep2Fr_PerformBtn)
    Button ForgetPasswordStep2FrPerformBtn;
    ProgressDialog progressDialog;
    ApiService apiService;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forgetpassword_step2, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.foreget_password),true,getContext());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ForgetPasswordStep2Fr_PerformBtn)
    public void onViewClicked() {
        RestaurantNewPassword();
    }
    public void RestaurantNewPassword()
    {

        String code=ForgetPasswordStep2FrCodeEt.getEditText().getText().toString().trim();
        String NewPassword=ForgetPasswordStep2FrNewPasswordEt.getEditText().getText().toString();
        String ConfirmNewPassword=ForgetPasswordStep2FrRepeatNewPasswordEt.getEditText().getText().toString();
        TextInputLayout[]TextInputLayoutArray={ForgetPasswordStep2FrCodeEt,ForgetPasswordStep2FrNewPasswordEt,ForgetPasswordStep2FrRepeatNewPasswordEt};
        for(int i=0;i<TextInputLayoutArray.length;i++)
        {HelperMethod.CheckTextInputEmpty(TextInputLayoutArray[i]);
        }
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        apiService.SetRestaurantNewPassword(code,NewPassword,ConfirmNewPassword).enqueue(new Callback<UserResetPassword>() {
            @Override
            public void onResponse(Call<UserResetPassword> call, Response<UserResetPassword> response) {
                progressDialog.dismiss();
                try {
                  if(response.body().getStatus().equals(1))
                  {
                      RestaurantLoignFragment restaurantLoignFragment=new RestaurantLoignFragment();
                      HelperMethod.replace(restaurantLoignFragment,getFragmentManager(),R.id.SellNavigation_RootLayout);
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
            public void onFailure(Call<UserResetPassword> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
