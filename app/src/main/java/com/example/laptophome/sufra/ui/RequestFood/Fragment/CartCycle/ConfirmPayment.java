package com.example.laptophome.sufra.ui.RequestFood.Fragment.CartCycle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.RoomDB.MenuItemEntity;
import com.example.laptophome.sufra.data.local.RoomDB.RoomDao;
import com.example.laptophome.sufra.data.local.RoomDB.RoomManager;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.ordercycle.neworder.NewOrder;
import com.example.laptophome.sufra.data.model.paymentmethod.PaymentData;
import com.example.laptophome.sufra.data.model.paymentmethod.PaymentMethod;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.RestaurantList;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.yanzhenjie.album.mvp.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConfirmPayment extends BaseFragment {


    @BindView(R.id.ConfirmPaymentFr_NotesEt)
    EditText ConfirmPaymentFrNotesEt;
    @BindView(R.id.ConfirmPaymentFr_DeliveryAddressEt)
    EditText ConfirmPaymentFrDeliveryAddressEt;
    @BindView(R.id.ConfirmPaymentFr_CashRBtn)
    RadioButton ConfirmPaymentFrCashRBtn;
    @BindView(R.id.ConfirmPaymentFr_NetworkpaymentRbtn)
    RadioButton ConfirmPaymentFrNetworkpaymentRbtn;
    @BindView(R.id.ConfirmPaymentFr_TotalTv)
    TextView ConfirmPaymentFrTotalTv;
    @BindView(R.id.ConfirmPaymentFr_DeliveryTaxTv)
    TextView ConfirmPaymentFrDeliveryTaxTv;
    @BindView(R.id.ConfirmPaymentFr_TotalPriceTv)
    TextView ConfirmPaymentFrTotalPriceTv;
    @BindView(R.id.ConfirmPaymentFr_ConfirmPaymentBtn)
    Button ConfirmPaymentFrConfirmPaymentBtn;
    String ClientAddress, deliverycost, UserName, UserPhone, RestaurantId, UserApi, OrderAddress, OrderNote;
    int PaymentMethodId = 1;
    RoomDao roomDao;
    ApiService apiService;
    ArrayList<Integer> ItemIdList;
    ArrayList<Integer> QuantityList;
    ArrayList<String> NoteList;
    List<MenuItemEntity> menuItemEntities;
    List<PaymentData> PaymentList;
    ProgressDialog progressDialog;
    Unbinder unbinder;
    @BindView(R.id.ConfirmPaymentFr_PaymentRadioGroup)
    RadioGroup ConfirmPaymentFrPaymentRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_payment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ItemIdList = new ArrayList<>();
        QuantityList = new ArrayList<>();
        NoteList = new ArrayList<>();
        menuItemEntities = new ArrayList<>();
        PaymentList = new ArrayList<>();
        Bundle b = getArguments();
        roomDao = RoomManager.getinstance(getContext()).roomDao();
        apiService = RetrofitClient.getclient().create(ApiService.class);
        ((UserNavigationActivity) getActivity()).onBackPressedobject = null;
        HelperMethod.SetToolBar(getString(R.string.request_details), true, getContext());
        GetPayment();
        ClientAddress = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File, Constant.User_Address_Key, getContext());
        deliverycost = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File, Constant.Rest_DeliveryCost_Key, getContext());
        UserName = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File, Constant.User_Name_Key, getContext());
        UserPhone = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.User_File, Constant.User_Phone_Key, getContext());
        RestaurantId = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Restaurant_File, Constant.Rest_Id, getContext());
        UserApi = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName, Constant.User_Api_Token, getContext());
        final int TotalPrice = (int) Double.parseDouble(deliverycost) + (int) Double.parseDouble(b.getString("TotalPrice"));
        ConfirmPaymentFrTotalTv.setText(b.getString("TotalPrice"));
        ConfirmPaymentFrDeliveryTaxTv.setText(deliverycost);
        ConfirmPaymentFrDeliveryAddressEt.setHint(ClientAddress);
        ConfirmPaymentFrTotalPriceTv.setText(String.valueOf(TotalPrice));
        if (ConfirmPaymentFrDeliveryAddressEt.getText().toString().equals("")) {
            OrderAddress = ConfirmPaymentFrDeliveryAddressEt.getHint().toString();
        } else {
            OrderAddress = ConfirmPaymentFrDeliveryAddressEt.getText().toString();
        }

        ConfirmPaymentFrPaymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ConfirmPaymentFr_CashRBtn) {
                    PaymentMethodId = 1;
                } else if (checkedId == R.id.ConfirmPaymentFr_NetworkpaymentRbtn) {
                    PaymentMethodId = 2;
                }
            }
        });


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                menuItemEntities = roomDao.GetAll();
                for (int i = 0; i < menuItemEntities.size(); i++) {
                    ItemIdList.add(menuItemEntities.get(i).getItem_id());
                    QuantityList.add(menuItemEntities.get(i).getQuantity());
                    NoteList.add(menuItemEntities.get(i).getItemNote());
                }

            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ConfirmPaymentFr_CashRBtn, R.id.ConfirmPaymentFr_NetworkpaymentRbtn, R.id.ConfirmPaymentFr_ConfirmPaymentBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ConfirmPaymentFr_CashRBtn:
                break;
            case R.id.ConfirmPaymentFr_NetworkpaymentRbtn:
                break;
            case R.id.ConfirmPaymentFr_ConfirmPaymentBtn:
                if (ConfirmPaymentFrPaymentRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), getString(R.string.please_select_payment), Toast.LENGTH_LONG).show();
                } else {
                    MakeOrder();
                }
                break;
        }
    }

    public void MakeOrder() {
        progressDialog=HelperMethod.ProgressDialog(getContext(),getString(R.string.please_wait));
        apiService.addorder(Integer.parseInt(RestaurantId), ConfirmPaymentFrNotesEt.getText().toString(), OrderAddress, PaymentMethodId, UserPhone, UserName, UserApi,
                ItemIdList, QuantityList, NoteList).enqueue(new Callback<NewOrder>() {
            @Override
            public void onResponse(Call<NewOrder> call, Response<NewOrder> response) {
                progressDialog.dismiss();
                try {

                    if (response.body().getStatus().equals(1)) {
                       SharedPrerefrencesManager.clearsharedprefrences(Constant.Restaurant_File, getContext(), Constant.Rest_DeliveryCost_Key);
                        SharedPrerefrencesManager.clearsharedprefrences(Constant.Restaurant_File, getContext(), Constant.Rest_Id);
                        RestaurantList list = new RestaurantList();
                        HelperMethod.replace(list, getFragmentManager(), R.id.UserNavigation_RootLayout);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                roomDao.DeleteAll();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<NewOrder> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GetPayment() {
        final RadioButton[] radioaraay = {ConfirmPaymentFrCashRBtn, ConfirmPaymentFrNetworkpaymentRbtn};
        apiService.GetPaymentMethod().enqueue(new Callback<PaymentMethod>() {
            @Override
            public void onResponse(Call<PaymentMethod> call, Response<PaymentMethod> response) {
                try {

                    if (response.body().getStatus().equals(1)) {
                        PaymentList.addAll(response.body().getData());
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            radioaraay[i].setText(response.body().getData().get(i).getName());
                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onFailure(Call<PaymentMethod> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
