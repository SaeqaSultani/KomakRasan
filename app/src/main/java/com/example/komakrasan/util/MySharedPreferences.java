package com.example.komakrasan.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    static MySharedPreferences mySharedPreferences;
    private SharedPreferences sharedPreferences;

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }


    public static MySharedPreferences getInstance(Context context){
        if (mySharedPreferences == null){
             return  new MySharedPreferences(context);
        }
        return  mySharedPreferences;
    }


    public void setId(int id){
        sharedPreferences.edit().putInt("ID",id).apply();
    }


    public void setName(String name){
        sharedPreferences.edit().putString("NAME",name).apply();
    }

    public String getName(){
        return sharedPreferences.getString("NAME", "");
    }

    public int getId(){
        return sharedPreferences.getInt("ID",-1);
    }

    public void clearAll(){
        sharedPreferences.edit().clear().apply();
    }

}
