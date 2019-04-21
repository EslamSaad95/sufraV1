package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.registernotificationtoke.RegisterUnregisterNotiToken;
import com.example.laptophome.sufra.data.model.restaurantcycle.restaurantlogin.RestaurantLogin;
import com.example.laptophome.sufra.data.model.userRequests.Restaurant;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantList;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantLoignFragment extends Fragment {


    Unbinder unbinder;
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
            RestaurantNavigationActivity.finished=true;


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        RestaurantNavigationActivity.finished=false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void RegisterNotificationToken()
    {
        String Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getContext());
        String Notification_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.NotificationToken,getContext());
        apiService.RegisterRestaurantNotificationToken(Notification_Token,"android",Api_Token).enqueue(new Callback<RegisterUnregisterNotiToken>() {
            @Override
            public void onResponse(Call<RegisterUnregisterNotiToken> call, Response<RegisterUnregisterNotiToken> response) {
                if(response.body().getStatus().equals(1))
                {
                    Toast.makeText(getContext(),"noti register",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUnregisterNotiToken> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @OnClick({R.id.LoginFr_LoginBtn, R.id.LoginFr_ForgetPasswordTv, R.id.LoignFr_RegisterBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.LoginFr_LoginBtn:
                RestaurantLogin();


                break;
            case R.id.LoginFr_ForgetPasswordTv:
                ForgetPsswordStep1Fragment forgetPsswordStep1Fragment=new ForgetPsswordStep1Fragment();
                HelperMethod.replace(forgetPsswordStep1Fragment,getFragmentManager(),R.id.SellNavigation_RootLayout);
                break;
            case R.id.LoignFr_RegisterBtn:
               RestaurantRegisterFragment1 registerFragment=new RestaurantRegisterFragment1();
                HelperMethod.replace(registerFragment,getFragmentManager(),R.id.SellNavigation_RootLayout);
                break;
        }
    }



    public void RestaurantLogin()
    {
        String ResaurantMail=LoginFrMailEt.getEditText().getText().toString().trim();
        final String RestaurantPass=LoginFrPasswordEt.getEditText().getText().toString().trim();
        TextInputLayout[]TextinputArray={LoginFrMailEt,LoginFrPasswordEt};
        for(int i=0;i<TextinputArray.length;i++)
        {
            HelperMethod.CheckTextInputEmpty(TextinputArray[i]);
        }
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.login_txt));
        apiService.RestaurantLogin(ResaurantMail,RestaurantPass).enqueue(new Callback<RestaurantLogin>() {
            @Override
            public void onResponse(Call<RestaurantLogin> call, Response<RestaurantLogin> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,Constant.Restaurant_User,getContext());
                        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,response.body().getData().getApiToken(),getContext());
                        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.RestaurantId,String.valueOf(response.body().getData().getUser().getId()),getContext());
                        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Restaurant_Email,response.body().getData().getUser().getEmail(),getContext());
                       SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Name,response.body().getData().getUser().getName(),getContext());
                       SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Restaurant_Password,RestaurantPass,getContext());
                      RegisterNotificationToken();
                        RestaurantDetails details=new RestaurantDetails();
                        HelperMethod.replace(details,getFragmentManager(),R.id.SellNavigation_RootLayout);
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
            public void onFailure(Call<RestaurantLogin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


    }
}
