package com.shizhou.bus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.interfaces.IRouteSearch;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.shizhou.bus.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ways extends AppCompatActivity implements LocationSource, AMapLocationListener, View.OnClickListener, RouteSearch.OnRouteSearchListener, Inputtips.InputtipsListener {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public LocationSource.OnLocationChangedListener mListener;
    private MapView mapView;
    public AMap aMap;
    private String citycode;
    private ImageView search;
    private RouteSearch routeSearch;
    private EditText start_edit;
    private EditText finish_edit;
    private ArrayList<Map<String, String>> data;
    private PopupWindow popupWindow;
    private View contentView;
    private String cityname;
    private LinearLayout bus_listlayout;
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_CROSSTOWN = 4;
    private Context mContext=Ways.this;
    private ProgressDialog progDialog;
    private RouteSearch mRouteSearch;
    private BusRouteResult mBusRouteResult;
    private ListView mBusResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ways);
        mapView = (MapView) findViewById(R.id.ways_map);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
            // 设置定位监听
            aMap.setLocationSource(this);
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(true);
            // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        }
        findID();
        search.setOnClickListener(this);
    }

    private void findID() {
        search = (ImageView) findViewById(R.id.ways_search_img);
        start_edit = (EditText) findViewById(R.id.start_locaiton);
        finish_edit = (EditText) findViewById(R.id.finish_locaiton);
        pop();
        start_edit.addTextChangedListener(start_watcher);
        finish_edit.addTextChangedListener(finish_watcher);
        bus_listlayout = (LinearLayout) findViewById(R.id.bus_listlayout);
        mBusResultList=(ListView) findViewById(R.id.bus_listview);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    private void pop() {
        contentView = LayoutInflater.from(Ways.this).inflate(R.layout.station_list, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        bus_listlayout.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                citycode = amapLocation.getCityCode();
                cityname=amapLocation.getCity();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ways_search_img:
                if (!start_edit.getText().toString().isEmpty()&&!finish_edit.getText().toString().isEmpty()){
                    searchRouteResult(ROUTE_TYPE_BUS,RouteSearch.BusDefault);
                bus_listlayout.setVisibility(View.VISIBLE);}
                else if (start_edit.getText().toString().isEmpty()){
                    Toast.makeText(this,"请输入起点",Toast.LENGTH_SHORT).show();
                }
                else if (finish_edit.getText().toString().isEmpty()){
                    Toast.makeText(this,"请输入终点",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /*private void waysResearch() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        SharedPreferences sp=getSharedPreferences("latlng",MODE_PRIVATE);
        String latlng1=sp.getString("latlng1","0.00,0.00");
        String latlng2=sp.getString("latlng2","0.00,0.00");
        String[] sp1=latlng1.split("\\,");
        String[] sp2=latlng1.split("\\,");
        LatLonPoint latLonPoint1=new LatLonPoint(Double.parseDouble(sp1[0]),Double.parseDouble(sp1[1]));
        LatLonPoint latLonPoint2=new LatLonPoint(Double.parseDouble(sp2[0]),Double.parseDouble(sp2[1]));
        Log.e("bus","latlng1="+latLonPoint1.toString()+"=====latlng2="+latLonPoint2.toString());
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(latLonPoint1,latLonPoint2);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, "010", 1);
        routeSearch.calculateBusRouteAsyn(query);
    }*/

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        SharedPreferences sp=getSharedPreferences("latlng",MODE_PRIVATE);
        String latlng1=sp.getString("latlng1","0.00,0.00");
        String latlng2=sp.getString("latlng2","0.00,0.00");
        String[] sp1=latlng1.split("\\,");
        String[] sp2=latlng2.split("\\,");
        LatLonPoint mStartPoint=new LatLonPoint(Double.parseDouble(sp1[0]),Double.parseDouble(sp1[1]));
        LatLonPoint mEndPoint=new LatLonPoint(Double.parseDouble(sp2[0]),Double.parseDouble(sp2[1]));
        Log.e("bus","latlng1="+mStartPoint.toString()+"=====latlng2="+mEndPoint.toString());
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    cityname, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        dissmissProgressDialog();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                    mBusResultList.setAdapter(mBusResultListAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }

    }
    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索...");
        progDialog.show();
    }
    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    private List<? extends Map<String,?>> getData(List<Tip> list) {
        data = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            map=new HashMap<String, String>();
            map.put("station",list.get(i).getName());
            map.put("latlng", String.valueOf(list.get(i).getPoint()));
            data.add(map);
        }
        return data;
    }
    private TextWatcher start_watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
            InputtipsQuery inputquery = new InputtipsQuery(start_edit.getText().toString(), cityname);
            inputquery.setCityLimit(true);//限制在当前城市
            Inputtips inputTips = new Inputtips(Ways.this, inputquery);
            inputTips.setInputtipsListener(Ways.this);
            inputTips.requestInputtipsAsyn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher finish_watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
            InputtipsQuery inputquery = new InputtipsQuery(finish_edit.getText().toString(), cityname);
            inputquery.setCityLimit(true);//限制在当前城市
            Inputtips inputTips = new Inputtips(Ways.this, inputquery);
            inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                @Override
                public void onGetInputtips(List<Tip> list, int i) {
                    if (i==1000){
                        Log.e("bus","搜索成功");
                        SimpleAdapter adapter = new SimpleAdapter(Ways.this, getData(list), R.layout.list, new String[]{"station"}, new int[]{R.id.txt});
                        final ListView listview = (ListView) contentView.findViewById(R.id.listview);
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                finish_edit.setText(data.get(position).get("station"));
                                SharedPreferences sp=getSharedPreferences("latlng",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putString("latlng2",data.get(position).get("latlng"));
                                editor.apply();
                            }
                        });
                        popupWindow.showAsDropDown(finish_edit);}
                    else {
                        ToastUtil.show(Ways.this,"获取信息失败");
                    }
                }
            });
            inputTips.requestInputtipsAsyn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if (i==1000){
            Log.e("bus","搜索成功");
        SimpleAdapter adapter = new SimpleAdapter(Ways.this, getData(list), R.layout.list, new String[]{"station"}, new int[]{R.id.txt});
        final ListView listview = (ListView) contentView.findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                start_edit.setText(data.get(position).get("station"));
                SharedPreferences sp=getSharedPreferences("latlng",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("latlng1",data.get(position).get("latlng"));
                editor.apply();
            }
        });
        popupWindow.showAsDropDown(start_edit);}
        else {
            ToastUtil.show(this,"获取信息失败");
        }
    }
    public void back(View view){
        Intent intent=new Intent(Ways.this,MainActivity.class);
        startActivity(intent);
    }
}
