package com.aze51.bidbid_client.Network;



import com.aze51.bidbid_client.Fragment.PushListViewItem;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
/**
 * Created by Leekh on 2016-06-29.
 */
public interface NetworkService {

    @POST("content/join")
    Call<Join> newMember(@Body Join join);

    @POST("content/login")
    Call<Login> getMember2(@Body Login login);

    @POST("sign/in")
    Call<Login> getMember(@Body Login login);

    @GET("content/product")
    Call<List<Product>> getContents();

    @GET("content/check_id/{user_id}")
    Call<String> getID(@Path("user_id") String user_id);

    @GET("/detail/{product_id}/{user_id}")
    Call<List<Product>> getContent(@Path("product_id") long id, @Path("user_id") String userid);
    // SMS 인증
    @GET("certify/{phonenum}")
    Call<String> getPhoneCertification(@Path("phonenum") long phoneNum);
    @POST("bid")
    Call<Auction> finishbid(@Body Auction auction);
    @GET("search/{inputContents}")
    Call<List<Product>> searchContents(@Path("inputContents") String intputContents);
    @GET("product")
    Call<List<List<Product>>> getProducts();
    @GET("userinfo/{user_id}")
    Call<List<Product>> getMyPage(@Path("user_id") String userId);
    //@POST("favorite/{user_id,}")
    // check session
    @GET("/sign")
    Call<User> getSession();
    // 로그아웃
    @GET("/sign/out")
    Call<User> logout();

    //즐겨찾기 조회
    @GET("favorite/{user_id}")
    Call<List<Product>> getFavoriteProduct(@Path("user_id") String userId);

    //즐겨찾기 등록
    @POST("favorite")
    Call<Favorite> registerFavorite(@Body Favorite favorite);

    //즐겨찾기 삭제
    @GET("favorite/{user_id}/{register_id}")
    Call<Void> deleteFavorite(@Path("user_id") String userId, @Path("register_id") int registerId);
    // 푸시 알림 리스트
    @GET("notify/{user_id}")
    Call<List<PushListViewItem>> getPushList(@Path("user_id") String userId);


}
