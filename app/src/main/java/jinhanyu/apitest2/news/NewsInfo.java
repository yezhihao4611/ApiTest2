package jinhanyu.apitest2.news;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/8 0008.
 */
public class NewsInfo {
    private String title;
    private String imageUrl;
    private String newsUrl;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




    public String timePast(String time) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = date.getTime();
        if (System.currentTimeMillis() - l > 24 * 60 * 60 * 1000) {
            return (System.currentTimeMillis()) / 24 / 60 / 60 / 1000 + "天前";
        } else if (System.currentTimeMillis() - l > 60 * 60 * 1000) {
            return (System.currentTimeMillis()) / 60 / 60 / 1000 + "小时前";
        } else if (System.currentTimeMillis() - l > 60 * 1000) {
            return (System.currentTimeMillis()) / 60 / 1000 + "分钟前";
        } else {
            return (System.currentTimeMillis() - l) / 1000 + "秒前";
        }
    }
}
