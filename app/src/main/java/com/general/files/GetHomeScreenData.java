package com.general.files;

import android.content.Context;

import com.service.handler.ApiHandler;
import com.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

public class GetHomeScreenData {

    private Context context;
    private GeneralFunctions generalFunctions;
    private JSONObject obj_userProfile;

    public void getHomeScreenData(Context context, GeneralFunctions generalFunctions, JSONObject obj_userProfile) {
        this.context = context;
        this.generalFunctions = generalFunctions;
        this.obj_userProfile = obj_userProfile;
        boolean isNewHome_23 = generalFunctions.getJsonValueStr("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile) != null && generalFunctions.getJsonValueStr("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equalsIgnoreCase("Yes");
        homeScreenApiCall(isNewHome_23);

    }

    public void getHomeScreenDeliverAllData(Context context, GeneralFunctions generalFunctions, JSONObject obj_userProfile) {
        this.context = context;
        this.generalFunctions = generalFunctions;
        this.obj_userProfile = obj_userProfile;
        homeScreenApiCall(false);

    }

    private void homeScreenApiCall(boolean isNewHome_23) {
        HashMap<String, String> parameters = new HashMap<>();
        if (isNewHome_23) {
            parameters.put("type", "getServiceCategoriesPro");
        } else {
            parameters.put("type", "getServiceCategories");
        }
        parameters.put("userId", generalFunctions.getMemberId());
        parameters.put("parentId", generalFunctions.getJsonValueStr(Utils.UBERX_PARENT_CAT_ID, obj_userProfile));
        parameters.put("vLatitude", "");
        parameters.put("vLongitude", "");
        parameters.put("eForVideoConsultation", "");

        ApiHandler.execute(context, parameters, false, false, generalFunctions, responseString -> {
            if (isNewHome_23) {
                generalFunctions.storeData("SERVICE_HOME_DATA_23", responseString);
            } else {
                generalFunctions.storeData("SERVICE_HOME_DATA", responseString);
            }
        });
    }
}