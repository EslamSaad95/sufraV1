package com.example.laptophome.sufra.helper;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.sufra.R;
import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.ui.RequestFood.Fragment.RestaurantDetailsCycle.Notification;
import com.example.laptophome.sufra.ui.RequestFood.activity.UserNavigationActivity;
import com.example.laptophome.sufra.ui.SellingFood.activity.RestaurantNavigationActivity;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class HelperMethod {
    public static void replace(Fragment fragment, FragmentManager supportfragment, int id) {
        FragmentTransaction transaction = supportfragment.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void SpinnerAdapter(Context context, List<String> list, ArrayAdapter<String>SpArrayAdapter, Spinner spinner)
    {
        SpArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        SpArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(SpArrayAdapter);
        SpArrayAdapter.notifyDataSetChanged();
    }

    public static void SetToolBar(final String ToolBarTitle, boolean hideCartIc, final Context context)
    {
        if(SharedPrerefrencesManager.GetSharedPrerefrences(Constant.CheckUser_File,Constant.Validation_Key,context).equals(Constant.Client_User)) {
            ImageView CartIv = (ImageView) ((UserNavigationActivity) context).findViewById(R.id.ToolBar_NotificationIv);
            TextView toolbartitle = (TextView) ((UserNavigationActivity) context).findViewById(R.id.ToolbarTitle);
            RelativeLayout BackIc = (RelativeLayout) ((UserNavigationActivity) context).findViewById(R.id.Toolbar_ArrowIv);


            CartIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification notification=new Notification();
                   UserNavigationActivity activityinstance = (UserNavigationActivity) context;
                    HelperMethod.replace(notification,activityinstance.getSupportFragmentManager(),R.id.UserNavigation_RootLayout);
                }
            });
            toolbartitle.setText(ToolBarTitle);
            if (hideCartIc == true) {

                CartIv.setVisibility(View.INVISIBLE);

            } else {

                CartIv.setVisibility(View.VISIBLE);
            }

            BackIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fragment=((UserNavigationActivity)context).getSupportFragmentManager().getBackStackEntryCount();
                    if(fragment==1)
                    {
                        ((UserNavigationActivity)context).finish();
                    }

                    ((UserNavigationActivity) context).getSupportFragmentManager().popBackStack();
                }
            });

        }
        else
        {
            ImageView CartIv = (ImageView) ((RestaurantNavigationActivity) context).findViewById(R.id.ToolBar_NotificationIv);
            TextView toolbartitle = (TextView) ((RestaurantNavigationActivity) context).findViewById(R.id.ToolbarTitle);
            RelativeLayout BackIc = (RelativeLayout) ((RestaurantNavigationActivity) context).findViewById(R.id.Toolbar_ArrowIv);
            toolbartitle.setText(ToolBarTitle);
            if (hideCartIc == true) {

                CartIv.setVisibility(View.INVISIBLE);

            } else {

                CartIv.setVisibility(View.VISIBLE);
                CartIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Notification notification=new Notification();
                        RestaurantNavigationActivity activityinstance = (RestaurantNavigationActivity) context;
                        HelperMethod.replace(notification,activityinstance.getSupportFragmentManager(),R.id.SellNavigation_RootLayout);
                    }
                });
            }

            BackIc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fragment=((RestaurantNavigationActivity)context).getSupportFragmentManager().getBackStackEntryCount();
                    if(fragment==1)
                    {
                        ((RestaurantNavigationActivity)context).finish();
                    }

                    ((RestaurantNavigationActivity) context).getSupportFragmentManager().popBackStack();
                }
            });
        }
    }



    public static ProgressDialog ProgressDialog(Context context,String Message)
    {

        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(Message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }
    public static boolean CheckTextInputEmpty(TextInputLayout textInputLayout)
    {
        if(textInputLayout.getEditText().getText().length()==0)
        {
            textInputLayout.getEditText().requestFocus();
            textInputLayout.getEditText().setError("please enter the field");
            return false;
        }
        return true;
    }


//This method to convert String to RequestBody
    public static RequestBody convertToRequestBody(String part) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), part);
        return requestBody;
    }


    //convert File to Multipat
    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {

        File file = new File(pathImageFile);

        RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"),file);

        MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);

        return Imagebody;
    }

    public static MultipartBody.Part CheckPicture(String imagepath,String Key)
    {
        MultipartBody.Part Imagebody;
        if(imagepath==null)
     {
         Imagebody=null;
     }
     else
     {
         File file = new File(imagepath);

         RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"),file);

          Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);
          }
        return Imagebody;
    }


    public static void openAlbum(int Counter, Context context, final ArrayList<AlbumFile> ImagesFiles, Action<ArrayList<AlbumFile>> action) {
        Album album = new Album();
        Album.initialize(AlbumConfig.newBuilder(context)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.ENGLISH).build());
        album.image(context)// Image and video mix options.
                .multipleChoice()// Multi-Mode, Single-Mode: singleChoice().
                .columnCount(3) // The number of columns in the page list.
                .selectCount(Counter)  // Choose up to a few images.
                .camera(true)// Whether the camera appears in the Item.
                .checkedList(ImagesFiles) // To reverse the list.
                .widget(
                        Widget.newLightBuilder(context)
                                .title("")
                                .statusBarColor(Color.WHITE) // StatusBar color.
                                .toolBarColor(Color.WHITE) // Toolbar color.
                                .navigationBarColor(Color.WHITE) // Virtual NavigationBar color of Android5.0+.
                                .mediaItemCheckSelector(Color.BLUE, Color.GREEN) // Image or video selection box.
                                .bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
                                .build()
                )
                .onResult(action)
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
// The user canceled the operation.
                    }
                })
                .start();
    }


    public static String ConvertDatetoMonthFormat(String datestring)
    {
        String month_name=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        SimpleDateFormat month_date = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        try {
            Date date = sdf.parse(datestring);
             month_name = month_date.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  month_name;
    }

    public static void setdatepicker(DatePickerDialog datePickerDialog, final TextView textView, Context context) {
        final Calendar calendar = Calendar.getInstance();
        final StringBuilder stringBuilder = new StringBuilder();
        final int year = calendar.get(Calendar.YEAR);
        int months = calendar.get(Calendar.MONTH);
        // String monthstring=String.valueOf(months);


        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String daystring = String.valueOf(dayOfMonth);
                int actual_month = month + 1;//as month begin with index 0
                String monthstring = String.valueOf(actual_month);

                if (daystring.length() == 1) {
                    daystring = "0" + daystring;
                }
                if (monthstring.length() == 1) {
                    monthstring = "0" + monthstring;
                }

                stringBuilder.append(year).append("-").append(monthstring).append("-").append(daystring);
                textView.setText(stringBuilder);

            }
        }, year, months, day);
        datePickerDialog.show();
    }
    public  static void hidesoftkeyboard(View view,Activity activity)
    {
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}