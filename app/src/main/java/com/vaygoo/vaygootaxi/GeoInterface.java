package com.vaygoo.vaygootaxi;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.vaygoo.vaygootaxi.model.TaxiModel;

import java.util.ArrayList;
import java.util.List;

import static com.vaygoo.vaygootaxi.fragment.MapFragment.stLocation1;
import static com.vaygoo.vaygootaxi.fragment.MapFragment.stLocation2;

/**
 * Created by Wizard on 07.05.2017.
 */

public class GeoInterface {
    private Context mContext;//???????
    private String[] longLatit = {"43.212292","76.952732","43.210916","76.950860", "43.211460","76.950951"};
    private List<TaxiModel> taxiModels;

    public GeoInterface(Context c) {
        mContext = c;
        taxiModels = new ArrayList<>();
        int ii = 0;
        int iy = 1;
        for(int i=0; i< 3; i++)
        {
            TaxiModel taxiModel = new TaxiModel(i, longLatit[iy], longLatit[ii]);
            taxiModels.add(taxiModel);
            ii = ii+2;
            iy = iy+2;
        }

    }

/*
    @JavascriptInterface
    public String getLongitude() {

        if(stLocation2 == null)
            // stLocation2 = "37.615";
            stLocation2 = "76.952";
        Log.v("Excel", "Longitude "+ stLocation2);
        return  stLocation2;
    }
    @JavascriptInterface
    public String getLatitude() {
//в сеттингс сохранять значение, а стартовое будут эти два
        if(stLocation1 == null)
            // stLocation1 = "55.752";
            stLocation1 = "43.212";
        Log.v("Excel", "Latitude "+ stLocation1);
        return  stLocation1;
    }*/
    @JavascriptInterface
    public String getTaxiLongitude(int position) {
        Log.v("Excel", "Taxi longitude "+ taxiModels.get(position).getLongitude() + " " + position);
        return taxiModels.get(position).getLongitude();
    }
    @JavascriptInterface
    public String getTaxiLatitude(int position) {
        Log.v("Excel", "Taxi latitude " + taxiModels.get(position).getLatitude() + " " + position);
        return taxiModels.get(position).getLatitude();
    }
    @JavascriptInterface
    public int getTaxiQuantity() {
        Log.v("Excel", "Quantity " +  taxiModels.size());
        return taxiModels.size();
    }


}