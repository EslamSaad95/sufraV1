package com.example.laptophome.sufra.data.rest;

import com.example.laptophome.sufra.data.model.acceptorder.AcceptOrder;
import com.example.laptophome.sufra.data.model.addcontact.AddContact;
import com.example.laptophome.sufra.data.model.categories.Categories;
import com.example.laptophome.sufra.data.model.changestate.ChangeState;
import com.example.laptophome.sufra.data.model.cities.Cities;
import com.example.laptophome.sufra.data.model.commision.Commision;
import com.example.laptophome.sufra.data.model.notificationlist.NotificationList;
import com.example.laptophome.sufra.data.model.offercycle.addoffer.AddOffer;
import com.example.laptophome.sufra.data.model.offercycle.deleteoffer.DeleteOffer;
import com.example.laptophome.sufra.data.model.offercycle.offerdetails.OfferDetails;
import com.example.laptophome.sufra.data.model.offercycle.offers.Offers;
import com.example.laptophome.sufra.data.model.offercycle.offerupdate.OfferUpdate;
import com.example.laptophome.sufra.data.model.ordercycle.neworder.NewOrder;
import com.example.laptophome.sufra.data.model.paymentmethod.PaymentMethod;
import com.example.laptophome.sufra.data.model.regions.Regions;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import com.example.laptophome.sufra.data.model.registernotificationtoke.RegisterUnregisterNotiToken;
import com.example.laptophome.sufra.data.model.restaurantcycle.Restaurantproducts.RestaurantProducts;
import com.example.laptophome.sufra.data.model.restaurantcycle.Restaurantproducts.RestaurantProductsData;
import com.example.laptophome.sufra.data.model.restaurantcycle.restauranteditprofile.RestaurantEditProfile;
import com.example.laptophome.sufra.data.model.restaurantcycle.restaurantlogin.RestaurantLogin;
import com.example.laptophome.sufra.data.model.restaurantcycle.restaurantregister.RestaurantRegister;
import com.example.laptophome.sufra.data.model.restaurantdetails.addrestaurantproduct.AddRestaurantProduct;
import com.example.laptophome.sufra.data.model.restaurantdetails.addreview.AddReview;
import com.example.laptophome.sufra.data.model.restaurantdetails.deleteitem.DeleteItem;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantdetails.RestaurantDetails;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantlist.RestaurantList;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantmenu.RestaurantMenu;
import com.example.laptophome.sufra.data.model.restaurantdetails.restaurantreviews.RestaurantReviews;
import com.example.laptophome.sufra.data.model.restaurantrequests.RestaurantRequests;
import com.example.laptophome.sufra.data.model.settings.Settings;
import com.example.laptophome.sufra.data.model.userRequests.UserRequests;
import com.example.laptophome.sufra.data.model.usercycle.userlogin.UserLogin;
import com.example.laptophome.sufra.data.model.usercycle.userprofile.UserProfile;
import com.example.laptophome.sufra.data.model.usercycle.userregister.UserRegister;
import com.example.laptophome.sufra.data.model.usercycle.userresetpassword.UserResetPassword;
import com.yanzhenjie.durban.Durban;

import java.util.ArrayList;
import java.util.List;

public interface ApiService {
    //////////////////////////////////////////General////////////////////////////////
    @GET("cities")
    Call<Cities> GetCities();

    @GET("regions")
    Call<Regions> GetRegions(@Query("city_id") int city_id);

    @GET("categories")
    Call<Categories> GetCategories();

    @GET("payment-methods")
    Call<PaymentMethod> GetPaymentMethod();

    @GET("settings")
    Call<Settings>GetSetting(@Query("email") String email, @Query("password") String password);

    @POST("contact")
    @FormUrlEncoded
    Call<AddContact> ContactUs(@Field("name") String name,@Field("email") String email,@Field("phone") String phone,
                               @Field("type") String type,@Field("content")  String content);
    /////////////////////////////////////////////UserCycle/////////////////////////////////////
    @POST("client/register")
    @FormUrlEncoded
    Call<UserRegister> UserRegister(@Field("name") String name, @Field("email") String email, @Field("password") String password
            , @Field("password_confirmation") String password_confirmation, @Field("phone") String phone, @Field("address") String address
            , @Field("region_id") int region_id);

    @POST("client/login")
    @FormUrlEncoded
    Call<UserLogin> UserLogin(@Field("email") String email, @Field("password") String password);

    @POST("client/reset-password")
    @FormUrlEncoded
    Call<UserResetPassword> UserResetPassword(@Field("email") String email);

