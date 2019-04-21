package com.example.laptophome.sufra.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.RoomDB.MenuItemEntity;
import com.example.laptophome.sufra.data.local.RoomDB.RoomDao;
import com.example.laptophome.sufra.data.local.RoomDB.RoomManager;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartRvAdapter extends RecyclerView.Adapter<CartRvAdapter.Viewholder> {

    private Context context;
    private List<MenuItemEntity> itemList;
    int Totalprice;
    TextView TotalBill;
    RoomDao roomDao;

    public CartRvAdapter(Context context, List<MenuItemEntity> itemList,TextView txt) {
        this.context = context;
        this.itemList = itemList;
        this.TotalBill =txt;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_recycleview, viewGroup, false);
        roomDao=RoomManager.getinstance(context).roomDao();
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        SetAction(viewholder, i);
        SetData(viewholder, i);
    }

    private void SetData(Viewholder viewholder, int i) {
        viewholder.ItemCartRvItemTitleTv.setText(itemList.get(i).getTitle());
        viewholder.ItemCartRvItemPriceTv.setText(String.valueOf(itemList.get(i).getPrice()));
        viewholder.ItemCartRvEnterQuanEt.setText(String.valueOf(itemList.get(i).getQuantity()));
        Glide.with(context).load(itemList.get(i).getImagePath()).into(viewholder.ItemCartRvItemPicIv);
      Totalprice=itemList.get(i).getQuantity()*itemList.get(i).getPrice();
        itemList.get(i).setTotalprice(Totalprice);
        viewholder.ItemCartRvTotalPriceTv.setText(context.getString(R.string.price_txt)+String.valueOf(Totalprice));
        GetTotalBill();





    }

    private void GetTotalBill()
    { int total=0;
        for(int i=0;i<itemList.size();i++)
        {
            int key = itemList.get(i).getTotalprice();
            total += key;
            TotalBill.setText(String.valueOf(total));

        }



    }
    public boolean CheckZeroQuantity(final int position)
    {
        for(int i=0;i<itemList.size();i++)
        {
            if(itemList.get(i).getQuantity()==0)
            {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.do_you_want_to_delete))
                        .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                itemList.remove(position);
                                notifyItemRemoved(position);



                            }
                        });
                final AlertDialog  alert = builder.create();
                alert.show();
                return  true;
            }

        }
        return false;
    }

    private void SetAction(final Viewholder viewholder, final int i) {
        //increase quantity btn
        viewholder.ItemCartRvIncreaseQuantIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.get(i).setQuantity(itemList.get(i).getQuantity()+1);
                viewholder.ItemCartRvEnterQuanEt.setText(String.valueOf(itemList.get(i).getQuantity()));
                 Totalprice =(int)Double.parseDouble(viewholder.ItemCartRvEnterQuanEt.getText().toString())*(int)Double.parseDouble(viewholder.ItemCartRvItemPriceTv.getText().toString());
                itemList.get(i).setTotalprice(Totalprice);
                GetTotalBill();
                viewholder.ItemCartRvTotalPriceTv.setText(context.getString(R.string.price_txt)+String.valueOf(Totalprice));
            }
        });

        //decrease quantity Btn
        viewholder.ItemCartRvDecreaseQuantIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.get(i).setQuantity(itemList.get(i).getQuantity()-1);
                if(itemList.get(i).getQuantity()<0)
                {
                    itemList.get(i).setQuantity(0);
                }
                if(CheckZeroQuantity(i)==true)
                {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDao.Delete(itemList.get(i));
                        }
                    });
                }
                viewholder.ItemCartRvEnterQuanEt.setText(String.valueOf(itemList.get(i).getQuantity()));
                 Totalprice =(int)Double.parseDouble(viewholder.ItemCartRvEnterQuanEt.getText().toString())*(int)Double.parseDouble(viewholder.ItemCartRvItemPriceTv.getText().toString());
                itemList.get(i).setTotalprice(Totalprice);
                GetTotalBill();
                viewholder.ItemCartRvTotalPriceTv.setText(context.getString(R.string.price_txt)+String.valueOf(Totalprice));
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        roomDao.Update(itemList.get(i));
                    }
                });
            }
        });




    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_CartRv_ItemPicIv)
        ImageView ItemCartRvItemPicIv;
        @BindView(R.id.Item_CartRv_ItemTitleTv)
        TextView ItemCartRvItemTitleTv;
        @BindView(R.id.Item_CartRv_DecreaseQuant_IV)
        ImageView ItemCartRvDecreaseQuantIV;
        @BindView(R.id.Item_CartRv_EnterQuan_Et)
        EditText ItemCartRvEnterQuanEt;
        @BindView(R.id.Item_CartRv_IncreaseQuant_IV)
        ImageView ItemCartRvIncreaseQuantIV;
        @BindView(R.id.Item_CartRv_ItemPriceTv)
        TextView ItemCartRvItemPriceTv;
        @BindView(R.id.Item_CartRv_TotalPriceTv)
        TextView ItemCartRvTotalPriceTv;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
