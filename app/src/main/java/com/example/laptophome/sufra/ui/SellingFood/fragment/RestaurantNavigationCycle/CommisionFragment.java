package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.commision.Commision;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommisionFragment extends Fragment {


    @BindView(R.id.CommisionFr_RestaurantSalesTv)
    TextView CommisionFrRestaurantSalesTv;
    @BindView(R.id.CommisionFr_AppCommisionTv)
    TextView CommisionFrAppCommisionTv;
    @BindView(R.id.CommisionFr_paidTv)
    TextView CommisionFrPaidTv;
    @BindView(R.id.CommisionFr_ResidualTv)
    TextView CommisionFrResidualTv;
    ProgressDialog progressDialog;
    ApiService apiService;
    String Api_Token;


    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commision, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        apiService=RetrofitClient.getclient().create(ApiService.class);
        GetRestaurantCommision();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void GetRestaurantCommision()
    {Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,getContext());
        apiService.GetRestaurantCommision(Api_Token).enqueue(new Callback<Commision>() {
            @Override
            public void onResponse(Call<Commision> call, Response<Commision> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        CommisionFrRestaurantSalesTv.setText(response.body().getData().getTotal());
                        CommisionFrPaidTv.setText(response.body().getData().getPayments());
                        CommisionFrAppCommisionTv.setText(response.body().getData().getNetCommissions());
                        CommisionFrResidualTv.setText(response.body().getData().getNetCommissions());

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
            public void onFailure(Call<Commision> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
