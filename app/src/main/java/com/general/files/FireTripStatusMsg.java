package com.general.files;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.general.call.CommunicationManager;
import com.model.ChatMsgHandler;
import com.tiktak24.user.AdditionalChargeActivity;
import com.tiktak24.user.BookingActivity;
import com.tiktak24.user.ChatActivity;
import com.tiktak24.user.ConfirmEmergencyTapActivity;
import com.tiktak24.user.HistoryDetailActivity;
import com.tiktak24.user.MainActivity;
import com.tiktak24.user.PaymentWebviewActivity;
import com.tiktak24.user.RatingActivity;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.rideSharing.RideShareActiveTripActivity;
import com.tiktak24.user.rideSharingPro.RideSharingProHomeActivity;
import com.utils.Logger;
import com.utils.MyUtils;
import com.utils.Utils;
import com.view.GenerateAlertBox;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

/**
 * Created by Admin on 20/03/18.
 */

public class FireTripStatusMsg {

    private final String TAGS = FireTripStatusMsg.class.getSimpleName();
    private Context mContext;
    private static String tmp_msg_chk = "", tmp_store_key = "";

    public FireTripStatusMsg() {
    }

    public FireTripStatusMsg(Context mContext) {
        this.mContext = mContext;
    }

    public void fireTripMsg(String message) {

        if (LiveActivityNotificationHandle(message)) {
            return;
        }

        Logger.d(TAGS, "MsgReceived :: called");
        if (!Utils.checkText(message) || tmp_msg_chk.equals(message)) {
            Logger.d(TAGS, "MsgReceived :: return");
            return;
        }
        tmp_msg_chk = message;

        Logger.e(TAGS, "MsgReceived::" + message);
        String finalMsg = message;

        if (!GeneralFunctions.isJsonObj(finalMsg)) {
            try {
                finalMsg = new JSONTokener(message).nextValue().toString();
            } catch (JSONException e) {
                Logger.e("Exception", "::" + e.getMessage());
            }

            if (!GeneralFunctions.isJsonObj(finalMsg)) {
                finalMsg = finalMsg.replaceAll("(?:^\")|(?:\"$)", "");

                if (!GeneralFunctions.isJsonObj(finalMsg)) {
                    finalMsg = message.replaceAll("\\\\", "");
                    finalMsg = finalMsg.replaceAll("(?:^\")|(?:\"$)", "");
                    if (!GeneralFunctions.isJsonObj(finalMsg)) {
                        finalMsg = message.replace("\\\"", "\"").replaceAll("(?:^\")|(?:\"$)", "");
                    }
                    finalMsg = finalMsg.replace("\\\\\"", "\\\"");
                }
            }
        }

        if (MyApp.getInstance() == null) {
            Log.d("FireTripStatusMsg", " dispatchNotification 1 outer");
            if (mContext != null) {
                Log.d("FireTripStatusMsg", " dispatchNotification 1" + " finalMsg : " + finalMsg);
                dispatchNotification(finalMsg);
            }
            return;
        }

        if (MyApp.getInstance().getCurrentAct() != null) {
            mContext = MyApp.getInstance().getCurrentAct();
        }

        if (mContext == null) {
            Log.d("FireTripStatusMsg", " dispatchNotification 2" + " finalMsg : " + finalMsg);
            dispatchNotification(finalMsg);
            return;
        }

        GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(mContext);
        JSONObject obj_msg = generalFunc.getJsonObject(finalMsg);
        String tSessionId = generalFunc.getJsonValueStr("tSessionId", obj_msg);

        if (!tSessionId.equals("") && !tSessionId.equals(generalFunc.retrieveValue(Utils.SESSION_ID_KEY))) {
            return;
        }

        if (!generalFunc.isUserLoggedIn() && !Utils.checkText(generalFunc.getMemberId())) {
            return;
        }

        if (!GeneralFunctions.isJsonObj(finalMsg)) {
            String passMessage = generalFunc.convertNumberWithRTL(message);
            MyApp.getInstance().getLoclaNotificationObj().dispatchLocalNotification(mContext, passMessage, true);
            generalFunc.showGeneralMessage("", passMessage);
            return;
        }

        boolean isMsgExist = isTripStatusMsgExist(generalFunc, finalMsg, mContext);
        Logger.d(TAGS, "MsgReceived:: MsgExist-> " + isMsgExist);

        if (isMsgExist) {
            return;
        }

        if (msgHandling(generalFunc, obj_msg)) {
            return;
        }

        if (rentItemListPostHandling(generalFunc, obj_msg)) {
            return;
        }

        if (mContext instanceof RideSharingProHomeActivity objAct && objAct.isRidesFrg) {
//            objAct.rsRidesFragment.pubNubMsgArrived(finalMsg);
//        } else if (mContext instanceof RideMyList objAct) {
//            objAct.pubNubMsgArrived(finalMsg);
        } else if (mContext instanceof RideShareActiveTripActivity objAct) {
            objAct.pubNubMsgArrived(finalMsg);
//        } else if (mContext instanceof TrackOrderActivity objAct) {
//            objAct.pubnubMessage(obj_msg);
//        } else if (mContext instanceof TrackAnyLiveTracking objAct) {
//            objAct.pubNubMsgArrived(obj_msg.toString());
//        } else if (mContext instanceof TrackAnyList objAct) {
//            objAct.pubNubMsgArrived(obj_msg.toString());
//        }  else if (mContext instanceof PairCodeGenrateActivity objAct) {
//            objAct.pubNubMsgArrived(obj_msg.toString());
        } else if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(() -> continueDispatchMsg(generalFunc, obj_msg));
        } else {
            Log.d("FireTripStatusMsg", " dispatchNotification 1" + " finalMsg : " + finalMsg);
            dispatchNotification(finalMsg);
        }
    }

    private boolean rentItemListPostHandling(GeneralFunctions generalFunc, JSONObject obj_msg) {
//        if (mContext instanceof UberXHomeActivity) {
//            if (((UberXHomeActivity) mContext).myRentItemListFragment != null && ((UberXHomeActivity) mContext).isRentItemListFrg) {
//                ((UberXHomeActivity) mContext).myRentItemListFragment.pubNubMsgArrived(obj_msg.toString());
//                return true;
//            }
//        } else if (mContext instanceof RentItemListPostActivity objAct) {
//            objAct.pubNubMsgArrived(obj_msg.toString());
//            return true;
//        }
        return false;
    }

    private boolean LiveActivityNotificationHandle(String message) {
        Context mLocContext = this.mContext;
        if (mLocContext == null && MyApp.getInstance() != null && MyApp.getInstance().getCurrentAct() == null) {
            mLocContext = MyApp.getInstance().getApplicationContext();
        }

        if (mLocContext != null) {
            GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(mLocContext);
            JSONObject obj_msg = generalFunc.getJsonObject(message);

            if (generalFunc.getJsonValue("LiveActivityEnd", message).equalsIgnoreCase("Yes")) {
                LiveActivityNotification.getInstance().liveNotificationCancelAll();
                return false;
            }

            boolean isLiveActivity = generalFunc.getJsonValue("ENABLE_NOTIFICATION_LIVE_ACTIVITY", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON)).equalsIgnoreCase("Yes");
            JSONObject mDataObj = generalFunc.getJsonObject("LiveActivityData", obj_msg);
            String APP_TYPE = generalFunc.getJsonValueStr("APP_TYPE", mDataObj);

            if (isLiveActivity && (APP_TYPE.equalsIgnoreCase(Utils.CabGeneralType_Ride) || APP_TYPE.equalsIgnoreCase("DELIVERALL"))) {
                Logger.d(TAGS, "MsgReceived :: LiveActivity >> " + message);
                LiveActivityNotification.getInstance().liveNotification(mLocContext, message);
                return true;
            }
        }
        return false;
    }

    private void continueDispatchMsg(GeneralFunctions generalFunc, JSONObject obj_msg) {
        boolean isTripEndDialogShown = false;
        String messageStr = generalFunc.getJsonValueStr("Message", obj_msg);

        String vTitle = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vTitle", obj_msg));
        String eType = generalFunc.getJsonValueStr("eType", obj_msg);

        if (messageStr.equals("")) {

            String msgTypeStr = generalFunc.getJsonValueStr("MsgType", obj_msg);
            //   String messageType_str = generalFunc.getJsonValueStr("MessageType", obj_msg);

            if (msgTypeStr.equalsIgnoreCase("CHAT")) {

                ChatMsgHandler.performAction(obj_msg.toString());

                return;
            } else if (msgTypeStr.equalsIgnoreCase("VOIP")) {
                if (MyApp.getInstance().isMyAppInBackGround()) {
                    LocalNotification.dispatchLocalNotification(mContext, generalFunc.getJsonValueStr("vTitle", obj_msg), false);
                }
            } else if (!msgTypeStr.equalsIgnoreCase("LocationUpdate") && !msgTypeStr.equalsIgnoreCase("LocationUpdateOnTrip")) {
                if (obj_msg.has("RestartAPP") && generalFunc.getJsonValueStr("RestartAPP", obj_msg).equalsIgnoreCase("Yes")) {
                    MyApp.getInstance().restartWithGetDataApp();
                    return;
                } else {
                    LocalNotification.dispatchLocalNotification(mContext, vTitle, true);
                }

                if (!msgTypeStr.equalsIgnoreCase("REQUEST_TIMEOUT")) {
                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), btn_id -> doOperations());
                }
            }
        } else {
            String cNotif = generalFunc.getJsonValueStr("CustomNotification", obj_msg);
            if (messageStr.equalsIgnoreCase("TaxiBidDriverQuotation")) {
                if (mContext instanceof MainActivity mainActivity) {
                    mainActivity.updateTaxiBidDriver(obj_msg);
                }
            } else if (messageStr.equalsIgnoreCase("RiderShareBooking")) {
                dispatchRiderShareBookingRequest(generalFunc, obj_msg.toString());
            } else if (messageStr.equalsIgnoreCase("GoPayVerifyAmount")) {
                final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                generateAlert.setCancelable(false);
                generateAlert.setContentMessage("", vTitle);
                generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                generateAlert.showAlertBox();
                LocalNotification.dispatchLocalNotification(mContext, vTitle, true);
            } else if (Utils.checkText(cNotif) && cNotif.equalsIgnoreCase("Yes") && MyApp.getInstance().isMyAppInBackGround()) {

                MyApp.getInstance().getLoclaNotificationObj().customNotification(MyApp.getInstance().getApplicationContext(), obj_msg.toString());
                if (messageStr.equalsIgnoreCase("TripStarted") || messageStr.equalsIgnoreCase("TripEnd") || messageStr.equalsIgnoreCase("TripCancelledByDriver") || messageStr.equalsIgnoreCase("TripCancelled") || messageStr.equalsIgnoreCase("DestinationAdded")
                        || messageStr.equalsIgnoreCase("VerifyCharges") || messageStr.equalsIgnoreCase("VerifyChargesDeclined") || messageStr.equalsIgnoreCase("CabRequestAccepted") || messageStr.equalsIgnoreCase("DriverArrived")) {
                    if (messageStr.equalsIgnoreCase("TripEnd")) {
                        Logger.e("FireTripStatusMsg", "::mContext::" + ((Activity) mContext).isFinishing());

                        if (mContext != null) {
                            isTripEndDialogShown = true;
                            generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"),
                                    i -> {
                                        Logger.e("onAlertButtonClick", ":onAlertButtonClick:");
                                        MyApp.getInstance().restartWithGetDataApp();
                                    });
                        }
                    }
                } else if (messageStr.equalsIgnoreCase("OrderDelivered")) {
                    handleOrderDeliverdDialog(generalFunc, obj_msg, vTitle);
                } else {
                    return;
                }
            }

            if (messageStr.equalsIgnoreCase("RideShareStartTrip") || messageStr.equalsIgnoreCase("RideShareEndTrip") || messageStr.equalsIgnoreCase("RideSharePickup") || messageStr.equalsIgnoreCase("schedulePublishRideRide") || messageStr.equalsIgnoreCase("RideShareAlert")) {
                generalFunc.showGeneralMessage("", vTitle);
            } else if (messageStr.equalsIgnoreCase("TripCancelledByDriver") || messageStr.equalsIgnoreCase("TripCancelled") || messageStr.equalsIgnoreCase("DestinationAdded") || messageStr.equalsIgnoreCase("TripEnd")) {

                if (messageStr.equalsIgnoreCase("TripEnd") || messageStr.equalsIgnoreCase("TripCancelledByDriver") || messageStr.equalsIgnoreCase("TripCancelled")) {
                    generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "Yes");
                }

                if (eType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

                    String iDriverId = generalFunc.getJsonValueStr("iDriverId", obj_msg);
                    String iTripId = generalFunc.getJsonValueStr("iTripId", obj_msg);
                    String showTripFare = generalFunc.getJsonValueStr("ShowTripFare", obj_msg);

                    if (MyApp.getInstance().getCurrentAct() instanceof AdditionalChargeActivity && MyApp.getInstance().additionalChargesAct != null && MyApp.getInstance().additionalChargesAct.tripDetail.get("iTripId").equals(iTripId)) {
                        MyApp.getInstance().additionalChargesAct.pubNubMsgArrived(obj_msg.toString(), true);
                    } else {
                        if (MyApp.getInstance().getCurrentAct() instanceof BookingActivity) {
                            ((BookingActivity) MyApp.getInstance().getCurrentAct()).focusFragment(1);
                        }

                        if (messageStr.equalsIgnoreCase("TripEnd") || showTripFare.equalsIgnoreCase("true")) {
                            showPubnubGeneralMessage(generalFunc, iTripId, vTitle, false, true);
                        } else {

                            if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity || MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity) {

                                String tripId = "";
                                if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity activity) {
//                                    tripId = activity.data_trip_ada.get("iTripId");
                                } else if (MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity activity) {
                                    tripId = activity.iTripId;
                                }

                                if (!tripId.equalsIgnoreCase("") && iTripId.equalsIgnoreCase(tripId)) {
                                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"),
                                            buttonId -> {
                                                MyApp.getInstance().restartWithGetDataApp();
                                            });
                                } else {
                                    generalFunc.showGeneralMessage("", vTitle);
                                }
                            } else {
                                generalFunc.showGeneralMessage("", vTitle);
                            }
                        }
                    }
                } else if (eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {

                    String iDriverId = generalFunc.getJsonValueStr("iDriverId", obj_msg);
                    String iTripId = generalFunc.getJsonValueStr("iTripId", obj_msg);
                    String showTripFare = generalFunc.getJsonValueStr("ShowTripFare", obj_msg);
                    String Is_Last_Delivery = generalFunc.getJsonValueStr("Is_Last_Delivery", obj_msg);

                    {
                        if (MyApp.getInstance().getCurrentAct() instanceof BookingActivity) {
                            ((BookingActivity) MyApp.getInstance().getCurrentAct()).focusFragment(1);
                        }

                        if (messageStr.equalsIgnoreCase("TripEnd") || showTripFare.equalsIgnoreCase("true")) {

                            /*Multi Related Condi*/

                            if (Is_Last_Delivery.equalsIgnoreCase("Yes")) {
                                showMultiPubnubGeneralMessage(generalFunc, obj_msg, true);
                            } else {
                                showMultiPubnubGeneralMessage(generalFunc, obj_msg, false);
                            }

                        } else {

                            if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity || MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity) {
                                String tripId = "";
                                if (MyApp.getInstance().getCurrentAct() instanceof ChatActivity activity) {
//                                    tripId = activity.data_trip_ada.get("iTripId");
                                } else if (MyApp.getInstance().getCurrentAct() instanceof ConfirmEmergencyTapActivity activity) {
                                    tripId = activity.iTripId;
                                }

                                if (!tripId.equalsIgnoreCase("") && iTripId.equalsIgnoreCase(tripId)) {
                                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"),
                                            buttonId -> {
                                                MyApp.getInstance().restartWithGetDataApp();

                                            });
                                } else {
                                    generalFunc.showGeneralMessage("", vTitle);
                                }

                            } else {
                                generalFunc.showGeneralMessage("", vTitle);
                            }
                        }
                    }

                } else if (generalFunc.getJsonValueStr("eSystem", obj_msg).equalsIgnoreCase(Utils.eSystem_Type)) {
                    if (messageStr.equalsIgnoreCase("OrderConfirmByRestaurant") || messageStr.equalsIgnoreCase("OrderDeclineByRestaurant") || messageStr.equalsIgnoreCase("OrderPickedup") ||
                            messageStr.equalsIgnoreCase("OrderDelivered") || messageStr.equalsIgnoreCase("OrderCancelByAdmin")) {

                        if (messageStr.equalsIgnoreCase("OrderCancelByAdmin")) {
                            final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                            generateAlert.setCancelable(false);
                            generateAlert.setBtnClickList(btn_id -> MyApp.getInstance().restartWithGetDataApp());
                            generateAlert.setContentMessage("", vTitle);
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                            generateAlert.showAlertBox();
                        } else if (messageStr.equalsIgnoreCase("OrderDelivered")) {
                            handleOrderDeliverdDialog(generalFunc, obj_msg, vTitle);
                        } else {

                            generalFunc.showGeneralMessage("", vTitle);
                        }
                    }
                } else {
                    if (!isTripEndDialogShown) {
                        MyApp.getInstance().restartWithGetDataApp();
                       /* final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                        generateAlert.setCancelable(false);
//                    generateAlert.setSystemAlertWindow(true);
                        generateAlert.setBtnClickList(btn_id -> MyApp.getInstance().restartWithGetDataApp());
                        generateAlert.setContentMessage("", vTitle);
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.showAlertBox();*/
                        generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), btn_id -> MyApp.getInstance().restartWithGetDataApp());
                    }
                }
                return;
            } else if (messageStr.equalsIgnoreCase("TripStarted") || messageStr.equalsIgnoreCase("DriverArrived")) {
                generalFunc.showGeneralMessage("", vTitle);
            } else if (messageStr.equalsIgnoreCase("PostApprovedByAdmin") || messageStr.equalsIgnoreCase("PostRejectByAdmin")) {
                generalFunc.showGeneralMessage("", vTitle);
            } else {
                if (messageStr.equalsIgnoreCase("OrderConfirmByRestaurant") || messageStr.equalsIgnoreCase("OrderDeclineByRestaurant") || messageStr.equalsIgnoreCase("OrderPickedup") ||
                        messageStr.equalsIgnoreCase("OrderDelivered") || messageStr.equalsIgnoreCase("OrderCancelByAdmin") || messageStr.equalsIgnoreCase("OrderReviewItems")) {

                    if (Utils.checkText(cNotif) && cNotif.equalsIgnoreCase("Yes") && MyApp.getInstance().isMyAppInBackGround()) {

                        MyApp.getInstance().getLoclaNotificationObj().customNotification(mContext, obj_msg.toString());
                        return;
                    } else if (messageStr.equalsIgnoreCase("OrderDelivered")) {

                        handleOrderDeliverdDialog(generalFunc, obj_msg, vTitle);

                    } else if (messageStr.equalsIgnoreCase("OrderConfirmByRestaurant")) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            generateAlert.closeAlertBox();
                            if (btn_id == 0) {
                                Bundle bn = new Bundle();
                                bn.putString("iOrderId", generalFunc.getJsonValueStr("iOrderId", obj_msg));
                                bn.putString("iServiceId", generalFunc.getJsonValueStr("iServiceId", obj_msg));
                                bn.putBoolean("isDeliverNotify", false);
                            }
                        });
                        generateAlert.setContentMessage("", vTitle);
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_VIEW_DETAILS"));
                        generateAlert.showAlertBox();
                    } else {
                        generalFunc.showGeneralMessage("", vTitle);
                    }
                } else if (messageStr.equalsIgnoreCase("VerifyCharges")) {
                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN"),
                            buttonId -> {
                                if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                                    MyApp.getInstance().restartWithGetDataApp();
                                } else {
                                    String iTripId = generalFunc.getJsonValueStr("iTripId", obj_msg);

                                    if (MyApp.getInstance().additionalChargesAct != null) {
                                        MyApp.getInstance().additionalChargesAct.pubNubMsgArrived(obj_msg.toString(), false);


                                    } else {
                                        Bundle bn = new Bundle();
                                        bn.putBoolean("isRestart", true);
                                        bn.putString("iTripId", iTripId);
                                        bn.putBoolean("fromNoti", true);
                                        new ActUtils(MyApp.getInstance().getCurrentAct()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                                    }
                                }
                            });
                } else if (messageStr.equalsIgnoreCase("VerifyChargesDeclined")) {
                    generalFunc.showGeneralMessage("", vTitle, "", generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN"),
                            buttonId -> {
                                if (eType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                                    MyApp.getInstance().restartWithGetDataApp();
                                }
                            });
                } else if (messageStr.contains("Bidding")) {
                    manageBindViewTypeWise(messageStr, obj_msg);
                } else if (messageStr.equalsIgnoreCase("AcceptPublishRide") || messageStr.equalsIgnoreCase("CancelPublishRide") || messageStr.equalsIgnoreCase("DeclinePublishRide")) {
                    generalFunc.showGeneralMessage("", vTitle);
                }
            }
        }
        if (obj_msg != null && messageStr.equalsIgnoreCase("OutstandingGenerated")) {
            generalFunc.showGeneralMessage("", generalFunc.getJsonValue("vTitle", obj_msg.toString()), "", generalFunc.retrieveLangLBl("", "LBL_BTN_PAYMENT_TXT"), button_Id -> {
                if (button_Id == 1) {
                    Bundle bn = new Bundle();
                    bn.putString("url", generalFunc.getJsonValue("PAYMENT_URL", obj_msg.toString()));
                    bn.putBoolean("handleResponse", true);
                    bn.putBoolean("isBack", false);
                    bn.putBoolean("isApiCall", true);
                    new ActUtils(mContext).startActWithData(PaymentWebviewActivity.class, bn);
                }
            });
        }

