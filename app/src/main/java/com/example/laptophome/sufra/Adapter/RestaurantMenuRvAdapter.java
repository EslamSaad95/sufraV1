package com.example.laptophome.sufra.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.data.model.restaurantdetails.deleteitem.DeleteItem;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenuData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.Constant;
import com.example.laptophome.sufra.helper.HelperMethod;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.Restaurant_MenuItem_Details;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantProducts.AddNewItem;
import com.example.laptophome.sufra.ui.SellingFood.fragment.RestaurantNavigationCycle.RestaurantProducts.EditItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantMenuRvAdapter extends RecyclerView.Adapter<RestaurantMenuRvAdapter.Viewholder> {



    private Context context;
    private ApiService apiService;
    private List<RestaurantMenuData> MenuList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    String Api_Token;
    ProgressDialog progressDialog;

    public RestaurantMenuRvAdapter(Context context, List<RestaurantMenuData> menuList) {
        this.context = context;
        MenuList = menuList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantmenu_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        SetData(viewholder, i);
        SetAction(viewholder, i);

    }

    private void SetData(Viewholder viewholder, int i) {
        viewBinderHelper.bind(viewholder.ItemRestaurantMenuRvSwipLayout, String.valueOf(MenuList.get(i).getId()));
        viewholder.ItemRestaurantMenuRvItemTitleIv.setText(MenuList.get(i).getName());
        viewholder.ItemRestaurantMenuRvItemPriceIv.setText(MenuList.get(i).getPrice());
        viewholder.ItemRestaurantMenuRvItemDetailsIv.setText(MenuList.get(i).getDescription());
      //  Glide.with(context).load(MenuList.get(i).getPhotoUrl()).into(viewholder.ItemRestaurantMenuRvItemIv);

        //secondary Layout
        viewholder.ItemRestaurantMenuRvSwipItemPriceTv.setText(MenuList.get(i).getPrice());
        viewholder.ItemRestaurantListRvSwipItemTitleTv.setText(MenuList.get(i).getName());
        viewholder.ItemRestaurantMenuRvSwipItemDescriptionTv.setText(MenuList.get(i).getDescription());

        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,context).equals(Constant.Client_User))
        {
            viewBinderHelper.lockSwipe(String.valueOf(MenuList.get(i).getId()));
        }


    }


    private void SetAction(final Viewholder viewholder, final int i) {
        Api_Token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.Token_FileName,Constant.Restaurant_Api_Token,context);
        viewholder.ItemRestaurantMenuRvRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,context).equals(Constant.Client_User)) {
                    UserNavigationActivity activityinstance = (UserNavigationActivity) context;
                    Restaurant_MenuItem_Details restaurant_menuItem_details = new Restaurant_MenuItem_Details();
                    Bundle b = new Bundle();
                    b.putString("ItemName", MenuList.get(i).getName());
                    b.putString("ItemPrice", MenuList.get(i).getPrice());
                    b.putString("ItemPreparingTime", MenuList.get(i).getPreparingTime());
                    b.putString("ItemDetails", MenuList.get(i).getDescription());
                    b.putString("ItemPic", MenuList.get(i).getPhotoUrl());
                    b.putString("ItemId", String.valueOf(MenuList.get(i).getId()));
                    restaurant_menuItem_details.setArguments(b);
                    HelperMethod.replace(restaurant_menuItem_details, activityinstance.getSupportFragmentManager(), R.id.UserNavigation_RootLayout);
                }
                }
        });


        viewholder.ItemRestaurantMenuRvSwipDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=HelperMethod.ProgressDialog(context,context.getString(R.string.deleting_item));
                apiService.DeleteRestaurantProduct(Api_Token,MenuList.get(i).getId()).enqueue(new Callback<DeleteItem>() {
                    @Override
                    public void onResponse(Call<DeleteItem> call, Response<DeleteItem> response) {

                        try {

                            if(response.body().getStatus().equals(1))
                            {progressDialog.dismiss();
                                MenuList.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i,MenuList.size());
                                notifyDataSetChanged();
                                Toast.makeText(context,context.getString(R.string.deleting_item),Toast.LENGTH_LONG).show();

                            }
                            else
                            {   progressDialog.dismiss();
                                Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception ee)
                        {}
                    }

                    @Override
                    public void onFailure(Call<DeleteItem> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        viewholder.ItemRestaurantMenuRvSwipEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantNavigationActivity activityinstance = (RestaurantNavigationActivity) context;
                EditItem editItem=new EditItem();
                Bundle b=new Bundle();
                        b.putString("ProductName",MenuList.get(i).getName());
                        b.putString("ProductPrice",MenuList.get(i).getPrice());
                        b.putString("ProductDescr",MenuList.get(i).getDescription());
                        b.putString("ProductImage",MenuList.get(i).getPhotoUrl());
                        b.putString("ProductPreparaing",MenuList.get(i).getPreparingTime());
                        b.putString("productid",String.valueOf(MenuList.get(i).getId()));
                        editItem.setArguments(b);
                        HelperMethod.replace(editItem,activityinstance.getSupportFragmentManager(),R.id.SellNavigation_RootLayout);
            }
        });
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    @Override
    public int getItemCount() {
        return MenuList.size();
    }

    @OnClick({R.id.Item_RestaurantMenuRv_Swip_EditBtn, R.id.Item_RestaurantMenuRv_Swip_DeleteBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Item_RestaurantMenuRv_Swip_EditBtn:
                break;
            case R.id.Item_RestaurantMenuRv_Swip_DeleteBtn:
                break;
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_RestaurantMenuRv_ItemIv)
        ImageView ItemRestaurantMenuRvItemIv;
        @BindView(R.id.Item_RestaurantMenuRv_ItemTitleIv)
        TextView ItemRestaurantMenuRvItemTitleIv;
        @BindView(R.id.Item_RestaurantMenuRv_ItemDetailsIv)
        TextView ItemRestaurantMenuRvItemDetailsIv;
        @BindView(R.id.Item_RestaurantMenuRv_ItemPriceIv)
        TextView ItemRestaurantMenuRvItemPriceIv;
        @BindView(R.id.Item_RestaurantMenuRv_RootLayout)
        LinearLayout ItemRestaurantMenuRvRootLayout;

        @BindView(R.id.Item_RestaurantListRv_Swip_ItemTitleTv)
        TextView ItemRestaurantListRvSwipItemTitleTv;
        @BindView(R.id.Item_RestaurantMenuRv_Swip_ItemDescriptionTv)
        TextView ItemRestaurantMenuRvSwipItemDescriptionTv;
        @BindView(R.id.Item_RestaurantMenuRv_Swip_ItemPriceTv)
        TextView ItemRestaurantMenuRvSwipItemPriceTv;
        @BindView(R.id.Item_RestaurantMenuRv_Swip_EditBtn)
        Button ItemRestaurantMenuRvSwipEditBtn;
        @BindView(R.id.Item_RestaurantMenuRv_Swip_DeleteBtn)
        Button ItemRestaurantMenuRvSwipDeleteBtn;
        @BindView(R.id.Item_RestaurantMenuRv_SecondaryLayout)
        FrameLayout ItemRestaurantMenuRvSecondaryLayout;
        @BindView(R.id.Item_RestaurantMenuRv_MainLayout)
        FrameLayout ItemRestaurantMenuRvMainLayout;
        @BindView(R.id.Item_RestaurantMenuRv_SwipLayout)
        SwipeRevealLayout ItemRestaurantMenuRvSwipLayout;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);

        }
    }
}
