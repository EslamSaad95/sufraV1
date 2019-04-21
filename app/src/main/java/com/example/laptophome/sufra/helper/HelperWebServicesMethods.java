package com.example.laptophome.sufra.helper;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laptophome.sufra.data.model.cities.Cities;
import com.example.laptophome.sufra.data.model.regions.City;
import com.example.laptophome.sufra.data.model.regions.Regions;
import com.example.laptophome.sufra.data.rest.ApiService;
import com.example.laptophome.sufra.data.rest.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperWebServicesMethods {
    static ApiService apiService;
   static List<String>RegionLists;
   static List<String>CitiesList=new ArrayList<>();
   static ArrayAdapter<String>CitiesArrayAdapter;
    static ArrayAdapter<String>RegionArrayAdapter;
    public static void clearcity()
    {CitiesList.clear();}

    public static void GetCities(final Context context,  final Spinner spinner)
    {   CitiesList.clear();
        CitiesList.add("Select City");
        apiService = RetrofitClient.getclient().create(ApiService.class);
        apiService.GetCities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                try {
                    if(response.body().getStatus().equals(1))
                    {
                        for(int i=0;i<response.body().getData().getData().size();i++)
                        {
                            CitiesList.add(response.body().getData().getData().get(i).getName());
                        }
                        HelperMethod.SpinnerAdapter(context,CitiesList,CitiesArrayAdapter,spinner);
                    }
                    else
                    {
                        Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {}
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    public static void GetRegions(final Context context
            , final Spinner RegionSpinner, final Spinner CitySpinner)
    {
        apiService = RetrofitClient.getclient().create(ApiService.class);
        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RegionLists=new ArrayList<>();
                RegionLists.add("Select Region");

              apiService.GetRegions((int) CitySpinner.getSelectedItemId()).enqueue(new Callback<Regions>() {
                  @Override
                  public void onResponse(Call<Regions> call, Response<Regions> response) {
                      if(response.body().getStatus().equals(1))
                      {
                          for(int i=0;i<response.body().getData().getData().size();i++)
                          {
                              RegionLists.add(response.body().getData().getData().get(i).getName());

                          }
                          HelperMethod.SpinnerAdapter(context,RegionLists,RegionArrayAdapter,RegionSpinner);
                      }
                      else
                      {
                          Toast.makeText(context,response.body().getMsg(),Toast.LENGTH_LONG).show();
                      }
                  }

                  @Override
                  public void onFailure(Call<Regions> call, Throwable t) {

                  }
              });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
