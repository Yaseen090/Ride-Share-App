package com.tiktak24.user.homescreen23;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.adapter.files.CommonBanner23Adapter;
import com.general.files.GeneralFunctions;
import com.general.files.SpacesItemDecoration;
import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.ActivityUberXhome23Binding;
import com.tiktak24.user.homescreen23.adapter.UFXSubCategory23ProAdapter;
import com.service.handler.ApiHandler;
import com.utils.Utils;
import com.view.MButton;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UFXServices23ProView {

    private final UberXHomeActivity mActivity;
    private final GeneralFunctions generalFunc;
    private final ActivityUberXhome23Binding binding;
    private final UFXSubCategory23ProAdapter ufxCatAdapter;

    //    private final Banner23Adapter banner23Adapter;
    private final CommonBanner23Adapter commonBanner23Adapter;
    private final OnUFXServiceViewListener listener;
    private PagerSnapHelper mSnapHelper = null;

    private JSONObject mResponseObject = new JSONObject();
    private JSONArray mServiceArray = new JSONArray();

    private final ArrayList<String> multiServiceSelect = new ArrayList<>();
    private final ArrayList<String> multiServiceCategorySelect = new ArrayList<>();
    private boolean isBidding = false, isVideoConsultEnable = false;
    private JSONObject mDataObject;
    private JSONArray mBannerArray;
    private String iVehicleCategoryId;

    public UFXServices23ProView(@NonNull UberXHomeActivity activity, @NonNull GeneralFunctions generalFunc, @NonNull ActivityUberXhome23Binding activityUberXhome23Binding, JSONObject dataObject, OnUFXServiceViewListener listener) {
        this.mActivity = activity;
        this.generalFunc = generalFunc;
        this.binding = activityUberXhome23Binding;
        this.listener = listener;
        if (dataObject.has("imagesArr")) {
            this.mBannerArray = mActivity.generalFunc.getJsonArray("imagesArr", dataObject);
        } else if (dataObject.has("BANNER_DATA")) {
            this.mBannerArray = mActivity.generalFunc.getJsonArray("BANNER_DATA", dataObject);
        }
//        this.banner23Adapter = new Banner23Adapter(mActivity, mResponseObject, (position, jsonObject) -> {
//
//        });
        this.commonBanner23Adapter = new CommonBanner23Adapter(mActivity, generalFunc, mBannerArray);
//        binding.UFX23ProArea.rvBanner.setAdapter(banner23Adapter);
        if (mSnapHelper == null) {
            mSnapHelper = new PagerSnapHelper();
            mSnapHelper.attachToRecyclerView(binding.UFX23ProArea.rvBanner);
        }
        binding.UFX23ProArea.rvBanner.setAdapter(commonBanner23Adapter);

        this.ufxCatAdapter = new UFXSubCategory23ProAdapter(activity, generalFunc, mServiceArray, new UFXSubCategory23ProAdapter.OnItemClickList() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onMultiItem(String id, String category, boolean b) {
                if (b) {
                    multiServiceSelect.add(id);
                    multiServiceCategorySelect.add(category);
                } else {
                    multiServiceSelect.remove(id);
                    multiServiceCategorySelect.remove(category);
                }
            }
        });
        binding.UFX23ProArea.rvUFXServices.addItemDecoration(new SpacesItemDecoration(1, mActivity.getResources().getDimensionPixelSize(R.dimen._12sdp), false));
        binding.UFX23ProArea.rvUFXServices.setAdapter(ufxCatAdapter);

        //
        MButton btn_type2 = ((MaterialRippleLayout) binding.UFX23ProArea.btnType2).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
        btn_type2.setOnClickListener(v -> {

            if (mActivity.latitude.equalsIgnoreCase("0.0") || mActivity.longitude.equalsIgnoreCase("0.0")) {
                generalFunc.showMessage(binding.topArea, generalFunc.retrieveLangLBl("", "LBL_SET_LOCATION"));
            } else {
                String SelectedVehicleTypeId, SelectedCategoryName;
                if (multiServiceSelect.size() > 0) {
                    SelectedVehicleTypeId = android.text.TextUtils.join(",", multiServiceSelect);
                    SelectedCategoryName = android.text.TextUtils.join(",", multiServiceCategorySelect);
                } else {
                    generalFunc.showMessage(binding.topArea, generalFunc.retrieveLangLBl("", "LBL_SELECT_SERVICE_TXT"));
                    return;
                }
                listener.onSubmitButtonClick(SelectedVehicleTypeId, iVehicleCategoryId, SelectedCategoryName, isVideoConsultEnable, isBidding);
            }
        });

        //
        initializeView(dataObject);
    }

    public void initializeView(JSONObject dataObject) {

        mDataObject = dataObject;

        binding.Main23ProArea.setVisibility(View.GONE);
        binding.UFX23ProSPArea.getRoot().setVisibility(View.GONE);
        binding.UFX23ProArea.getRoot().setVisibility(View.VISIBLE);

        binding.UFX23ProArea.headerAddressTxt.setText(binding.headerAddressTxt.getText().toString());
        binding.UFX23ProArea.selectServiceTxt.setVisibility(View.GONE);

        multiServiceSelect.clear();
        multiServiceCategorySelect.clear();

        if (mServiceArray != null) {
            mServiceArray = null;
            mServiceArray = new JSONArray();
            ufxCatAdapter.updateList(mServiceArray, false, false);
        }

        initializeView();
    }

    public void initializeView() {
        getSubCateGoryListCateGoryWise();
    }

    @SuppressLint({"SetTextI18n"})
    private void getSubCateGoryListCateGoryWise() {
        this.iVehicleCategoryId = generalFunc.getJsonValueStr("iVehicleCategoryId", mDataObject);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "getServiceCategories");
        parameters.put("parentId", iVehicleCategoryId);
        parameters.put("userId", generalFunc.getMemberId());
        parameters.put("vLatitude", mActivity.latitude);
        parameters.put("vLongitude", mActivity.longitude);

        String eCatType = generalFunc.getJsonValueStr("eCatType", mDataObject);
        isBidding = eCatType != null && eCatType.equalsIgnoreCase("Bidding");
        if (isBidding) {
            parameters.put("eCatType", eCatType);
            parameters.put("parentId", "" + generalFunc.getJsonValueStr("iBiddingId", mDataObject));
        }

        isVideoConsultEnable = mDataObject.has("isVideoConsultEnable") && generalFunc.getJsonValueStr("isVideoConsultEnable", mDataObject).equalsIgnoreCase("Yes");
        if (isVideoConsultEnable) {
            parameters.put("eForVideoConsultation", "Yes");
        }
        binding.UFX23ProArea.btnArea.setVisibility(View.GONE);
        listener.onProcess(true);

        ApiHandler.execute(mActivity, parameters, responseString -> {
            listener.onProcess(false);
            // data get Done

            if (responseString != null && !responseString.equals("")) {
                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {
                    binding.UFX23ProArea.btnArea.setVisibility(View.VISIBLE);
                    binding.UFX23ProArea.selectServiceTxt.setVisibility(View.VISIBLE);
                    binding.UFX23ProArea.selectServiceTxt.setText("" + generalFunc.getJsonValue("vParentCategoryName", responseString));

                    mResponseObject = generalFunc.getJsonObject(responseString);
//                    banner23Adapter.updateData(mResponseObject);

                    if (mResponseObject.has("imagesArr")) {
                        this.mBannerArray = mActivity.generalFunc.getJsonArray("imagesArr", mResponseObject);
                    } else if (mResponseObject.has("BANNER_DATA")) {
                        this.mBannerArray = mActivity.generalFunc.getJsonArray("BANNER_DATA", mResponseObject);
                    }
                    commonBanner23Adapter.updateData(mBannerArray);
                    mServiceArray = generalFunc.getJsonArray(Utils.message_str, responseString);
                    if (mServiceArray != null) {

                        boolean isMultiSelect = false, isRadioSelection = false;
                        if (generalFunc.getJsonValueStr("SERVICE_PROVIDER_FLOW", mActivity.obj_userProfile).equalsIgnoreCase("PROVIDER")) {
                            isMultiSelect = true;
                        }
                        if (isBidding || isVideoConsultEnable) {
                            isMultiSelect = false;
                            isRadioSelection = true;
                        }
                        ufxCatAdapter.updateList(mServiceArray, isMultiSelect, isRadioSelection);
                    }
                } else {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                }
            } else {
                generalFunc.showError();
            }
            listener.onProcess(false);
        });
    }

    public interface OnUFXServiceViewListener {
        void onProcess(boolean isLoadingView);

        void onSubmitButtonClick(String selectedVehicleTypeId, String selectedCategoryName, String categoryName, boolean isVideoConsultEnable, boolean isBidding);
    }
}