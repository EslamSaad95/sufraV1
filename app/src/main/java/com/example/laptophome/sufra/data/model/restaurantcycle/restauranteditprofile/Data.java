
package com.example.laptophome.sufra.data.model.restaurantcycle.restauranteditprofile;

import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user")
    @Expose
    private RestaurantListData user;

    public RestaurantListData getUser() {
        return user;
    }

    public void setUser(RestaurantListData user) {
        this.user = user;
    }

}
