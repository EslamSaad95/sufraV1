package com.example.laptophome.sufra.ui.SellingFood.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.registernotificationtoke.RegisterUnregisterNotiToken;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.OnBackPressed;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle.RestaurantLoignFragment;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.AboutApp;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.CommisionFragment;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.ContactUs;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RequestedSubmitted.RestaurantRequestsTab;

import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers.RestaurantOffers;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantProducts.RestaurantProducts;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnBackPressed {

    @BindView(R.id.nav_view)
    NavigationView navView;
    ActionBarDrawerToggle toggle;
    public  static  boolean finished=false;
    public OnBackPressed onBackPressedobject;
    ApiService apiService;
    String RestaurantName;


    public void setOnBackPressedListener(OnBackPressed onBackPressedListener) {
        this.onBackPressedobject = onBackPressedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_navigation);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigationdrawer);
        navigationView.setNavigationItemSelectedListener(this);
        View view=navView.getHeaderView(0);
        TextView Nav_Header_UserName=view.findViewById(R.id.Restaurant_Nav_Heaer_UsernameTv);

        try {
            RestaurantName=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Name,getApplicationContext());
            if(RestaurantName==null)
            {
                Nav_Header_UserName.setText(getString(R.string.please_sign_in));
                Nav_Header_UserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        RestaurantLoignFragment restaurantLoignFragment=new RestaurantLoignFragment();
                        HelperMethod.replace(restaurantLoignFragment,getSupportFragmentManager(),R.id.SellNavigation_RootLayout);
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

        CheckLogin();
    }







    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getApplicationContext())==null)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            int id = item.getItemId();

            if (id == R.id.nav_signout) {
                RemoveNotificationToken();
                SharedPrerefrencesManager.clearsharedprefrences(Constant.Token_FileName, getApplicationContext(), Constant.Restaurant_Api_Token);
                SharedPrerefrencesManager.clearsharedprefrences(Constant.Restaurant_File,getApplicationContext(),Constant.Rest_Name);
                SharedPrerefrencesManager.clearsharedprefrences(Constant.Restaurant_File,getApplicationContext(),Constant.Rest_Id);
                finish();

            }

            if (id == R.id.nav_myproducts) {
                RestaurantProducts products = new RestaurantProducts();
                HelperMethod.replace(products, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }

            if (id == R.id.nav_myoffers) {
               RestaurantOffers restaurantOffers = new RestaurantOffers();
                HelperMethod.replace(restaurantOffers, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }
            if (id == R.id.nav_contactus) {
                ContactUs contactUs = new ContactUs();
                HelperMethod.replace(contactUs, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }
            if (id == R.id.nav_requests_submitted) {
                RestaurantRequestsTab restaurantRequestsTab = new RestaurantRequestsTab();
                HelperMethod.replace(restaurantRequestsTab, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }
            if (id == R.id.nav_home) {
                RestaurantDetails details = new RestaurantDetails();
                HelperMethod.replace(details, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }
            if (id == R.id.nav_commision) {
                CommisionFragment commisionFragment = new CommisionFragment();
                HelperMethod.replace(commisionFragment, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }
            if (id == R.id.nav_aboutapp) {
                AboutApp aboutApp = new AboutApp();
                HelperMethod.replace(aboutApp, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedobject != null) {
            onBackPressedobject.OnBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();

        }
        if (finished == true) {
            finish();

        }

    }

    public void RemoveNotificationToken()
    {
        String apiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getApplicationContext());
        String NotiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.NotificationToken,getApplicationContext());
        apiService.RemoveRestaurantNotificationToken(NotiToken,apiToken).enqueue(new Callback<RegisterUnregisterNotiToken>() {
            @Override
            public void onResponse(Call<RegisterUnregisterNotiToken> call, Response<RegisterUnregisterNotiToken> response) {
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        Toast.makeText(getApplicationContext(),"The Notification Token is Removed",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ee)
                {}
            }

            @Override
            public void onFailure(Call<RegisterUnregisterNotiToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnBackPressed() {

    }
    public void HiderKeyboard(View view)
    {
        HelperMethod.hidesoftkeyboard(view,this);
    }

    public void CheckLogin()
    {
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getApplicationContext())!=null)
        {

            RestaurantDetails restaurantDetails=new RestaurantDetails();
            HelperMethod.replace(restaurantDetails,getSupportFragmentManager(),R.id.SellNavigation_RootLayout);
        }
        else
        {
            RestaurantLoignFragment restaurantLoignFragment = new RestaurantLoignFragment();
            HelperMethod.replace(restaurantLoignFragment, getSupportFragmentManager(), R.id.SellNavigation_RootLayout);
        }
    }
}
