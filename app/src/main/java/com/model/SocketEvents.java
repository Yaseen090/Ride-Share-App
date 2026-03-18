package com.model;

import com.service.handler.AppService;
import com.utils.Utils;

public class SocketEvents {
    public static String MEMBER_EVENT = "";
    public static String CHAT_SERVICE = "";
    public static String SERVICE_REQUEST_STATUS = "";

    public static void buildEvents(String memberId) {
        CHAT_SERVICE = "CHAT_SERVICE_" + memberId;
        SERVICE_REQUEST_STATUS = "SERVICE_REQUEST_STATUS_" + memberId;
        MEMBER_EVENT = Utils.userType.toUpperCase() + "_" + memberId;

        AppService.listOfEvents.clear();
        AppService.listOfEvents.add(SocketEvents.MEMBER_EVENT);
        AppService.listOfEvents.add(SocketEvents.CHAT_SERVICE);
        AppService.listOfEvents.add(SocketEvents.SERVICE_REQUEST_STATUS);
    }
}
