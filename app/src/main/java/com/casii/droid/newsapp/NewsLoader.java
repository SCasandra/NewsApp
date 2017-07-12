package com.casii.droid.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casi on 12.07.2017.
 */

public class NewsLoader extends AsyncTaskLoader {
    private String stringUrl;

    public NewsLoader(Context context, String stringUrl) {
        super(context);
        this.stringUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        if (stringUrl == null) {
            return null;
        }
        URL url = createUrl(stringUrl);
        String jsonResponse;
        jsonResponse = makeHttpRequest(url);
        List<New> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        return url;
    }

    private String makeHttpRequest(URL url) {
        String jsonResponse = "";
        HttpURLConnection urlConnection;
        InputStream inputStream;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        String out = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                out = out + line;
            }
        } catch (IOException e) {
        }
        return out;
    }

    private List<New> extractFeatureFromJson(String newsJSON) {
        List<New> news = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(newsJSON);
            String section = "";
            String title = "";
            String webUrl = "";
            if (root.has("response")) {
                JSONObject response = root.getJSONObject("response");

                if (response.has("results")) {
                    JSONArray results = response.getJSONArray("results");
                    if (results.length() > 0) {
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            if (item.has("sectionName")) {
                                section = item.getString("sectionName");
                            }
                            if (item.has("webTitle")) {
                                title = item.getString("webTitle");
                            }
                            if (item.has("webUrl")) {
                                webUrl = item.getString("webUrl");
                            }
                            news.add(new New(title, section, webUrl));
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }
}
