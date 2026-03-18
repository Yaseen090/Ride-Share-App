package com.tiktak24.user;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import com.activity.ParentActivity;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.tiktak24.user.databinding.ActivityStaticPageBinding;
import com.service.handler.ApiHandler;
import com.utils.Utils;
import com.view.MTextView;

import org.json.JSONObject;

import java.util.HashMap;

public class StaticPageActivity extends ParentActivity {
    private ActivityStaticPageBinding binding;

    public String static_page_id = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_static_page);

        static_page_id = getIntent().getStringExtra("staticpage");

        initViews();
    }

    private void initViews() {
        ImageView backImgView = findViewById(R.id.backImgView);
        if (generalFunc.isRTLmode()) {
            backImgView.setRotation(180);
        }
        addToClickHandler(backImgView);
        MTextView titleTxt = findViewById(R.id.titleTxt);

        String staticData = getIntent().getStringExtra("staticData");
        if (Utils.checkText(staticData)) {
            titleTxt.setText(getIntent().getStringExtra("vTitle"));
            loadAboutUsDetail(staticData);
        } else {
            if (static_page_id.equalsIgnoreCase("1")) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ABOUT_US_HEADER_TXT"));

            } else if (static_page_id.equalsIgnoreCase("33")) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PRIVACY_POLICY_TEXT"));

            } else if (static_page_id.equals("4")) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TERMS_AND_CONDITION"));

            } else {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DETAILS"));
            }
            loadAboutUsData();
        }
    }

    private void loadAboutUsData() {
        if (binding.errorView.getVisibility() == View.VISIBLE) {
            binding.errorView.setVisibility(View.GONE);
        }
        if (binding.loading.getVisibility() != View.VISIBLE) {
            binding.loading.setVisibility(View.VISIBLE);
        }

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "staticPage");
        parameters.put("iPageId", static_page_id);
        parameters.put("appType", Utils.app_type);
        parameters.put("iMemberId", generalFunc.getMemberId());

        if (generalFunc.getMemberId().equalsIgnoreCase("")) {
            parameters.put("vLangCode", generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY));
        }

        ApiHandler.execute(getActContext(), parameters, responseString -> {
            closeLoader();
            JSONObject responseObj = generalFunc.getJsonObject(responseString);
            if (responseObj != null && !responseObj.toString().equals("")) {
                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseObj)) {
                    String message = generalFunc.getJsonValueStr(Utils.message_str, responseObj);
                    loadAboutUsDetail(generalFunc.getJsonValue("tPageDesc", message));
                } else {
                    loadAboutUsDetail(generalFunc.getJsonValueStr("page_desc", responseObj));
                }
            } else {
                generateErrorView();
            }
        });
    }

    private void loadAboutUsDetail(String tPageDesc) {
        WebView view = new WebView(this);
        binding.container.addView(view);
        view.setOnTouchListener((v, event) -> {
            if (event.getPointerCount() > 1) {
                return true;
            } else {
                return false;
            }
        });
        MyApp.executeWV(view, generalFunc, tPageDesc);
    }

    private void closeLoader() {
        if (binding.loading.getVisibility() == View.VISIBLE) {
            binding.loading.setVisibility(View.GONE);
        }
    }

    private void generateErrorView() {
        closeLoader();
        generalFunc.generateErrorView(binding.errorView, "LBL_ERROR_TXT", "LBL_NO_INTERNET_TXT");
        if (binding.errorView.getVisibility() != View.VISIBLE) {
            binding.errorView.setVisibility(View.VISIBLE);
        }
        binding.errorView.setOnRetryListener(this::loadAboutUsData);
    }

    private Context getActContext() {
        return StaticPageActivity.this;
    }

    public void onClick(View view) {
        Utils.hideKeyboard(StaticPageActivity.this);
        if (view.getId() == R.id.backImgView) {
            StaticPageActivity.super.onBackPressed();
        }
    }
}