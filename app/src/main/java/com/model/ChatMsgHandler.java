package com.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.general.files.GeneralFunctions;
import com.general.files.LocalNotification;
import com.general.files.MyApp;
import com.tiktak24.user.ChatActivity;
import com.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatMsgHandler {
    static GeneralFunctions generalFunc;

    public static ArrayList<String> tripsList = new ArrayList<>();

    public static void performAction(String message_str) {

        /*if(tripsList.size() == 0){
            tripsList.add("123");
            tripsList.add("120");
            tripsList.add("140");
            tripsList.add("131");
        }*/

        if (generalFunc == null) {
            generalFunc = MyApp.getInstance().getAppLevelGeneralFunc();
        }

        JSONObject obj_data = generalFunc.getJsonObject(message_str);
        if (obj_data == null) {
            return;
        }

        Activity currentAct = MyApp.getInstance().getCurrentAct();

        if (currentAct instanceof ChatActivity) {
            ChatActivity chatAct = (ChatActivity) currentAct;

            chatAct.handleIncomingMessages(obj_data);

        } else {
            openChatAct(obj_data);
        }

        String tMessage = generalFunc.getJsonValueStr("tMessage", obj_data);
        String tMsgNotification = generalFunc.getJsonValueStr("tMsgNotification", obj_data);
        String iFromMemberType = generalFunc.getJsonValueStr("iFromMemberType", obj_data);

        if(!iFromMemberType.equalsIgnoreCase(Utils.app_type)){
            LocalNotification.dispatchLocalNotification(MyApp.getInstance().getApplicationContext(), tMsgNotification.trim().equalsIgnoreCase("") ? tMessage : tMsgNotification, true);
        }
    }

    private static void openChatAct(JSONObject obj_data) {

        String iBiddingPostId = generalFunc.getJsonValueStr("iBiddingPostId", obj_data);
        String iTripId = generalFunc.getJsonValueStr("iTripId", obj_data);
        String vBookingNo = generalFunc.getJsonValueStr("vBookingNo", obj_data);
        String vRideNo = generalFunc.getJsonValueStr("vRideNo", obj_data);
//        String iDriverId = generalFunc.getJsonValueStr("iDriverId", obj_data);
        String iToMemberType = generalFunc.getJsonValueStr("iToMemberType", obj_data);
        String iOrderId = generalFunc.getJsonValueStr("iOrderId", obj_data);
        String iToMemberId = generalFunc.getJsonValueStr("iToMemberId", obj_data);

        Intent chatActInt = new Intent(MyApp.getInstance().getApplicationContext(), ChatActivity.class);

        Bundle bn = new Bundle();
        bn.putString("iBiddingPostId", iBiddingPostId);
//        bn.putString("iDriverId", iDriverId);

        bn.putString("iOrderId", iOrderId);

        bn.putString("iToMemberType", iToMemberType);
        bn.putString("iToMemberId", iToMemberId);

        bn.putString("iTripId", iTripId);
        bn.putString("vBookingNo", vBookingNo.trim().equalsIgnoreCase("") ? vRideNo : vBookingNo);

        chatActInt.putExtras(bn);

        MyApp.getInstance().getCurrentAct().startActivity(chatActInt);
    }
}