    @POST("client/new-password")
    @FormUrlEncoded
    Call<UserResetPassword> UserNewPassword(@Field("code") int code, @Field("password") String password
            , @Field("password_confirmation") String password_confirmation);

    @GET("client/notifications")
    Call<NotificationList>GetClientNotification(@Query("api_token") String api_token);

    @POST("client/register-token")
    @FormUrlEncoded
    Call<RegisterUnregisterNotiToken>RegisterClientNotificationToken(@Field("token") String token, @Field("type") String type, @Field("api_token") String api_token);

    @POST("client/remove-token")
    @FormUrlEncoded
    Call<RegisterUnregisterNotiToken>RemoveClientNotificationToken(@Field("token") String token,@Field("api_token") String api_token);

    @POST("client/profile")
    @FormUrlEncoded
    Call<UserProfile> GetUserProfile(@Field("api_token") String api_token);

    @POST("client/profile")
    @FormUrlEncoded
    Call<UserProfile> EditUserProfile(@Field("api_token") String api_token,@Field("name") String name,@Field("phone") String phone
    ,@Field("email") String email,@Field("password") String password,@Field("password_confirmation") String password_confirmation,
                                       @Field("address") String address,@Field("region_id") int region_id);
    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////ResautantCycle//////////////////////////////////////
    @POST("restaurant/login")
    @FormUrlEncoded
    Call<RestaurantLogin> RestaurantLogin(@Field("email") String email, @Field("password") String password);

    @POST("restaurant/register")
    @Multipart
    Call<RestaurantRegister> RestaurantRegister(@Part("name") RequestBody name, @Part("email") RequestBody email, @Part("password") RequestBody password
            , @Part("password_confirmation") RequestBody password_confirmation, @Part("phone") RequestBody phone, @Part("address") RequestBody address
            , @Part("whatsapp") RequestBody whatsapp, @Part("region_id") RequestBody region_id, @Part("categories[]") RequestBody categories
            , @Part("delivery_period") RequestBody delivery_period, @Part("delivery_cost") RequestBody delivery_cost, @Part("minimum_charger") RequestBody minimum_charger
            , @Part MultipartBody.Part pic, @Part("availability") RequestBody availability);

    @POST("restaurant/reset-password")
    @FormUrlEncoded
    Call<UserResetPassword> RestaurantForgetPassword(@Field("email") String email);

    @POST("restaurant/new-password")
    @FormUrlEncoded
    Call<UserResetPassword> SetRestaurantNewPassword(@Field("code") String code, @Field("password") String password, @Field("password_confirmation") String password_confirmation);


    @POST("restaurant/profile")
    @Multipart
    Call<RestaurantEditProfile> SetRestaurantEdit(@Part("email") RequestBody email, @Part("password") RequestBody password, @Part("password_confirmation") RequestBody password_confirmation
            , @Part("name") RequestBody name, @Part("phone") RequestBody phone, @Part("region_id") RequestBody region_id,
                                                  @Part("categotries[]") RequestBody categotries, @Part("delivery_cost") RequestBody delivery_cost
            , @Part("minimum_charger") RequestBody minimum_charger, @Part("availability") RequestBody availability, @Part MultipartBody.Part photo, @Part("api_token") RequestBody api_token);

    @GET("restaurant/notifications")
    Call<NotificationList>GetRestaurantNotification(@Query("api_token") String api_token);

    @POST("restaurant/register-token")
    @FormUrlEncoded
    Call<RegisterUnregisterNotiToken>RegisterRestaurantNotificationToken(@Field("token") String token, @Field("type") String type, @Field("api_token") String api_token);

    @POST("restaurant/remove-token")
    @FormUrlEncoded
    Call<RegisterUnregisterNotiToken>RemoveRestaurantNotificationToken(@Field("token") String token,@Field("api_token") String api_token);

    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////RestaurantDetails///////////////////////////////////////////

    @GET("restaurants")
    Call<RestaurantList> RestaurantList(@Query("page") int page);

    @GET("restaurant")
    Call<RestaurantDetails> RestaurantDetails(@Query("restaurant_id") int restaurant_id);

    @GET("items")
    Call<RestaurantMenu> RestaurantMenu(@Query("restaurant_id") int restaurant_id, @Query("page") int page);

    @GET("restaurant/reviews")
    Call<RestaurantReviews> RestaurantReviews(@Query("api_token") String api_token, @Query("restaurant_id") int restaurant_id, @Query("page") int page);

    @POST("client/restaurant/review")
    @FormUrlEncoded
    Call<AddReview>AddReview(@Field("rate") int  rate, @Field("comment") String comment, @Field("restaurant_id") int restaurant_id, @Field("api_token")String api_token);



