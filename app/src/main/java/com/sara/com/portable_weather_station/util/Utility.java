package com.sara.com.portable_weather_station.util;

import com.google.gson.Gson;
import com.sara.com.portable_weather_station.gson.AQI;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 17682 on 2017/3/2.
 */

public class Utility {
    public static AQI handleAQIResponse(String response){
        try{
            JSONArray jsonArray = new JSONArray(response);
            String AQIContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(AQIContent,AQI.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
