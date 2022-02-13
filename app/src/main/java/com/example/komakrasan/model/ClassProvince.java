package com.example.komakrasan.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

public class ClassProvince {

    @SerializedName("c_id")
    private Integer Id;

    @SerializedName("c_name")
    private String Name;


    @SerializedName("response")
    private String Response ;

    public String getName() {
        return Name;
    }

    public Integer getId() {
        return Id;
    }

    public String getResponse() {
        return Response;
    }

}
