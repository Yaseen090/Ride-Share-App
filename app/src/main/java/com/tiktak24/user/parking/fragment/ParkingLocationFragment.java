package com.tiktak24.user.parking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fragments.BaseFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.ParkingLocationFragmentLayoutBinding;
import com.tiktak24.user.parking.ParkingDetailsActivity;
import com.tiktak24.user.parking.adapter.ParkingListAdapter;
import com.map.GeoMapLoader;
import com.map.Marker;
import com.map.models.LatLng;
import com.map.models.MarkerOptions;
import com.utils.VectorUtils;

public class ParkingLocationFragment extends BaseFragment implements GeoMapLoader.OnMapReadyCallback {

    public ParkingLocationFragmentLayoutBinding binding;
    private ParkingDetailsActivity mActivity;
    ParkingListAdapter adapter;

    private GeneralFunctions generalFunc;
    private Marker parkingLocationMarker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.parking_location_fragment_layout, container, false);
        (new GeoMapLoader(mActivity, R.id.mapParkingBookingContainer)).bindMap(this);
        binding.mapParkingBookingContainer.setEnabled(false);
        binding.mapParkingBookingContainer.setClickable(false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApp.getInstance().getCurrentAct() instanceof ParkingDetailsActivity) {
            mActivity = (ParkingDetailsActivity) requireActivity();
            generalFunc = mActivity.generalFunc;
        }
    }

    @Override
    public void onMapReady(GeoMapLoader.GeoMap geoMap) {
        double latitude = Double.parseDouble(generalFunc.getJsonValueStr("vLatitude", mActivity.obj_msg));
        double longitude = Double.parseDouble(generalFunc.getJsonValueStr("vLongitude", mActivity.obj_msg));
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions parking_marker = new MarkerOptions().position(latLng).icon(VectorUtils.vectorToBitmap(getContext(), R.drawable.parking_loc_pin_dark, 0));
        parkingLocationMarker = geoMap.addMarker(parking_marker);
        geoMap.getUiSettings().setAllGesturesEnabled(false);
        geoMap.moveCamera(latLng);
    }

}
