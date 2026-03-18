package com.tiktak24.user;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.activity.ParentActivity;
import com.dialogs.BottomScheduleDialog;
import com.dialogs.OpenListView;
import com.dialogs.RequestNearestCab;
import com.dialogs.RequestNearestCabTaxiBid;
import com.facebook.ads.AdSize;
import com.fragments.CabSelectionFragment;
import com.fragments.DriverAssignedHeaderFragment;
import com.fragments.DriverDetailFragment;
import com.fragments.MainHeaderFragment;
import com.fragments.PickUpLocSelectedFragment;
import com.general.files.ActUtils;
import com.general.files.AddDrawer;
import com.general.files.CreateAnimation;
import com.general.files.CustomDialog;
import com.general.files.GeneralFunctions;
import com.general.files.GetLocationUpdates;
import com.general.files.HashMapComparator;
import com.general.files.InternetConnection;
import com.general.files.LoadAvailableCab;
import com.general.files.LocalNotification;
import com.general.files.LockableBottomSheetBehavior;
import com.general.files.MyApp;
import com.general.files.OpenAdvertisementDialog;
import com.general.files.PolyLineAnimator;
import com.general.files.RecurringTask;
import com.general.files.SuccessDialog;
import com.general.files.ToolTipDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.map.BitmapDescriptorFactory;
import com.map.Circle;
import com.map.GeoMapLoader;
import com.map.Marker;
import com.map.helper.MarkerAnim;
import com.map.helper.SphericalUtil;
import com.map.minterface.CancelableCallback;
import com.map.minterface.OnMapClickListener;
import com.map.models.CircleOptions;
import com.map.models.LatLng;
import com.map.models.LatLngBounds;
import com.map.models.MarkerOptions;
import com.model.ContactModel;
import com.model.ServiceModule;
import com.model.Stop_Over_Points_Data;
import com.model.getProfilePaymentModel;
import com.model.profileDelegate;
import com.service.handler.ApiHandler;
import com.service.handler.AppService;
import com.service.model.EventInformation;
import com.service.server.ServerTask;
import com.skyfishjy.library.RippleBackground;
import com.tooltip.tooltipdeledgate;
import com.utils.CommonUtilities;
import com.utils.LoadImage;
import com.utils.Logger;
import com.utils.Utils;
import com.view.CreateRoundedView;
import com.view.GenerateAlertBox;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.SelectableRoundedImageView;
import com.view.simpleratingbar.SimpleRatingBar;
import com.view.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

@SuppressLint({"SetTextI18n", "ResourceType", "RedundantCast", "CheckResult", "NotifyDataSetChanged", "UseCompatLoadingForDrawables", "DefaultLocale", "ObsoleteSdkInt"})
@SuppressWarnings({"RedundantCast", "NewApi", "CommentedOutCode", "Convert2Diamond", "ConstantConditions", "ResultOfMethodCallIgnored", "CollectionAddAllCanBeReplacedWithConstructor", "FieldMayBeFinal", "UnnecessarilyQualifiedInnerClassAccess", "UnnecessaryReturnStatement", "rawtypes", "StringOperationCanBeSimplified", "StatementWithEmptyBody", "unchecked"})
public class MainActivity extends ParentActivity implements GeoMapLoader.OnMapReadyCallback, GetLocationUpdates.LocationUpdates, tooltipdeledgate, profileDelegate, BottomScheduleDialog.OndateSelectionListener, OnMapClickListener {


    public static int RENTAL_REQ_CODE = 1234;

    public String currentGeoCodeObject = "";
    public SlidingUpPanelLayout sliding_layout;
    public ImageView userLocBtnImgView;
    public ImageView userTripBtnImgView;
    public Location userLocation;
    public ArrayList<HashMap<String, String>> currentLoadedDriverList;
    public ImageView emeTapImgView;

    public AddDrawer addDrawer;
    public CabSelectionFragment cabSelectionFrag;
    public LoadAvailableCab loadAvailCabs;
    public Location pickUpLocation;
    public String selectedCabTypeId = "";
    public boolean isDestinationAdded = false;
    public String destLocLatitude = "";
    public String destLocLongitude = "";
    public String destAddress = "";

    public String pickUpLocationAddress = "";
    public ArrayList<Stop_Over_Points_Data> stopOverPointsList = new ArrayList<>();
    public String app_type = "Ride";
    public DrawerLayout mDrawerLayout;
    public View loaderView;
    public ImageView pinImgView;
    public ArrayList<HashMap<String, String>> cabTypesArrList = new ArrayList<>();
    public boolean iswallet = false;
    public boolean isUserLocbtnclik = false;
    public String tempPickupGeoCode = "";
    public String tempDestGeoCode = "";
    public boolean isUfx = false;
    public String uberXAddress = "";
    public double uberXlat = 0.0;
    public double uberXlong = 0.0;
    public boolean ishandicap = false;
    public boolean isChildSeat = false;
    public boolean isWheelChair = false;
    public boolean isfemale = false;
    public String timeval = "";
    public DriverAssignedHeaderFragment driverAssignedHeaderFrag;
    public RequestNearestCab requestNearestCab;
    public RequestNearestCabTaxiBid requestNearestCabTaxiBid;
    public boolean isDestinationMode = false;
    public LinearLayout ridelaterHandleView;
    public boolean isUfxRideLater = false;
    public String bookingtype = "";
    public String selectedprovidername = "";
    public String vCurrencySymbol = "";
    public String UfxAmount = "";
    public boolean noCabAvail = false;
    public Location destLocation;
    public boolean isDriverAssigned = false;
    public GenerateAlertBox noCabAvailAlertBox;
    public String SelectDate = "";
    public String sdate = "";
    public String Stime = "";
    public boolean isFirstTime = true;
    public boolean isFirstTimeIsRideOnly = true;
    public String ACCEPT_CASH_TRIPS = "";
    MTextView titleTxt;
    /*public SupportMapFragment map;*/ GetLocationUpdates getLastLocation;
    GeoMapLoader.GeoMap geoMap;
    boolean isFirstLocation = true;
    RelativeLayout dragView;
    RelativeLayout mainArea;
    View otherArea;
    FrameLayout mainContent;
    RelativeLayout uberXDriverListArea;
    public MainHeaderFragment mainHeaderFrag;
    DriverDetailFragment driverDetailFrag;
    public ArrayList<HashMap<String, String>> cabTypeList;
    ArrayList<HashMap<String, String>> uberXDriverList = new ArrayList<>();
    public HashMap<String, String> driverAssignedData;
    public String assignedDriverId = "";
    public String assignedTripId = "";
    String DRIVER_REQUEST_METHOD = "All";
    MTextView uberXNoDriverTxt;
    SelectableRoundedImageView driverImgView;
    RecurringTask allCabRequestTask;
    SendNotificationsToDriverByDist sendNotificationToDriverByDist;
    public String selectedDateTime = "";
    public Date scheduledate;
    String selectedDateTimeZone = "";
    public String cabRquestType = Utils.CabReqType_Now; // Later OR Now
    View rideArea;
    View deliverArea;

    Intent deliveryData;
    String eTripType = "";
    androidx.appcompat.app.AlertDialog alertDialog_surgeConfirm;
    String required_str = "";

    RecyclerView uberXOnlineDriversRecyclerView;
    LinearLayout driver_detail_bottomView;
    String markerId = "";
    boolean isMarkerClickable = true;
    String currentUberXChoiceType = Utils.Cab_UberX_Type_List;
    String vUberXCategoryName = "";
    Handler ufxFreqTask = null;
    String tripId = "";
    String RideDeliveryType = "";
    SelectableRoundedImageView deliverImgView, deliverImgViewsel, rideImgView, rideImgViewsel, otherImageView, otherImageViewsel;
    PickUpLocSelectedFragment pickUpLocSelectedFrag;
    double tollamount = 0.0;
    String tollcurrancy = "";
    boolean isrideschedule = false;
    boolean isreqnow = false;
    ImageView prefBtnImageView;
    androidx.appcompat.app.AlertDialog pref_dialog;
    androidx.appcompat.app.AlertDialog tolltax_dialog;

    boolean isTollCostdilaogshow = false;
    boolean istollIgnore = false;
    boolean isnotification = false;
    boolean isdelivernow = false;
    boolean isdeliverlater = false;
    LinearLayout ridelaterView;
    MTextView rideLaterTxt;
    MTextView btn_type_ridelater;
    public boolean isTripStarted = false;
    boolean isTripEnded = false;
    InternetConnection intCheck;
    boolean isfirstsearch = true;
    boolean isufxpayment = false;
    String appliedPromoCode = "";
    String userComment = "";
    boolean schedulrefresh = false;
    String iCabBookingId = "";
    boolean isRebooking = false;
    String type = "";
    //Noti
    boolean isufxbackview = false;
    String payableAmount = "";
    private String SelectedDriverId = "";
    private String tripStatus = "";
    private String currentTripId = "";
    private ActionBarDrawerToggle mDrawerToggle;

    public RelativeLayout rootRelView;
    public static String PACKAGE_TYPE_ID_KEY = "PACKAGE_TYPE_ID";

    public boolean isUserTripClick = false;
    boolean isTripActive = false;

    public boolean isFirstZoomlevel = true;

    public LinearLayout rduTollbar, pickup_loc_bar;
    ImageView backImgView, edit_location;
    public boolean isMenuImageShow = true;

    public boolean isRental = false;
    public boolean iscubejekRental = false;
    public String eShowOnlyMoto = "";
    public boolean eFly = false;

    public double pickUp_tmpLatitude = 0.0;
    public double pickUp_tmpLongitude = 0.0;
    public String pickUp_tmpAddress = "";

    GenerateAlertBox reqSentErrorDialog = null;
    public String eWalletDebitAllow = "No";
    public String vProfileName = "";
    public String vReasonName = "";
    MTextView filterTxtView;
    public LinearLayout llFilter;
    public boolean isMultiDeliveryTrip = false;
    public String selectedSortValue = "";
    public String selectedSort = "";

    public String ePaymentBy = "Passenger";
    boolean iswalletShow = true;

    public String selectReasonId = "";
    public String vReasonTitle = "";

    public int selectPos = 0;
    public String vImage = "";
    public boolean isPoolCabTypeIdSelected = false;
    public String SERVICE_PROVIDER_FLOW = "";

    RecurringTask allNonFavCabRequestTask;
    boolean requestSentToFavDrivers = false;
    String[] driverIDS = new String[2];

    LinearLayout homeArea, workArea, recentArea;
    MTextView homeTxt, workTxt, recentTxt;

    View cardArea;
    MTextView destSelectTxt;
    LinearLayout destSelectArea;
    View scheduleArea;
    private int filterPosition = 0;

    ArrayList<HashMap<String, String>> arrayList;

    private static final String TAG = "MainActivity";

    public ArrayList<Marker> map_SkyPort_MarkerList = new ArrayList<>();
    ArrayList<View> map_SkyPort_ViewList = new ArrayList<>();

    public String iFromStationId = "";
    public String iToStationId = "";
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    int selectedMarkerPos = -1;
    boolean prevSatate;
    public Intent data;
    int height;
    public int staticPanelHeight;
    public int staticFlyPanelHeight;
    public boolean isPickup;

    public RecyclerView skyPortsListRecyclerView;

    private MTextView tvTitle;
    private MTextView tvNoDetails;
    private MTextView tvSelectedAddress, addFlyStationNote;
    private LinearLayout changeArea, google_banner_container, banner_container;
    private MButton btn_type2;
    int submitBtnId;
    public Location finalAddressLocation = null;
    public String finalAddress = "";
    public String finaliLocationId = "";
    private ArrayList<HashMap<String, String>> dateList = new ArrayList<>();
    private ArrayList<LatLng> flyPortsLocList = new ArrayList<>();
    int pos = -1;
    LockableBottomSheetBehavior bottomSheetBehavior;

    int leftMargin = 0;
    int rightMargin = 0;
    boolean isPreferenceEnable = false;
    public RelativeLayout transperntView;

    boolean isHomeClick = false;
    boolean isWorkClick = false;
    public String eWalletIgnore = "No";
    public View selAddresArea;
    MTextView destHTxt, schedulrHtxt, pickup_from, pickup_location;
    boolean isCameraMove = false;
    boolean isCameraMoveFirstTime = true;

    ImageView imgHome, imgWork, imgrecent;
    public boolean isTaxiBid = false, isVideoConsultEnable = false, eForMedicalService = false, isRidePool = false;

    private String pickup_latitude, pickup_longitude, pickup_address;
    private String selType;

    public String again_Msg = "", eAskToWait = "", DRIVER_IDS = "", time_val = "", showRetryBtn = "", isRetryRequest = "No";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickup_latitude = getIntent().getStringExtra("latitude");
        pickup_longitude = getIntent().getStringExtra("longitude");
        pickup_address = getIntent().getStringExtra("address");

        isHomeClick = getIntent().getBooleanExtra("isHome", false);
        isWorkClick = getIntent().getBooleanExtra("isWork", false);

        isTaxiBid = getIntent().getBooleanExtra("isTaxiBid", false);
        isVideoConsultEnable = getIntent().getBooleanExtra("isVideoConsultEnable", false);
        eForMedicalService = getIntent().getBooleanExtra("eForMedicalService", false);
        isRidePool = getIntent().getBooleanExtra("isRidePool", false);
        eFly = getIntent().getBooleanExtra("eFly", false);
        selType = getIntent().getStringExtra("selType");
        if (selType != null && selType.equalsIgnoreCase("rental")) {
            iscubejekRental = true;
        }
        pickup_loc_bar = (LinearLayout) findViewById(R.id.pickup_loc_bar);
        pickup_from = (MTextView) findViewById(R.id.pickup_from);
        pickup_from.setText(generalFunc.retrieveLangLBl("", "LBL_PICK_UP_FROM"));
        pickup_location = (MTextView) findViewById(R.id.pickup_location);
        pickup_location.setText(pickup_address);

        showBookingLaterArea();
        reDirectAction();

        transperntView = findViewById(R.id.transperntView);
        cabSelectionFrag = null;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        staticPanelHeight = (int) (height * 0.5);

        destHTxt = findViewById(R.id.destHTxt);
        schedulrHtxt = findViewById(R.id.schedulrHtxt);
        selAddresArea = findViewById(R.id.selAddresArea);
        cardArea = findViewById(R.id.cardArea);
        destSelectTxt = (MTextView) findViewById(R.id.destSelectTxt);
        destSelectArea = (LinearLayout) findViewById(R.id.destSelectArea);
        addToClickHandler(destSelectTxt);
        userLocBtnImgView = (ImageView) findViewById(R.id.userLocBtnImgView);


        rootRelView = (RelativeLayout) findViewById(R.id.rootRelView);
        homeArea = (LinearLayout) findViewById(R.id.homeArea);
        workArea = (LinearLayout) findViewById(R.id.workArea);
        recentArea = findViewById(R.id.recentArea);
        homeTxt = (MTextView) findViewById(R.id.homeTxt);
        workTxt = (MTextView) findViewById(R.id.workTxt);
        recentTxt = (MTextView) findViewById(R.id.recentTxt);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgWork = (ImageView) findViewById(R.id.imgWork);
        imgrecent = (ImageView) findViewById(R.id.imgrecent);

//        manageVectorImage(imgHome, R.drawable.ic_new_home, R.drawable.ic_new_home_compat);
//        manageVectorImage(imgWork, R.drawable.ic_new_work, R.drawable.ic_new_work_compat);
//        manageVectorImage(imgrecent, R.drawable.ic_recent, R.drawable.ic_recent_compat);
        addToClickHandler(homeArea);
        addToClickHandler(workArea);
        addToClickHandler(recentArea);


        isTripActive = getIntent().getBooleanExtra("isTripActive", false);
        rduTollbar = (LinearLayout) findViewById(R.id.rduTollbar);
        backImgView = (ImageView) findViewById(R.id.backImgView);
        titleTxt = (MTextView) findViewById(R.id.titleTxt);
        prefBtnImageView = (ImageView) findViewById(R.id.prefBtnImageView);
        addToClickHandler(backImgView);
        filterTxtView = (MTextView) findViewById(R.id.filterTxtView);
        llFilter = (LinearLayout) findViewById(R.id.llFilter);
        filterTxtView.setText(generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
        addToClickHandler(filterTxtView);
        selectedSortValue = generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT");

        if (getIntent().getStringExtra("iCabBookingId") != null) {
            iCabBookingId = getIntent().getStringExtra("iCabBookingId");
        }

        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
            bookingtype = getIntent().getStringExtra("type");
        }
        manageRideArea(true);
        getUserProfileJson();
        flyElementsInit();

        SERVICE_PROVIDER_FLOW = generalFunc.getJsonValueStr("SERVICE_PROVIDER_FLOW", obj_userProfile);

        app_type = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);

        if (app_type.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {

            String advertise_banner_data = generalFunc.getJsonValueStr("advertise_banner_data", obj_userProfile);
            if (advertise_banner_data != null && !advertise_banner_data.equalsIgnoreCase("")) {

                if (generalFunc.getJsonValue("image_url", advertise_banner_data) != null && !generalFunc.getJsonValue("image_url", advertise_banner_data).equalsIgnoreCase("")) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("image_url", generalFunc.getJsonValue("image_url", advertise_banner_data));
                    map.put("tRedirectUrl", generalFunc.getJsonValue("tRedirectUrl", advertise_banner_data));
                    map.put("vImageWidth", generalFunc.getJsonValue("vImageWidth", advertise_banner_data));
                    map.put("vImageHeight", generalFunc.getJsonValue("vImageHeight", advertise_banner_data));
                    new OpenAdvertisementDialog(getActContext(), map, generalFunc);
                }
            }
            MyApp.getInstance().showOutsatandingdilaog(backImgView);
        } else {
            userLocBtnImgView.setVisibility(View.GONE);

        }

        isRebooking = getIntent().getBooleanExtra("isRebooking", false);
        intCheck = new InternetConnection(getActContext());
        isufxpayment = getIntent().getBooleanExtra("isufxpayment", false);

        isUfx = getIntent().getBooleanExtra("isufx", false);

        isnotification = getIntent().getBooleanExtra("isnotification", false);


        if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            RideDeliveryType = Utils.CabGeneralType_Ride;
        }

        if (selType != null && !isTripActive) {

            if (getIntent().getBooleanExtra("emoto", false)) {
                eShowOnlyMoto = "Yes";
            }

            RideDeliveryType = selType;
            rduTollbar.setVisibility(View.GONE);
            //bug_002 start
            if (selType.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_RIDE"));

                if (getIntent().getBooleanExtra("emoto", false)) {
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_RIDE"));
                } else if (eFly) {
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_FLY_RIDE"));
                }
                //rduTollbar.setVisibility(View.VISIBLE);
            } else if (selType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SERVICE_PROVIDER_TXT"));


            } else if (selType.equalsIgnoreCase("rental")) {
                isRental = true;
                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_RENTAL"));
                if (getIntent().getBooleanExtra("emoto", false)) {
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_RENTAL"));
                }
                RideDeliveryType = Utils.CabGeneralType_Ride;
                iscubejekRental = true;
                // rduTollbar.setVisibility(View.VISIBLE);
            } else {


                if (getIntent().hasExtra("fromMulti")) {

//                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));
//                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));

//                    if (getIntent().getStringExtra("maxDestination") != null && getIntent().getStringExtra("maxDestination").equals("1")) {
//                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_MULTI_SINGLE_OPTION_TITLE_TXT"));
//                    } else {
//                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_MULTI_OPTION_TITLE_TXT"));
//                    }
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MULTI_NEW_BOOKING_TXT"));

                    if (generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")) {
                        if (ServiceModule.isDeliveronly()) {
                            if (getIntent().getStringExtra("maxDestination") != null && getIntent().getStringExtra("maxDestination").equals("1")) {
                                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELIVERY_SEND_PARCEL_SINGLE_TITLE"));
                            } else {
                                titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DELIVERY_SEND_PARCEL_MULTI_TITLE"));
                            }
                        }
                    }

                    manageRideArea(false);
                    rduTollbar.setVisibility(View.VISIBLE);


                } else {
                    manageRideArea(true);
                    titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_DELIVERY"));
                    reSetButton(false);
                }

                prefBtnImageView.setVisibility(View.GONE);
                //rduTollbar.setVisibility(View.VISIBLE);

                if (getIntent().getBooleanExtra("emoto", false)) {
                    if (getIntent().hasExtra("fromMulti")) {
                        manageRideArea(false);
//                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_DELIVERY") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));
//                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SEND_TXT") + "-" + generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MULTI_DELIVERY"));
//                        if (getIntent().getStringExtra("maxDestination") != null && getIntent().getStringExtra("maxDestination").equals("1")) {
//                            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SEND_TXT") + "-" + generalFunc.retrieveLangLBl("", "LBL_MULTI_SINGLE_OPTION_TITLE_TXT"));
//                        } else {
//                            titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_SEND_TXT") + "-" + generalFunc.retrieveLangLBl("", "LBL_MULTI_OPTION_TITLE_TXT"));
//                        }
                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_MULTI_NEW_BOOKING_TXT"));
                    } else {
                        manageRideArea(true);
                        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HEADER_RDU_MOTO_DELIVERY"));
                        reSetButton(false);
                    }
                }
            }
            //bug_002 stop
            isMenuImageShow = false;
        }

        if (getIntent().hasExtra("tripId")) {
            tripId = getIntent().getStringExtra("tripId");
        }
        String TripDetails = generalFunc.getJsonValueStr("TripDetails", obj_userProfile);

        if (TripDetails != null && !TripDetails.equals("")&& !generalFunc.getJsonValue("iTripId", TripDetails).equalsIgnoreCase("")&& generalFunc.getJsonValue("iTripId", TripDetails) !=null) {
            tripId = generalFunc.getJsonValue("iTripId", TripDetails);
        }

        mainContent = (FrameLayout) findViewById(R.id.mainContent);
        userLocBtnImgView = (ImageView) findViewById(R.id.userLocBtnImgView);
        userTripBtnImgView = (ImageView) findViewById(R.id.userTripBtnImgView);
        edit_location = (ImageView) findViewById(R.id.edit_location);
        addToClickHandler(edit_location);


        PreferenceButtonEnable();

        if (!isUfx) {
            mainContent.setVisibility(View.VISIBLE);
            // userLocBtnImgView.setVisibility(View.VISIBLE);
        } else {
            prefBtnImageView.setVisibility(View.GONE);
        }

        addDrawer = new AddDrawer(getActContext(), obj_userProfile.toString(), false);

        if (app_type.equalsIgnoreCase("UberX")) {
            addDrawer.configDrawer(true);
            selectedCabTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");
            vUberXCategoryName = getIntent().getStringExtra("vCategoryName");
            setMainHeaderView(true);
        } else {
            addDrawer.configDrawer(false);
        }


        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            if (isUfx) {
                selectedCabTypeId = getIntent().getStringExtra("SelectedVehicleTypeId");
                vUberXCategoryName = getIntent().getStringExtra("vCategoryName");

                setMainHeaderView(true);
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (rduTollbar.getVisibility() == View.VISIBLE) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 1, 2) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getActionBar().setTitle("Closed");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // getActionBar().setTitle("Opened");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        ridelaterView = (LinearLayout) findViewById(R.id.ridelaterView);

        uberXNoDriverTxt = (MTextView) findViewById(R.id.uberXNoDriverTxt);
        deliverImgView = (SelectableRoundedImageView) findViewById(R.id.deliverImgView);
        deliverImgViewsel = (SelectableRoundedImageView) findViewById(R.id.deliverImgViewsel);
        rideImgView = (SelectableRoundedImageView) findViewById(R.id.rideImgView);
        rideImgViewsel = (SelectableRoundedImageView) findViewById(R.id.rideImgViewsel);
        otherImageView = (SelectableRoundedImageView) findViewById(R.id.otherImageView);
        otherImageViewsel = (SelectableRoundedImageView) findViewById(R.id.otherImageViewsel);

        rideLaterTxt = (MTextView) findViewById(R.id.rideLaterTxt);


        ridelaterHandleView = (LinearLayout) findViewById(R.id.ridelaterHandleView);

        btn_type_ridelater = (MTextView) findViewById(R.id.btn_type_ridelater);

        if (type.equals(Utils.CabReqType_Now)) {
            btn_type_ridelater.setText(generalFunc.retrieveLangLBl("", "LBL_BOOK_LATER"));
        } else {
            btn_type_ridelater.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        }


        btn_type_ridelater.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("latitude", pickup_latitude);
            bundle.putString("longitude", pickup_longitude);
            bundle.putString("address", pickup_address);
            bundle.putString("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
            bundle.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
            bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));
            bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
            bundle.putBoolean("isMain", true);

            schedulrefresh = true;
        });

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 35), 2, getActContext().getResources().getColor(R.color.white), deliverImgViewsel);

        deliverImgViewsel.setColorFilter(getActContext().getResources().getColor(R.color.black));

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 30), 2, getActContext().getResources().getColor(R.color.white), deliverImgView);

        deliverImgView.setColorFilter(getActContext().getResources().getColor(R.color.black));

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 35), 2, getActContext().getResources().getColor(R.color.white), rideImgViewsel);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 30), 2, getActContext().getResources().getColor(R.color.white), rideImgView);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 35), 2, getActContext().getResources().getColor(R.color.white), otherImageViewsel);

        new CreateRoundedView(getActContext().getResources().getColor(R.color.white), Utils.dipToPixels(getActContext(), 30), 2, getActContext().getResources().getColor(R.color.white), otherImageView);

        loaderView = findViewById(R.id.loaderView);
        uberXOnlineDriversRecyclerView = (RecyclerView) findViewById(R.id.uberXOnlineDriversRecyclerView);


        sliding_layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        sliding_layout.setCoveredFadeColor(Color.TRANSPARENT);

        dragView = (RelativeLayout) findViewById(R.id.dragView);
        mainArea = (RelativeLayout) findViewById(R.id.mainArea);
        otherArea = findViewById(R.id.otherArea);
        mainContent = (FrameLayout) findViewById(R.id.mainContent);
        driver_detail_bottomView = (LinearLayout) findViewById(R.id.driver_detail_bottomView);
        pinImgView = (ImageView) findViewById(R.id.pinImgView);

        uberXDriverListArea = (RelativeLayout) findViewById(R.id.uberXDriverListArea);
        emeTapImgView = (ImageView) findViewById(R.id.emeTapImgView);
        rideArea = findViewById(R.id.rideArea);
        deliverArea = findViewById(R.id.deliverArea);
        addToClickHandler(prefBtnImageView);
        addToClickHandler(pickup_loc_bar);

        (new GeoMapLoader(this, R.id.mapFragmentContainer)).bindMap(this);

        setGeneralData();
        setLabels();

        if (generalFunc.isRTLmode()) {
            ((ImageView) findViewById(R.id.deliverImg)).setRotation(-180);
            ((ImageView) findViewById(R.id.rideImg)).setRotation(-180);
            ((ImageView) findViewById(R.id.rideImg)).setScaleY(-1);
            ((ImageView) findViewById(R.id.deliverImg)).setScaleY(-1);
        }


        new CreateAnimation(dragView, getActContext(), R.anim.design_bottom_sheet_slide_in, 100, true).startAnimation();
        addToClickHandler(userTripBtnImgView);
        addToClickHandler(userLocBtnImgView);
        addToClickHandler(emeTapImgView);
        addToClickHandler(rideArea);
        addToClickHandler(deliverArea);
        addToClickHandler(otherArea);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            String restratValue_str = savedInstanceState.getString("RESTART_STATE");

            if (restratValue_str != null && !restratValue_str.equals("") && restratValue_str.trim().equals("true")) {
                releaseScheduleNotificationTask();
                generalFunc.restartApp();
            }
        }

        generalFunc.deleteTripStatusMessages();


        String eEmailVerified = generalFunc.getJsonValueStr("eEmailVerified", obj_userProfile);
        String ePhoneVerified = generalFunc.getJsonValueStr("ePhoneVerified", obj_userProfile);
        String RIDER_EMAIL_VERIFICATION = generalFunc.getJsonValueStr("RIDER_EMAIL_VERIFICATION", obj_userProfile);
        String RIDER_PHONE_VERIFICATION = generalFunc.getJsonValueStr("RIDER_PHONE_VERIFICATION", obj_userProfile);

        if (/*(!eEmailVerified.equalsIgnoreCase("YES") && RIDER_EMAIL_VERIFICATION.equalsIgnoreCase("Yes")) ||*/
                (!ePhoneVerified.equalsIgnoreCase("YES") && RIDER_PHONE_VERIFICATION.equalsIgnoreCase("Yes"))) {

            Bundle bn = new Bundle();
            if (!ePhoneVerified.equalsIgnoreCase("YES")) {
                bn.putString("msg", "DO_PHONE_VERIFY");
            }

            showMessageWithAction(mainArea, generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);
        }
        arrayList = populateArrayList();
        getProfilePaymentModel.getProfilePayment(getCurrentCabGeneralType(), this, this, false, false);

        if (generalFunc.getJsonValueStr("ENABLE_FACEBOOK_ADS", obj_userProfile).equalsIgnoreCase("Yes") && app_type.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
            facebooksAdds();
        }
        if (generalFunc.getJsonValueStr("ENABLE_GOOGLE_ADS", obj_userProfile).equalsIgnoreCase("Yes") && app_type.equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
            googleAdds();
        }
    }

    private void reDirectAction() {
        if (getIntent().getBooleanExtra("isWhereTo", false)) {
            LatLng pickupPlaceLocation;

            Bundle bn = new Bundle();
            bn.putString("locationArea", "dest");
            bn.putBoolean("isDriverAssigned", isDriverAssigned);

            double latitude = GeneralFunctions.parseDoubleValue(0.0, pickup_latitude);
            double longitude = GeneralFunctions.parseDoubleValue(0.0, pickup_longitude);
            String address = pickup_address;

            addOrResetStopOverPoints(latitude, longitude, address, true);

            pickupPlaceLocation = new LatLng(latitude, longitude);
            bn.putDouble("lat", pickupPlaceLocation.latitude);
            bn.putDouble("long", pickupPlaceLocation.longitude);
            bn.putString("address", address);

            bn.putString("type", getCurrentCabGeneralType());

            if (isMultiStopOverEnabled()) {
                Gson gson = new Gson();
                String json = gson.toJson(stopOverPointsList);
                bn.putString("stopOverPointsList", json);
                bn.putString("iscubejekRental", "" + iscubejekRental);
                bn.putString("isRental", "" + isRental);
            }

            bn.putBoolean("isSchedule", false);
            bn.putBoolean("isWhereTo", true);
            bn.putBoolean("isAddStop", getIntent().getBooleanExtra("isAddStop", false));
            new ActUtils(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.SEARCH_DEST_LOC_REQ_CODE);
        } else if (getIntent().getBooleanExtra("isShowSchedule", false)) {
            BottomScheduleDialog bottomScheduleDialog = new BottomScheduleDialog(getActContext(), this);
            bottomScheduleDialog.showPreferenceDialog(generalFunc.retrieveLangLBl("", "LBL_SCHEDULE_BOOKING_TXT"), generalFunc.retrieveLangLBl("", "LBL_SET"), generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), "", false, Calendar.getInstance());
        }
    }

    private void addOrResetStopOverPoints(Double latitude, Double longitude, String address, boolean isSource) {
        if (isMultiStopOverEnabled()) {
            Stop_Over_Points_Data stop_over_points_data = new Stop_Over_Points_Data();
            stop_over_points_data.setDestAddress(address);
            stop_over_points_data.setDestLat(latitude);
            stop_over_points_data.setDestLong(longitude);
            stop_over_points_data.setDestLatLong(new LatLng(latitude, longitude));
            stop_over_points_data.setHintLable(isSource ? generalFunc.retrieveLangLBl("", "LBL_PICK_UP_FROM") : generalFunc.retrieveLangLBl("", "LBL_DROP_AT"));
            stop_over_points_data.setAddressAdded(true);
            stop_over_points_data.setDestination(!isSource);
            stop_over_points_data.setRemovable(!isSource);

            if (stopOverPointsList.size() == 1 && isSource) {
                // reSet Source values
                stopOverPointsList.set(0, stop_over_points_data);
            } else if (stopOverPointsList.size() == 1 && !isSource) {
                // Set New Destination values
                stopOverPointsList.add(stop_over_points_data);
            } else if (stopOverPointsList.size() == 2 && !isSource) {
                // reSet Destination values
                stopOverPointsList.set(1, stop_over_points_data);
            } else if (stopOverPointsList.size() == 2 && isSource) {
                // reSet Source values
                stopOverPointsList.set(0, stop_over_points_data);
            } else if (stopOverPointsList.size() < 1) {
                // add Source & destinations
                stopOverPointsList = new ArrayList<>();
                stopOverPointsList.add(stop_over_points_data);

                if (stopOverPointsList.size() == 1) {
                    Stop_Over_Points_Data stop_over_points_data1 = new Stop_Over_Points_Data();
                    stop_over_points_data1.setDestAddress("");
                    stop_over_points_data1.setDestLat(null);
                    stop_over_points_data1.setDestLong(null);
                    stop_over_points_data1.setDestLatLong(null);
                    stop_over_points_data1.setHintLable(generalFunc.retrieveLangLBl("", "LBL_DROP_AT"));
                    stop_over_points_data1.setAddressAdded(false);
                    stop_over_points_data1.setDestination(true);
                    stop_over_points_data1.setRemovable(false);
                    stopOverPointsList.add(stop_over_points_data1);
                }
            }
        }
    }

    private void googleAdds() {
        AdView mAdView;
        google_banner_container = findViewById(R.id.google_banner_container);
        google_banner_container.setVisibility(View.VISIBLE);
        //manage Google ads
        MobileAds.initialize(getActContext());
        mAdView = new AdView(getActContext());
        mAdView.setAdSize(com.google.android.gms.ads.AdSize.FULL_BANNER);
        mAdView.setAdUnitId(generalFunc.getJsonValueStr("GOOGLE_ADMOB_ID", obj_userProfile));
        AdRequest adRequest = new AdRequest.Builder().build();
        google_banner_container.addView(mAdView);
        mAdView.loadAd(adRequest);
    }

    private void facebooksAdds() {
        banner_container = findViewById(R.id.banner_container);
        banner_container.setVisibility(View.VISIBLE);
        com.facebook.ads.AdView adView;
        adView = new com.facebook.ads.AdView(this, "IMG_16_9_APP_INSTALL#" + generalFunc.getJsonValueStr("FACEBOOK_PLACEMENT_ID", obj_userProfile), AdSize.BANNER_HEIGHT_50);
        // Add the ad view to your activity layout
        banner_container.addView(adView);
        // Request an ad
        adView.loadAd();
    }

    public void setAdsView(boolean isVisibility) {
        if (isVisibility) {
            if (google_banner_container != null) {
                google_banner_container.setVisibility(View.VISIBLE);
            }
            if (banner_container != null) {
                banner_container.setVisibility(View.VISIBLE);
            }
        } else {
            if (google_banner_container != null) {
                google_banner_container.setVisibility(View.GONE);
            }
            if (banner_container != null) {
                banner_container.setVisibility(View.GONE);
            }
        }
    }

    private void showBookingLaterArea() {
        scheduleArea = (LinearLayout) findViewById(R.id.scheduleArea);
        addToClickHandler(scheduleArea);
        if (generalFunc.getJsonValueStr("RIDE_LATER_BOOKING_ENABLED", obj_userProfile).equalsIgnoreCase("Yes")) {

            // schedule not show pool ride
            if (eFly || isPoolCabTypeIdSelected || isRidePool || iscubejekRental || isTaxiBid) {
                showRideLaterBtn(false);
            } else {
                showRideLaterBtn(true);
            }
        } else {
            showRideLaterBtn(false);

        }
    }

    private void showRideLaterBtn(boolean show) {
        scheduleArea.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void manageRideArea(boolean isShow) {
        if (isShow) {
            homeArea.setVisibility(View.VISIBLE);
            workArea.setVisibility(View.VISIBLE);
            cardArea.setVisibility(View.VISIBLE);

            if (getIntent().getBooleanExtra("isPlacesLocation", false) || eFly) {
                selAddresArea.setVisibility(View.GONE);
                pickup_loc_bar.setVisibility(View.GONE);
            } else {
                pickup_loc_bar.setVisibility(View.VISIBLE);
                selAddresArea.setVisibility(View.VISIBLE);
            }

            if (getMap() != null) {
                getMap().setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), 62));
            }
        } else {
            homeArea.setVisibility(View.GONE);
            workArea.setVisibility(View.GONE);
            cardArea.setVisibility(View.GONE);
            selAddresArea.setVisibility(View.GONE);
            pickup_loc_bar.setVisibility(View.GONE);

        }

    }

    // Fly changes

    public void showSelectionDialog(Intent data, boolean isPickup) {
        try {

            configDestinationMode(!isPickup);

            String destLoc = isPickup ? pickUpLocation != null ? "" + pickUpLocation.getLatitude() : "" : destLocLatitude;

            if (data == null && Utils.checkText(destLoc)) {
                data = new Intent();
                data.putExtra("Address", isPickup ? pickUpLocationAddress : destAddress);
                data.putExtra("Latitude", isPickup ? pickUpLocation != null ? "" + pickUpLocation.getLatitude() : "" : destLocLatitude);
                data.putExtra("Longitude", isPickup ? pickUpLocation != null ? "" + pickUpLocation.getLongitude() : "" : destLocLongitude);
            } else if (data != null) {
                this.data = data;
            }
            this.isPickup = isPickup;

            if (!eFly) {
                return;
            }


            findViewById(R.id.dragView).setVisibility(View.GONE);
//            findViewById(R.id.dragView1).setVisibility(View.VISIBLE);
            findViewById(R.id.DetailsArea).setVisibility(View.VISIBLE);

            if (data != null) {
                staticFlyPanelHeight = (int) (height * 0.60);
                bottomSheetBehavior.setLocked(false);
                bottomSheetBehavior.setPeekHeight(staticFlyPanelHeight, true);
                if (mainHeaderFrag != null) {
                    mainHeaderFrag.area_source.setVisibility(View.GONE);
                    mainHeaderFrag.area2.setVisibility(View.GONE);
                }
                findViewById(R.id.stationListArea).setVisibility(View.VISIBLE);
                findViewById(R.id.userLocBtnImgView).setVisibility(View.GONE);
                findViewById(R.id.addFlyStationNote).setVisibility(View.GONE);
                findViewById(R.id.popupView).setVisibility(View.VISIBLE);
                findViewById(R.id.swipeArea).setVisibility(View.VISIBLE);

                enableDisableScroll(false);
                setFlySheetHeight();
                setFlyElements(isPickup);
            } else {
                selectedMarkerPos = -1;
                prevSatate = isPickup;
                findViewById(R.id.stationListArea).setVisibility(View.GONE);
                findViewById(R.id.userLocBtnImgView).setVisibility(View.VISIBLE);
                findViewById(R.id.popupView).setVisibility(View.GONE);
                findViewById(R.id.addFlyStationNote).setVisibility(View.VISIBLE);
                findViewById(R.id.swipeArea).setVisibility(View.GONE);

                tvSelectedAddress.setText(!isPickup ? generalFunc.retrieveLangLBl("Add Your Location", "LBL_ADD_DESTINATION_LOCATION_TXT") : generalFunc.retrieveLangLBl("Detecting your Location", "LBL_DETACTING_YOUR_LOCATION"));
                tvTitle.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_DROP_STATION_TXT" : "LBL_FLY_PICKUP_STATION_TXT"));
                btn_type2.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_CONFIRM_LOCATION_TXT" : "LBL_FLY_CONFIRM_PICKUP_TXT"));
                addFlyStationNote.setText(!isPickup ? generalFunc.retrieveLangLBl("Tap on edit icon to enter or select your destination location.", "LBL_ADD_DESTINATION_LOCATION_NOTE_TEXT") : generalFunc.retrieveLangLBl("", "LBL_FETCHING_LOCATION_NOTE_TEXT"));

                staticFlyPanelHeight = (int) (height * 0.40);
                bottomSheetBehavior.setLocked(true);
                bottomSheetBehavior.setPeekHeight(staticFlyPanelHeight);


                setFlySheetHeight();
            }


            try {
                super.onPostResume();
            } catch (Exception e) {
                Logger.e("Exception", "::" + e.getMessage());
            }
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }

    }

    private void setFlyElements(boolean isPickup) {
        skyPortsListRecyclerView = (RecyclerView) findViewById(R.id.skyPortsListRecyclerView);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        skyPortsListRecyclerView.setLayoutManager(mLayoutManager);


        reSetDetails();
    }

    private void flyElementsInit() {

        tvTitle = (MTextView) findViewById(R.id.tvTitle);
        tvNoDetails = (MTextView) findViewById(R.id.tvNoDetails);
        tvSelectedAddress = (MTextView) findViewById(R.id.tvSelectedAddress);
        addFlyStationNote = (MTextView) findViewById(R.id.addFlyStationNote);
        tvSelectedAddress.setText(generalFunc.retrieveLangLBl("", "LBL_DETACTING_YOUR_LOCATION"));
        addFlyStationNote.setText(generalFunc.retrieveLangLBl("", "LBL_FETCHING_LOCATION_NOTE_TEXT"));
        changeArea = (LinearLayout) findViewById(R.id.changeArea);
        MTextView tvMoreStations = (MTextView) findViewById(R.id.tvMoreStations);

        btn_type2 = ((MaterialRippleLayout) findViewById(R.id.btn_type2)).getChildView();
        submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        // init the bottom sheet behavior
        bottomSheetBehavior = (LockableBottomSheetBehavior) BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setHideable(false);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //                        skyPortsListRecyclerView.setNestedScrollingEnabled(true);
                        skyPortsListRecyclerView.setPadding(0, 0, 0, 0);
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        int[] location = new int[2];
                        tvMoreStations.getLocationOnScreen(location);
                        int x = location[0];
                        int y = location[1];

                        int[] location_1 = new int[2];
                        (findViewById(R.id.swipeArea)).getLocationOnScreen(location_1);
                        int x_1 = location_1[0];
                        int y_1 = location_1[1];

                        int subY = y - y_1;
                        skyPortsListRecyclerView.setPadding(0, 0, 0, y - subY);

//                        float padding = Utils.getScreenPixelHeight(getActContext()) - y;

                        //                        skyPortsListRecyclerView.getLayoutManager().smoothScrollToPosition(skyPortsListRecyclerView, new RecyclerView.State(), pos);

//                        skyPortsListRecyclerView.setNestedScrollingEnabled(false);

                        new Handler().postDelayed(() -> skyPortsListRecyclerView.scrollToPosition(pos), 200);

                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        break;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                int[] location = new int[2];
                tvMoreStations.getLocationOnScreen(location);
                int y = location[1];

                int[] location_1 = new int[2];
                (findViewById(R.id.swipeArea)).getLocationOnScreen(location_1);
                int y_1 = location_1[1];

                int subY = y - y_1;
                skyPortsListRecyclerView.setPadding(0, 0, 0, y - subY);

            }
        });

