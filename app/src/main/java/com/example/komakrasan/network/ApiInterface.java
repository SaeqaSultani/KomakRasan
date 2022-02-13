package com.example.komakrasan.network;


import com.example.komakrasan.model.ClassCounty;
import com.example.komakrasan.model.ClassProvince;
import com.example.komakrasan.model.ClassRegister;
import com.example.komakrasan.model.ClassResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("ProvincesApi")
    Call<List<ClassProvince>> getProvince();

    @GET("DistrictsApi/{city_id}")
    Call<List<ClassCounty>> getCounty(@Path("city_id") int id);

    @GET("DistitutesApi/{region_id}")
    Call<List<ClassResult>> getData(@Path("region_id") int id);

    @Multipart
    @POST("DistitutesApi")
    Call<ResponseBody> sendData(
            @Part("d_name") RequestBody d_name,
            @Part("d_phone") RequestBody d_phone,
            @Part("d_family_num") RequestBody d_family_num,
            @Part("d_region_village_name") RequestBody d_region_village_name,
            @Part MultipartBody.Part userfile,
            @Part MultipartBody.Part userfile2,
            @Part("region_id") int region_id
    );
}
