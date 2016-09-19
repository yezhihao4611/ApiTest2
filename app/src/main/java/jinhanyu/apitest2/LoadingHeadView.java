package jinhanyu.apitest2;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class LoadingHeadView extends FrameLayout implements PtrUIHandler {
    private ImageView iv_loadIcon;
    private TextView tv_loadText;
    AnimationDrawable drawable;

    public LoadingHeadView(Context context) {
        this(context, null);
    }

    public LoadingHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ultra_pull_to_refresh_head, this);
        iv_loadIcon = (ImageView) view.findViewById(R.id.iv_loadIcon);
        tv_loadText = (TextView) view.findViewById(R.id.tv_loadText);
        drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.loading_anim);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        tv_loadText.setText("下拉刷新");
        iv_loadIcon.setImageResource(R.drawable.tableview_pull_refresh_arrow_down);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        tv_loadText.setText("下拉刷新");
        iv_loadIcon.setImageResource(R.drawable.tableview_pull_refresh_arrow_down);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tv_loadText.setText("正在加载");
        iv_loadIcon.setImageDrawable(drawable);
        drawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        tv_loadText.setText("加载完成");
        drawable.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        int moff = frame.getOffsetToRefresh();
        int currPos = ptrIndicator.getCurrentPosY();
        int lastPos = ptrIndicator.getLastPosY();

        if (currPos < moff && lastPos >= moff) {
            if (currPos < moff && lastPos == PtrFrameLayout.PTR_STATUS_PREPARE) {
                tv_loadText.setText("下拉刷新");
                iv_loadIcon.setImageResource(R.drawable.tableview_pull_refresh_arrow_down);
            }
        } else if (currPos > moff && lastPos <= moff) {
            tv_loadText.setText("释放刷新");
            iv_loadIcon.setImageResource(R.drawable.tableview_pull_refresh_arrow_up);
        }
    }
}
