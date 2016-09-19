package jinhanyu.apitest2.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<String> images_url;
    private Context context;
    Bitmap bitmap;
    ImageView imageView;

    public ViewPagerAdapter(Context context, ArrayList<String> image_url) {
        this.context = context;
        this.images_url = image_url;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mGifView = new ImageView(context);
        try {
            bitmap=getBitmap(images_url.get(position));
            mGifView.setImageBitmap(bitmap);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        container.addView(mGifView,0);
        return mGifView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images_url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Bitmap getBitmap(String filePath){
        FileInputStream fs = null;

        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPurgeable = true;
            opt.inInputShareable = true;

            fs = new FileInputStream(filePath);

            return BitmapFactory.decodeFileDescriptor(fs.getFD(), null, opt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
