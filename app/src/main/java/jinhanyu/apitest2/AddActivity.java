package jinhanyu.apitest2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import jinhanyu.apitest2.custom.GVSelected;
import jinhanyu.apitest2.custom.GVUnSelected;

/**
 * Created by anzhuo on 2016/9/22.
 */

public class AddActivity extends AppCompatActivity {

    GVSelected mGVSelected;
    GVUnSelected mGVUnSelected;
    private SimpleAdapter adapterUp;
    private SimpleAdapter adapterDown;
    Thread threadUp, threadDown;
    int mPosition;
    private static final int NOGIZAKA = 46;
    private static final int AKB = 48;

    String[] a = {"头条","财经","游戏","电影","娱乐","军事","手机","体育","科技","汽车","笑话",
            "游戏","时尚","情感","精选","电台","数码","NBA","移动","彩票","教育","论坛","旅游",
            "博客","社会","家居","暴雪","亲子","CBA","足球","房产"};
    private List<String> mTitles = new ArrayList<>(50);
    private List<String> nTitles = new ArrayList<>(50);

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NOGIZAKA) {
                nTitles.add(mTitles.get(mPosition));
                mTitles.remove(mPosition);
            } else if (msg.what == AKB) {
                mTitles.add(nTitles.get(mPosition));
                nTitles.remove(mPosition);
            }
            adapterDown.notifyDataSetChanged();
            adapterUp.notifyDataSetChanged();
            Log.i("TZ", "up" + mTitles);
            Log.i("TZ", "down" + nTitles);
            sea();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initViews();//初始化控件
        sea();

        mGVSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                Toast.makeText(AddActivity.this, mTitles.get(position), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = NOGIZAKA;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        mGVUnSelected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                Toast.makeText(AddActivity.this, nTitles.get(position), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = AKB;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    private void initViews() {
        mGVSelected = (GVSelected) findViewById(R.id.gv_selected);
        mGVUnSelected = (GVUnSelected) findViewById(R.id.gv_unselected);
        for (String b : a) {
            mTitles.add(b);
            nTitles.add(b);
        }
    }

    private void initDatas() {
        adapterUp = new SimpleAdapter(this, mGVSelected.getData(), R.layout.activity_add_s,
                new String[]{"title"}, new int[]{R.id.bt_add_s});
        mGVSelected.setAdapter(adapterUp);
        adapterDown = new SimpleAdapter(this, mGVUnSelected.getData(), R.layout.activity_add_s,
                new String[]{"title"}, new int[]{R.id.bt_add_s});
        mGVUnSelected.setAdapter(adapterDown);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.ib_close:
                finish();
                overridePendingTransition(R.anim.anim_null, R.anim.anim_out);
                break;
        }
    }

    public void sea() {
        mGVSelected.setTitles(mTitles);
        mGVUnSelected.setTitles(nTitles);
        initDatas();
    }

    public void mThread() {
        threadUp = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = NOGIZAKA;
                handler.sendMessage(message);
            }
        });
        threadDown = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = AKB;
                handler.sendMessage(message);
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)  ) {
            finish();
            overridePendingTransition(R.anim.anim_null, R.anim.anim_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
