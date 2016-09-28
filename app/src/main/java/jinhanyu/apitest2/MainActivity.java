package jinhanyu.apitest2;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jinhanyu.apitest2.custom.ViewPagerIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerIndicator mViewPagerIndicator;

    private List<String> mTitle = Arrays.asList("头条","财经","游戏","电影","娱乐","军事","手机","体育","科技","汽车","笑话",
            "游戏","时尚","情感","精选","电台","数码","NBA","移动","彩票","教育","论坛","旅游",
            "博客","社会","家居","暴雪","亲子","CBA","足球","房产");
    private List<VpSimpleFragment> mContents = new ArrayList<VpSimpleFragment>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();

        mViewPagerIndicator.setTabItemTitles(mTitle);

        mViewPager.setAdapter(mAdapter);
        mViewPagerIndicator.setViewPager(mViewPager, 0);
    }

    public void doClick(View view) {
        switch (view.getId()){
            case R.id.ib_add_main:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in,R.anim.anim_null);
                break;
        }
    }

    private void initDatas() {
        for (String title : mTitle) {
            VpSimpleFragment fragment = VpSimpleFragment.newInstance(title);
            mContents.add(fragment);

        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            public int getCount() {
                return mContents.size();
            }

            public Fragment getItem(int position) {
                return mContents.get(position);
            }
        };
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mViewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.ypi_indicator);
    }
}