    @GET("restaurant/my-items")
        Call<RestaurantMenu> RestaurantProducts(@Query("api_token") String api_token, @Query("page") int page);

    @POST("restaurant/new-item")
    @Multipart
    Call<AddRestaurantProduct>AddRestaurantProduct(@Part("description") RequestBody description , @Part("price") RequestBody price
            , @Part("preparing_time") RequestBody preparing_time , @Part("name") RequestBody name , @Part MultipartBody.Part photo
            , @Part("api_token") RequestBody api_token);

    @POST("restaurant/update-item")
    @Multipart
    Call<AddRestaurantProduct>UpdateRestaurantProduct(@Part("description") RequestBody description , @Part("price") RequestBody price
            , @Part("preparing_time") RequestBody preparing_time , @Part("name") RequestBody name , @Part MultipartBody.Part photo
         , @Part("item_id") RequestBody item_id  , @Part("api_token") RequestBody api_token);

    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<DeleteItem> DeleteRestaurantProduct(@Field("api_token") String api_token ,@Field("item_id") int item_id);


    @GET("restaurant/my-orders")
    Call<RestaurantRequests> GetNewRestaurantRequest(@Query("api_token") String api_token,@Query("state") String state,@Query("page") int page);

    @POST("restaurant/accept-order")
    @FormUrlEncoded
    Call<AcceptOrder>AcceptOrder(@Field("api_token") String api_token,@Field("order_id") int  order_id);

    @POST("restaurant/reject-order")
    @FormUrlEncoded
    Call<AcceptOrder>RejectOrder(@Field("api_token") String api_token,@Field("order_id") int  order_id);

    @POST("restaurant/confirm-order")
    @FormUrlEncoded
    Call<AcceptOrder>ConfirmOrderDelivery(@Field("order_id") int order_id,@Field("api_token") String api_token);

    @GET("restaurant/commissions")
    Call<Commision>GetRestaurantCommision(@Query("api_token") String api_token);

    @POST("restaurant/change-state")
    @FormUrlEncoded
    Call<ChangeState> ChangeState(@Field("state")String state, @Field("api_token") String api_token);

    ////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////USERS Orders//////////////////////////////////////////////

    @GET("client/my-orders")
    Call<UserRequests> GetUserRequests(@Query("api_token") String api_token, @Query("state") String state, @Query("page") int page);

    @POST("client/new-order")
    @FormUrlEncoded
    Call<NewOrder> addorder(@Field("restaurant_id") int restaurant_id, @Field("note") String note, @Field("address") String address, @Field("payment_method_id")int payment_method_id
    , @Field("phone") String phone, @Field("name") String name, @Field("api_token") String api_token, @Field("items[]") ArrayList<Integer>items,@Field("quantities[]")ArrayList<Integer> quantities
    ,@Field("notes[]") ArrayList<String> notes);

    @POST("client/confirm-order")
    @FormUrlEncoded
    Call<AcceptOrder>ConfirmUserOrderDelivery(@Field("order_id") int order_id,@Field("api_token") String api_token);

    ///////////met2aglaaaaa//////////////////////////////////////////////////////////////////////////
    @POST("client/decline-order")
    @FormUrlEncoded
    Call<AcceptOrder>RefuseOrderByUser(@Field("api_token") String api_token);
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////Offers/////////////////////////////////////////////////////////

    @GET("offers")
    Call<Offers> GetAllOffers(@Query("page") int page);

    @GET("offer")
    Call<OfferDetails> GetOfferDetails(@Query("offer_id") int offer_id);

    @GET("restaurant/my-offers")
    Call<Offers>GetRestaurantOffers(@Query("api_token") String api_token, @Query("page") int page);

    @POST("restaurant/new-offer")
    @Multipart
    Call<AddOffer>AddNewOffer(@Part("description") RequestBody description , @Part("price") RequestBody price, @Part("starting_at") RequestBody starting_at
    , @Part("name") RequestBody name, @Part MultipartBody.Part photo, @Part("ending_at") RequestBody ending_at, @Part("api_token") RequestBody api_token);

    @POST("restaurant/delete-offer")
    @FormUrlEncoded
    Call<DeleteOffer> DeleteOffer(@Field("offer_id") int offer_id, @Field("api_token") String api_token);

    @POST("restaurant/update-offer")
    @Multipart
    Call<OfferUpdate>UpdateOffer (@Part("description") RequestBody description , @Part("price") RequestBody price, @Part("starting_at") RequestBody starting_at
         , @Part("name") RequestBody name, @Part MultipartBody.Part photo, @Part("ending_at") RequestBody ending_at, @Part("offer_id") RequestBody offer_id , @Part("api_token") RequestBody api_token);

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}