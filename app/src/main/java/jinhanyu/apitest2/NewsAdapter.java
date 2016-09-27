package jinhanyu.apitest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class NewsAdapter extends BaseAdapter {
    NewsInfo newsInfo;
    List<NewsInfo> list;
    Context context;

    public NewsAdapter(Context context, List<NewsInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_news, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.iv_titleImage = (ImageView) view.findViewById(R.id.iv_titleImage);
            viewHolder.tv_newsUrl = (TextView) view.findViewById(R.id.tv_newsUrl);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        newsInfo = list.get(i);
        viewHolder.tv_title.setText(newsInfo.getTitle());
        Picasso.with(context).load(newsInfo.getImageUrl()).into(viewHolder.iv_titleImage);
        viewHolder.tv_newsUrl.setText(newsInfo.getNewsUrl());

        return view;
    }

    class ViewHolder {
        private TextView tv_title;
        private TextView tv_newsUrl;
        private ImageView iv_titleImage;
    }
}
