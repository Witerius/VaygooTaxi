package com.vaygoo.vaygootaxi.fragment;

/**
 * Created by Wizard on 02.05.2017.
 */


import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.vaygoo.vaygootaxi.GpsProviderMap;
import com.vaygoo.vaygootaxi.MainActivity;
import com.vaygoo.vaygootaxi.MapItemizedOverlay;
import com.vaygoo.vaygootaxi.R;
import com.vaygoo.vaygootaxi.ReloadInterface;
import com.vaygoo.vaygootaxi.model.OrderModel;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.vaygoo.vaygootaxi.MainActivity.mSettings;
import static com.vaygoo.vaygootaxi.MainActivity.saveCoordinate_s;
import static com.vaygoo.vaygootaxi.MainActivity.saveZoom;

import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class MapFragment extends Fragment implements ReloadInterface {

    public static double stLocation1, stLocation2;
    private MapController mMapController;
    MapItemizedOverlay myItemizedOverlay = null;
    MapView mapView;
    //по умолчанию сделать координаты Москвы

    GpsProviderMap imlp;
    MyLocationNewOverlay myLoc;
    ResourceProxy resourceProxy;
    Drawable marker;
    public static ReloadInterface reloadInterface;
    private int idOfSheetPanelData = 0;
    MainActivity activity;
    boolean isListHidden = true;
    boolean isMinivan = false;
    boolean isChild = false;
    boolean isFinalPrice = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            reloadInterface = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            activity = (MainActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //ЖОБАВЬ ПРИБЛИЖЕНИЕ МАКСИМАЛЬНОЕ, ЦВЕТА НА БЕЛЫЙ ИЗМЕНИ, ИЛИ НА СВЕТЛО СЕРЫЙ, ДОБАВЬ ПРОЗРАЧНЫЙ СТАТУС БАР
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.setUseSafeCanvas(false);
        //  mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);


        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapController = (MapController) mapView.getController();
        mMapController.setZoom(saveZoom);

        GeoPoint gPt;
        if (saveCoordinate_s == null) {
            gPt = new GeoPoint(55.750681, 37.6007998);
        } else {
            final String[] temp = saveCoordinate_s.split("~##~");

            stLocation1 = Double.valueOf(temp[0]);
            stLocation2 = Double.valueOf(temp[1]);

            gPt = new GeoPoint(stLocation1, stLocation2);
        }

        mMapController.setCenter(gPt);

        //в метод все это и после резюма перезагружать
        imlp = new GpsProviderMap(getActivity().getBaseContext());

        imlp.setLocationUpdateMinDistance(1000);
        imlp.setLocationUpdateMinTime(6000);
        IMyLocationConsumer mMyLocationConsumer = new IMyLocationConsumer() {
            @Override
            public void onLocationChanged(Location location, IMyLocationProvider iMyLocationProvider) {
                changeLocation(location);
            }
        };
        imlp.startLocationProvider(mMyLocationConsumer);

        resourceProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());

        myLoc = new MyLocationNewOverlay(imlp, mapView, resourceProxy);
        myLoc.enableFollowLocation();

        myLoc.setDrawAccuracyEnabled(true);
        myLoc.setUseSafeCanvas(false);
        mapView.getOverlays().add(myLoc);

        GeoPoint  currentLocation = new GeoPoint(stLocation1, stLocation2);

        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", currentLocation);
        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.ic_action_directions_car);
        myCurrentLocationMarker.setBounds(0, 60, 30, 0);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);


        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(myLocationOverlayItem);

        ItemizedIconOverlay<OverlayItem> currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().add(currentLocationOverlay);

        /////////////

        createBottomSheetStartPanel(inflater);
        return rootView;
    }

   /* private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            changeLocation(location);
            Log.v("Excel", "Changed " + String.valueOf(location));
            GeoPoint locationOne = new GeoPoint(stLocation1, stLocation2);
            try {
                mapView.getOverlays().remove(myItemizedOverlay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            myItemizedOverlay = new MapItemizedOverlay(marker, resourceProxy);
            marker = getResources().getDrawable(R.drawable.man);
            int markerWidth = 30; //marker.getIntrinsicWidth();
            int markerHeight = 60; //marker.getIntrinsicHeight();
            marker.setBounds(0, markerHeight, markerWidth, 0);

            myItemizedOverlay = new MapItemizedOverlay(marker, resourceProxy);
            mapView.getOverlays().add(myItemizedOverlay);


            Log.v("Excel", "Creaaaaa9 " + stLocation1 + " " + stLocation2);
            myItemizedOverlay.addItem(locationOne, "Location 2", "2 Location");

            mapView.getOverlays().add(myItemizedOverlay);
            try {
                Log.v("Excel", " location " + myLoc.getMyLocation() + " " + myLoc.getLastFix());
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("Excel", " location111 ");
            }
        }

    };*/

    public void zoomMap(boolean zoomPlum) {
        Log.v("Excel", "Zoom " + saveZoom + " " + mapView.getZoomLevel());
        // saveZoom = mapView.getZoomLevel();
        if (zoomPlum) {
            saveZoom++;
        } else {
            saveZoom--;
        }
        mMapController.setZoom(saveZoom);
    }


    @Override
    public void onPause() {
        super.onPause();
        imlp.stopLocationProvider();

        saveCoordinate_s = "" + stLocation1 + "~##~" + stLocation2;

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(MainActivity.APP_PREFERENCES_SAVE_COORDINATE, MainActivity.saveCoordinate_s);
        editor.apply();

        SharedPreferences.Editor editor1 = mSettings.edit();
        editor1.putInt(MainActivity.APP_PREFERENCES_SAVE_ZOOM, MainActivity.saveZoom);
        editor1.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imlp.reloadLocationForResume();

    }

    private void changeLocation(Location location) {

        if (location != null) {
            GeoPoint gPt = new GeoPoint(location.getLatitude(), location.getLongitude());
            mMapController.setCenter(gPt);

            stLocation1 = location.getLatitude();
            stLocation2 = location.getLongitude();

            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try
            {
                addresses = geocoder.getFromLocation(stLocation1, stLocation2, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
               Log.v("Excel", "ADDRESS " + addresses.get(0).getMaxAddressLineIndex() + " "+ city + " "+ postalCode + " "+
                       knownName + " "+ addresses.get(0).getPhone() + " "+ addresses.get(0).getPremises() + " "+
                       addresses.get(0).getSubAdminArea() + " "+ addresses.get(0).getSubLocality() + " "+
                       addresses.get(0).getSubThoroughfare() + " "+ addresses.get(0).getThoroughfare() );

            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getLocationReload() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // imlp.reloadLocationForResume();  //скорее всего излишне
        Log.v("Excel", "change local   numOfperm ");
        changeLocation(imlp.getLastKnownLocation());

    }

    @Override
    public void reloadGpsPosition(Location location) {
        changeLocation(location);
    }

    public void onClickLocationSettings() {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void createBottomSheetStartPanel(final LayoutInflater inflater) {

        final LinearLayout viewOfSheetPanel = (LinearLayout) inflater.inflate(R.layout.zakaz_taxi, null);
        viewOfSheetPanel.setId(idOfSheetPanelData);

        //OrderModel orderModel = new OrderModel();

        final EditText etStartPoint = (EditText) viewOfSheetPanel.findViewById(R.id.et_start);
        etStartPoint.setHint(R.string.start_point);
        etStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.collapseOrExpandBottomSheetWhenTouchMap(false);
            }
        });

        final EditText etEndPoint = (EditText) viewOfSheetPanel.findViewById(R.id.et_end);
        etEndPoint.setHint(R.string.end_point);

        final EditText etPrice = (EditText) viewOfSheetPanel.findViewById(R.id.et_price);
        etPrice.setHint(R.string.price);

        final EditText etIntermediatePoint = (EditText) viewOfSheetPanel.findViewById(R.id.et_intermediate);
        etIntermediatePoint.setHint(R.string.int_point);

        final LinearLayout group_ll_expand = (LinearLayout) viewOfSheetPanel.findViewById(R.id.group_ll_expand);

        ImageView expand_list_of_order = (ImageView) viewOfSheetPanel.findViewById(R.id.expand_list_of_order);
        expand_list_of_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isListHidden) {
                    isListHidden = true;
                    group_ll_expand.setVisibility(View.GONE);
                } else {
                    isListHidden = false;
                    group_ll_expand.setVisibility(View.VISIBLE);
                }
            }
        });

        final LinearLayout llPanelIntermediate = (LinearLayout) viewOfSheetPanel.findViewById(R.id.ll_add_intermediate);

        ImageView addIntermediate = (ImageView) viewOfSheetPanel.findViewById(R.id.add_intermediate);
        addIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayoutIntermediate = (RelativeLayout)
                        inflater.inflate(R.layout.intermediate_new, null);

                EditText newIntermediate = (EditText) relativeLayoutIntermediate.findViewById(R.id.et_intermediate_new);
                newIntermediate.setHint(R.string.int_point);
                addNewIntermediate(inflater,  llPanelIntermediate);

            }
        });


        final EditText etComment = (EditText) viewOfSheetPanel.findViewById(R.id.et_comment);
        etComment.setHint(R.string.add_comment);

        Switch switch_child = (Switch) viewOfSheetPanel.findViewById(R.id.switch_child);
        switch_child.setChecked(isChild);
        switch_child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChild = !isChild;
            }
        });

        Switch switch_f_price = (Switch) viewOfSheetPanel.findViewById(R.id.switch_f_price);
        switch_f_price.setChecked(isFinalPrice);
        switch_f_price.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isFinalPrice = !isFinalPrice;
            }
        });

        CardView order_start = (CardView) viewOfSheetPanel.findViewById(R.id.order_start);
        order_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////
                OrderModel orderModel = new OrderModel(String.valueOf(etStartPoint.getText()), String.valueOf(etEndPoint.getText()),
                        String.valueOf(etIntermediatePoint.getText())/*ВРЕМЕННООООООООО*/, String.valueOf(etPrice.getText()),
                        String.valueOf(etComment.getText()), isChild, isMinivan, isFinalPrice);
                Log.v("Excel", "PROVERKA MODEL ORDER "+ String.valueOf(etStartPoint.getText())+ " "+
                                String.valueOf(etEndPoint.getText()) + " "+
                        String.valueOf(etIntermediatePoint.getText()) + " "+ String.valueOf(etPrice.getText()) + " "+
                        String.valueOf(etComment.getText())+ " "+ isChild + " "+  isMinivan+ " "+ isFinalPrice);
                //добавить отправку куда нужно
            }
        });

        final List<String > typeCar = new ArrayList<>();
        typeCar.add(getActivity().getString(R.string.standart_car));
        typeCar.add(getActivity().getString(R.string.minivan));
        typeCar.add(getActivity().getString(R.string.limousine));
        typeCar.add(getActivity().getString(R.string.elite_car));

        final List<String > typeTrip = new ArrayList<>();
        typeTrip.add(getActivity().getString(R.string.standart_trip));
        typeTrip.add(getActivity().getString(R.string.hourly_pay));
        typeTrip.add(getActivity().getString(R.string.taxi_day));

        Spinner spTypeAuto = (Spinner) viewOfSheetPanel.findViewById(R.id.spTypeOfAuto);
        ArrayAdapter<String> selectTypeCar = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, typeCar);

        spTypeAuto.setAdapter(selectTypeCar);

        spTypeAuto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a = typeCar.get(position);
                //modelBaseOfMusic.setTonality(a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spTypeTrip = (Spinner) viewOfSheetPanel.findViewById(R.id.spTypeOfDrive);
        ArrayAdapter<String> selectTypeTrip = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, typeTrip);

        spTypeTrip.setAdapter(selectTypeTrip);

        spTypeTrip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a = typeTrip.get(position);
                //modelBaseOfMusic.setTonality(a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LinearLayout botZakaz = ((LinearLayout) getActivity().findViewById(R.id.botZakaz));
        botZakaz.addView(viewOfSheetPanel);
    }

    private void addNewIntermediate(final LayoutInflater inflater, final LinearLayout llPanelIntermediate) {
        final RelativeLayout relativeLayoutIntermediate = (RelativeLayout)
                inflater.inflate(R.layout.intermediate_new, null);
       // relativeLayoutIntermediate.setId(idOfSheetPanelData);

        EditText newIntermediate = (EditText) relativeLayoutIntermediate.findViewById(R.id.et_intermediate_new);
        newIntermediate.setHint(R.string.int_point);

        ImageView removeIntermediate = (ImageView) relativeLayoutIntermediate.findViewById(R.id.remove_intermediate_new);
        removeIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llPanelIntermediate.removeView(relativeLayoutIntermediate);
            }
        });
        llPanelIntermediate.addView(relativeLayoutIntermediate);

       // idOfSheetPanelData++;

    }
}