package com.example.laptophome.sufra.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.model.notificationlist.NotificationListData;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;
import com.example.laptophome.sufra.helper.HelperMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationListRvAdapter extends RecyclerView.Adapter<NotificationListRvAdapter.ViewHolder> {

    private Context context;
    private List<NotificationListData> NotificationList;
    private ApiService apiService;

    public NotificationListRvAdapter(Context context, List<NotificationListData> notificationList) {
        this.context = context;
        NotificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        SetData(viewHolder, i);
        SetAction(viewHolder, i);
    }

    private void SetData(ViewHolder viewHolder, int i) {
        viewHolder.ItemNotificationRvTitleTv.setText(NotificationList.get(i).getTitle());
        String Date=HelperMethod.ConvertDatetoMonthFormat(NotificationList.get(i).getCreatedAt());
        viewHolder.ItemNotificationRvDateTv.setText(Date);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        Date d = null;

        try
        {
            d = input.parse(NotificationList.get(i).getCreatedAt());


        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        viewHolder.ItemNotificationRvTimeTv.setText(formatted);
    }

    private void SetAction(ViewHolder viewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return NotificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Item_NotificationRv_TitleTv)
        TextView ItemNotificationRvTitleTv;
        @BindView(R.id.Item_NotificationRv_DateTv)
        TextView ItemNotificationRvDateTv;
        @BindView(R.id.Item_NotificationRv_TimeTv)
        TextView ItemNotificationRvTimeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
