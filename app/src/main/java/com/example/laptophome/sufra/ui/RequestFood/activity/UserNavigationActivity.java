package com.example.laptophome.sufra.ui.RequestFood.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.RoomDB.RoomDao;
import com.example.laptophome.sufra.data.local.RoomDB.RoomManager;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.registernotificationtoke.RegisterUnregisterNotiToken;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.OnBackPressed;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.OffersCycle.NewOffers;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.Notification;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantList;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserCycle.UserLoginFragment;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.MyRequests.MyRequests;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.AboutApp;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.ContactUs;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnBackPressed {
    public OnBackPressed onBackPressedobject;
    @BindView(R.id.ToolbarTitle)
    TextView ToolbarTitle;
    @BindView(R.id.ToolBar_NotificationIv)
    ImageView ToolBarNotificationIv;
    @BindView(R.id.Toolbar_ArrowIv)
    RelativeLayout ToolbarArrowIv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    public static boolean finished = false;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    RoomDao roomDao;
    ApiService apiService;
    String UserName;


    public void setOnBackPressedListener(OnBackPressed onBackPressedListener) {
        this.onBackPressedobject = onBackPressedListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);
        ButterKnife.bind(this);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigationdrawer);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigationdrawer);



        navView.setNavigationItemSelectedListener(this);


        View view=navView.getHeaderView(0);
        TextView Nav_Header_UserName=view.findViewById(R.id.Restaurant_Nav_Heaer_UsernameTv);
        try {
            UserName=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File,Constant.User_Name_Key,getApplicationContext());
            if(UserName==null)
            {
                Nav_Header_UserName.setText(getString(R.string.please_sign_in));
                Nav_Header_UserName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        UserLoginFragment userLogin=new UserLoginFragment();
                        HelperMethod.replace(userLogin,getSupportFragmentManager(),R.id.UserNavigation_RootLayout);
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

        CheckLogin();
    }
    public void HideKeybiard(View view)
    {
        HelperMethod.hidesoftkeyboard(view,this);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,getApplicationContext())==null)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            int id = item.getItemId();

            if (id == R.id.nav_myrequest) {
                MyRequests myRequests = new MyRequests();
                HelperMethod.replace(myRequests, getSupportFragmentManager(), R.id.UserNavigation_RootLayout);
            } else if (id == R.id.nav_newoffers) {
                NewOffers newOffers=new NewOffers();
                HelperMethod.replace(newOffers,getSupportFragmentManager(),R.id.UserNavigation_RootLayout);


            } else if (id == R.id.nav_contactus) {
                ContactUs contactUs = new ContactUs();
                HelperMethod.replace(contactUs, getSupportFragmentManager(), R.id.UserNavigation_RootLayout);
            } else if (id == R.id.nav_signout) {
                RemoveClientNotificationToken();
                SharedPrerefrencesManager.clearsharedprefrences(Constant.Token_FileName, getApplicationContext(), Constant.User_Api_Token);
                SharedPrerefrencesManager.clearsharedprefrences(Constant.User_File,getApplicationContext(),Constant.User_Name_Key);
                ClearDatabase();
                finish();

            }
            else if(id==R.id.nav_notification)
            {
                Notification notification=new Notification();
                HelperMethod.replace(notification,getSupportFragmentManager(),R.id.UserNavigation_RootLayout);
            }
            else if(id==R.id.nav_aboutapp)
            {
                AboutApp aboutApp=new AboutApp();
                HelperMethod.replace(aboutApp,getSupportFragmentManager(),R.id.UserNavigation_RootLayout);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void OnBackPressed() {

    }
    public void ClearDatabase()
    {
        roomDao = RoomManager.getinstance(getApplicationContext()).roomDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                roomDao.DeleteAll();
            }
        });
    }

    public void RemoveClientNotificationToken()
    {
        String NotificationToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.NotificationToken,getApplicationContext());
        String Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,getApplicationContext());
        apiService.RemoveClientNotificationToken(NotificationToken,Api_Token).enqueue(new Callback<RegisterUnregisterNotiToken>() {
            @Override
            public void onResponse(Call<RegisterUnregisterNotiToken> call, Response<RegisterUnregisterNotiToken> response) {
                try {

                    if(response.body().getStatus().equals(0))
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
    public void CheckLogin()
    {
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.User_Api_Token,getApplicationContext())!=null)
        {

            RestaurantList list=new RestaurantList();
            HelperMethod.replace(list,getSupportFragmentManager(),R.id.UserNavigation_RootLayout);
        }
        else
        {
            UserLoginFragment restaurantList = new UserLoginFragment();
            HelperMethod.replace(restaurantList, getSupportFragmentManager(), R.id.UserNavigation_RootLayout);
        }
    }


}
