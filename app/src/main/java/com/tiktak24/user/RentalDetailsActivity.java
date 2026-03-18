package com.tiktak24.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.activity.ParentActivity;
import com.adapter.files.PackageAdapter;
import com.general.files.ActUtils;
import com.general.files.GeneralFunctions;
import com.general.files.GetLocationUpdates;
import com.map.GeoMapLoader;
import com.service.handler.ApiHandler;
import com.utils.CommonUtilities;
import com.utils.LoadImage;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RentalDetailsActivity extends ParentActivity implements PackageAdapter.setPackageClickList {

    MTextView titleTxt;
    ImageView backImgView;

    MTextView packageHtxt, packageVtxt;
    String imgName;
    String vehicleIconPath = CommonUtilities.SERVER_URL + "webimages/icons/VehicleType/";
    RecyclerView packageRecyclerView;
    public ArrayList<HashMap<String, String>> packageList = new ArrayList<>();
    PackageAdapter adapter;
    public MButton acceptBtn;
    MTextView fareTitletxt, fareMsgtxt, pick_Up_title;
    View fareInfoArea;
    int selpos = 0;
    LinearLayout packageDetails;

    String page_desc;
    String vehicle_list_title = "";

    GeoMapLoader.GeoMap geoMap;
    Location userLocation;
    GetLocationUpdates getLastLocation;
    String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_details);


        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        packageHtxt = (MTextView) findViewById(R.id.packageHtxt);
        packageVtxt = (MTextView) findViewById(R.id.packageVtxt);
        pick_Up_title = (MTextView) findViewById(R.id.pick_Up_title);
        fareTitletxt = (MTextView) findViewById(R.id.fareTitletxt);
        fareMsgtxt = (MTextView) findViewById(R.id.fareMsgtxt);
        packageRecyclerView = (RecyclerView) findViewById(R.id.packageRecyclerView);
        fareInfoArea = findViewById(R.id.fareInfoArea);
        packageDetails = (LinearLayout) findViewById(R.id.pkgArea);
        packageDetails.setVisibility(View.GONE);
        addToClickHandler(fareInfoArea);
        addToClickHandler(backImgView);


        acceptBtn = ((MaterialRippleLayout) findViewById(R.id.acceptBtn)).getChildView();
        acceptBtn.setId(Utils.generateViewId());
        acceptBtn.setAllCaps(false);
        addToClickHandler(acceptBtn);
        setLabel();
        getPackageDetails();


        packageVtxt.setVisibility(View.GONE);


        // manageBottomSheeetDefaultHeight((int) getResources().getDimension(R.dimen._120sdp));

        // setting the bottom sheet callback for interacting with state changes and sliding


        //map.setListener(() -> (bottomSheet.requestDisallowInterceptTouchEvent(false));
    }

    @SuppressLint("SetTextI18n")
    public void setLabel() {

        boolean eFly = getIntent().hasExtra("eFly") && getIntent().getBooleanExtra("eFly", false);

        if (getIntent().getStringExtra("eMoto") != null && !getIntent().getStringExtra("eMoto").equalsIgnoreCase("") && getIntent().getStringExtra("eMoto").equalsIgnoreCase("Yes")) {
            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_MOTO_TITLE_TXT"));
        } else if (eFly) {
            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_AIRCRAFT_TITLE_TXT"));
        } else {
            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_A_CAR"));

        }

        packageHtxt.setText(/*generalFunc.convertNumberWithRTL("3") + ". " + */generalFunc.retrieveLangLBl("", "LBL_SELECT_PACKAGE_TXT"));
        acceptBtn.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_CONFIRM"));
        fareTitletxt.setText(generalFunc.retrieveLangLBl("", "LBL_FARE_DETAILS_AND_RULES_TXT"));
        fareMsgtxt.setText(generalFunc.retrieveLangLBl("", "LBL_FARE_DETAILS_DESCRIPTION_TXT"));

        imgName = getImageName(getIntent().getStringExtra("vLogo"));


        if (imgName.equalsIgnoreCase("")) {
            imgUrl = CommonUtilities.SERVER_URL + "webimages/icons/DefaultImg/" + "hover_ic_car.png";
        } else {
            imgUrl = vehicleIconPath + getIntent().getStringExtra("iVehicleTypeId") + "/android/" + imgName;
        }
        setVehicleDetailMarker();

    }

    public void getPackageDetails() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getRentalPackages");
        parameters.put("GeneralMemberId", generalFunc.getMemberId());
        parameters.put("iVehicleTypeId", getIntent().getStringExtra("iVehicleTypeId"));
        parameters.put("UserType", Utils.userType);
        parameters.put("PromoCode", getIntent().getStringExtra("PromoCode"));

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {

                    page_desc = generalFunc.getJsonValue("page_desc", responseString);
                    vehicle_list_title = generalFunc.getJsonValue("vehicle_list_title", responseString);

                    JSONArray vehicleTypesArr = generalFunc.getJsonArray(Utils.message_str, responseString);
                    for (int i = 0; i < vehicleTypesArr.length(); i++) {

                        JSONObject obj_temp = generalFunc.getJsonObject(vehicleTypesArr, i);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("iRentalPackageId", generalFunc.getJsonValueStr("iRentalPackageId", obj_temp));
                        map.put("vPackageName", generalFunc.getJsonValueStr("vPackageName", obj_temp));
                        map.put("fPrice", generalFunc.getJsonValueStr("fPrice", obj_temp));
                        map.put("fKiloMeter", generalFunc.getJsonValueStr("fKiloMeter", obj_temp));
                        map.put("fHour", generalFunc.getJsonValueStr("fHour", obj_temp));
                        map.put("fPricePerKM", generalFunc.getJsonValueStr("fPricePerKM", obj_temp));
                        map.put("fPricePerHour", generalFunc.getJsonValueStr("fPricePerHour", obj_temp));
                        map.put("fKiloMeter_LBL", generalFunc.getJsonValueStr("fKiloMeter_data", obj_temp));
                        packageList.add(map);

                    }


                    if (packageList.size() > 0) {
                        adapter = new PackageAdapter(getActContext(), packageList);
                        adapter.itemPackageClick(RentalDetailsActivity.this);
                        packageRecyclerView.setAdapter(adapter);
                        packageDetails.setVisibility(View.VISIBLE);
                    }


                }
            } else {
                generalFunc.showError();
            }
        });

    }


    private String getImageName(String vLogo) {
        String imageName = "";

        if (vLogo.equals("")) {
            return vLogo;
        }

        DisplayMetrics metrics = (getActContext().getResources().getDisplayMetrics());
        int densityDpi = (int) (metrics.density * 160f);
        switch (densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                imageName = "mdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                imageName = "mdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                imageName = "hdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_TV:
                imageName = "hdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                imageName = "xhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_280:
                imageName = "xhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_400:
                imageName = "xxhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_360:
                imageName = "xxhdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_420:
                imageName = "xxhdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                imageName = "xxhdpi_" + vLogo;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                imageName = "xxxhdpi_" + vLogo;
                break;

            case DisplayMetrics.DENSITY_560:
                imageName = "xxxhdpi_" + vLogo;
                break;

            default:
                imageName = "xxhdpi_" + vLogo;
                break;
        }

        return imageName;
    }

    public Context getActContext() {
        return RentalDetailsActivity.this;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void itemPackageClick(int position) {
        selpos = position;
        adapter.selPos(selpos);
        adapter.notifyDataSetChanged();
    }


    public void onClick(View view) {
        int i = view.getId();
        Utils.hideKeyboard(getActContext());
        if (i == backImgView.getId()) {
            onBackPressed();
        } else if (i == acceptBtn.getId()) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("iRentalPackageId", packageList.get(selpos).get("iRentalPackageId"));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else if (i == fareInfoArea.getId()) {
            Bundle bn = new Bundle();
            HashMap<String, String> map = packageList.get(selpos);
            map.put("vVehicleType", getIntent().getStringExtra("vVehicleType"));
            map.put("page_desc", page_desc);
            bn.putSerializable("data", map);
            new ActUtils(getActContext()).startActWithData(RentalInfoActivity.class, bn);

        }


    }

    private void setVehicleDetailMarker() {
        TextView vehicleTypeText = findViewById(R.id.vehicleTypeText);
        MTextView addressText = findViewById(R.id.addressText);
        ImageView carTypeImgView = findViewById(R.id.carTypeImgView);

        addressText.setText(getIntent().getStringExtra("address"));
        vehicleTypeText.setText(getIntent().getStringExtra("vVehicleType"));
        pick_Up_title.setText(generalFunc.retrieveLangLBl("", "LBL_PICKUP_LOCATION_TXT"));

        new LoadImage.builder(LoadImage.bind(imgUrl), carTypeImgView).setErrorImagePath(R.mipmap.ic_no_icon).setPlaceholderImagePath(R.mipmap.ic_no_icon).build();
    }

    @Override
    protected void onDestroy() {
        if (getLastLocation != null) {
            getLastLocation.stopLocationUpdates();
        }
        this.geoMap = null;
        super.onDestroy();
    }
}
