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
    int TYPE_0 = 0;
    int TYPE_1 = 1;

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
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_0;
        } else
            return TYPE_1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder_0 viewHolder_0 = null;
        ViewHolder_1 viewHolder_1 = null;

        if (view == null || view.getTag() == null) {
            switch (getItemViewType(i)) {
                case 0:
                    view = LayoutInflater.from(context).inflate(R.layout.item_news, null);
                    viewHolder_0 = new ViewHolder_0();
                    viewHolder_0.tv_title_0 = (TextView) view.findViewById(R.id.tv_title);
                    viewHolder_0.iv_titleImage_0 = (ImageView) view.findViewById(R.id.iv_titleImage);
                    viewHolder_0.tv_newsUrl_0 = (TextView) view.findViewById(R.id.tv_newsUrl);
                    view.setTag(viewHolder_0);
                    break;
                case 1:
                    view = LayoutInflater.from(context).inflate(R.layout.item_news, null);
                    viewHolder_1 = new ViewHolder_1();
                    viewHolder_1.tv_title_1 = (TextView) view.findViewById(R.id.tv_title);
                    viewHolder_1.iv_titleImage_1 = (ImageView) view.findViewById(R.id.iv_titleImage);
                    viewHolder_1.tv_newsUrl_1 = (TextView) view.findViewById(R.id.tv_newsUrl);
                    view.setTag(viewHolder_1);
                    break;
            }
        }
        switch (getItemViewType(i)) {
            case 0:
                viewHolder_0 = (ViewHolder_0) view.getTag();
                newsInfo = list.get(i);
                viewHolder_0.tv_title_0.setText(newsInfo.getTitle());
                Picasso.with(context).load(newsInfo.getImageUrl()).into(viewHolder_0.iv_titleImage_0);
                viewHolder_0.tv_newsUrl_0.setText(newsInfo.getNewsUrl());
                break;
            case 1:
                viewHolder_1 = (ViewHolder_1) view.getTag();
                newsInfo = list.get(i);
                viewHolder_1.tv_title_1.setText(newsInfo.getTitle());
                Picasso.with(context).load(newsInfo.getImageUrl()).into(viewHolder_1.iv_titleImage_1);
                viewHolder_1.tv_newsUrl_1.setText(newsInfo.getNewsUrl());
                break;
        }

        return view;
    }

    class ViewHolder_0 {
        private TextView tv_title_0;
        private TextView tv_newsUrl_0;
        private ImageView iv_titleImage_0;
    }

    class ViewHolder_1 {
        private TextView tv_title_1;
        private TextView tv_newsUrl_1;
        private ImageView iv_titleImage_1;
    }
}
