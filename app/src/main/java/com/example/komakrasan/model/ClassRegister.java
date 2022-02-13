package com.example.komakrasan.model;

import com.google.gson.annotations.SerializedName;

public class ClassRegister {

    @SerializedName("d_name")
    private String Province;

    @SerializedName("d_phone")
    private String District;

    @SerializedName("d_family_num")
    private String  FamilyNumber;

    @SerializedName("d_region_village_name")
    private String PlaceName;

    @SerializedName("userfile")
    private String ParsonImage;

    @SerializedName("userfile2")
    private String HomeImage;

    @SerializedName("region_id")
    private Integer Id;

    @SerializedName("response")
    private String Response;

    public String getProvince() {
        return Province;
    }

    public String getDistrict() {
        return District;
    }

    public String getFamilyNumber() {
        return FamilyNumber;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public String getParsonImage() {
        return ParsonImage;
    }

    public String getHomeImage() {
        return HomeImage;
    }

    public Integer getId() {
        return Id;
    }

    public String getResponse() {
        return Response;
    }
}
