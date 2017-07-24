package com.example.android.newsappbysoumyadeep;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<News>> {

    private String mQuery;
    private NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //ButterKnife library import sourced from the internet
    @BindView(R.id.progress_indicator)
    View mProgressIndicator;
    @BindView(R.id.news_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_text_view)
    TextView mEmptyTextView;

    private static final int NEWS_LOADER_ID = 1;
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ButterKnife.bind(this);

        //RecyclerView and LayoutManager implemented from different source
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsAdapter(new ArrayList<News>(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                String url = news.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "News App activity Loader Manager called here");

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            Log.i(LOG_TAG, "News App activity initLoader called here");
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            Log.i(LOG_TAG, "News App activity NO WIFI called here called here");
            View loadingIndicator = findViewById(R.id.progress_indicator);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            Log.i(LOG_TAG, "Update empty state with no connection called here");
            mProgressIndicator.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mQuery = query;
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        return true;
    }

    //Loader methods
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        mProgressIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("api-key", "84aafb93-62d6-4ce3-a4db-ac7b6d4507d1");
            if (mQuery != null)
                uriBuilder.appendQueryParameter("q", mQuery);
            return new NewsLoader(this, uriBuilder.toString());
        } else {
            mProgressIndicator.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.no_internet_connection);
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        mProgressIndicator.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.VISIBLE);
        mEmptyTextView.setText(R.string.no_news);

        mAdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);
            mAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_button_click));
        searchView.setOnQueryTextListener(this);
        return true;
    }
}