//        tvChangeAddress.setText(generalFunc.retrieveLangLBl("", "LBL_CHANGE"));
        tvMoreStations.setText(generalFunc.retrieveLangLBl("", "LBL_FLY_VIEW_MORE_STATIONS"));

//        btn_type2.setOnClickListener(new setOnClickList());
//        changeArea.setOnClickListener(new setOnClickList());

        btn_type2.setOnClickListener(v -> {
            if (pos == -1) {
                generalFunc.showMessage(skyPortsListRecyclerView, generalFunc.retrieveLangLBl("Please select any 1 skyPort.", "LBL_FLY_WARNING_MSG"));
            } else {
                String address = dateList.get(pos).get("skyPortAddress");
                finalAddress = dateList.get(pos).get("skyPortTitle") + (Utils.checkText(address) ? (" | " + dateList.get(pos).get("skyPortAddress")) : "");
                finaliLocationId = dateList.get(pos).get("iLocationId");

                if (finalAddressLocation == null) {
                    finalAddressLocation = new Location(!isPickup ? "dest" : "");
                }
                finalAddressLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, dateList.get(pos).get("skyPortLatitude")));
                finalAddressLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, dateList.get(pos).get("skyPortLongitude")));

                if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                setSelectedSkyPortPoint(isPickup, finalAddressLocation, finalAddress, finaliLocationId, isPickup);


                if (destLocation == null && pickUpLocation != null) {
                    showSelectionDialog(null, false);
                }
            }
        });

/*

        // Implement it's on touch listener.
        findViewById(R.id.swipeArea).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                enableDisableScroll(true);
                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });


        // Implement it's on touch listener.

        findViewById(R.id.dataArea).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                enableDisableScroll(false);

                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });

        findViewById(R.id.popupView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                enableDisableScroll(false);
                // Return false, then android os will still process click event,
                // if return true, the on click listener will never be triggered.
                return false;
            }
        });

*/

        changeArea.setOnClickListener(v -> {
            if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            changeAddress(isPickup);
        });
    }

    private void reSetDetails() {
        if (!isPickup && data != null) {
            destAddress = data.getStringExtra("Address");
            if (destLocation == null) {
                destLocation = new Location("dest");
            }
            destLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Latitude")));
            destLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Longitude")));

        }


        tvSelectedAddress.setText(!isPickup ? destAddress : Utils.checkText(pickUpLocationAddress) ? pickUpLocationAddress : generalFunc.retrieveLangLBl("", "LBL_DETACTING_YOUR_LOCATION"));

        tvTitle.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_DROP_STATION_TXT" : "LBL_FLY_PICKUP_STATION_TXT"));
        btn_type2.setText(generalFunc.retrieveLangLBl("", !isPickup ? "LBL_FLY_CONFIRM_LOCATION_TXT" : "LBL_FLY_CONFIRM_PICKUP_TXT"));

        getSkyPortsPoints();
    }

    public void getSkyPortsPoints() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "getNearestFlyStations");
        parameters.put("lattitude", !isPickup ? "" + destLocation.getLatitude() : "" + pickUpLocation.getLatitude());
        parameters.put("longitude", !isPickup ? "" + destLocation.getLongitude() : "" + pickUpLocation.getLongitude());
        parameters.put("address", Utils.getText(tvSelectedAddress));

        String iLocationId = "";

        iLocationId = iFromStationId;


        if (Utils.checkText(iLocationId)) {
            parameters.put("iLocationId", iLocationId);
        }


        ApiHandler.execute(getActContext(), parameters, responseString -> {

            if (responseString != null && !responseString.equals("")) {
                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);
                dateList.clear();
                flyPortsLocList.clear();
                pos = -1;


                if (isDataAvail) {

                    JSONArray arr_sky_ports = generalFunc.getJsonArray(Utils.message_str, responseString);

                    if (arr_sky_ports != null && arr_sky_ports.length() > 0) {
                        for (int i = 0; i < arr_sky_ports.length(); i++) {
                            JSONObject obj_temp = generalFunc.getJsonObject(arr_sky_ports, i);

                            String tCentroidLattitude = generalFunc.getJsonValueStr("tCentroidLattitude", obj_temp);
                            String tCentroidLongitude = generalFunc.getJsonValueStr("tCentroidLongitude", obj_temp);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("iLocationId", generalFunc.getJsonValueStr("iLocationId", obj_temp));
                            map.put("skyPortTitle", generalFunc.getJsonValueStr("vLocationName", obj_temp));
                            map.put("skyPortKm", generalFunc.getJsonValueStr("distance", obj_temp));
                            map.put("skyPortLatitude", tCentroidLattitude);
                            map.put("skyPortLongitude", tCentroidLongitude);
                            map.put("skyPortAddress", generalFunc.getJsonValueStr("vLocationAddress", obj_temp));
                            map.put("LBL_AWAY_TXT", generalFunc.retrieveLangLBl("", "LBL_AWAY_TXT"));
                            dateList.add(map);
                            flyPortsLocList.add(new LatLng(GeneralFunctions.parseDoubleValue(0.0, tCentroidLattitude), GeneralFunctions.parseDoubleValue(0.0, tCentroidLongitude)));
                        }

                        addListOfSkyPortPointMarkers(isPickup, dateList);
                        tvNoDetails.setVisibility(View.GONE);
                    }

                    onDataSelect(0, false);
                    loaderView.setVisibility(View.GONE);

                    userLocBtnImgView.performClick();


                } else {
                    tvNoDetails.setVisibility(View.VISIBLE);

                    tvNoDetails.setText(generalFunc.retrieveLangLBl("No station found nearby to this location.", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    loaderView.setVisibility(View.GONE);
                }

            } else {
                generalFunc.showError();
            }
        });

    }

    private void onDataSelect(int position, boolean isGotoLoc) {
        skyPortsListRecyclerView.scrollToPosition(pos);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        pos = position;
        if (isGotoLoc) {
            highlightSelectedSkyPortPointMarkers(isPickup, finalAddressLocation, finalAddress, position);
        }

        skyPortsListRecyclerView.scrollToPosition(position);
        new Handler().postDelayed(() -> skyPortsListRecyclerView.smoothScrollToPosition(position), 150);
    }


    @Override
    public void ontooltipTouch() {
        new ToolTipDialog(getActContext(), generalFunc, generalFunc.retrieveLangLBl("", "LBL_DEL_HELPER"), cabSelectionFrag.cabTypeList.get(cabSelectionFrag.selpos).get("tDeliveryHelperNoteUser") + "");
    }

    @Override
    public void notifyProfileInfoInfo() {
        if (cabSelectionFrag != null && getProfilePaymentModel.getProfileInfo() != null) {
            cabSelectionFrag.manageProfilePayment();
        }
    }

    @Override
    public void onScheduleSelection(String selDateTime, Date date, String iCabBookingId) {
        selectedDateTime = selDateTime;
        scheduledate = date;


        setCabReqType(Utils.CabReqType_Later);
        selectedTime = selDateTime;

        if (cabSelectionFrag != null) {
            cabSelectionFrag.generateCarType();
        }
        checkForSourceLocation(destSelectTxt.getId());

    }

    RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN && rv.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                rv.findChildViewUnder(e.getX(), e.getY()).performClick();
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            enableDisableScroll(false);
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    };

    public void setSelectedSkyPortPoint(boolean isPickup, Location location, String address, String iLocationId, boolean showLocationNameArea) {

        resetMapView();
        getMap().setPadding(0, 0, 0, 0);
        getMap().requestLayout();

        removeSkyPortsPointsFromMap();

        Intent data = new Intent();
        data.putExtra("Address", address);
        data.putExtra("Latitude", "" + location.getLatitude());
        data.putExtra("Longitude", "" + location.getLongitude());
        data.putExtra("isSkip", false);
        data.putExtra("iLocationId", iLocationId);

        mainHeaderFrag.addOrResetListOfAddresses(data, !isPickup);

        if (showLocationNameArea && cabSelectionFrag == null) {
            mainHeaderFrag.showAddressArea();
        }

        releaseInstances(false);
    }

    public void releaseInstances(boolean showHeader) {
        findViewById(R.id.dragView).setVisibility(View.VISIBLE);
//        findViewById(R.id.dragView1).setVisibility(View.GONE);


        if (showHeader && mainHeaderFrag != null) {
            if (isMenuImageShow) {
                mainHeaderFrag.menuImgView.setVisibility(View.VISIBLE);
                mainHeaderFrag.backImgView.setVisibility(View.GONE);
            }

            mainHeaderFrag.setDefaultView();

            pinImgView.setVisibility(View.GONE);
            if (loadAvailCabs != null) {
                selectedCabTypeId = loadAvailCabs.getFirstCarTypeID();
            }
            resetUserLocBtnView();

           /* if (mainHeaderFrag != null) {
                mainHeaderFrag.releaseAddressFinder();
            }*/

            resetMapView();

            if (pickUpLocation != null) {
                getMap().moveCamera(new LatLng(this.pickUpLocation.getLatitude(), this.pickUpLocation.getLongitude(), Utils.defaultZomLevel));
            } else if (userLocation != null) {
                getMap().moveCamera(cameraForUserPosition());
            }
        }

        if (mainHeaderFrag != null && !/*flyStationSelectionFragment.*/isPickup) {
            mainHeaderFrag.handleDestAddIcon();
            mainHeaderFrag.isclickabledest = false;
            mainHeaderFrag.isclickablesource = false;
            configDestinationMode(false);

            reSetButton(false);
        }

        if (findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE) {
            findViewById(R.id.DetailsArea).setVisibility(View.GONE);
            findViewById(R.id.dragView).setVisibility(View.VISIBLE);
            reSetButton(false);
        }

//        if (flyStationSelectionFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(flyStationSelectionFragment).commit();
//            flyStationSelectionFragment = null;
//        }

        enableDisableScroll(false);

        removeSkyPortsPointsFromMap();
        setPanelHeight(0);
    }

    public void addListOfSkyPortPointMarkers(boolean isPickup, ArrayList<HashMap<String, String>> skyPortsPointsList) {
        removeSkyPortsPointsFromMap();

        builder = new LatLngBounds.Builder();

        for (int i = 0; i < skyPortsPointsList.size(); i++) {

            View skyport_marker_view = ((LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_skyports_marker, null);

            MarkerOptions temp_option;
            map_SkyPort_ViewList.add(skyport_marker_view);
            double lat = GeneralFunctions.parseDoubleValue(0.00, skyPortsPointsList.get(i).get("skyPortLatitude"));
            double longi = GeneralFunctions.parseDoubleValue(0.00, skyPortsPointsList.get(i).get("skyPortLongitude"));
            temp_option = new MarkerOptions().position(new LatLng(lat, longi)).title("skyPort_" + i).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActContext(), skyport_marker_view))).anchor(0.00f, 0.20f);

                /*
                temp_dest_option = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));*/
            if (getMap() != null) {
                Marker skyPortsTempMarker = getMap().addMarker(temp_option);
                map_SkyPort_MarkerList.add(skyPortsTempMarker);
                builder.include(skyPortsTempMarker.getPosition());


            }
        }

        if (getMap() != null) {
            /*if (skyPortsPointsList.size() > 0) {
                buildSkyPortBuilder();
            }*/
            new Handler().postDelayed(() -> userLocBtnImgView.performClick(), 500);
        }

    }

    public void highlightSelectedSkyPortPointMarkers(boolean isPickup, Location location, String address, int pos) {


        try {
            if (prevSatate == isPickup && selectedMarkerPos != -1) {

                View view = map_SkyPort_ViewList.get(selectedMarkerPos);
                ImageView mImageGreySource = (ImageView) view.findViewById(R.id.image_grey);
                int Size = Utils.dpToPx(getActContext(), 30);
                mImageGreySource.setLayoutParams(new LinearLayout.LayoutParams(Size, Size));
                mImageGreySource.setImageResource(R.drawable.ic_airport_unselected);
                reSetMapIcon(view);

            }
            selectedMarkerPos = pos;
            prevSatate = isPickup;


            View skyport_marker_view = map_SkyPort_ViewList.get(pos);
            Marker skyportMarker = map_SkyPort_MarkerList.get(pos);

            ImageView mImageGreySource = (ImageView) skyport_marker_view.findViewById(R.id.image_grey);
            int Size = Utils.dpToPx(getActContext(), 45);
            mImageGreySource.setLayoutParams(new LinearLayout.LayoutParams(Size, Size));
            mImageGreySource.setImageResource(R.drawable.ic_airport_selected);

            reSetMapIcon(skyport_marker_view);

            getMap().animateCamera(skyportMarker.getPosition().zoom(12.0f));
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }

//        animateImageView(mImageGreySource);
//        pulseCircleTest(skyportMarker);
    }

    public void reSetMapIcon(View view) {
        map_SkyPort_ViewList.set(selectedMarkerPos, view);

        Marker oldMarker = map_SkyPort_MarkerList.get(selectedMarkerPos);
        oldMarker.remove();

        MarkerOptions temp_option = new MarkerOptions().position(oldMarker.getPosition()).title("skyPort_" + selectedMarkerPos).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActContext(), view))).anchor(0.00f, 0.20f);

        Marker skyPortsTempMarker = getMap().addMarker(temp_option);
        map_SkyPort_MarkerList.set(selectedMarkerPos, skyPortsTempMarker);
    }

    public void buildSkyPortBuilder() {
        if (cabSelectionFrag == null) {
            if (getMap() != null && getMap().getViewTreeObserver().isAlive()) {
                getMap().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("SingleStatementInBlock")
                    @Override
                    public void onGlobalLayout() {

                        boolean isBoundIncluded = true;
                        if (isBoundIncluded) {

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                getMap().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                getMap().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }


                            LatLngBounds bounds = builder.build();


                            LatLng center = bounds.getCenter();

                            LatLng northEast = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), SphericalUtil.computeHeading(center, bounds.northeast));
                            LatLng southWest = SphericalUtil.computeOffset(center, 10 * Math.sqrt(2.0), (180 + (180 + SphericalUtil.computeHeading(center, bounds.southwest))));

                            builder.include(southWest);
                            builder.include(northEast);

                            /*  Method 1 */
//                            getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);

                            int width = metrics.widthPixels;
                            int height = metrics.heightPixels;
                            // Set Padding according to included bounds

                            int padding = (int) (width * 0.25); // offset from edges of the map 10% of screen

                            int h = (int) (height * 0.5);
//                            int h=flyStationSelectionFragment.fragmentHeight;
                            try {
                                /*  Method 3 */
                                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                                padding = (height - ((h + 5) + Utils.dipToPixels(getActContext(), 60))) / 3;
                                getMap().animateCamera(builder.build(), screenWidth, screenHeight, padding);
                            } catch (Exception e) {
                                Logger.e("Exception", "::" + e.getMessage());

                                /*  Method 1 */
                                getMap().moveCamera(builder.build(), width - Utils.dipToPixels(getActContext(), 80), height - ((h + 5) + Utils.dipToPixels(getActContext(), 60)), padding);
                            }


                        }

                    }
                });
            }
        }
    }

    public void removeSkyPortsPointsFromMap() {
        if (map_SkyPort_MarkerList.size() > 0) {
            flyPortsLocList.clear();
            ArrayList<Marker> tempDriverMarkerList = new ArrayList<>();
            tempDriverMarkerList.addAll(map_SkyPort_MarkerList);
            for (int i = 0; i < tempDriverMarkerList.size(); i++) {
                Marker marker_temp = map_SkyPort_MarkerList.get(0);
                marker_temp.remove();
                map_SkyPort_MarkerList.remove(0);
                map_SkyPort_ViewList.remove(0);
            }
        }

        reSetButton(findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE);
    }


    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public void pulseCircleTest(Marker skyportMarker) {

        // move map to point of interest
        getMap().animateCamera(skyportMarker.getPosition().zoom(8.0F));


        // create a circle and start animation
        int color = Color.BLACK;
        float initialRadius = 10;
        float maxRadius = 20000f;
        CircleOptions co = new CircleOptions().center(skyportMarker.getPosition()).radius(initialRadius).strokeColor(color).fillColor(color).strokeWidth(1.0f);
        Circle c = getMap().addCircle(co);
        Circle c2 = getMap().addCircle(co);

        final Handler h = new Handler();
        h.postDelayed(new Fader(h, c, initialRadius, maxRadius, Color.BLUE, co), 300);
        h.postDelayed(new Fader(h, c2, initialRadius, maxRadius, Color.BLUE, co), 750);
    }

    public void changeAddress(boolean isPickup) {

        if (mainHeaderFrag != null) {
            if (isPickup) {
                mainHeaderFrag.isclickablesource = false;
                mainHeaderFrag.pickupLocArea1.performClick();
            } else {
                mainHeaderFrag.isclickabledest = false;
                mainHeaderFrag.destarea.performClick();
            }
        }

    }

    public void enableDisableScroll(boolean b) {
        sliding_layout.setTouchEnabled(b);
        /*
        if (b) {
            sliding_layout.requestFocus();
        }
        else
        {
            sliding_layout.clearFocus();
        }*/
      /*  sliding_layout.setDragView(b?R.id.dragView1:null);
        sliding_layout.setScrollableView(b?(flyStationSelectionFragment!=null?flyStationSelectionFragment.skyPortsListRecyclerView:null):null);*/
    }

    private class Fader implements Runnable {
        private float radius, initialRadius, maxRadius;
        private int baseColor, color, initialColor;
        private Handler h;
        private Circle c;
        private float radiusJump = 400;
        int numIncrements, alphaIncrement;
        private CircleOptions co;

        public Fader(Handler h, Circle c, float initialRadius, float maxRadius, int initialColor, CircleOptions co) {
            this.initialRadius = initialRadius;
            this.initialColor = initialColor;
            this.maxRadius = maxRadius;
            this.h = h;
            this.c = c;
            this.co = co;
            reset();
        }

        private void reset() {
            radius = initialRadius;
            this.color = initialColor;
            this.baseColor = initialColor;
            numIncrements = (int) ((maxRadius - initialRadius) / radiusJump);
            alphaIncrement = 0x100 / numIncrements;
            if (alphaIncrement <= 0) alphaIncrement = 1;
        }

        public void run() {
            int alpha = Color.alpha(color);
            radius = radius + radiusJump;
            c.setRadius(radius);
            alpha -= alphaIncrement;
            color = Color.argb(alpha, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor));
            c.setFillColor(color);
            c.setStrokeColor(color);

            if (radius < maxRadius) {
                h.postDelayed(this, 25);
            } else {
                c.remove();
                reset();
                c = getMap().addCircle(co);
                h.postDelayed(this, 2000);
            }

            //done
        }
    }

    public void animateImageView(final ImageView v) {

        int colorFrom = getResources().getColor(R.color.gray);
        int colorTo = getResources().getColor(R.color.btnnavtripselcolor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(animator -> {
//                v.setBackgroundColor((int) animator.getAnimatedValue());
            v.setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.SRC_ATOP);
        });
        colorAnimation.start();


        Animator animatorScaleX = ObjectAnimator.ofFloat(v, View.SCALE_X, 1.5f, 1.f);
        Animator animatorScaleY = ObjectAnimator.ofFloat(v, View.SCALE_Y, 1.5f, 1.f);
        Animator animator = ObjectAnimator.ofFloat(v, View.ALPHA, 0.f, 1.f).setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                v.setVisibility(View.VISIBLE);
                //invalidate();
            }
        });
        AnimatorSet showAnimatorSet = new AnimatorSet();
        showAnimatorSet.playTogether(animator, animatorScaleX, animatorScaleY);

       /* Animation scaleAnimation = AnimationUtils.loadAnimation(getActContext(), R.anim.pulse);
        scaleAnimation.setDuration(100);*/

        /*
        final int black = getResources().getColor(R.color.btnnavtripselcolor);

        final ValueAnimator colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(black, mul);
                v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
                if (mul == 0.0) {
                    v.setColorFilter(null);
                }
            }
        });

        colorAnim.setDuration(1500);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();*/

    }


    public void addcabselectionfragment() {

        pickup_loc_bar.setVisibility(View.GONE);

        setRiderDefaultView();

        // Map Height resetting n Backpress done by user then app crashes
        if (isMultiDelivery() && isFinishing() || isDestroyed()) {
            return;
        }

        resetMapView();
        getMap().setPadding(0, 0, 0, Utils.dipToPixels(getActContext(), 280));

        getMap().requestLayout();
    }

    public void setSelectedDriverId(String driver_id) {
        SelectedDriverId = driver_id;
    }

    public void setLabels() {
        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        ((MTextView) findViewById(R.id.rideTxt)).setText(generalFunc.retrieveLangLBl("Ride", "LBL_RIDE"));
        ((MTextView) findViewById(R.id.selrideTxt)).setText(generalFunc.retrieveLangLBl("Ride", "LBL_RIDE"));
        ((MTextView) findViewById(R.id.deliverTxt)).setText(generalFunc.retrieveLangLBl("Deliver", "LBL_DELIVER"));
        ((MTextView) findViewById(R.id.otherTxt)).setText(generalFunc.retrieveLangLBl("Other", "LBL_SERVICES"));

        destHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_DROP_OFF_LOCATION_TXT"));
        schedulrHtxt.setText(generalFunc.retrieveLangLBl("schedule", "LBL_SCHEDULE"));
        homeTxt.setText(generalFunc.retrieveLangLBl("", "LBL_HOME"));
        workTxt.setText(generalFunc.retrieveLangLBl("", "LBL_OFFICE"));
        destSelectTxt.setText(generalFunc.retrieveLangLBl("", "LBL_WHERE_TO"));

        String work_address_str = generalFunc.retrieveValue("userWorkLocationAddress");
        String home_address_str = generalFunc.retrieveValue("userHomeLocationAddress");
        // homeAddressTxt.setText(home_address_str);
        // workAddressTxt.setText(work_address_str);
        recentTxt.setText(generalFunc.retrieveLangLBl("Recent", "LBL_RECENT"));

        if (type.equals(Utils.CabReqType_Now)) {
            if (generalFunc.getJsonValueStr("RIDE_LATER_BOOKING_ENABLED", obj_userProfile).equalsIgnoreCase("Yes")) {
                rideLaterTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_PROVIDERS_AVAIL_NOW"));


                btn_type_ridelater.setVisibility(View.VISIBLE);
            } else {

                rideLaterTxt.setText(generalFunc.retrieveLangLBl("", "LBL_NO_PROVIDERS_AVAIL"));


                btn_type_ridelater.setVisibility(View.GONE);
            }
        } else {

            rideLaterTxt.setText(generalFunc.retrieveLangLBl("", SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider") ? "LBL_NO_PROVIDER_AVA_AT_LOCATION" : "LBL_NO_PROVIDERS_AVAIL_LATER"));


            btn_type_ridelater.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        try {
            outState.putString("RESTART_STATE", "true");
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }
    }

    public void setGeneralData() {
        HashMap<String, String> storeData = new HashMap<>();
        storeData.put(Utils.MOBILE_VERIFICATION_ENABLE_KEY, generalFunc.getJsonValueStr("MOBILE_VERIFICATION_ENABLE", obj_userProfile));
        String DRIVER_REQUEST_METHOD = generalFunc.getJsonValueStr("DRIVER_REQUEST_METHOD", obj_userProfile);

        this.DRIVER_REQUEST_METHOD = DRIVER_REQUEST_METHOD.equals("") ? "All" : DRIVER_REQUEST_METHOD;

        storeData.put(Utils.REFERRAL_SCHEME_ENABLE, generalFunc.getJsonValueStr("REFERRAL_SCHEME_ENABLE", obj_userProfile));
        storeData.put(Utils.WALLET_ENABLE, generalFunc.getJsonValueStr("WALLET_ENABLE", obj_userProfile));
        storeData.put(Utils.SMS_BODY_KEY, generalFunc.getJsonValueStr(Utils.SMS_BODY_KEY, obj_userProfile));
        generalFunc.storeData(storeData);
    }

    public MainHeaderFragment getMainHeaderFrag() {
        return mainHeaderFrag;
    }

    private void openBottomView() {
        if (driver_detail_bottomView == null) {
            return;
        }
        Animation bottomUp = AnimationUtils.loadAnimation(getActContext(), R.anim.slide_up_anim);
        driver_detail_bottomView.startAnimation(bottomUp);
        driver_detail_bottomView.setVisibility(View.VISIBLE);
    }

    boolean isFirst = true;

    @Override
    public void onMapReady(GeoMapLoader.GeoMap geoMap) {

        (findViewById(R.id.LoadingMapProgressBar)).setVisibility(View.GONE);

        if (geoMap == null) {
            return;
        }

        this.geoMap = geoMap;

        if (isUfx) {
            if (getIntent().getStringExtra("SelectDate") != null) {
                SelectDate = getIntent().getStringExtra("SelectDate");
            }
            if (pickUpLocation == null) {
                Location temploc = new Location("PickupLoc");
                if (pickup_latitude != null) {
                    temploc.setLatitude(GeneralFunctions.parseDoubleValue(0.0, pickup_latitude));
                    temploc.setLatitude(GeneralFunctions.parseDoubleValue(0.0, getIntent().getStringExtra("lat")));
                    temploc.setLongitude(GeneralFunctions.parseDoubleValue(0.0, pickup_longitude));
                    onLocationUpdate(temploc);
//                    pickUpLocation = temploc;
//                    pickUpLocationAddress = pickup_address;
                }
            }
        }


        if (generalFunc.checkLocationPermission(true)) {
            getMap().setMyLocationEnabled(true);
            getMap().getUiSettings().setTiltGesturesEnabled(false);
            getMap().getUiSettings().setCompassEnabled(false);
            getMap().getUiSettings().setMyLocationButtonEnabled(false);


            if (eFly) {
//                getMap().getUiSettings().setZoomGesturesEnabled(false);
            }

            getMap().setOnMarkerClickListener(marker -> {
                marker.hideInfoWindow();

                if (isUfx) {
                    if (isMarkerClickable) {
                        openBottomView();
                        markerId = marker.getId();
                        setBottomView(marker);
                    }
                } else {
                    try {

                        getMap().getUiSettings().setMapToolbarEnabled(false);
                        if (marker.getTag().equals("1")) {
                            if (mainHeaderFrag != null) {
                                mainHeaderFrag.pickupLocArea1.performClick();
                            }

                        } else if (marker.getTag().equals("2")) {
                            if (mainHeaderFrag != null) {
                                mainHeaderFrag.destarea.performClick();
                            }
                        }
                    } catch (Exception ignored) {

                    }

                }
                return true;
            });

            getMap().setOnMapClickListener(this);

            getMap().setOnCameraMoveStartedListener(i -> {
                if (!isCameraMoveFirstTime) {
                    manageUserLocBtn(isCameraMove);
                    isCameraMove = true;
                }
                isCameraMoveFirstTime = false;
            });
        }

        if (isUfx) {
            if (isFirst) {
                isFirst = false;
                initializeLoadCab();
            }
        }

        String vTripStatus = generalFunc.getJsonValueStr("vTripStatus", obj_userProfile);

        if (vTripStatus != null && (vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip"))) {
            getMap().setMyLocationEnabled(false);
            String tripDetailJson = generalFunc.getJsonValueStr("TripDetails", obj_userProfile);

            if (tripDetailJson != null && !tripDetailJson.trim().equals("")) {
                double latitude = GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("tStartLat", tripDetailJson));
                double longitude = GeneralFunctions.parseDoubleValue(0.0, generalFunc.getJsonValue("tStartLong", tripDetailJson));
                Location loc = new Location("gps");
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                onLocationUpdate(loc);
            }
        }

        initializeViews();

        checkLocation();
        if (workArea.getVisibility() == View.VISIBLE) {
            if (getMap() != null) {
                getMap().setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen._210sdp));
            }
        }

        if (getIntent().getBooleanExtra("isPlacesLocation", false)) {
            if (pickUpLocation == null) {
                Location temploc = new Location("PickupLoc");
                if (pickup_latitude != null) {
                    temploc.setLatitude(GeneralFunctions.parseDoubleValue(0.0, pickup_latitude));
                    temploc.setLongitude(GeneralFunctions.parseDoubleValue(0.0, pickup_longitude));
                    onLocationUpdate(temploc);
                }
            }
        }
    }

    private void checkLocation() {
        String latitude = pickup_latitude;
        String longitude = pickup_longitude;
        String address = pickup_address;

        if (Utils.checkText(latitude) && Utils.checkText(longitude) && Utils.checkText(address)) {
            if (pickUpLocation == null) {
                if (getIntent().getBooleanExtra("fromMulti", false)) {

                    Location pickUpLoc = new Location("");
                    pickUpLoc.setLatitude(Double.parseDouble(latitude));
                    pickUpLoc.setLongitude(Double.parseDouble(longitude));
                    pickUpLocation = pickUpLoc;

                    new Handler(Looper.myLooper()).postDelayed(() -> {
                        Intent data1 = new Intent();
                        data1.putExtra("Address", address);
                        data1.putExtra("Latitude", latitude);
                        data1.putExtra("Longitude", longitude);
                        data1.putExtra("isSkip", false);
                        mainHeaderFrag.addOrResetListOfAddresses(data1, false);
                    }, 700);

                } else {
                    Location temploc = new Location("PickupLoc");
                    temploc.setLatitude(Double.parseDouble(latitude));
                    temploc.setLongitude(Double.parseDouble(longitude));
                    onLocationUpdate(temploc);

                }
            }
        } else {
            if (generalFunc.isLocationEnabled() && generalFunc.isLocationPermissionGranted(false)) {
                if (getLastLocation != null) {
                    getLastLocation.stopLocationUpdates();
                    getLastLocation = null;
                }
                GetLocationUpdates.locationResolutionAsked = false;
                getLastLocation = new GetLocationUpdates(getActContext(), Utils.LOCATION_UPDATE_MIN_DISTANCE_IN_MITERS, true, this);
            }
        }
    }

    public void manageUserLocBtn(boolean ismove) {

        if (ismove) {


            userLocBtnImgView.setVisibility(View.VISIBLE);
            if (driverDetailFrag != null) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userTripBtnImgView).getLayoutParams();
                params.bottomMargin = Utils.dipToPixels(getActContext(), 5);
                userTripBtnImgView.requestLayout();
            }

        } else {
            userLocBtnImgView.setVisibility(View.GONE);
            if (driverDetailFrag != null) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userTripBtnImgView).getLayoutParams();
                params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
                userTripBtnImgView.requestLayout();
            }

        }


    }

    public void checkDrawerState() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START) == true) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public GeoMapLoader.GeoMap getMap() {
        return this.geoMap;
    }

    public void setShadow() {
        if (cabSelectionFrag != null) {
            cabSelectionFrag.setShadow();
        }
    }

    public void setUserLocImgBtnMargin(int margin) {
    }

    public void initializeLoadCab() {
        if (isDriverAssigned == true) {
            return;
        }

        /*loadAvailCabs = new LoadAvailableCab(getActContext(), generalFunc, selectedCabTypeId, userLocation, getMap(), obj_userProfile.toString());*/
        if (loadAvailCabs == null) {
            loadAvailCabs = new LoadAvailableCab(getActContext(), generalFunc, selectedCabTypeId, userLocation, getMap(), obj_userProfile.toString());
        }

        loadAvailCabs.pickUpAddress = pickUpLocationAddress;
        loadAvailCabs.currentGeoCodeResult = currentGeoCodeObject;
        loadAvailCabs.checkAvailableCabs();
    }

    public void getWalletBalDetails() {

        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "GetMemberWalletBalance");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.app_type);

        ApiHandler.execute(getActContext(), parameters, responseString -> {


            if (responseString != null && !responseString.equals("")) {

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (isDataAvail) {
                    try {
                        generalFunc.storeData(Utils.ISWALLETBALNCECHANGE, "No");
                        String userProfileJson = generalFunc.retrieveValue(Utils.USER_PROFILE_JSON);
                        JSONObject object = generalFunc.getJsonObject(userProfileJson);
                        object.put("user_available_balance", generalFunc.getJsonValue("MemberBalance", responseString));
                        generalFunc.storeData(Utils.USER_PROFILE_JSON, object.toString());

                        getUserProfileJson();


                        setUserInfo();
                    } catch (Exception ignored) {

                    }
                }
            }
        });

    }


    public void showMessageWithAction(View view, String message, final Bundle bn) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(generalFunc.retrieveLangLBl("", "LBL_BTN_VERIFY_TXT"), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActUtils(getActContext()).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_INFO_REQ_CODE);

            }
        });
        snackbar.setActionTextColor(getActContext().getResources().getColor(R.color.verfiybtncolor));
        snackbar.setDuration(10000);
        snackbar.show();
    }

    boolean isIinitializeViewsCall = false;

    public void initializeViews() {
        if (isIinitializeViewsCall) {
            if (pickUpLocation != null && mainHeaderFrag != null) {
                mainHeaderFrag.setSourceAddress(pickUpLocation.getLatitude(), pickUpLocation.getLongitude());
                return;
            }
            return;
        }

        if (pickUpLocation != null && mainHeaderFrag != null) {
            mainHeaderFrag.setSourceAddress(pickUpLocation.getLatitude(), pickUpLocation.getLongitude());
            return;
        }

        isIinitializeViewsCall = true;

        String vTripStatus = generalFunc.getJsonValueStr("vTripStatus", obj_userProfile);


        if (vTripStatus != null && (vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip") || vTripStatus.equals("Arrived"))) {

            JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

            if (tripDetailJson != null) {
                String tripId = generalFunc.getJsonValueStr("iTripId", tripDetailJson);
                String eFly = generalFunc.getJsonValueStr("eFly", tripDetailJson);

                if (vTripStatus.equals("Arrived") && !eFly.equalsIgnoreCase("Yes")) {
                    setMainHeaderView(isMultiDelivery() ? false : isUfx);
                    return;
                }

                eTripType = generalFunc.getJsonValueStr("eType", tripDetailJson);

                this.tripId = tripId;

                if (eTripType.equals("Deliver")) {
                    eTripType = Utils.CabGeneralType_Deliver;
                }

                if (eTripType.equalsIgnoreCase(Utils.eType_Multi_Delivery) && !TextUtils.isEmpty(tripId)) {
                    configureAssignedDriver(true);
                    configureDeliveryView(true);

                    return;
                } else if (!eTripType.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                    configureAssignedDriver(true);
                    configureDeliveryView(true);

                    return;
                }
            }
        }

        if (eFly) {
            showSelectionDialog(null, true);
        }

        setMainHeaderView(isMultiDelivery() ? false : isUfx);

        Utils.runGC();
    }

    private void setMainHeaderView(boolean isUfx) {
        try {
            if (mainHeaderFrag == null) {

                mainHeaderFrag = new MainHeaderFragment();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isUfx", isUfx);
                bundle.putBoolean("isRedirectMenu", true);
                mainHeaderFrag.setArguments(bundle);
                if (getMap() != null) {
                    mainHeaderFrag.setGoogleMapInstance(getMap());
                }
            }
            if (mainHeaderFrag != null) {
                if (getMap() != null) {
                    mainHeaderFrag.releaseAddressFinder();
                }
            }
            try {
                super.onPostResume();
            } catch (Exception e) {
                Logger.e("Exception", "::" + e.getMessage());
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.headerContainer, mainHeaderFrag).commit();

            configureDeliveryView(false);


        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }

    }

    private void setRiderDefaultView() {
        if (cabSelectionFrag == null) {

            if (generalFunc.getJsonValueStr("VEHICLE_TYPE_SHOW_METHOD", obj_userProfile) != null && generalFunc.getJsonValueStr("VEHICLE_TYPE_SHOW_METHOD", obj_userProfile).equalsIgnoreCase("Vertical")) {
                isVerticalCabscroll = true;
            }
            Bundle bundle = new Bundle();
            bundle.putString("RideDeliveryType", RideDeliveryType);
            cabSelectionFrag = new CabSelectionFragment();
            cabSelectionFrag.setArguments(bundle);
            pinImgView.setVisibility(View.GONE);

            if (driverAssignedHeaderFrag != null) {
                userTripBtnImgView.setVisibility(View.VISIBLE);
            }
        }

        if (mainHeaderFrag != null) {
            mainHeaderFrag.addAddressFinder();
        }

        if (driverAssignedHeaderFrag != null) {
            pinImgView.setVisibility(View.GONE);
            if (!driverAssignedHeaderFrag.isMultiDelivery()) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
                params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
                userTripBtnImgView.setVisibility(View.VISIBLE);
            }

        }

        setCurrentType();

        if (!isFinishing() && !isDestroyed() && isMultiDelivery()) {

            if (app_type.equalsIgnoreCase("Ride-Delivery") && generalFunc.isMultiDelivery() && loadAvailCabs != null) {
                loadAvailCabs.onPauseCalled();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.dragView, cabSelectionFrag).commitAllowingStateLoss();

            if (app_type.equalsIgnoreCase("Ride-Delivery") && generalFunc.isMultiDelivery() && loadAvailCabs != null) {
                loadAvailCabs.onResumeCalled();
            }

            configureDeliveryView(false);


        }


//        try {
//            super.onPostResume();
//        } catch (Exception ignored) {
//        }

        if (!isMultiDelivery()) {
            prefBtnImageView.setVisibility(View.GONE);
            userLocBtnImgView.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.dragView, cabSelectionFrag).commit();

            configureDeliveryView(false);

        }

        if (mainHeaderFrag != null && !isMultiDelivery()) {
            mainHeaderFrag.menuBtn.setVisibility(View.GONE);
            mainHeaderFrag.backBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setCurrentType() {

        if (cabSelectionFrag == null) {
            return;
        }
        if (app_type.equalsIgnoreCase("Delivery")) {
            cabSelectionFrag.currentCabGeneralType = "Deliver";
        } else if (app_type.equalsIgnoreCase("UberX")) {
            cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_UberX;
        } else if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) || app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
            if (isDeliver(RideDeliveryType)) {
                cabSelectionFrag.currentCabGeneralType = "Deliver";
            } else {
                cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Ride;
            }
        } else {
            cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Ride;
        }
    }

    public void configureDeliveryView(boolean isHidden) {
        if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {

        }
//        else if (generalFunc.getJsonValue("APP_TYPE", userProfileJson).equalsIgnoreCase("Ride-Delivery") && isHidden == false) {
//            (findViewById(R.id.deliveryArea)).setVisibility(View.VISIBLE);
//            setUserLocImgBtnMargin(190);
//        }
        else {
            (findViewById(R.id.deliveryArea)).setVisibility(View.GONE);
            setUserLocImgBtnMargin(105);
        }

    }

    public void configDestinationMode(boolean isDestinationMode) {
        this.isDestinationMode = isDestinationMode;
        try {
            if (!isDestinationMode) {
                if (loadAvailCabs != null) {
                    loadAvailCabs.filterDrivers(false);
                }
                animateToLocation(getPickUpLocation().getLatitude(), getPickUpLocation().getLongitude());
                if (cabSelectionFrag != null) {
                    noCabAvail = false;
                    changeLable();
                }
            } else {
                pinImgView.setImageResource(R.drawable.pin_dest_select);
                if (cabSelectionFrag != null) {
                    if (loadAvailCabs != null) {
                        if (loadAvailCabs.isAvailableCab) {
                            changeLable();
                            noCabAvail = true;
                        }
                    }
                }

                noCabAvail = !timeval.equalsIgnoreCase("\n" + "--");

                changeLable();
                pinImgView.setImageResource(R.drawable.pin_dest_select);
                if (isDestinationAdded && !getDestLocLatitude().trim().equals("") && !getDestLocLongitude().trim().equals("")) {
                    animateToLocation(GeneralFunctions.parseDoubleValue(0.0, getDestLocLatitude()), GeneralFunctions.parseDoubleValue(0.0, getDestLocLongitude()));
                }

            }
            changeLable();

            if (mainHeaderFrag != null) {
                mainHeaderFrag.configDestinationMode(isDestinationMode);
            }
        } catch (Exception ignored) {

        }
    }

    private void changeLable() {
        if (cabSelectionFrag != null) {
            cabSelectionFrag.setLabels(false);
        }
    }

    public void animateToLocation(double latitude, double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            getMap().animateCamera(new LatLng(latitude, longitude, getMap().getCameraPosition().zoom));
        }
    }

    public void animateToLocation(double latitude, double longitude, float zoom) {
        try {
            if (latitude != 0.0 && longitude != 0.0) {
                getMap().animateCamera(new LatLng(latitude, longitude, zoom));
            }
        } catch (Exception ignored) {

        }
    }

    public void configureAssignedDriver(boolean isAppRestarted) {
        isDriverAssigned = true;
        addDrawer.setIsDriverAssigned(isDriverAssigned);

        if (driverAssignedHeaderFrag != null) {
            driverAssignedHeaderFrag.releaseAllTask();
            driverAssignedHeaderFrag = null;
        }

        manageRideArea(false);

        driverDetailFrag = new DriverDetailFragment();
        driverAssignedHeaderFrag = new DriverAssignedHeaderFragment();

        Bundle bn = new Bundle();
        bn.putString("isAppRestarted", "" + isAppRestarted);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
        //params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
        params.bottomMargin = (int) getResources().getDimension(R.dimen._150sdp);

        if (driverAssignedHeaderFrag != null) {
            userTripBtnImgView.setVisibility(View.VISIBLE);
        }

        driverAssignedData = new HashMap<>();
        releaseScheduleNotificationTask();
        if (isAppRestarted) {

            JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);
            JSONObject driverDetailJson = generalFunc.getJsonObject("DriverDetails", obj_userProfile);
            JSONObject driverCarDetailJson = generalFunc.getJsonObject("DriverCarDetails", obj_userProfile);
            driverAssignedData.put("ePoolRide", generalFunc.getJsonValueStr("ePoolRide", tripDetailJson));
            driverAssignedData.put("eBookingFrom", generalFunc.getJsonValueStr("eBookingFrom", tripDetailJson));

            String vTripPaymentMode = generalFunc.getJsonValueStr("vTripPaymentMode", tripDetailJson);
            String tEndLat = generalFunc.getJsonValueStr("tEndLat", tripDetailJson);
            String tEndLong = generalFunc.getJsonValueStr("tEndLong", tripDetailJson);
            String tDaddress = generalFunc.getJsonValueStr("tDaddress", tripDetailJson);
            driverAssignedData.put("eAskCodeToUser", generalFunc.getJsonValueStr("eAskCodeToUser", tripDetailJson));
            driverAssignedData.put("vRandomCode", generalFunc.getJsonValueStr("vRandomCode", tripDetailJson));


            assignedDriverId = generalFunc.getJsonValueStr("iDriverId", tripDetailJson);
            assignedTripId = generalFunc.getJsonValueStr("iTripId", tripDetailJson);
            eTripType = generalFunc.getJsonValueStr("eType", tripDetailJson);

            if (!tEndLat.equals("0.0") && !tEndLong.equals("0.0") && !tDaddress.equals("Not Set") && !tEndLat.equals("") && !tEndLong.equals("") && !tDaddress.equals("")) {
                isDestinationAdded = true;
                destAddress = tDaddress;
                destLocLatitude = tEndLat;
                destLocLongitude = tEndLong;
            }

            driverAssignedData.put("destLatitude", generalFunc.getJsonValueStr("tEndLat", tripDetailJson));
            driverAssignedData.put("eRental", generalFunc.getJsonValueStr("eRental", tripDetailJson));
            driverAssignedData.put("destLongitude", generalFunc.getJsonValueStr("tEndLong", tripDetailJson));
            driverAssignedData.put("PickUpLatitude", generalFunc.getJsonValueStr("tStartLat", tripDetailJson));
            driverAssignedData.put("PickUpLongitude", generalFunc.getJsonValueStr("tStartLong", tripDetailJson));
            driverAssignedData.put("eFlatTrip", generalFunc.getJsonValueStr("eFlatTrip", tripDetailJson));
            driverAssignedData.put("vDeliveryConfirmCode", generalFunc.getJsonValueStr("vDeliveryConfirmCode", tripDetailJson));
            driverAssignedData.put("recipientNameTxt", generalFunc.getJsonValueStr("Running_Receipent_Detail", tripDetailJson));
            driverAssignedData.put("PickUpAddress", generalFunc.getJsonValueStr("tSaddress", tripDetailJson));
            driverAssignedData.put("vVehicleType", generalFunc.getJsonValueStr("vVehicleType", tripDetailJson));
            driverAssignedData.put("eIconType", generalFunc.getJsonValueStr("eIconType", tripDetailJson));
            driverAssignedData.put("eType", generalFunc.getJsonValueStr("eType", tripDetailJson));
            driverAssignedData.put("DriverTripStatus", generalFunc.getJsonValueStr("vTripStatus", driverDetailJson));
            driverAssignedData.put("DriverPhone", generalFunc.getJsonValueStr("vPhone", driverDetailJson));
            driverAssignedData.put("DriverPhoneCode", generalFunc.getJsonValueStr("vCode", driverDetailJson));
            driverAssignedData.put("DriverRating", generalFunc.getJsonValueStr("vAvgRating", driverDetailJson));
            driverAssignedData.put("DriverAppVersion", generalFunc.getJsonValueStr("iAppVersion", driverDetailJson));
            driverAssignedData.put("DriverLatitude", generalFunc.getJsonValueStr("vLatitude", driverDetailJson));
            driverAssignedData.put("DriverLongitude", generalFunc.getJsonValueStr("vLongitude", driverDetailJson));
            driverAssignedData.put("DriverImage", generalFunc.getJsonValueStr("vImage", driverDetailJson));
            driverAssignedData.put("DriverName", generalFunc.getJsonValueStr("vName", driverDetailJson));
            driverAssignedData.put("iGcmRegId_D", generalFunc.getJsonValueStr("iGcmRegId", driverDetailJson));
            driverAssignedData.put("DriverCarPlateNum", generalFunc.getJsonValueStr("vLicencePlate", driverCarDetailJson));
            driverAssignedData.put("DriverCarName", generalFunc.getJsonValueStr("make_title", driverCarDetailJson));
            driverAssignedData.put("DriverCarModelName", generalFunc.getJsonValueStr("model_title", driverCarDetailJson));
            driverAssignedData.put("DriverCarColour", generalFunc.getJsonValueStr("vColour", driverCarDetailJson));
            driverAssignedData.put("vCode", generalFunc.getJsonValueStr("vCode", driverDetailJson));
            driverAssignedData.put("ePoolRide", generalFunc.getJsonValueStr("ePoolRide", tripDetailJson));
            driverAssignedData.put("iStopId", generalFunc.getJsonValueStr("iStopId", tripDetailJson));
            driverAssignedData.put("eFly", generalFunc.getJsonValueStr("eFly", tripDetailJson));
            driverAssignedData.put("eDestinationMode", generalFunc.getJsonValueStr("eDestinationMode", tripDetailJson));


        } else {

            if (currentLoadedDriverList == null) {
                generalFunc.restartApp();
                return;
            }

            boolean isDriverIdMatch = false;
            for (int i = 0; i < currentLoadedDriverList.size(); i++) {
                HashMap<String, String> driverDataMap = currentLoadedDriverList.get(i);
                String iDriverId = driverDataMap.get("driver_id");

                if (iDriverId.equals(assignedDriverId)) {
                    isDriverIdMatch = true;

                    if (destLocation != null) {

                        driverAssignedData.put("destLatitude", destLocation.getLatitude() + "");
                        driverAssignedData.put("destLongitude", destLocation.getLongitude() + "");
                    }
                    driverAssignedData.put("PickUpLatitude", "" + getPickUpLocation().getLatitude());
                    driverAssignedData.put("PickUpLongitude", "" + getPickUpLocation().getLongitude());

                    if (mainHeaderFrag != null) {
                        driverAssignedData.put("PickUpAddress", mainHeaderFrag.getPickUpAddress());
                    } else {
                        driverAssignedData.put("PickUpAddress", pickUpLocationAddress);
                    }

                    driverAssignedData.put("vVehicleType", generalFunc.getSelectedCarTypeData(selectedCabTypeId, cabTypesArrList, "vVehicleType"));
                    driverAssignedData.put("eIconType", generalFunc.getSelectedCarTypeData(selectedCabTypeId, cabTypesArrList, "eIconType"));
                    driverAssignedData.put("vDeliveryConfirmCode", "");
                    driverAssignedData.put("recipientNameTxt", "");
                    driverAssignedData.put("DriverTripStatus", "");
                    driverAssignedData.put("DriverPhone", driverDataMap.get("vPhone_driver"));
                    driverAssignedData.put("DriverPhoneCode", driverDataMap.get("vPhoneCode_driver"));
                    driverAssignedData.put("DriverRating", driverDataMap.get("average_rating"));
                    driverAssignedData.put("DriverAppVersion", driverDataMap.get("iAppVersion"));
                    driverAssignedData.put("DriverLatitude", driverDataMap.get("Latitude"));
                    driverAssignedData.put("DriverLongitude", driverDataMap.get("Longitude"));
                    driverAssignedData.put("DriverImage", driverDataMap.get("driver_img"));
                    driverAssignedData.put("iGcmRegId_D", driverDataMap.get("iGcmRegId"));

                    driverAssignedData.put("DriverName", driverDataMap.get("Name"));
                    driverAssignedData.put("DriverCarPlateNum", driverDataMap.get("vLicencePlate"));
                    driverAssignedData.put("DriverCarName", driverDataMap.get("make_title"));
                    driverAssignedData.put("DriverCarModelName", driverDataMap.get("model_title"));
                    driverAssignedData.put("DriverCarColour", driverDataMap.get("vColour"));
                    driverAssignedData.put("eType", getCurrentCabGeneralType());
                    driverAssignedData.put("ePoolRide", driverDataMap.get("ePoolRide"));
                    driverAssignedData.put("iStopId", driverDataMap.get("iStopId"));
                    driverAssignedData.put("eFly", driverDataMap.get("eFly"));
                    driverAssignedData.put("eDestinationMode", driverDataMap.get("eDestinationMode"));
                    break;
                }
            }

            if (!isDriverIdMatch) {
                generalFunc.restartApp();
                return;
            }
        }

        driverAssignedData.put("iDriverId", assignedDriverId);
        driverAssignedData.put("iTripId", assignedTripId);

        driverAssignedData.put("PassengerName", generalFunc.getJsonValueStr("vName", obj_userProfile));
        driverAssignedData.put("PassengerImageName", generalFunc.getJsonValueStr("vImgName", obj_userProfile));

        bn.putSerializable("TripData", driverAssignedData);
        driverAssignedHeaderFrag.setArguments(bn);
        driverAssignedHeaderFrag.setGoogleMap(getMap());
        if (!TextUtils.isEmpty(tripId)) {
            driverAssignedHeaderFrag.isBackVisible = true;
        }

        driverDetailFrag.setArguments(bn);


        Location pickUpLoc = new Location("");
        pickUpLoc.setLatitude(GeneralFunctions.parseDoubleValue(0.0, driverAssignedData.get("PickUpLatitude")));
        pickUpLoc.setLongitude(GeneralFunctions.parseDoubleValue(0.0, driverAssignedData.get("PickUpLongitude")));
        this.pickUpLocation = pickUpLoc;

        if (mainHeaderFrag != null) {
            mainHeaderFrag.releaseResources();
            mainHeaderFrag = null;
        }

        if (cabSelectionFrag != null) {
            stopOverPointsList.clear();
            cabSelectionFrag.releaseResources();
            cabSelectionFrag = null;
        }

        Utils.runGC();

        if (isnotification) {
            chatMsg();
        }

        setPanelHeight(175);

        try {
            super.onPostResume();
        } catch (Exception ignored) {

        }

        if (driverDetailFrag != null) {
            deliverArea.setVisibility(View.GONE);
            otherArea.setEnabled(false);
            deliverArea.setEnabled(false);
            rideArea.setEnabled(false);
        }

        if (!isFinishing()) {
            getMap().clear();

            getMap().setMyLocationEnabled(false);

            resetMapView();

            getSupportFragmentManager().beginTransaction().replace(R.id.headerContainer, driverAssignedHeaderFrag).commit();

            if (!isAppRestarted) {
                if (isFixFare) {
                    if (driverAssignedHeaderFrag != null) {
                        driverAssignedHeaderFrag.eConfirmByUser = "Yes";
                        driverAssignedHeaderFrag.handleEditDest();
                    }
                }
            }

            pickup_loc_bar.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.dragView, driverDetailFrag).commit();


            if (driverAssignedHeaderFrag != null) {
                userTripBtnImgView.setVisibility(View.VISIBLE);
            }

            String OPEN_CHAT = generalFunc.retrieveValue("OPEN_CHAT");
            if (Utils.checkText(OPEN_CHAT)) {
                JSONObject OPEN_CHAT_DATA_OBJ = generalFunc.getJsonObject(OPEN_CHAT);
                generalFunc.removeValue("OPEN_CHAT");
               /*
                Bundle bnChat = new Bundle();

                bnChat.putString("iFromMemberId", driverAssignedData.get("iDriverId"));
                bnChat.putString("FromMemberImageName", driverAssignedData.get("DriverImage"));
                bnChat.putString("iTripId", driverAssignedData.get("iTripId"));
                bnChat.putString("FromMemberName", driverAssignedData.get("DriverName"));
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", userProfileJson);
                bnChat.putString("vBookingNo", tripDetailJson != null ? generalFunc.getJsonValueStr("vRideNo", tripDetailJson) : "");
*/
//                if (OPEN_CHAT_DATA_OBJ != null)
//                    new ActUtils(getActContext()).startActWithData(ChatActivity.class, generalFunc.createChatBundle(OPEN_CHAT_DATA_OBJ));
            }

