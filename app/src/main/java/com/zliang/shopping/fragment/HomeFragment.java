package com.zliang.shopping.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zliang.shopping.Config.ConstantsValues;
import com.zliang.shopping.R;
import com.zliang.shopping.adapter.DividerItemDecortion;
import com.zliang.shopping.adapter.HomeCatgoryAdapter;
import com.zliang.shopping.bean.Banner;
import com.zliang.shopping.bean.Campaign;
import com.zliang.shopping.bean.HomeCampaign;
import com.zliang.shopping.bean.HomeCategory;
import com.zliang.shopping.http.BaseCallBack;
import com.zliang.shopping.http.OkHttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Ivan on 15/9/25.
 */
public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;
    HomeCatgoryAdapter homeCatgoryAdapter;

    private List<Banner> mBanner;
    private OkHttpHelper mHttpler = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        mIndicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        requestImg();
        initRecycleView(view);
        return view;
    }

    private void requestImg() {
        String url = ConstantsValues.BANNER;
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        mHttpler.post(url, params, new BaseCallBack<List<Banner>>() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int responseCode, Exception e) {

            }
        });

    }

    private void initRecycleView(final View view) {
        String url = ConstantsValues.CAMPAIGN_HOME;
        mHttpler.get(url, new BaseCallBack<List<HomeCampaign>>() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(view, homeCampaigns);
            }

            @Override
            public void onError(Response response, int responseCode, Exception e) {

            }
        });

    }

    private void initData(View view, List<HomeCampaign> homeCampaigns) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        homeCatgoryAdapter = new HomeCatgoryAdapter(this.getActivity(), homeCampaigns);
        mRecyclerView.setAdapter(homeCatgoryAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeCatgoryAdapter.setOnItemClickListener(new HomeCatgoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, Campaign campaign) {
                Toast.makeText(getActivity(), campaign.getTitle(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initSlider() {

        if (mBanner != null && mBanner.size() > 0) {
            for (Banner banner : mBanner) {
                TextSliderView sliderView = new TextSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                mSliderLayout.addSlider(sliderView);
            }
        }


        // 设置默认指示器位置(默认指示器白色,位置在sliderlayout底部)
//        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
        mSliderLayout.setDuration(3000);
//        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
//        mSliderLayout.setCustomIndicator(mIndicator);
        mSliderLayout.startAutoCycle();


    }

    @Override
    public void onStop() {
        super.onStop();
        mSliderLayout.stopAutoCycle();
    }
}
