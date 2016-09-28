package jinhanyu.apitest2.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anzhuo on 2016/9/23.
 */

public class GVUnSelected extends GridView {
    private List<String> mTitles;
    private List<Map<String, Object>> dataList;

    public GVUnSelected(Context context) {
        super(context, null);
    }

    public GVUnSelected(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //接收titles
    public void setTitles(List<String> titles) {
        if (titles != null && titles.size() > 0) {
            mTitles = titles;
        }
    }

    public List<Map<String, Object>> getData() {
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mTitles.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", mTitles.get(i));
            dataList.add(map);
        }
        return dataList;
    }
}
