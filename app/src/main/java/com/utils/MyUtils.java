package com.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.fontanalyzer.SystemFont;
import com.general.files.ActUtils;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.tiktak24.user.MobileStegeActivity;
import com.tiktak24.user.R;
import com.view.MTextView;
import com.view.editBox.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyUtils {
    public static final int WEBVIEWPAYMENT = 7801;
    public static final int REFRESH_DATA_REQ_CODE = 7802;

    public static ArrayList<HashMap<String, String>> createArrayListJSONArray(@NonNull GeneralFunctions generalFunc, @NonNull ArrayList<HashMap<String, String>> hashMaps, @Nullable JSONArray dataArray) {
        if (dataArray != null && dataArray.length() > 0) {

            for (int i = 0; i < dataArray.length(); i++) {
                hashMaps.add(createHashMap(generalFunc, new HashMap<>(), generalFunc.getJsonObject(dataArray, i)));
            }
        }
        return hashMaps;
    }

    public static HashMap<String, String> createHashMap(@NonNull GeneralFunctions generalFunc, @NonNull HashMap<String, String> mapData, @NonNull JSONObject dataJObject) {
        Iterator<String> keysItr = dataJObject.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            String value = generalFunc.getJsonValueStr(key, dataJObject);

            mapData.put(key, value);
        }
        return mapData;
    }

    public static JSONObject createJsonObject(HashMap<String, String> mapData, JSONObject jsonObject) {
        for (int i = 0; i < mapData.size(); i++) {
            for (String mapKey : mapData.keySet()) {
                try {
                    jsonObject.put(mapKey, mapData.get(mapKey));
                } catch (JSONException e) {
                    Logger.e("Exception", "::" + e.getMessage());
                }
            }
        }
        return jsonObject;
    }

    public static Integer getNumOfColumns(Activity mActivity) {
        try {
            DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
            /*float dpWidth = (displayMetrics.widthPixels - Utils.dipToPixels(getActContext(), 10)) / displayMetrics.density;
            int margin_int_value = getActContext().getResources().getDimensionPixelSize(R.dimen._10sdp) * 2;
            int menuItem_int_value = getActContext().getResources().getDimensionPixelSize(R.dimen._5sdp) * 2;*/
            int margin_int_value = mActivity.getResources().getDimensionPixelSize(R.dimen._10sdp);
            int menuItem_int_value = mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp);
            int catWidth_int_value = mActivity.getResources().getDimensionPixelSize(R.dimen.category_grid_size_more);
            int screenWidth_int_value = displayMetrics.widthPixels - margin_int_value - menuItem_int_value;
            return (int) (screenWidth_int_value / catWidth_int_value);
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }
        return -1;
    }

    public static void manageAddressHeader(Activity mActivity, GeneralFunctions generalFunc, MTextView headerTxtView, int tintColor) {
        Drawable locationDrawable = AppCompatResources.getDrawable(mActivity, R.drawable.ic_place_address_fill);
        Drawable arrowDrawable = AppCompatResources.getDrawable(mActivity, R.drawable.ic_down_arrow_header);

        Drawable wLocDrawable = null;
        if (locationDrawable != null) {
            wLocDrawable = DrawableCompat.wrap(locationDrawable);
            DrawableCompat.setTint(wLocDrawable.mutate(), ContextCompat.getColor(mActivity, tintColor));
        }

        Drawable wArrowDrawable = null;
        if (arrowDrawable != null) {
            wArrowDrawable = DrawableCompat.wrap(arrowDrawable);
            DrawableCompat.setTint(wArrowDrawable.mutate(), ContextCompat.getColor(mActivity, tintColor));
        }

        if (generalFunc.isRTLmode()) {
            headerTxtView.setCompoundDrawablesWithIntrinsicBounds(wArrowDrawable, null, wLocDrawable, null);
        } else {
            headerTxtView.setCompoundDrawablesWithIntrinsicBounds(wLocDrawable, null, wArrowDrawable, null);
        }
    }

    @SuppressLint("InflateParams")
    public static View addFareDetailRow(Context context, GeneralFunctions generalFunc, String rName, String rValue, boolean isLast) {
        View convertView;
        if (rName.equalsIgnoreCase("eDisplaySeperator")) {
            convertView = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(context, 1));
            params.setMargins(0, Utils.dipToPixels(context, context.getResources().getDimensionPixelSize(R.dimen._3sdp)), 0, Utils.dipToPixels(context, context.getResources().getDimensionPixelSize(R.dimen._2sdp)));
            //params.setMarginStart(Utils.dipToPixels(getActContext(), 10));
            //params.setMarginEnd(Utils.dipToPixels(getActContext(), 10));
            convertView.setLayoutParams(params);
            convertView.setBackgroundColor(Color.parseColor("#DEDEDE"));
        } else {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.design_fare_breakdown_row, null);

            MTextView titleHTxt = convertView.findViewById(R.id.titleHTxt);
            MTextView titleVTxt = convertView.findViewById(R.id.titleVTxt);

            titleHTxt.setText(generalFunc.convertNumberWithRTL(rName));
            titleVTxt.setText(generalFunc.convertNumberWithRTL(rValue));

            if (isLast) {
                titleHTxt.setTextColor(context.getResources().getColor(R.color.text23Pro_Dark));
                titleHTxt.setTypeface(SystemFont.FontStyle.BOLD.font);
                titleHTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                titleVTxt.setTextColor(context.getResources().getColor(R.color.appThemeColor_1));
                titleVTxt.setTypeface(SystemFont.FontStyle.BOLD.font);
                titleVTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            }
        }
        return convertView;
    }

    public static void timeSlotRow(Context context, GeneralFunctions generalFunc, LinearLayout llView, ArrayList<HashMap<String, String>> slotsArray, boolean isDisplaySeparator) {
        for (int i = 0; i < slotsArray.size(); i++) {

            LayoutInflater itemCartInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (isDisplaySeparator && (i + 1) != slotsArray.size()) {
                View convertView = new View(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dipToPixels(context, 1));
                //params.setMarginStart(Utils.dipToPixels(getActContext(), 10));
                //params.setMarginEnd(Utils.dipToPixels(getActContext(), 10));
                convertView.setLayoutParams(params);
                convertView.setBackgroundColor(Color.parseColor("#DEDEDE"));
                llView.addView(convertView);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void editBoxMultiLine(MaterialEditText editText) {
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        editText.setOnTouchListener((v, event) -> {
            if (editText.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
            return false;
        });
        editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NONE);
        editText.setSingleLine(false);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setGravity(Gravity.TOP);
    }

    public static void setBounceAnimation(@NonNull Context context, @NonNull View view, int animView, @Nullable BounceAnimListener bounceAnimListener) {
        Animation anim = AnimationUtils.loadAnimation(context, animView);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (bounceAnimListener != null) {
                    bounceAnimListener.onAnimationFinished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public interface BounceAnimListener {
        void onAnimationFinished();
    }

    public static void openMobileStageActivity(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "login");
        new ActUtils(context).startActWithData(MobileStegeActivity.class, bundle);
    }

    public static void getLanguageLabelServiceWise(@NonNull GeneralFunctions generalFunc, @NonNull String iServiceId) {
        generalFunc.storeData(Utils.iServiceId_KEY, iServiceId);
        if (generalFunc.retrieveValue("LanguagesForAllServiceType") != null) {
            String LBLresponseString = generalFunc.retrieveValue("LanguagesForAllServiceType");
            if (LBLresponseString != null && !LBLresponseString.equalsIgnoreCase("")) {
                JSONArray messageArray = generalFunc.getJsonArray(Utils.message_str, LBLresponseString);
                if (messageArray != null) {
                    for (int i = 0; i < messageArray.length(); i++) {
                        JSONObject obj_temp = generalFunc.getJsonObject(messageArray, i);

                        if (iServiceId.equalsIgnoreCase(generalFunc.getJsonValueStr("iServiceId", obj_temp))) {

                            if (generalFunc.retrieveValue(Utils.LANGUAGE_CODE_KEY).equalsIgnoreCase(generalFunc.getJsonValue("vCode", LBLresponseString))) {

                                generalFunc.storeData(Utils.languageLabelsKey, generalFunc.getJsonValueStr("dataDic", obj_temp));
                                generalFunc.storeData(Utils.LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vCode", LBLresponseString));
                                generalFunc.storeData(Utils.LANGUAGE_IS_RTL_KEY, generalFunc.getJsonValue("eType", LBLresponseString));

                                generalFunc.storeData(Utils.GOOGLE_MAP_LANGUAGE_CODE_KEY, generalFunc.getJsonValue("vGMapLangCode", LBLresponseString));
                                GeneralFunctions.clearAndResetLanguageLabelsData(MyApp.getInstance().getApplicationContext());
                                Utils.setAppLocal(MyApp.getInstance().getCurrentAct());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}