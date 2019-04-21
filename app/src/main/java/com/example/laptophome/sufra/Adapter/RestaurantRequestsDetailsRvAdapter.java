package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.model.restaurantrequests.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantRequestsDetailsRvAdapter extends RecyclerView.Adapter<RestaurantRequestsDetailsRvAdapter.ViewHolder> {

    private Context context;
    private List<Item> RestaurantRequestsDetailsList;

    public RestaurantRequestsDetailsRvAdapter(Context context, List<Item> restaurantRequestsDetailsList) {
        this.context = context;
        RestaurantRequestsDetailsList = restaurantRequestsDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requestsdetails_recycleview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        SetData(viewHolder, i);
        SetAction(viewHolder, i);
    }

    private void SetData(ViewHolder viewHolder, int i) {
        viewHolder.itemRequestsDetailsRvItemNameTV.setText(RestaurantRequestsDetailsList.get(i).getName());
        Glide.with(context).load(RestaurantRequestsDetailsList.get(i).getPhotoUrl()).into(viewHolder.itemRequestsDetailsRvIV);
        viewHolder.itemRequestsDetailsRvQuantityTv.setText(RestaurantRequestsDetailsList.get(i).getPivot().getQuantity());
        if (RestaurantRequestsDetailsList.get(i).getPivot().getNote().isEmpty()) {
            viewHolder.itemRequestsDetailsRvNotesTv.setText("No Notes");
        } else {
            viewHolder.itemRequestsDetailsRvNotesTv.setText(RestaurantRequestsDetailsList.get(i).getPivot().getNote());
        }
    }

    private void SetAction(ViewHolder viewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return RestaurantRequestsDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.item_RequestsDetailsRv_IV)
        ImageView itemRequestsDetailsRvIV;
        @BindView(R.id.item_RequestsDetailsRv_ItemNameTV)
        TextView itemRequestsDetailsRvItemNameTV;
        @BindView(R.id.item_RequestsDetailsRv_QuantityTv)
        TextView itemRequestsDetailsRvQuantityTv;
        @BindView(R.id.item_RequestsDetailsRv_NotesTv)
        TextView itemRequestsDetailsRvNotesTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
