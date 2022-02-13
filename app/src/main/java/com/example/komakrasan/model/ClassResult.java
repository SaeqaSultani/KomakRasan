package com.example.komakrasan.model;

import com.google.gson.annotations.SerializedName;

public class ClassResult {

    @SerializedName("d_name")
    private String Name;

    @SerializedName("d_family_num")
    private String Number_Family;

    @SerializedName("d_region_village_name")
    private String Place;



    @SerializedName("h_govermant_answer")
    private String Answer;


    @SerializedName("response")
    private String Response;

    public String getName() {
        return Name;
    }

    public String getNumber_Family() {
        return Number_Family;
    }

    public String getPlace() {
        return Place;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getResponse() {
        return Response;
    }
}
