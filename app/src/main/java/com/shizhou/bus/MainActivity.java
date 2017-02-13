package com.shizhou.bus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.shizhou.bus.DATA.weather;
import com.shizhou.bus.Utils.ToastUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String wt_key="v9gmkhm7jdblw82a";
    public String my_lalng=null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    String city=amapLocation.getCity();
                    double latitude=amapLocation.getLatitude();//获取纬度
                    double longitude=amapLocation.getLongitude();//获取经度
                    my_lalng=latitude+","+longitude;
                    my_city.setText(city);
                    if (!my_lalng.isEmpty()){
                        String wt_url="http://api.yytianqi.com/"+"forecast7d"+"?city="+my_lalng+"&key="+wt_key;
                        Log.e("bus",wt_url);
                        OkHttpUtils.get(wt_url).execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                Toast.makeText(MainActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Gson gson=new Gson();
                                weather wt=gson.fromJson(s,weather.class);
                                String icon_wt=wt.getData().getList().get(0).getTq1();
                                bus_qw.setText(wt.getData().getList().get(0).getQw1()+"℃");
                                bus_wt_txt.setText(icon_wt);
                                setIcon(icon_wt);
                                progressDialog.dismiss();
                            }

                            private void setIcon(String icon_wt) {
                                switch (icon_wt){
                                    case "晴":bus_wticon.setImageResource(R.drawable.wt1);break;
                                    case "多云":bus_wticon.setImageResource(R.drawable.wt2);break;
                                    case "阴":bus_wticon.setImageResource(R.drawable.wt3);break;
                                    case "阵雨":bus_wticon.setImageResource(R.drawable.wt4);break;
                                    case "雷阵雨":bus_wticon.setImageResource(R.drawable.wt5);break;
                                    case "雷阵雨伴有冰雹":bus_wticon.setImageResource(R.drawable.wt6);break;
                                    case "雨夹雪":bus_wticon.setImageResource(R.drawable.wt7);break;
                                    case "小雨":bus_wticon.setImageResource(R.drawable.wt8);break;
                                    case "中雨":bus_wticon.setImageResource(R.drawable.wt9);break;
                                    case "大雨":bus_wticon.setImageResource(R.drawable.wt10);break;
                                    case "暴雨":bus_wticon.setImageResource(R.drawable.wt11);break;
                                    case "大暴雨":bus_wticon.setImageResource(R.drawable.wt12);break;
                                    case "阵雪":bus_wticon.setImageResource(R.drawable.wt13);break;
                                    case "小雪":bus_wticon.setImageResource(R.drawable.wt14);break;
                                    case "中雪":bus_wticon.setImageResource(R.drawable.wt15);break;
                                    case "大雪":bus_wticon.setImageResource(R.drawable.wt16);break;
                                    case "暴雪":bus_wticon.setImageResource(R.drawable.wt17);break;
                                    case "雾":bus_wticon.setImageResource(R.drawable.wt18);break;
                                    case "冻雨":bus_wticon.setImageResource(R.drawable.wt19);break;
                                    case "沙尘暴":bus_wticon.setImageResource(R.drawable.wt20);break;
                                    case "小到中雨":bus_wticon.setImageResource(R.drawable.wt21);break;
                                    case "中到大雨":bus_wticon.setImageResource(R.drawable.wt22);break;
                                    case "大到暴雨":bus_wticon.setImageResource(R.drawable.wt23);break;
                                    case "暴雨到大暴雨":bus_wticon.setImageResource(R.drawable.wt24);break;
                                    case "大暴雨到特大暴雨":bus_wticon.setImageResource(R.drawable.wt25);break;
                                    case "小到中雪":bus_wticon.setImageResource(R.drawable.wt26);break;
                                    case "中到大雪":bus_wticon.setImageResource(R.drawable.wt27);break;
                                    case "大到暴雪":bus_wticon.setImageResource(R.drawable.wt28);break;
                                    case "浮尘":bus_wticon.setImageResource(R.drawable.wt29);break;
                                    case "扬沙":bus_wticon.setImageResource(R.drawable.wt30);break;
                                    case "强沙尘暴":bus_wticon.setImageResource(R.drawable.wt31);break;
                                    case "浓雾":bus_wticon.setImageResource(R.drawable.wt32);break;case "霾":bus_wticon.setImageResource(R.drawable.wt33);break;
                                    case "强浓雾":bus_wticon.setImageResource(R.drawable.wt34);break;
                                    case "中度霾":bus_wticon.setImageResource(R.drawable.wt35);break;
                                    case "重度霾":bus_wticon.setImageResource(R.drawable.wt36);break;
                                    case "大雾":bus_wticon.setImageResource(R.drawable.wt37);break;
                                    case "特强浓雾":bus_wticon.setImageResource(R.drawable.wt38);break;
                                    case "刮风":bus_wticon.setImageResource(R.drawable.wt39);break;
                                }
                            }
                        });
                    }
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    Toast.makeText(MainActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private TextView my_city;
    private ImageView bus_wticon;
    private ProgressDialog progressDialog;
    private TextView bus_qw;
    private TextView bus_wt_txt;
    private TextView station_search;
    private TextView ways_search;
    private View contentView;
    private PopupWindow popupWindow;
    private TextView nave_sure;
    private TextView navi_flase;
    private int select_id;
    private TextView navi_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocaiton();
        findID();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        station_search.setOnClickListener(this);
        ways_search.setOnClickListener(this);
        navi_txt.setOnClickListener(this);
    }

    private void findID() {
        my_city = (TextView) findViewById(R.id.bus_mycity);
        bus_wticon = (ImageView) findViewById(R.id.bus_wticon);
        bus_qw = (TextView) findViewById(R.id.bus_qw);
        bus_wt_txt = (TextView) findViewById(R.id.bus_wt_txt);
        station_search = (TextView) findViewById(R.id.station_search);
        ways_search = (TextView) findViewById(R.id.ways_search);
        navi_txt = (TextView) findViewById(R.id.navi_txt);
    }

    private void getLocaiton() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(10000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.station_search:
                Intent intent=new Intent(MainActivity.this,BusStation.class);
                startActivity(intent);break;
            case R.id.ways_search:
                Intent intent1=new Intent(MainActivity.this,Ways.class);
                startActivity(intent1);break;
            case R.id.navi_txt:
                Log.e("bus","此处应有POP");
                pop();
            default:break;
        }
    }
    private void pop() {
        contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.navi, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(station_search, Gravity.CENTER,0,0);
        nave_sure = (TextView) contentView.findViewById(R.id.button);
        navi_flase = (TextView) contentView.findViewById(R.id.button2);
        RadioGroup navi_select = (RadioGroup) contentView.findViewById(R.id.radio_gruop);
        navi_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.navi2:
                        Toast.makeText(MainActivity.this,"调用百度地图",Toast.LENGTH_SHORT).show();
                        select_id=2;
                        break;
                    case R.id.navi3:
                        Toast.makeText(MainActivity.this,"调用高德地图",Toast.LENGTH_SHORT).show();
                        select_id=3;
                        break;
                    default:break;
                }
            }
        });
        navi_flase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        nave_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (select_id){
                    case 2:
                    popupWindow.dismiss();
                        //调起百度地图客户端
                            if(isInstallByread("com.baidu.BaiduMap")){
                                Intent intent1=new Intent();
                                PackageManager packageManager = MainActivity.this.getPackageManager();
                                intent1=packageManager.getLaunchIntentForPackage("com.baidu.BaiduMap");
                                startActivity(intent1); //启动调用
                                ToastUtil.show(MainActivity.this,"启动成功");
                            }else{
                                ToastUtil.show(MainActivity.this,"您尚未安装百度地图");
                            }
                        break;
                    case 3:
                        popupWindow.dismiss();
                        if(isInstallByread("com.autonavi.minimap")){
                            Intent intent1=new Intent();
                            PackageManager packageManager = MainActivity.this.getPackageManager();
                            intent1=packageManager.getLaunchIntentForPackage("com.autonavi.minimap");
                            startActivity(intent1); //启动调用
                            ToastUtil.show(MainActivity.this,"启动成功");
                        }else{
                            ToastUtil.show(MainActivity.this,"您尚未安装高德地图");
                        }
                        break;
                    default:break;

                }
            }
        });


    }
    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
