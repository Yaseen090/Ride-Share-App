package com.general.files;

import android.content.Context;

import androidx.annotation.NonNull;

import com.service.handler.ApiHandler;
import com.service.server.ServerTask;
import com.utils.Utils;

import java.io.File;
import java.util.HashMap;

public class GetDeliverAllCategoryDataType {

    private final GeneralFunctions mGeneralFunc;
    private final Context mContext;
    private final String iServiceId;
    private final String latitude;
    private final String longitude;

    public GetDeliverAllCategoryDataType(@NonNull Context context, @NonNull GeneralFunctions generalFunc, String iServiceId, String latitude, String longitude ) {
        this.mContext = context;
        mGeneralFunc = generalFunc;
        this.iServiceId = iServiceId;
        this.latitude = latitude;
        this.longitude = longitude;

         //handling for GCS(Google Cloud) wait for GCS RL
        //ApiHandler.downloadFile(this, CommonUtilities.DELIVER_ALL_PATH, this).execute();
            getDeliverAllListCategoryeWise();

    }

    private void getDeliverAllListCategoryeWise() {
        HashMap<String, String> parameters = new HashMap<>();
       // parameters.put("type", "loadAvailableRestaurants");
        parameters.put("type", "loadAvailableRestaurantsAll");
        parameters.put("DEFAULT_SERVICE_CATEGORY_ID_ALL", iServiceId );
        parameters.put("userId", mGeneralFunc.getMemberId());
        parameters.put("PassengerLat", latitude);
        parameters.put("PassengerLon", longitude);
        parameters.put("vLang", mGeneralFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        parameters.put("eSystem", Utils.eSystem_Type);
        parameters.put("UserType", Utils.app_type);

        parameters.put("fOfferType", "NO");
        parameters.put("eFavStore",  "NO");

        ApiHandler.execute(mContext, parameters, false, false, mGeneralFunc, responseString -> {
            // data get Done
            mGeneralFunc.storeData("SubcategoryForAllDeliver", responseString);
        });
    }

}