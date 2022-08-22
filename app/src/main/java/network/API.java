/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

import model.ConstructionPage;
import model.NavigationPage;
import model.PlanetenPage;
import model.ProductionPage;
import model.IntroPage;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 * @author nico
 */
public interface API {
    @POST("rechner.phtml")
    Call<RechnerResponse> calc(
            @Query("sid") String sid,
            @Body RequestBody requestBody
    );
    @FormUrlEncoded
    @POST("intro.phtml")
    Call<IntroPage> intro(
            @Query("sid") String sid,
            @Query("planetindex") Integer index,
            @Query("gotouniverse") Integer universe,
            @Query("cancel") Integer fleetCancel,
            @Query("ajax") Integer one,
            @Field("captcha_id") String captchaId,
            @Field("x") Float x,
            @Field("y") Float y
    );

    @POST("navi.phtml")
    Call<NavigationPage> callNavipage(
            @Query("sid") String sid);

    @POST("produktion.phtml")
    Call<ProductionPage> callProduction(
            @Query("planetindex") Integer planetindex,
            @Query("gotouniverse") Integer gotouniverse,
            @Query("speedperc") Integer speedperc,
            @Body RequestBody requestBody);

    @POST("produktion.phtml")
    Call<ProductionPage> gotoPlani(
            @Query("sid") String sid,
            @Query("planetindex") Integer planetindex,
            @Query("gotouniverse") Integer gotouniverse);

    @POST("produktion.phtml")
    Call<ProductionPage> sortProductionSchleifen(
            @Query("sid") String sid,
            @Query("ajax") int ajax,
            @Body RequestBody requestBody);

    @GET("planeten.phtml")
    Call<PlanetenPage> getPlanetenPage(
            @Query("show") String show,
            @Query("showsub") String showsub,
            @Query("sid") String sid,
            @Query("planetindex") Integer planetindex,
            @Query("gotouniverse") Integer gotouniverse
    );

    @POST("planeten.phtml")
    Call<PlanetenPage> postPlanetenPage(
            @Query("show") String show,
            @Query("showsub") String showsub,
            @Query("sid") String sid,
            @Query("planetindex") Integer planetindex,
            @Query("gotouniverse") Integer gotouniverse,
            @Body RequestBody requestBody
    );

    @POST("user_info.phtml")
    Call<ResponseBody> sendPlayerSpio(@Body RequestBody requestBody);

    @POST("galaxy.phtml")
    Call<ResponseBody> sendSpio(@Body RequestBody requestBody);

    //-------------
    @GET("flottenbewegung.phtml?start=")
    Call<ResponseBody> sendFleetSpio(@Query("spy2") String antriebssignaturId, @Query("sid") String sid);

    @GET("planeten.phtml?show=20")
    Call<ResponseBody> getRessAufPlanis(@Query("sid") String sid);

    @FormUrlEncoded
    @POST("construction.phtml")
    Call<ConstructionPage> construction(
            @Query("sid") String sid,
            @Query("planetindex") Integer index,
            @Query("gotouniverse") Integer universe,
            @Field("gebaeude") Integer gebaeude,
            @Field("key") String buildKey,
            @Query("switch") Integer switchMode,
            @Query("cancel") Integer cancel,
            @Field("askOK") Integer askOK
    );
}
