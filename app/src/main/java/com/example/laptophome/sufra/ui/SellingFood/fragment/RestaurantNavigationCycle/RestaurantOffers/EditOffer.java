package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantOffers;


import android.app.DatePickerDialog;
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
import com.example.laptophome.sufra.data.model.offercycle.offerupdate.OfferUpdate;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
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


public class EditOffer extends Fragment {


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
    ApiService apiService;
    ArrayList<AlbumFile> imagesfiles;
    Bundle b;
    String ApiToken,Imagepath;
    int Offer_ID;
    DatePickerDialog datePickerDialog;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_offer, container, false);
        unbinder = ButterKnife.bind(this, view);
        AddNewOfferFrAddOfferBtn.setText(getString(R.string.updatebtn));
        apiService = RetrofitClient.getclient().create(ApiService.class);
        imagesfiles = new ArrayList<>();
        b = getArguments();
        HelperMethod.SetToolBar(getString(R.string.update_offer), true, getContext());
        try {

            if (!b.isEmpty()) {
                GetOfferInfo();

            }
        } catch (NullPointerException ee) {
        }


      try {
        Imagepath=imagesfiles.get(0).getPath();
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





    public void UpdateOffer() {
        Offer_ID = Integer.parseInt(b.getString("OfferId"));
        ApiToken = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.Restaurant_Api_Token, getContext());
        apiService.UpdateOffer(HelperMethod.convertToRequestBody(AddNewOfferFrOfferDescrFr.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewOfferFrPriceTv.getEditText().getText().toString()),
                HelperMethod.convertToRequestBody(AddNewOfferFrOffeDateFromTv.getText().toString()),
                HelperMethod.convertToRequestBody(AddNewOfferFrOfferNameTv.getEditText().getText().toString()),
                HelperMethod.CheckPicture(Imagepath,"photo"),
                HelperMethod.convertToRequestBody(AddNewOfferFrOffeDateToTv.getText().toString()),
                HelperMethod.convertToRequestBody(String.valueOf(Offer_ID)), HelperMethod.convertToRequestBody(ApiToken)).enqueue(new Callback<OfferUpdate>() {
            @Override
            public void onResponse(Call<OfferUpdate> call, Response<OfferUpdate> response) {
                try {

                    if (response.body().getStatus().equals(1)) {
                        Toast.makeText(getContext(), getString(R.string.offer_updated), Toast.LENGTH_LONG).show();
                        RestaurantOffers restaurantOffers = new RestaurantOffers();
                        HelperMethod.replace(restaurantOffers, getFragmentManager(), R.id.SellNavigation_RootLayout);
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<OfferUpdate> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LoadImage() {
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

    public void GetOfferInfo() {
        AddNewOfferFrOfferNameTv.getEditText().setText(b.getString("OfferName"));
        AddNewOfferFrOfferDescrFr.getEditText().setText(b.getString("OfferDescr"));
        AddNewOfferFrPriceTv.getEditText().setText(b.getString("offerprice"));
        AddNewOfferFrOffeDateFromTv.setText(b.getString("OfferStartAt"));
        AddNewOfferFrOffeDateToTv.setText(b.getString("OfferEndAt"));
        Glide.with(getContext()).load(b.getString("ProductImage")).into(AddNewOfferFrOfferPicIv);

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
                UpdateOffer();
                break;
        }
    }
}
