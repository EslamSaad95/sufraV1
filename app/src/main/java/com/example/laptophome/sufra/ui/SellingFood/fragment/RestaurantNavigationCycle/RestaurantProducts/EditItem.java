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
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditItem extends Fragment {
    ApiService apiService;
    ArrayList<AlbumFile> imagesfiles;
    String ImagePath,ApiToken;
    ProgressDialog progressDialog;
    int ItemId;
    Bundle b;
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
    Button UpdateItemFrAddBtn;
    Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        UpdateItemFrAddBtn.setText(getString(R.string.updatebtn));
        HelperMethod.SetToolBar(getString(R.string.edit_menuitem), true, getContext());
        apiService = RetrofitClient.getclient().create(ApiService.class);
        imagesfiles=new ArrayList<>();
        b=getArguments();
       try {

           if(!b.isEmpty())
           {
               GetProductInfo();
           }
       }
       catch (NullPointerException ee)
       {}
        try {
            ImagePath=imagesfiles.get(0).getPath();
        }
        catch (Exception ee)
        {}

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
                UpdateProductInfo();
                break;
        }
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

    public void GetProductInfo()
    {
        AddNewItemFrProductNameTv.getEditText().setText(b.getString("ProductName"));
        AddNewItemFrProductPriceTv.getEditText().setText(b.getString("ProductPrice"));
        AddNewItemFrPreParationTimeTv.getEditText().setText(b.getString("ProductPreparaing"));
        AddNewItemFrProductDetailsTv.getEditText().setText(b.getString("ProductDescr"));
        ItemId=Integer.parseInt(b.getString("productid"));
        Glide.with(getContext()).load(b.getString("ItemPic")).asBitmap().into(AddNewItemFrProductPicIv);

    }

    public void UpdateProductInfo()
    {progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        ApiToken=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getContext());
        apiService.UpdateRestaurantProduct(
                HelperMethod.convertToRequestBody(AddNewItemFrProductDetailsTv.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewItemFrProductPriceTv.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewItemFrPreParationTimeTv.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewItemFrProductNameTv.getEditText().getText().toString()),
               HelperMethod.CheckPicture(ImagePath,"photo")
                ,
                HelperMethod.convertToRequestBody(String.valueOf(ItemId)),HelperMethod.convertToRequestBody(ApiToken)).enqueue(new Callback<AddRestaurantProduct>() {
            @Override
            public void onResponse(Call<AddRestaurantProduct> call, Response<AddRestaurantProduct> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        Toast.makeText(getContext(),"Done",Toast.LENGTH_LONG).show();
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
}
