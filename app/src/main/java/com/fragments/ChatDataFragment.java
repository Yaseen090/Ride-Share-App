package com.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.databinding.DataBindingUtil;

import com.activity.ParentActivity;
import com.adapter.files.ChatMessagesRecycleAdapter;
import com.general.files.GeneralFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiktak24.user.ChatActivity;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.FragmentChatDataBinding;
import com.model.SocketEvents;
import com.service.handler.ApiHandler;
import com.service.handler.AppService;
import com.utils.LoadImage;
import com.utils.Logger;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatDataFragment extends BaseFragment implements TextWatcher, ViewTreeObserver.OnGlobalLayoutListener {

    FragmentChatDataBinding binder;

    private ChatMessagesRecycleAdapter chatAdapter;
    private ArrayList<HashMap<String, String>> list_msgs = new ArrayList<>();

    String vBookingNo = "";

    public HashMap<String, String> dataMap;

    JSONObject obj_data;

    boolean isChatHistoryLoaded = false;

    String eServiceType = "";

    String vImage;

    GeneralFunctions generalFunc;

    ChatActivity chatAct;

    View view;

    public ChatDataFragment(HashMap<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_data, container, false);

        chatAct = (ChatActivity) getActivity();

        chatAct.currentVisibleFrag = this;

        generalFunc = new GeneralFunctions(getActContext());

        vImage = generalFunc.getJsonValueStr("vImgName", ((ParentActivity) getActivity()).obj_userProfile);

        binder.chatParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        initView();

        view = binder.getRoot();

        return view;
    }

    private Context getActContext() {
        return getActivity();
    }

    @SuppressLint("SetTextI18n")
    public void initView() {

        binder.msgbtn.setImageResource(R.drawable.ic_chat_send_disable);

        binder.input.addTextChangedListener(this);

        binder.input.setHint(generalFunc.retrieveLangLBl("Enter a message", "LBL_ENTER_MESSAGE"));

        if (generalFunc.isRTLmode()) {
            binder.toolbarInclude.backImgView.setRotation(180);
        }

        binder.toolbarInclude.backImgView.setOnClickListener(v -> {
            Utils.hideKeyboard(getActContext());
            getActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        binder.msgbtn.setOnClickListener(new setOnClickList());

        chatAdapter = new ChatMessagesRecycleAdapter(getActContext(), list_msgs, generalFunc);
        binder.chatCategoryRecyclerView.setAdapter(chatAdapter);

        if (dataMap.get("vBookingNo") != null && !dataMap.get("vBookingNo").trim().equalsIgnoreCase("")) {
            binder.toolbarInclude.titleTxt.setText("#" + generalFunc.convertNumberWithRTL(dataMap.get("vBookingNo")));
        }

        getChatHistory();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getChatHistory() {

        binder.mainArea.setVisibility(View.GONE);
        binder.progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getMessageHistory");
        parameters.put("iFromMemberType", Utils.userType);
        parameters.put("iToMemberType", dataMap.get("iToMemberType"));
        parameters.put("iToMemberId", dataMap.get("iToMemberId"));

        parameters.put("iBiddingPostId", dataMap.get("iBiddingPostId"));
        parameters.put("iTripId", dataMap.get("iTripId"));
        parameters.put("iOrderId", dataMap.get("iOrderId"));

        ApiHandler.execute(getActContext(), parameters, responseString -> {
            if (responseString == null || responseString.trim().equals("")) {
                generalFunc.showError(i -> getActivity().getOnBackPressedDispatcher().onBackPressed());
                return;
            }

            JSONObject objData = generalFunc.getJsonObject(responseString.toString());

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, objData);
            if (isDataAvail) {
                JSONArray msgsArr = generalFunc.getJsonArray("data", objData);

                if (msgsArr != null) {
                    for (int i = 0; i < msgsArr.length(); i++) {
                        JSONObject obj_data = generalFunc.getJsonObject(msgsArr, i);

                        HashMap<String, String> dataMap = new Gson().fromJson(obj_data.toString(), new TypeToken<HashMap<String, String>>() {
                        }.getType());
                        list_msgs.add(dataMap);

                    }

                    chatAdapter.notifyDataSetChanged();

                    binder.chatCategoryRecyclerView.scrollToPosition(list_msgs.size() - 1);
                }


                JSONObject SERVICE_DATA_OBJ = generalFunc.getJsonObject("SERVICE_DATA", objData);

                ChatDataFragment.this.obj_data = SERVICE_DATA_OBJ;

                JSONObject memberData = generalFunc.getJsonObject("MemberData", SERVICE_DATA_OBJ);
                JSONObject serviceData = generalFunc.getJsonObject("ServiceData", SERVICE_DATA_OBJ);

                String vBookingNoTMP = generalFunc.getJsonValueStr("vBookingNo", serviceData);
                String vRideNoTMP = generalFunc.getJsonValueStr("vRideNo", serviceData);

                vBookingNo = vBookingNoTMP.trim().equalsIgnoreCase("") ? vRideNoTMP : vBookingNoTMP;

                Logger.e("JSON_DATA", ":" + memberData.toString());

                binder.userNameTxt.setText(generalFunc.getJsonValueStr("vName", memberData));
                binder.catTypeText.setText(generalFunc.getJsonValueStr("vServiceName", serviceData));
                eServiceType = generalFunc.getJsonValueStr("eType", serviceData);

                new LoadImage.builder(LoadImage.bind(generalFunc.getJsonValueStr("vImage", memberData)), binder.userImgView).setErrorImagePath(R.mipmap.ic_no_pic_user).setPlaceholderImagePath(R.mipmap.ic_no_pic_user).build();

                binder.driverRating.setText(generalFunc.getJsonValueStr("vAvgRating", memberData));
                binder.toolbarInclude.titleTxt.setText("#" + generalFunc.convertNumberWithRTL(vBookingNo));

                binder.toolbarInclude.chatsubtitleTxt.setVisibility(View.VISIBLE);
                binder.toolbarInclude.chatsubtitleTxt.setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("tTripRequestDate", serviceData)));

                isChatHistoryLoaded = true;

                unHideMainView();

            } else {
                generalFunc.showError(i -> getActivity().getOnBackPressedDispatcher().onBackPressed());
            }
        });
    }

    public void unHideMainView() {
        if (obj_data != null && isChatHistoryLoaded) {
            binder.progressBar.setVisibility(View.GONE);
            binder.mainArea.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onResume() {
        super.onResume();

        chatAct.currentVisibleFrag = this;

        Logger.e("CHA_ACT_BACK", ":onResume:" + this.getTag());
    }


    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 0) {
            binder.msgbtn.setImageResource(R.drawable.ic_chat_send_disable);
        } else {
            binder.msgbtn.setImageResource(R.drawable.ic_chat_send);
        }
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        binder.chatParentLayout.getWindowVisibleDisplayFrame(r);
        int screenHeight = binder.chatParentLayout.getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;

        if (keypadHeight > screenHeight * 0.15) {
            binder.detailArea.setVisibility(View.GONE);
            binder.catTypeText.setVisibility(View.GONE);
        } else {
            binder.detailArea.setVisibility(View.VISIBLE);
            binder.catTypeText.setVisibility(View.VISIBLE);
        }
    }

    public class setOnClickList implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.msgbtn:
                    if (Utils.checkText(binder.input) && Utils.getText(binder.input).length() > 0) {

                        HashMap<String, String> dataMap = new HashMap<>();
                        dataMap.put("iFromMemberId", generalFunc.getMemberId());
                        dataMap.put("iFromMemberType", Utils.app_type);
                        dataMap.put("iFromMemberImage", vImage);

                        dataMap.put("iToMemberId", generalFunc.getJsonValueStr("iMemberId", generalFunc.getJsonObject("MemberData", obj_data)));
                        dataMap.put("iToMemberType", generalFunc.getJsonValueStr("iMemberType", generalFunc.getJsonObject("MemberData", obj_data)));

                        dataMap.put("iTripId", ChatDataFragment.this.dataMap.get("iTripId"));
                        dataMap.put("iBiddingPostId", ChatDataFragment.this.dataMap.get("iBiddingPostId"));
                        dataMap.put("iOrderId", ChatDataFragment.this.dataMap.get("iOrderId"));

                        dataMap.put("tMessage", Utils.getText(binder.input));
                        dataMap.put("vBookingNo", "" + vBookingNo);
                        dataMap.put("eServiceType", "" + eServiceType);

                        binder.msgbtn.setEnabled(false);
                        binder.input.setEnabled(false);

                        Logger.e("JSON_DATA", "::" + (new Gson()).toJson(dataMap).toString());

                        AppService.getInstance().sendMessage(SocketEvents.CHAT_SERVICE, (new Gson()).toJson(dataMap), 10000, (name, errorObj, dataObj) -> {

                            if (errorObj == null) {
                                try {
                                    MediaPlayer.create(getActContext(), R.raw.chat_msg_sent).start();
                                } catch (Exception ignored) {

                                }

                                binder.input.setText("");
                            } else {
                                if (!chatAct.isFinishing()) {
                                    generalFunc.showMessage(generalFunc.getCurrentView((Activity) getActContext()), generalFunc.retrieveLangLBl("We're unable to communicate with the server. Please check your internet connection.", "LBL_TRY_AGAIN_LATER_TXT"));
                                }
                            }

                            binder.input.setEnabled(true);
                            binder.msgbtn.setEnabled(true);

                        });
                    }
                    break;
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void handleIncomingMessages(HashMap<String, String> dataMap) {
        if (!isChatHistoryLoaded) {
            return;
        }

        list_msgs.add(dataMap);

        chatAdapter.notifyDataSetChanged();

        binder.chatCategoryRecyclerView.scrollToPosition(list_msgs.size() - 1);

        if (dataMap.get("isPlaySound") != null && dataMap.get("isPlaySound").equalsIgnoreCase("Yes")) {
            try {
                MediaPlayer.create(getActContext(), R.raw.chat_msg_received).start();
            } catch (Exception ignored) {

            }
        }
    }
}