package jinhanyu.apitest2;

import java.util.ArrayList;



import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class NewsAdapter2 extends BaseAdapter{
	  ImageView leftImg;
	  ImageView rightImg;
	  mPagerAdapter mPageAdaper;
	  private ArrayList<ImageView> mNewsImages;  //上方viewpager的图片
	  private ViewPager mNewsViewPager;
	  Context mContext;
	  private View mTopView;
	  
	     
	public NewsAdapter2(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mPageAdaper = new mPagerAdapter();
		mNewsImages = new ArrayList<ImageView>();
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		
		if (position == 0) 
			return 0;
		else
			return position-1;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return position > 0 ? 0 : 1;
		
	}
 

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(position == 0)
		{	
	        return getTopView(convertView);   
		}		
		else 
			{
			
			   View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, null);

			   return view;
		
			}
	}
	
	private View getTopView(View convertView) {
		
		if(mTopView==null){
		mTopView = LayoutInflater.from(mContext).inflate(R.layout.item_picnews, null);

		LayoutParams  lp=new LayoutParams(LayoutParams.MATCH_PARENT,300);
		 
		 lp.gravity = Gravity.TOP;
		// 页码指示点
		// 图片切换控件
		mNewsViewPager = (ViewPager) mTopView.findViewById(R.id.vp_imageNews);
		mNewsViewPager.setLayoutParams(lp);
	
			 leftImg = new ImageView(mContext);

				
				leftImg.setBackgroundColor(Color.WHITE);
				
				leftImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
				leftImg.setImageResource(R.drawable.img2);
				
			//	photoFront.setOnClickListener(new MyOnClickListener(0));

				 rightImg = new ImageView(mContext);
				
				rightImg.setBackgroundColor(Color.WHITE);
				
			    rightImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
			    rightImg.setImageResource(R.drawable.img1);
			    

			 //   videoFront.setOnClickListener(new MyOnClickListener(1));
			    
				mNewsImages.add(leftImg);
				mNewsImages.add(rightImg);
				mNewsViewPager.setAdapter(mPageAdaper);
				mNewsViewPager.setCurrentItem(0);
				OnPageChangeListener pageChangeListener =new OnPageChangeListener() {

					// 页面选择

					@Override
					public void onPageSelected(int position) {
						//draw_Point(position);
					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}

					@Override
					public void onPageScrolled(int position,

					float positionOffset, int positionOffsetPixels) {

					}

				};
				mNewsViewPager.setOnPageChangeListener(pageChangeListener);
		}
		
		return mTopView;
	}
	
	public class mPagerAdapter extends PagerAdapter {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;

		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
	//		return super.getItemPosition(object);
			  return POSITION_NONE;   
		}
		@Override
		public int getCount() {

			return mNewsImages.size();

		}


        @Override 
        public Parcelable saveState() { 
            return null; 
        } 
 
        @Override 
        public void restoreState(Parcelable arg0, ClassLoader arg1) { 
        } 
 
        @Override 
        public void startUpdate(View arg0) { 
        } 
 
        @Override 
        public void finishUpdate(View arg0) { 
        } 
        
		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container).removeView(mNewsImages.get(position));

		}

		@Override
		public Object instantiateItem(View container, int position) {
		
			((ViewPager) container).addView(mNewsImages.get(position));

			return mNewsImages.get(position);

		}

	};

}
