package com.tiktak24.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.activity.ParentActivity;
import com.fragments.ChatDataFragment;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tiktak24.user.databinding.ActivityChatBinding;
import com.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


@SuppressLint("all")
public class ChatActivity extends ParentActivity {

    ActivityChatBinding binder;

    HashMap<String, ChatDataFragment> fragMap = new HashMap<>();
    ArrayList<String> fragTagsLst = new ArrayList<>();

    public ChatDataFragment currentVisibleFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        continueExecution();
    }

    private void continueExecution() {
        HashMap<String, String> dataMap = new HashMap<>();

        dataMap.put("iTripId", getIntent().getStringExtra("iTripId") != null ? getIntent().getStringExtra("iTripId") : "");
        dataMap.put("iBiddingPostId", getIntent().getStringExtra("iBiddingPostId") != null ? getIntent().getStringExtra("iBiddingPostId") : "");
        dataMap.put("vBookingNo", getIntent().getStringExtra("vBookingNo") != null ? getIntent().getStringExtra("vBookingNo") : "");
        dataMap.put("iOrderId", getIntent().getStringExtra("iOrderId") != null ? getIntent().getStringExtra("iOrderId") : "");
        dataMap.put("iToMemberType", getIntent().getStringExtra("iToMemberType") != null ? getIntent().getStringExtra("iToMemberType") : "");
        dataMap.put("iToMemberId", getIntent().getStringExtra("iToMemberId") != null ? getIntent().getStringExtra("iToMemberId") : "");

        replaceFragment(dataMap);
    }

    private void replaceFragment(HashMap<String, String> dataMap) {
        if (dataMap == null) {
            generalFunc.showError(true);
            return;
        }

        String iBiddingPostId = dataMap.get("iBiddingPostId");
        String iToMemberId = dataMap.get("iToMemberId");
        String iToMemberType = dataMap.get("iToMemberType");
        String iTripId = dataMap.get("iTripId");
        String iOrderId = dataMap.get("iOrderId");

        if (currentVisibleFrag != null) {

            boolean isSameBidTask = Utils.checkText(iBiddingPostId) && currentVisibleFrag.dataMap.get("iBiddingPostId").equalsIgnoreCase(iBiddingPostId) && currentVisibleFrag.dataMap.get("iToMemberId").equalsIgnoreCase(iToMemberId);

            boolean isSameService = !Utils.checkText(iBiddingPostId) && Utils.checkText(iTripId) && currentVisibleFrag.dataMap.get("iTripId").equalsIgnoreCase(iTripId);

            boolean isSameOrder = Utils.checkText(iOrderId) && currentVisibleFrag.dataMap.get("iOrderId").equalsIgnoreCase(iOrderId) && currentVisibleFrag.dataMap.get("iToMemberType").equalsIgnoreCase(iToMemberType);

            if (isSameBidTask || isSameService || isSameOrder) {
                currentVisibleFrag.handleIncomingMessages(dataMap);
                return;
            }
        }

        String frag_tag;
        if (Utils.checkText(iBiddingPostId)) {
            frag_tag = "ServiceBid_" + iBiddingPostId + "_" + iToMemberId;
        } else if (Utils.checkText(iTripId)) {
            frag_tag = "Service_" + iTripId;
        } else if (Utils.checkText(iOrderId)) {
            frag_tag = "Order_" + iOrderId + "_" + iToMemberType + "_" + iToMemberId;
        } else {
            frag_tag = "Service";
        }


        ChatDataFragment chatDataFrag;

        if (fragMap.get(frag_tag) != null) {
            chatDataFrag = fragMap.get(frag_tag);
            fragMap.remove(frag_tag);
        } else {
            chatDataFrag = new ChatDataFragment(dataMap);
        }

        String finalFrag_tag = frag_tag;
        fragTagsLst.removeIf(value -> value.equalsIgnoreCase(finalFrag_tag));

        fragTagsLst.add(frag_tag);


        fragMap.put(frag_tag, chatDataFrag);

        this.currentVisibleFrag = chatDataFrag;

        if (chatDataFrag != null) {
            getSupportFragmentManager().beginTransaction().replace(binder.fragContainer.getId(), chatDataFrag, frag_tag).commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {

        int frag_size = fragMap.size();

        if (frag_size == 1) {
            super.getOnBackPressedDispatcher().onBackPressed();
        } else {
            fragMap.remove(currentVisibleFrag.getTag());
            fragTagsLst.remove(fragTagsLst.size() - 1);

            currentVisibleFrag = null;

            String lastTag = fragTagsLst.get(fragTagsLst.size() - 1);

            ChatDataFragment frag = fragMap.get(lastTag);
            replaceFragment(frag.dataMap);
        }

    }

    public Context getActContext() {
        return ChatActivity.this;
    }

    public void handleIncomingMessages(JSONObject obj_data) {
        replaceFragment(new Gson().fromJson(obj_data.toString(), new TypeToken<HashMap<String, String>>() {
        }.getType()));
    }
}
