package jinhanyu.apitest2.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    //ViewPager动画
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // 图片向左移出屏幕
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            // 图片向左移动
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) { // (0,1]
            // 渐隐
//            view.setAlpha(1 - 0.5f*position);
            // 右边图片移入屏幕
            view.setTranslationX(0.8f * pageWidth * -position);

        } else { // (1,+Infinity]
            // 图片向右移出屏幕
            view.setAlpha(0);

        }
    }
}