//            setMapPadding(false);

        } else {
            generalFunc.restartApp();
        }


    }

    public void setMapPadding(boolean isBoundToMap) {
        resetMapView();

        int topHeight = Utils.dipToPixels(getActContext(), 10);
        int bottomHeight = Utils.dipToPixels(getActContext(), 10);

        if (driverAssignedHeaderFrag != null && driverAssignedHeaderFrag.getView() != null) {
            if (driverAssignedHeaderFrag.getView() != null) {
                topHeight = Math.max(driverAssignedHeaderFrag.fragmentHeight, driverAssignedHeaderFrag.getView().getMeasuredHeight());
            } else {
                topHeight = driverAssignedHeaderFrag.fragmentHeight;
            }

            if (driverAssignedHeaderFrag.time_marker != null && driverAssignedHeaderFrag.driverMarker != null) {
//                driverAssignedHeaderFrag.time_marker.setPosition(new LatLng(22.502787, 73.051793));
                try {
                    SphericalUtil.Direction direction = SphericalUtil.computeDirection(driverAssignedHeaderFrag.driverMarker.getPosition(), driverAssignedHeaderFrag.time_marker.getPosition());

                    if (direction == SphericalUtil.Direction.UPWARD) {
                        topHeight = topHeight + Utils.dipToPixels(getActContext(), 90);
                    }
                } catch (Exception e) {
                    Logger.e("Exception", "::" + e.getMessage());
                }
            }
        }

        if (driverDetailFrag != null) {
            bottomHeight = bottomHeight + Math.max(232, driverDetailFrag.fragmentHeight);
        }

        getMap().setPadding(Utils.dipToPixels(getActContext(), 35), topHeight, Utils.dipToPixels(getActContext(), 35), bottomHeight);

        if (driverAssignedHeaderFrag != null && driverDetailFrag != null) {
            getMap().setPadding(0, 0, 0, (driverDetailFrag.fragmentHeight - (int) getResources().getDimension(R.dimen._25sdp)));
        }

        if (userLocBtnImgView != null && isBoundToMap) {
            userLocBtnImgView.performClick();
        }
    }

    private void resetMapView() {
        if (getMap() != null && getMap().getView() != null) {
            getMap().invalidate();
            getMap().setPadding(0, 0, 0, 0);
            getMap().requestLayout();
        }
    }

    private void resetUserLocBtnView() {
        userLocBtnImgView.invalidate();
        userLocBtnImgView.requestLayout();
    }

    @Override
    public void onLocationUpdate(Location location) {

        if (location == null) {
            return;
        }

        if (pickup_latitude != null && pickup_longitude != null && !pickup_longitude.equalsIgnoreCase("")) {
            Location loc_ufx = new Location("gps");
            loc_ufx.setLatitude(GeneralFunctions.parseDoubleValue(0.0, pickup_latitude));
            loc_ufx.setLongitude(GeneralFunctions.parseDoubleValue(0.0, pickup_longitude));
            this.userLocation = loc_ufx;
        } else {
            this.userLocation = location;

        }

        if (isFirstLocation) {

            float currentZoomLevel = Utils.defaultZomLevel;


            if (getMap() != null) {
                getMap().moveCamera(new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude(), currentZoomLevel));
            }

            if (pickUpLocation == null) {
                pickUpLocation = this.userLocation;
                initializeViews();
                if (dialog != null && isFromSourceDialog) {
                    dialog.dismiss();
                    checkForSourceLocation(selectedViewID);
                }
            }
            isFirstLocation = false;

            if (isHomeClick) {

                homeArea.performClick();

            } else if (isWorkClick) {
                workArea.performClick();
            }

        }

        if (getIntent().getBooleanExtra("isPlacesLocation", false)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                String Lat = getIntent().getStringExtra("vPlacesLocationLat");
                String Long = getIntent().getStringExtra("vPlacesLocationLong");
                String address1 = getIntent().getStringExtra("vPlacesLocation");

                if (Utils.checkText(Lat) && Utils.checkText(Long) && Utils.checkText(address1)) {
                    setDestinationPoint(Lat, Long, address1, true);
                    if (cabSelectionFrag == null) {
                        addcabselectionfragment();
                    }
                }
            }, 1);
        }
    }


    public void setETA(String time) {

        timeval = time;

        if (cabSelectionFrag != null) {
            cabSelectionFrag.handleSourceMarker(time);
            if (!(isMultiDelivery())) {
                cabSelectionFrag.mangeMrakerPostion();
            }

        }
    }

    public LatLng cameraForUserPosition() {

        try {
            if (cabSelectionFrag != null) {
                return null;
            }

            double currentZoomLevel = Utils.defaultZomLevel;

            String TripDetails = generalFunc.getJsonValueStr("TripDetails", obj_userProfile);

            String vTripStatus = generalFunc.getJsonValueStr("vTripStatus", obj_userProfile);
            if (generalFunc.isLocationEnabled()) {

                double startLat = 0.0;
                double startLong = 0.0;

                if (vTripStatus != null && startLat != 0.0 && startLong != 0.0 && ((vTripStatus.equals("Active") || vTripStatus.equals("On Going Trip")))) {

                    Location tempickuploc = new Location("temppickkup");

                    tempickuploc.setLatitude(startLat);
                    tempickuploc.setLongitude(startLong);

                    return new LatLng(tempickuploc.getLatitude(), tempickuploc.getLongitude(), (float) currentZoomLevel);
                } else {
                    currentZoomLevel = Utils.defaultZomLevel;
                    if (userLocation != null) {
                        return new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude(), (float) currentZoomLevel);
                    }
                }
            } else if (userLocation != null) {
//                if (Utils.defaultZomLevel > currentZoomLevel) {
//                    currentZoomLevel = Utils.defaultZomLevel;
//                }
                return new LatLng(this.userLocation.getLatitude(), this.userLocation.getLongitude(), (float) currentZoomLevel);
            }
        } catch (Exception ignored) {

        }
        return new LatLng(0.0, 0.0, Utils.defaultZomLevel);
    }

    public void redirectToMapOrList(String choiceType, boolean autoLoad) {

        manageRideArea(false);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
        if (generalFunc.isRTLmode()) {
            params.leftMargin = Utils.dipToPixels(getActContext(), 10);
        } else {
            params.rightMargin = Utils.dipToPixels(getActContext(), 10);
        }

        if (autoLoad && currentUberXChoiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map)) {
            return;
        }

        this.currentUberXChoiceType = choiceType;

        mainHeaderFrag.listTxt.setBackgroundColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? Color.parseColor("#FFFFFF") : getResources().getColor(R.color.appThemeColor_1));
        mainHeaderFrag.mapTxt.setBackgroundColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? getResources().getColor(R.color.appThemeColor_1) : Color.parseColor("#FFFFFF"));

        if (isVideoConsultEnable) {
            mainHeaderFrag.mapImage.setVisibility(View.GONE);
        } else {
            mainHeaderFrag.mapImage.setVisibility(View.VISIBLE);
        }

        mainHeaderFrag.listImage.setBackground(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? getResources().getDrawable(R.drawable.square_border_bottom) : getResources().getDrawable(R.drawable.square_border_bottom_gray));
        mainHeaderFrag.mapImage.setBackground(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map) ? getResources().getDrawable(R.drawable.square_border_bottom) : getResources().getDrawable(R.drawable.square_border_bottom_gray));
        mainHeaderFrag.filterImage.setBackground(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Filter) ? getResources().getDrawable(R.drawable.square_border_bottom) : getResources().getDrawable(R.drawable.square_border_bottom_gray));

        if (choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map)) {
            mainHeaderFrag.uberXMainHeaderLayout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mainHeaderFrag.uberXMainHeaderLayout.setBackgroundColor(Color.WHITE);
        }

        mainHeaderFrag.listImage.setColorFilter(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? ContextCompat.getColor(getActContext(), R.color.appThemeColor_1) : ContextCompat.getColor(getActContext(), R.color.black));
        mainHeaderFrag.mapImage.setColorFilter(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Map) ? ContextCompat.getColor(getActContext(), R.color.appThemeColor_1) : ContextCompat.getColor(getActContext(), R.color.black));
        mainHeaderFrag.filterImage.setColorFilter(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_Filter) ? ContextCompat.getColor(getActContext(), R.color.appThemeColor_1) : ContextCompat.getColor(getActContext(), R.color.black));


        mainHeaderFrag.mapTxt.setTextColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? Color.parseColor("#FFFFFF") : Color.parseColor("#1C1C1C"));
        mainHeaderFrag.listTxt.setTextColor(choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List) ? Color.parseColor("#1C1C1C") : Color.parseColor("#FFFFFF"));


        if (driver_detail_bottomView != null && driver_detail_bottomView.getVisibility() == View.VISIBLE) {

            driver_detail_bottomView.setVisibility(View.GONE);
        }
        if (choiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List)) {
            if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {
                SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
                mSwipeRefreshLayout.setOnRefreshListener(() -> {

                    //  getFilterList();
                    if (loadAvailCabs != null) {
                        loadAvailCabs.checkAvailableCabs();
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                });
            }

            uberXNoDriverTxt.setText(generalFunc.retrieveLangLBl("No Provider Available", "LBL_NO_PROVIDER_AVAIL_TXT"));

            if (!isUfxRideLater) {

                uberXDriverListArea.setVisibility(View.VISIBLE);
                uberXNoDriverTxt.setVisibility(View.GONE);
                ridelaterView.setVisibility(View.GONE);

                uberXDriverList.clear();

            }

            configDriverListForUfx();

        } else {
            (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
            uberXDriverListArea.setVisibility(View.GONE);
        }
    }

    public void configDriverListForUfx() {

        if (ufxFreqTask != null) {
            return;
        }

        if (isufxbackview) {
            return;
        }

        (findViewById(R.id.llFilter)).setVisibility(View.GONE);
        (findViewById(R.id.driverListAreaLoader)).setVisibility(View.VISIBLE);
        (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);
        ((MTextView) findViewById(R.id.searchingDriverTxt)).setText(generalFunc.retrieveLangLBl("Searching Provider", "LBL_SEARCH_PROVIDER_WAIT_TXT"));
        uberXNoDriverTxt.setVisibility(View.GONE);
        ridelaterView.setVisibility(View.GONE);

        if (currentLoadedDriverList != null) {
            uberXDriverList.addAll(currentLoadedDriverList);

            if (currentLoadedDriverList.size() > 0) {
                llFilter.setVisibility(View.VISIBLE);
            } else {
                llFilter.setVisibility(View.GONE);
            }
        }


        if (uberXDriverList.size() > 0) {
            uberXNoDriverTxt.setVisibility(View.GONE);
            ridelaterView.setVisibility(View.GONE);
            (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
            (findViewById(R.id.searchingDriverTxt)).setVisibility(View.GONE);
        } else {
            if (!isUfxRideLater) {

                if (isfirstsearch) {
                    isfirstsearch = false;
                    (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);
                    ((MTextView) findViewById(R.id.searchingDriverTxt)).setText(generalFunc.retrieveLangLBl("Searching Provider", "LBL_SEARCH_PROVIDER_WAIT_TXT"));
                } else {
                    (findViewById(R.id.searchingDriverTxt)).setVisibility(View.GONE);
                    uberXNoDriverTxt.setVisibility(View.GONE);
                    (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
                    (findViewById(R.id.searchingDriverTxt)).setVisibility(View.GONE);
                    uberXNoDriverTxt.setVisibility(View.GONE);
                    ridelaterView.setVisibility(View.VISIBLE);
                    uberXNoDriverTxt.setVisibility(View.GONE);

                    (findViewById(R.id.driverListAreaLoader)).setVisibility(View.GONE);
                }
            }


        }


        ufxFreqTask = null;
    }

    private void closeBottomView() {

        if (driver_detail_bottomView == null) {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
        params.bottomMargin = Utils.dipToPixels(getActContext(), Utils.dpToPx(getActContext(), 10));
        userLocBtnImgView.requestLayout();
        Animation bottomUp = AnimationUtils.loadAnimation(getActContext(), R.anim.slide_out_down_anim);
        driver_detail_bottomView.startAnimation(bottomUp);
        driver_detail_bottomView.setVisibility(View.GONE);
    }

    private void setBottomView(final Marker marker) {

        if (loadAvailCabs == null) {
            return;
        }
        HashMap<String, String> map = loadAvailCabs.getMarkerDetails(marker);

        if (!isPickUpLocationCorrect() || map.size() == 0) {
            closeBottomView();
            return;
        } else {


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            params.bottomMargin = Utils.dipToPixels(getActContext(), 150);
            if (generalFunc.isRTLmode()) {
                params.leftMargin = Utils.dipToPixels(getActContext(), 10);
            } else {
                params.rightMargin = Utils.dipToPixels(getActContext(), 10);
            }

            userLocBtnImgView.requestLayout();

            SimpleRatingBar bottomViewratingBar = (SimpleRatingBar) findViewById(R.id.bottomratingBar);
            MTextView nameTxt = (MTextView) findViewById(R.id.bottomdriverNameTxt);
            MTextView milesTxt = (MTextView) findViewById(R.id.bottommilesTxt);
            MTextView btnTxt = (MTextView) findViewById(R.id.bottombtnTxt);
            ImageView btnImg = (ImageView) findViewById(R.id.bottombtnImg);
            MTextView priceTxt = (MTextView) findViewById(R.id.bottompriceTxt);

            ImageView driverbottomStatus = (ImageView) findViewById(R.id.bottomdriverStatus);
            TextView eIsFeatured = (TextView) findViewById(R.id.bottomlabelFeatured);
            LinearLayout btnArea = (LinearLayout) findViewById(R.id.bottombtnArea);
            ImageView cancelImg = (ImageView) findViewById(R.id.cancelImg);
            cancelImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeBottomView();
                }
            });


            if (generalFunc.isRTLmode()) {
                btnImg.setRotation(180);
                btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
            }

            if (map.get("fAmount") != null && !map.get("fAmount").trim().equals("")) {
                priceTxt.setText(map.get("fAmount"));
            } else {
                priceTxt.setVisibility(View.GONE);
            }

            String LBL_FEATURED_TXT = generalFunc.retrieveLangLBl("Featured", "LBL_FEATURED_TXT");

            if (map.get("eIsFeatured").equalsIgnoreCase("Yes")) {
                String LANGUAGE_IS_RTL_KEY = generalFunc.retrieveValue(Utils.LANGUAGE_IS_RTL_KEY);
                eIsFeatured.setText(LBL_FEATURED_TXT);
                eIsFeatured.setVisibility(View.VISIBLE);
            } else if (map.get("eIsFeatured").equalsIgnoreCase("No")) {
                eIsFeatured.setVisibility(View.GONE);
            }

            bottomViewratingBar.setRating(GeneralFunctions.parseFloatValue(0, map.get("average_rating")));
            btnTxt.setText(generalFunc.retrieveLangLBl("More Info", "LBL_MORE_DETAILS"));

            nameTxt.setText(map.get("Name") + " " + map.get("LastName"));
            LikeButton likeButton = (LikeButton) findViewById(R.id.likeButtonbottom);
            if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes") && map.get("eFavDriver").equalsIgnoreCase("Yes")) {
                likeButton.setVisibility(View.VISIBLE);
                likeButton.setLiked(true);
                likeButton.setEnabled(false);
            } else {
                likeButton.setVisibility(View.GONE);
            }

            double SourceLat = pickUpLocation.getLatitude();
            double SourceLong = pickUpLocation.getLongitude();
            double DesLat = GeneralFunctions.parseDoubleValue(0.0, map.get("Latitude"));
            double DesLong = GeneralFunctions.parseDoubleValue(0.0, map.get("Longitude"));
            //   milesTxt.setText(generalFunc.CalculationByLocationKm(SourceLat, SourceLong, DesLat, DesLong) + " km away");

            /*if (userProfileJson != null && !generalFunc.getJsonValue("eUnit", userProfileJson).equalsIgnoreCase("KMs")) {
                milesTxt.setText(GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "Miles") + generalFunc.retrieveLangLBl("km away", "LBL_MILE_DISTANCE_TXT"));
            } else {
                milesTxt.setText(GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "KM") + generalFunc.retrieveLangLBl("km away", "LBL_KM_AWAY"));

            }*/

            if (generalFunc.getJsonValueStr("eUnit", obj_userProfile).equals("KMs")) {
                milesTxt.setText(String.format("%.2f", (float) GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "KM")) + " " + generalFunc.retrieveLangLBl("", "LBL_KM_DISTANCE_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_AWAY"));
            } else {
                milesTxt.setText(String.format("%.2f", (float) (GeneralFunctions.calculationByLocation(SourceLat, SourceLong, DesLat, DesLong, "KM") * 0.621371)) + " " + generalFunc.retrieveLangLBl("", "LBL_MILE_DISTANCE_TXT") + " " + generalFunc.retrieveLangLBl("", "LBL_AWAY"));

            }

            String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + map.get("driver_id") + "/" + map.get("driver_img");


            new LoadImage.builder(LoadImage.bind(image_url), ((SelectableRoundedImageView) findViewById(R.id.bottomdriverImgView))).setErrorImagePath(R.mipmap.ic_no_pic_user).setPlaceholderImagePath(R.mipmap.ic_no_pic_user).build();


            if (map.get("IS_PROVIDER_ONLINE").equalsIgnoreCase("Yes")) {

                driverbottomStatus.setColorFilter(ContextCompat.getColor(getActContext(), R.color.Green));
            } else {
                driverbottomStatus.setColorFilter(ContextCompat.getColor(getActContext(), R.color.Red));
            }

            btnArea.setOnClickListener(view -> {
                closeBottomView();
                loadAvailCabs.closeDialog();

                if (SERVICE_PROVIDER_FLOW.equalsIgnoreCase("Provider")) {

                    HashMap<String, String> mapData = loadAvailCabs.getMarkerDetails(marker);
                    SelectedDriverId = mapData.get("driver_id");
                    Bundle bn = new Bundle();
                    bn.putString("iDriverId", SelectedDriverId);
                    bn.putString("name", mapData.get("Name") + " " + mapData.get("LastName"));
                    bn.putString("fname", mapData.get("Name"));
                    bn.putString("vProviderLatitude", mapData.get("Latitude"));
                    bn.putString("vProviderLongitude", mapData.get("Longitude"));
                    bn.putString("serviceName", getIntent().getStringExtra("SelectvVehicleType"));
                    bn.putString("parentId", getIntent().getStringExtra("parentId"));
                    bn.putString("SelectedVehicleTypeId", getIntent().getStringExtra("SelectedVehicleTypeId"));
                    bn.putString("latitude", pickup_latitude);
                    bn.putString("longitude", pickup_longitude);
                    bn.putString("address", pickup_address);
                    bn.putString("average_rating", mapData.get("average_rating"));
                    bn.putString("driver_img", mapData.get("driver_img"));
                    bn.putString("IS_PROVIDER_ONLINE", mapData.get("IS_PROVIDER_ONLINE"));
                    bn.putString("LBL_FEATURED_TXT", generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
                    bn.putBoolean("isVideoConsultEnable", isVideoConsultEnable);

                } else {
                    if (loadAvailCabs.getMarkerDetails(marker).size() > 0) {
                        loadAvailCabs.selectProviderId = "";
                    }
                    if (loadAvailCabs.getMarkerDetails(marker).isEmpty()) {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_PROVIDER_NOT_AVAILABLE"));
                    } else {
                        loadAvailCabs.loadDriverDetails(loadAvailCabs.getMarkerDetails(marker));
                    }
                }

            });

            //ll_bottom.setOnClickListener(view -> closeBottomView());
        }
    }


    public void OpenCardPaymentAct(boolean fromcabselection) {
        iswallet = true;
        Bundle bn = new Bundle();
        // bn.putString("UserProfileJson", userProfileJson);
        bn.putBoolean("fromcabselection", fromcabselection);
        new ActUtils(getActContext()).startActForResult(CardPaymentActivity.class, bn, Utils.CARD_PAYMENT_REQ_CODE);
    }

    public boolean isPickUpLocationCorrect() {
        String pickUpLocAdd = mainHeaderFrag != null ? (mainHeaderFrag.getPickUpAddress().equals(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainHeaderFrag.getPickUpAddress()) : "";

        if (isUfx) {
            return true;
        }

        if (!pickUpLocAdd.equals("")) {
            return true;
        }
        return false;
    }

    public void continuePickUpProcess() {
        String pickUpLocAdd = mainHeaderFrag != null ? (mainHeaderFrag.getPickUpAddress().equals(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainHeaderFrag.getPickUpAddress()) : "";

        if (!pickUpLocAdd.equals("")) {
//            if (isUfx) {
//                checkSurgePrice("", null);
//            } else if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase("UberX")) {
//                checkSurgePrice("", null);
//            } else {
            // setCabReqType(Utils.CabReqType_Now);
            checkSurgePrice("", null);
//            }
        } else {
            if (isUfx) {
                checkSurgePrice("", null);
            } else if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase("UberX")) {
                checkSurgePrice("", null);
            }
        }
    }

    public String getCurrentCabGeneralType() {

        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX) || app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
            if (!RideDeliveryType.equals("")) {
                if (isUfx) {
                    return Utils.CabGeneralType_UberX;
                }


                if (isDeliver(RideDeliveryType)) {
                    return "Deliver";
                } else {
                    return RideDeliveryType;
                }

            } else {
                if (isUfx) {
                    return Utils.CabGeneralType_UberX;
                }

                return Utils.CabGeneralType_Ride;
            }
        }


        if (cabSelectionFrag != null) {
            return cabSelectionFrag.getCurrentCabGeneralType();
        } else if (!eTripType.trim().equals("")) {
            return eTripType;
        }

        if (isUfx) {
            return Utils.CabGeneralType_UberX;
        }
        return app_type;
    }

    String selectedTime = "";

    public void chooseDateTime() {

        if (!isPickUpLocationCorrect()) {
            return;
        }

        BottomScheduleDialog bottomScheduleDialog = new BottomScheduleDialog(getActContext(), this);
        bottomScheduleDialog.showPreferenceDialog(generalFunc.retrieveLangLBl("", "LBL_SCHEDULE_BOOKING_TXT"), generalFunc.retrieveLangLBl("", "LBL_SET"), generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), "", false, Calendar.getInstance());
    }

    public void setCabTypeList(ArrayList<HashMap<String, String>> cabTypeList) {
        this.cabTypeList = cabTypeList;
    }

    public void changeCabType(String selectedCabTypeId) {
        this.selectedCabTypeId = selectedCabTypeId;
        if (loadAvailCabs != null) {
            loadAvailCabs.setCabTypeId(this.selectedCabTypeId);
            loadAvailCabs.setPickUpLocation(pickUpLocation);
            loadAvailCabs.changeCabs();
        }
    }

    public String getSelectedCabTypeId() {

        return this.selectedCabTypeId;

    }

    public boolean isFixFare = false;

    public void checkSurgePrice(final String selectedTime, final Intent data) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("type", "checkSurgePrice");
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("UserType", Utils.userType);
        parameters.put("SelectedCarTypeID", "" + getSelectedCabTypeId());

        parameters.put("ePaymentBy", ePaymentBy);


        if (isMultiStopOverEnabled() && stopOverPointsList != null && stopOverPointsList.size() > 2) {
            JSONArray jaStore = new JSONArray();
            for (int j = 0; j < stopOverPointsList.size(); j++) {
                Stop_Over_Points_Data data1 = stopOverPointsList.get(j);
                if (data1.isDestination()) {
                    try {
                        JSONObject stopOverPointsObj = new JSONObject();
                        stopOverPointsObj.put("tDAddress", "" + data1.getDestAddress());
                        stopOverPointsObj.put("tDestLatitude", "" + data1.getDestLat());
                        stopOverPointsObj.put("tDestLongitude", "" + data1.getDestLong());
                        jaStore.put(stopOverPointsObj);
                    } catch (Exception e) {
                        Logger.e("Exception", "::" + e.getMessage());
                    }
                }
            }
            parameters.put("stopoverpoint_arr", jaStore.toString());
        }

        if (isPoolCabTypeIdSelected) {
            parameters.put("ePool", "Yes");
        } else {
            parameters.put("ePool", "No");
        }

        if (cabSelectionFrag != null && !cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
            parameters.put("iRentalPackageId", cabSelectionFrag.iRentalPackageId);
        }
        if (!selectedTime.trim().equals("")) {
            parameters.put("SelectedTime", selectedTime);
        }

        if (getPickUpLocation() != null) {
            parameters.put("PickUpLatitude", "" + getPickUpLocation().getLatitude());
            parameters.put("PickUpLongitude", "" + getPickUpLocation().getLongitude());
        }

        if (getDestLocLatitude() != null && !getDestLocLatitude().equalsIgnoreCase("")) {
            parameters.put("DestLatitude", "" + getDestLocLatitude());
            parameters.put("DestLongitude", "" + getDestLocLongitude());
        }

        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                generalFunc.sendHeartBeat();

                String fOutStandingAmount = generalFunc.getJsonValue("fOutStandingAmount", responseString);

                if (GeneralFunctions.parseDoubleValue(0.0, fOutStandingAmount) > 0) {
                    if (data != null && data.hasExtra("isMulti")) // Skip this OutStanding handling for Multi Delivery
                    {
                        continueSurgeChargeExecution(responseString, data);
                    } else if (cabSelectionFrag != null) {

                        cabSelectionFrag.outstandingDialog(responseString, data);
                    } else {
                        continueSurgeChargeExecution(responseString, data);
                    }
                } else {
                    continueSurgeChargeExecution(responseString, data);
                }

            } else {
                generalFunc.showError();
            }
        });

    }

    public void continueSurgeChargeExecution(String responseString, final Intent data) {
        boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

        if (isDataAvail) {


            if (data != null && data.hasExtra("isufxpayment")) {
                isUfxRequest(data);
            } else if (!selectedTime.trim().equals("")) {

                if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || isUfx) {
                    ridelaterView.setVisibility(View.GONE);
                    uberXDriverListArea.setVisibility(View.GONE);
                    pickUpLocClicked();
                } else {

                    if (generalFunc.getJsonValue("eFlatTrip", responseString).equalsIgnoreCase("Yes")) {
                        isFixFare = true;
                        openFixChargeDialog(responseString, false, data);
                    } else {
                        handleRequest(data);
                    }

                }
            } else {
                if (generalFunc.getJsonValue("eFlatTrip", responseString).equalsIgnoreCase("Yes")) {
                    isFixFare = true;
                    openFixChargeDialog(responseString, false, data);
                } else {
                    if (!isUfx) {
                        handleRequest(data);
                    }
                }

                if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || isUfx) {
                    ridelaterView.setVisibility(View.GONE);
                    uberXDriverListArea.setVisibility(View.GONE);
                    pickUpLocClicked();
                }
            }

        } else {

            if (data != null && data.hasExtra("isufxpayment")) {
//                openSurgeConfirmDialog(responseString, selectedTime, data); // enable this condition to enable Surge for Uberx
                isUfxRequest(data);
            } else if (app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX) || isUfx) {
                ridelaterView.setVisibility(View.GONE);
                uberXDriverListArea.setVisibility(View.GONE);
                pickUpLocClicked();
            }

            if (!isUfx) {
                if (generalFunc.getJsonValue("eFlatTrip", responseString).equalsIgnoreCase("Yes")) {
                    isFixFare = true;
                    openFixChargeDialog(responseString, true, data);

                } else {
                    openSurgeConfirmDialog(responseString, selectedTime, data);
                }
            }
        }
    }

    private void isUfxRequest(Intent data) {
        if (bookingtype.equals(Utils.CabReqType_Now)) {
            requestPickUp();
        } else {
            setRideSchedule();
            bookRide();
        }
    }


    private void handleRequest(Intent data) {


        String driverIds = getAvailableDriverIds();

        JSONObject cabRequestedJson = new JSONObject();
        try {
            cabRequestedJson.put("Message", "CabRequested");
            cabRequestedJson.put("sourceLatitude", "" + getPickUpLocation().getLatitude());
            cabRequestedJson.put("sourceLongitude", "" + getPickUpLocation().getLongitude());
            cabRequestedJson.put("PassengerId", generalFunc.getMemberId());
            cabRequestedJson.put("PName", generalFunc.getJsonValueStr("vName", obj_userProfile) + " " + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
            cabRequestedJson.put("PPicName", generalFunc.getJsonValueStr("vImgName", obj_userProfile));
            cabRequestedJson.put("PFId", generalFunc.getJsonValueStr("vFbId", obj_userProfile));
            cabRequestedJson.put("PRating", generalFunc.getJsonValueStr("vAvgRating", obj_userProfile));
            cabRequestedJson.put("PPhone", generalFunc.getJsonValueStr("vPhone", obj_userProfile));
            cabRequestedJson.put("PPhoneC", generalFunc.getJsonValueStr("vPhoneCode", obj_userProfile));
            cabRequestedJson.put("REQUEST_TYPE", getCurrentCabGeneralType());
            cabRequestedJson.put("eFly", eFly ? "Yes" : "No");

            cabRequestedJson.put("selectedCatType", vUberXCategoryName);
            if (getDestinationStatus()) {
                cabRequestedJson.put("destLatitude", "" + getDestLocLatitude());
                cabRequestedJson.put("destLongitude", "" + getDestLocLongitude());
            } else {
                cabRequestedJson.put("destLatitude", "");
                cabRequestedJson.put("destLongitude", "");
            }


            getTollcostValue(driverIds, cabRequestedJson.toString(), data);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Logger.e("Exception", "::" + e.getMessage());
        }


    }

    public void openFixChargeDialog(String responseString, boolean isSurCharge, Intent data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
        builder.setView(dialogView);

        MTextView payableAmountTxt;
        MTextView payableTxt;

        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.retrieveLangLBl("", "LBL_FIX_FARE_HEADER")));


        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
        if (!generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString).equalsIgnoreCase("")) {
            payableAmountTxt.setVisibility(View.VISIBLE);
            payableTxt.setVisibility(View.GONE);

            if (isSurCharge) {

                payableAmount = generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString) + " " + "(" + generalFunc.retrieveLangLBl("", "LBL_AT_TXT") + " " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("SurgePrice", responseString)) + ")";
                ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(payableAmount));
            } else {
                payableAmount = generalFunc.getJsonValue("fFlatTripPricewithsymbol", responseString);
                ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(payableAmount));
            }
        } else {
            payableAmountTxt.setVisibility(View.GONE);
            payableTxt.setVisibility(View.VISIBLE);
        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_TXT"));
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(view -> {
            alertDialog_surgeConfirm.dismiss();
            handleRequest(data);
        });

        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> {
            isFixFare = false;
            alertDialog_surgeConfirm.dismiss();
            closeRequestDialog(false);
        });


        alertDialog_surgeConfirm = builder.create();
        alertDialog_surgeConfirm.setCancelable(false);
        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
        }
        alertDialog_surgeConfirm.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        alertDialog_surgeConfirm.show();
    }

    public void openSurgeConfirmDialog(String responseString, final String selectedTime, Intent data) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.surge_confirm_design, null);
        builder.setView(dialogView);

        MTextView payableAmountTxt;
        MTextView payableTxt;
        MTextView payableAmountValue;

        ((MTextView) dialogView.findViewById(R.id.headerMsgTxt)).setText(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
        ((MTextView) dialogView.findViewById(R.id.surgePriceTxt)).setText(generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("SurgePrice", responseString)));

        ((MTextView) dialogView.findViewById(R.id.tryLaterTxt)).setText(generalFunc.retrieveLangLBl("", "LBL_TRY_LATER"));

        payableTxt = (MTextView) dialogView.findViewById(R.id.payableTxt);
        payableAmountTxt = (MTextView) dialogView.findViewById(R.id.payableAmountTxt);
        payableAmountValue = (MTextView) dialogView.findViewById(R.id.payableAmountValue);
        payableTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PAYABLE_AMOUNT"));


        if (cabSelectionFrag != null && cabTypeList != null && cabTypeList.size() > 0 && cabTypeList.get(cabSelectionFrag.selpos).get("total_fare") != null && !cabTypeList.get(cabSelectionFrag.selpos).get("total_fare").equals("") && !cabTypeList.get(cabSelectionFrag.selpos).get("eRental").equals("Yes")) {

            payableAmountTxt.setVisibility(View.VISIBLE);
            payableAmountValue.setVisibility(View.VISIBLE);
            payableTxt.setVisibility(View.GONE);
            if (isMultiDelivery() && data != null) {
                payableAmount = generalFunc.convertNumberWithRTL(data.getStringExtra("totalFare"));

            } else {
                payableAmount = generalFunc.convertNumberWithRTL(cabTypeList.get(cabSelectionFrag.selpos).get("total_fare"));

            }

            // payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": " + payableAmount);
            if (isPoolCabTypeIdSelected) {

                payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": ");
                payableAmountValue.setText(Utils.getText(cabSelectionFrag.poolFareTxt));
            } else {
                payableAmountTxt.setText(generalFunc.retrieveLangLBl("Approx payable amount", "LBL_APPROX_PAY_AMOUNT") + ": ");
                payableAmountValue.setText(payableAmount);
            }
            if (cabRquestType.equalsIgnoreCase(Utils.CabReqType_Later)) {
                payableAmountTxt.setVisibility(View.GONE);
            }
        } else {
            payableAmountTxt.setVisibility(View.GONE);
            payableTxt.setVisibility(View.VISIBLE);

        }

        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_ACCEPT_SURGE"));
        btn_type2.setId(Utils.generateViewId());

        btn_type2.setOnClickListener(view -> {

            alertDialog_surgeConfirm.dismiss();

            if (data != null && data.hasExtra("isufxpayment")) {
                isUfxRequest(data);
            } else {
                handleRequest(data);
            }
        });

        (dialogView.findViewById(R.id.tryLaterTxt)).setOnClickListener(view -> {
            alertDialog_surgeConfirm.dismiss();
            closeRequestDialog(false);
            cabSelectionFrag.ride_now_btn.setClickable(true);
            isdelivernow = false;
            isdeliverlater = false;

        });


        alertDialog_surgeConfirm = builder.create();
        alertDialog_surgeConfirm.setCancelable(false);
        alertDialog_surgeConfirm.setCanceledOnTouchOutside(false);
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(alertDialog_surgeConfirm);
        }

        alertDialog_surgeConfirm.show();
    }

    public void pickUpLocClicked() {

        configureDeliveryView(true);
        redirectToMapOrList(Utils.Cab_UberX_Type_List, false);

        Bundle bundle = new Bundle();
        bundle.putString("latitude", pickup_latitude);
        bundle.putString("longitude", pickup_longitude);
        bundle.putString("address", pickup_address);
        bundle.putString("SelectvVehicleType", getIntent().getStringExtra("SelectvVehicleType"));

        bundle.putString("type", bookingtype);
        bundle.putString("Quantity", getIntent().getStringExtra("Quantity"));

        bundle.putString("Pname", selectedprovidername);
        if (sdate.equals("")) {
            sdate = getIntent().getStringExtra("Sdate");

        }
        if (Stime.equals("")) {
            Stime = getIntent().getStringExtra("Stime");

        }
        bundle.putString("Sdate", sdate);
        bundle.putString("Stime", Stime);

        if (UfxAmount.equals("")) {
            bundle.putString("SelectvVehiclePrice", getIntent().getStringExtra("SelectvVehiclePrice"));
            bundle.putString("Quantityprice", getIntent().getStringExtra("Quantityprice"));
        } else {

            bundle.putString("SelectvVehiclePrice", UfxAmount + "");


            if (!getIntent().getStringExtra("Quantity").equals("0")) {
                UfxAmount = UfxAmount.replace(vCurrencySymbol, "");
                int qty = GeneralFunctions.parseIntegerValue(0, getIntent().getStringExtra("Quantity"));
                float amount = GeneralFunctions.parseFloatValue(0, UfxAmount);
                bundle.putString("Quantityprice", vCurrencySymbol + (qty * amount) + "");
            } else {
                bundle.putString("Quantityprice", UfxAmount + "");
            }


            UfxAmount = "";
        }

        bundle.putString("ACCEPT_CASH_TRIPS", ACCEPT_CASH_TRIPS);
        //   new ActUtils(getActContext()).startActForResult(BookingSummaryActivity.class, bundle, Utils.UFX_REQUEST_CODE);
    }

    public void setDefaultView() {

        try {
            super.onPostResume();
        } catch (Exception ignored) {

        }


        try {


            cabRquestType = Utils.CabReqType_Now;


            if (mainHeaderFrag != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.headerContainer, mainHeaderFrag).commit();
            }


            if (!app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                if (mainHeaderFrag != null) {
                    mainHeaderFrag.releaseAddressFinder();
                }

            } else if (app_type.equalsIgnoreCase("UberX")) {
                (findViewById(R.id.dragView)).setVisibility(View.GONE);
                setUserLocImgBtnMargin(5);
            }


            configDestinationMode(false);
            userLocBtnImgView.performClick();
            Utils.runGC();

            if (!app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                configureDeliveryView(false);
            }

            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            try {
                new CreateAnimation(dragView, getActContext(), R.anim.design_bottom_sheet_slide_in, 600, true).startAnimation();
            } catch (Exception ignored) {

            }


            if (loadAvailCabs != null) {
                loadAvailCabs.setTaskKilledValue(false);
                loadAvailCabs.onResumeCalled();
            }
        } catch (Exception ignored) {

        }
    }

    public boolean isVerticalCabscroll = false;

    public void setPanelHeight(int value) {
        Logger.d("setPanelHeight", "::" + value + "::" + isVerticalCabscroll);

        int FragHeight;

        if (cabSelectionFrag != null) {
            if (isVerticalCabscroll) {

                if (cabSelectionFrag.rentalarea.getVisibility() == View.GONE) {
                    FragHeight = getActContext().getResources().getDimensionPixelSize(R.dimen._370sdp);
                } else {
                    FragHeight = getActContext().getResources().getDimensionPixelSize(R.dimen._410sdp);
                }
            } else {

                if (cabSelectionFrag.fragmentHeight != 0) {
                    FragHeight = cabSelectionFrag.fragmentHeight;
                } else {
                    FragHeight = Utils.dipToPixels(getActContext(), value);
                }
            }
        } else if (driverDetailFrag != null) {
            FragHeight = driverDetailFrag.fragmentHeight;
        } else {
            FragHeight = Utils.dipToPixels(getActContext(), value);
        }
        sliding_layout.setPanelHeight(FragHeight);

        if (cabSelectionFrag != null && cabSelectionFrag.poolArea != null && cabSelectionFrag.poolArea.getVisibility() == View.VISIBLE) {
            sliding_layout.setPanelHeight(getActContext().getResources().getDimensionPixelSize(R.dimen._233sdp));
            cabSelectionFrag.managePayView(true);
        }

        if (cabSelectionFrag != null && isVerticalCabscroll && isMultiDelivery()) {
            sliding_layout.setPanelHeight(isVerticalCabscroll ? getActContext().getResources().getDimensionPixelSize(R.dimen._330sdp) : FragHeight);
        }

        if (cabSelectionFrag != null && isVerticalCabscroll && isDeliver(getCurrentCabGeneralType()) && !isMultiDelivery()) {
            sliding_layout.setPanelHeight(isVerticalCabscroll ? getActContext().getResources().getDimensionPixelSize(R.dimen._360sdp) : FragHeight);
        }


        //  sliding_layout.setPanelHeight((cabSelectionFrag != null && cabSelectionFrag.fragmentHeight != 0) ? cabSelectionFrag.fragmentHeight : driverDetailFrag != null ? value : Utils.dipToPixels(getActContext(), value));

        //resize map padding/height according panel height

        resetMapView();


        if (isMultiDelivery() && cabSelectionFrag != null) {
            getMap().setPadding(0, 0, 0, sliding_layout.getPanelHeight() + 5);
            getMap().requestLayout();
        } else {
            getMap().setPadding(0, 0, 0, (sliding_layout.getPanelHeight() - (int) getResources().getDimension(R.dimen._30sdp)));
            getMap().requestLayout();
        }

        if (userLocBtnImgView != null && (cabSelectionFrag != null || driverDetailFrag != null || driverAssignedHeaderFrag != null)) {
            resetUserLocBtnView();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) (userTripBtnImgView).getLayoutParams();
            if (isMultiDelivery()) {
                params.bottomMargin = sliding_layout.getPanelHeight() + Utils.dipToPixels(getActContext(), 20);
            } else {
                if (driverDetailFrag != null) {
                    getMap().setPadding(0, 0, 0, (sliding_layout.getPanelHeight() + (int) getResources().getDimension(R.dimen._25sdp)));
                } else {
                    params.bottomMargin = sliding_layout.getPanelHeight() - (int) getResources().getDimension(R.dimen._200sdp);
                }

            }

            if (generalFunc.isRTLmode()) {
                params.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
                params1.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
            } else {
                params.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 20);
                params1.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 20);
            }

            userLocBtnImgView.requestLayout();
        }
    }


    public void setFlyPanelHeight(int value) {
        sliding_layout.setPanelHeight(staticPanelHeight);
        if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

        if (userLocBtnImgView != null /*&& flyStationSelectionFragment != null*/) {
            resetUserLocBtnView();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) (userTripBtnImgView).getLayoutParams();
            if (isMultiDelivery()) {
                //params.bottomMargin = sliding_layout.getPanelHeight() + Utils.dipToPixels(getActContext(), 5);
                params.bottomMargin = (int) (sliding_layout.getPanelHeight() + getResources().getDimension(R.dimen._5sdp));
            } else {
                if (driverDetailFrag != null) {
//                     userTripBtnImgView.setX(getResources().getDimension(R.dimen._6sdp));
//                     userLocBtnImgView.setX(getResources().getDimension(R.dimen._6sdp));
//                    params.bottomMargin = sliding_layout.getPanelHeight();
//                    params1.bottomMargin = sliding_layout.getPanelHeight();
//                    getMap().setPadding(0, 0, 0, (sliding_layout.getPanelHeight() + (int) getResources().getDimension(R.dimen._25sdp)));
                } else {
                    //params.bottomMargin = sliding_layout.getPanelHeight() - Utils.dipToPixels(getActContext(), 50);
                    params.bottomMargin = (int) (sliding_layout.getPanelHeight() - getResources().getDimension(R.dimen._50sdp));
                }

            }

            if (generalFunc.isRTLmode()) {
                params.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
                params1.leftMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 10);
            } else {
                params.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 20);
                params1.rightMargin = driverDetailFrag != null ? (int) getResources().getDimension(R.dimen._15sdp) : Utils.dipToPixels(getActContext(), 20);
            }

            userLocBtnImgView.requestLayout();
        }
    }


    public void setFlySheetHeight() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        }

        getMap().setPadding(Utils.dipToPixels(getActContext(), 10), Utils.dipToPixels(getActContext(), 25), Utils.dipToPixels(getActContext(), 10), staticFlyPanelHeight + Utils.dipToPixels(getActContext(), 25));

        reSetButton(true);
    }


    public Location getPickUpLocation() {
        return this.pickUpLocation;
    }

    public String getPickUpLocationAddress() {
        return this.pickUpLocationAddress;
    }

    public void notifyCarSearching() {
        setETA("\n" + "--");

        if (getCurrentCabGeneralType().equals(Utils.CabGeneralType_UberX)) {
            ridelaterView.setVisibility(View.GONE);

            if (currentUberXChoiceType.equalsIgnoreCase(Utils.Cab_UberX_Type_List)) {

                (findViewById(R.id.driverListAreaLoader)).setVisibility(View.VISIBLE);
                (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);
                ((MTextView) findViewById(R.id.searchingDriverTxt)).setText(generalFunc.retrieveLangLBl("Searching Provider", "LBL_SEARCH_PROVIDER_WAIT_TXT"));
                uberXNoDriverTxt.setVisibility(View.GONE);

                uberXDriverList.clear();


            }
        }
    }

    public void notifyNoCabs() {

        if (isufxbackview) {
            return;
        }

        setETA("\n" + "--");
        setCurrentLoadedDriverList(new ArrayList<HashMap<String, String>>());

        if (cabSelectionFrag != null) {
            noCabAvail = false;
            changeLable();
        }

        changeLable();

    }


    public void notifyCabsAvailable() {
        if (cabSelectionFrag != null && loadAvailCabs != null && loadAvailCabs.listOfDrivers != null && loadAvailCabs.listOfDrivers.size() > 0) {
            if (cabSelectionFrag.isroutefound) {
                if (loadAvailCabs.isAvailableCab) {
                    if (!timeval.equalsIgnoreCase("\n" + "--")) {
                        noCabAvail = true;
                    }
                }
            }
        }


        if (cabSelectionFrag != null) {
            cabSelectionFrag.setLabels(false);
        }
    }

    public void onMapCameraChanged() {
        if (cabSelectionFrag != null) {

            if (loadAvailCabs != null) {
                loadAvailCabs.filterDrivers(true);
            }

            if (mainHeaderFrag != null) {
                //notifyCarSearching();
//                if(cabSelectionFrag!=null) {
//                    cabSelectionFrag.img_ridelater.setEnabled(false);
//                }


                if (isDestinationMode) {
                    mainHeaderFrag.setDestinationAddress(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT"));
                } else {
                    mainHeaderFrag.setPickUpAddress(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT"));
                }
            }
        }
    }

    public void onAddressFound(String address) {
        try {


            if (cabSelectionFrag != null) {
                notifyCabsAvailable();
                if (cabSelectionFrag.img_ridelater != null) {
                    cabSelectionFrag.img_ridelater.setEnabled(true);
                }
                if (mainHeaderFrag != null) {

                    if (!isHomeClick && !isWorkClick) {
                        if (isDestinationMode) {
                            mainHeaderFrag.setDestinationAddress(address);
                        } else {
                            mainHeaderFrag.setPickUpAddress(address);
                        }
                    }
                }

                // cabSelectionFrag.findRoute("--");
            } else {
                if (isUserLocbtnclik) {
                    isUserLocbtnclik = false;

                    if (mainHeaderFrag != null && eFly) {

                        if (isDestinationMode) {
                            mainHeaderFrag.setDestinationAddress(address);
                        } else {
                            mainHeaderFrag.setPickUpAddress(address);
                        }
                    } else {
                        if (mainHeaderFrag != null) {
                            mainHeaderFrag.setPickUpAddress(address);
                        }
                    }

                }
            }
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }


    }

    public void setDestinationPoint(String destLocLatitude, String destLocLongitude, String destAddress, boolean isDestinationAdded) {
        Logger.d("setDestinationPoint", "::" + destAddress + "::" + isDestinationAdded);

        if (destLocation == null) {
            destLocation = new Location("dest");
        }
        destLocation.setLatitude(GeneralFunctions.parseDoubleValue(0.0, destLocLatitude));
        destLocation.setLongitude(GeneralFunctions.parseDoubleValue(0.0, destLocLongitude));

        this.isDestinationAdded = isDestinationAdded;
        this.destLocLatitude = destLocLatitude;
        this.destLocLongitude = destLocLongitude;
        this.destAddress = destAddress;

        if (isMultiStopOverEnabled()) {
            handleMultiStopOverData();
        }

    }

    private void handleMultiStopOverData() {
        // reSet or add addresses
        if (mainHeaderFrag != null && stopOverPointsList.size() < 3 && isDestinationAdded) {
            mainHeaderFrag.addOrResetStopOverPoints(destLocation.getLatitude(), destLocation.getLongitude(), destAddress, false);
        }

        // Manage pool & rental for multi stop Over
        if (cabSelectionFrag != null) {
            // if MultiStop Over Added & pool selected then restrict from do pool trip
            if (stopOverPointsList.size() > 2 && isPoolCabTypeIdSelected && cabSelectionFrag.cabTypeList.size() > 0) {
                cabSelectionFrag.onItemClick(0);
            }

            cabSelectionFrag.manageRentalArea();
        }
    }

    public boolean getDestinationStatus() {
        return this.isDestinationAdded;
    }

    public String getDestLocLatitude() {
        return this.destLocLatitude;
    }

    public String getDestLocLongitude() {
        return this.destLocLongitude;
    }

    public String getDestAddress() {
        return this.destAddress;
    }

    public void setCashSelection(boolean isCashSelected) {
        if (loadAvailCabs != null) {
            loadAvailCabs.changeCabs();
        }
    }

    public String getCabReqType() {
        return this.cabRquestType;
    }

    public void setCabReqType(String cabRquestType) {
        this.cabRquestType = cabRquestType;
    }

    public Bundle getFareEstimateBundle() {
        Bundle bn = new Bundle();
        bn.putString("PickUpLatitude", "" + getPickUpLocation().getLatitude());
        bn.putString("PickUpLongitude", "" + getPickUpLocation().getLongitude());
        bn.putString("isDestinationAdded", "" + getDestinationStatus());
        bn.putString("DestLocLatitude", "" + getDestLocLatitude());
        bn.putString("DestLocLongitude", "" + getDestLocLongitude());
        bn.putString("DestLocAddress", "" + getDestAddress());
        bn.putString("SelectedCarId", "" + getSelectedCabTypeId());
        bn.putString("SelectedCabType", "" + generalFunc.getSelectedCarTypeData(getSelectedCabTypeId(), cabTypesArrList, "vVehicleType"));
//        bn.putString("UserProfileJson", "" + userProfileJson);

        return bn;
    }

    public void continueDeliveryProcess() {
        String pickUpLocAdd = mainHeaderFrag != null ? (mainHeaderFrag.getPickUpAddress().equals(generalFunc.retrieveLangLBl("", "LBL_SELECTING_LOCATION_TXT")) ? "" : mainHeaderFrag.getPickUpAddress()) : "";

        if (!pickUpLocAdd.equals("")) {

            if (isDeliver(getCurrentCabGeneralType())) {
                setDeliverySchedule();
            } else {
                checkSurgePrice("", null);
            }
        }
    }

    public void setRideSchedule() {
        isrideschedule = true;


        if (!getDestinationStatus() && generalFunc.retrieveValue(Utils.APP_DESTINATION_MODE).equalsIgnoreCase(Utils.STRICT_DESTINATION)) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_ADD_DEST_MSG_BOOK_RIDE"));
        }
        if (isDeliver(getCurrentCabGeneralType())) {
            setDeliverySchedule();

        } else {

            if (!cabSelectionFrag.handleRnetalView(Utils.convertDateToFormat(Utils.getDetailDateFormat(getActContext()), scheduledate))) {
                checkSurgePrice(selectedTime, deliveryData);
            }
        }
    }

    public void setDeliverySchedule() {


        boolean skipDestCheck = false;
        if (isMultiDelivery()) {
            skipDestCheck = true;
        }
        if (!skipDestCheck && !getDestinationStatus()) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please add your destination location " + "to deliver your package.", "LBL_ADD_DEST_MSG_DELIVER_ITEM"));
        } else {

            if (skipDestCheck) {
                if (getSelectedCabTypeId().equals("-1") || TextUtils.isEmpty(getSelectedCabTypeId())) {
                    generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_NO_CAR_AVAIL_TXT"));
                    return;
                }

                Bundle bn = new Bundle();

                HashMap<String, String> map = new HashMap<>();
                map.put("isDeliverNow", "" + getCabReqType().equals(Utils.CabReqType_Now));
                map.put("vVehicleType", generalFunc.getSelectedCarTypeData(selectedCabTypeId, cabTypesArrList, "vVehicleType"));
                map.put("SelectedCar", "" + getSelectedCabTypeId());

                map.put("pickUpLocLattitude", "" + pickUpLocation.getLatitude());
                map.put("pickUpLocLongitude", "" + pickUpLocation.getLongitude());
                map.put("pickUpLocAddress", "" + pickUpLocationAddress);
                /*Single Delivery UI as Multi Delivery - SdUiAsMd*/
                map.put("maxDestination", "" + (getIntent().hasExtra("maxDestination") ? getIntent().getStringExtra("maxDestination") : ""));
                bn.putSerializable("selectedData", map);
                bn.putBoolean("fromMulti", getIntent().getBooleanExtra("fromMulti", true));


            }

        }
    }

    public void bookRide() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", "ScheduleARide");


        parameters.put("ePaymentBy", ePaymentBy);
        parameters.put("eWalletIgnore", eWalletIgnore);


        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && generalFunc.retrieveValue(Utils.BOOK_FOR_ELSE_ENABLE_KEY).equalsIgnoreCase("yes")) {

            if (generalFunc.containsKey(Utils.BFSE_SELECTED_CONTACT_KEY) && Utils.checkText(generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY))) {
                Gson gson = new Gson();
                String data1 = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
                ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                }.getType());


                if (contactdetails != null && Utils.checkText(contactdetails.name) && !contactdetails.name.equalsIgnoreCase("ME")) {
                    if (Utils.checkText(contactdetails.mobileNumber)) {
                        parameters.put("eBookSomeElseNumber", contactdetails.mobileNumber);
                        parameters.put("eBookForSomeOneElse", "Yes");
                    }
                    if (Utils.checkText(contactdetails.name)) {
                        parameters.put("eBookSomeElseName", contactdetails.name);
                    }
                }
            }

        }

        if (!selectReasonId.equalsIgnoreCase("")) {
            parameters.put("iTripReasonId", selectReasonId);
        }
        if (!vReasonTitle.equalsIgnoreCase("")) {
            parameters.put("vReasonTitle", vReasonTitle);
        }


        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                parameters.put("vDistance", cabSelectionFrag.distance);
                parameters.put("vDuration", cabSelectionFrag.time);
            }
        }

        if (mainHeaderFrag != null) {
            if (!app_type.equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                if (isUfx) {
                    parameters.put("pickUpLocAdd", pickUpLocationAddress);
                } else {
                    parameters.put("pickUpLocAdd", mainHeaderFrag != null ? mainHeaderFrag.getPickUpAddress() : "");
                }

            } else {
                parameters.put("pickUpLocAdd", pickUpLocationAddress);
            }
        }

        if (cabSelectionFrag != null && !cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
            parameters.put("iRentalPackageId", cabSelectionFrag.iRentalPackageId);

        }
        parameters.put("iUserId", generalFunc.getMemberId());
        if (isUfx) {
            parameters.put("pickUpLatitude", pickup_latitude);
            parameters.put("pickUpLongitude", pickup_longitude);
        } else {
            parameters.put("pickUpLatitude", "" + getPickUpLocation().getLatitude());
            parameters.put("pickUpLongitude", "" + getPickUpLocation().getLongitude());
            if (eFly) parameters.put("iFromStationId", iFromStationId);

        }
        parameters.put("destLocAdd", getDestAddress());
        if (eFly) {
            parameters.put("iToStationId", iToStationId);
        }

        parameters.put("destLatitude", getDestLocLatitude());
        parameters.put("destLongitude", getDestLocLongitude());
        parameters.put("iCabBookingId", iCabBookingId);
        parameters.put("scheduleDate", selectedDateTime);
        parameters.put("iVehicleTypeId", getSelectedCabTypeId());
        parameters.put("SelectedDriverId", SelectedDriverId);
        // parameters.put("TimeZone", selectedDateTimeZone);


        parameters.put("ePayWallet", "No");

