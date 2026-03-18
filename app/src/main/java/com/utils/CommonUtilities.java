package com.utils;

import java.util.ArrayList;

public class CommonUtilities {

    public static final String idNo = "78";

    public static final String bNumber = "222017";
    public static final String pName = "tiktak24";
    //public static final String SERVER = "https://www.tiktak24.ch/beta";
    public static final String SERVER = "https://www.tiktak24.ch/";
    //--------------------------------------------------------------------------------------------
    // TODO : Do not Delete
    public static final String DATA_REQ_KEY = "UB792LXUMYBEWVLHZBHQFEGTUKAIP12OFBQAYCXOHXSTSNJNPXSKXBCER31UDOSRRIQZULWXDFVPB429LNRHCE16MUYBVBJCNRXTDGSGGIBIDQMUOK68UIARJRKPO8860UYFIGUSW9037R14IXVBBNPI";
    public static final String APP_REQ_KEY = "0B:D9:A6:C3:CD:31:02:AA:92:CE:DB:16:11:D1:98:1C:B7:AC:CD:D9";

    public static final String TOLLURL = "https://fleet.api.here.com/2/calculateroute.json?apiKey=";
    public static final String SERVER_FOLDER_PATH = "";
    public static final String WEBSERVICE = "webservice_shark.php";
    public static final String SERVER_WEBSERVICE_PATH = SERVER_FOLDER_PATH + WEBSERVICE + "?";

    public static final String SERVER_URL = SERVER + SERVER_FOLDER_PATH;
    public static final String SERVER_URL_WEBSERVICE = SERVER + SERVER_WEBSERVICE_PATH + "?";
    public static final String SERVER_URL_PHOTOS = SERVER_URL + "webimages/";

    public static final String LINKEDINLOGINLINK = SERVER + "linkedin-login/linkedin-app.php";
    public static final String PAYMENTLINK = SERVER + "assets/libraries/webview/payment_configuration_trip.php?";

    public static final String USER_PHOTO_PATH = CommonUtilities.SERVER_URL_PHOTOS + "upload/Passenger/";
    public static final String PROVIDER_PHOTO_PATH = CommonUtilities.SERVER_URL_PHOTOS + "upload/Driver/";
    public static final String STORE_PHOTO_PATH = CommonUtilities.SERVER_URL_PHOTOS + "upload/Company/";

    public static final String BUCKET_NAME = "system_" + pName + "_" + bNumber;
    public static final String BUCKET_FILE_NAME = "ANDROID_USER_" + pName + "_" + bNumber + ".txt";
    public static final String BUCKET_PATH = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + BUCKET_FILE_NAME;

    public static final String DELIVER_ALL_PATH = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + BUCKET_FILE_NAME;

    public static String OriginalDateFormate = "dd MMM, yyyy (EEE)";
    public static String OriginalTimeFormate = "hh:mm aa";
    public static String WithoutDayFormat = "dd MMM, yyyy";
    public static String DayFormatEN = "yyyy-MM-dd";
    public static String DayTimeFormat = "dd MMM, yyyy hh:mm aa";
    public static String BookingDateTimeFormat = "EEE, dd MMM yy";
    public static String serverDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static ArrayList<String> ageRestrictServices = new ArrayList<>();
}