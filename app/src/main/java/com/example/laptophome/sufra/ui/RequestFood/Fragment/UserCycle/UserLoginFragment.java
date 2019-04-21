package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.registernotificationtoke.RegisterUnregisterNotiToken;
import com.example.laptophome.sufra.data.model.usercycle.userlogin.UserLogin;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.OnBackPressed;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantList;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserLoginFragment extends Fragment implements OnBackPressed {

    @BindView(R.id.LoginFr_HeaderTv)
    TextView LoginFrHeaderTv;
    @BindView(R.id.LoginFr_MailEt)
    TextInputLayout LoginFrMailEt;
    @BindView(R.id.LoginFr_PasswordEt)
    TextInputLayout LoginFrPasswordEt;
    @BindView(R.id.LoginFr_LoginBtn)
    Button LoginFrLoginBtn;
    @BindView(R.id.LoginFr_ForgetPasswordTv)
    TextView LoginFrForgetPasswordTv;
    @BindView(R.id.LoignFr_RegisterBtn)
    Button LoignFrRegisterBtn;
    Unbinder unbinder;
    ApiService apiService;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.login_txt),true,getContext());
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


    @OnClick({R.id.LoginFr_LoginBtn, R.id.LoginFr_ForgetPasswordTv, R.id.LoignFr_RegisterBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.LoginFr_LoginBtn:
                UserLoign();
                break;
            case R.id.LoginFr_ForgetPasswordTv:
                UserForgetPasswordStep1Fragment forgetPasswordStep1Fragment=new UserForgetPasswordStep1Fragment();
                HelperMethod.replace(forgetPasswordStep1Fragment,getFragmentManager(),R.id.UserNavigation_RootLayout);
                break;
            case R.id.LoignFr_RegisterBtn:
                UserRegisterFragment userRegisterFragment=new UserRegisterFragment();
                HelperMethod.replace(userRegisterFragment,getFragmentManager(),R.id.UserNavigation_RootLayout);
                break;
        }
    }
    public void UserLoign()
    { progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.login_txt));
        TextInputLayout[]TextinputLayoutArray={LoginFrMailEt,LoginFrPasswordEt};
        String UserMail=LoginFrMailEt.getEditText().getText().toString().trim();
        String UserPassword=LoginFrPasswordEt.getEditText().getText().toString().trim();
        for(int i=0;i<TextinputLayoutArray.length;i++)
        {if(HelperMethod.CheckTextInputEmpty(TextinputLayoutArray[i]))
        {

            apiService.UserLogin(UserMail,UserPassword).enqueue(new Callback<UserLogin>() {
                @Override
                public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                    progressDialog.dismiss();
                    try {
                        if (response.body().getStatus().equals(1))
                        {
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,Constant.Client_User,getContext());
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,response.body().getData().getApiToken(),getContext());
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Address_Key,response.body().getData().getUser().getAddress(),getContext());
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Name_Key,response.body().getData().getUser().getName(),getContext());
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Phone_Key,response.body().getData().getUser().getPhone(),getContext());
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.User_Password,LoginFrPasswordEt.getEditText().getText().toString(),getContext());
                            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.User_File,Constant.UserEmail,response.body().getData().getUser().getEmail(),getContext());

                            RegisterClientNotification(response.body().getData().getApiToken());
                            RestaurantList restaurantList=new RestaurantList();
                            HelperMethod.replace(restaurantList,getFragmentManager(),R.id.UserNavigation_RootLayout);
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
                public void onFailure(Call<UserLogin> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }
        }

    }

    public void RegisterClientNotification(final String Api_Token)
    {
      //  String Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,getContext());
        final String NotificationToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.NotificationToken,getContext());
        apiService.RegisterClientNotificationToken(NotificationToken,"android",Api_Token).enqueue(new Callback<RegisterUnregisterNotiToken>() {
            @Override
            public void onResponse(Call<RegisterUnregisterNotiToken> call, Response<RegisterUnregisterNotiToken> response) {
                try {
                    if(response.body().getStatus().equals(0))
                    {
                       Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ee)
                {}
            }

            @Override
            public void onFailure(Call<RegisterUnregisterNotiToken> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnBackPressed() {
        ((UserNavigationActivity)getActivity()).finish();
    }
}
