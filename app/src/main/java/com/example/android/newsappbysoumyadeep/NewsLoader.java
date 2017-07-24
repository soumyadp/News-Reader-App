package com.example.android.newsappbysoumyadeep;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Soumya on 12-07-2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl.length() < 1 || mUrl == null) {
            return null;
        }
        List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }
}
