package com.tiktak24.user.parking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fragments.BaseFragment;
import com.general.files.ActUtils;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.ParkingListFragmentLayoutBinding;
import com.tiktak24.user.parking.AvailableParkingSpacesActivity;
import com.tiktak24.user.parking.ParkingDetailsActivity;
import com.tiktak24.user.parking.ReviewOrCancelParkingBookingActivity;
import com.tiktak24.user.parking.adapter.ParkingListAdapter;

import java.util.HashMap;

public class ParkingListFragment extends BaseFragment {

    public ParkingListFragmentLayoutBinding binding;
    private AvailableParkingSpacesActivity mActivity;
    ParkingListAdapter adapter;

    private GeneralFunctions generalFunc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.parking_list_fragment_layout, container, false);

        adapter = new ParkingListAdapter(mActivity, generalFunc, mActivity.listData, new ParkingListAdapter.OnClickListener() {
            @Override
            public void onBookNowClick(int position, HashMap<String, String> mapData) {
                Bundle bn = new Bundle();
                bn.putString("CallApi", "yes");
                bn.putString("parkingSpacesId", mapData.get("iParkingSpaceId"));
                bn.putString("duration", mActivity.getIntent().getStringExtra("duration"));
                bn.putString("bookingLatitude", mapData.get("vLatitude"));
                bn.putString("bookingLongitude", mapData.get("vLongitude"));
                bn.putString("iParkingVehicleSizeId", mActivity.getIntent().getStringExtra("parkingId"));
                bn.putString("ArrivalDate", mActivity.getIntent().getStringExtra("dateTime"));
                bn.putSerializable("vehicleSizes", mActivity.getIntent().getSerializableExtra("vehicleSizes"));
                new ActUtils(mActivity).startActWithData(ReviewOrCancelParkingBookingActivity.class, bn);
            }

            @Override
            public void onItemClick(int position, HashMap<String, String> mapData) {
                Bundle bn = new Bundle();
                bn.putString("parkingSpacesId", mapData.get("iParkingSpaceId"));
                bn.putString("duration", mActivity.getIntent().getStringExtra("duration"));
                bn.putString("bookingLatitude", mapData.get("vLatitude"));
                bn.putString("bookingLongitude", mapData.get("vLongitude"));
                bn.putString("ArrivalDate", mActivity.getIntent().getStringExtra("dateTime"));
                bn.putString("iParkingVehicleSizeId", mActivity.getIntent().getStringExtra("parkingId"));
                bn.putSerializable("vehicleSizes", mActivity.getIntent().getSerializableExtra("vehicleSizes"));
                new ActUtils(mActivity).startActWithData(ParkingDetailsActivity.class, bn);
            }
        });

        binding.rvParkingList.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApp.getInstance().getCurrentAct() instanceof AvailableParkingSpacesActivity) {
            mActivity = (AvailableParkingSpacesActivity) requireActivity();
            generalFunc = mActivity.generalFunc;
        }
    }

}
