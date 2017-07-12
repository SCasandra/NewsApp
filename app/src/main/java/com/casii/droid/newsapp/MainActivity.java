package com.casii.droid.newsapp;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<New>> {
    private TextView textView;
    private ListView listView;
    private ProgressDialog progress;
    private Adapter adapter;
    private String URL = "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=";
    private static final String API_KEY = "82471b02-72c2-40a1-a25d-4f15831fe3a1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.message);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                New clickedNew = (New) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedNew.getWebUrl()));
                startActivity(intent);
            }
        });
        if (isConnectedToInternet()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
            showProgressBar();
        }
    }

    private void updateUI(List<New> news) {
        adapter = new Adapter(this, news);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
    }

    private void showMessage(String message) {
        textView.setText(message);
        listView.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progress = new ProgressDialog(MainActivity.this, R.style.DialogStyle);
        progress.setMessage(getString(R.string.loading_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo nInfo = connectivity.getActiveNetworkInfo();
            if (nInfo != null && nInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Loader<List<New>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, URL + API_KEY);
    }

    @Override
    public void onLoadFinished(Loader<List<New>> loader, List<New> news) {
        if (news == null) {
            progress.dismiss();
            return;
        }
        if (news.size() == 0) {
            showMessage(getString(R.string.no_results));
            progress.dismiss();
            return;
        }
        showMessage("");
        listView.setVisibility(View.VISIBLE);
        updateUI(news);
        try {
            progress.dismiss();
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void onLoaderReset(Loader<List<New>> loader) {
        adapter.clear();
    }
}
