package com.example.laptophome.sufra.ui.RequestFood.Fragment.UserNavigationCycle.OffersCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferDetails extends Fragment {
    ApiService apiService;

    int OrderId;
    @BindView(R.id.OfferDetailsFr_Iv)
    ImageView OfferDetailsFrIv;
    @BindView(R.id.OffersDetails_OfferName)
    TextView OffersDetailsOfferName;
    @BindView(R.id.OffersDetails_RestaurantrName)
    TextView OffersDetailsRestaurantrName;
    @BindView(R.id.OffersDetails_Price)
    TextView OffersDetailsPrice;
    @BindView(R.id.OffersDetails_startat)
    TextView OffersDetailsStartat;
    @BindView(R.id.OffersDetails_Endat)
    TextView OffersDetailsEndat;
    @BindView(R.id.OffersDetails_description)
    TextView OffersDetailsDescription;
    ProgressDialog progressDialog;
    Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer_details, container, false);
        HelperMethod.SetToolBar(getString(R.string.offer_details), true, getContext());
        apiService = RetrofitClient.getclient().create(ApiService.class);
        Bundle b = getArguments();
        OrderId = Integer.parseInt(b.getString("OfferId"));
        unbinder = ButterKnife.bind(this, view);
        GetOrderDetails();
        return view;
    }

    public void GetOrderDetails() {
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        apiService.GetOfferDetails(OrderId).enqueue(new Callback<com.example.laptophome.sufra.data.model.offercycle.offerdetails.OfferDetails>() {
            @Override
            public void onResponse(Call<com.example.laptophome.sufra.data.model.offercycle.offerdetails.OfferDetails> call, Response<com.example.laptophome.sufra.data.model.offercycle.offerdetails.OfferDetails> response) {
                progressDialog.dismiss();
                try {

                    if (response.body().getStatus().equals(1)) {

                        Glide.with(getContext()).load(response.body().getData().getPhotoUrl()).into(OfferDetailsFrIv);
                        OffersDetailsOfferName.setText(response.body().getData().getName());
                        OffersDetailsPrice.setText(response.body().getData().getPrice());
                        OffersDetailsDescription.setText(response.body().getData().getDescription());
                        OffersDetailsRestaurantrName.setText(response.body().getData().getRestaurant().getName());
                        OffersDetailsStartat.setText(HelperMethod.ConvertDatetoMonthFormat(response.body().getData().getStartingAt()));
                        OffersDetailsEndat.setText(HelperMethod.ConvertDatetoMonthFormat(response.body().getData().getEndingAt()));
                    } else {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<com.example.laptophome.sufra.data.model.offercycle.offerdetails.OfferDetails> call, Throwable t) {
               progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
