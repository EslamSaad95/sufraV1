package com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.RoomDB.MenuItemEntity;
import com.example.laptophome.sufra.data.local.RoomDB.RoomDao;
import com.example.laptophome.sufra.data.local.RoomDB.RoomManager;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.CartCycle.ItemsCart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class Restaurant_MenuItem_Details extends Fragment {


    @BindView(R.id.MenuItemFr_ItemPicIv)
    ImageView MenuItemFrItemPicIv;
    @BindView(R.id.MenuItemFr_ItemTitlteTv)
    TextView MenuItemFrItemTitlteTv;
    @BindView(R.id.MenuItemFr_ItemDetailsTv)
    TextView MenuItemFrItemDetailsTv;
    @BindView(R.id.MenuItemFr_PriceTxt)
    TextView MenuItemFrPriceTxt;
    @BindView(R.id.MenuItemFr_ItemPriceTv)
    TextView MenuItemFrItemPriceTv;
    @BindView(R.id.MenuItemFr_ItemPeriodTv)
    TextView MenuItemFrItemPeriodTv;
    @BindView(R.id.MenuItemFr_SpecialOrderEt)
    EditText MenuItemFrSpecialOrderEt;
    @BindView(R.id.MenuItemFr_DecreaseQuant_IV)
    ImageView MenuItemFrDecreaseQuantIV;
    @BindView(R.id.MenuItemFr_EnterQuan_Et)
    EditText MenuItemFrEnterQuanEt;
    @BindView(R.id.MenuItemFr_IncreaseQuant_IV)
    ImageView MenuItemFrIncreaseQuantIV;
    @BindView(R.id.MenuItemFr_AddtoCartBtn)
    Button MenuItemFrAddtoCartBtn;
    Bundle b;
    List<MenuItemEntity> menuItemEntityList=new ArrayList<>();
    int ItemQuantity=1;
    RoomDao roomDao;
    int ItemId;
    String ItemTitle,ItemDetails,ItemPicPath,ItemTotalPrice;
    int ItemPrice;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__menu_item__details, container, false);
        unbinder = ButterKnife.bind(this, view);
        roomDao=RoomManager.getinstance(getContext()).roomDao();

        b=getArguments();
        ItemTitle=b.getString("ItemName");
        ItemDetails=b.getString("ItemDetails");
        ItemPicPath=b.getString("ItemPic");
        MenuItemFrItemTitlteTv.setText(ItemTitle);
        MenuItemFrItemPeriodTv.setText("Order Preparation Time :  "+b.getString("ItemPreparingTime"));
        ItemId=Integer.parseInt(b.getString("ItemId"));
        HelperMethod.SetToolBar(ItemTitle,false,getContext());
        MenuItemFrItemDetailsTv.setText(ItemDetails);
        Glide.with(getContext()).load(ItemPicPath).into(MenuItemFrItemPicIv);
        MenuItemFrEnterQuanEt.setText(String.valueOf(ItemQuantity));
       ItemQuantity= Integer.parseInt(MenuItemFrEnterQuanEt.getText().toString());
        ItemPrice=(int)Double.parseDouble(b.getString("ItemPrice"));
        ItemTotalPrice=String.valueOf((int)Double.parseDouble((b.getString("ItemPrice")))*ItemQuantity);
        MenuItemFrItemPriceTv.setText(ItemPrice+"Saudi Real");

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.MenuItemFr_DecreaseQuant_IV, R.id.MenuItemFr_IncreaseQuant_IV, R.id.MenuItemFr_AddtoCartBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.MenuItemFr_DecreaseQuant_IV:
                ItemQuantity=ItemQuantity-1;
                MenuItemFrEnterQuanEt.setText(String.valueOf(ItemQuantity));
                break;
            case R.id.MenuItemFr_IncreaseQuant_IV:
                ItemQuantity=ItemQuantity+1;
                MenuItemFrEnterQuanEt.setText(String.valueOf(ItemQuantity));
                break;
            case R.id.MenuItemFr_AddtoCartBtn:
                ItemQuantity= Integer.parseInt(MenuItemFrEnterQuanEt.getText().toString());
                ItemTotalPrice=String.valueOf((int)Double.parseDouble((b.getString("ItemPrice")))*ItemQuantity);
                Toast.makeText(getContext(),ItemTotalPrice,Toast.LENGTH_LONG).show();
                addtoRoom();
                break;
        }
    }


    public void addtoRoom()
    {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                MenuItemEntity item=new MenuItemEntity(ItemId,ItemQuantity,ItemTitle,ItemPrice,ItemPicPath,Integer.parseInt(ItemTotalPrice),"");


            roomDao.Add(item);
                ItemsCart cart=new ItemsCart();
                HelperMethod.replace(cart,getFragmentManager(),R.id.UserNavigation_RootLayout);
            }
        });

    }
}
