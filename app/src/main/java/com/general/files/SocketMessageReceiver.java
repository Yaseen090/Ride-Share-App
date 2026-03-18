package com.general.files;

import com.model.ChatMsgHandler;
import com.model.SocketEvents;

import org.json.JSONObject;

public class SocketMessageReceiver {
    private static SocketMessageReceiver instance;
    GeneralFunctions generalFunc;

    public SocketMessageReceiver() {
        generalFunc = MyApp.getInstance().getAppLevelGeneralFunc();
    }

    public static SocketMessageReceiver getInstance() {
        if (instance == null) {
            instance = new SocketMessageReceiver();
        }
        return instance;
    }

    public void handleMsg(String eventName, String message_str) {
        JSONObject obj_data = generalFunc.getJsonObject(message_str);
        if (obj_data == null || MyApp.isAppKilled()) {
            return;
        }

        if (eventName.equalsIgnoreCase(SocketEvents.CHAT_SERVICE)) {
            ChatMsgHandler.performAction(message_str);
        }

        if (eventName.equalsIgnoreCase(SocketEvents.SERVICE_REQUEST_STATUS)) {
            new FireTripStatusMsg(MyApp.getInstance().getApplicationContext()).fireTripMsg(message_str);
        }
    }
}
