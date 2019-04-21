package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle;


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
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle.ForgetPasswordStep2Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserForgetPasswordStep1Fragment extends Fragment {


    @BindView(R.id.ForgetPasswordStep1Fr_EnterpassHeaderTv)
    TextView ForgetPasswordStep1FrEnterpassHeaderTv;
    @BindView(R.id.ForgetPasswordStep1Fr_MailEt)
    TextInputLayout ForgetPasswordStep1FrMailEt;
    @BindView(R.id.ForgetPasswordStep1Fr_SendBtn)
    Button ForgetPasswordStep1FrSendBtn;
    ApiService apiService;
    ProgressDialog progressDialog;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forgetpassword_step1, container, false);
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

    @OnClick(R.id.ForgetPasswordStep1Fr_SendBtn)
    public void onViewClicked() {
        ForgetPassword();
    }
    public void ForgetPassword()
    {   progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        String UserMail=ForgetPasswordStep1FrMailEt.getEditText().getText().toString().trim();
        HelperMethod.CheckTextInputEmpty(ForgetPasswordStep1FrMailEt);
        apiService.UserResetPassword(UserMail).enqueue(new Callback<UserResetPassword>() {
            @Override
            public void onResponse(Call<UserResetPassword> call, Response<UserResetPassword> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        ForgetPasswordStep2Fragment forgetPasswordStep2Fragment=new ForgetPasswordStep2Fragment();
                        HelperMethod.replace(forgetPasswordStep2Fragment,getFragmentManager(),R.id.UserNavigation_RootLayout);
                    }
                    else
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {}
            }

            @Override
            public void onFailure(Call<UserResetPassword> call, Throwable t) {
                progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG);
            }
        });

    }
}
