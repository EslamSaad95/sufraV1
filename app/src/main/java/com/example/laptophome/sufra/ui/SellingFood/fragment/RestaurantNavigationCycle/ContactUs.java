package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.addcontact.AddContact;
import com.example.laptophome.sufra.data.model.settings.Settings;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactUs extends Fragment {


    @BindView(R.id.ContactUsFr_NameEt)
    TextInputLayout ContactUsFrNameEt;
    @BindView(R.id.ContactUsFr_EmailEt)
    TextInputLayout ContactUsFrEmailEt;
    @BindView(R.id.ContactUsFr_PhoneEt)
    TextInputLayout ContactUsFrPhoneEt;
    @BindView(R.id.ContactUsFr_MessageContentEt)
    TextInputLayout ContactUsFrMessageContentEt;
    @BindView(R.id.suggestionRadioBtn)
    RadioButton suggestionRadioBtn;
    @BindView(R.id.complaintRadioBtn)
    RadioButton complaintRadioBtn;
    @BindView(R.id.inquiryRadioBtn)
    RadioButton inquiryRadioBtn;
    @BindView(R.id.ContactUsFr_SendBtn)
    Button ContactUsFrSendBtn;
    @BindView(R.id.ContactUsFr_TwitterIv)
    CircleImageView ContactUsFrTwitterIv;
    @BindView(R.id.ContactUsFr_InstagramIv)
    CircleImageView ContactUsFrInstagramIv;
    @BindView(R.id.ContactUsFr_FacebookIv)
    CircleImageView ContactUsFrFacebookIv;
    ApiService apiService;
    String Name, Email, Phone, MessageBody,Type;
    String UserEmail,UserPassword,FaceBookLink,InstagramLink,TwitterLink;
    Unbinder unbinder;
    @BindView(R.id.ContactUFr_RadioGroup)
    RadioGroup ContactUFrRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.contact_us),true,getContext());
        GetSetting();
        ContactUFrRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.suggestionRadioBtn)
                {
                 Type="suggestion";
                }
                else if(checkedId==R.id.inquiryRadioBtn)
                {
                    Type="inquiry";
                }
                else
                {
                    Type="complaint";
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ContactUsFr_SendBtn, R.id.ContactUsFr_TwitterIv, R.id.ContactUsFr_InstagramIv, R.id.ContactUsFr_FacebookIv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ContactUsFr_SendBtn:
                ContactUs();
                break;
            case R.id.ContactUsFr_TwitterIv:
                SocialmediaIntent(TwitterLink,"com.twitter.android");
                break;
            case R.id.ContactUsFr_InstagramIv:
                SocialmediaIntent(InstagramLink,"com.instagram.android");
                break;
            case R.id.ContactUsFr_FacebookIv:
                SocialmediaIntent(FaceBookLink,"com.facebook.katana");
                break;
        }
    }

    public void ContactUs() {
        Name = ContactUsFrNameEt.getEditText().getText().toString();
        Email = ContactUsFrEmailEt.getEditText().getText().toString();
        Phone = ContactUsFrPhoneEt.getEditText().getText().toString();
        MessageBody = ContactUsFrMessageContentEt.getEditText().getText().toString();
                apiService.ContactUs(Name,Email,Phone,Type,MessageBody).enqueue(new Callback<AddContact>() {
                    @Override
                    public void onResponse(Call<AddContact> call, Response<AddContact> response) {
                        try {

                            if(response.body().getStatus().equals(1))
                            {
                                Toast.makeText(getContext(),getString(R.string.message_has_been_sent_txt),Toast.LENGTH_LONG).show();
                                if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Client_User))
                                {
                                    RestaurantMenu menu=new RestaurantMenu();
                                    HelperMethod.replace(menu,getFragmentManager(),R.id.UserNavigation_RootLayout);
                                }
                                else
                                {
                                    RestaurantDetails details=new RestaurantDetails();
                                    HelperMethod.replace(details,getFragmentManager(),R.id.SellNavigation_RootLayout);
                                }

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
                    public void onFailure(Call<AddContact> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void GetSetting()
    {
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,getContext()).equals(Constant.Client_User))
        {
            UserEmail=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.UserEmail,getContext());
            UserPassword=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Password,getContext());
        }
        else
        {
            UserEmail=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File,Constant.Restaurant_Email,getContext());
            UserPassword=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File,Constant.Restaurant_Password,getContext());
        }
        apiService.GetSetting(UserEmail,UserPassword).enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        FaceBookLink=response.body().getData().getFacebook();
                        InstagramLink=response.body().getData().getInstagram();
                        TwitterLink=response.body().getData().getTwitter();
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
            public void onFailure(Call<Settings> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void SocialmediaIntent(String url, String packagename) {
        Uri uri = Uri.parse(url);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage(packagename);

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        }
    }
}
