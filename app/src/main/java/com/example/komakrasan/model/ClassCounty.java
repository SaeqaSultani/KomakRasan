package com.example.komakrasan.model;

import com.google.gson.annotations.SerializedName;

public class ClassCounty {

    @SerializedName("r_name")
    private String Name;

    @SerializedName("r_id")
    private Integer Id;

    public String getName() {
        return Name;
    }

    public Integer getId() {
        return Id;
    }
}
