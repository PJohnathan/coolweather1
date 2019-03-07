package com.kuyun.policevideo.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kuyun.policevideo.coolweather.db.City;
import com.kuyun.policevideo.coolweather.db.County;
import com.kuyun.policevideo.coolweather.db.Province;
import com.kuyun.policevideo.coolweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utility {
    //解析和处理服务器返回的省级数据
    public static boolean handlePronvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allPronvices = new JSONArray(response);
                for (int i=0;i<allPronvices.length();i++){
                    JSONObject provinceObjecty = allPronvices.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObjecty.getString("name"));
                    province.setProvinceCode(provinceObjecty.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*Gson gson = new GsonBuilder().create();
            Province province = gson.fromJson("http://guolin.tech/api/china",Province.class);
            province.getProvinceName();
            province.getId();
            province.save();*/
        }
        return false;
    }

    //解析处理服务器返回的市级数据
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    //解析和处理器返回的县级数据
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allcounties = new JSONArray(response);
                for (int i=0;i<allcounties.length();i++){
                    JSONObject countyObject = allcounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //将返回的json数据解析成weather实体类
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  null;
    }
}
