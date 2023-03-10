package com.appadi.todoapp.API;

import com.appadi.todoapp.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestData {
    @GET("retrieve.php")
    Call<ResponseModel> ardRetrieveData();

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel> ardCreateData(
            @Field("nama") String nama,
            @Field("deskripsi") String deskripsi,
            @Field("tanggal") String tanggal,
            @Field("waktu") String waktu
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseModel> ardDeleteData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("get.php")
    Call<ResponseModel> ardGetData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("deskripsi") String deskripsi,
            @Field("tanggal") String tanggal,
            @Field("waktu") String waktu
    );

    @FormUrlEncoded
    @POST("complete.php")
    Call<ResponseModel> ardCompleteData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("uncomplete.php")
    Call<ResponseModel> ardUncompleteData(
            @Field("id") int id
    );
}
