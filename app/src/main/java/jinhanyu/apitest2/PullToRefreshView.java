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
 * Created by anzhuo on 2016/9/2.
 */
public class PullToRefreshView extends FrameLayout implements PtrUIHandler {
    private ImageView iv_iamge;
    private TextView tv_arrow;
    AnimationDrawable drawable;


    public PullToRefreshView(Context context) {
        this(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_reflesh_head, this);
        iv_iamge = (ImageView) view.findViewById(R.id.iv_image);
        tv_arrow = (TextView) view.findViewById(R.id.tv_arrow);
        drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.arrow);


    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        tv_arrow.setText("下拉刷新");
        iv_iamge.setImageResource(R.drawable.z_arrow_down);

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        tv_arrow.setText("松开刷新");
        iv_iamge.setImageResource(R.drawable.z_arrow_down);

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tv_arrow.setText("正在加载");
        iv_iamge.setImageDrawable(drawable);
        drawable.start();

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

        tv_arrow.setText("加载完成");
        drawable.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        int moff = frame.getOffsetToRefresh();
        int currpos = ptrIndicator.getCurrentPosY();
        int lastPos=ptrIndicator.getLastPosY();
        if (currpos<moff&&lastPos>=moff) {
            if (currpos < moff && lastPos == PtrFrameLayout.PTR_STATUS_PREPARE) {
                tv_arrow.setText("下拉刷新");
                iv_iamge.setImageResource(R.drawable.z_arrow_down);
            }
        }
            else if (currpos>moff&&lastPos<=moff){
                tv_arrow.setText("释放刷新");
                iv_iamge.setImageResource(R.drawable.z_arrow_up);
            }



    }
}
