package com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantCycle;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.helper.HelperWebServicesMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class RestaurantRegisterFragment1 extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.RestaurantRegister_StoreNameEt)
    TextInputLayout RestaurantRegisterStoreNameEt;
    @BindView(R.id.RestaurantRegisterFr_CitySp)
    Spinner RestaurantRegisterFrCitySp;
    @BindView(R.id.RestaurantRegister_CitySp_Layout)
    RelativeLayout RestaurantRegisterCitySpLayout;
    @BindView(R.id.RestaurantRegisterFr_RegionSp)
    Spinner RestaurantRegisterFrRegionSp;
    @BindView(R.id.RestaurantRegister_RegionSp_Layout)
    RelativeLayout RestaurantRegisterRegionSpLayout;
    @BindView(R.id.RestaurantRegister_EmailEt)
    TextInputLayout RestaurantRegisterEmailEt;
    @BindView(R.id.RestaurantRegister_PasswordEt)
    TextInputLayout RestaurantRegisterPasswordEt;
    @BindView(R.id.RestaurantRegister_RepeatPasswordEt)
    TextInputLayout RestaurantRegisterRepeatPasswordEt;
    @BindView(R.id.RestaurantRegisterFr_InputRootLayout)
    LinearLayout RestaurantRegisterFrInputRootLayout;
    @BindView(R.id.RestaurantRegister_ContinureBtn)
    Button RestaurantRegisterContinureBtn;
    ApiService apiService;
    String Name,Email,Password,RepeatPassword,City;
    int Cityid,Regionid;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_register1, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperWebServicesMethods.GetCities(getContext(),RestaurantRegisterFrCitySp);
        HelperWebServicesMethods.GetRegions(getContext(),RestaurantRegisterFrRegionSp,RestaurantRegisterFrCitySp);
        HelperMethod.SetToolBar(getString(R.string.new_account),true,getContext());

        bundle=new Bundle();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick(R.id.RestaurantRegister_ContinureBtn)
    public void onViewClicked() {

         TextInputLayout[]Textinputarray={RestaurantRegisterStoreNameEt,RestaurantRegisterEmailEt,
                RestaurantRegisterPasswordEt,RestaurantRegisterRepeatPasswordEt};
         for(int i=0;i<Textinputarray.length;i++) {
             if (!HelperMethod.CheckTextInputEmpty(Textinputarray[i])) {
                                    return;
             }
         }

         if(RestaurantRegisterFrCitySp.getSelectedItem().equals(getString(R.string.select_city)))
         {
             Toast.makeText(getContext(),getString(R.string.please_select_city),Toast.LENGTH_LONG).show();
             return;
         }
         if(RestaurantRegisterFrRegionSp.getSelectedItem().equals(getString(R.string.select_region)))
         {
             Toast.makeText(getContext(),getString(R.string.please_select_region),Toast.LENGTH_LONG).show();
             return;
         }
        if (!Patterns.EMAIL_ADDRESS.matcher(RestaurantRegisterEmailEt.getEditText().getText().toString()).matches()) {
          RestaurantRegisterEmailEt.getEditText().setError(getString(R.string.please_enter_valid_email));
            RestaurantRegisterEmailEt.getEditText().requestFocus();
            return;
        }
        if(!RestaurantRegisterRepeatPasswordEt.getEditText().getText().toString().equals(RestaurantRegisterPasswordEt.getEditText().getText().toString()))
        {   RestaurantRegisterRepeatPasswordEt.getEditText().setError("password not match");
            RestaurantRegisterRepeatPasswordEt.getEditText().requestFocus();
            return;
        }

         bundle.putString("RestaurantName",RestaurantRegisterStoreNameEt.getEditText().getText().toString());
         bundle.putString("RestaurantEmail",RestaurantRegisterEmailEt.getEditText().getText().toString());
         bundle.putString("RestaurantPassword",RestaurantRegisterPasswordEt.getEditText().getText().toString());
         bundle.putString("RestaurantRepeatPass",RestaurantRegisterRepeatPasswordEt.getEditText().getText().toString());
         bundle.putString("RestaurantCity",RestaurantRegisterFrCitySp.getSelectedItem().toString());
         bundle.putString("RestaurantRegionId", String.valueOf(RestaurantRegisterFrRegionSp.getSelectedItemId()));
         RestaurantRegisterFragment2 registerFragment2=new RestaurantRegisterFragment2();
         registerFragment2.setArguments(bundle);
         HelperMethod.replace(registerFragment2,getFragmentManager(),R.id.SellNavigation_RootLayout);
    }

    @Override
    public void onPause() {
        super.onPause();
        HelperWebServicesMethods.clearcity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
