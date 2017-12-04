package cn.czyugang.tcg.client.modules.address.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.czyugang.tcg.client.R;
import cn.czyugang.tcg.client.base.BaseActivity;
import cn.czyugang.tcg.client.base.BaseRecyclerAdapter;
import cn.czyugang.tcg.client.base.DefaultItemDecoration;
import cn.czyugang.tcg.client.common.LocationManager;
import cn.czyugang.tcg.client.modules.address.adapter.SelectLocationAdapter;
import cn.czyugang.tcg.client.widget.MapView;

/**
 * Created by wuzihong on 2017/10/30.
 */

public class SelectLocationActivity extends BaseActivity implements View.OnFocusChangeListener,
        AMap.OnCameraChangeListener, RadioGroup.OnCheckedChangeListener, TextWatcher,
        PoiSearch.OnPoiSearchListener, BaseRecyclerAdapter.OnItemClickListener,
        GeocodeSearch.OnGeocodeSearchListener {
    public static final String KEY_LOCATION = "location";
    public static final String KEY_STREET = "street";
    private final int MAP_LEVEL = 16;//地图空间等级
    private final int LINE_TAB_MARGIN = 15;//红色标志线左右边距
    private final String CTGR_ALL = "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|" +
            "生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|" +
            "科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
    @BindView(R.id.title)
    View titleL ;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.iv_current_location)
    ImageView iv_current_location;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.group_tab)
    RadioGroup group_tab;
    @BindView(R.id.line_tab)
    View line_tab;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.list_search)
    RecyclerView list_search;
    private DisplayMetrics mDm;
    private AMap mAMap;
    private SelectLocationAdapter mAdapter;
    private SelectLocationAdapter mSearchAdapter;

    private AMapLocation mLocation;
    private PoiSearch mPoiSearch;
    private GeocodeSearch mGeocodeSearch;
    private String ctgr = CTGR_ALL;
    private PoiItem mPoiItem;

    public static void startSelectLocationActivity(Activity activity,int requestCode){
        Intent intent=new Intent(getTopActivity(),SelectLocationActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startSelectLocationActivity(Activity activity,int requestCode,String title,String searchHint){
        Intent intent=new Intent(getTopActivity(),SelectLocationActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("searchHint",searchHint);
        activity.startActivityForResult(intent,requestCode);
    }

    private void parseIntentData(){
        String title=getIntent().getStringExtra("title");
        String searchHint=getIntent().getStringExtra("searchHint");
        if (title!=null){
            titleText.setText(title);
            findViewById(R.id.iv_back).setVisibility(View.GONE);
        }else {
            titleL.setVisibility(View.GONE);
            findViewById(R.id.title_bar).setVisibility(View.GONE);
        }
        if (searchHint!=null){
            et_search.setHint(searchHint);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        initView();
        parseIntentData();
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

    private void initView() {
        et_search.setOnFocusChangeListener(this);
        et_search.addTextChangedListener(this);

        mAMap = mapView.getMap();
        mAMap.setOnCameraChangeListener(this);
        mLocation = LocationManager.getInstance().getLocation();
        LatLng latLng = getIntent().getParcelableExtra(KEY_LOCATION);
        if (latLng == null) {
            if (mLocation != null) {
                latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            } else {
                //潮州人民政府
                latLng = new LatLng(23.657626, 116.621468);
            }
        }
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_LEVEL));
        iv_current_location.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_current_location.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ((FrameLayout.LayoutParams) iv_current_location.getLayoutParams()).topMargin = -iv_current_location.getHeight() / 2;
            }
        });

        group_tab.setOnCheckedChangeListener(this);
        //初始化红色标志线宽度（屏幕宽度 / 2 - 左右边距）
        Resources resources = getResources();
        mDm = resources.getDisplayMetrics();
        line_tab.getLayoutParams().width = (int) (mDm.widthPixels / 4 - 2 * LINE_TAB_MARGIN * mDm.density);
        group_tab.check(R.id.rbtn_all);

        DefaultItemDecoration itemDecoration = new DefaultItemDecoration(resources, DefaultItemDecoration.NOT_HEADER_FOOTER_DECORATION);
        itemDecoration.setDividerHeightResource(R.dimen.divider);
        itemDecoration.setDividerColorResource(R.color.div_light);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter = new SelectLocationAdapter(context);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        list_search.setLayoutManager(new LinearLayoutManager(context));
        list_search.addItemDecoration(itemDecoration);
        mSearchAdapter = new SelectLocationAdapter(context);
        mSearchAdapter.setOnItemClickListener(this);
        list_search.setAdapter(mSearchAdapter);
    }

    private void poiSearch(String s, LatLng latLng) {
        PoiSearch.Query query = new PoiSearch.Query(s, latLng == null ? CTGR_ALL : ctgr, mLocation != null ? mLocation.getCityCode() : "0768");
        if (mPoiSearch == null) {
            mPoiSearch = new PoiSearch(this, query);
            mPoiSearch.setOnPoiSearchListener(this);
        } else {
            mPoiSearch.setQuery(query);
        }
        if (latLng != null) {
            mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 1000));
        } else {
            mPoiSearch.setBound(null);
        }
        mPoiSearch.searchPOIAsyn();
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.iv_relocation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                et_search.clearFocus();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.iv_relocation:
                Location location = mAMap.getMyLocation();
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_LEVEL));
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        list_search.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        poiSearch("", cameraPosition.target);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int position = 0;
        switch (checkedId) {
            case R.id.rbtn_all:
                position = 0;
                ctgr = CTGR_ALL;
                break;
            case R.id.rbtn_office:
                position = 1;
                ctgr = "写字楼";
                break;
            case R.id.rbtn_house:
                position = 2;
                ctgr = "小区";
                break;
            case R.id.rbtn_school:
                position = 3;
                ctgr = "学校";
                break;
        }
        //设置红色标志线位置（屏幕宽度 / 2 * 偏移量 + 左边距）
        line_tab.setTranslationX(mDm.widthPixels / 4 * position + LINE_TAB_MARGIN * mDm.density);
        Location location = mAMap.getMyLocation();
        if (location != null) {
            poiSearch("", new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            mSearchAdapter.setData(null);
            mSearchAdapter.notifyDataSetChanged();
            return;
        }
        poiSearch(s.toString(), null);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (poiResult.getBound() == null) {
            mSearchAdapter.setData(poiResult.getPois());
            mSearchAdapter.notifyDataSetChanged();
            list_search.scrollToPosition(0);
        } else {
            mAdapter.setData(poiResult.getPois());
            mAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object data) {
        if (mGeocodeSearch == null) {
            mGeocodeSearch = new GeocodeSearch(context);
            mGeocodeSearch.setOnGeocodeSearchListener(this);
        }
        mPoiItem = (PoiItem) data;
        RegeocodeQuery query = new RegeocodeQuery(mPoiItem.getLatLonPoint(), 200, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Intent intent = new Intent();
        intent.putExtra(KEY_LOCATION, mPoiItem);
        intent.putExtra(KEY_STREET, regeocodeResult.getRegeocodeAddress().getTownship());
        setResult(RESULT_OK, intent);
        finish();
    }

    public static String getStreet(Intent intent){
        return intent.getStringExtra(KEY_STREET);
    }

    public static PoiItem getLocation(Intent intent){
        return intent.getParcelableExtra(KEY_LOCATION);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }
}
