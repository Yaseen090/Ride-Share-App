package com.general.files;

import android.content.Context;

import androidx.annotation.NonNull;

import com.service.handler.ApiHandler;

import java.util.HashMap;

public class GetSubCategoryDataAllCategoryType {

    private final GeneralFunctions mGeneralFunc;
    private final Context mContext;
    private final String iServiceId;
    private final String latitude;
    private final String longitude;

    public GetSubCategoryDataAllCategoryType(@NonNull Context context, @NonNull GeneralFunctions generalFunc, String iServiceId, String latitude, String longitude ,Boolean isBidding) {
        this.mContext = context;
        mGeneralFunc = generalFunc;
        this.iServiceId = iServiceId;
        this.latitude = latitude;
        this.longitude = longitude;

        if(isBidding)
        {
            getBiddingSubcateGoryListCategoryeWise();
        }
        else
        {
            getSubcateGoryListCategoryeWise();
        }
    }

    private void getSubcateGoryListCategoryeWise() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getServiceCategories");
        parameters.put("parentId", iServiceId );
        parameters.put("userId", mGeneralFunc.getMemberId());
        parameters.put("vLatitude", latitude);
        parameters.put("vLongitude", longitude);

        ApiHandler.execute(mContext, parameters, false, false, mGeneralFunc, responseString -> {
            // data get Done
            mGeneralFunc.storeData("SubcategoryForAllCategory", responseString);
        });
    }

    private void getBiddingSubcateGoryListCategoryeWise() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getServiceCategories");
        parameters.put("parentId", iServiceId );
        parameters.put("userId", mGeneralFunc.getMemberId());
        parameters.put("vLatitude", latitude);
        parameters.put("vLongitude", longitude);

        parameters.put("eCatType","Bidding");

        ApiHandler.execute(mContext, parameters, false, false, mGeneralFunc, responseString -> {
            // data get Done
            mGeneralFunc.storeData("SubcategoryForBiddingCategory", responseString);
        });
    }
}