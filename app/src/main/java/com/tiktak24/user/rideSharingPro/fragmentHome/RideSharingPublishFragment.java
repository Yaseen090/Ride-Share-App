//package com.tiktak24.user.rideSharingPro.fragmentHome;
//
//import android.annotation.SuppressLint;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.Toolbar;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.adapter.ViewPager2Adapter;
//import com.fragments.BaseFragment;
//import com.general.files.ActUtils;
//import com.general.files.CustomDialog;
//import com.general.files.FileSelector;
//import com.general.files.GeneralFunctions;
//import com.general.files.MyApp;
//import com.general.files.UploadProfileImage;
//import com.tiktak24.user.MyWalletActivity;
//import com.tiktak24.user.R;
//import com.tiktak24.user.databinding.ActivityRidePublishBinding;
//import com.tiktak24.user.rideSharingPro.RideSharingProHomeActivity;
//import com.tiktak24.user.rideSharingPro.fragment.RidePublishStep1Fragment;
//import com.tiktak24.user.rideSharingPro.fragment.RidePublishStep1MultiStopFragment;
//import com.tiktak24.user.rideSharingPro.fragment.RidePublishStep2Fragment;
//import com.tiktak24.user.rideSharingPro.fragment.RidePublishStep3Fragment;
//import com.tiktak24.user.rideSharingPro.fragment.RidePublishStep4Fragment;
//import com.tiktak24.user.rideSharingPro.model.RideProPublishData;
//import com.service.handler.ApiHandler;
//import com.service.server.ServerTask;
//import com.utils.Logger;
//import com.utils.Utils;
//import com.view.MTextView;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Objects;
//
//public class RideSharingPublishFragment extends BaseFragment {
//
//    public ActivityRidePublishBinding binding;
//    @Nullable
//    private RideSharingProHomeActivity mActivity;
//    private GeneralFunctions generalFunc;
//    private View frgView;
//    public Toolbar mToolbar;
//    private ViewPager2Adapter mViewPager2Adapter;
//    private final ArrayList<Fragment> listOfFrag = new ArrayList<>();
//
//    public RideProPublishData mPublishData;
//    public HashMap<String, String> myRideDataHashMap;
//    public String vImage = "", cImageName = "";
//    public boolean isUploadImageNew = false;
//    private ServerTask currentCallExeWebServer;
//    public String mDistance, mDuration;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (MyApp.getInstance().getCurrentAct() instanceof RideSharingProHomeActivity) {
//            mActivity = (RideSharingProHomeActivity) requireActivity();
//            generalFunc = mActivity.generalFunc;
//        }
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.activity_ride_publish, container, false);
//        frgView = binding.getRoot();
//
//        mPublishData = new RideProPublishData();
//
//        if (getArguments() != null) {
//            myRideDataHashMap = (HashMap<String, String>) getArguments().getSerializable("myRideDataHashMap");
//        }
//
//        if (myRideDataHashMap != null) {
//            setEditData(myRideDataHashMap);
//        }
//
//        initialization();
//        mainDataSet();
//
//        return binding.getRoot();
//    }
//
//    private void setEditData(HashMap<String, String> rideDataHashMap) {
//        assert mActivity != null;
//        ArrayList<RideProPublishData.MultiStopData> multiStopData = new ArrayList<>();
//        multiStopData.get(0).setDestLat(Double.parseDouble(rideDataHashMap.get("tStartLat")));
//        multiStopData.get(0).setDestLat(Double.parseDouble(rideDataHashMap.get("tStartLong")));
//        multiStopData.get(0).setDestAddress(rideDataHashMap.get("tStartLocation"));
//
//        multiStopData.get(1).setDestLat(Double.parseDouble(rideDataHashMap.get("tEndLat")));
//        multiStopData.get(1).setDestLat(Double.parseDouble(rideDataHashMap.get("tEndLong")));
//        multiStopData.get(1).setDestAddress(rideDataHashMap.get("tEndLocation"));
//        mPublishData.setMultiStopData(multiStopData);
//
//        mPublishData.setDateTime(rideDataHashMap.get("StartDate") + " " + rideDataHashMap.get("StartTime"));
//        mPublishData.setPerSeat(rideDataHashMap.get("AvailableSeats"));
//        mPublishData.setRecommendedPrice(rideDataHashMap.get("fPrice"));
//
//        mPublishData.setDocumentIds(rideDataHashMap.get("tDocumentIds"));
//        mPublishData.setDynamicDetailsArray(generalFunc.getJsonArray(rideDataHashMap.get("tDriverDetails")));
//
//        mPublishData.setStartCity(rideDataHashMap.get("tStartCity"));
//        mPublishData.setEndCity(rideDataHashMap.get("tEndCity"));
//    }
//
//    private void initialization() {
//        assert mActivity != null;
//
//        mToolbar = frgView.findViewById(R.id.toolbar);
//        mActivity.setSupportActionBar(mToolbar);
//        ImageView backImgView = frgView.findViewById(R.id.backImgView);
//        backImgView.setVisibility(View.GONE);
//
//        MTextView titleTxt = frgView.findViewById(R.id.titleTxt);
//        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RIDE_SHARE_PUBLISH_TXT"));
//
//        binding.bottomAreaView.setVisibility(View.VISIBLE);
//        binding.loading.setVisibility(View.GONE);
//
//        binding.publishRideBtn.setText(generalFunc.retrieveLangLBl("", "LBL_RIDE_SHARE_PUBLISH_BTN_TXT"));
//        binding.publishRideBtn.setVisibility(View.GONE);
//        addToClickHandler(binding.previousBtn);
//        addToClickHandler(binding.nextBtn);
//        addToClickHandler(binding.publishRideBtn);
//        if (generalFunc.isRTLmode()) {
//            binding.previousBtn.setRotation(0);
//            binding.nextBtn.setRotation(180);
//        }
//
//        mViewPager2Adapter = new ViewPager2Adapter(mActivity.getSupportFragmentManager(), this.getLifecycle(), listOfFrag);
//        binding.rideSharingStepViewPager.setAdapter(mViewPager2Adapter);
//        binding.rideSharingStepViewPager.setUserInputEnabled(false);
//        binding.rideSharingStepViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                binding.headerHTxt.setVisibility(View.GONE);
//                binding.shadowHeaderView.setVisibility(View.GONE);
//                setPagerHeight();
//            }
//        });
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void mainDataSet() {
//        listOfFrag.clear();
//
//        listOfFrag.add(new RidePublishStep1Fragment());
//        listOfFrag.add(new RidePublishStep1MultiStopFragment());
//        listOfFrag.add(new RidePublishStep2Fragment());
//        listOfFrag.add(new RidePublishStep3Fragment());
//        listOfFrag.add(new RidePublishStep4Fragment());
//
//        binding.rideSharingStepViewPager.setOffscreenPageLimit(listOfFrag.size());
//        Objects.requireNonNull(binding.rideSharingStepViewPager.getAdapter()).notifyDataSetChanged();
//        if (listOfFrag.size() >= 2) {
//            binding.previousBtn.setVisibility(View.INVISIBLE);
//            binding.nextBtn.setVisibility(View.VISIBLE);
//            setToolSubTitle();
//        }
//    }
//
//    public void setReturnRideFrag(ArrayList<RideProPublishData.MultiStopData> multiStopData) {
//        new Handler().postDelayed(() -> {
//            if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep1Fragment fragment) {
//                fragment.setReturnRideFrag(multiStopData);
//            }
//        }, 200);
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void setToolSubTitle() {
//        assert mActivity != null;
//
//        int currItemPos = binding.rideSharingStepViewPager.getCurrentItem();
//        if (generalFunc.isRTLmode()) {
//            binding.StepHTxt.setText(listOfFrag.size() + "/" + (currItemPos + 1) + " " + generalFunc.retrieveLangLBl("", "LBL_STEP_TXT"));
//        } else {
//            binding.StepHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_STEP_TXT") + " " + (currItemPos + 1) + "/" + listOfFrag.size() + " ");
//        }
//        if (currItemPos > 0) {
//            binding.previousBtn.setVisibility(View.VISIBLE);
//        } else {
//            binding.previousBtn.setVisibility(View.INVISIBLE);
//        }
//        if ((currItemPos + 1) == listOfFrag.size()) {
//            binding.publishRideBtn.setVisibility(View.VISIBLE);
//            binding.nextBtn.setVisibility(View.GONE);
//        } else {
//            binding.publishRideBtn.setVisibility(View.GONE);
//            binding.nextBtn.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    public void setPagePrevious() {
//        Utils.hideKeyboard(mActivity);
//        binding.rideSharingStepViewPager.setCurrentItem(binding.rideSharingStepViewPager.getCurrentItem() - 1, true);
//        setToolSubTitle();
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    public void setPageNext() {
//        assert mActivity != null;
//        Utils.hideKeyboard(mActivity);
//
//        int currItemPos = binding.rideSharingStepViewPager.getCurrentItem();
//        if (currItemPos == (listOfFrag.size() - 1)) {
//
//            generalFunc.showMessage(binding.bottomAreaView, "Done");
//
//        } else {
//            binding.rideSharingStepViewPager.setCurrentItem(currItemPos + 1, true);
//            setToolSubTitle();
//        }
//    }
//
//    public void setPagerHeight() {
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            Fragment fragment = mViewPager2Adapter.createFragment(binding.rideSharingStepViewPager.getCurrentItem());
//            View childView = fragment.getView();
//            if (childView == null) return;
//
//            int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(childView.getWidth(), View.MeasureSpec.EXACTLY);
//            int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//            childView.measure(wMeasureSpec, hMeasureSpec);
//
//            LinearLayout.LayoutParams lyParams = (LinearLayout.LayoutParams) binding.rideSharingStepViewPager.getLayoutParams();
//            if (lyParams.height != childView.getMeasuredHeight()) {
//                lyParams.height = childView.getMeasuredHeight();
//                binding.rideSharingStepViewPager.setLayoutParams(lyParams);
//            }
//        }, 200);
//    }
//
//    public void onClickView(View view) {
//        int i = view.getId();
//        if (i == binding.previousBtn.getId()) {
//            if (binding.previousBtn.getVisibility() == View.VISIBLE) {
//                setPagePrevious();
//            }
//
//        } else if (i == binding.nextBtn.getId()) {
//            if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep1Fragment fragment) {
//                fragment.checkPageNext();
//            } else if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep1MultiStopFragment fragment) {
//                fragment.checkPageNext();
//            } else if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep2Fragment fragment) {
//                fragment.checkPageNext();
//            } else if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep3Fragment fragment) {
//                fragment.checkPageNext();
//            }
//
//        } else if (i == binding.publishRideBtn.getId()) {
//            if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep4Fragment fragment) {
//                fragment.checkPageNext();
//            }
//        }
//    }
//
//    public void onRecommendedPrice() {
//        assert mActivity != null;
//
//        if (!Utils.checkText(mDistance) || !Utils.checkText(mDuration)) {
//            generalFunc.showMessage(binding.headerHTxt, generalFunc.retrieveLangLBl("", "LBL_REQUEST_FETCH_LOCATION_DETAILS"));
//            return;
//        }
//
//        HashMap<String, Object> parameters = new HashMap<>();
//        parameters.put("type", "GetRideShareRecommendedPrice");
//        parameters.put("distance", mDistance);
//        parameters.put("duration", mDuration);
//
//        ArrayList<RideProPublishData.MultiStopData> mMultiStopData = mPublishData.getMultiStopData();
//        if (mMultiStopData != null) {
//            JSONArray jaStore = new JSONArray();
//            for (int j = 0; j < mMultiStopData.size(); j++) {
//                RideProPublishData.MultiStopData data = mMultiStopData.get(j);
//                try {
//                    JSONObject stopOverPointsObj = new JSONObject();
//                    stopOverPointsObj.put("add", data.getDestAddress());
//                    stopOverPointsObj.put("lat", data.getDestLat());
//                    stopOverPointsObj.put("long", "" + data.getDestLong());
//                    jaStore.put(stopOverPointsObj);
//                } catch (Exception e) {
//                    Logger.e("Exception", "::" + e.getMessage());
//                }
//            }
//            parameters.put("MSP", jaStore);
//        }
//
//        if (currentCallExeWebServer != null) {
//            currentCallExeWebServer.cancel(true);
//            currentCallExeWebServer = null;
//        }
//
//        currentCallExeWebServer = ApiHandler.execute(mActivity, parameters, true, generalFunc, responseString -> {
//            currentCallExeWebServer = null;
//            if (Utils.checkText(responseString)) {
//                if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {
//
//                    mPublishData.setRecommendedPrice("" + generalFunc.getJsonValue("RecommdedPrice", responseString));
//                    mPublishData.setPassengerNo("" + generalFunc.getJsonValue("PassengerNo", responseString));
//                    mPublishData.setRecommdedPriceText("" + generalFunc.getJsonValue("RecommdedPriceText", responseString));
//                    mPublishData.setRecommdedPriceRange("" + generalFunc.getJsonValue("RecommdedPriceRange", responseString));
//                    mPublishData.setPointRecommendedPrice("" + generalFunc.getJsonValue("PointRecommendedPrice", responseString));
//                    mPublishData.setCarDetails("" + generalFunc.getJsonValue("carDetails", responseString));
//                    setPageNext();
//                } else {
//                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
//                }
//            } else {
//                generalFunc.showError();
//            }
//        });
//    }
//
//    public void onFileSelected(Uri mFileUri, String mFilePath, FileSelector.FileType mFileType) {
//        if (listOfFrag.get(binding.rideSharingStepViewPager.getCurrentItem()) instanceof RidePublishStep3Fragment fragment) {
//            fragment.onFileSelected(mFileUri, mFilePath, mFileType);
//        }
//    }
//
//    public void handleImgUploadResponse(String responseString) {
//        assert mActivity != null;
//        if (responseString != null && !responseString.equalsIgnoreCase("")) {
//            if (GeneralFunctions.checkDataAvail(Utils.action_str, responseString)) {
//                CustomDialog customDialog = new CustomDialog(mActivity, mActivity.generalFunc);
//                customDialog.setDetails(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue("message_title", responseString)),
//                        generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)),
//                        generalFunc.retrieveLangLBl("", "LBL_RIDE_SHARE_VIEW_MY_RIDES_TXT"),
//                        generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"),
//                        false, R.drawable.ic_correct_2, false, 1, true);
//                customDialog.setRoundedViewBackgroundColor(R.color.appThemeColor_1);
//                customDialog.setRoundedViewBorderColor(R.color.white);
//                customDialog.setImgStrokWidth(15);
//                customDialog.setBtnRadius(10);
//                customDialog.setIconTintColor(R.color.white);
//                customDialog.setPositiveBtnBackColor(R.color.appThemeColor_2);
//                customDialog.setPositiveBtnTextColor(R.color.white);
//                customDialog.createDialog();
//                customDialog.setPositiveButtonClick(() -> mActivity.setFrag(0));
//                customDialog.setNegativeButtonClick(() -> MyApp.getInstance().restartWithGetDataApp());
//                customDialog.setFullButton(generalFunc.retrieveLangLBl("", "LBL_RETUEN_RIDE_RIDE_SHARE_TEXT"), () -> {
//                    mActivity.setReturnRideFrag(mPublishData.getMultiStopData());
//                }, "", null);
//                customDialog.show();
//            } else {
//                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), generalFunc.retrieveLangLBl("", "LBL_ADD_NOW"),
//                        generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), buttonId -> {
//                            if (buttonId == 0) {
//                                new ActUtils(mActivity).startAct(MyWalletActivity.class);
//                            }
//                        });
//            }
//        } else {
//            generalFunc.showError();
//        }
//    }
//
//    public void sendToPublishRide() {
//
//        ArrayList<RideProPublishData.MultiStopData> multiStopData = mPublishData.getMultiStopData();
//        if (multiStopData == null || binding.loading.getVisibility() == View.VISIBLE) {
//            return;
//        }
//
//        ArrayList<String[]> paramsList = new ArrayList<>();
//        paramsList.add(Utils.generateImageParams("type", "PublishRide"));
//
//        paramsList.add(Utils.generateImageParams("tStartCity", mPublishData.getStartCity()));
//        paramsList.add(Utils.generateImageParams("tEndCity", mPublishData.getEndCity()));
//
//        // Step1 Data
//        paramsList.add(Utils.generateImageParams("tStartLat", "" + multiStopData.get(0).getDestLat()));
//        paramsList.add(Utils.generateImageParams("tStartLong", "" + multiStopData.get(0).getDestLong()));
//        paramsList.add(Utils.generateImageParams("tEndLat", "" + multiStopData.get(multiStopData.size() - 1).getDestLat()));
//        paramsList.add(Utils.generateImageParams("tEndLong", "" + multiStopData.get(multiStopData.size() - 1).getDestLong()));
//
//        // Step2 Data
//        paramsList.add(Utils.generateImageParams("dStartDate", mPublishData.getDateTime()));
//        paramsList.add(Utils.generateImageParams("iAvailableSeats", mPublishData.getPerSeat()));
//        paramsList.add(Utils.generateImageParams("fPrice", mPublishData.getRecommendedPrice()));
//
//        // step3Data
//        paramsList.add(Utils.generateImageParams("tDriverDetails", mPublishData.getDynamicDetailsArray() != null ? mPublishData.getDynamicDetailsArray().toString() : ""));
//
//        // step4Data
//        paramsList.add(Utils.generateImageParams("documentIds", mPublishData.getDocumentIds()));
//        paramsList.add(Utils.generateImageParams("PointRecommendedPrice", mPublishData.getPointRecommendedPrice()));
//
//        UploadProfileImage uploadProfileImage;
//        if (Utils.checkText(vImage)) {
//
//            if (isUploadImageNew) {
//                uploadProfileImage = new UploadProfileImage(true, mActivity, vImage, "TempFile." + Utils.getFileExt(vImage), paramsList);
//            } else {
//                paramsList.add(Utils.generateImageParams("existingCarImageName", cImageName));
//                uploadProfileImage = new UploadProfileImage(true, mActivity, "", "TempFile." + Utils.getFileExt(vImage), paramsList);
//            }
//        } else {
//            uploadProfileImage = new UploadProfileImage(true, mActivity, vImage, "TempFile." + Utils.getFileExt(vImage), paramsList);
//        }
//        uploadProfileImage.execute();
//    }
//}