package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.categories.Categories;
import com.example.laptophome.sufra.data.model.restaurantcycle.restaurantregister.RestaurantRegister;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.MediaLoader;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRegisterFragment2 extends Fragment {

    @BindView(R.id.RestaurantRegister2_CategorySp)
    MultiSelectSpinner RestaurantRegister2CategorySp;
    @BindView(R.id.RestaurantRegister2_CategotySpLayout)
    RelativeLayout RestaurantRegister2CategotySpLayout;
    @BindView(R.id.RestaurantRegister2_MinimumRequestEt)
    TextInputLayout RestaurantRegister2MinimumRequestEt;
    @BindView(R.id.RestaurantRegister2_DeliveryFeeEt)
    TextInputLayout RestaurantRegister2DeliveryFeeEt;
    @BindView(R.id.RestaurantRegister2_CommunicationCb)
    CheckBox RestaurantRegister2CommunicationCb;
    @BindView(R.id.RestaurantRegister2_PhoneEt)
    TextInputLayout RestaurantRegister2PhoneEt;
    @BindView(R.id.RestaurantRegister2_WhatsappEt)
    TextInputLayout RestaurantRegister2WhatsappEt;
    @BindView(R.id.RestaurantRegister2_InputRootLayout)
    LinearLayout RestaurantRegister2InputRootLayout;
    @BindView(R.id.RestaurantRegister2_StorePicTv)
    TextView RestaurantRegister2StorePicTv;
    @BindView(R.id.RestaurantRegister2_RegisterBtn)
    Button RestaurantRegister2RegisterBtn;
    List<String> CategoriesList = new ArrayList<>();
   ArrayList<String> CategoriesId=new ArrayList<>();
    ArrayAdapter<String> CategoriesArrayAdapter;
    ArrayList<AlbumFile> imagesfiles = new ArrayList<>();
    ProgressDialog progressDialog;

    ApiService apiService;
    Bundle bundle;
    String MinRequest, DeleiveryCommision, PhoneNumber, Whatsapp;
    Unbinder unbinder;
    @BindView(R.id.RestaurantRegister2_StorePicIv)
    ImageView RestaurantRegister2StorePicIv;
    @BindView(R.id.RestaurantRegister2_StorePicIv_Layout)
    RelativeLayout RestaurantRegister2StorePicIvLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_restaurant_register2, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        bundle= getArguments();
        Toast.makeText(getContext(),bundle.getString("RestaurantName"),Toast.LENGTH_LONG).show();
        HelperMethod.SetToolBar(getString(R.string.new_account),true,getContext());



        GetCategories();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.RestaurantRegister2_CommunicationCb, R.id.RestaurantRegister2_StorePicIv_Layout,
            R.id.RestaurantRegister2_RegisterBtn,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.RestaurantRegister2_CommunicationCb:
                break;
            case R.id.RestaurantRegister2_StorePicIv_Layout:
                loadImage();

                break;
            case R.id.RestaurantRegister2_RegisterBtn:
                if(imagesfiles.isEmpty())
                {
                    Toast.makeText(getContext(),getString(R.string.please_select_img_txt),Toast.LENGTH_LONG).show();
                }
                else {
                    Registeration();
                }
                break;
        }
    }

    public void GetCategories() {
        CategoriesList.add(getString(R.string.select_categories));
        apiService.GetCategories().enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                try {
                    if (response.body().getStatus().equals(1)) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            CategoriesList.add(response.body().getData().get(i).getName());

                        }
                        HelperMethod.SpinnerAdapter(getContext(), CategoriesList, CategoriesArrayAdapter, RestaurantRegister2CategorySp);
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });
    }

    public void Registeration() {
        String PhoneNumber=RestaurantRegister2PhoneEt.getEditText().getText().toString();
        String whatsapp=RestaurantRegister2WhatsappEt.getEditText().getText().toString();
        final RestaurantRegisterFragment1 registerFragment1=new RestaurantRegisterFragment1();
        CategoriesId.add(String.valueOf(RestaurantRegister2CategorySp.getSelectedItemId()));
        String Name=bundle.getString("RestaurantName");
        String Email=bundle.getString("RestaurantEmail");
        final String Password=bundle.getString("RestaurantPassword");
        String RepeatPassword=bundle.getString("RestaurantRepeatPass");
        String regionid=bundle.getString("RestaurantRegionId");
        String City=bundle.getString("RestaurantCity");
        final String CategoryId= String.valueOf(RestaurantRegister2CategorySp.getSelectedItemId());
        String MinCharger=RestaurantRegister2MinimumRequestEt.getEditText().getText().toString();
        String DeliveryCost=RestaurantRegister2DeliveryFeeEt.getEditText().getText().toString();
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.register));

    apiService.RestaurantRegister(HelperMethod.convertToRequestBody(Name),HelperMethod.convertToRequestBody(Email)
            ,HelperMethod.convertToRequestBody(Password),HelperMethod.convertToRequestBody(RepeatPassword)
    ,HelperMethod.convertToRequestBody(PhoneNumber),HelperMethod.convertToRequestBody(City)
    ,HelperMethod.convertToRequestBody(whatsapp),HelperMethod.convertToRequestBody(regionid)
     ,HelperMethod.convertToRequestBody(CategoryId),HelperMethod.convertToRequestBody("30")
       ,HelperMethod.convertToRequestBody(DeliveryCost),HelperMethod.convertToRequestBody(MinCharger)
     ,HelperMethod.convertFileToMultipart(imagesfiles.get(0).getPath(),"photo"),HelperMethod.convertToRequestBody("open")).enqueue(new Callback<RestaurantRegister>() {
        @Override
        public void onResponse(Call<RestaurantRegister> call, Response<RestaurantRegister> response) {
            progressDialog.dismiss();
            try {

                if(response.body().getStatus().equals(1))
                {
                    Toast.makeText(getContext(),"Registeration process done",Toast.LENGTH_LONG).show();
                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,Constant.Restaurant_User,getContext());
                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,response.body().getData().getApiToken(),getContext());
                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.RestaurantId,String.valueOf(response.body().getData().getData().getId()),getContext());
                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Restaurant_Email,response.body().getData().getData().getEmail(),getContext());
                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Rest_Name,response.body().getData().getData().getName(),getContext());
                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Restaurant_File,Constant.Restaurant_Password,Password,getContext());
                    RestaurantDetails details=new RestaurantDetails();
                    HelperMethod.replace(details,getFragmentManager(),R.id.SellNavigation_RootLayout);
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
        public void onFailure(Call<RestaurantRegister> call, Throwable t) {
            progressDialog.dismiss();
            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
        }
    });

    }

    public void loadImage() {

        Action<ArrayList<AlbumFile>> action = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                imagesfiles.clear();
                imagesfiles.addAll(result);
                Glide.with(getContext())
                        .load(result.get(0).getPath())
                        .placeholder(R.drawable.ic_person)
                        .into(RestaurantRegister2StorePicIv);




            }
        };

        HelperMethod.openAlbum(3, getActivity(), imagesfiles, action);

    }


}
