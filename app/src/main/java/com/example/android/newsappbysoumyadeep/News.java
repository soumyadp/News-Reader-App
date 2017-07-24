package com.example.android.newsappbysoumyadeep;

/**
 * Created by Soumya on 12/07/2017.
 */

public class News {
    private String mTitle;
    private String mDate;
    private String mSection;
    private String mUrl;

    public News(String title, String date, String section, String url) {
        this.mTitle = title;
        this.mDate = date;
        this.mSection = section;
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }
}
