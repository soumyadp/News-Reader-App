package com.example.android.newsappbysoumyadeep;

/**
 * Created by Soumya on 12-07-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private List<News> mNewsList;
    private OnItemClickListener mListener;
    private Context mContext;

    public NewsAdapter(List<News> newsList, OnItemClickListener listener){
        mNewsList = newsList;
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View newsView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(newsView);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder viewHolder, int position) {
        News news = mNewsList.get(position);
        viewHolder.mTitle.setText(news.getTitle());
        viewHolder.mDate.setText(news.getDate());
        viewHolder.mSection.setText(news.getSection());
        viewHolder.bind(mNewsList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView mTitle;
        @BindView(R.id.date) TextView mDate;
        @BindView(R.id.section) TextView mSection;

        public NewsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(final News news, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(news);
                }
            });
        }
    }

    public void clear(){
        mNewsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<News> newsList){
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }
}