package jinhanyu.apitest2.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by anzhuo on 2016/9/20.
 */

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;//画笔
    private Path mPath;
    private int mTriangleWidth;//三角形宽度
    private int mTriangleHeight;//三角形高度
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;//占屏比
    private int mInitTranslationX;//初始化时的三角形偏移量
    private int mTranslationX;//滑动时三角形的偏移量
    private int mTabVisibleCount=5;
    private List<String> mTitles;
    private static final int tabWidth = 192;
    private static final int TITLE_TEXT_SIZE = 16;
    private static final int AFTER_TITLE_TEXT_SIZE = 18;
    private static final int COLOR_TEXT_NORMAL = 0x77ffffff;
    private static final int COLOR_TEXT_HIGHLIGHT = 0xffffffff;
    private ViewPager mViewPager;

    public interface PageOnChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public PageOnChangeListener mListener;

    public void setOnPageChangeListener(PageOnChangeListener listener) {
        this.mListener = listener;
    }


    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 初始化画笔
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override//设置三角形大小
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (tabWidth * RADIO_TRIANGLE_WIDTH);
        mInitTranslationX = tabWidth / 2 - mTriangleWidth / 2;
        initTriangle();
    }

    @Override//接在获取可见TAB数量后，设置子控件宽度
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0) return;
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.width = tabWidth;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mTriangleHeight = mTriangleWidth / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    /**
     * 指示器根据情况滚动
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        mTranslationX = (int) (tabWidth * (offset + position));
        //容器移动，当Tab处于移动至最后一个时
        if (position >= (mTabVisibleCount - 1) && offset > 0 && getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo(
                        (position - (mTabVisibleCount - 1)) * tabWidth + (int) (tabWidth * offset),
                        0);
            } else {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }

    public void setTabItemTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            this.removeAllViews();
            mTitles = titles;
            for (String title : mTitles) {
                addView(generateTextView(title));
            }
            setItemClickEvent();
        }
    }

    /**
     * 根据title创建tab
     * 动态生成
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = tabWidth;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 设置关联的ViewPager
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, final int pos) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
                hightLightTextView(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabWidth*positionOffset+position*tabWidth
                scroll(position, positionOffset);
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        hightLightTextView(pos);
    }

    /**
     * 重置tab文本颜色
     */
    public void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
                ((TextView) view).setTextSize(TITLE_TEXT_SIZE);
            }
        }
    }

    /**
     * 高亮某tab的文本
     *
     * @param pos
     */
    public void hightLightTextView(int pos) {
        resetTextViewColor();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
            ((TextView) view).setTextSize(AFTER_TITLE_TEXT_SIZE);
        }
    }

    /**
     * 设置tab的点击事件
     */
    public void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