//        if (!isCashSelected && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
//
//            parameters.put("ePayWallet", "Yes");
//        }


        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                parameters.put("vDistance", cabSelectionFrag.distance);
                parameters.put("vDuration", cabSelectionFrag.time);
            }
        }
        // parameters.put("PickUpAddGeoCodeResult", tempPickupGeoCode);
        // parameters.put("DestAddGeoCodeResult", tempDestGeoCode);


        parameters.put("HandicapPrefEnabled", ishandicap ? "Yes" : "No");
        parameters.put("PreferFemaleDriverEnable", isfemale ? "Yes" : "No");
        parameters.put("ChildPrefEnabled", isChildSeat ? "Yes" : "No");
        parameters.put("WheelChairPrefEnabled", isWheelChair ? "Yes" : "No");
        parameters.put("vTollPriceCurrencyCode", tollcurrancy);
        String tollskiptxt = "";

        if (istollIgnore) {
            tollamount = 0;
            tollskiptxt = "Yes";

        } else {
            tollskiptxt = "No";
        }
        parameters.put("fTollPrice", tollamount + "");
        parameters.put("eTollSkipped", tollskiptxt);


        parameters.put("eType", getCurrentCabGeneralType());

        if (cabSelectionFrag != null) {
            parameters.put("PromoCode", cabSelectionFrag.getAppliedPromoCode());
        }
        if (app_type.equalsIgnoreCase("UberX") || isUfx) {
            parameters.put("PromoCode", appliedPromoCode);
            parameters.put("eType", Utils.CabGeneralType_UberX);
            if (getIntent().getStringExtra("Quantity").equals("0")) {
                parameters.put("Quantity", "1");
            } else {
                parameters.put("Quantity", getIntent().getStringExtra("Quantity"));
            }

            parameters.put("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
            parameters.put("tUserComment", userComment);
            parameters.put("scheduleDate", SelectDate);
        } else {
            parameters.put("scheduleDate", selectedDateTime);
        }

        if (deliveryData != null) {
            if (isMultiDelivery()) {
                String data = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);

                if (deliveryData.hasExtra("isMulti")) {

                    parameters.put("PromoCode", deliveryData.getStringExtra("promocode"));
                    parameters.put("ePaymentBy", deliveryData.getStringExtra("paymentMethod"));


                    parameters.put("eType", Utils.eType_Multi_Delivery);
                }

                if (deliveryData.hasExtra("total_del_dist")) {
                    parameters.put("total_del_dist", "" + deliveryData.getStringExtra("total_del_dist"));
                }

                if (deliveryData.hasExtra("total_del_time")) {
                    parameters.put("total_del_time", "" + deliveryData.getStringExtra("total_del_time"));
                }

                JSONArray array = generalFunc.getJsonArray(data);
                parameters.put("delivery_arr", array);

            }
        }

        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && stopOverPointsList.size() > 2) {
            JSONArray jaStore = new JSONArray();

            for (int j = 0; j < stopOverPointsList.size(); j++) {
                Stop_Over_Points_Data data1 = stopOverPointsList.get(j);
                if (data1.isDestination()) {
                    try {
                        JSONObject stopOverPointsObj = new JSONObject();
                        stopOverPointsObj.put("tDAddress", "" + data1.getDestAddress());
                        stopOverPointsObj.put("tDestLatitude", "" + data1.getDestLat());
                        stopOverPointsObj.put("tDestLongitude", "" + data1.getDestLong());
                        jaStore.put(stopOverPointsObj);
                    } catch (Exception e) {
                        Logger.e("Exception", "::" + e.getMessage());
                    }
                }
            }
            parameters.put("stopoverpoint_arr", jaStore);

        }


        ServerTask exeWebServer = ApiHandler.execute(getActContext(), parameters, true, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {


                if (generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_PHONE_VERIFY") || generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_PHONE_VERIFY") || generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_VERIFY")) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + generalFunc.getJsonValue(Utils.message_str, responseString));
                    //  bn.putString("UserProfileJson", userProfileJson);
                    accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);

                    return;
                }

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);

                if (action.equals("1")) {
                    setDestinationPoint("", "", "", false);
                    setDefaultView();
                    isrideschedule = false;

                    if (isRebooking) {


                        showBookingAlert();
                    } else {

                        reSetFields();
                        showBookingAlert(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), false);
                    }

                } else {


                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);
                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }

                        boolean isCancelShow = generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No");

                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN") : generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), isCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "", button_Id -> {
                            if (button_Id == 1) {

                                new ActUtils(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    bookRide();
                                }
                            }
                        });

                        return;

                    }
                    if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            if (btn_id == 0) {


                            } else if (btn_id == 1) {
                                Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                // finish();

                            }
                        });

                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                        generateAlert.showAlertBox();
                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                }

            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.setCancelAble(false);
    }

    private void reSetFields() {
        iFromStationId = "";
        iToStationId = "";
        generalFunc.resetStoredDetails();
    }


    public void chatMsg() {
        JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

        String vBookingNo = generalFunc.getJsonValueStr("vBookingNo", tripDetailJson);
        String vRideNo = generalFunc.getJsonValueStr("vRideNo", tripDetailJson);

        Bundle bn = new Bundle();
        bn.putString("iTripId", driverDetailFrag.getTripData().get("iTripId"));
        bn.putString("vBookingNo", vBookingNo.trim().equalsIgnoreCase("") ? vRideNo : vBookingNo);

        new ActUtils(getActContext()).startActWithData(ChatActivity.class, bn);
    }


    public void showBookingAlert() {
        reSetFields();
        SuccessDialog.showSuccessDialog(getActContext(), "", generalFunc.retrieveLangLBl("Your selected booking has been updated.", "LBL_BOOKING_UPDATED"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), false, () -> {
            reSetFields();
            Bundle bn = new Bundle();
            bn.putBoolean("isrestart", true);
            new ActUtils(getActContext()).startActWithData(BookingActivity.class, bn);

            finish();
        });

        /*
         generalFunc.resetStoredDetails();

        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());

        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            generalFunc.resetStoredDetails();

            generateAlert.closeAlertBox();
            Bundle bn = new Bundle();
            bn.putBoolean("isrestart", true);
            new ActUtils(getActContext()).startActWithData(BookingActivity.class, bn);

            finish();
        });
        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Your selected booking has been updated.", "LBL_BOOKING_UPDATED"));
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));

        generateAlert.showAlertBox();
        */
    }

    public void showBookingAlert(String message, boolean isongoing) {
        reSetFields();
//        SuccessDialog.showSuccessDialog(getActContext(), generalFunc.retrieveLangLBl("Booking Successful"
//                        , "LBL_BOOKING_ACCEPTED"), message, isongoing ? generalFunc.retrieveLangLBl(""
//                        , "LBL_VIEW_ON_GOING_TRIPS") : generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS")
//                , generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), false, () -> {
//                    reSetFields();
//                    if (isongoing) {
//                        generalFunc.resetStoredDetails();
//                        Bundle bn = new Bundle();
//                        if (driverAssignedHeaderFrag != null) {
//                            bn.putString("isTripRunning", "yes");
//                        }
//                        bn.putBoolean("isRestart", true);
////                new ActUtils(getActContext()).startActForResult(OnGoingTripsActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
//                        new ActUtils(getActContext()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
//                        finishAffinity();
//                    } else {
//                        Bundle bn = new Bundle();
//                        bn.putBoolean("isrestart", true);
//                        String selType = getIntent().getStringExtra("selType");
//                        if (selType != null) {
//                            bn.putString("selType", selType);
//                        }
//                        new ActUtils(getActContext()).startActWithData(BookingActivity.class, bn);
//                        finish();
//                    }
//
//                }, () -> {
//                    reSetFields();
//                    Bundle bn = new Bundle();
//                    if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equals(Utils.CabGeneralType_UberX)) {
//                        new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
//                    } else {
//                        if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
//                            new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
//                        } else if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase("Delivery")) {
//                            if (generalFunc.getJsonValueStr("ENABLE_RIDE_DELIVERY_NEW_FLOW", obj_userProfile).equals("Yes")) {
//                                if (generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")) {
//                                    new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
//                                } else {
//                                    new ActUtils(getActContext()).startActWithData(RideDeliveryActivity.class, bn);
//                                }
//                            } else {
//                                bn.putString("iVehicleCategoryId", generalFunc.getJsonValueStr("DELIVERY_CATEGORY_ID", obj_userProfile));
//                                bn.putString("vCategory", generalFunc.getJsonValueStr("DELIVERY_CATEGORY_NAME", obj_userProfile));
//                                new ActUtils(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
//                            }
//
//                        } else if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
//                            if (generalFunc.getJsonValueStr("ENABLE_RIDE_DELIVERY_NEW_FLOW", obj_userProfile).equals("Yes")) {
//                                if (generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")) {
//                                    new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
//                                } else {
//                                    new ActUtils(getActContext()).startActWithData(RideDeliveryActivity.class, bn);
//                                }
//                            } else {
//                                bn.putString("iVehicleCategoryId", generalFunc.getJsonValueStr("DELIVERY_CATEGORY_ID", obj_userProfile));
//                                bn.putString("vCategory", generalFunc.getJsonValueStr("DELIVERY_CATEGORY_NAME", obj_userProfile));
//                                new ActUtils(getActContext()).startActWithData(CommonDeliveryTypeSelectionActivity.class, bn);
//                            }
//                        } else {
//                            new ActUtils(getActContext()).startActWithData(MainActivity.class, bn);
//                        }
//                    }
//                    finishAffinity();
//                });

        CustomDialog customDialog = new CustomDialog(this, generalFunc);
        customDialog.setDetails(generalFunc.retrieveLangLBl("Booking Successful", "LBL_BOOKING_ACCEPTED"), message, isongoing ? generalFunc.retrieveLangLBl("", "LBL_VIEW_ON_GOING_TRIPS") : generalFunc.retrieveLangLBl("Done", "LBL_VIEW_BOOKINGS"), generalFunc.retrieveLangLBl("Cancel", "LBL_CANCEL_TXT"), false, R.drawable.ic_correct_2, false, 1, true);
        customDialog.setRoundedViewBackgroundColor(R.color.appThemeColor_1);
        customDialog.setRoundedViewBorderColor(R.color.white);
        customDialog.setImgStrokWidth(15);
        customDialog.setBtnRadius(10);
        customDialog.setIconTintColor(R.color.white);
        customDialog.setPositiveBtnBackColor(R.color.appThemeColor_2);
        customDialog.setPositiveBtnTextColor(R.color.white);
        customDialog.createDialog();
        customDialog.setNegativeButtonClick(() -> {
            reSetFields();
            Bundle bn = new Bundle();
            if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equals(Utils.CabGeneralType_UberX)) {
                new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
            } else {
                if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                    new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                } else if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase("Delivery")) {
                    if (generalFunc.getJsonValueStr("ENABLE_RIDE_DELIVERY_NEW_FLOW", obj_userProfile).equals("Yes")) {
                        if (generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")) {
                            new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                        } else {
                            new ActUtils(getActContext()).startActWithData(RideDeliveryActivity.class, bn);
                        }

                    }

                } else if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                    if (generalFunc.getJsonValueStr("ENABLE_RIDE_DELIVERY_NEW_FLOW", obj_userProfile).equals("Yes")) {
                        if (generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")) {
                            new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                        } else {
                            new ActUtils(getActContext()).startActWithData(RideDeliveryActivity.class, bn);
                        }

                    }
                } else {
                    if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                        new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                    } else {
                        new ActUtils(getActContext()).startActWithData(MainActivity.class, bn);
                    }
                    //new ActUtils(getActContext()).startActWithData(MainActivity.class, bn);
                }
            }
            finishAffinity();
        });
        customDialog.setPositiveButtonClick(() -> {
            reSetFields();
            if (isongoing) {
                generalFunc.resetStoredDetails();
                Bundle bn = new Bundle();
                if (driverAssignedHeaderFrag != null) {
                    bn.putString("isTripRunning", "yes");
                }
                bn.putBoolean("isRestart", true);
//                new ActUtils(getActContext()).startActForResult(OnGoingTripsActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                new ActUtils(getActContext()).startActForResult(BookingActivity.class, bn, Utils.ASSIGN_DRIVER_CODE);
                finishAffinity();
            } else {
                Bundle bn = new Bundle();
                bn.putBoolean("isrestart", true);
                if (selType != null) {
                    bn.putString("selType", selType);
                }
                new ActUtils(getActContext()).startActWithData(BookingActivity.class, bn);
                finish();
            }
        });
        customDialog.show();
    }

    public void scheduleDelivery(Intent data) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("type", "ScheduleARide");
        parameters.put("iUserId", generalFunc.getMemberId());
        parameters.put("eWalletIgnore", eWalletIgnore);
        parameters.put("pickUpLocAdd", mainHeaderFrag != null ? mainHeaderFrag.getPickUpAddress() : "");
        if (eFly) {
            parameters.put("iFromStationId", iFromStationId);
            parameters.put("iToStationId", iToStationId);
        }
        parameters.put("pickUpLatitude", "" + getPickUpLocation().getLatitude());
        parameters.put("pickUpLongitude", "" + getPickUpLocation().getLongitude());
        parameters.put("destLocAdd", getDestAddress());
        parameters.put("destLatitude", getDestLocLatitude());
        parameters.put("destLongitude", getDestLocLongitude());
        parameters.put("scheduleDate", selectedDateTime);
        parameters.put("iVehicleTypeId", getSelectedCabTypeId());
        //  parameters.put("TimeZone", selectedDateTimeZone);

        parameters.put("eType", "Deliver");


        parameters.put("ePaymentBy", ePaymentBy);
        if (!selectReasonId.equalsIgnoreCase("")) {
            parameters.put("iTripReasonId", selectReasonId);
        }
        if (!vReasonTitle.equalsIgnoreCase("")) {
            parameters.put("vReasonTitle", vReasonTitle);
        }

        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                parameters.put("vDistance", cabSelectionFrag.distance);
                parameters.put("vDuration", cabSelectionFrag.time);
            }
        }

        if (data != null && data.hasExtra("isMulti")) {
            String deliveryData = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);

            if (data.getBooleanExtra("isMulti", true)) {

                parameters.put("PromoCode", data.getStringExtra("promocode"));
                parameters.put("ePaymentBy", data.getStringExtra("paymentMethod"));
                parameters.put("eType", Utils.eType_Multi_Delivery);
            }

            if (data.hasExtra("total_del_dist")) {
                parameters.put("total_del_dist", "" + data.getStringExtra("total_del_dist"));
            }

            if (data.hasExtra("total_del_time")) {
                parameters.put("total_del_time", "" + data.getStringExtra("total_del_time"));
            }

            JSONArray array = generalFunc.getJsonArray(deliveryData);
            parameters.put("delivery_arr", array);
        } else {
            String data1 = generalFunc.retrieveValue(Utils.DELIVERY_DETAILS_KEY);
            JSONArray deliveriesArr = generalFunc.getJsonArray("deliveries", data1);
            if (deliveriesArr != null) {
                for (int j = 0; j < deliveriesArr.length(); j++) {
                    if (data != null) {
                        JSONObject ja = generalFunc.getJsonObject(deliveriesArr, j);
                        parameters.put("iPackageTypeId", generalFunc.getJsonValue(PACKAGE_TYPE_ID_KEY, ja.toString()));

                    }
                }
            }
        }


        String tollskiptxt = "";

        if (istollIgnore) {
            tollskiptxt = "Yes";
            tollamount = 0;
        } else {
            tollskiptxt = "No";
        }
        parameters.put("fTollPrice", tollamount + "");
        parameters.put("vTollPriceCurrencyCode", tollcurrancy);
        parameters.put("eTollSkipped", tollskiptxt);

        ServerTask exeWebServer = ApiHandler.execute(getActContext(), parameters, true, generalFunc, responseString -> {

            if (responseString != null && !responseString.equals("")) {

                if (generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_PHONE_VERIFY") || generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_PHONE_VERIFY") || generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_VERIFY")) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + generalFunc.getJsonValue(Utils.message_str, responseString));
                    accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);
                    return;
                }

                String action = generalFunc.getJsonValue(Utils.action_str, responseString);

                if (action.equals("1")) {

                    generalFunc.removeValue(Utils.DELIVERY_DETAILS_KEY);
                    setDestinationPoint("", "", "", false);
                    setDefaultView();

                    if (isRebooking) {
                        showBookingAlert();
                    } else {
                        reSetFields();

                        showBookingAlert(generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), false);
                    }
                } else {


                    if (generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);

                        String walletMsg = "";
                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }


                        boolean isCancelShow = false;
                        if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                            isCancelShow = true;
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN") : generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), isCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "", button_Id -> {
                            if (button_Id == 1) {
                                new ActUtils(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    scheduleDelivery(data);
                                }
                            }
                        });

                        return;
                    }

                    if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                        generateAlert.setCancelable(false);
                        generateAlert.setBtnClickList(btn_id -> {
                            if (btn_id == 1) {
                                Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                // finish();
                            }
                        });

                        generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                        generateAlert.showAlertBox();
                    } else {
                        generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                    }
                }
            } else {
                generalFunc.showError();
            }
        });
        exeWebServer.setCancelAble(false);

    }

    public void deliverNow(Intent data) {

        this.deliveryData = data;


        requestPickUp();
    }

    public void requestPickUp() {
        setLoadAvailCabTaskValue(true);

        try {
            if (!isTaxiBid) {
                requestNearestCab = new RequestNearestCab(getActContext(), generalFunc);
                requestNearestCab.run();
            }
        } catch (Exception ignored) {

        }

        String driverIds = getAvailableDriverIds();

        JSONObject cabRequestedJson = new JSONObject();
        try {
            cabRequestedJson.put("Message", "CabRequested");
            cabRequestedJson.put("sourceLatitude", "" + getPickUpLocation().getLatitude());
            cabRequestedJson.put("sourceLongitude", "" + getPickUpLocation().getLongitude());
            cabRequestedJson.put("PassengerId", generalFunc.getMemberId());
            cabRequestedJson.put("PName", generalFunc.getJsonValueStr("vName", obj_userProfile) + " " + generalFunc.getJsonValueStr("vLastName", obj_userProfile));
            cabRequestedJson.put("PPicName", generalFunc.getJsonValueStr("vImgName", obj_userProfile));
            cabRequestedJson.put("PFId", generalFunc.getJsonValueStr("vFbId", obj_userProfile));
            cabRequestedJson.put("PRating", generalFunc.getJsonValueStr("vAvgRating", obj_userProfile));
            cabRequestedJson.put("PPhone", generalFunc.getJsonValueStr("vPhone", obj_userProfile));
            cabRequestedJson.put("PPhoneC", generalFunc.getJsonValueStr("vPhoneCode", obj_userProfile));
            cabRequestedJson.put("REQUEST_TYPE", getCurrentCabGeneralType());
            cabRequestedJson.put("eFly", eFly ? "Yes" : "No");

            cabRequestedJson.put("selectedCatType", vUberXCategoryName);
            if (getDestinationStatus()) {
                cabRequestedJson.put("destLatitude", "" + getDestLocLatitude());
                cabRequestedJson.put("destLongitude", "" + getDestLocLongitude());
            } else {
                cabRequestedJson.put("destLatitude", "");
                cabRequestedJson.put("destLongitude", "");
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Logger.e("Exception", "::" + e.getMessage());
        }

        if (!generalFunc.getJsonValue("Message", cabRequestedJson.toString()).equals("")) {
            if (requestNearestCab != null) {
                requestNearestCab.setRequestData(driverIds, cabRequestedJson.toString());
            } else if (requestNearestCabTaxiBid != null) {
                requestNearestCabTaxiBid.setRequestData(driverIds, cabRequestedJson.toString());
            }

            /*if (DRIVER_REQUEST_METHOD.equals("All")) {*/
            driverIds = sendRequestAsPerFav(driverIds);
//                sendReqToAll(driverIds, cabRequestedJson.toString());
            sendRequestToDrivers(driverIds, cabRequestedJson.toString());
            /*} else if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
                sendReqByDist(driverIds, cabRequestedJson.toString());
            } else {
                driverIds = sendRequestAsPerFav(driverIds);
                sendReqToAll(driverIds, cabRequestedJson.toString());
            }*/
        }


    }

    private String sendRequestAsPerFav(String oriDriverIds) {
        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
            String favDriverIds = getDriverID(true);
            String nonfavDriverIds = getDriverID(false);

            String driverIds = "";

            if (Utils.checkText(favDriverIds) && Utils.checkText(nonfavDriverIds)) {
                driverIds = favDriverIds + "-" + nonfavDriverIds;
            } else if (Utils.checkText(favDriverIds)) {
                driverIds = favDriverIds;
            } else if (Utils.checkText(nonfavDriverIds)) {
                driverIds = nonfavDriverIds;
            } else {
                driverIds = oriDriverIds;
            }

            return driverIds;
        } else {
            return oriDriverIds;
        }

    }

    public void sendReqToAll(String driverIds, String cabRequestedJson) {
        isreqnow = true;
        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {

            requestSentToFavDrivers = false;

            if (driverIds.contains("-")) {
                driverIDS = driverIds.split("-");
                sendRequestToDrivers(driverIDS[0], cabRequestedJson);
            } else {
                requestSentToFavDrivers = true;
                sendRequestToDrivers(driverIds, cabRequestedJson);
            }
        } else {
            sendRequestToDrivers(driverIds, cabRequestedJson);
        }

        if (allCabRequestTask != null) {
            allCabRequestTask.stopRepeatingTask();
            allCabRequestTask = null;
        }

        int interval = GeneralFunctions.parseIntegerValue(30, generalFunc.getJsonValue("RIDER_REQUEST_ACCEPT_TIME", generalFunc.retrieveValue(Utils.USER_PROFILE_JSON)));

        allCabRequestTask = new RecurringTask((interval + 5) * 1000);
        allCabRequestTask.startRepeatingTask();
        allCabRequestTask.setTaskRunListener(instance -> {
            if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
                if (!requestSentToFavDrivers && Utils.checkText(driverIDS[1])) {
                    requestSentToFavDrivers = true;
                    sendRequestToDrivers(driverIDS[1], cabRequestedJson);
                    sendReqToNonFav(interval);
                } else {
                    setRetryReqBtn(true, false);
                    allCabRequestTask.stopRepeatingTask();
                }

            } else {
                setRetryReqBtn(true, false);
                allCabRequestTask.stopRepeatingTask();
            }
        });

    }


    public void sendReqToNonFav(int interval) {

        if (allNonFavCabRequestTask != null) {
            allNonFavCabRequestTask.stopRepeatingTask();
            allNonFavCabRequestTask = null;
        }

        allNonFavCabRequestTask = new RecurringTask((interval + 5) * 1000);

        allNonFavCabRequestTask.startRepeatingTask();
        allNonFavCabRequestTask.setTaskRunListener(instance -> {
            setRetryReqBtn(true, false);
            allNonFavCabRequestTask.stopRepeatingTask();
            if (allCabRequestTask != null) allCabRequestTask.stopRepeatingTask();
        });
    }


    public void sendReqByDist(String driverIds, String cabRequestedJson) {
        if (sendNotificationToDriverByDist == null) {
            sendNotificationToDriverByDist = new SendNotificationsToDriverByDist(driverIds, cabRequestedJson);
        } else {
            sendNotificationToDriverByDist.startRepeatingTask();
        }
    }

    public void setRetryReqBtn(boolean isVisible, boolean isAgain) {
        if (isVisible) {
            if (requestNearestCab != null) {
                requestNearestCab.setVisibleBottomArea(View.VISIBLE, false, isAgain);
            } else if (requestNearestCabTaxiBid != null) {
                requestNearestCabTaxiBid.setVisibleBottomArea(isVisible, isAgain);
            }
        } else {
            if (requestNearestCab != null) {
                requestNearestCab.setInVisibleBottomArea(View.GONE, isAgain);
            } else if (requestNearestCabTaxiBid != null) {
                requestNearestCabTaxiBid.setVisibleBottomArea(isVisible, isAgain);
            }
        }
    }

    public void retryReqBtnPressed(String driverIds, String cabRequestedJson) {
        isRetryRequest = "Yes";

        driverIds = sendRequestAsPerFav(driverIds);
        sendRequestToDrivers(driverIds, cabRequestedJson.toString());

        /*if (DRIVER_REQUEST_METHOD.equals("All")) {
            driverIds = sendRequestAsPerFav(driverIds);
            sendReqToAll(driverIds, cabRequestedJson.toString());
        } else if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
            sendReqByDist(driverIds, cabRequestedJson.toString());
        } else {
            driverIds = sendRequestAsPerFav(driverIds);
            sendReqToAll(driverIds, cabRequestedJson.toString());
        }*/

        setRetryReqBtn(false, false);
    }

    public String getDriverID(boolean getFavId) {

        String driverIds = "";

        ArrayList<HashMap<String, String>> favLoadedDriverList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> nonFavLoadedDriverList = new ArrayList<HashMap<String, String>>();
        String fav_driverIds = "";
        String noFav_driverIds = "";


        if (currentLoadedDriverList == null) {

            return driverIds;
        }

        ArrayList<HashMap<String, String>> finalLoadedDriverList = new ArrayList<HashMap<String, String>>();
        finalLoadedDriverList.addAll(currentLoadedDriverList);

        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes") && DRIVER_REQUEST_METHOD.equalsIgnoreCase("ALL")) {


            for (int j = 0; j < finalLoadedDriverList.size(); j++) {
                HashMap<String, String> driversDataMap = finalLoadedDriverList.get(j);
                String eFavDriver = driversDataMap.get("eFavDriver");

                if (eFavDriver.equalsIgnoreCase("Yes")) {
                    favLoadedDriverList.add(driversDataMap);
                } else {
                    nonFavLoadedDriverList.add(driversDataMap);
                }
            }

            if (favLoadedDriverList.size() > 0 && getFavId) {

                for (int fd = 0; fd < favLoadedDriverList.size(); fd++) {
                    String iDriverId = favLoadedDriverList.get(fd).get("driver_id");

                    fav_driverIds = fav_driverIds.equals("") ? iDriverId : (fav_driverIds + "," + iDriverId);
                }

            }

            if (nonFavLoadedDriverList.size() > 0 && !getFavId) {
                for (int nfd = 0; nfd < nonFavLoadedDriverList.size(); nfd++) {
                    String iDriverId = nonFavLoadedDriverList.get(nfd).get("driver_id");

                    noFav_driverIds = noFav_driverIds.equals("") ? iDriverId : (noFav_driverIds + "," + iDriverId);
                }
            }
        }

        return getFavId ? fav_driverIds : noFav_driverIds;

    }


    public void setLoadAvailCabTaskValue(boolean value) {
        if (loadAvailCabs != null) {
            loadAvailCabs.setTaskKilledValue(value);
        }
    }

    public void setCurrentLoadedDriverList(ArrayList<HashMap<String, String>> currentLoadedDriverList) {
        this.currentLoadedDriverList = currentLoadedDriverList;
        if (app_type.equalsIgnoreCase("UberX") || isUfx) {
            // load list here but wait for 5 seconds
            redirectToMapOrList(Utils.Cab_UberX_Type_List, true);
            findViewById(R.id.driverListAreaLoader).setVisibility(View.GONE);
        }
    }

    public ArrayList<String> getDriverLocationChannelList() {

        ArrayList<String> channels_update_loc = new ArrayList<>();

        if (currentLoadedDriverList != null) {

            for (int i = 0; i < currentLoadedDriverList.size(); i++) {
                channels_update_loc.add(Utils.pubNub_Update_Loc_Channel_Prefix + "" + (currentLoadedDriverList.get(i).get("driver_id")));
            }

        }
        return channels_update_loc;
    }

    public ArrayList<String> getDriverLocationChannelList(ArrayList<HashMap<String, String>> listData) {

        ArrayList<String> channels_update_loc = new ArrayList<>();

        if (listData != null) {

            for (int i = 0; i < listData.size(); i++) {
                channels_update_loc.add(Utils.pubNub_Update_Loc_Channel_Prefix + "" + (listData.get(i).get("driver_id")));
            }

        }
        return channels_update_loc;
    }

    public String getAvailableDriverIds() {
        String driverIds = "";

        if (currentLoadedDriverList == null) {
            return driverIds;
        }

        ArrayList<HashMap<String, String>> finalLoadedDriverList = new ArrayList<HashMap<String, String>>();
        finalLoadedDriverList.addAll(currentLoadedDriverList);

        if (DRIVER_REQUEST_METHOD.equals("Distance")) {
            Collections.sort(finalLoadedDriverList, new HashMapComparator("DIST_TO_PICKUP"));
        }


        if (!DRIVER_REQUEST_METHOD.equals("All") && generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
            ArrayList<HashMap<String, String>> favDriverLoadedDriverList = new ArrayList<HashMap<String, String>>();
            ArrayList<HashMap<String, String>> sequeceLoadedDriverList = new ArrayList<HashMap<String, String>>();

            for (HashMap<String, String> item : finalLoadedDriverList) {
                if (item.get("eFavDriver").equalsIgnoreCase("Yes")) {
                    favDriverLoadedDriverList.add(item);
                } else {
                    sequeceLoadedDriverList.add(item);
                }
            }
            if (DRIVER_REQUEST_METHOD.equals("Distance")) {
                Collections.sort(sequeceLoadedDriverList, new HashMapComparator("DIST_TO_PICKUP"));
            }

            finalLoadedDriverList.clear();
            finalLoadedDriverList.addAll(favDriverLoadedDriverList);
            finalLoadedDriverList.addAll(sequeceLoadedDriverList);
        }
       /* else if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {

            Collections.sort(finalLoadedDriverList, new favDriverComparator("eFavDriver", 0.0));
        }*/


        for (int i = 0; i < finalLoadedDriverList.size(); i++) {
            String iDriverId = finalLoadedDriverList.get(i).get("driver_id");

            driverIds = driverIds.equals("") ? iDriverId : (driverIds + "," + iDriverId);
        }

        return driverIds;
    }

    public void sendRequestToDrivers(String driverIds, String cabRequestedJson) {


        HashMap<String, Object> requestCabData = new HashMap<String, Object>();
        requestCabData.put("type", "sendRequestToDrivers");
        requestCabData.put("message", cabRequestedJson);
        requestCabData.put("userId", generalFunc.getMemberId());


        requestCabData.put("PickUpAddress", getPickUpLocationAddress());
        requestCabData.put("iFromStationId", iFromStationId);
        requestCabData.put("eWalletIgnore", eWalletIgnore);


        requestCabData.put("vTollPriceCurrencyCode", tollcurrancy);

        requestCabData.put("ePayWallet", "No");

//        if (!isCashSelected && !generalFunc.getJsonValue("SYSTEM_PAYMENT_FLOW", userProfileJson).equalsIgnoreCase("Method-1")) {
//            requestCabData.put("ePayWallet", "Yes");
//        }


        if (!selectReasonId.equalsIgnoreCase("")) {
            requestCabData.put("iTripReasonId", selectReasonId);
        }
        if (!vReasonTitle.equalsIgnoreCase("")) {
            requestCabData.put("vReasonTitle", vReasonTitle);
        }

        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && generalFunc.retrieveValue(Utils.BOOK_FOR_ELSE_ENABLE_KEY).equalsIgnoreCase("yes")) {

            String BFSE_SELECTED_CONTACT_KEY = generalFunc.retrieveValue(Utils.BFSE_SELECTED_CONTACT_KEY);
            if (generalFunc.containsKey(Utils.BFSE_SELECTED_CONTACT_KEY) && Utils.checkText(BFSE_SELECTED_CONTACT_KEY)) {
                Gson gson = new Gson();
                String data1 = BFSE_SELECTED_CONTACT_KEY;

                ContactModel contactdetails = gson.fromJson(data1, new TypeToken<ContactModel>() {
                }.getType());


                if (contactdetails != null && Utils.checkText(contactdetails.name) && !contactdetails.name.equalsIgnoreCase("ME")) {
                    if (Utils.checkText(contactdetails.mobileNumber)) {
                        requestCabData.put("eBookSomeElseNumber", contactdetails.mobileNumber);
                        requestCabData.put("eBookForSomeOneElse", "Yes");
                    }
                    if (Utils.checkText(contactdetails.name)) {
                        requestCabData.put("eBookSomeElseName", contactdetails.name);
                    }
                }
            }
        }

        requestCabData.put("ePaymentBy", ePaymentBy);

        if (cabSelectionFrag != null) {
            if (cabSelectionFrag.distance != null && cabSelectionFrag.time != null) {
                requestCabData.put("vDistance", cabSelectionFrag.distance);
                requestCabData.put("vDuration", cabSelectionFrag.time);
            }

            if (isPoolCabTypeIdSelected) {
                requestCabData.put("ePoolRequest", "Yes");
                requestCabData.put("iPersonSize", cabSelectionFrag.poolSeatsList.get(cabSelectionFrag.seatsSelectpos));
            }
        }

        String tollskiptxt = "";

        if (istollIgnore) {
            tollamount = 0;
            tollskiptxt = "Yes";

        } else {
            tollskiptxt = "No";
        }
        requestCabData.put("fTollPrice", tollamount + "");
        requestCabData.put("eTollSkipped", tollskiptxt);

        requestCabData.put("eType", getCurrentCabGeneralType());

        if ((app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX))) {
            if (isUfx) {
                requestCabData.put("eType", Utils.CabGeneralType_UberX);
                requestCabData.put("driverIds", generalFunc.retrieveValue(Utils.SELECTEDRIVERID));
            } else {
                requestCabData.put("driverIds", driverIds);
            }

        }
        if ((app_type.equalsIgnoreCase("UberX") || isUfx)) {
            requestCabData.put("driverIds", generalFunc.retrieveValue(Utils.SELECTEDRIVERID));
        } else {
            requestCabData.put("driverIds", driverIds);
        }
        requestCabData.put("SelectedCarTypeID", "" + selectedCabTypeId);

        if (!isMultiDelivery()) {
            requestCabData.put("DestLatitude", getDestLocLatitude());
            requestCabData.put("DestLongitude", getDestLocLongitude());
            requestCabData.put("DestAddress", getDestAddress());
            requestCabData.put("iToStationId", iToStationId);
        }

        if (isUfx) {
            requestCabData.put("PickUpLatitude", pickup_latitude);
            requestCabData.put("PickUpLongitude", pickup_longitude);
        } else {
            requestCabData.put("PickUpLatitude", "" + getPickUpLocation().getLatitude());
            requestCabData.put("PickUpLongitude", "" + getPickUpLocation().getLongitude());
        }


        if (deliveryData != null) {
            if (isMultiDelivery()) {
                String data = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_JSON_DETAILS_KEY);

                if (deliveryData.hasExtra("isMulti")) {

                    requestCabData.put("PromoCode", deliveryData.getStringExtra("promocode"));
                    requestCabData.put("ePaymentBy", deliveryData.getStringExtra("paymentMethod"));

                    requestCabData.put("eType", Utils.eType_Multi_Delivery);
                }

                if (deliveryData.hasExtra("total_del_dist")) {
                    requestCabData.put("total_del_dist", "" + deliveryData.getStringExtra("total_del_dist"));
                }

                if (deliveryData.hasExtra("total_del_time")) {
                    requestCabData.put("total_del_time", "" + deliveryData.getStringExtra("total_del_time"));
                }

                JSONArray array = generalFunc.getJsonArray(data);
                requestCabData.put("delivery_arr", array);

            }
        }

        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride) && stopOverPointsList.size() > 2) {
            JSONArray jaStore = new JSONArray();

            for (int j = 0; j < stopOverPointsList.size(); j++) {
                Stop_Over_Points_Data data = stopOverPointsList.get(j);

                if (data.isDestination()) {
                    try {
                        JSONObject stopOverPointsObj = new JSONObject();
                        stopOverPointsObj.put("tDAddress", "" + data.getDestAddress());
                        stopOverPointsObj.put("tDestLatitude", "" + data.getDestLat());
                        stopOverPointsObj.put("tDestLongitude", "" + data.getDestLong());
                        jaStore.put(stopOverPointsObj);

                    } catch (Exception e) {
                        Logger.e("Exception", "::" + e.getMessage());
                    }

                }


            }
            requestCabData.put("stopoverpoint_arr", jaStore);
        }

        if ((app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX))) {
            if (isUfx) {
                requestCabData.put("Quantity", getIntent().getStringExtra("Quantity"));
            }
        }

        if (!isMultiDelivery()) {
            if (app_type.equalsIgnoreCase("UberX") || isUfx) {
                requestCabData.put("PromoCode", appliedPromoCode);
                requestCabData.put("iUserAddressId", getIntent().getStringExtra("iUserAddressId"));
                requestCabData.put("tUserComment", userComment);

                if (getIntent().getStringExtra("Quantity").equals("0")) {
                    requestCabData.put("Quantity", "1");
                } else {
                    requestCabData.put("Quantity", getIntent().getStringExtra("Quantity"));
                }
            }

            if (cabSelectionFrag != null) {
                requestCabData.put("PromoCode", cabSelectionFrag.getAppliedPromoCode());

                if (!cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
                    requestCabData.put("iRentalPackageId", cabSelectionFrag.iRentalPackageId);
                }
            }
        }

        boolean isLoader = false;
        if (isTaxiBid) {
            if (requestNearestCabTaxiBid != null) {
                requestCabData.put("isTaxiBid", "Yes");
                requestCabData.put("OfferFare", requestNearestCabTaxiBid.getOfferFareValue());

                if (requestNearestCabTaxiBid.aOfferFare != null) {
                    requestCabData.put("OfferFare", requestNearestCabTaxiBid.aOfferFare);
                }
                if (requestNearestCabTaxiBid.aDriverId != null) {
                    requestCabData.put("driverId", requestNearestCabTaxiBid.aDriverId);
                }
                if (requestNearestCabTaxiBid.aMsgCode != null) {
                    requestCabData.put("RequestMsgCode", requestNearestCabTaxiBid.aMsgCode);
                    isLoader = true;
                }
            }
        }
        if (eAskToWait.equalsIgnoreCase("Yes")) {
            requestCabData.put("eSkipWait", "Yes");
        }
        requestCabData.put("isRetryRequest", isRetryRequest);

        ServerTask exeWebServer = ApiHandler.execute(getActContext(), requestCabData, isLoader, generalFunc, responseString -> {

            if (cabSelectionFrag != null) {
                cabSelectionFrag.isclickableridebtn = false;
            }

            if (responseString != null && !responseString.equals("")) {

                generalFunc.sendHeartBeat();

                boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);

                if (!isDataAvail) {
                    Bundle bn = new Bundle();
                    bn.putString("msg", "" + generalFunc.getJsonValue(Utils.message_str, responseString));

                    String message = generalFunc.getJsonValue(Utils.message_str, responseString);

                    if (message.equals("SESSION_OUT")) {
                        closeRequestDialog(false);
                        MyApp.getInstance().notifySessionTimeOut();
                        Utils.runGC();
                        return;
                    }
                    if (message.equalsIgnoreCase("LOW_WALLET_AMOUNT")) {

                        closeRequestDialog(false);

                        String walletMsg = "";

                        String low_balance_content_msg = generalFunc.getJsonValue("low_balance_content_msg", responseString);

                        if (low_balance_content_msg != null && !low_balance_content_msg.equalsIgnoreCase("")) {
                            walletMsg = low_balance_content_msg;
                        } else {
                            walletMsg = generalFunc.retrieveLangLBl("", "LBL_WALLET_LOW_AMOUNT_MSG_TXT");
                        }


                        boolean isCancelShow = false;
                        if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                            isCancelShow = true;
                        }
                        generalFunc.showGeneralMessage(generalFunc.retrieveLangLBl("", "LBL_LOW_WALLET_BALANCE"), walletMsg, generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No") ? generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN") : generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_ADD_MONEY"), isCancelShow ? generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT") : "", button_Id -> {
                            if (button_Id == 1) {
                                new ActUtils(getActContext()).startAct(MyWalletActivity.class);
                            } else if (button_Id == 0) {
                                if (generalFunc.getJsonValue("IS_RESTRICT_TO_WALLET_AMOUNT", responseString).equalsIgnoreCase("No")) {
                                    eWalletIgnore = "Yes";
                                    requestPickUp();
                                }
                            }
                        });

                        return;

                    }

                    if (message.equals("NO_CARS") && !DRIVER_REQUEST_METHOD.equalsIgnoreCase("ALL") && sendNotificationToDriverByDist != null) {
                        sendNotificationToDriverByDist.incTask();
                        return;

                    }
                    again_Msg = message;
                    eAskToWait = generalFunc.getJsonValue("eAskToWait", responseString);
                    DRIVER_IDS = generalFunc.getJsonValue("DRIVER_IDS", responseString);
                    time_val = generalFunc.getJsonValue("time_val", responseString);
                    showRetryBtn = generalFunc.getJsonValue("showRetryBtn", responseString);

                    if (eAskToWait.equalsIgnoreCase("Yes") && generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")) {
                        setRetryReqBtn(true, true);
                    } else if (message.equals("NO_CARS") || message.equals("LBL_PICK_DROP_LOCATION_NOT_ALLOW") || message.equals("LBL_DROP_LOCATION_NOT_ALLOW") || message.equals("LBL_PICKUP_LOCATION_NOT_ALLOW")) {
                        closeRequestDialog(false);

                        String messageLabel = "";

                        if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride)) {
                            messageLabel = "LBL_NO_CAR_AVAIL_TXT";

                        } else if (getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                            messageLabel = "LBL_NO_PROVIDERS_AVAIL_TXT";
                        } else {
                            messageLabel = "LBL_NO_CARRIERS_AVAIL_TXT";
                        }
                        if (!generalFunc.getJsonValue("eAskToWait", responseString).equalsIgnoreCase("Yes")/* && !generalFunc.getJsonValue("ENABLE_NEW_HOME_SCREEN_LAYOUT_APP_23", obj_userProfile).equals("Yes")*/) {
                            buildMessage(generalFunc.retrieveLangLBl("", message.equals("NO_CARS") ? messageLabel : message), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), false);
                        }

                        if (loadAvailCabs != null) {
                            isufxbackview = false;
                            loadAvailCabs.onResumeCalled();
                        }

                    } else if (message.equals(Utils.GCM_FAILED_KEY) || message.equals(Utils.APNS_FAILED_KEY)) {
                        releaseScheduleNotificationTask();
                        generalFunc.restartApp();
                    } else if (generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_PHONE_VERIFY") || generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_PHONE_VERIFY") || generalFunc.getJsonValue(Utils.message_str, responseString).equals("DO_EMAIL_VERIFY")) {
                        closeRequestDialog(true);
                        isFixFare = false;
                        isTollCostdilaogshow = false;
                        accountVerificationAlert(generalFunc.retrieveLangLBl("", "LBL_ACCOUNT_VERIFY_ALERT_RIDER_TXT"), bn);

                        if (loadAvailCabs != null) {
                            isufxbackview = false;
                            loadAvailCabs.onResumeCalled();
                        }

                    } else if (!generalFunc.getJsonValue(Utils.message_str, responseString).equalsIgnoreCase("")) {
                        closeRequestDialog(false);

                        if (generalFunc.getJsonValue("isShowContactUs", responseString) != null && generalFunc.getJsonValue("isShowContactUs", responseString).equalsIgnoreCase("Yes")) {
                            final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
                            generateAlert.setCancelable(false);
                            generateAlert.setBtnClickList(btn_id -> {
                                if (btn_id == 1) {
                                    Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)));
                            generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
                            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_CONTACT_US_TXT"));

                            generateAlert.showAlertBox();
                        } else {
                            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", generalFunc.getJsonValue(Utils.message_str, responseString)), "", generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), null);
                        }
                    } else {
                        closeRequestDialog(false);
                        buildMessage(generalFunc.retrieveLangLBl("", "LBL_REQUEST_FAILED_PROCESS"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), true);
                    }

                }
            } else {
                if (reqSentErrorDialog != null) {
                    reqSentErrorDialog.closeAlertBox();
                    reqSentErrorDialog = null;
                }

                InternetConnection intConnection = new InternetConnection(getActContext());

                reqSentErrorDialog = generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", intConnection.isNetworkConnected() ? "LBL_TRY_AGAIN_TXT" : "LBL_NO_INTERNET_TXT"), generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"), generalFunc.retrieveLangLBl("", "LBL_RETRY_TXT"), buttonId -> {
                    if (buttonId == 1) {
                        sendRequestToDrivers(driverIds, cabRequestedJson);
                    } else {
                        closeRequestDialog(true);
                        MyApp.getInstance().restartWithGetDataApp();
                    }
                });
            }
        });
        exeWebServer.setCancelAble(false);

        generalFunc.sendHeartBeat();
    }

    public void accountVerificationAlert(String message, final Bundle bn) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            if (btn_id == 1) {
                generateAlert.closeAlertBox();
//                bn.putString("msg", "DO_PHONE_VERIFY");
                (new ActUtils(getActContext())).startActForResult(VerifyInfoActivity.class, bn, Utils.VERIFY_INFO_REQ_CODE);
            } else if (btn_id == 0) {
                generateAlert.closeAlertBox();
            }
        });
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
        generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_CANCEL_TRIP_TXT"));
        generateAlert.showAlertBox();

    }

    public void closeRequestDialog(boolean isSetDefault) {

        if (requestNearestCab != null) {
            requestNearestCab.dismissDialog();
        } else if (requestNearestCabTaxiBid != null) {
            requestNearestCabTaxiBid.dismissDialog();
        }

        if (loadAvailCabs != null) {
            loadAvailCabs.selectProviderId = "";

        }

        if (!isDriverAssigned) {
            setLoadAvailCabTaskValue(false);
        }

        releaseScheduleNotificationTask();

        if (isSetDefault) {
            setDefaultView();
        }

    }

    public void releaseScheduleNotificationTask() {
        if (allCabRequestTask != null) {
            allCabRequestTask.stopRepeatingTask();
            allCabRequestTask = null;
        }
        if (allNonFavCabRequestTask != null) {
            allNonFavCabRequestTask.stopRepeatingTask();
            allNonFavCabRequestTask = null;
        }

        if (sendNotificationToDriverByDist != null) {
            sendNotificationToDriverByDist.stopRepeatingTask();
            sendNotificationToDriverByDist = null;
        }
    }

    public DriverDetailFragment getDriverDetailFragment() {
        return driverDetailFrag;
    }

    public void buildMessage(String message, String positiveBtn, final boolean isRestart) {
        final GenerateAlertBox generateAlert = new GenerateAlertBox(getActContext());
        generateAlert.setCancelable(false);
        generateAlert.setBtnClickList(btn_id -> {
            generateAlert.closeAlertBox();
            if (isRestart) {
                generalFunc.restartApp();
            } else if (!TextUtils.isEmpty(tripId) && eTripType.equals(Utils.eType_Multi_Delivery)) {
                MyApp.getInstance().restartWithGetDataApp(tripId);
            }
        });
        generateAlert.setContentMessage("", message);
        generateAlert.setPositiveBtn(positiveBtn);
        generateAlert.showAlertBox();
    }


    public void onGcmMessageArrived(String message) {

        String driverMsg = generalFunc.getJsonValue("Message", message);
        String eType = generalFunc.getJsonValue("eType", message);

        if (!eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
            if (!assignedTripId.equals("") && !generalFunc.getJsonValue("iTripId", message).equalsIgnoreCase("") && !generalFunc.getJsonValue("iTripId", message).equalsIgnoreCase(assignedTripId)) {
                return;
            }
        }
        currentTripId = generalFunc.getJsonValue("iTripId", message);

        if (driverMsg.equals("CabRequestAccepted")) {
            if (isDriverAssigned) {
                return;
            }

            if (generalFunc.getJsonValue("eSystem", message) != null && generalFunc.getJsonValue("eSystem", message).equalsIgnoreCase("DeliverAll")) {
                generalFunc.showGeneralMessage("", generalFunc.getJsonValue("vTitle", message));
                return;
            }

            isDriverAssigned = true;
            addDrawer.setIsDriverAssigned(isDriverAssigned);
            userLocBtnImgView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
            params.bottomMargin = Utils.dipToPixels(getActContext(), 210);
            assignedDriverId = generalFunc.getJsonValue("iDriverId", message);
            assignedTripId = generalFunc.getJsonValue("iTripId", message);

            generalFunc.removeValue(Utils.DELIVERY_DETAILS_KEY);

            reSetFields();

            if (cabSelectionFrag != null) {
                releaseCabSelectionInstances(false);
            }

            if (generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                pinImgView.setVisibility(View.GONE);
                setDestinationPoint("", "", "", false);
                closeRequestDialog(true);
                if (deliveryData != null) {
                    if (isMultiDelivery()) {
                        if (deliveryData.hasExtra("isMulti")) {
                            generalFunc.removeValue(Utils.MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY);
                        }
                    }
                }
                showBookingAlert(generalFunc.retrieveLangLBl("", "LBL_ONGOING_TRIP_TXT"), true);
                return;
            } else if (app_type != null && app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {

                if (!generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {

                    closeRequestDialog(false);

                    MyApp.getInstance().restartWithGetDataApp();
                    return;
                }
            }

            if (generalFunc.isJSONkeyAvail("iCabBookingId", message) && !generalFunc.getJsonValue("iCabBookingId", message).trim().equals("")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else {
                if (generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.CabGeneralType_UberX) || generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                    isDriverAssigned = false;
                    pinImgView.setVisibility(View.GONE);
                    setDestinationPoint("", "", "", false);
                    closeRequestDialog(true);
                    showBookingAlert(generalFunc.retrieveLangLBl("", "LBL_ONGOING_TRIP_TXT"), true);
                    return;
                } else {
                    MyApp.getInstance().restartWithGetDataApp();
                    //    configureAssignedDriver(false);
                    pinImgView.setVisibility(View.GONE);
                    closeRequestDialog(false);

                    if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery)) {
                        rduTollbar.setVisibility(View.GONE);
                    }
                    configureDeliveryView(true);
                }
            }

            tripStatus = "Assigned";

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (userLocBtnImgView.getVisibility() == View.VISIBLE) {
                    userLocBtnImgView.performClick();
                }
            }, 1500);


        } else if (driverMsg.equals("TripEnd")) {
            if (!isDriverAssigned) {
                return;
            }

            if (isTripEnded && !isDriverAssigned) {
                generalFunc.restartApp();
                return;
            }

            if (isTripEnded) {
                return;
            }

            tripStatus = "TripEnd";
            if (driverAssignedHeaderFrag != null) {

                isTripEnded = true;

                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.setTaskKilledValue(true);
                }
            }

        } else if (driverMsg.equals("TripStarted")) {
            try {
                if (!isDriverAssigned) {
                    return;
                }

                if (!isDriverAssigned && isTripStarted) {
                    generalFunc.restartApp();
                    return;
                }

                if (isTripStarted && !eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                    return;
                }


                // Change Status as per trip
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

                if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message)) && eType.equalsIgnoreCase(Utils.eType_Multi_Delivery)) {
                    return;
                }

                tripStatus = "TripStarted";


                isTripStarted = true;
                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.setTripStartValue(true);
                    if (driverAssignedHeaderFrag.sourceMarker != null) {
                        driverAssignedHeaderFrag.sourceMarker.remove();
                    }
                }

                if (driverDetailFrag != null) {
                    driverDetailFrag.configTripStartView(generalFunc.getJsonValue("VerificationCode", message));
                }
                userLocBtnImgView.performClick();
            } catch (Exception ignored) {

            }


        } else if (driverMsg.equals("DestinationAdded")) {
            if (!isDriverAssigned) {
                return;
            }

            // Change Status as per trip
            JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

            if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message))) {
                return;
            }

            LocalNotification.dispatchLocalNotification(getActContext(), generalFunc.retrieveLangLBl("Destination is added by driver.", "LBL_DEST_ADD_BY_DRIVER"), true);

            buildMessage(generalFunc.retrieveLangLBl("Destination is added by driver.", "LBL_DEST_ADD_BY_DRIVER"), generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"), false);

            String destLatitude = generalFunc.getJsonValue("DLatitude", message);
            String destLongitude = generalFunc.getJsonValue("DLongitude", message);
            String destAddress = generalFunc.getJsonValue("DAddress", message);
            String eFlatTrip = generalFunc.getJsonValue("eFlatTrip", message);

            setDestinationPoint(destLatitude, destLongitude, destAddress, true);
            if (driverAssignedHeaderFrag != null) {
                driverAssignedHeaderFrag.setDestinationAddress(eFlatTrip);
                driverAssignedHeaderFrag.configDestinationView();
            }
        } else if (driverMsg.equals("TripCancelledByDriver") || driverMsg.equals("TripCancelled")) {

            if (!generalFunc.getJsonValue("eType", message).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {
                if (!isDriverAssigned) {
                    generalFunc.restartApp();
                    return;
                }
            }

            if (tripStatus.equals("TripCanelled")) {
                return;
            }

            tripStatus = "TripCanelled";
            if (driverAssignedHeaderFrag != null) {
                driverAssignedHeaderFrag.setTaskKilledValue(true);
            }
        }
    }

    public DriverAssignedHeaderFragment getDriverAssignedHeaderFrag() {
        return driverAssignedHeaderFrag;
    }

    public void unSubscribeCurrentDriverChannels() {
        if (currentLoadedDriverList != null) {
            AppService.getInstance().executeService(new EventInformation.EventInformationBuilder().setChanelList(getDriverLocationChannelList()).build(), AppService.Event.UNSUBSCRIBE);

        }
    }

    public boolean isDeliver(String selctedType) {
        return (selctedType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || selctedType.equalsIgnoreCase("Deliver"));
    }

    public boolean isMultiDelivery() {
        if (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery_UberX)) {
            return (getIntent().hasExtra("fromMulti") && generalFunc.isMultiDelivery() && !isDeliver(app_type) && isDeliver(getCurrentCabGeneralType()));
        } else if (isDeliver(app_type) && getIntent().hasExtra("fromMulti")) {
            return generalFunc.isMultiDelivery();
        } else {
            return generalFunc.isMultiDelivery() && !isDeliver(app_type) && isDeliver(getCurrentCabGeneralType()) && getIntent().hasExtra("fromMulti");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (loadAvailCabs != null) {
            loadAvailCabs.onPauseCalled();
        }

        if (driverAssignedHeaderFrag != null) {
            driverAssignedHeaderFrag.onPauseCalled();
        }

        unSubscribeCurrentDriverChannels();
    }


    public void setWalletInfo() {

        String text1 = generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile));
        String text2 = "\n" + generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE");
        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(18, getActContext())), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(12, getActContext())), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(Color.parseColor("#808080")), 0, text2.length(), 0);
        CharSequence finalText = TextUtils.concat(span1, "", span2);

        addDrawer.walletbalncetxt.setText(finalText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (generalFunc.retrieveValue(Utils.ISWALLETBALNCECHANGE).equalsIgnoreCase("Yes")) {
            getWalletBalDetails();
        }

        getUserProfileJson();

        setUserInfo();


        if (addDrawer != null) {
            addDrawer.userProfileJson = obj_userProfile.toString();
        }


        if (iswallet) {

            if (addDrawer != null) {
                addDrawer.changeUserProfileJson(obj_userProfile.toString());
            }
            iswallet = false;
        }

        if (addDrawer != null) {
            //  addDrawer.walletbalncetxt.setText(generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson)));

            setWalletInfo();

//            String text1 = generalFunc.convertNumberWithRTL(generalFunc.getJsonValue("user_available_balance", userProfileJson));
//            String text2 = "/n" +generalFunc.retrieveLangLBl("wallet Balance", "LBL_WALLET_BALANCE");
//            SpannableString span1 = new SpannableString(text1);
//            span1.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(18, getActContext())), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);
//
//            SpannableString span2 = new SpannableString(text2);
//            span2.setSpan(new AbsoluteSizeSpan(Utils.dpToPx(12, getActContext())), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
//            span2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray)), 0, text2.length(), 0);
//            CharSequence finalText = TextUtils.concat(span1, "", span2);
//
//            addDrawer.walletbalncetxt.setText(finalText);

        }

        if (!schedulrefresh) {
            if (loadAvailCabs != null) {
                loadAvailCabs.onResumeCalled();
            }
        }

        app_type = generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile);


        if (driverAssignedHeaderFrag != null) {
            driverAssignedHeaderFrag.onResumeCalled();
            pinImgView.setVisibility(View.GONE);
            if (!driverAssignedHeaderFrag.isMultiDelivery()) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (userLocBtnImgView).getLayoutParams();
                //params.bottomMargin = Utils.dipToPixels(getActContext(), 200);
                params.bottomMargin = (int) getResources().getDimension(R.dimen._150sdp);
            }
        }

        if (!isufxbackview) {

            if (currentLoadedDriverList != null) {
                AppService.getInstance().executeService(new EventInformation.EventInformationBuilder().setChanelList(getDriverLocationChannelList()).build(), AppService.Event.SUBSCRIBE);
            }
        }
        if (getMap() != null) {
            checkLocation();
        }
    }

    private void getUserProfileJson() {
        obj_userProfile = generalFunc.getJsonObject(generalFunc.retrieveValue(Utils.USER_PROFILE_JSON));
    }

    public void setUserInfo() {
        View view = ((Activity) getActContext()).findViewById(android.R.id.content);
        ((MTextView) view.findViewById(R.id.userNameTxt)).setText(generalFunc.getJsonValueStr("vName", obj_userProfile) + " " + generalFunc.getJsonValueStr("vLastName", obj_userProfile));

        ((MTextView) view.findViewById(R.id.userNameTxt)).setSelected(true);
        //  ((MTextView) view.findViewById(R.id.walletbalncetxt)).setText(generalFunc.retrieveLangLBl("", "LBL_WALLET_BALANCE") + ": " + generalFunc.convertNumberWithRTL(generalFunc.getJsonValueStr("user_available_balance", obj_userProfile)));
        setWalletInfo();

        String url = CommonUtilities.USER_PHOTO_PATH + generalFunc.getMemberId() + "/" + generalFunc.getJsonValue("vImgName", obj_userProfile);
        generalFunc.checkProfileImage((SelectableRoundedImageView) view.findViewById(R.id.userImgView), url, R.mipmap.ic_no_pic_user, R.mipmap.ic_no_pic_user);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            releaseScheduleNotificationTask();
            if (getLastLocation != null) {
                getLastLocation.stopLocationUpdates();
                getLastLocation = null;
            }

            if (getMap() != null) {
                getMap().clear();
                getMap().releaseMap();
            }
            if (loadAvailCabs != null) {
                loadAvailCabs.onPauseCalled();
            }
            Utils.runGC();

        } catch (Exception ignored) {

        }

    }

    public void setDriverImgView(SelectableRoundedImageView driverImgView) {
        this.driverImgView = driverImgView;
    }

    public void pubNubMsgArrived(final String message) {

        currentTripId = generalFunc.getJsonValue("iTripId", message);
        runOnUiThread(() -> {

            String msgType = generalFunc.getJsonValue("MsgType", message);

            if (msgType.equals("TripEnd")) {

                if (!isDriverAssigned) {
                    generalFunc.restartApp();
                    return;
                }
            }
            if (msgType.equals("LocationUpdate")) {
                if (loadAvailCabs == null) {
                    return;
                }

                String iDriverId = generalFunc.getJsonValue("iDriverId", message);
                String vLatitude = generalFunc.getJsonValue("vLatitude", message);
                String vLongitude = generalFunc.getJsonValue("vLongitude", message);

                Marker driverMarker = getDriverMarkerOnPubNubMsg(iDriverId, false);

                LatLng driverLocation_update = new LatLng(GeneralFunctions.parseDoubleValue(0.0, vLatitude), GeneralFunctions.parseDoubleValue(0.0, vLongitude));
                Location driver_loc = new Location("gps");
                driver_loc.setLatitude(driverLocation_update.latitude);
                driver_loc.setLongitude(driverLocation_update.longitude);

                if (driverMarker != null) {
                    float rotation = (float) SphericalUtil.computeHeading(driverMarker.getPosition(), driverLocation_update);

                    if (generalFunc.getJsonValueStr("APP_TYPE", obj_userProfile).equalsIgnoreCase("UberX") || isUfx) {
                        rotation = 0;
                    }

                    MarkerAnim.animateMarker(driverMarker, getMap(), driver_loc, rotation, 1200);
                }

            } else if (msgType.equals("REQUEST_TIMEOUT")) {
                setRetryReqBtn(true, false);
            }/* else if (msgType.equals("TripRequestCancel")) {

                tripStatus = "TripCanelled";
                if (TextUtils.isEmpty(tripId) && eTripType.equals(Utils.CabGeneralType_Deliver) && getCurrentCabGeneralType().equals(Utils.CabGeneralType_Deliver)) {
                    if (tripId.equalsIgnoreCase(currentTripId)) {
                        if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
                            if (sendNotificationToDriverByDist != null) {
                                sendNotificationToDriverByDist.incTask();
                            }
                        }
                    }
                } else {
                    if (DRIVER_REQUEST_METHOD.equals("Distance") || DRIVER_REQUEST_METHOD.equals("Time")) {
                        if (sendNotificationToDriverByDist != null) {
                            sendNotificationToDriverByDist.incTask();
                        }
                    }
                }
            } */ else if (msgType.equals("LocationUpdateOnTrip")) {

                if (!isDriverAssigned) {
                    return;
                }

                if (generalFunc.checkLocationPermission(true)) {
                    getMap().setMyLocationEnabled(false);
                }
                // Change Status as per trip
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

                if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message))) {
                    return;
                }
                if (driverAssignedHeaderFrag != null) {
                    driverAssignedHeaderFrag.updateDriverLocation(message);
                }

            } else if (msgType.equals("VerifyTollCharges")) {

                generalFunc.restartApp();

            } else if (msgType.equals("DriverArrived")) {

                if (!isDriverAssigned) {
                    generalFunc.restartApp();
                    return;
                }

                // Change Status as per trip
                JSONObject tripDetailJson = generalFunc.getJsonObject("TripDetails", obj_userProfile);

                if (tripDetailJson != null && !generalFunc.getJsonValueStr("iDriverId", tripDetailJson).equalsIgnoreCase(generalFunc.getJsonValue("iDriverId", message))) {
                    return;
                }

                tripStatus = "DriverArrived";


                if (driverAssignedHeaderFrag != null) {

                    String vRandomCode = generalFunc.getJsonValue("vRandomCode", message);
                    String eAskCodeToUser = generalFunc.getJsonValue("eAskCodeToUser", message);
                    if (Utils.checkText(eAskCodeToUser) && Utils.checkText(vRandomCode)) {
                        driverAssignedHeaderFrag.showOtpArea(Utils.checkText(eAskCodeToUser) && eAskCodeToUser.equalsIgnoreCase("Yes") ? vRandomCode : "");
                    } else {
                        driverAssignedHeaderFrag.showOtp();
                    }


                    driverAssignedHeaderFrag.isDriverArrived = true;
                    if (generalFunc.getJsonValue("eFly", message).equalsIgnoreCase("Yes")) {
                        driverAssignedHeaderFrag.setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_FLY_ARRIVED"));
                    } else if (generalFunc.getJsonValue("eType", message).equalsIgnoreCase("Deliver") || generalFunc.getJsonValue("eType", message).equals(Utils.eType_Multi_Delivery)) {
                        driverAssignedHeaderFrag.setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_CARRIER_ARRIVED_TXT"));
                    } else {
                        driverAssignedHeaderFrag.setDriverStatusTitle(generalFunc.retrieveLangLBl("", "LBL_DRIVER_ARRIVED_TXT"));
                    }
                    getMap().clear();


                    if (driverAssignedHeaderFrag.updateDestMarkerTask != null) {
                        driverAssignedHeaderFrag.updateDestMarkerTask.stopRepeatingTask();
                        driverAssignedHeaderFrag.updateDestMarkerTask = null;
                        if (driverAssignedHeaderFrag.time_marker != null) {
                            driverAssignedHeaderFrag.time_marker.remove();
                            driverAssignedHeaderFrag.time_marker = null;
                        }
                        if (driverAssignedHeaderFrag.route_polyLine != null) {
                            driverAssignedHeaderFrag.route_polyLine.remove();
                        }
                    }
                    if (driverAssignedHeaderFrag.driverMarker != null) {
                        driverAssignedHeaderFrag.driverMarker.remove();
                        driverAssignedHeaderFrag.driverMarker = null;
                    }
                    if (driverAssignedHeaderFrag.driverData != null) {
                        driverAssignedHeaderFrag.driverData.put("DriverTripStatus", "Arrived");
                    }


                    driverAssignedHeaderFrag.configDriverLoc();
                    driverAssignedHeaderFrag.addPickupMarker();

                }

                userLocBtnImgView.performClick();

                if (driverAssignedHeaderFrag != null) {
                    if (driverAssignedHeaderFrag.isDriverArrived || driverAssignedHeaderFrag.isDriverArrivedNotGenerated) {
                        return;
                    }
                }

            } else {

                onGcmMessageArrived(message);

            }

        });

    }

    public Marker getDriverMarkerOnPubNubMsg(String iDriverId, boolean isRemoveFromList) {

        if (loadAvailCabs != null) {
            ArrayList<Marker> currentDriverMarkerList = loadAvailCabs.getDriverMarkerList();

            if (currentDriverMarkerList != null) {
                for (int i = 0; i < currentDriverMarkerList.size(); i++) {
                    Marker marker = currentDriverMarkerList.get(i);

                    String driver_id = marker.getTitle().replace("DriverId", "");

                    if (driver_id.equals(iDriverId)) {

                        if (isRemoveFromList) {
                            loadAvailCabs.getDriverMarkerList().remove(i);
                        }

                        return marker;
                    }

                }
            }
        }


        return null;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("isWhereTo", false)) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else if (getIntent().getBooleanExtra("isPlacesLocation", false)) {
            super.onBackPressed();
        } else {
            if (isMultiDelivery() || eFly) {
                pickup_loc_bar.setVisibility(View.GONE);
            } else {
                pickup_loc_bar.setVisibility(View.VISIBLE);
            }
            callBackEvent(false);
        }
    }

    public void callBackEvent(boolean status) {
        try {

            if (pickUpLocSelectedFrag != null) {
                pickUpLocSelectedFrag = null;

                if (loadAvailCabs != null) {
                    loadAvailCabs.selectProviderId = "";
                    loadAvailCabs.changeCabs();
                }


                if (mainHeaderFrag != null) {
                    mainHeaderFrag = null;
                }

                setMainHeaderView(false);
                // setDefaultView();
                return;

            }
            if (status) {
                if (requestNearestCab != null) {
                    requestNearestCab.dismissDialog();
                } else if (requestNearestCabTaxiBid != null) {
                    requestNearestCabTaxiBid.dismissDialog();
                }

                releaseScheduleNotificationTask();
            }

            if (addDrawer.checkDrawerState(false) && !isMultiDelivery()) {
                return;
            }

            if (eFly && findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE) {
                releaseCabSelectionInstances(true);
                return;
            } else if (eFly && !isPickup && cabSelectionFrag != null) {
                releaseCabSelectionInstances(true);
                return;
            }

           /* if (eFly && findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE) {
                releaseInstances(cabSelectionFrag != null ? false : true);
                return;
            }*/


            if (isMultiDelivery() && !app_type.equalsIgnoreCase("Ride-Delivery")) {
                releaseCabSelectionInstances(true);
                return;
            }
            if (cabSelectionFrag == null) {


            } else if (isDeliver(app_type) || (!app_type.equalsIgnoreCase("Ride-Delivery") && generalFunc.isMultiDelivery() && getIntent().hasExtra("fromMulti")) || (app_type.equalsIgnoreCase(Utils.CabGeneralTypeRide_Delivery) && getIntent().hasExtra("fromMulti"))) {
                releaseCabSelectionInstances(true);

            } else {


                //  PolyLineAnimator.getInstance().stopRouteAnim();
//                if (cabSelectionFrag != null) {
//                    cabSelectionFrag.manageisRentalValue();
//                }
//                getSupportFragmentManager().beginTransaction().remove(cabSelectionFrag).commit();

                if (PolyLineAnimator.getInstance() != null) {
                    PolyLineAnimator.getInstance().stopRouteAnim();
                }

                if (cabSelectionFrag != null) {
                    if (isVerticalCabscroll && sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        return;
                    }


                    cabSelectionFrag.manageisRentalValue();
                    cabSelectionFrag.releaseResources();
                    getSupportFragmentManager().beginTransaction().remove(cabSelectionFrag).commit();
                    cabSelectionFrag = null;
                    setAdsView(true);
                    if (loadAvailCabs != null) {
                        loadAvailCabs.checkAvailableCabs();
                    }

                    if (mainHeaderFrag != null) {
                        mainHeaderFrag.isSchedule = false;
                    }

                }

                if (stopOverPointsList.size() > 1) {
                    ArrayList<Stop_Over_Points_Data> tempStopOverPointsList = new ArrayList<>();
                    tempStopOverPointsList.add(stopOverPointsList.get(0));
                    stopOverPointsList.clear();
                    stopOverPointsList.addAll(tempStopOverPointsList);
                }


                eWalletDebitAllow = "";
                selectReasonId = "";
                vReasonTitle = "";

                vProfileName = "";
                ePaymentBy = "";
                vReasonName = "";
                selectPos = 0;
                vImage = "";
                getMap().clear();


                configDestinationMode(false);

                isRental = false;
                if (loadAvailCabs != null) {
                    loadAvailCabs.changeCabs();
                }

                if (isMenuImageShow) {
                    mainHeaderFrag.menuBtn.setVisibility(View.VISIBLE);
                    mainHeaderFrag.backBtn.setVisibility(View.GONE);
                }

                mainHeaderFrag.handleDestAddIcon();
                cabTypesArrList.clear();

                if (generalFunc.isMultiDelivery() && getIntent().hasExtra("fromMulti")) {
                    rideArea.performClick();
                }

                //  mainHeaderFrag.setDestinationAddress(generalFunc.retrieveLangLBl("", "LBL_ADD_DESTINATION_BTN_TXT"));
                mainHeaderFrag.setDefaultView();

                pinImgView.setVisibility(View.GONE);
                if (loadAvailCabs != null) {
                    selectedCabTypeId = loadAvailCabs.getFirstCarTypeID();
                }


                reSetButton(false);

                if (mainHeaderFrag != null) {
                    mainHeaderFrag.releaseAddressFinder();
                }


                resetMapView();

                if (pickUpLocation != null) {
                    getMap().moveCamera(new LatLng(this.pickUpLocation.getLatitude(), this.pickUpLocation.getLongitude(), Utils.defaultZomLevel));
                } else if (userLocation != null) {
                    getMap().moveCamera(cameraForUserPosition());
                }
//                userLocBtnImgView.performClick();

                if (workArea.getVisibility() == View.VISIBLE) {
                    if (getMap() != null) {
                        getMap().setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen._210sdp));
                    }
                }
                return;
            }


            super.onBackPressed();
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }
    }

    private void reSetButton(boolean changeDefaultBottomMargin) {
        resetUserLocBtnView();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) userLocBtnImgView.getLayoutParams();
        if (isPreferenceEnable) {

            if (!isPickup) {
                prefBtnImageView.setVisibility(View.GONE);
                int margin = (int) getResources().getDimension(R.dimen._8sdp);
                params.setMarginStart(margin);
                params.setMarginEnd(margin);
            } else {
                prefBtnImageView.setVisibility(View.VISIBLE);
                if (leftMargin == 0 && rightMargin == 0) {
                    leftMargin = params.leftMargin;
                    rightMargin = params.rightMargin;
                }
                params.setMargins(leftMargin, params.topMargin, rightMargin, (int) getResources().getDimension(R.dimen._15sdp));
            }
        } else {
            int margin = (int) getResources().getDimension(R.dimen._8sdp);
            params.setMarginStart(margin);
            params.setMarginEnd(margin);
        }
        userLocBtnImgView.requestLayout();
    }

    public void releaseCabSelectionInstances(boolean isBackPress) {

        if (mainHeaderFrag != null) {
            mainHeaderFrag.releaseAddressFinder();
        }

        if (loadAvailCabs != null) {
            loadAvailCabs.setTaskKilledValue(true);
        }

        if (PolyLineAnimator.getInstance() != null) {
            PolyLineAnimator.getInstance().stopRouteAnim();
        }

        if (eFly && findViewById(R.id.DetailsArea).getVisibility() == View.VISIBLE/*flyStationSelectionFragment != null*/) {
            if (!/*flyStationSelectionFragment.*/isPickup) {
                mainHeaderFrag.handleDestAddIcon();
                mainHeaderFrag.isclickabledest = false;
                mainHeaderFrag.isclickablesource = false;
                configDestinationMode(false);
            }


          /*  getSupportFragmentManager().beginTransaction().remove(flyStationSelectionFragment).commit();
            flyStationSelectionFragment = null;*/
            reSetButton(false);

            findViewById(R.id.DetailsArea).setVisibility(View.GONE);
            findViewById(R.id.dragView).setVisibility(View.VISIBLE);

        }

        boolean isCabSelFragRemoved = false;

        if (cabSelectionFrag != null) {
            cabSelectionFrag.manageisRentalValue();
            cabSelectionFrag.releaseResources();
            try {
                getSupportFragmentManager().beginTransaction().remove(cabSelectionFrag).commit();
            } catch (Exception ignored) {

            }

            cabSelectionFrag = null;
            setAdsView(true);
            isCabSelFragRemoved = true;
        }

        if (stopOverPointsList.size() > 1) {
            ArrayList<Stop_Over_Points_Data> tempStopOverPointsList = new ArrayList<>();
            tempStopOverPointsList.add(stopOverPointsList.get(0));
            stopOverPointsList.clear();
            stopOverPointsList.addAll(tempStopOverPointsList);
        }

        eWalletDebitAllow = "";
        selectReasonId = "";
        vReasonTitle = "";

        vProfileName = "";
        vReasonName = "";
        selectPos = 0;


        getMap().clear();

        if (eFly && !isPickup && isCabSelFragRemoved) {
            destAddress = "";
            destLocLatitude = "";
            destLocLongitude = "";
            showSelectionDialog(null, false);
            return;
        }

        if (isBackPress) {
            super.onBackPressed();
        }
    }

    public Context getActContext() {
        return MainActivity.this;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 1, 0, "" + generalFunc.retrieveLangLBl("", "LBL_CALL_TXT"));
        menu.add(0, 2, 0, "" + generalFunc.retrieveLangLBl("", "LBL_MESSAGE_TXT"));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == 1) {

            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + driverDetailFrag.getDriverPhone()));
                startActivity(callIntent);
            } catch (Exception ignored) {
                // TODO: handle exception
            }

        } else if (item.getItemId() == 2) {

            try {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "" + driverDetailFrag.getDriverPhone());
                startActivity(smsIntent);
            } catch (Exception ignored) {

            }

        }

        return super.onContextItemSelected(item);
    }

    private static final int WEBVIEWPAYMENT = 001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Location pickUpLoc_new = new Location("");
            pickUpLocationAddress = data.getStringExtra("Address");
            pickUp_tmpAddress = data.getStringExtra("Address");
            if (isMultiDelivery()) {
                pickup_loc_bar.setVisibility(View.GONE);
            } else {
                pickup_loc_bar.setVisibility(View.VISIBLE);
            }
            pickup_latitude = data.getStringExtra("Latitude");
            pickup_longitude = data.getStringExtra("Longitude");
            pickup_address = data.getStringExtra("Address");
            pickup_location.setText(data.getStringExtra("Address"));
            pickUpLoc_new.setLatitude(Double.parseDouble(data.getStringExtra("Latitude")));
            pickUpLoc_new.setLongitude(Double.parseDouble(data.getStringExtra("Longitude")));
            this.pickUpLocation = pickUpLoc_new;
            addOrResetStopOverPoints(Double.parseDouble(data.getStringExtra("Latitude")), Double.parseDouble(data.getStringExtra("Longitude")), pickup_address, true);

        }

        if (requestCode == WEBVIEWPAYMENT) {
            getProfilePaymentModel.getProfilePayment(getCurrentCabGeneralType(), this, this, false, false);

        } else if (requestCode == Utils.MY_PROFILE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            getUserProfileJson();
            addDrawer.obj_userProfile.toString();
        } else if (requestCode == Utils.VERIFY_INFO_REQ_CODE && resultCode == RESULT_OK && data != null) {

            String msgType = data.getStringExtra("MSG_TYPE");

            if (msgType.equalsIgnoreCase("EDIT_PROFILE")) {
                addDrawer.openMenuProfile();
            }
            getUserProfileJson();
            addDrawer.obj_userProfile.toString();
            addDrawer.obj_userProfile = obj_userProfile;
            addDrawer.buildDrawer();
        } else if (requestCode == Utils.VERIFY_INFO_REQ_CODE) {

            getUserProfileJson();
            addDrawer.obj_userProfile.toString();
            addDrawer.obj_userProfile = obj_userProfile;
            addDrawer.buildDrawer();
        } else if (requestCode == Utils.CARD_PAYMENT_REQ_CODE && resultCode == RESULT_OK && data != null) {

            iswallet = true;
            getUserProfileJson();

            if (cabSelectionFrag != null) {
                cabSelectionFrag.isCardValidated = true;
            }

            addDrawer.changeUserProfileJson(obj_userProfile.toString());
        } else if (requestCode == Utils.DELIVERY_DETAILS_REQ_CODE && resultCode == RESULT_OK && data != null) {
            try {
                if (!getCabReqType().equals(Utils.CabReqType_Later)) {
                    isdelivernow = true;
                } else {
                    isdeliverlater = true;
                }

                deliveryData = data;
                checkSurgePrice("", data);

            } catch (Exception ignored) {

            }
        } else if (requestCode == Utils.MULTI_DELIVERY_DETAILS_REQ_CODE && resultCode == RESULT_OK && data != null) {
            try {

                if (loadAvailCabs != null) {
                    loadAvailCabs.isMulti = true;
                    loadAvailCabs.filterDrivers(true);
                }


                if (!data.getStringExtra("cabRquestType").equals(Utils.CabReqType_Later)) {
                    isdelivernow = true;
                } else {
                    isdeliverlater = true;
                }


                if (isdeliverlater) {
                    selectedDateTime = data.getStringExtra("selectedTime");
                    checkSurgePrice(data.getStringExtra("selectedTime"), data);
                } else {
                    checkSurgePrice("", data);
                }

            } catch (Exception ignored) {

            }
        } else if (requestCode == Utils.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                LatLng placeLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                setDestinationPoint(placeLocation.latitude + "", placeLocation.longitude + "", place.getAddress().toString(), true);
                mainHeaderFrag.setDestinationAddress(place.getAddress().toString());

                if (getMap() != null) {
                    getMap().clear();
                    getMap().moveCamera(placeLocation.zoom(Utils.defaultZomLevel));
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);


                generalFunc.showMessage(generalFunc.getCurrentView(MainActivity.this), status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }


        } else if (requestCode == Utils.ASSIGN_DRIVER_CODE) {

            if (data != null && data.hasExtra("callGetDetail")) {
                MyApp.getInstance().restartWithGetDataApp();
            } else {
                if (app_type.equals(Utils.CabGeneralTypeRide_Delivery_UberX)) {
                    if (!isUfx) {

                        if (!((generalFunc.getJsonValueStr("vTripStatus", obj_userProfile).equalsIgnoreCase("Active") || generalFunc.getJsonValueStr("vTripStatus", obj_userProfile).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValueStr("eType", obj_userProfile).equalsIgnoreCase(Utils.CabGeneralType_UberX))) {

                            Bundle bn = new Bundle();
                            new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                            finishAffinity();

                        }

                    } else {
                        isUfx = false;
                        Bundle bn = new Bundle();
                        new ActUtils(getActContext()).startActWithData(MainActivity.class, bn);
                        finishAffinity();
                    }
                } else {

                    if ((generalFunc.getJsonValueStr("vTripStatus", obj_userProfile).equalsIgnoreCase("Active") || generalFunc.getJsonValueStr("vTripStatus", obj_userProfile).equalsIgnoreCase("On Going Trip")) && !generalFunc.getJsonValueStr("eType", obj_userProfile).equalsIgnoreCase(Utils.CabGeneralType_UberX)) {


                    } else if ((isDeliver(app_type) || isMultiDeliveryTrip || app_type.equals("Ride-Delivery")) && generalFunc.isMultiDelivery()) {
                        if (isMultiDeliveryTrip && Utils.checkText(tripId) && driverAssignedHeaderFrag != null) {
                            MyApp.getInstance().restartWithGetDataApp();
                        }
                    } else {

                        Bundle bn = new Bundle();
                        new ActUtils(getActContext()).startActWithData(UberXHomeActivity.class, bn);
                        finishAffinity();
                    }
                }
            }
        } else if (requestCode == Utils.MULTIDELIVERY_HISTORY_RATE_CODE) {
            MyApp.getInstance().restartWithGetDataApp();
        } else if (requestCode == Utils.REQUEST_CODE_GPS_ON) {

//            gpsEnabled();

        } else if (requestCode == Utils.SEARCH_PICKUP_LOC_REQ_CODE && resultCode == RESULT_OK && data != null && getMap() != null) {

            pickup_location.setText(pickup_address);

            if (data.getStringExtra("Address") != null) {
                pickUp_tmpAddress = data.getStringExtra("Address");
                pickUpLocationAddress = pickUp_tmpAddress;
                pickup_location.setText(data.getStringExtra("Address"));
            }


            pickUp_tmpLatitude = GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Latitude"));
            pickUp_tmpLongitude = GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Longitude"));

//            final Location location = new Location("gps");
//            location.setLatitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Latitude")));
//            location.setLongitude(GeneralFunctions.parseDoubleValue(0.0, data.getStringExtra("Longitude")));
//            onLocationUpdate(location);

            //  mainHeaderFrag.setPickUpAddress(pickUp_tmpAddress);

            Location pickUpLoc = new Location("");
            pickUpLoc.setLatitude(pickUp_tmpLatitude);
            pickUpLoc.setLongitude(pickUp_tmpLongitude);
            this.pickUpLocation = pickUpLoc;


            if (getMap() != null) {
                getMap().moveCamera(new LatLng(pickUp_tmpLatitude, pickUp_tmpLongitude, Utils.defaultZomLevel));
            }

            if (isMultiStopOverEnabled()) {
                mainHeaderFrag.addOrResetStopOverPoints(pickUp_tmpLatitude, pickUp_tmpLongitude, pickUp_tmpAddress, true);

            }
            getAvailableDriverIds();
            initializeLoadCab();

            destSelectTxt.performClick();

        } else if (requestCode == Utils.UFX_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                schedulrefresh = true;
                isufxbackview = true;
                ridelaterView.setVisibility(View.GONE);

                if (loadAvailCabs != null) {
                    loadAvailCabs.setTaskKilledValue(true);
                }
                if (data != null) {
                    appliedPromoCode = data.getStringExtra("promocode");
                    userComment = data.getStringExtra("comment");
                }

                checkSurgePrice("", data);
            } else {
                loadAvailCabs.selectProviderId = "";
            }
        } else if (requestCode == Utils.SCHEDULE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                SelectDate = data.getStringExtra("SelectDate");
                sdate = data.getStringExtra("Sdate");
                Stime = data.getStringExtra("Stime");
            }