/*
        if (obj_msg != null &&  messageStr.equalsIgnoreCase("CabRequestAccepted")) {
            // for manage restart apps when notification come
        } else*/
        if (obj_msg != null && MyApp.getInstance().mainAct != null && (!eType.equalsIgnoreCase(Utils.CabGeneralType_UberX))) {
            if (messageStr.equalsIgnoreCase("CabRequestAccepted")) {
                generalFunc.showGeneralMessage("", vTitle, buttonId -> {
                    MyApp.getInstance().mainAct.pubNubMsgArrived(obj_msg.toString());
                });
            } else {
                MyApp.getInstance().mainAct.pubNubMsgArrived(obj_msg.toString());
            }
        } else if (obj_msg != null && MyApp.getInstance().uberXAct != null && messageStr.equalsIgnoreCase("CabRequestAccepted")) {
            MyApp.getInstance().uberXAct.pubNubMsgArrived(obj_msg.toString());
        } else if (obj_msg != null && MyApp.getInstance().uberXHomeAct != null && messageStr.equalsIgnoreCase("CabRequestAccepted")) {
            MyApp.getInstance().uberXHomeAct.pubNubMsgArrived(obj_msg.toString());
        } else if (obj_msg != null && MyApp.getInstance().rideDeliveryActivity != null && messageStr.equalsIgnoreCase("CabRequestAccepted")) {
            MyApp.getInstance().rideDeliveryActivity.pubNubMsgArrived(obj_msg.toString());
        }
    }

    private void manageBindViewTypeWise(String type, JSONObject obj_msg) {
        GenerateAlertBox alertBox = new GenerateAlertBox(MyApp.getInstance().getCurrentAct());
        alertBox.setContentMessage("", MyApp.getInstance().getAppLevelGeneralFunc().convertNumberWithRTL(MyApp.getInstance().getAppLevelGeneralFunc().getJsonValueStr("vTitle", obj_msg)));
        alertBox.setPositiveBtn(MyApp.getInstance().getAppLevelGeneralFunc().retrieveLangLBl("", "LBL_BTN_OK_TXT"));
        alertBox.setBtnClickList(btn_id -> {
            alertBox.closeAlertBox();
            if (btn_id == 1) {

                switch (type) {
                    case "BiddingTaskReoffered":
                    case "BiddingTaskDeclined":

                    case "BiddingTaskArrived":
                    case "BiddingTaskStarted":
                    case "BiddingTaskOngoing":

                    case "BiddingTaskFinished":
                        Bundle bn = new Bundle();
                        bn.putBoolean("isBid", true);
                        bn.putString("iBiddingPostId", MyApp.getInstance().getAppLevelGeneralFunc().getJsonValueStr("iBiddingPostId", obj_msg));
                        new ActUtils(mContext).startActWithData(RatingActivity.class, bn);
                        return;
                }
                if (MyApp.getInstance().getCurrentAct() instanceof UberXHomeActivity mainActivity) {
                    mainActivity.checkBiddingView(2);


                } else {
                    if (MyApp.getInstance().getCurrentAct() instanceof BookingActivity bookingsActivity) {
                        bookingsActivity.setFrag(2);
                    } else {
                        Intent booksActInt = new Intent(MyApp.getInstance().getApplicationContext(), BookingActivity.class);
                        if (obj_msg != null) {
                            booksActInt.putExtras(MyApp.getInstance().getAppLevelGeneralFunc().createChatBundle(obj_msg));
                        }
                        booksActInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        booksActInt.putExtra("viewPos", 2);
                        MyApp.getInstance().getApplicationContext().startActivity(booksActInt);
                    }
                }
            }
        });
        alertBox.showAlertBox();
    }


    private void doOperations() {
//        MyApp.getInstance().restartWithGetDataApp()
    }

    private void dispatchNotification(String message) {
        Context mLocContext = this.mContext;

        if (mLocContext == null && MyApp.getInstance() != null && MyApp.getInstance().getCurrentAct() == null) {
            mLocContext = MyApp.getInstance().getApplicationContext();
        }

        if (mLocContext != null) {
            GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(mLocContext);

            if (!GeneralFunctions.isJsonObj(message)) {
                LocalNotification.dispatchLocalNotification(mLocContext, message, true);
                return;
            }

            JSONObject obj_msg = generalFunc.getJsonObject(message);

            if (msgHandling(generalFunc, obj_msg)) {
                return;
            }

            String message_str = generalFunc.getJsonValueStr("Message", obj_msg);

            if (message_str.equals("")) {
                String msgType_str = generalFunc.getJsonValueStr("MsgType", obj_msg);

                switch (msgType_str) {
                    case "CHAT":
                        generalFunc.storeData("OPEN_CHAT", obj_msg.toString());
                        String tMessage = generalFunc.getJsonValueStr("tMessage", obj_msg);
                        String tMsgNotification = generalFunc.getJsonValueStr("tMsgNotification", obj_msg);
                        LocalNotification.dispatchLocalNotification(mLocContext, tMsgNotification.trim().equalsIgnoreCase("") ? tMessage : tMsgNotification, false);
                        break;

                    default:
                        LocalNotification.dispatchLocalNotification(mLocContext, generalFunc.getJsonValueStr("vTitle", obj_msg), false);
                }

            } else {
                String pass_msg = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vTitle", obj_msg));
                String cNotif = generalFunc.getJsonValueStr("CustomNotification", obj_msg);
                switch (message_str) {
                    case "AcceptPublishRide", "CancelPublishRide", "DeclinePublishRide", "StartToReturn", "Reached" ->
                            LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                    case "PostApprovedByAdmin", "PostRejectByAdmin" ->
                            LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                    case "RiderShareBooking" -> {
                        LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                        dispatchRiderShareBookingRequest(generalFunc, message);
                    }
                    case "TripCancelledByDriver", "TripCancelled" -> {
                        generalFunc.saveGoOnlineInfo();
                        if (Utils.checkText(cNotif) && cNotif.equalsIgnoreCase("Yes") && MyApp.getInstance().isMyAppInBackGround()) {

                            MyApp.getInstance().getLoclaNotificationObj().customNotification(MyApp.getInstance().getApplicationContext(), obj_msg.toString());
                        } else {
                            LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                        }
                    }
                    case "RideShareStartTrip", "RideSharePickup", "schedulePublishRideRide", "RideShareEndTrip", "RideShareAlert", "DriverArrived", "DestinationAdded", "TripStarted", "TripEnd", "CabRequestAccepted" -> {
                        if (Utils.checkText(cNotif) && cNotif.equalsIgnoreCase("Yes") && MyApp.getInstance().isMyAppInBackGround()) {

                            MyApp.getInstance().getLoclaNotificationObj().customNotification(MyApp.getInstance().getApplicationContext(), obj_msg.toString());

                        } else {
                            LocalNotification.dispatchLocalNotification(mLocContext, pass_msg, false);
                        }
                    }
                    case "OrderDelivered", "OrderPickedup", "OrderConfirmByRestaurant", "OrderDeclineByRestaurant", "OrderCancelByAdmin", "OrderReviewItems" -> {
                        if (Utils.checkText(cNotif) && cNotif.equalsIgnoreCase("Yes") && MyApp.getInstance().isMyAppInBackGround()) {
                            MyApp.getInstance().getLoclaNotificationObj().customNotification(MyApp.getInstance().getApplicationContext(), obj_msg.toString());
                            if (message_str.equalsIgnoreCase("OrderDelivered")) {
                                handleOrderDeliverdDialog(generalFunc, obj_msg, pass_msg);
                            }
                        } else {
                            if (message_str.equalsIgnoreCase("OrderDelivered")) {
                                handleOrderDeliverdDialog(generalFunc, obj_msg, pass_msg);
                            } else {
                                MyApp.getInstance().getLoclaNotificationObj().customNotification(MyApp.getInstance().getApplicationContext(), obj_msg.toString());
                            }
                        }
                    }
                }
            }
        }
    }

    private void dispatchRiderShareBookingRequest(GeneralFunctions generalFunc, String responseString) {


        Bundle bn = new Bundle();
        bn.putSerializable("myRideDataHashMap", MyUtils.createHashMap(generalFunc, new HashMap<>(), generalFunc.getJsonObject("notiData", responseString)));


    }

    private void handleOrderDeliverdDialog(GeneralFunctions generalFunc, JSONObject obj_msg, String msg) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            Bundle bn = new Bundle();
            bn.putString("iOrderId", generalFunc.getJsonValueStr("iOrderId", obj_msg));
            bn.putString("iServiceId", generalFunc.getJsonValueStr("iServiceId", obj_msg));
            bn.putBoolean("isDeliverNotify", true);

        });
        generateAlert.setContentMessage("", msg);
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
        generateAlert.showAlertBox();
    }

    private boolean msgHandling(GeneralFunctions generalFunc, JSONObject obj_msg) {
        String MsgType = generalFunc.getJsonValueStr("MsgType", obj_msg);
        if (MsgType != null) {
            //String messageStr = generalFunc.getJsonValueStr("Message", obj_msg);

            switch (MsgType) {
                case "TwilioVideocall":
                    CommunicationManager.getInstance().incomingCommunicate(mContext, generalFunc, null, obj_msg);
                    return true;
            }
        }
        return false;
    }

    private boolean isTripStatusMsgExist(GeneralFunctions generalFunc, String msg, Context mContext) {

        JSONObject obj_tmp = generalFunc.getJsonObject(msg);

        if (obj_tmp != null) {

            String message = generalFunc.getJsonValueStr("Message", obj_tmp);
            String vConfirmationCode = generalFunc.getJsonValueStr("vConfirmationCode", obj_tmp);
            String randomUniqueCode = generalFunc.getJsonValueStr("iamunique", obj_tmp);
            String Message = generalFunc.getJsonValueStr("Message", obj_tmp);

            if (!message.equals("")) {
                String iTripId = "";
                String iBiddingPostId = "";
                if (generalFunc.getJsonValue("eSystem", msg).equalsIgnoreCase(Utils.eSystem_Type)) {
                    iTripId = generalFunc.getJsonValueStr("iOrderId", obj_tmp);
                } else {
                    iTripId = generalFunc.getJsonValueStr("iTripId", obj_tmp);
                }

                if (generalFunc.getJsonValue("iBiddingPostId", obj_tmp) != null && !generalFunc.getJsonValue("iBiddingPostId", obj_tmp).equals("")) {
                    iBiddingPostId = generalFunc.getJsonValueStr("iBiddingPostId", obj_tmp);
                }
                String iTripDeliveryLocationId = generalFunc.getJsonValueStr("iTripDeliveryLocationId", obj_tmp);
                if (!iTripId.equals("")) {
                    String vTitle = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("vTitle", obj_tmp));
                    String time = generalFunc.getJsonValueStr("time", obj_tmp);
                    String key = "";
                    if (generalFunc.getJsonValue("eType", msg).equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                        key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iTripId + "_" + iTripDeliveryLocationId + "_" + message;
                    } else if (Message.equalsIgnoreCase("VerifyCharges") || Message.equalsIgnoreCase("VerifyChargesDeclined")) {
                        key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iTripId + "_" + vConfirmationCode + "_" + randomUniqueCode + "_" + message;
                    } else {
                        key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iTripId + "_" + message;
                    }
                    if (message.equals("DestinationAdded")) {
                        long newMsgTime = GeneralFunctions.parseLongValue(0, time);

                        String destKeyValueStr = GeneralFunctions.retrieveValue(key, mContext);
                        if (!destKeyValueStr.equals("")) {

                            long destKeyValue = GeneralFunctions.parseLongValue(0, destKeyValueStr);
                            if (newMsgTime > destKeyValue) {
                                generalFunc.removeValue(key);
                            } else {
                                return true;
                            }
                        }
                    }
                    if (tmp_store_key.equalsIgnoreCase(key)) {
                        return true;
                    }
                    tmp_store_key = key;
                    String data = generalFunc.retrieveValue(key);

                    if (data.equals("")) {
                        if (!message.equalsIgnoreCase("TripRequestCancel")) {
                            LocalNotification.dispatchLocalNotification(mContext, vTitle, true);
                        }
                        if (message.equalsIgnoreCase("TripRequestCancel")) {
                            generalFunc.storeData(key + "_" + System.currentTimeMillis(), "" + System.currentTimeMillis());
                        } else {
                            if (time.equals("")) {
                                generalFunc.storeData(key, "" + System.currentTimeMillis());
                            } else {
                                generalFunc.storeData(key, "" + time);
                            }
                        }
                        tmp_store_key = "";
                        return false;
                    } else {
                        tmp_store_key = "";
                        return true;
                    }
                } else if (!iBiddingPostId.equalsIgnoreCase("")) {
                    String time = generalFunc.getJsonValueStr("time", obj_tmp);
                    String key = "";
                    key = Utils.TRIP_REQ_CODE_PREFIX_KEY + iBiddingPostId + "_" + message + "" + time;
                    String data = generalFunc.retrieveValue(key);
                    if (data.equals("")) {
                        if (time.equals("")) {
                            generalFunc.storeData(key, "" + System.currentTimeMillis());
                        } else {
                            generalFunc.storeData(key, "" + time);
                        }
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    String msgType = generalFunc.getJsonValueStr("MsgType", obj_tmp);
                    if (msgType != null) {
                        String key, data, tRandomValue = "";
                        switch (msgType) {
                            case "TwilioVideocall":
                                tRandomValue = generalFunc.getJsonValueStr("tRandomCode", obj_tmp);
                                break;
                            case "PostApprovedByAdmin":
                            case "PostRejectByAdmin":
                                tRandomValue = generalFunc.getJsonValueStr("tRandomCode", obj_tmp);
                                break;
                        }
                        if (Utils.checkText(tRandomValue)) {
                            key = Utils.TRIP_REQ_CODE_PREFIX_KEY + tRandomValue + "_" + msgType;
                            data = generalFunc.retrieveValue(key);
                            generalFunc.storeData(key, "" + System.currentTimeMillis());
                            return !data.equals("");
                        }
                    }
                }
            } else {
                String msgType = generalFunc.getJsonValueStr("MsgType", obj_tmp);
                if (msgType != null) {
                    String key, data, tRandomValue = "";
                    switch (msgType) {
                        case "TripRequestCancel":
                            tRandomValue = generalFunc.getJsonValueStr("iTripId", obj_tmp);
                            break;
                        case "Notification":
                            tRandomValue = generalFunc.getJsonValueStr("tRandomCode", obj_tmp);
                            break;
                    }
                    if (Utils.checkText(tRandomValue)) {
                        key = Utils.TRIP_REQ_CODE_PREFIX_KEY + tRandomValue + "_" + msgType;
                        data = generalFunc.retrieveValue(key);
                        generalFunc.storeData(key, "" + System.currentTimeMillis());
                        return !data.equals("");
                    }
                }
            }
        }
        return false;
    }

    private void showMultiPubnubGeneralMessage(GeneralFunctions generalFunc, final JSONObject msg_Obj, final boolean isMultirate) {
        try {
            String message = generalFunc.getJsonValueStr("vTitle", msg_Obj);

            final GenerateAlertBox generateAlert = new GenerateAlertBox(MyApp.getInstance().getCurrentAct());
            generateAlert.setContentMessage("", message);
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));

            if (generalFunc.getJsonValueStr("eType", msg_Obj).equalsIgnoreCase(Utils.eType_Multi_Delivery) && generalFunc.getJsonValueStr("Is_Last_Delivery", msg_Obj).equalsIgnoreCase("Yes")) {
                generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
            }

            generateAlert.setBtnClickList(btn_id -> {
                generateAlert.closeAlertBox();
                if (mContext instanceof MainActivity) {
                    if (((MainActivity) mContext).driverAssignedHeaderFrag != null && ((MainActivity) mContext).driverAssignedHeaderFrag.backImgView != null) {
                        ((MainActivity) mContext).driverAssignedHeaderFrag.backImgView.performClick();
                    }
                }
                if (btn_id == 0) {
                    return;
                } else if (btn_id == 1 && isMultirate) {
                    Bundle bn = new Bundle();
                    bn.putBoolean("isUfx", false);
                    bn.putString("iTripId", generalFunc.getJsonValueStr("iTripId", msg_Obj));

                    if (!Utils.checkText(generalFunc.getJsonValueStr("iTripId", msg_Obj))) {
                        return;
                    }
                    new ActUtils(mContext).startActForResult(HistoryDetailActivity.class, bn, Utils.MULTIDELIVERY_HISTORY_RATE_CODE);
                }
            });
            generateAlert.showAlertBox();
        } catch (Exception e) {
            Logger.d("AlertEx", e.toString());
        }
    }

    private void showPubnubGeneralMessage(GeneralFunctions generalFunc, final String iTripId, final String message, final boolean isrestart, final boolean isufxrate) {
        try {
            final GenerateAlertBox generateAlert = new GenerateAlertBox(MyApp.getInstance().getCurrentAct());
            generateAlert.setContentMessage("", message);
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("Ok", "LBL_BTN_OK_TXT"));
            generateAlert.setBtnClickList(btn_id -> {
                generateAlert.closeAlertBox();
                if (isrestart) {
                    MyApp.getInstance().restartWithGetDataApp();
                }
                if (isufxrate) {
                    Bundle bn = new Bundle();
                    bn.putBoolean("isUfx", true);
                    bn.putString("iTripId", iTripId);
                    new ActUtils(mContext).startActWithData(RatingActivity.class, bn);
                }
            });
            generateAlert.showAlertBox();
        } catch (Exception e) {
            Logger.d("AlertEx", e.toString());
        }
    }
}