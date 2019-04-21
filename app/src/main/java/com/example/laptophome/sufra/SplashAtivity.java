package com.example.laptophome.sufra;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantList;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SplashAtivity extends AppCompatActivity {

    @BindView(R.id.splash_logo_iv)
    ImageView splashLogoIv;
    @BindView(R.id.splash_txt_tv)
    TextView splashTxtTv;
    @BindView(R.id.splash_RequestFood_btn)
    Button splashRequestFoodBtn;
    @BindView(R.id.splash_SellFood_btn)
    Button splashSellFoodBtn;
    @BindView(R.id.splash_Twitterlogo_iv)
    CircleImageView splashTwitterlogoIv;
    @BindView(R.id.splash_Instagramlogo_iv)
    CircleImageView splashInstagramlogoIv;
    @BindView(R.id.floatbtn_Layout)
    LinearLayout floatbtnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);;
        String notitoken=FirebaseInstanceId.getInstance().getToken();
        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Token_FileName,Constant.NotificationToken,notitoken,getApplicationContext());




    }


    @OnClick({R.id.splash_RequestFood_btn, R.id.splash_SellFood_btn, R.id.splash_Twitterlogo_iv, R.id.splash_Instagramlogo_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_RequestFood_btn:
                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,Constant.Client_User,getApplicationContext());
                startActivity(new Intent(this,UserNavigationActivity.class));
                break;
            case R.id.splash_SellFood_btn:
                SharedPrerefrencesManager.SetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,Constant.Restaurant_User,getApplicationContext());

                startActivity(new Intent(this,RestaurantNavigationActivity.class));
                break;
            case R.id.splash_Twitterlogo_iv:
                SocialmediaIntent("http://twitter.com","com.twitter.android");
                break;
            case R.id.splash_Instagramlogo_iv:
                SocialmediaIntent("http://instagram.com","com.instagram.android");
                break;
        }
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
