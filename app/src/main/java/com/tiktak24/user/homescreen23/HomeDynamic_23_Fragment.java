package com.tiktak24.user.homescreen23;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dialogs.OpenListView;
import com.fragments.BaseFragment;
import com.general.SkeletonViewHandler;
import com.general.files.ActUtils;
import com.general.files.GeneralFunctions;
import com.general.files.GetAddressFromLocation;
import com.general.files.GetLocationUpdates;
import com.general.files.MyApp;
import com.general.files.OpenNoLocationView;
import com.general.files.showTermsDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.model.ServiceModule;
import com.service.handler.ApiHandler;
import com.service.server.ServerTask;
import com.skyfishjy.library.RippleBackground;
import com.tiktak24.user.CommonIntroActivity;
import com.tiktak24.user.InformationActivity;
import com.tiktak24.user.MainActivity;
import com.tiktak24.user.MyProfileActivity;
import com.tiktak24.user.R;
import com.tiktak24.user.SearchLocationActivity;
import com.tiktak24.user.SearchServiceActivity;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.ActivityUberXhome23Binding;
import com.tiktak24.user.databinding.DialogMore23ProBinding;
import com.tiktak24.user.homescreen23.adapter.Main23Adapter;
import com.tiktak24.user.homescreen23.adapter.Main23DeliverAllAdapter;
import com.tiktak24.user.homescreen23.adapter.MoreService23Adapter;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.MyUtils;
import com.utils.Utils;
import com.view.MTextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HomeDynamic_23_Fragment extends BaseFragment implements GetLocationUpdates.LocationUpdates, GetAddressFromLocation.AddressFound {

    private static final int UBER_X_SEARCH_SERVICE_REQ_CODE = 201;
    public ActivityUberXhome23Binding binding;
    private UberXHomeActivity mActivity;
    private GeneralFunctions generalFunc;
    private JSONObject mUserProfileObj;

    private AppCompatDialog noSourceLocationDialog;
    private BottomSheetDialog moreDialog;
    private MoreService23Adapter moreS23Adapter;

    private ServerTask currentCallExeWebServer;
    private JSONArray homeScreenDataArray = new JSONArray();
    private Main23Adapter main23Adapter;
    private Main23DeliverAllAdapter main23DeliverAllAdapter;

    private GetLocationUpdates getLastLocation;
    private GetAddressFromLocation getAddressFromLocation;

    public String CAT_TYPE_MODE = "0";
    private String UBERX_PARENT_CAT_ID = "";
    private String mAddress = "", mLatitude = "0.0", mLongitude = "0.0";

    public boolean isUfxAddress = false;
    public UFXServices23ProView mUFXServices23ProView;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private ArrayList<HashMap<String, String>> newListingArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_uber_xhome_23, container, false);
        newListingArrayList = new ArrayList<>();

        isUfxAddress = false;

        if (getArguments() != null) {
            mAddress = getArguments().getString("uberXAddress");
            mLatitude = getArguments().getString("uberXlat");
            mLongitude = getArguments().getString("uberXlong");
            if (!mLatitude.equalsIgnoreCase("0.0") && !mLongitude.equalsIgnoreCase("0.0")) {
                isUfxAddress = true;
                binding.headerAddressTxt.setText(mAddress);
                binding.UFX23ProArea.headerAddressTxt.setText(mAddress);
                binding.UFX23ProSPArea.headerAddressTxt.setText(mAddress);
                binding.UFX23ProDeliverAllArea.headerAddressTxt.setText(mAddress);
                binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setText(mAddress);

            }
        }

        if (generalFunc.isDeliverOnlyEnabled() && ServiceModule.DeliverAll) {
            if (!isUfxAddress) {
                generalFunc.isLocationPermissionGranted(true);
            }
        } else {
            if (!generalFunc.retrieveValue("isSmartLoginEnable").equalsIgnoreCase("Yes") ||
                    generalFunc.retrieveValue("isFirstTimeSmartLoginView").equalsIgnoreCase("Yes")) {
                if (!isUfxAddress) {
                    generalFunc.isLocationPermissionGranted(true);
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////
        initializeView();

        if (CAT_TYPE_MODE.equalsIgnoreCase("0")) {
            if (generalFunc.isDeliverOnlyEnabled() && ServiceModule.DeliverAll) {
                if (Utils.checkText(generalFunc.retrieveValue("SERVICE_HOME_DATA"))) {
                    manageHomeScreenView(generalFunc.retrieveValue("SERVICE_HOME_DATA"));
                } else {
                    getCategory(true);
                }
            } else {
                if (Utils.checkText(generalFunc.retrieveValue("SERVICE_HOME_DATA_23"))) {
                    manageHomeScreenView(generalFunc.retrieveValue("SERVICE_HOME_DATA_23"));
                } else {
                    getCategory(true);
                }
            }
        }
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                        JSONObject dataObject = null;
                        try {
                            dataObject = new JSONObject(data.getStringExtra("serviceDataObject"));
                        } catch (JSONException e) {
                            Logger.e("Exception", "::" + e.getMessage());
                        }
                        if (dataObject != null) {
                            onItemClickHandle(0, dataObject);
                        }
                    }
                });
        return binding.getRoot();
    }

    private void initializeView() {
        binding.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProSPArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProDeliverAllArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.searchTxtView.setHint(generalFunc.retrieveLangLBl("", "LBL_SEARCH_SERVICES"));
        binding.UFX23ProSPArea.searchTxtView.setHint(generalFunc.retrieveLangLBl("", "LBL_SEARCH_SERVICES"));
        binding.headerAddressTxt.setOnClickListener(new setOnClickLst());
        binding.searchArea.setOnClickListener(new setOnClickLst());
        binding.UFX23ProSPArea.searchArea.setOnClickListener(new setOnClickLst());
        binding.userImgView.setOnClickListener(new setOnClickLst());
        binding.userProfileView.setOnClickListener(new setOnClickLst());

        // UFX View
        binding.UFX23ProArea.backImgView.setOnClickListener(new setOnClickLst());
        binding.UFX23ProArea.headerAddressTxt.setOnClickListener(new setOnClickLst());
        binding.UFX23ProSPArea.headerAddressTxt.setOnClickListener(new setOnClickLst());
        binding.UFX23ProDeliverAllArea.headerAddressTxt.setOnClickListener(new setOnClickLst());
        binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setOnClickListener(new setOnClickLst());

        if (generalFunc.isRTLmode()) {
            binding.UFX23ProArea.backImgView.setRotation(180);
        }

        // TODO : Service module wish Show UI
        binding.Main23ProArea.setVisibility(View.GONE);
        binding.UFX23ProArea.getRoot().setVisibility(View.GONE);
        binding.rideDelivery23Area.getRoot().setVisibility(View.GONE);
        binding.UFX23ProSPArea.getRoot().setVisibility(View.GONE);
        binding.UFX23ProDeliverAllArea.getRoot().setVisibility(View.GONE);
        binding.UFX23ProDeliveryOnlyArea.getRoot().setVisibility(View.GONE);

        if (ServiceModule.isDeliveronly()) {
            binding.UFX23ProDeliveryOnlyArea.getRoot().setVisibility(View.VISIBLE);
        } else if (ServiceModule.isRideOnly() || ServiceModule.RideDeliveryProduct || ServiceModule.isCubeXApp) {
            binding.rideDelivery23Area.getRoot().setVisibility(View.VISIBLE);
            setRideDeliveryView();
        } else if (ServiceModule.isServiceProviderOnly()) {
            onlyServiceProvider();

        } else if ((generalFunc.isDeliverOnlyEnabled() && ServiceModule.DeliverAll) || ServiceModule.isDeliveryKingApp) {
            binding.UFX23ProDeliverAllArea.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.Main23ProArea.setVisibility(View.VISIBLE);
        }
    }

    private void onlyServiceProvider() {
        if (UBERX_PARENT_CAT_ID.equalsIgnoreCase("0") || UBERX_PARENT_CAT_ID.equalsIgnoreCase("")) {
            binding.UFX23ProSPArea.getRoot().setVisibility(View.VISIBLE);
            CAT_TYPE_MODE = "0";
        } else {
            CAT_TYPE_MODE = "1";
            binding.UFX23ProArea.getRoot().setVisibility(View.VISIBLE);
            binding.UFX23ProArea.backImgView.setVisibility(View.GONE);

            try {
                JSONObject dataObject = new JSONObject();
                dataObject.put("iParentId", UBERX_PARENT_CAT_ID);
                dataObject.put("iVehicleCategoryId", UBERX_PARENT_CAT_ID);
                setSubCategoryList(dataObject);
            } catch (JSONException e) {
                Logger.e("Exception", "::" + e.getMessage());
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void setRideDeliveryView() {
        binding.rideDelivery23Area.toolsSubTitleTxt.setText(generalFunc.getJsonValueStr("vName", mActivity.obj_userProfile) + " "
                + generalFunc.getJsonValueStr("vLastName", mActivity.obj_userProfile));
        binding.rideDelivery23Area.userImgView.setOnClickListener(new setOnClickLst());

        if (ServiceModule.isRideOnly()) {
            binding.rideDelivery23Area.toolsTitleTxt.setText(generalFunc.retrieveLangLBl("hello", "LBL_HELLO"));
            binding.rideDelivery23Area.whereTOArea.setVisibility(View.VISIBLE);
            binding.rideDelivery23Area.whereToTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WHERE_TO"));

            binding.rideDelivery23Area.nowBtnArea.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity, R.color.white)));
            binding.rideDelivery23Area.nowTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NOW"));

            addToClickHandler(binding.rideDelivery23Area.whereTOArea);
            addToClickHandler(binding.rideDelivery23Area.nowBtnArea);
            if (generalFunc.getJsonValueStr("RIDE_LATER_BOOKING_ENABLED", mActivity.obj_userProfile).equalsIgnoreCase("Yes")) {
                binding.rideDelivery23Area.nowBtnArea.setVisibility(View.VISIBLE);
            } else {
                binding.rideDelivery23Area.nowBtnArea.setVisibility(View.GONE);
            }
        } else {
            binding.rideDelivery23Area.toolsTitleTxt.setText(generalFunc.retrieveLangLBl("welcome", "LBL_WELCOME_TXT"));
            binding.rideDelivery23Area.whereTOArea.setVisibility(View.GONE);
        }
    }

    private void setInitToolbarArea() {
        String url = CommonUtilities.USER_PHOTO_PATH + generalFunc.getMemberId() + "/" + generalFunc.getJsonValue("vImgName", mUserProfileObj);
        generalFunc.checkProfileImage(binding.userImgView, url, R.mipmap.ic_no_pic_user, R.mipmap.ic_no_pic_user);
        generalFunc.checkProfileImage(binding.rideDelivery23Area.userImgView, url, R.mipmap.ic_no_pic_user, R.mipmap.ic_no_pic_user);

        if (Utils.checkText(generalFunc.getJsonValueStr("UBERX_CAT_ID", mUserProfileObj))) {
            binding.searchArea.setVisibility(View.GONE);
        } else {
            if (generalFunc.getJsonValueStr("ENABLE_APP_HOME_SCREEN_SEARCH", mUserProfileObj).equalsIgnoreCase("Yes")) {
                binding.searchArea.setVisibility(View.VISIBLE);
            } else {
                binding.searchArea.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (requireActivity() instanceof UberXHomeActivity activity) {
            mActivity = activity;
            getUserData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
        setInitToolbarArea();
        initializeLocationCheckDone();
    }

    private void getUserData() {
        generalFunc = mActivity.generalFunc;
        if (generalFunc == null) {
            generalFunc = MyApp.getInstance().getGeneralFun(mActivity);
        }
        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
        if (Utils.checkText(userProfileJson)) {
            mUserProfileObj = generalFunc.getJsonObject(userProfileJson);
        } else {
            mUserProfileObj = mActivity.obj_userProfile;
        }
        UBERX_PARENT_CAT_ID = generalFunc.getJsonValueStr(Utils.UBERX_PARENT_CAT_ID, mUserProfileObj);
    }

    public void initializeLocationCheckDone() {
        if (generalFunc.isLocationPermissionGranted(false) && generalFunc.isLocationEnabled()) {
            if (isUfxAddress) {
                stopLocationUpdates();
                Location temploc = new Location("PickupLoc");

                if (mActivity.latitude.equals(mLatitude) && mActivity.longitude.equals(mLongitude)) {
                    return;
                }
                temploc.setLatitude(Double.parseDouble(mLatitude));
                temploc.setLongitude(Double.parseDouble(mLongitude));
                onLocationUpdate(temploc);
            } else {
                initializeLocation();
            }
        } else if (generalFunc.isLocationPermissionGranted(false) && !generalFunc.isLocationEnabled()) {
            if (!generalFunc.retrieveValue("isSmartLoginEnable").equalsIgnoreCase("Yes") ||
                    generalFunc.retrieveValue("isFirstTimeSmartLoginView").equalsIgnoreCase("Yes")) {
                OpenNoLocationView.getInstance(mActivity, binding.screen23MainArea).configView(false);
            }
        } else if (isUfxAddress) {
            OpenNoLocationView.getInstance(mActivity, binding.screen23MainArea).configView(false);
        }
    }

    private void initializeLocation() {
        binding.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProSPArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProDeliverAllArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));
        binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setHint(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"));

        stopLocationUpdates();
        GetLocationUpdates.locationResolutionAsked = false;
        getLastLocation = new GetLocationUpdates(mActivity, Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, this);
    }

    private void stopLocationUpdates() {
        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
        }
    }

    @Override
    public void onLocationUpdate(Location mLastLocation) {
        stopLocationUpdates();
        mActivity.latitude = mLatitude = mLastLocation.getLatitude() + "";
        mActivity.longitude = mLongitude = mLastLocation.getLongitude() + "";
        isUfxAddress = true;
        if (getAddressFromLocation == null) {
            getAddressFromLocation = new GetAddressFromLocation(mActivity, generalFunc);
            getAddressFromLocation.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            getAddressFromLocation.setAddressList(this);
            getAddressFromLocation.execute();
            if (mActivity.ENABLE_LOCATION_WISE_BANNER.equalsIgnoreCase("Yes")) {
                if (CAT_TYPE_MODE.equalsIgnoreCase("0")) {
                    getCategory(false);
                } else {
                    if (mUFXServices23ProView != null) {
                        mUFXServices23ProView.initializeView();
                    }
                }
            }
        }
    }

    @Override
    public void onAddressFound(String address, double latitude, double longitude, String geocodeobject) {
        if (getArguments() != null && mAddress != null) {
            address = mAddress;
            isUfxAddress = true;
            binding.headerAddressTxt.setText(address);
            binding.UFX23ProArea.headerAddressTxt.setText(address);
            binding.UFX23ProSPArea.headerAddressTxt.setText(address);
            binding.UFX23ProDeliverAllArea.headerAddressTxt.setText(address);
            binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setText(address);

            mLatitude = getArguments().getString("uberXlat");
            mLongitude = getArguments().getString("uberXlong");
        } else {
            if (address != null && !address.equals("")) {
                isUfxAddress = true;
                mLatitude = latitude + "";
                mLongitude = longitude + "";
                binding.headerAddressTxt.setText(address);
                binding.UFX23ProArea.headerAddressTxt.setText(address);
                binding.UFX23ProSPArea.headerAddressTxt.setText(address);
                binding.UFX23ProDeliverAllArea.headerAddressTxt.setText(address);
                binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setText(address);

                if (noSourceLocationDialog != null) {
                    noSourceLocationDialog.dismiss();
                }
            }
        }
        mActivity.address = address;
        // MyApp.getInstance().updatedeliverAllcategoryType(generalFunc, deliverallObjStr, mLatitude, mLongitude);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OpenNoLocationView.getInstance(mActivity, binding.screen23MainArea).configView(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE && resultCode == getActivity().RESULT_OK && data != null) {

            mActivity.address = data.getStringExtra("Address");
            mActivity.latitude = mLatitude = data.getStringExtra("Latitude") == null ? "0.0" : data.getStringExtra("Latitude");
            mActivity.longitude = mLongitude = data.getStringExtra("Longitude") == null ? "0.0" : data.getStringExtra("Longitude");

            binding.headerAddressTxt.setText(data.getStringExtra("Address"));
            binding.UFX23ProArea.headerAddressTxt.setText(data.getStringExtra("Address"));
            binding.UFX23ProSPArea.headerAddressTxt.setText(data.getStringExtra("Address"));
            binding.UFX23ProDeliverAllArea.headerAddressTxt.setText(data.getStringExtra("Address"));
            binding.UFX23ProDeliveryOnlyArea.headerAddressTxt.setText(data.getStringExtra("Address"));
            if (!this.mLatitude.equalsIgnoreCase("0.0") && !this.mLongitude.equalsIgnoreCase("0.0")) {
                isUfxAddress = true;
            }
            if (mActivity.ENABLE_LOCATION_WISE_BANNER.equalsIgnoreCase("Yes") || isUfxAddress) {
                if (noSourceLocationDialog != null) {
                    noSourceLocationDialog.dismiss();
                    noSourceLocationDialog = null;
                }
                if (CAT_TYPE_MODE.equalsIgnoreCase("0")) {
                    new Handler(Looper.myLooper()).postDelayed(() -> getCategory(true), 100);
                    //MyApp.getInstance().updatedeliverAllcategoryType(generalFunc, deliverallObjStr, this.mLatitude, this.mLongitude);
                } else {
                    if (mUFXServices23ProView != null) {
                        mUFXServices23ProView.initializeView();
                    }
                }
            }

        } else if (requestCode == UBER_X_SEARCH_SERVICE_REQ_CODE && resultCode == getActivity().RESULT_OK && data != null) {

            if (data.hasExtra("selectedItem")) {
                HashMap<String, String> mapData = (HashMap<String, String>) data.getSerializableExtra("selectedItem");
                if (mapData == null) {
                    return;
                }

                JSONObject dataObject = new JSONObject();
                MyUtils.createJsonObject(mapData, dataObject);
                onItemClickHandle(0, dataObject);
            }
        } else if (requestCode == Utils.VERIFY_MOBILE_REQ_CODE) {
            OpenNoLocationView.getInstance(mActivity, binding.screen23MainArea).configView(false);
        }
    }

    private void openSourceLocationView() {
        if (noSourceLocationDialog != null) {
            noSourceLocationDialog.dismiss();
            noSourceLocationDialog = null;
        }
        noSourceLocationDialog = new AppCompatDialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
        noSourceLocationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noSourceLocationDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        noSourceLocationDialog.setContentView(R.layout.no_source_location_design);
        Objects.requireNonNull(noSourceLocationDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        noSourceLocationDialog.setCancelable(false);

        ((RippleBackground) Objects.requireNonNull(noSourceLocationDialog.findViewById(R.id.rippleBgView))).startRippleAnimation();
        ImageView closeImage = noSourceLocationDialog.findViewById(R.id.closeImage);
        assert closeImage != null;
        closeImage.setOnClickListener(v -> noSourceLocationDialog.dismiss());
        MTextView locationHintText = noSourceLocationDialog.findViewById(R.id.locationHintText);
        MTextView locationDescText = noSourceLocationDialog.findViewById(R.id.locationDescText);
        MTextView btnTxt = noSourceLocationDialog.findViewById(R.id.btnTxt);
        ImageView btnImg = noSourceLocationDialog.findViewById(R.id.btnImg);
        LinearLayout btnArea = noSourceLocationDialog.findViewById(R.id.btnArea);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        RippleBackground.LayoutParams buttonLayoutParams = new RippleBackground.LayoutParams(RippleBackground.LayoutParams.MATCH_PARENT, RippleBackground.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.setMargins(0, 0, 0, -(height / 2));
        ((RippleBackground) Objects.requireNonNull(noSourceLocationDialog.findViewById(R.id.rippleBgView))).setLayoutParams(buttonLayoutParams);

        assert btnImg != null;
        assert btnArea != null;
        assert btnTxt != null;
        assert locationDescText != null;
        assert locationHintText != null;

        if (generalFunc.isRTLmode()) {
//            btnImg.setRotation(180);
            btnArea.setBackground(AppCompatResources.getDrawable(mActivity, R.drawable.login_border_rtl));
        }

        btnTxt.setText(generalFunc.retrieveLangLBl("ENTER", "LBL_ADD_ADDRESS_TXT"));
        locationDescText.setText(generalFunc.retrieveLangLBl("Please wait while we are trying to access your location. meanwhile you can enter your source location.", "LBL_FETCHING_LOCATION_NOTE_TEXT"));
        locationHintText.setText(generalFunc.retrieveLangLBl("Location", "LBL_LOCATION_FOR_FRONT"));

        btnArea.setOnClickListener(v -> {
            Bundle bn = new Bundle();
            bn.putString("locationArea", "source");
            if (!mLatitude.equalsIgnoreCase("0.0") && !mLongitude.equalsIgnoreCase("0.0")) {
                bn.putDouble("lat", GeneralFunctions.parseDoubleValue(0.0, mLatitude));
                bn.putDouble("long", GeneralFunctions.parseDoubleValue(0.0, mLongitude));
            }
            bn.putString("address", binding.headerAddressTxt.getText().toString().trim());

            new ActUtils(mActivity).startActForResult(SearchLocationActivity.class, bn, Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE);

        });
        noSourceLocationDialog.show();
    }

    private void openMoreDialog(@Nullable String vCategoryTitle, @NonNull JSONArray moreServicesArr) {
        if (moreDialog != null && moreDialog.isShowing()) {
            return;
        }
        moreDialog = new BottomSheetDialog(mActivity);
        DialogMore23ProBinding binding = DialogMore23ProBinding.inflate(LayoutInflater.from(getContext()));
        View contentView = binding.getRoot();
        if (generalFunc.isRTLmode()) {
            contentView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        moreDialog.setContentView(binding.getRoot());
        moreDialog.setCancelable(false);
        BottomSheetBehavior<View> mBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        if (Utils.checkText(vCategoryTitle)) {
            binding.moreTitleTxt.setText(vCategoryTitle);
        } else {
            binding.moreTitleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_SERVICE"));
        }
        binding.cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));
        binding.cancelTxt.setOnClickListener(v -> moreDialog.dismiss());
        binding.closeVideoView.setOnClickListener(v -> moreDialog.dismiss());

        if (moreS23Adapter == null) {
            moreS23Adapter = new MoreService23Adapter(mActivity, generalFunc, moreServicesArr, (morePos, mServiceObject) -> {
                moreDialog.cancel();
                onItemClickHandle(morePos, mServiceObject);
            });
        } else {
            moreS23Adapter.updateData(moreServicesArr);
        }

        try {
            JSONObject moreObject = generalFunc.getJsonObject(moreServicesArr, 0);
            if (moreObject.has("GridView")) {
                binding.rvMoreServices.setLayoutManager(new LinearLayoutManager(mActivity));
                int bannerHeight = (int) (Utils.getScreenPixelHeight(mActivity) - getResources().getDimensionPixelSize(R.dimen._50sdp));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (bannerHeight > binding.rvMoreServices.getMeasuredHeight()) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.rvMoreServices.getLayoutParams();
                        params.height = bannerHeight;
                        binding.rvMoreServices.setLayoutParams(params);
                    }
                }, 20);
                mBehavior.setDraggable(false);
            } else {
                binding.rvMoreServices.setLayoutManager(new GridLayoutManager(mActivity, MyUtils.getNumOfColumns(mActivity)));
            }
        } catch (Exception e) {
            binding.rvMoreServices.setLayoutManager(new GridLayoutManager(mActivity, MyUtils.getNumOfColumns(mActivity)));
        }

        binding.rvMoreServices.setAdapter(moreS23Adapter);

        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBehavior.setHideable(false);
        moreDialog.show();
    }

    private class setOnClickLst implements View.OnClickListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            Utils.hideKeyboard(mActivity);
            switch (v.getId()) {
                case R.id.backImgView:
                    if (CAT_TYPE_MODE.equals("1") && UBERX_PARENT_CAT_ID.equalsIgnoreCase("0")) {
                        setMainCategory();
                        return;
                    }
                    break;
                case R.id.headerAddressTxt:

                    Bundle bn = new Bundle();
                    bn.putString("locationArea", "source");
                    if (mLatitude != null && !mLatitude.equalsIgnoreCase("0.0") && mLongitude != null && !mLongitude.equalsIgnoreCase("0.0")) {
                        bn.putDouble("lat", GeneralFunctions.parseDoubleValue(0.0, mLatitude));
                        bn.putDouble("long", GeneralFunctions.parseDoubleValue(0.0, mLongitude));
                    }
                    bn.putString("address", Utils.getText(binding.headerAddressTxt));
                    new ActUtils(mActivity).startActForResult(SearchLocationActivity.class, bn, Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE);
                    break;

                case R.id.searchArea:
                    new ActUtils(mActivity).startActForResult(SearchServiceActivity.class, UBER_X_SEARCH_SERVICE_REQ_CODE);
                    break;

                case R.id.userImgView:
                    new ActUtils(mActivity).startActForResult(MyProfileActivity.class, Utils.MY_PROFILE_REQ_CODE);
                    break;

                case R.id.userProfileView:
                    mActivity.profileArea.performClick();
                    break;
            }
        }
    }

    @Override
    public void onClickView(View view) {
        Utils.hideKeyboard(mActivity);
        if (view.getId() == binding.rideDelivery23Area.whereTOArea.getId()) {
            reDirectAction(true, false, false);
        } else if (view.getId() == binding.rideDelivery23Area.nowBtnArea.getId()) {
            reDirectAction(false, true, false);
        }
    }

    ///////////// ===================================================================================
    private void manageHomeScreenView(String responseString) {
        if (generalFunc.isDeliverOnlyEnabled() && ServiceModule.DeliverAll) {

            while (homeScreenDataArray.length() > 0) {
                homeScreenDataArray.remove(0);
            }
            JSONArray bannerArr = generalFunc.getJsonArray("BANNER_DATA", responseString);
            if (bannerArr != null) {
                for (int i = 0; i < bannerArr.length(); i++) {
                    homeScreenDataArray.put(generalFunc.getJsonObject(responseString));
                }
            }
            JSONArray homeDataArr = generalFunc.getJsonArray("HOME_SCREEN_DATA", responseString);
            if (homeDataArr != null) {
                for (int i = 0; i < homeDataArr.length(); i++) {
                    homeScreenDataArray.put(generalFunc.getJsonObject(homeDataArr, i));
                }
            }
            if (main23DeliverAllAdapter == null) {
                main23DeliverAllAdapter = new Main23DeliverAllAdapter(mActivity, homeScreenDataArray, new Main23DeliverAllAdapter.OnClickListener() {
                    @Override
                    public void onBannerItemClick(int position, JSONObject dataObject) {
                        if (dataObject.has("servicesArr")) {
                            JSONArray servicesArr = mActivity.generalFunc.getJsonArray("servicesArr", dataObject);
                            if (servicesArr != null && servicesArr.length() > 0) {
                                if (servicesArr.length() > 1) {
                                    openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), servicesArr);
                                } else {
                                    onItemClickHandle(position, generalFunc.getJsonObject(servicesArr, 0));
                                }
                            }
                        } else {
                            if (dataObject.has("MedicalServiceSection")) {
                                if (generalFunc.getJsonValueStr("MedicalServiceSection", dataObject).equalsIgnoreCase("Yes")) {
                                    if (dataObject.has("MoreSubCategories")) {
                                        JSONArray array = generalFunc.getJsonArray("MoreSubCategories", dataObject);
                                        openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), array);
                                        return;
                                    }
                                }
                            }
                            onItemClickHandle(position, dataObject);
                        }
                    }

                    @Override
                    public void onGridItemClick(int position, JSONObject dataObject) {
                        if (dataObject.has("SubCategories")) {
                            openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), mActivity.generalFunc.getJsonArray("SubCategories", dataObject));
                        } else {
                            onItemClickHandle(position, dataObject);
                        }
                    }
                });
            }
            binding.UFX23ProDeliverAllArea.dynamicHomeList23RecyclerView.setAdapter(main23DeliverAllAdapter);
            main23DeliverAllAdapter.updateData(homeScreenDataArray);
        } else {

            homeScreenDataArray = generalFunc.getJsonArray("HOME_SCREEN_DATA", responseString);
            if (main23Adapter == null) {
                main23Adapter = new Main23Adapter(mActivity, homeScreenDataArray, new Main23Adapter.OnClickListener() {
                    @Override
                    public void onBannerItemClick(int position, JSONObject dataObject) {
                        if (dataObject.has("servicesArr")) {
                            JSONArray servicesArr = mActivity.generalFunc.getJsonArray("servicesArr", dataObject);
                            if (servicesArr != null && servicesArr.length() > 0) {
                                if (servicesArr.length() > 1) {
                                    openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), servicesArr);
                                } else {
                                    onItemClickHandle(position, generalFunc.getJsonObject(servicesArr, 0));
                                }
                            }
                        } else {
                            onItemClickHandle(position, dataObject);
                        }
                    }

                    @Override
                    public void onServiceBannerItemClick(int position, JSONObject dataObject) {
                        if (dataObject.has("servicesArr")) {
                            JSONArray servicesArr = mActivity.generalFunc.getJsonArray("servicesArr", dataObject);
                            if (servicesArr != null && servicesArr.length() > 0) {
                                if (servicesArr.length() > 1) {
                                    openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), servicesArr);
                                } else {
                                    onItemClickHandle(position, generalFunc.getJsonObject(servicesArr, 0));
                                }
                            }
                        } else {
                            if (dataObject.has("moreCategories")) {
                                openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), mActivity.generalFunc.getJsonArray("moreCategories", dataObject));
                            } else {
                                onItemClickHandle(position, dataObject);
                            }
                        }
                    }

                    @Override
                    public void onGridItemClick(int position, JSONObject dataObject) {
                        if (ServiceModule.isDeliveronly() && dataObject.has("DELIVERY_SERVICES")) {
                            openMoreDialog(generalFunc.retrieveLangLBl("", "LBL_DELIVERY_SEND_PARCEL_BTN_TXT"), mActivity.generalFunc.getJsonArray("DELIVERY_SERVICES", dataObject));
                        } else if (dataObject.has("SubCategories")) {
                            openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), mActivity.generalFunc.getJsonArray("SubCategories", dataObject));
                        } else {
                            onItemClickHandle(position, dataObject);
                        }
                    }

                    @Override
                    public void onServiceListItemClick(int position, JSONObject dataObject) {
                        if (dataObject.has("moreCategories")) {
                            openMoreDialog(generalFunc.getJsonValueStr("vCategoryTitle", dataObject), mActivity.generalFunc.getJsonArray("moreCategories", dataObject));
                        } else {
                            onItemClickHandle(position, dataObject);
                        }
                    }

                    @Override
                    public void onServiceOtherListItemClick(int position, JSONObject dataObject) {
                        String eCatType = generalFunc.getJsonValueStr("eCatType", dataObject);

                        if (eCatType.equalsIgnoreCase("AddStop") ||
                                eCatType.equalsIgnoreCase("RIDE") &&
                                        (dataObject.has("vSubTitle") && Utils.checkText(generalFunc.getJsonValueStr("vSubTitle", dataObject))) ||
                                eCatType.equalsIgnoreCase("RIDEPOOL") &&
                                        (dataObject.has("vSubTitle") && Utils.checkText(generalFunc.getJsonValueStr("vSubTitle", dataObject)))) {

                            Intent intent = new Intent(mActivity, InformationActivity.class);
                            intent.putExtra("serviceDataObject", dataObject.toString());
                            someActivityResultLauncher.launch(intent);
                        } else {
                            onItemClickHandle(position, dataObject);
                        }
                    }

                    @Override
                    public void onWhereToClick(int position, JSONObject jsonObject) {
                        reDirectAction(true, false, false);
                    }

                    @Override
                    public void onNowClick(int position, JSONObject jsonObject) {
                        reDirectAction(false, true, false);
                    }

                    @Override
                    public void onSeeAllClick(int position, JSONObject itemObject) {
                        mActivity.servicesArea.performClick();
                    }

                    @Override
                    public void onNewListingClick(int position, JSONObject itemObject) {
                        showNewListingDialog(position, itemObject);
                    }
                });
            }
            main23Adapter.setResponseString(responseString);
            if (ServiceModule.isOnlyBuySellRentEnable()) {
//                binding.UFX23BuySellRentOnlyArea.dynamicHomeList23RecyclerView.setAdapter(main23Adapter);
            } else if (ServiceModule.isDeliveronly()) {
                binding.UFX23ProDeliveryOnlyArea.dynamicHomeList23RecyclerView.setAdapter(main23Adapter);
            } else if (ServiceModule.isRideOnly() || ServiceModule.RideDeliveryProduct || ServiceModule.isCubeXApp) {
                binding.rideDelivery23Area.dynamicHomeList23RecyclerView.setAdapter(main23Adapter);
            } else if (ServiceModule.isServiceProviderOnly()) {
                binding.UFX23ProSPArea.dynamicHomeList23RecyclerView.setAdapter(main23Adapter);
            } else if (ServiceModule.isDeliveryKingApp) {
                binding.UFX23ProDeliverAllArea.dynamicHomeList23RecyclerView.setAdapter(main23Adapter);
            } else {
                binding.dynamicHomeList23RecyclerView.setAdapter(main23Adapter);
            }
            main23Adapter.updateData(homeScreenDataArray);
        }

    }

    private void showNewListingDialog(int position, JSONObject dataObject) {
        newListingArrayList.clear();
        if (dataObject.has("servicesArr")) {
            JSONArray servicesArr = mActivity.generalFunc.getJsonArray("servicesArr", dataObject);
            if (servicesArr != null && servicesArr.length() > 0) {
                MyUtils.createArrayListJSONArray(generalFunc, newListingArrayList, servicesArr);
            }

            OpenListView instance = OpenListView.getInstance(mActivity, generalFunc.retrieveLangLBl("", "LBL_SELECT_TYPE"), newListingArrayList, OpenListView.OpenDirection.BOTTOM, false, new OpenListView.OnItemClickList() {
                @Override
                public void onItemClick(int position) {
                    Bundle bn = new Bundle();
                    bn.putBoolean("isHome", true);
                    bn.putString("eType", newListingArrayList.get(position).get("eType"));

                }
            });
            instance.setArrowImageReplacingCheckmark(true);
            instance.show(-1, "vCategoryName");
        }
    }

    private void reDirectAction(boolean isWhereTo, boolean isShowSchedule, boolean isAddStop) {
        if (binding.headerAddressTxt.getText().toString().equalsIgnoreCase(generalFunc.retrieveLangLBl("", "")) || binding.headerAddressTxt.getText().toString().equalsIgnoreCase(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"))) {
            openSourceLocationView();
            return;
        }
        Bundle bn = new Bundle();
        bn.putString("selType", Utils.CabGeneralType_Ride);
        bn.putBoolean("isRestart", false);

        bn.putBoolean("isWhereTo", isWhereTo);
        bn.putBoolean("isShowSchedule", isShowSchedule);
        bn.putBoolean("isAddStop", isAddStop);

        bn.putString("address", Utils.getText(binding.headerAddressTxt));

        bn.putString("latitude", Utils.checkText(mLatitude) ? mLatitude : "");
        bn.putString("lat", Utils.checkText(mLatitude) ? mLatitude : "");

        bn.putString("longitude", Utils.checkText(mLongitude) ? mLongitude : "");
        bn.putString("long", Utils.checkText(mLongitude) ? mLongitude : "");

        new ActUtils(mActivity).startActWithData(MainActivity.class, bn);
    }

    private void onItemClickHandle(int position, JSONObject dataObject) {
        Utils.hideKeyboard(mActivity);
        if (binding.headerAddressTxt.getText().toString().equalsIgnoreCase(generalFunc.retrieveLangLBl("", "")) || binding.headerAddressTxt.getText().toString().equalsIgnoreCase(generalFunc.retrieveLangLBl("", "LBL_LOAD_ADDRESS"))) {
            openSourceLocationView();
            return;
        }

        boolean eShowTerms = generalFunc.getJsonValueStr("eShowTerms", dataObject).equalsIgnoreCase("Yes");
        if (eShowTerms && !CommonUtilities.ageRestrictServices.contains("1")) {
            new showTermsDialog(mActivity, generalFunc, position, generalFunc.getJsonValueStr("vCategory", dataObject), false, () -> {
                //
                onItemClickHandle(position, dataObject);
            });
            return;
        }

        String eCatType = generalFunc.getJsonValueStr("eCatType", dataObject);
        if (eCatType.equalsIgnoreCase("RideReserve")) {
            reDirectAction(false, true, false);
            return;
        } else if (eCatType.equalsIgnoreCase("AddStop")) {
            reDirectAction(true, false, true);
            return;
        } else if (eCatType.equalsIgnoreCase("TAXIBID") || eCatType.equalsIgnoreCase("MOTOBID")) {
            String myType = "isTaxiBid";
            if (!Utils.checkText(generalFunc.retrieveValue(myType))) {
                Intent intent = new Intent(mActivity, CommonIntroActivity.class);
                Bundle bn = new Bundle();
                bn.putString("dataObject", dataObject.toString());
                bn.putString("viewIntroType", myType);
                intent.putExtras(bn);
                introActivity.launch(intent);
                return;
            }
        }

        if (eCatType.equalsIgnoreCase("ServiceProvider") || eCatType.equalsIgnoreCase("Bidding")) {

            if (generalFunc.getJsonValueStr("eForMedicalService", dataObject).equalsIgnoreCase("Yes")) {
                (new OpenCatType23Pro(mActivity, generalFunc, dataObject, mLatitude, mLongitude, mActivity.address)).execute();

            } else if (eCatType.equalsIgnoreCase("Bidding") && generalFunc.getJsonValueStr("other", dataObject).equalsIgnoreCase("Yes")) {
                (new OpenCatType23Pro(mActivity, generalFunc, dataObject, mLatitude, mLongitude, mActivity.address)).execute();

            } else {
                setSubCategoryList(dataObject);
            }

        } else {
            (new OpenCatType23Pro(mActivity, generalFunc, dataObject, mLatitude, mLongitude, mActivity.address)).execute();
        }
    }

    ActivityResultLauncher<Intent> introActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                    JSONObject dataObject;
                    try {
                        dataObject = new JSONObject(data.getStringExtra("dataObject"));
                        (new OpenCatType23Pro(mActivity, generalFunc, dataObject, mLatitude, mLongitude, mActivity.address)).execute();
                    } catch (JSONException e) {
                        Logger.e("Exception", "::" + e.getMessage());
                    }
                }
            });

    public void setMainCategory() {
        CAT_TYPE_MODE = "0";
        if (ServiceModule.isServiceProviderOnly()) {
            onlyServiceProvider();

        } else {
            binding.Main23ProArea.setVisibility(View.VISIBLE);
        }
        binding.UFX23ProArea.getRoot().setVisibility(View.GONE);
        setInitToolbarArea();
        mActivity.rduTopArea.setVisibility(View.VISIBLE);
    }

    private void setSubCategoryList(JSONObject dataObject) {
        if (CAT_TYPE_MODE.equalsIgnoreCase("0") &&
                !generalFunc.getJsonValueStr("iParentId", dataObject).equalsIgnoreCase("0")) {
            boolean isVideoConsultEnable = dataObject.has("isVideoConsultEnable") && generalFunc.getJsonValueStr("isVideoConsultEnable", dataObject).equalsIgnoreCase("Yes");
            boolean isBidding = dataObject.has("iBiddingId") && Utils.checkText(generalFunc.getJsonValueStr("iBiddingId", dataObject));
            SubmitButtonClick(generalFunc.getJsonValueStr("iVehicleCategoryId", dataObject), generalFunc.getJsonValueStr("iParentId", dataObject), generalFunc.getJsonValueStr("vCategory", dataObject), isVideoConsultEnable, isBidding);
            return;
        }
        if (CAT_TYPE_MODE.equalsIgnoreCase("0")) {
            mActivity.rduTopArea.setVisibility(View.GONE);
        }

        CAT_TYPE_MODE = "1";
        if (mUFXServices23ProView == null) {
            mUFXServices23ProView = new UFXServices23ProView(mActivity, generalFunc, binding, dataObject, new UFXServices23ProView.OnUFXServiceViewListener() {
                @Override
                public void onProcess(boolean isLoadingView) {
                    if (isLoadingView) {
                        SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.UFX23ProArea.UFXDataArea, R.layout.subcategory_shimmer_view_22);
                    } else {
                        SkeletonViewHandler.getInstance().hideSkeletonView();
                    }
                }

                @Override
                public void onSubmitButtonClick(String selectedVehicleTypeId, String iParentId, String categoryName, boolean mIsVideoConsultEnable, boolean isBidding) {
                    SubmitButtonClick(selectedVehicleTypeId, iParentId, categoryName, mIsVideoConsultEnable, isBidding);
                }
            });
        } else {
            mUFXServices23ProView.initializeView(dataObject);
        }
    }

    private void SubmitButtonClick(String selectedVehicleTypeId, String iParentId, String categoryName, boolean mIsVideoConsultEnable, boolean isBidding) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isufx", true);
        bundle.putString("latitude", mLatitude);
        bundle.putString("longitude", mLongitude);
        bundle.putString("address", binding.headerAddressTxt.getText().toString());
        bundle.putString("SelectvVehicleType", binding.UFX23ProArea.selectServiceTxt.getText().toString());

        if (Utils.checkText(selectedVehicleTypeId)) {
            bundle.putString("SelectedVehicleTypeId", selectedVehicleTypeId);
            bundle.putString("parentId", iParentId);
        } else {
            bundle.putString("SelectedVehicleTypeId", iParentId);
            bundle.putString("parentId", iParentId);
        }

        bundle.putBoolean("isCarwash", true);
        bundle.putBoolean("isVideoConsultEnable", mIsVideoConsultEnable);

        if (isBidding) {
            if (Utils.checkText(selectedVehicleTypeId) && Utils.checkText(categoryName)) {
                bundle.putString("SelectvVehicleType", categoryName);
            }

        } else {
            new ActUtils(mActivity).startActWithData(MainActivity.class, bundle);
        }
    }

    private void getCategory(boolean isShimmerView) {
        if (isShimmerView) {
           /* if (binding.UFX23BuySellRentOnlyArea.getRoot().getVisibility() == View.VISIBLE) {
                SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.UFX23BuySellRentOnlyArea.dataArea, R.layout.shimmer_home_screen_23);
            } else*/
            if (binding.UFX23ProDeliveryOnlyArea.getRoot().getVisibility() == View.VISIBLE) {
                SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.UFX23ProDeliveryOnlyArea.dataArea, R.layout.shimmer_home_screen_23);
            } else if (binding.rideDelivery23Area.getRoot().getVisibility() == View.VISIBLE) {
                SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.rideDelivery23Area.dataArea, R.layout.shimmer_home_screen_23);
            } else if (binding.UFX23ProSPArea.getRoot().getVisibility() == View.VISIBLE) {
                SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.UFX23ProSPArea.dataArea, R.layout.shimmer_home_screen_23);
            } else if (binding.UFX23ProDeliverAllArea.getRoot().getVisibility() == View.VISIBLE) {
                SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.UFX23ProDeliverAllArea.dataArea, R.layout.shimmer_home_screen_23);
            } else {
                SkeletonViewHandler.getInstance().ShowNormalSkeletonView(binding.dataArea, R.layout.shimmer_home_screen_23);
            }
            binding.headerAddressTxt.setEnabled(false);
        }

        HashMap<String, String> parameters = new HashMap<>();
        if (generalFunc.isDeliverOnlyEnabled() && ServiceModule.DeliverAll) {
            parameters.put("type", "getServiceCategories");
        } else {
            parameters.put("type", "getServiceCategoriesPro");
        }
        parameters.put("parentId", UBERX_PARENT_CAT_ID);
        parameters.put("userId", generalFunc.getMemberId());
        parameters.put("vLatitude", mActivity.latitude);
        parameters.put("vLongitude", mActivity.longitude);

        if (currentCallExeWebServer != null) {
            currentCallExeWebServer.cancel(true);
            currentCallExeWebServer = null;
        }

        currentCallExeWebServer = ApiHandler.execute(mActivity, parameters, responseString -> {

            currentCallExeWebServer = null;
            manageHomeScreenView(responseString);

            binding.headerAddressTxt.setEnabled(true);
            SkeletonViewHandler.getInstance().hideSkeletonView();

        });
    }
}