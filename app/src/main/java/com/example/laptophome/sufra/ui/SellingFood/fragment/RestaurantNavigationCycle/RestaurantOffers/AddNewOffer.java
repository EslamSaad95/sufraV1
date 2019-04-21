package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers;


import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.offercycle.addoffer.AddOffer;
import com.example.laptophome.sufra.data.model.offercycle.offerupdate.OfferUpdate;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers.RestaurantOffers;
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


public class AddNewOffer extends Fragment {


    @BindView(R.id.AddNewOfferFr_OfferNameTv)
    TextInputLayout AddNewOfferFrOfferNameTv;
    @BindView(R.id.AddNewOfferFr_OfferDescrFr)
    TextInputLayout AddNewOfferFrOfferDescrFr;
    @BindView(R.id.AddNewOfferFr_PriceTv)
    TextInputLayout AddNewOfferFrPriceTv;
    @BindView(R.id.AddNewOfferFr_OffeDateFromTv)
    TextView AddNewOfferFrOffeDateFromTv;
    @BindView(R.id.AddNewOfferFr_OffeDateToTv)
    TextView AddNewOfferFrOffeDateToTv;
    @BindView(R.id.AddNewOfferFr_OfferPicIv)
    ImageView AddNewOfferFrOfferPicIv;
    @BindView(R.id.AddNewOfferFr_AddOfferBtn)
    Button AddNewOfferFrAddOfferBtn;
    ArrayList<AlbumFile> imagesfiles = new ArrayList<>();
    DatePickerDialog datePickerDialog;
    ProgressDialog progressDialog;
    ApiService apiService;
    String ApiToken;
    int Offer_ID;
    Bundle b;
    boolean isupdated=false;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_offer, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.SetToolBar(getString(R.string.add_offer),true,getContext());
        b=getArguments();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void AddNewOffer() {
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.add_newoffer_loading));

        ApiToken = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, getContext());
        apiService.AddNewOffer(
                HelperMethod.convertToRequestBody(AddNewOfferFrOfferDescrFr.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewOfferFrPriceTv.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewOfferFrOffeDateFromTv.getText().toString()),
                HelperMethod.convertToRequestBody(AddNewOfferFrOfferNameTv.getEditText().getText().toString()),
                HelperMethod.convertFileToMultipart(imagesfiles.get(0).getPath(),"photo"),
                HelperMethod.convertToRequestBody(AddNewOfferFrOffeDateToTv.getText().toString()),
                HelperMethod.convertToRequestBody(ApiToken)).enqueue(new Callback<AddOffer>() {
            @Override
            public void onResponse(Call<AddOffer> call, Response<AddOffer> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        Toast.makeText(getContext(),"Done",Toast.LENGTH_LONG).show();
                        RestaurantOffers offers=new RestaurantOffers();
                        HelperMethod.replace(offers,getFragmentManager(),R.id.SellNavigation_RootLayout);
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
            public void onFailure(Call<AddOffer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @OnClick({R.id.AddNewOfferFr_OffeDateFromTv, R.id.AddNewOfferFr_OffeDateToTv, R.id.AddNewOfferFr_OfferPicIv, R.id.AddNewOfferFr_AddOfferBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.AddNewOfferFr_OffeDateFromTv:
                HelperMethod.setdatepicker(datePickerDialog,AddNewOfferFrOffeDateFromTv,getContext());
                break;
            case R.id.AddNewOfferFr_OffeDateToTv:
                HelperMethod.setdatepicker(datePickerDialog,AddNewOfferFrOffeDateToTv,getContext());
                break;
            case R.id.AddNewOfferFr_OfferPicIv:
                LoadImage();
                break;
            case R.id.AddNewOfferFr_AddOfferBtn:
                if(imagesfiles.isEmpty())
                {
                    Toast.makeText(getContext(),getString(R.string.select_pic_for_offer),Toast.LENGTH_LONG).show();
                }

             else
                {
                    AddNewOffer();
                }

                break;
        }
    }

    public void LoadImage()
    {
        Action<ArrayList<AlbumFile>> action = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                imagesfiles.clear();
                imagesfiles.addAll(result);
                Glide.with(getContext())
                        .load(result.get(0).getPath())
                        .placeholder(R.drawable.ic_person)
                        .into(AddNewOfferFrOfferPicIv);
            }
        };

        HelperMethod.openAlbum(1, getActivity(), imagesfiles, action);


    }



}
