package jinhanyu.apitest2;

import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liuguangqiang.swipeback.SwipeBackLayout;

import static jinhanyu.apitest2.R.id.swipeBackLayout;

/**
 * Created by Administrator on 2016/9/22 0022.
 */

public class SwipeBackActivityWithoutPullToBack extends ActionBarActivity implements SwipeBackLayoutWithoutPullToBack.SwipeBackListener  {
    private SwipeBackLayoutWithoutPullToBack swipeBackLayoutWithoutPullToBack;
    private ImageView ivShadow;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getContainer());
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        swipeBackLayoutWithoutPullToBack.addView(view);
    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayoutWithoutPullToBack = new SwipeBackLayoutWithoutPullToBack(this);
        swipeBackLayoutWithoutPullToBack.setOnSwipeBackListener(this);
        ivShadow = new ImageView(this);
        ivShadow.setBackgroundColor(getResources().getColor(com.liuguangqiang.swipeback.R.color.black_p50));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        container.addView(ivShadow, params);
        container.addView(swipeBackLayoutWithoutPullToBack);
        return container;
    }

    public void setDragEdge(SwipeBackLayoutWithoutPullToBack.DragEdge dragEdge) {
        swipeBackLayoutWithoutPullToBack.setDragEdge(dragEdge);
    }

    public SwipeBackLayoutWithoutPullToBack getSwipeBackLayoutWithoutPullToBack() {
        return swipeBackLayoutWithoutPullToBack;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
        ivShadow.setAlpha(1 - fractionScreen);
    }

}
