package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantProducts;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.addrestaurantproduct.AddRestaurantProduct;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantDetails;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddNewItem extends Fragment {


    @BindView(R.id.AddNewItemFr_productNameTv)
    TextInputLayout AddNewItemFrProductNameTv;
    @BindView(R.id.AddNewItemFr_productDetailsTv)
    TextInputLayout AddNewItemFrProductDetailsTv;
    @BindView(R.id.AddNewItemFr_productPriceTv)
    TextInputLayout AddNewItemFrProductPriceTv;
    @BindView(R.id.AddNewItemFr_PreParationTimeTv)
    TextInputLayout AddNewItemFrPreParationTimeTv;
    @BindView(R.id.AddNewItemFr_ProductPicIv)
    ImageView AddNewItemFrProductPicIv;
    @BindView(R.id.AddNewItemFr_AddBtn)
    Button AddNewItemFrAddBtn;
    ApiService apiService;
    String ProductName,ProductDescr,Productprice,ProductPreparationTime,ApiToken;
    ArrayList<AlbumFile>imagesfiles=new ArrayList<>();
    Bundle b;
    int ItemId;
    ProgressDialog progressDialog;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService=RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.addnewitem),true,getContext());


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.AddNewItemFr_ProductPicIv, R.id.AddNewItemFr_AddBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.AddNewItemFr_ProductPicIv:
                loadImage();
                break;
            case R.id.AddNewItemFr_AddBtn:
                if(imagesfiles.isEmpty())
                {
                    Toast.makeText(getContext(),getString(R.string.please_select_img_txt),Toast.LENGTH_LONG).show();
                    break;
                }
           else
                {
                    AddProduct();
                }
                break;
        }
    }
    public void AddProduct()
    {
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        ApiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getContext());
        ProductName=AddNewItemFrProductNameTv.getEditText().getText().toString();
        ProductDescr=AddNewItemFrProductDetailsTv.getEditText().getText().toString();
        Productprice=AddNewItemFrProductPriceTv.getEditText().getText().toString();
        ProductPreparationTime=AddNewItemFrPreParationTimeTv.getEditText().getText().toString();
        apiService.AddRestaurantProduct(HelperMethod.convertToRequestBody(ProductDescr),HelperMethod.convertToRequestBody(Productprice)
        ,HelperMethod.convertToRequestBody(ProductPreparationTime),HelperMethod.convertToRequestBody(ProductName),HelperMethod.convertFileToMultipart(imagesfiles.get(0).getPath(),"photo")
                , HelperMethod.convertToRequestBody(ApiToken)).enqueue(new Callback<AddRestaurantProduct>() {
            @Override
            public void onResponse(Call<AddRestaurantProduct> call, Response<AddRestaurantProduct> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {

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
            public void onFailure(Call<AddRestaurantProduct> call, Throwable t) {
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
                        .into(AddNewItemFrProductPicIv);
            }
        };

        HelperMethod.openAlbum(3, getActivity(), imagesfiles, action);

    }


}
