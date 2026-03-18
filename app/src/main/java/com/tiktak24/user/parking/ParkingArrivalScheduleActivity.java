package com.tiktak24.user.parking;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.activity.ParentActivity;
import com.general.DatePicker;
import com.general.files.ActUtils;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.ActivityParkingArrivalScheduleBinding;
import com.tiktak24.user.parking.adapter.ParkingTimeSlotAdapter;
import com.utils.CommonUtilities;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ParkingArrivalScheduleActivity extends ParentActivity implements ParkingTimeSlotAdapter.setRecentTimeSlotClickList {
    public ActivityParkingArrivalScheduleBinding binding;
    private final Calendar dateTimeCalender = Calendar.getInstance(Locale.getDefault());
    private final Calendar maxDateCalender = Calendar.getInstance(Locale.getDefault());
    private ParkingTimeSlotAdapter timeSlotAdapter;
    ArrayList<HashMap<String, String>> timeSlotList = new ArrayList<>();
    ArrayList<String> selectedPos = new ArrayList<>();
    public Toolbar mToolbar;
    String parkingId = "";
    MButton btn_type2;

    private String durationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parking_arrival_schedule);
        setData();
    }

    private void setData() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ImageView backImgView = findViewById(R.id.backImgView);
        if (generalFunc.isRTLmode()) {
            backImgView.setRotation(180);
        }
        MTextView titleTxt = findViewById(R.id.titleTxt);
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PARKING_CHOOSE_ARRIVAL_INFO_TITLE"));
        addToClickHandler(backImgView);
        binding.selectArrivalHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_PARKING_SELECT_ARRIVAL_DATE_TIME_TITLE"));
        binding.selectDurationHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_SELECT_DURATION"));
        addToClickHandler(binding.dateTimeEditBox);
        binding.dateTimeEditBox.setHint(generalFunc.retrieveLangLBl("", "LBL_SELECT_DATE_TIME_HINT"));
        btn_type2 = ((MaterialRippleLayout) binding.btnType2).getChildView();
        btn_type2.setText(generalFunc.retrieveLangLBl("", "LBL_NEXT"));
        btn_type2.setId(Utils.generateViewId());
        addToClickHandler(btn_type2);
        maxDateCalender.set(Calendar.MONTH, dateTimeCalender.get(Calendar.MONTH) + 2);
        timeSlotAdapter = new ParkingTimeSlotAdapter(this, timeSlotList, selectedPos);
        binding.timeSlotsRV.setLayoutManager(new GridLayoutManager(this, 3));
        timeSlotAdapter.setOnClickList(this);
        binding.timeSlotsRV.setAdapter(timeSlotAdapter);
        getDurationData();
    }

    private void getDurationData() {
        String response = getIntent().getStringExtra("Duration");
        parkingId = getIntent().getStringExtra("parkingId");
        JSONArray duration_data = generalFunc.getJsonArray("Duration", response);
        if (duration_data != null) {
            for (int i = 0; i < duration_data.length(); i++) {
                JSONObject obj_tmp = generalFunc.getJsonObject(duration_data, i);
                HashMap<String, String> mapdata = new HashMap<>();
                mapdata.put("name", generalFunc.getJsonValueStr("tDuration", obj_tmp));
                mapdata.put("iDurationId", generalFunc.getJsonValueStr("iDurationId", obj_tmp));
                timeSlotList.add(mapdata);

            }
            timeSlotAdapter.notifyDataSetChanged();
        }

    }


    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.backImgView) {
            onBackPressed();
        } else if (i == binding.dateTimeEditBox.getId()) {
            DatePicker.show(this, generalFunc, Calendar.getInstance(), maxDateCalender, Utils.convertDateToFormat(CommonUtilities.DayFormatEN, dateTimeCalender.getTime()), null, (year, monthOfYear, dayOfMonth) -> {

                dateTimeCalender.set(Calendar.YEAR, year);
                dateTimeCalender.set(Calendar.MONTH, monthOfYear - 1);
                dateTimeCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                new TimePickerDialog(this, R.style.calender_dialog_theme, (timePicker, selectedHour, selectedMinute) -> {

                    dateTimeCalender.set(Calendar.HOUR_OF_DAY, selectedHour);
                    dateTimeCalender.set(Calendar.MINUTE, selectedMinute);
                    dateTimeCalender.set(Calendar.SECOND, 0);
                    dateTimeCalender.set(Calendar.MILLISECOND, 0);
                    if (Calendar.getInstance().getTimeInMillis() <= dateTimeCalender.getTimeInMillis()) {
                        binding.dateTimeEditBox.setText(Utils.convertDateToFormat(CommonUtilities.serverDateTimeFormat, dateTimeCalender.getTime()));

                    } else {
                        generalFunc.showMessage(binding.selectArrivalHTxt, generalFunc.retrieveLangLBl("", "LBL_RIDE_SHARE_INVALID_PUBLISH_TIME_MSG"));

                    }

                }, dateTimeCalender.get(Calendar.HOUR_OF_DAY), dateTimeCalender.get(Calendar.MINUTE), false).show();
            });
        } else if (i == btn_type2.getId()) {

            if (Utils.checkText(binding.dateTimeEditBox.getText().toString()) && Utils.checkText(durationId)) {
                Bundle bn = new Bundle();
                bn.putString("parkingId", parkingId);
                bn.putString("duration", durationId);
                bn.putString("bookingAddress", getIntent().getStringExtra("bookingAddress"));
                bn.putString("bookingLatitude", getIntent().getStringExtra("bookingLatitude"));
                bn.putString("bookingLongitude", getIntent().getStringExtra("bookingLongitude"));
                bn.putString("dateTime", binding.dateTimeEditBox.getText().toString());
                bn.putSerializable("vehicleSizes", getIntent().getSerializableExtra("vehicleSizes"));
                new ActUtils(ParkingArrivalScheduleActivity.this).startActWithData(AvailableParkingSpacesActivity.class, bn);
            } else if (!Utils.checkText(binding.dateTimeEditBox.getText().toString())) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_PARKING_SELECT_DURATION_VALIDATION_MSG"), buttonId -> {
                });
            } else if (!Utils.checkText(durationId)) {
                generalFunc.showGeneralMessage("", generalFunc.retrieveLangLBl("", "LBL_PARKING_SELECT_DURATION_VALIDATION_MSG"), buttonId -> {
                });
            }
        }
    }

    @Override
    public void itemTimeSlotLocClick(int position) {
        timeSlotAdapter.setSelectedTime(position);
        durationId = timeSlotList.get(position).get("iDurationId");
        timeSlotAdapter.notifyDataSetChanged();
    }
}