//
            bookingtype = Utils.CabReqType_Later;

            uberXDriverListArea.setVisibility(View.VISIBLE);
            uberXNoDriverTxt.setVisibility(View.GONE);
            ridelaterView.setVisibility(View.GONE);
            (findViewById(R.id.driverListAreaLoader)).setVisibility(View.VISIBLE);
            (findViewById(R.id.searchingDriverTxt)).setVisibility(View.VISIBLE);

            if (loadAvailCabs != null) {
                loadAvailCabs.changeCabs();
            }
            schedulrefresh = false;

        } else if (requestCode == Utils.OTHER_AREA_CLICKED_CODE) {

            rideArea.performClick();
        } else if (requestCode == RENTAL_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && !data.getStringExtra("iRentalPackageId").equalsIgnoreCase("")) {
                    cabSelectionFrag.iRentalPackageId = data.getStringExtra("iRentalPackageId");
                }


                if (cabRquestType.equalsIgnoreCase(Utils.CabReqType_Now)) {
                    continuePickUpProcess();
                } else {
                    checkSurgePrice(selectedTime, deliveryData);

                }
            }
        } else if (requestCode == Utils.SELECT_ORGANIZATION_PAYMENT_CODE) {

            if (resultCode == RESULT_OK && data != null) {
                if (data.getSerializableExtra("data").equals("")) {

                    ePaymentBy = "Passenger";


                    if (data.getBooleanExtra("isWallet", false)) {
                        eWalletDebitAllow = "Yes";
                        iswalletShow = false;
                    } else {
                        iswalletShow = true;
                        eWalletDebitAllow = "No";
                    }
                    if (cabSelectionFrag != null) {
                        cabSelectionFrag.estimateFare(cabSelectionFrag.distance, cabSelectionFrag.time);
                    }
                    if (cabSelectionFrag != null) {
                        cabSelectionFrag.setOrganizationName(generalFunc.retrieveLangLBl("", "LBL_PERSONAL"), false);
                        // cabSelectionFrag.setPaymentType(isCashSelected ? "Cash" : "Card");
                        vProfileName = generalFunc.retrieveLangLBl("", "LBL_PERSONAL");


                    }
                    selectPos = data.getIntExtra("selectPos", 0);
                    vReasonName = data.getStringExtra("vReasonName");
                    selectReasonId = data.getStringExtra("iTripReasonId");
                    vReasonTitle = data.getStringExtra("vReasonTitle");
                    vProfileName = data.getStringExtra("vProfileName");
                    vImage = "Personal";

                } else {
                    HashMap<String, String> map = (HashMap<String, String>) data.getSerializableExtra("data");

                    ePaymentBy = map.get("ePaymentBy");
                    vImage = map.get("vImage");
                    iswalletShow = false;


                    selectReasonId = data.getStringExtra("iTripReasonId");
                    vReasonTitle = data.getStringExtra("vReasonTitle");
                    selectPos = data.getIntExtra("selectPos", 0);

                    if (!ePaymentBy.equalsIgnoreCase("Organization")) {

                        if (data.getBooleanExtra("isWallet", false)) {
                            eWalletDebitAllow = "Yes";
                            iswalletShow = false;
                        } else {
                            iswalletShow = true;
                            eWalletDebitAllow = "No";
                        }

                        //  cabSelectionFrag.setPaymentType(isCashSelected ? "Cash" : "Card");
                        if (cabSelectionFrag != null) {
                            cabSelectionFrag.setOrganizationName(map.get("vShortProfileName"), false);
                        }

                        vProfileName = map.get("vProfileName");
                        vReasonName = data.getStringExtra("vReasonName");

                    } else {
                        if (cabSelectionFrag != null) {
                            cabSelectionFrag.setOrganizationName(map.get("vShortProfileName"), true);
                        }
                        vProfileName = map.get("vProfileName");
                        vReasonName = data.getStringExtra("vReasonName");
                        eWalletDebitAllow = "No";
                        iswalletShow = false;
                    }


                }

            }
        } else if (data != null && requestCode == Utils.FILTER_REQ_CODE && resultCode == RESULT_OK) {
            if (data.getStringExtra("SelectedVehicleTypeId").equalsIgnoreCase(selectedCabTypeId)) {
                selectedCabTypeId = data.getStringExtra("SelectedVehicleTypeId");
                loadAvailCabs.changeCabs();
            } else {
                selectedCabTypeId = data.getStringExtra("SelectedVehicleTypeId");
                loadAvailCabs.changeCabs();
                if (loadAvailCabs != null) {
                    loadAvailCabs.checkAvailableCabs();
                }
            }

        } else if (requestCode == Utils.ADD_HOME_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {

            String Latitude = data.getStringExtra("Latitude");
            String Longitude = data.getStringExtra("Longitude");
            String Address = data.getStringExtra("Address");

            Intent data1 = new Intent();
            data1.putExtra("Address", Address);
            data1.putExtra("Latitude", Latitude);
            data1.putExtra("Longitude", Longitude);

            generalFunc.storeData("userHomeLocationLatitude", "" + Latitude);
            generalFunc.storeData("userHomeLocationLongitude", "" + Longitude);
            generalFunc.storeData("userHomeLocationAddress", "" + Address);

            setDestinationPoint(Latitude, Longitude, Address, true);

            if (eFly) {
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }


        } else if (requestCode == Utils.ADD_WORK_LOC_REQ_CODE && resultCode == RESULT_OK && data != null) {
            String Latitude = data.getStringExtra("Latitude");
            String Longitude = data.getStringExtra("Longitude");
            String Address = data.getStringExtra("Address");

            Intent data1 = new Intent();
            data1.putExtra("Address", Address);
            data1.putExtra("Latitude", Latitude);
            data1.putExtra("Longitude", Longitude);


            generalFunc.storeData("userWorkLocationLatitude", "" + Latitude);
            generalFunc.storeData("userWorkLocationLongitude", "" + Longitude);
            generalFunc.storeData("userWorkLocationAddress", "" + Address);


            setDestinationPoint(Latitude, Longitude, Address, true);
            if (eFly) {
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }
        } else if (requestCode == 77 && resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("isRental", false)) {
                if (cabSelectionFrag != null) {
                    cabSelectionFrag.onActivityResult(requestCode, resultCode, data);
                }
                return;
            }
            if (data.getBooleanExtra("isRideNow", false)) {
                continuePickUpProcess();
            } else if (data.getBooleanExtra("isDeliverNow", false)) {
                if (cabSelectionFrag != null) {
                    cabSelectionFrag.onActivityResult(requestCode, resultCode, data);
                }
                return;

            } else {
                setRideSchedule();
            }

        } else if (requestCode == Utils.SEARCH_DEST_LOC_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                mainHeaderFrag.onActivityResult(requestCode, resultCode, data);
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    if (data.getBooleanExtra("isWhereTo", false)) {
                        finish();
                    }
                }
            }
        }
    }

    public void openPrefrancedailog() {


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

        LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.activity_prefrance, null);

        final MTextView TitleTxt = (MTextView) dialogView.findViewById(R.id.TitleTxt);
        final MTextView noteText = (MTextView) dialogView.findViewById(R.id.noteText);
        View noteview = (View) dialogView.findViewById(R.id.noteview);
        View female_view = (View) dialogView.findViewById(R.id.femal_view);
        View handicap_view = (View) dialogView.findViewById(R.id.handicap_view);
        View checkboxWheelChair_view = (View) dialogView.findViewById(R.id.checkboxWheelChair_view);
        noteText.setText(generalFunc.retrieveLangLBl("", "LBL_NOTE") + ": " + generalFunc.retrieveLangLBl("", "LBL_OPTION_FOR_FEMALE_USERS"));
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans_SemiBold.ttf");
        final CheckBox checkboxHandicap = (CheckBox) dialogView.findViewById(R.id.checkboxHandicap);
        checkboxHandicap.setTypeface(tf, Typeface.NORMAL);
        final CheckBox checkboxChildseat = (CheckBox) dialogView.findViewById(R.id.checkboxChildseat);
        checkboxChildseat.setTypeface(tf, Typeface.NORMAL);
        final CheckBox checkboxWheelChair = (CheckBox) dialogView.findViewById(R.id.checkboxWheelChair);
        checkboxWheelChair.setTypeface(tf, Typeface.NORMAL);
        final CheckBox checkboxFemale = (CheckBox) dialogView.findViewById(R.id.checkboxFemale);
        checkboxFemale.setTypeface(tf, Typeface.NORMAL);
        final ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelImg);
        checkboxFemale.setOnCheckedChangeListener((compoundButton, b) -> {

            if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes") && generalFunc.getJsonValueStr("eGender", obj_userProfile).equals("") && generalFunc.retrieveValue("IS_RIDE_MODULE_AVAIL").equalsIgnoreCase("yes")) {
                checkboxFemale.setChecked(false);
                genderDailog();
                return;
            }

        });

        cancelImg.setOnClickListener(v -> pref_dialog.dismiss());


        if (generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("yes")) {
            checkboxHandicap.setVisibility(View.VISIBLE);
            handicap_view.setVisibility(View.VISIBLE);
        } else {
            checkboxHandicap.setVisibility(View.GONE);
            handicap_view.setVisibility(View.GONE);
        }
        if (generalFunc.retrieveValue(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION).equalsIgnoreCase("yes")) {
            checkboxChildseat.setVisibility(View.VISIBLE);
        } else {
            checkboxChildseat.setVisibility(View.GONE);
        }
        if (generalFunc.retrieveValue(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION).equalsIgnoreCase("yes")) {
            checkboxWheelChair.setVisibility(View.VISIBLE);
            checkboxWheelChair_view.setVisibility(View.VISIBLE);
        } else {
            checkboxWheelChair.setVisibility(View.GONE);
            checkboxWheelChair_view.setVisibility(View.GONE);
        }

        if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("yes") && generalFunc.retrieveValue("IS_RIDE_MODULE_AVAIL").equalsIgnoreCase("yes")) {
            if (!generalFunc.getJsonValueStr("eGender", obj_userProfile).equalsIgnoreCase("") && !generalFunc.getJsonValueStr("eGender", obj_userProfile).equalsIgnoreCase("Male")) {
                checkboxFemale.setVisibility(View.VISIBLE);
                female_view.setVisibility(View.VISIBLE);

                noteText.setVisibility(View.GONE);
                noteview.setVisibility(View.GONE);

            } else if (generalFunc.getJsonValueStr("eGender", obj_userProfile).equalsIgnoreCase("")) {
                checkboxFemale.setVisibility(View.VISIBLE);
                female_view.setVisibility(View.VISIBLE);
            } else {
                checkboxFemale.setVisibility(View.GONE);
                female_view.setVisibility(View.GONE);
                noteText.setVisibility(View.GONE);
                noteview.setVisibility(View.GONE);
            }
        } else {
            checkboxFemale.setVisibility(View.GONE);
            female_view.setVisibility(View.GONE);
            noteText.setVisibility(View.GONE);
            noteview.setVisibility(View.GONE);
        }
        if (isfemale) {
            checkboxFemale.setChecked(true);
        }

        if (ishandicap) {
            checkboxHandicap.setChecked(true);
        }
        if (isChildSeat) {
            checkboxChildseat.setChecked(true);
        }
        if (isWheelChair) {
            checkboxWheelChair.setChecked(true);
        }
        MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
        int submitBtnId = Utils.generateViewId();
        btn_type2.setId(submitBtnId);
        btn_type2.setText(generalFunc.retrieveLangLBl("Update", "LBL_UPDATE"));
        btn_type2.setOnClickListener(v -> {
            pref_dialog.dismiss();
            isfemale = checkboxFemale.isChecked();
            ishandicap = checkboxHandicap.isChecked();
            isChildSeat = checkboxChildseat.isChecked();
            isWheelChair = checkboxWheelChair.isChecked();

            if (loadAvailCabs != null) {
                loadAvailCabs.changeCabs();
            }

        });


        builder.setView(dialogView);
        TitleTxt.setText(generalFunc.retrieveLangLBl("Prefrance", "LBL_PREFRANCE_TXT"));
        checkboxHandicap.setText(generalFunc.retrieveLangLBl("Filter handicap accessibility drivers only", "LBL_MUST_HAVE_HANDICAP_ASS_CAR"));
        checkboxFemale.setText(generalFunc.retrieveLangLBl("Accept Female Only trip request", "LBL_ACCEPT_FEMALE_REQ_ONLY_PASSENGER"));
        checkboxChildseat.setText(generalFunc.retrieveLangLBl("", "LBL_MUST_HAVE_CHILD_SEAT_ASS_CAR"));
        checkboxWheelChair.setText(generalFunc.retrieveLangLBl("", "LBL_MUST_HAVE_WHEEL_CHAIR_ASS_CAR"));


        pref_dialog = builder.create();
        pref_dialog.setCancelable(false);
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(pref_dialog);
        }
        pref_dialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
        pref_dialog.show();

    }

    public void getTollcostValue(final String driverIds, final String cabRequestedJson, final Intent data) {

        if (isFixFare && !isMultiDelivery()) {
            setDeliverOrRideReq(driverIds, cabRequestedJson, data);
            return;
        }


        if (cabSelectionFrag != null && !isMultiDelivery()) {
            if (cabSelectionFrag.isSkip) {
                setDeliverOrRideReq(driverIds, cabRequestedJson, data);
                return;
            }
        }

        // Toll Disabled for MultiDelivery

        if (generalFunc.retrieveValue(Utils.ENABLE_TOLL_COST).equalsIgnoreCase("Yes") && !isMultiDelivery() && !eFly) {

            String wayPoints = "";


            String MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY = generalFunc.retrieveValue(Utils.MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY);
            if (isMultiDelivery() && Utils.checkText(MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY)) {

                Gson gson = new Gson();
                String data1 = MUTLI_DELIVERY_LIST_JSON_DETAILS_KEY;


            } else {
                wayPoints = "&waypoint1=" + getDestLocLatitude() + "," + getDestLocLongitude();
            }
            String vCurrencyPassenger = generalFunc.getJsonValueStr("vCurrencyPassenger", obj_userProfile);
            String url = CommonUtilities.TOLLURL + generalFunc.getJsonValue("TOLL_COST_API_KEY", obj_userProfile) + "&waypoint0=" + getPickUpLocation().getLatitude() + "," + getPickUpLocation().getLongitude() + wayPoints + "&mode=fastest;car&tollVehicleType=car" + "&currency=" + vCurrencyPassenger.toUpperCase(Locale.ENGLISH);

            ApiHandler.execute(getActContext(), url, true, true, generalFunc, responseString -> {


                if (responseString != null && !responseString.equals("")) {

                    String response = generalFunc.getJsonValue("response", responseString);
                    JSONArray route = generalFunc.getJsonArray("route", response);
                    JSONObject routeObj = generalFunc.getJsonObject(route, 0);
                    JSONObject tollCostMain = generalFunc.getJsonObject("tollCost", routeObj);

                    if (generalFunc.getJsonValueStr("onError", tollCostMain).equalsIgnoreCase("FALSE")) {
                        try {

                            JSONObject costs = generalFunc.getJsonObject("cost", routeObj);
                            String currency = generalFunc.getJsonValueStr("currency", costs);
                            JSONObject details = generalFunc.getJsonObject("details", costs);
                            String tollCost = generalFunc.getJsonValueStr("tollCost", details);
                            if (currency != null && !currency.equals("")) {
                                tollcurrancy = currency;
                            }
                            tollamount = 0.0;
                            if (tollCost != null && !tollCost.equals("") && !tollCost.equals("0.0")) {
                                tollamount = GeneralFunctions.parseDoubleValue(0.0, tollCost);
                            }


                            TollTaxDialog(driverIds, cabRequestedJson, data);


                        } catch (Exception ignored) {

                            TollTaxDialog(driverIds, cabRequestedJson, data);
                        }

                    } else {
                        TollTaxDialog(driverIds, cabRequestedJson, data);
                    }


                } else {
                    generalFunc.showError();
                }

            });


        } else {
            setDeliverOrRideReq(driverIds, cabRequestedJson, data);
        }

    }

    public void setCancelable(Dialog dialogview, boolean cancelable) {
        final Dialog dialog = dialogview;
        View touchOutsideView = dialog.getWindow().getDecorView().findViewById(R.id.touch_outside);
        View bottomSheetView = dialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

        if (cancelable) {
            touchOutsideView.setOnClickListener(v -> {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            });
            BottomSheetBehavior.from(bottomSheetView).setHideable(true);
        } else {
            touchOutsideView.setOnClickListener(null);
            BottomSheetBehavior.from(bottomSheetView).setHideable(false);
        }
    }

    private void setDeliverOrRideReq(String driverIds, String cabRequestedJson, Intent data) {

        if (isDeliver(getCurrentCabGeneralType()) && isDeliver(app_type)) {
            // setDeliverySchedule();
        } else {

            if (app_type.equals(Utils.CabGeneralType_UberX)) {
                pickUpLocClicked();
            } else {

                if (getCabReqType().equals(Utils.CabReqType_Later)) {
                    isrideschedule = true;

                } else {
                    isreqnow = true;

                }
            }
        }


        if (data != null) {
            if (isdelivernow) {
                isdelivernow = false;
                deliverNow(data);
            } else if (isdeliverlater) {
                isdeliverlater = false;
                scheduleDelivery(data);
            }


        } else {
            if (isrideschedule) {
                isrideschedule = false;
                bookRide();
            } else if (isreqnow) {
                isreqnow = false;
                if (isTaxiBid) {
                    requestNearestCabTaxiBid = new RequestNearestCabTaxiBid(MainActivity.this);
                    requestNearestCabTaxiBid.run();
                } else {
                    requestPickUp();
                }
            }

        }
    }

    public void updateTaxiBidDriver(JSONObject obj_msg) {
        if (isTaxiBid && requestNearestCabTaxiBid != null) {
            requestNearestCabTaxiBid.updateDriverList(obj_msg);
        }
    }

    public void TollTaxDialog(final String driverIds, final String cabRequestedJson, final Intent data) {

        if (!isTollCostdilaogshow) {
            if (tollamount != 0.0 && tollamount != 0 && tollamount != 0.00) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActContext());

                LayoutInflater inflater = (LayoutInflater) getActContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.dialog_tolltax, null);

                final MTextView tolltaxTitle = (MTextView) dialogView.findViewById(R.id.tolltaxTitle);
                final MTextView tollTaxMsg = (MTextView) dialogView.findViewById(R.id.tollTaxMsg);
                final MTextView tollTaxpriceTxt = (MTextView) dialogView.findViewById(R.id.tollTaxpriceTxt);
                final MButton cancelTxt = ((MaterialRippleLayout) dialogView.findViewById(R.id.cancelTxt)).getChildView();

                final CheckBox checkboxTolltax = (CheckBox) dialogView.findViewById(R.id.checkboxTolltax);

                checkboxTolltax.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (checkboxTolltax.isChecked()) {
                        istollIgnore = true;
                    } else {
                        istollIgnore = false;
                    }

                });


                MButton btn_type2 = ((MaterialRippleLayout) dialogView.findViewById(R.id.btn_type2)).getChildView();
                int submitBtnId = Utils.generateViewId();
                btn_type2.setId(submitBtnId);
                btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_CONTINUE_BTN"));
                btn_type2.setOnClickListener(v -> {
                    tolltax_dialog.dismiss();
                    isTollCostdilaogshow = true;
                    setDeliverOrRideReq(driverIds, cabRequestedJson, data);
                });


                builder.setView(dialogView);
                tolltaxTitle.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_ROUTE"));
                tollTaxMsg.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_PRICE_DESC"));

                tollTaxMsg.setText(generalFunc.retrieveLangLBl("", "LBL_TOLL_PRICE_DESC"));

                String payAmount = payableAmount;
                if (isMultiDelivery() && data != null) {
                    payableAmount = generalFunc.convertNumberWithRTL(data.getStringExtra("totalFare"));
                } else if (cabSelectionFrag != null && cabTypeList != null && cabTypeList.size() > 0 && cabTypeList.get(cabSelectionFrag.selpos).get("total_fare") != null && !cabTypeList.get(cabSelectionFrag.selpos).get("total_fare").equals("") && !cabTypeList.get(cabSelectionFrag.selpos).get("eRental").equals("Yes")) {

                    try {
                        payAmount = generalFunc.convertNumberWithRTL(cabTypeList.get(cabSelectionFrag.selpos).get("total_fare"));
                    } catch (Exception ignored) {

                    }


                }

                if (payAmount.equalsIgnoreCase("")) {
                    tollTaxpriceTxt.setText(generalFunc.retrieveLangLBl("Total toll price", "LBL_TOLL_PRICE_TOTAL") + ": " + generalFunc.formatNumAsPerCurrency(generalFunc, generalFunc.convertNumberWithRTL(tollamount + ""), tollcurrancy, true));

                } else {
                    tollTaxpriceTxt.setText(generalFunc.retrieveLangLBl("Current Fare", "LBL_CURRENT_FARE") + ": " + payAmount + "\n" + "+" + "\n" + generalFunc.retrieveLangLBl("Total toll price", "LBL_TOLL_PRICE_TOTAL") + ": " + generalFunc.formatNumAsPerCurrency(generalFunc, generalFunc.convertNumberWithRTL(tollamount + ""), tollcurrancy, true));
                }

                checkboxTolltax.setText(generalFunc.retrieveLangLBl("", "LBL_IGNORE_TOLL_ROUTE"));
                cancelTxt.setText(generalFunc.retrieveLangLBl("", "LBL_CANCEL_TXT"));

                cancelTxt.setOnClickListener(v -> {
                    tolltax_dialog.dismiss();
                    isreqnow = false;
                });


                tolltax_dialog = builder.create();
                if (generalFunc.isRTLmode() == true) {
                    generalFunc.forceRTLIfSupported(tolltax_dialog);
                }
                tolltax_dialog.setCancelable(false);

                tolltax_dialog.getWindow().setBackgroundDrawable(getActContext().getResources().getDrawable(R.drawable.all_roundcurve_card));
                tolltax_dialog.show();
            } else {
                setDeliverOrRideReq(driverIds, cabRequestedJson, data);
            }
        } else {
            setDeliverOrRideReq(driverIds, cabRequestedJson, data);

        }
    }

    public void callgederApi(String egender) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("type", "updateUserGender");
        parameters.put("UserType", Utils.userType);
        parameters.put("iMemberId", generalFunc.getMemberId());
        parameters.put("eGender", egender);


        ApiHandler.execute(getActContext(), parameters, true, false, generalFunc, responseString -> {

            boolean isDataAvail = GeneralFunctions.checkDataAvail(Utils.action_str, responseString);


            String message = generalFunc.getJsonValue(Utils.message_str, responseString);
            if (isDataAvail) {
                generalFunc.storeData(Utils.USER_PROFILE_JSON, message);
                getUserProfileJson();
                prefBtnImageView.performClick();
            }


        });
    }

    public void genderDailog() {
        closeDrawer();
        final Dialog builder = new Dialog(getActContext(), R.style.Theme_Dialog);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(R.layout.gender_view);
        builder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        final MTextView genderTitleTxt = (MTextView) builder.findViewById(R.id.genderTitleTxt);
        final MTextView maleTxt = (MTextView) builder.findViewById(R.id.maleTxt);
        final MTextView femaleTxt = (MTextView) builder.findViewById(R.id.femaleTxt);
        final ImageView gendercancel = (ImageView) builder.findViewById(R.id.gendercancel);
        final ImageView gendermale = (ImageView) builder.findViewById(R.id.gendermale);
        final ImageView genderfemale = (ImageView) builder.findViewById(R.id.genderfemale);
        final LinearLayout male_area = (LinearLayout) builder.findViewById(R.id.male_area);
        final LinearLayout female_area = (LinearLayout) builder.findViewById(R.id.female_area);


        if (generalFunc.isRTLmode()) {
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.gravity = Gravity.START;
            gendercancel.setLayoutParams(params1);
        }

        genderTitleTxt.setText(generalFunc.retrieveLangLBl("Select your gender to continue", "LBL_SELECT_GENDER"));
        maleTxt.setText(generalFunc.retrieveLangLBl("Male", "LBL_MALE_TXT"));
        femaleTxt.setText(generalFunc.retrieveLangLBl("FeMale", "LBL_FEMALE_TXT"));

        gendercancel.setOnClickListener(v -> builder.dismiss());

        male_area.setOnClickListener(v -> {
            if (pref_dialog != null) {
                pref_dialog.dismiss();
            }

            callgederApi("Male");
            builder.dismiss();

        });
        female_area.setOnClickListener(v -> {

            if (pref_dialog != null) {
                pref_dialog.dismiss();
            }
            callgederApi("Female");
            builder.dismiss();

        });

        builder.show();

    }


    public void homeClick() {
        Bundle bn = new Bundle();
        HashMap<String, String> data = new HashMap<>();
        data.put("userHomeLocationAddress", "");
        data.put("userHomeLocationLatitude", "");
        data.put("userHomeLocationLongitude", "");
        data = GeneralFunctions.retrieveValue(data, getActContext());

        final String home_address_str = data.get("userHomeLocationAddress");
        final String home_addr_latitude = data.get("userHomeLocationLatitude");
        final String home_addr_longitude = data.get("userHomeLocationLongitude");

        Intent data1 = new Intent();
        data1.putExtra("Address", home_address_str);
        data1.putExtra("Latitude", home_addr_latitude);
        data1.putExtra("Longitude", home_addr_longitude);
        if (home_address_str != null && !home_address_str.equalsIgnoreCase("")) {

            setDestinationPoint(home_addr_latitude, home_addr_longitude, home_address_str, true);

            if (eFly) {
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }

        } else {
            if (intCheck.isNetworkConnected()) {
                bn.putString("isHome", "true");
                new ActUtils(getActContext()).startActForResult(SearchPickupLocationActivity.class, bn, Utils.ADD_HOME_LOC_REQ_CODE);
            } else {
                generalFunc.showMessage(btn_type_ridelater, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
            }
        }

    }

    public void workClick() {
        Bundle bndl = new Bundle();

        HashMap<String, String> data = new HashMap<>();
        data.put("userWorkLocationAddress", "");
        data.put("userWorkLocationLatitude", "");
        data.put("userWorkLocationLongitude", "");
        data = generalFunc.retrieveValue(data);


        String work_address_str = data.get("userWorkLocationAddress");
        String work_addr_latitude = data.get("userWorkLocationLatitude");
        String work_addr_longitude = data.get("userWorkLocationLongitude");

        Intent data1 = new Intent();
        data1.putExtra("Address", work_address_str);
        data1.putExtra("Latitude", work_addr_latitude);
        data1.putExtra("Longitude", work_addr_longitude);

        if (work_address_str != null && !work_address_str.equalsIgnoreCase("")) {

            setDestinationPoint(work_addr_latitude, work_addr_longitude, work_address_str, true);
            if (eFly) {
                showSelectionDialog(data1, false);
            } else {
                if (cabSelectionFrag == null) {
                    addcabselectionfragment();
                }
            }
        } else {
            if (intCheck.isNetworkConnected()) {
                bndl.putString("isWork", "true");
                if (getIntent().hasExtra("isFromMulti")) {
                    bndl.putBoolean("isFromMulti", true);
                    bndl.putInt("pos", getIntent().getIntExtra("pos", -1));
                }

                new ActUtils(getActContext()).startActForResult(SearchPickupLocationActivity.class, bndl, Utils.ADD_WORK_LOC_REQ_CODE);
            } else {
                generalFunc.showMessage(btn_type_ridelater, generalFunc.retrieveLangLBl("", "LBL_NO_INTERNET_TXT"));
            }
        }
    }

    private void PreferenceButtonEnable() {

        String currentCabType = getCurrentCabGeneralType();
        String HANDICAP_ACCESSIBILITY_OPTION = generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION);
        String FEMALE_RIDE_REQ_ENABLE = generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE);
        String CHILD_SEAT_ACCESSIBILITY_OPTION = generalFunc.retrieveValue(Utils.CHILD_SEAT_ACCESSIBILITY_OPTION);
        String WHEEL_CHAIR_ACCESSIBILITY_OPTION = generalFunc.retrieveValue(Utils.WHEEL_CHAIR_ACCESSIBILITY_OPTION);

        if ((!HANDICAP_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !FEMALE_RIDE_REQ_ENABLE.equalsIgnoreCase("YES") && !CHILD_SEAT_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !WHEEL_CHAIR_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES")) || (FEMALE_RIDE_REQ_ENABLE.equalsIgnoreCase("YES") && generalFunc.getJsonValueStr("eGender", obj_userProfile).equals("Male") && !HANDICAP_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !CHILD_SEAT_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES") && !WHEEL_CHAIR_ACCESSIBILITY_OPTION.equalsIgnoreCase("YES")) || ((currentCabType.equalsIgnoreCase(Utils.CabGeneralType_Deliver) || currentCabType.equalsIgnoreCase("Deliver") || currentCabType.equalsIgnoreCase(Utils.CabGeneralType_UberX)))) {
            prefBtnImageView.setVisibility(View.GONE);
            isPreferenceEnable = false;
            reSetButton(false);
        } else {
            prefBtnImageView.setVisibility(View.VISIBLE);
            isPreferenceEnable = true;


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) userLocBtnImgView.getLayoutParams();
            int rightMargin = (int) getResources().getDimension(R.dimen._56sdp);
            params.setMarginEnd(rightMargin);
            userLocBtnImgView.setLayoutParams(params);
            userLocBtnImgView.requestLayout();

        }

      /*  if (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("No") &&
                generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("No")) {
            prefBtnImageView.setVisibility(View.GONE);

        } else if (generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("No") &&
                !generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes")
                || (generalFunc.retrieveValue(Utils.FEMALE_RIDE_REQ_ENABLE).equalsIgnoreCase("Yes") &&
                generalFunc.getJsonValue("eGender", userProfileJson).equals("Male")
                && !generalFunc.retrieveValue(Utils.HANDICAP_ACCESSIBILITY_OPTION).equalsIgnoreCase("Yes"))) {
            prefBtnImageView.setVisibility(View.GONE);
        }*/
    }


    public void onClick(View view) {
        int i = view.getId();
        setCabReqType(Utils.CabReqType_Now);
        Utils.hideKeyboard(getActContext());
        if (i == userLocBtnImgView.getId()) {
            moveToCurrentLoc();

        } else if (i == userTripBtnImgView.getId()) {

            if (!isUserTripClick) {
                isUserTripClick = true;
                userTripBtnImgView.setColorFilter(getActContext().getResources().getColor(R.color.btnnavtripselcolor));
                if (driverAssignedHeaderFrag != null && driverAssignedHeaderFrag.tempdriverLocation_update != null) {
                    animateToLocation(driverAssignedHeaderFrag.tempdriverLocation_update.latitude, driverAssignedHeaderFrag.tempdriverLocation_update.longitude, Utils.defaultZomLevel);
                }
            } else {
                isUserTripClick = false;
                userTripBtnImgView.setColorFilter(getActContext().getResources().getColor(R.color.black));
            }
        } else if (i == emeTapImgView.getId()) {
            Bundle bn = new Bundle();
            bn.putString("UserProfileJson", obj_userProfile.toString());
            bn.putString("TripId", assignedTripId);
            new ActUtils(getActContext()).startActWithData(ConfirmEmergencyTapActivity.class, bn);
        } else if (i == rideArea.getId()) {
            ((ImageView) findViewById(R.id.rideImg)).setImageResource(R.mipmap.ride_on);
            rideImgViewsel.setVisibility(View.VISIBLE);
            ((MTextView) findViewById(R.id.selrideTxt)).setVisibility(View.VISIBLE);
            ((MTextView) findViewById(R.id.rideTxt)).setVisibility(View.GONE);
            rideImgView.setVisibility(View.GONE);
            deliverImgView.setVisibility(View.VISIBLE);
            deliverImgViewsel.setVisibility(View.GONE);
            otherImageView.setVisibility(View.VISIBLE);
            otherImageViewsel.setVisibility(View.GONE);

            ((ImageView) findViewById(R.id.deliverImg)).setImageResource(R.mipmap.delivery_off);
            ((MTextView) findViewById(R.id.rideTxt)).setTextColor(Color.parseColor("#000000"));
            ((MTextView) findViewById(R.id.deliverTxt)).setTextColor(Color.parseColor("#000000"));

            RideDeliveryType = Utils.CabGeneralType_Ride;

            if (mainHeaderFrag != null && generalFunc.isMultiDelivery() && app_type.equalsIgnoreCase("Ride-Delivery")) {
                mainHeaderFrag.setDestinationViewEnableOrDisabled(RideDeliveryType, false);
            }

            prefBtnImageView.setVisibility(View.VISIBLE);
            PreferenceButtonEnable();

            if (cabSelectionFrag != null) {
                cabSelectionFrag.changeCabGeneralType(Utils.CabGeneralType_Ride);
                cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Ride;

                if (cabSelectionFrag.cabTypeList != null) {
                    cabSelectionFrag.cabTypeList.clear();
                    cabSelectionFrag.adapter.notifyDataSetChanged();
                }
            }

            if (loadAvailCabs != null) {
                loadAvailCabs.checkAvailableCabs();
            }

        } else if (i == deliverArea.getId()) {

            rideImgViewsel.setVisibility(View.GONE);
            ((MTextView) findViewById(R.id.selrideTxt)).setVisibility(View.GONE);
            ((MTextView) findViewById(R.id.rideTxt)).setVisibility(View.VISIBLE);
            rideImgView.setVisibility(View.VISIBLE);
            deliverImgView.setVisibility(View.GONE);
            deliverImgViewsel.setVisibility(View.VISIBLE);
            otherImageView.setVisibility(View.VISIBLE);
            otherImageViewsel.setVisibility(View.GONE);

            ((ImageView) findViewById(R.id.rideImg)).setImageResource(R.mipmap.ride_off);
            ((ImageView) findViewById(R.id.deliverImg)).setImageResource(R.mipmap.delivery_on);

            ((MTextView) findViewById(R.id.rideTxt)).setTextColor(Color.parseColor("#000000"));

            ((MTextView) findViewById(R.id.deliverTxt)).setTextColor(Color.parseColor("#000000"));

            RideDeliveryType = Utils.CabGeneralType_Deliver;

            if (mainHeaderFrag != null && generalFunc.isMultiDelivery() && app_type.equalsIgnoreCase("Ride-Delivery")) {
                mainHeaderFrag.setDestinationViewEnableOrDisabled(RideDeliveryType, true);
            }


            isfemale = false;
            ishandicap = false;
            isChildSeat = false;
            isWheelChair = false;
            prefBtnImageView.setVisibility(View.GONE);

            if (cabSelectionFrag != null) {
                cabSelectionFrag.changeCabGeneralType(Utils.CabGeneralType_Deliver);
                cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_Deliver;

                if (cabSelectionFrag.cabTypeList != null) {
                    cabSelectionFrag.cabTypeList.clear();
                    cabSelectionFrag.adapter.notifyDataSetChanged();
                }
            }

            if (loadAvailCabs != null) {
                loadAvailCabs.checkAvailableCabs();
            }

        } else if (i == otherArea.getId()) {
            rideImgViewsel.setVisibility(View.GONE);
            ((MTextView) findViewById(R.id.selrideTxt)).setVisibility(View.GONE);
            ((MTextView) findViewById(R.id.rideTxt)).setVisibility(View.VISIBLE);
            rideImgView.setVisibility(View.VISIBLE);
            deliverImgView.setVisibility(View.VISIBLE);
            deliverImgViewsel.setVisibility(View.GONE);
            otherImageView.setVisibility(View.GONE);
            otherImageViewsel.setVisibility(View.VISIBLE);


            RideDeliveryType = Utils.CabGeneralType_UberX;
            if (cabSelectionFrag != null) {
                cabSelectionFrag.changeCabGeneralType(Utils.CabGeneralType_UberX);
                cabSelectionFrag.currentCabGeneralType = Utils.CabGeneralType_UberX;

            }
            Bundle bn = new Bundle();
            bn.putBoolean("isback", true);
            if (pickUpLocation != null) {
                bn.putString("lat", pickUpLocation.getLatitude() + "");
                bn.putString("long", pickUpLocation.getLongitude() + "");
                bn.putString("address", pickUpLocationAddress);
            }
            new ActUtils(getActContext()).startActForResult(UberXActivity.class, bn, Utils.OTHER_AREA_CLICKED_CODE);
        } else if (i == prefBtnImageView.getId()) {

            getUserProfileJson();
            openPrefrancedailog();
        } else if (i == backImgView.getId()) {
            onBackPressed();
        } else if (i == filterTxtView.getId()) {
            openFilterDilaog();
        } else if (i == workArea.getId()) {
            checkForSourceLocation(i);/*workClick();*/
        } else if (i == homeArea.getId()) {
            checkForSourceLocation(i);/*homeClick();*/
        } else if (i == destSelectTxt.getId()) {

            checkForSourceLocation(i);
                /*if (mainHeaderFrag != null) {
                    mainHeaderFrag.destarea.performClick();
                }*/
        } else if (i == recentArea.getId()) {
            checkForSourceLocation(i);
        } else if (i == scheduleArea.getId()) {
            chooseDateTime();
        } else if (i == edit_location.getId()) {
            Bundle bn = new Bundle();
            bn.putString("locationArea", "source");
            bn.putString("loc_pick_bar", "true");
            bn.putDouble("lat", GeneralFunctions.parseDoubleValue(0.0, pickup_latitude));
            bn.putDouble("long", GeneralFunctions.parseDoubleValue(0.0, pickup_longitude));
            bn.putString("address", pickup_address);
            new ActUtils(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.UBER_X_SEARCH_PICKUP_LOC_REQ_CODE);
        }
    }


    private void checkForSourceLocation(int viewID) {
        selectedViewID = viewID;
        mainHeaderFrag.isRecent = false;
        if (pickUpLocation != null) {
            if (viewID == workArea.getId()) {
                workClick();
            } else if (viewID == homeArea.getId()) {
                homeClick();
            } else if (viewID == destSelectTxt.getId()) {
                if (mainHeaderFrag != null) {
                    if (cabRquestType.equalsIgnoreCase(Utils.CabReqType_Later)) {
                        mainHeaderFrag.isSchedule = true;
                    } else {
                        mainHeaderFrag.isSchedule = false;
                    }
                    if (mainHeaderFrag.destarea != null) {
                        mainHeaderFrag.destarea.performClick();
                    }
                }
            } else if (viewID == recentArea.getId()) {
                if (mainHeaderFrag != null) {
                    mainHeaderFrag.isRecent = true;
                    if (mainHeaderFrag.destarea != null) {
                        mainHeaderFrag.destarea.performClick();
                    }
                }
            }
        } else {
            openSourceLocationView();
        }
    }

    boolean isFromSourceDialog = false;
    AppCompatDialog dialog;
    int selectedViewID;

    private void openSourceLocationView() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new AppCompatDialog(getActContext(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.no_source_location_design);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);

        ((RippleBackground) dialog.findViewById(R.id.rippleBgView)).startRippleAnimation();

        ImageView closeImage = dialog.findViewById(R.id.closeImage);
        closeImage.setOnClickListener(v -> {
            isFromSourceDialog = false;
            isFirstLocation = false;
            dialog.dismiss();
        });
        MTextView locationHintText = dialog.findViewById(R.id.locationHintText);
        MTextView locationDescText = dialog.findViewById(R.id.locationDescText);
        MTextView btnTxt = dialog.findViewById(R.id.btnTxt);
        ImageView btnImg = dialog.findViewById(R.id.btnImg);
        LinearLayout btnArea = dialog.findViewById(R.id.btnArea);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        RippleBackground.LayoutParams buttonLayoutParams = new RippleBackground.LayoutParams(RippleBackground.LayoutParams.MATCH_PARENT, RippleBackground.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.setMargins(0, 0, 0, -(height / 2));
        ((RippleBackground) dialog.findViewById(R.id.rippleBgView)).setLayoutParams(buttonLayoutParams);


        if (generalFunc.isRTLmode()) {
            btnImg.setRotation(180);
            btnArea.setBackground(getActContext().getResources().getDrawable(R.drawable.login_border_rtl));
        }

        btnTxt.setText(generalFunc.retrieveLangLBl("ENTER", "LBL_ADD_ADDRESS_TXT"));
        locationDescText.setText(generalFunc.retrieveLangLBl("Please wait while we are trying to access your location. meanwhile you can enter your source location.", "LBL_FETCHING_LOCATION_NOTE_TEXT"));
        locationHintText.setText(generalFunc.retrieveLangLBl("Location", "LBL_LOCATION_FOR_FRONT"));

        btnArea.setOnClickListener(v -> {
            dialog.dismiss();
            isFirstLocation = false;
            isFromSourceDialog = false;

            Bundle bn = new Bundle();
            bn.putString("locationArea", "source");
            bn.putDouble("lat", 0.0);
            bn.putDouble("long", 0.0);
            new ActUtils(getActContext()).startActForResult(SearchLocationActivity.class, bn, Utils.SEARCH_PICKUP_LOC_REQ_CODE);
        });

        isFirstLocation = true;
        isFromSourceDialog = true;

        //
        new Handler().postDelayed(this::checkLocation, 1500);
        dialog.show();
    }

    private ArrayList<HashMap<String, String>> populateArrayList() {
        ArrayList<HashMap<String, String>> mapArrayList = new ArrayList<>();

        ArrayList<String> sortby_List = new ArrayList<String>();
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_FEATURED_TXT"));
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_NEAR_BY_TXT"));
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_RATING"));
        if (generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY).equalsIgnoreCase("Yes")) {
            sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_FAV_DRIVERS_FILTER_TXT"));
        }
        sortby_List.add(generalFunc.retrieveLangLBl("", "LBL_AVAILABILITY"));
        for (int i = 0; i < sortby_List.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("vName", "" + sortby_List.get(i));
            map.put("selectedSortValue", "" + selectedSortValue);
            mapArrayList.add(map);
        }
        return mapArrayList;
    }

    public void openFilterDilaog() {


        String enablefvdriverkey = generalFunc.retrieveValue(Utils.ENABLE_FAVORITE_DRIVER_MODULE_KEY);
        OpenListView.getInstance(getActContext(), generalFunc.retrieveLangLBl("", "LBL_SORT_BY_TXT"), arrayList, OpenListView.OpenDirection.BOTTOM, true, position -> {

            filterTxtView.setText(arrayList.get(position).get("vName"));

            filterPosition = position;
            if (position == 0) {
                selectedSort = "eIsFeatured";
                selectedSortValue = arrayList.get(0).get("vName");
            } else if (position == 1) {
                selectedSort = "distance";
                selectedSortValue = arrayList.get(1).get("vName");
            } else if (position == 2) {
                selectedSort = "vAvgRating";
                selectedSortValue = arrayList.get(2).get("vName");
            } else if (position == 3 && enablefvdriverkey.equalsIgnoreCase("Yes")) {
                selectedSort = "eFavDriver";
                selectedSortValue = arrayList.get(3).get("vName");
            } else if ((position == 4 && enablefvdriverkey.equalsIgnoreCase("Yes")) || position == 3) {
                if (enablefvdriverkey.equalsIgnoreCase("Yes")) {
                    selectedSort = "IS_PROVIDER_ONLINE";
                    selectedSortValue = arrayList.get(4).get("vName");
                } else {
                    selectedSort = "IS_PROVIDER_ONLINE";
                    selectedSortValue = arrayList.get(3).get("vName");
                }
            }
            findViewById(R.id.driverListAreaLoader).setVisibility(View.VISIBLE);

            if (loadAvailCabs != null) {

                loadAvailCabs.sortby = selectedSort;
                loadAvailCabs.changeCabs();
                loadAvailCabs.checkAvailableCabs();
            }

        }).show(filterPosition, "vName");
    }

    private void moveToCurrentLoc() {
        if (!generalFunc.isLocationEnabled()) {
            generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("Please enable you GPS location service", "LBL_GPSENABLE_TXT"));
            return;
        }
        isCameraMove = false;
        manageUserLocBtn(isCameraMove);
        isUserLocbtnclik = true;

        if (cabSelectionFrag == null) {

            if (driverAssignedHeaderFrag != null) {
                if (driverAssignedHeaderFrag.sourceMarker != null) {
                    driverAssignedHeaderFrag.sourceMarker.remove();
                    driverAssignedHeaderFrag.sourceMarker = null;
                }

                if (driverAssignedHeaderFrag.destinationPointMarker_temp != null) {
                    driverAssignedHeaderFrag.destinationPointMarker_temp.remove();
                    driverAssignedHeaderFrag.destinationPointMarker_temp = null;
                }
            }

            if (isDriverAssigned && driverAssignedHeaderFrag != null) {
                //driver topickup
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                if (driverAssignedHeaderFrag.driverMarker != null) {
//                    driverAssignedHeaderFrag.driverMarker.setPosition(new LatLng(23.049329, 72.543652));
                    builder.include(driverAssignedHeaderFrag.driverMarker.getPosition());
                }
                if (driverAssignedHeaderFrag.time_marker != null) {
                    builder.include(driverAssignedHeaderFrag.time_marker.getPosition());
                } else {
//                    driverAssignedHeaderFrag.addPickupMarker();
                    if (driverAssignedHeaderFrag.sourceMarker != null && !isTripStarted) {
                        builder.include(driverAssignedHeaderFrag.sourceMarker.getPosition());
                    }

                    if (driverAssignedHeaderFrag.destLocation != null && isTripStarted) {
                        builder.include(driverAssignedHeaderFrag.destLocation);
                    }
                }

                getMap().resetMinMaxZoomPreference();

                LatLngBounds bounds = builder.build();

                float maxZoomLevel = getMap().getMaxZoomLevel();
                getMap().setMaxZoomPreference(maxZoomLevel - 5);
                getMap().animateCamera(bounds, 0, new CancelableCallback() {
                    @Override
                    public void onCancel() {
                        getMap().setMaxZoomPreference(maxZoomLevel);
                    }

                    @Override
                    public void onFinish() {
                        getMap().setMaxZoomPreference(maxZoomLevel);
                    }
                });

            } else {
                try {
                    View detailsArea = findViewById(R.id.DetailsArea);
                    if (flyPortsLocList.size() > 0 && detailsArea != null && detailsArea.getVisibility() == View.VISIBLE) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng tmpLoc : flyPortsLocList) {
                            builder.include(tmpLoc);
                        }

                        getMap().animateCamera(builder.build(), Utils.dipToPixels(getActContext(), 10));

                        return;
                    }

                    LatLng cameraPosition = cameraForUserPosition();
                    if (cameraPosition != null) {

                        getMap().moveCamera(cameraPosition);

                        if (mainHeaderFrag != null && mainHeaderFrag.getAddressFromLocation != null && userLocation != null) {
                            mainHeaderFrag.getAddressFromLocation.setLocation(userLocation.getLatitude(), userLocation.getLongitude());
                            if (!pickup_address.equalsIgnoreCase("")) {
                                if (!eFly) {
                                    mainHeaderFrag.setPickUpAddress(pickup_address);
                                }
                            } else {
                                mainHeaderFrag.getAddressFromLocation.execute();
                            }
                        }
                    }
                } catch (Exception ignored) {

                }
            }


        } else if (cabSelectionFrag != null) {

            if (cabSelectionFrag.isSkip) {
                cabSelectionFrag.handleSourceMarker(timeval);
                return;
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (cabSelectionFrag.sourceMarker != null) {
                builder.include(cabSelectionFrag.sourceMarker.getPosition());
            }
            if (cabSelectionFrag.destDotMarker != null) {
                builder.include(cabSelectionFrag.destDotMarker.getPosition());
            }
            float maxZoomLevel = getMap().getMaxZoomLevel();
            if (cabSelectionFrag.sourceDotMarker != null && cabSelectionFrag.destDotMarker != null && getMap() != null) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                getMap().setMaxZoomPreference(maxZoomLevel);
                getMap().animateCamera(builder.build(), width - Utils.dipToPixels(getActContext(), 60), (metrics.heightPixels - Utils.dipToPixels(getActContext(), 15)), Utils.dipToPixels(getActContext(), 10));

            } else if (cabSelectionFrag.sourceDotMarker != null && getMap() != null) {
                if (isMultiDelivery()) {
                    /*DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int width = metrics.widthPixels;
                    getMap().setMaxZoomPreference(maxZoomLevel - 5);
                    getMap().animateCamera(builder.build(), width - Utils.dipToPixels(getActContext(), 60), (metrics.heightPixels - Utils.dipToPixels(getActContext(), 15)), Utils.dipToPixels(getActContext(), 10));*/
                    getMap().setMaxZoomPreference(maxZoomLevel - 5);
                    getMap().animateCamera(builder.build(), (int) Utils.getScreenPixelWidth(this) - Utils.dipToPixels(getActContext(), 60), ((int) Utils.getScreenPixelHeight(this) - Utils.dipToPixels(getActContext(), 15)), Utils.dipToPixels(getActContext(), 10));
                }
            }


        }


    }

    public class SendNotificationsToDriverByDist implements Runnable {

        String[] list_drivers_ids;
        String cabRequestedJson;

        int interval = GeneralFunctions.parseIntegerValue(30, generalFunc.getJsonValueStr("RIDER_REQUEST_ACCEPT_TIME", obj_userProfile));

        int mInterval = (interval + 5) * 1000;

        int current_position_driver_id = 0;
        private Handler mHandler_sendNotification;

        public SendNotificationsToDriverByDist(String list_drivers_ids, String cabRequestedJson) {
            if (!list_drivers_ids.isEmpty()){
                this.list_drivers_ids = list_drivers_ids.split(",");
                this.cabRequestedJson = cabRequestedJson;
                mHandler_sendNotification = new Handler();
            }

            startRepeatingTask();
        }

        @Override
        public void run() {
            if (current_position_driver_id == -1) {
                return;
            }
            setRetryReqBtn(false, false);

            if ((current_position_driver_id + 1) <= list_drivers_ids.length) {
                sendRequestToDrivers(list_drivers_ids[current_position_driver_id], cabRequestedJson);
                current_position_driver_id = current_position_driver_id + 1;
                mHandler_sendNotification.postDelayed(this, mInterval);
            } else {
                setRetryReqBtn(true, false);
                stopRepeatingTask();
            }

        }


        public void stopRepeatingTask() {
            mHandler_sendNotification.removeCallbacks(this);
            mHandler_sendNotification.removeCallbacksAndMessages(null);
            current_position_driver_id = -1;
        }

        public void incTask() {
            mHandler_sendNotification.removeCallbacks(this);
            mHandler_sendNotification.removeCallbacksAndMessages(null);
            if (current_position_driver_id != -1) {
                this.run();
            }
        }

        public void startRepeatingTask() {
            stopRepeatingTask();
            current_position_driver_id = 0;
            this.run();
        }
    }

    public boolean isMultiStopOverEnabled() {
        boolean isStopOverEnabled = generalFunc.retrieveValue(Utils.ENABLE_STOPOVER_POINT_KEY).equalsIgnoreCase("Yes") && getCurrentCabGeneralType().equalsIgnoreCase(Utils.CabGeneralType_Ride);
        boolean isRental = false;
        if (cabSelectionFrag != null) {
            if (!cabSelectionFrag.iRentalPackageId.equalsIgnoreCase("")) {
                isRental = true;
            }
        }
        return isStopOverEnabled && !isRental && !eFly;
    }
}